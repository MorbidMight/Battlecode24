package Version12;

import battlecode.common.*;
import battlecode.world.Trap;

import java.util.ArrayList;
import java.util.Arrays;

import static Version12.RobotPlayer.*;
import static Version12.Utilities.averageRobotLocation;
import static Version12.Utilities.bestHeal;

enum states{
    defense, attack, heist, escort, flagCarrier
}
public class Soldier
{
    //might be better to switching to using more variables up here, can be accessed/changed across various states - also persist across turns
    static RobotInfo[] enemyRobots;
    static RobotInfo[] enemyRobotsAttackRange;
    static RobotInfo[] allyRobots;
    static RobotInfo[] allyRobotsHealRange;
    //looks at enemies in a radius of sqrt(10), the radius from which any enemy can move and then attack you in the same turn
    static RobotInfo[] potentialEnemiesAttackRange;
    static FlagInfo[] nearbyFlagsAlly;
    static FlagInfo[] nearbyFlagsEnemy;
    static RobotInfo escortee;
    static RobotInfo lastSeenEnemy;
    final static float STOLEN_FLAG_CONSTANT = 2.5f;
    //records the enemies seen last round, to create the stunlist
    static ArrayList<RobotInfo> seenLast = new ArrayList<>();
    //keeps track of enemies we can see this round that we could see last round, which haven't moved
    static ArrayList<RobotInfo> stunList = new ArrayList<>();



    static states state;
    public static void runSoldier(RobotController rc) throws GameActionException {
        if (!rc.isSpawned()) {
            lastSeenEnemy = null;
            return;
        }
        tryGetCrumbs(rc);
        updateInfo(rc);
        //createStunList();
        //make sure we store the location of at least one enemy, so we know where one was if we need it next turn and can't see any
        if(enemyRobots.length > 0)
            lastSeenEnemy = enemyRobots[0];
        else if(lastSeenEnemy != null && rc.canSenseRobotAtLocation(lastSeenEnemy.getLocation())){
            lastSeenEnemy = null;
        }
        state = trySwitchState(rc);
        rc.setIndicatorString(state.toString());
        switch (state)
        {
            case defense:
                defense(rc);
                break;
            case attack:
                attack(rc);
                break;
            case heist:
                heist(rc);
                break;
            case escort:
                escort(rc);
                break;
            case flagCarrier:
                Carrier.runCarrier(rc);
                break;
        }
//        updateInfo(rc);
//        seenLast.clear();
//        seenLast.addAll(Arrays.asList(enemyRobots));
    }
    public static void createStunList(){
        stunList.clear();
        for(RobotInfo robot : seenLast){
            for(RobotInfo rob : enemyRobots){
                if(robot.getID() == rob.getID() && robot.getLocation().equals(rob.getLocation())){
                    stunList.add(robot);
                }
            }
        }
    }
    public static void tryGetCrumbs(RobotController rc) throws GameActionException {
        //tries to get neary crumbs
        MapLocation[] nearbyCrumbs = rc.senseNearbyCrumbs(-1);
        MapLocation targetCrumb = null;
        if (nearbyCrumbs.length > 0)
            targetCrumb = Explorer.chooseTargetCrumb(rc, nearbyCrumbs);
        if (targetCrumb != null) {
            MapInfo targetLoc = rc.senseMapInfo(targetCrumb);
            if (rc.canFill(rc.adjacentLocation(rc.getLocation().directionTo(targetCrumb))) && targetLoc.getCrumbs() > 30) {
                rc.fill(rc.adjacentLocation(rc.getLocation().directionTo(targetCrumb)));
            }
            //check if crumb is on water
            if (!targetLoc.isPassable() && rc.canFill(targetCrumb)) {
                rc.fill(targetCrumb);
            }
            Pathfinding.combinedPathfinding(rc, targetCrumb);
        }
    }
    //considers the situation, and decides what state the robot should be - with a bias towards current state,
    // b/c if conditions aren't strong enough to switch just default to current state
    public static states trySwitchState(RobotController rc) throws GameActionException {
        if(state == states.flagCarrier && !rc.hasFlag()){
            state = states.attack;
        }
        if(rc.hasFlag()) {
            return states.flagCarrier;
        }
        for (FlagInfo flag : nearbyFlagsAlly) {
            if (flag.isPickedUp())
                return states.defense;
        }

        for (FlagInfo flag : nearbyFlagsEnemy) {
            checkRecordEnemyFlag(rc, flag);
            if (flag.isPickedUp()) {
                escortee = rc.senseRobotAtLocation(flag.getLocation());
                return states.escort;
            }
        }
        StolenFlag closestFlag = Utilities.getClosestFlag(rc);
        if (closestFlag != null && rc.getLocation().distanceSquaredTo(closestFlag.location) < (rc.getMapWidth() + rc.getMapHeight()) * STOLEN_FLAG_CONSTANT)
        {
            return states.defense;
        }
        if (allyRobots.length >= enemyRobots.length && nearbyFlagsEnemy.length != 0)
            return states.heist;
        //if it seems like the escort or heist is over, return to attack
        if((state == states.escort || state == states.heist) && nearbyFlagsEnemy.length == 0)
            return states.attack;
        //similarly, if we are on defense but the shared array asking for help is clear, seems they no longer need defense
        else if (state == states.defense && Utilities.getClosestFlag(rc) == null){
            return states.attack;
        }
        if(state == null){
            //have our bots start in attack if nothing else compels them on the first turn
            return states.attack;
        }
        return state;
    }

    //updates the info arrays used
    public static void updateInfo(RobotController rc) throws GameActionException {
        //take in important info around you
        enemyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
        enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
        allyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam());
        allyRobotsHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
        potentialEnemiesAttackRange = rc.senseNearbyRobots(10, rc.getTeam().opponent());
        nearbyFlagsAlly = rc.senseNearbyFlags(-1, rc.getTeam());
        nearbyFlagsEnemy = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
    }
    public static void defense(RobotController rc) throws GameActionException {
        StolenFlag closestFlag = Utilities.getClosestFlag(rc);
        attemptAttack(rc);
        if(closestFlag != null){
            if(rc.canSenseLocation(closestFlag.location)){
                if (rc.isActionReady() || ((allyRobots.length - enemyRobots.length > 6) &&  enemyRobotsAttackRange.length == 0)){
                    runMicroAttack(rc);
                    updateInfo(rc);
                    if(enemyRobots.length > 6 && enemyRobotsAttackRange.length > 1){
                        if(rc.canBuild(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
                            rc.build(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
                        }
                    }
                    attemptAttack(rc);
                    attemptHeal(rc);
                }
                else{
                    Pathfinding.combinedPathfinding(rc, closestFlag.location);
                }
//                //try and kite backwards, wait for cooldown to refresh
//                else {
//                    runMicroKite(rc);
//                    updateInfo(rc);
//                    attemptAttack(rc);
//                    attemptHeal(rc);
//                }
            }
            else{
                Pathfinding.combinedPathfinding(rc, closestFlag.location);
            }
        }
        if(rc.isActionReady()){
            updateInfo(rc);
            attemptAttack(rc);
            attemptHeal(rc);
        }
    }
    public static void attack(RobotController rc) throws GameActionException{
        if(rc.isActionReady() && enemyRobotsAttackRange.length == 0 && enemyRobots.length > 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(enemyRobots[0].getLocation()))))
            rc.fill(rc.getLocation().add(rc.getLocation().directionTo(enemyRobots[0].getLocation())));
        // a temporary macro band-aid that calls this more often only to reap the part that removes flags we can see aren't there
        findCoordinatedActualFlag(rc);
        if(enemyRobots.length > 6 && enemyRobotsAttackRange.length > 1){
            TrapType toBeBuilt = TrapType.STUN;
            if(rc.canBuild(toBeBuilt, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
                rc.build(toBeBuilt, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
            }
            else if(rc.canBuild(toBeBuilt, rc.getLocation())){
                rc.build(toBeBuilt, rc.getLocation());
            }
        }
        if(rc.isActionReady()){
            attemptAttack(rc);
        }
        if(enemyRobots.length != 0) {
            //try and move into attack range of any nearby enemies
            if ((rc.isActionReady() || ((allyRobots.length - enemyRobots.length > 6) &&  enemyRobotsAttackRange.length == 0) && rc.getHealth() > 100)){
                runMicroAttack(rc);
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
            //try and kite backwards, wait for cooldown to refresh
            else {
                runMicroKite(rc);
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
        }
        //if we cant see any enemies, run macro not micro - move towards known flags, and if not possible, move towards broadcast flags
        else{
            if(lastSeenEnemy != null){
                //Pathfinding.combinedPathfinding(rc, lastSeenEnemy.getLocation());
                Pathfinding.bellmanFord5x5(rc, lastSeenEnemy.getLocation());
            }
            else if(knowFlag(rc)){
                MapLocation target = findCoordinatedActualFlag(rc);

                if(target != null){
                    if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target)))){
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(target)));
                    }
                    Pathfinding.combinedPathfinding(rc, target);
                }
            }
            else{
                MapLocation target = findCoordinatedBroadcastFlag(rc);
                if(target != null) {
                    if (rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target)))) {
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(target)));
                    }
                    Pathfinding.combinedPathfinding(rc, findCoordinatedBroadcastFlag(rc));
                }
            }
        }
    }

    //heist! we have more or equal allies to enemies in vision, and can see a flag, so we are gonna rush their flag
    public static void heist(RobotController rc) throws GameActionException {
        FlagInfo targetFlag = nearbyFlagsEnemy[0];
        if(rc.canPickupFlag(targetFlag.getLocation())) {
            rc.pickupFlag(targetFlag.getLocation());
            Pathfinding.tryToMove(rc, findClosestSpawnLocation(rc));
            state = states.flagCarrier;
        }
        else {
            if(rc.getLocation().distanceSquaredTo(targetFlag.getLocation()) < 9) {
                if (rc.canMove(rc.getLocation().directionTo(targetFlag.getLocation()))) {
                    rc.move(rc.getLocation().directionTo(targetFlag.getLocation()));
                }
                else{
                    Pathfinding.bellmanFord5x5(rc, targetFlag.getLocation());
                }
            }
            else if(rc.isActionReady()){
                runMicroAttack(rc);
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
            else{
                runMicroKite(rc);
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
        }
        if(rc.hasFlag())
            state = states.flagCarrier;
    }
    public static void escort(RobotController rc) throws GameActionException {
        if(rc.isActionReady()) {
            attemptHealCarrier(rc);
            attemptAttack(rc);
        }
        if(enemyRobots.length != 0){
            //try and move into attack range of any nearby enemies
            if (rc.isActionReady() || ((allyRobots.length - enemyRobots.length > 6) &&  enemyRobotsAttackRange.length == 0)){
                runMicroAttack(rc);
                updateInfo(rc);
                if(enemyRobots.length > 6 && enemyRobotsAttackRange.length > 1){
                    if(rc.canBuild(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
                        rc.build(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
                    }
                }
                attemptAttack(rc);
                attemptHeal(rc);
            }
            //try and kite backwards, wait for cooldown to refresh
            else {
                runMicroKite(rc);
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
        }
        //**CHANGE
        StolenFlag temp = new StolenFlag(escortee.getLocation(), false);
        //**CHANGE ^^^
        Pathfinding.bellmanFordFlag(rc, temp.location, temp);
        if(rc.isActionReady()) {
            updateInfo(rc);
            attemptHealCarrier(rc);
            attemptAttack(rc);
            attemptHeal(rc);
        }
    }
    //find the location that will move us into attack range of the least amount of enemies, while moving us towards at least one
    //move there, and fire
    public static void runMicroAttack(RobotController rc) throws GameActionException {
        engagementMicroSquare[] options = new engagementMicroSquare[8];
        populateMicroArray(rc, options);
        engagementMicroSquare best = null;
        float highScore = Integer.MIN_VALUE;
        for(engagementMicroSquare square : options){
            if(square.passable){
                float score;
                if(square.enemiesAttackRangedX == 1){
                    score = 1000000 + square.enemiesVisiondX + square.alliesVisiondX + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -1;
                }
                else {
                    score = square.enemiesAttackRangedX * 4 + square.enemiesVisiondX * 4 + square.alliesVisiondX * 1.5f + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX;
                }
                if(score > highScore){
                    highScore = score;
                    best = square;
                }
            }
        }
        if(best != null) {
            //make sure the move isnt outright harmful, or too stupid
            if (best.enemiesAttackRangedX >= 0 && best.enemiesAttackRangedX <= 2 && rc.canMove(rc.getLocation().directionTo(best.location))) {
                rc.move(rc.getLocation().directionTo(best.location));
            }
        }
    }
    //find the location that will be in attack range of the least amount of enemies, and move there
    //tiebreaker between multiple locations is location in vision range of least amount of enemies
    public static void runMicroKite(RobotController rc) throws GameActionException {
        engagementMicroSquare[] options = new engagementMicroSquare[8];
        populateMicroArray(rc, options);
        engagementMicroSquare best = null;
        float highScore = Integer.MIN_VALUE;
        for(engagementMicroSquare square : options){
            if(square.passable){
                float score = square.enemiesAttackRangedX * -6 + square.enemiesVisiondX * 0.5f + square.alliesVisiondX + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -3;
                if(score > highScore){
                    highScore = score;
                    best = square;
                }
            }
        }
        if(best != null) {
            if (best.enemiesAttackRangedX <= 0 && rc.canMove(rc.getLocation().directionTo(best.location))) {
                rc.move(rc.getLocation().directionTo(best.location));
            }
        }
    }
    public static void populateMicroArray(RobotController rc, engagementMicroSquare[] options) throws GameActionException {
        int index = 0;
        MapLocation curLoc = rc.getLocation();
        int mapWidth = rc.getMapWidth();
        int mapHeight = rc.getMapHeight();
        int curX = curLoc.x;
        int curY = curLoc.y;
        int newX = 0;
        int newY = 0;
        for (int k = -1; k <= 1; k++) {
//            if (-1 == 0 && k == 0)
//                continue;
            newX = curX + -1;
            newY = curY + k;
            if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
                options[index] = new engagementMicroSquare(newX, newY);
                MapLocation tempSquare = new MapLocation(newX, newY);
                MapInfo tempInfo = rc.senseMapInfo(tempSquare);
                options[index].passable = (tempInfo.isPassable() && rc.senseRobotAtLocation(tempSquare) == null);
                if (options[index].passable) {
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = potentialEnemiesAttackRangeNewLoc.length - potentialEnemiesAttackRange.length;
                }
            } else {
                options[index] = new engagementMicroSquare(false);
            }
            index++;
        }
        for (int k = -1; k <= 1; k++) {
            if (0 == 0 && k == 0)
                continue;
            newX = curX + 0;
            newY = curY + k;
            if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
                options[index] = new engagementMicroSquare(newX, newY);
                MapLocation tempSquare = new MapLocation(newX, newY);
                MapInfo tempInfo = rc.senseMapInfo(tempSquare);
                options[index].passable = (tempInfo.isPassable() && rc.senseRobotAtLocation(tempSquare) == null);
                if (options[index].passable) {
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = potentialEnemiesAttackRangeNewLoc.length - potentialEnemiesAttackRange.length;
                }
            } else {
                options[index] = new engagementMicroSquare(false);
            }
            index++;
        }
        for (int k = -1; k <= 1; k++) {
//            if (1 == 0 && k == 0)
//                continue;
            newX = curX + 1;
            newY = curY + k;
            if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
                options[index] = new engagementMicroSquare(newX, newY);
                MapLocation tempSquare = new MapLocation(newX, newY);
                MapInfo tempInfo = rc.senseMapInfo(tempSquare);
                options[index].passable = (tempInfo.isPassable() && rc.senseRobotAtLocation(tempSquare) == null);
                if (options[index].passable) {
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = potentialEnemiesAttackRangeNewLoc.length - potentialEnemiesAttackRange.length;
                }
            } else {
                options[index] = new engagementMicroSquare(false);
            }
            index++;
        }
    }

    public static void attemptAttack(RobotController rc) throws GameActionException {
        MapLocation toAttack = RobotPlayer.lowestHealth(enemyRobotsAttackRange);
        if (toAttack != null && rc.canAttack(toAttack)) {
            rc.attack(toAttack);
        }
        if(rc.isActionReady()){
            if (toAttack != null && rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        }
    }

    //attempts to heal lowest health nearby ally, but only does so if they are below the given health
    public static void attemptHealConditional(RobotController rc, int maxHealth) throws GameActionException{
        RobotInfo toHeal = bestHeal(rc, allyRobotsHealRange);
        if(toHeal != null && rc.canHeal(toHeal.getLocation()) && toHeal.health < maxHealth)
            rc.heal(toHeal.getLocation());
    }

    public static void attemptHeal(RobotController rc) throws GameActionException{
        RobotInfo toHeal = bestHeal(rc, allyRobotsHealRange);
        if(toHeal != null && rc.canHeal(toHeal.getLocation()))
            rc.heal(toHeal.getLocation());
    }

    //attempts to heal a flag carrier nearby - including moving towards them to get the heal done
    public static void attemptHealCarrier(RobotController rc) throws GameActionException {
        RobotInfo toHeal = bestHeal(rc, allyRobotsHealRange);
        if(toHeal != null && toHeal.hasFlag()){
            if(rc.canHeal(toHeal.getLocation()))
                rc.heal(toHeal.getLocation());
//            else if(rc.getLocation().distanceSquaredTo(toHeal.getLocation()) > GameConstants.HEAL_RADIUS_SQUARED && rc.getLocation().add(rc.getLocation().directionTo(toHeal.getLocation())).distanceSquaredTo(toHeal.getLocation()) <= GameConstants.HEAL_RADIUS_SQUARED){
//                if(rc.canMove(rc.getLocation().directionTo(toHeal.getLocation()))){
//                    rc.move(rc.getLocation().directionTo(toHeal.getLocation()));
//                    if(rc.canHeal(toHeal.getLocation()))
//                        rc.heal(toHeal.getLocation());
//                }
//            }
        }
    }

    public static void checkRecordEnemyFlag(RobotController rc, FlagInfo flag) throws GameActionException {
        int index3 = rc.readSharedArray(3);
        int index4 = rc.readSharedArray(4);
        int index5 = rc.readSharedArray(5);
        if(index3 != 0 && index4 != 0 && index5 != 0)
            return;
        MapLocation flagLoc = flag.getLocation();
        MapInfo flagLocInfo;
        if(rc.canSenseLocation(flagLoc)){
            flagLocInfo = rc.senseMapInfo(flagLoc);
        }
        else{
            return;
        }
        if(flagLocInfo.getSpawnZoneTeamObject() != rc.getTeam().opponent())
            return;
        int flagLocInt = Utilities.convertLocationToInt(flagLoc);

        MapLocation enemyFlag1 = Utilities.convertIntToLocation(index3);
        MapLocation enemyFlag2 = Utilities.convertIntToLocation(index4);
        MapLocation enemyFlag3 = Utilities.convertIntToLocation(index5);
        if(!enemyFlag1.equals(flagLoc) && !enemyFlag1.isAdjacentTo(flagLoc) && !enemyFlag2.equals(flagLoc) && !enemyFlag2.isAdjacentTo(flagLoc) && !enemyFlag3.equals(flagLoc) && !enemyFlag3.isAdjacentTo(flagLoc)){
            if(index3 == 0){
                rc.writeSharedArray(3, flagLocInt);
            }
            else if(index4 == 0){
                rc.writeSharedArray(4, flagLocInt);
            }
            else if(index5 == 0){
                rc.writeSharedArray(5, flagLocInt);
            }
        }
    }

    //erase an enemy flag from the array if we see the location and it isnt there
    public static void eraseEnemyFlag(RobotController rc, MapLocation m) throws GameActionException {
        int flagLocInt = Utilities.convertLocationToInt(m);
        int index3 = rc.readSharedArray(3);
        int index4 = rc.readSharedArray(4);
        int index5 = rc.readSharedArray(5);
        if(index3 == flagLocInt){
            rc.writeSharedArray(3, 0);
        }
        else if(index4 == flagLocInt){
            rc.writeSharedArray(4, 0);
        }
        else if(index5 == flagLocInt){
            rc.writeSharedArray(5, 0);
        }
    }

    //returns false if we dont know any flag locations, true otherwise
    public static boolean knowFlag(RobotController rc) throws GameActionException {
        int index3 = rc.readSharedArray(3);
        int index4 = rc.readSharedArray(4);
        int index5 = rc.readSharedArray(5);
        return index3 != 0 || index4 != 0 || index5 != 0;
    }

    //findCoordinatedActualFlag - returns lowest index flag that we know location of
    public static MapLocation findCoordinatedActualFlag(RobotController rc) throws GameActionException {
        int index3 = rc.readSharedArray(3);
        int index4 = rc.readSharedArray(4);
        int index5 = rc.readSharedArray(5);
        MapLocation enemyFlag1 = Utilities.convertIntToLocation(index3);
        MapLocation enemyFlag2 = Utilities.convertIntToLocation(index4);
        MapLocation enemyFlag3 = Utilities.convertIntToLocation(index5);
        MapLocation nullChecker = new MapLocation(0,0);
        if(index3 != 0){
            //we can see it, but couldnt sense any flags earlier... its been removed
            if(rc.canSenseLocation(enemyFlag1)) {
                eraseEnemyFlag(rc, enemyFlag1);
            }
            else{
                return enemyFlag1;
            }
        }
        else if(index4 != 0){
            //we can see it, but couldnt sense any flags earlier... its been removed
            if(rc.canSenseLocation(enemyFlag2)) {
                eraseEnemyFlag(rc, enemyFlag2);
            }
            else{
                return enemyFlag2;
            }
        }
        else if(index5 != 0){
            //we can see it, but couldnt sense any flags earlier... its been removed
            if(rc.canSenseLocation(enemyFlag3)) {
                eraseEnemyFlag(rc, enemyFlag3);
            }
            else{
                return enemyFlag3;
            }
        }
        return null;
    }


}

//used to keep track of the eight squares around a robot during engagement, only considers relevant details
//note - passability only takes into account if this robot can move there, so a square can be impassable due
//to either terrain, or just a robot being there, makes no difference to us - false passability could also indicate that
//the location is not on the map
class engagementMicroSquare{
    public MapLocation location;
    public boolean passable;
    public int enemiesAttackRangedX;
    public int enemiesVisiondX;
    public int alliesVisiondX;
    public int alliesHealRangedX;
    public int potentialEnemiesAttackRangedX;
    public engagementMicroSquare(){

    }
    public engagementMicroSquare(boolean p){
        passable = p;
    }
    public engagementMicroSquare(int x, int y){
        location = new MapLocation(x, y);
    }
    public engagementMicroSquare(boolean passability, int x, int y){
        passable = passability;
        this.enemiesAttackRangedX = 0;
        this.enemiesVisiondX = 0;
        location = new MapLocation(x, y);

    }
//    public engagementMicroSquare(boolean passabillity, int enemiesAttackRangedX, int enemiesVisiondX, int x, int y){
//        passable = passabillity;
//        this.enemiesAttackRangedX = enemiesAttackRangedX;
//        this.enemiesVisiondX = enemiesVisiondX;
//        this.x = x;
//        this.y = y;
//    }

}

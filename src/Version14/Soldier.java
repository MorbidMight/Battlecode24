package Version14;

import battlecode.common.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import static Version14.RobotPlayer.*;
import static Version14.Utilities.averageRobotLocation;
import static Version14.Utilities.bestHeal;

enum states{
    defense, attack, heist, escort, flagCarrier, explore
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
    //looks at enemies in a radius of sqrt(16), the enemies that we could attack in two moves
    static RobotInfo[] potentialEnemiesPrepareAttack;
    static FlagInfo[] nearbyFlagsAlly;
    static FlagInfo[] nearbyFlagsEnemy;
    static RobotInfo escortee;
    static RobotInfo lastSeenEnemy;
    //higher number -> less likely to retreat
    final static float RETREAT_CONSTANT = 2.7f;
    //higher number -> number of robots mean less, and health means more
    final static float POWER_CONSTANT = 2.1f;
    //higher number ->  proximity means less
    final static float POWER_PROXIMITY_CONSTANT = 3.5f;
    final static float maxPower = ((69 * GameConstants.DEFAULT_HEALTH) + ((13 * GameConstants.DEFAULT_HEALTH) / POWER_PROXIMITY_CONSTANT)) * (69 / POWER_CONSTANT);
    final static float STOLEN_FLAG_CONSTANT = 2.7f;
    //keeps track of enemies we can see this round that we could see last round, which haven't moved
    static MapLocation exploreTarget;
    static int turnsSinceGen;
    static HashSet<MapLocation> invalidBroadcasts = new HashSet<>();



    static states state;
    public static void runSoldier(RobotController rc) throws GameActionException {
        if (!rc.isSpawned()) {
            lastSeenEnemy = null;
            return;
        }
        if(rc.getRoundNum() % 100 == 0){
            invalidBroadcasts.clear();
        }
        updateInfo(rc);
        //used to update which flags we know are real or not
        findCoordinatedActualFlag(rc);
        tryGetCrumbs(rc);
        for(FlagInfo flag : nearbyFlagsEnemy){
            checkRecordEnemyFlag(rc, flag);
        }
        //createStunList();
        //make sure we store the location of at least one enemy, so we know where one was if we need it next turn and can't see any
        if(enemyRobots.length > 0)
            lastSeenEnemy = enemyRobots[0];
        else if(lastSeenEnemy != null && rc.canSenseRobotAtLocation(lastSeenEnemy.getLocation())){
            lastSeenEnemy = null;
        }
        if(nearbyFlagsEnemy.length != 0 && rc.canPickupFlag(nearbyFlagsEnemy[0].getLocation())){
            rc.pickupFlag(nearbyFlagsEnemy[0].getLocation());
            state = states.flagCarrier;
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
            case explore:
                explore(rc);
                break;
        }
//        updateInfo(rc);
//        seenLast.clear();
//        seenLast.addAll(Arrays.asList(enemyRobots));
    }
    //just try to find an enemy
    public static void explore(RobotController rc) throws GameActionException {
        attemptHeal(rc);
        if(exploreTarget == null) {
            if (Utilities.newGetClosestEnemy(rc) != null) {
                exploreTarget = Utilities.newGetClosestEnemy(rc);
                turnsSinceGen = 0;
            }
            else {
                exploreTarget = generateTargetLoc(rc);
                turnsSinceGen = 1;
            }
        }
        if(turnsSinceGen != 0)
            turnsSinceGen++;
        if(turnsSinceGen == 20)
            exploreTarget = generateTargetLoc(rc);
        Pathfinding.combinedPathfinding(rc, exploreTarget);
        updateInfo(rc);
        attemptHeal(rc);
        if(enemyRobots.length != 0)
            state = states.attack;
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
        if(state == states.explore && enemyRobots.length != 0){
            state = states.attack;
        }
        if(state == states.flagCarrier && !rc.hasFlag()){
            state = states.attack;
        }
        if(rc.hasFlag()) {
            return states.flagCarrier;
        }
        for (FlagInfo flag : nearbyFlagsAlly) {
            if (flag.isPickedUp() || (rc.canSenseLocation(flag.getLocation()) && rc.senseMapInfo(flag.getLocation()).getSpawnZoneTeamObject() != rc.getTeam()))
                return states.defense;
        }

        for (FlagInfo flag : nearbyFlagsEnemy) {
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
        if(state == states.heist && (enemyRobots.length + 1 > allyRobots.length || enemyRobots.length <= 1))
            return states.attack;
        if ((allyRobots.length >= (enemyRobots.length + 1) || enemyRobots.length <= 1) && nearbyFlagsEnemy.length != 0)
            return states.heist;
//        if(state == states.heist && calculatePowerIndexQuotient(rc) > 1.3f)
//            return states.attack;
//        if(calculatePowerIndexQuotient(rc) < 1.2f && nearbyFlagsEnemy.length != 0)
//            return states.heist;
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
        potentialEnemiesPrepareAttack = rc.senseNearbyRobots(16, rc.getTeam().opponent());
        nearbyFlagsAlly = rc.senseNearbyFlags(-1, rc.getTeam());
        nearbyFlagsEnemy = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
    }
    public static void defense(RobotController rc) throws GameActionException {
        StolenFlag closestFlag = Utilities.getClosestFlag(rc);
        attemptAttack(rc);
        if(closestFlag != null){
            if(rc.canSenseLocation(closestFlag.location)){
                Pathfinding.bellmanFord5x5(rc, closestFlag.location);
                if(rc.isActionReady()) {
                    updateInfo(rc);
                    attemptAttack(rc);
                    attemptHeal(rc);
                }
//                if (rc.isActionReady()){
//                    runMicroAttack(rc);
//                    updateInfo(rc);
//                    if(enemyRobots.length > 6 && enemyRobotsAttackRange.length > 1){
//                        if(rc.canBuild(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
//                            rc.build(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
//                        }
//                    }
//                    attemptAttack(rc);
//                    attemptHeal(rc);
//                }
//                else{
//                    if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(closestFlag.location)))){
//                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(closestFlag.location)));
//                    }
//                    Pathfinding.bellmanFord5x5(rc, closestFlag.location);
//                }
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
        if(enemyRobots.length > 6 && enemyRobotsAttackRange.length == 0){
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
            //only kite for the first five rounds following the dam break
            if(rc.getRoundNum() >= 200 && rc.getRoundNum() <= 205 && enemyRobots.length > 2){
                runMicroKite(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
            else if(totalHealth(enemyRobots) / (totalHealth(allyRobots) + rc.getHealth()) > 2/* || rc.getHealth() < 150*/){
                retreat(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
//            else if(calculatePowerIndexEnemies(rc) / calculatePowerIndexAllies(rc) > RETREAT_CONSTANT){
//                System.out.println(rc.getLocation() + " : " + calculatePowerIndexEnemies(rc) / calculatePowerIndexAllies(rc));
//                retreat(rc);
//                attemptAttack(rc);
//                attemptHeal(rc);
//            }
            //try and move into attack range of any nearby enemies
            else if ((rc.isActionReady() || (allyRobots.length + 1 - enemyRobots.length > 6 && enemyRobotsAttackRange.length == 0)) && rc.getHealth() > 150){
                runMicroAttack(rc);
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
//            else if(rc.getActionCooldownTurns() >= 10 && rc.getActionCooldownTurns()<= 19 && rc.getHealth() > 150){
//                runMicroPrepare(rc);
//                updateInfo(rc);
//                attemptAttack(rc);
//                if(potentialEnemiesAttackRange.length == 0)
//                    attemptHeal(rc);
//            }
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
            if(rc.isActionReady()) attemptHeal(rc);
            MapLocation target = null;
            if(lastSeenEnemy != null){
                //Pathfinding.combinedPathfinding(rc, lastSeenEnemy.getLocation());
                Pathfinding.bellmanFord5x5(rc, lastSeenEnemy.getLocation());
            }
            else if(knowFlag(rc)){
                //MapLocation target = findCoordinatedActualFlag(rc);
                target = findClosestActualFlag(rc);
                if(target != null){
                    if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target)))){
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(target)));
                    }
                    Pathfinding.combinedPathfinding(rc, target);
                }
            }
            else{
                //MapLocation target = findCoordinatedBroadcastFlag(rc);
                target = findClosestBroadcastFlags(rc);
                if(target != null && rc.getLocation().distanceSquaredTo(target) < 6){
                    invalidBroadcasts.add(target);
                }
                if(target != null && !rc.getLocation().equals(target) && !invalidBroadcasts.contains(target)) {
                    if (rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target)))) {
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(target)));
                    }
                    Pathfinding.combinedPathfinding(rc, target);
                }
                else if (Utilities.getClosestFlag(rc) != null){
                    state = states.defense;
                }
                else if(Utilities.newGetClosestEnemy(rc) != null){
                    Pathfinding.combinedPathfinding(rc, Utilities.newGetClosestEnemy(rc));
                }

//                else{
//                    state = states.explore;
//                }
            }
            if(rc.isActionReady()) {
                updateInfo(rc);
                attemptHeal(rc);
            }
        }
    }

    //heist! we have more or equal allies to enemies in vision, and can see a flag, so we are gonna rush their flag
    public static void heist(RobotController rc) throws GameActionException {
        FlagInfo targetFlag = nearbyFlagsEnemy[0];
        if(rc.canPickupFlag(targetFlag.getLocation())) {
            rc.pickupFlag(targetFlag.getLocation());
            Pathfinding.combinedPathfinding(rc, findClosestSpawnLocation(rc));
            state = states.flagCarrier;
        }
        else {
            if(enemyRobotsAttackRange.length == 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(targetFlag.getLocation())))){
                rc.fill(rc.getLocation().add(rc.getLocation().directionTo(targetFlag.getLocation())));
            }
            if(rc.getLocation().distanceSquaredTo(targetFlag.getLocation()) < 16) {
                Pathfinding.bellmanFord5x5(rc, targetFlag.getLocation());
            }
            else if(rc.isActionReady()){
                if(rc.canPickupFlag(targetFlag.getLocation())) {
                    rc.pickupFlag(targetFlag.getLocation());
                    if(rc.isMovementReady()) Pathfinding.combinedPathfinding(rc, findClosestSpawnLocation(rc));
                    state = states.flagCarrier;
                    return;
                }
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
            //try to dig behind us to maybe slow them down
            if(enemyRobotsAttackRange.length == 0 && Utilities.isBetween(rc.getLocation(), escortee.getLocation(), averageRobotLocation(enemyRobots))){
                if(rc.canBuild(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
                    rc.build(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
                }
//                if(rc.canDig(rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
//                    rc.dig(rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
//                }
//                if(rc.canBuild(TrapType.WATER, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
//                    rc.build(TrapType.WATER, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
//                }
            }
            if(enemyRobotsAttackRange.length == 0){
                //if we are ahead of escortee, maybe fill in some water
                if(!Utilities.isBetween(rc.getLocation(), averageRobotLocation(enemyRobots), escortee.getLocation())){
                    if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(findClosestSpawnLocation(rc))))){
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(findClosestSpawnLocation(rc))));
                    }
                }
            }
            //try and move into attack range of any nearby enemies
            if (rc.isActionReady()){
                runMicroAttack(rc);
                updateInfo(rc);
                if(enemyRobots.length > 6 && enemyRobotsAttackRange.length == 0){
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
        if(rc.isMovementReady()){
            //**CHANGE
            StolenFlag temp = new StolenFlag(escortee.getLocation(), false);
            //**CHANGE ^^^
            Pathfinding.bellmanFordFlag(rc, temp.location, temp);
        }
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
                if(square.enemiesAttackRangedX < 0 || square.enemiesAttackRangedX > 1)
                    continue;
                float score;
                if(square.enemiesAttackRangedX == 1){
                    score = 1000000 + square.enemiesVisiondX + square.alliesVisiondX * -1 + +square.alliesHealRangedX * -1 + square.potentialEnemiesAttackRangedX * -3 + square.hasTrap.compareTo(false) * 1.0f;
                }
                else {
                    score = /*square.enemiesAttackRangedX * 4 + */square.enemiesVisiondX * 3 + square.alliesVisiondX * -1 + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -1 + square.hasTrap.compareTo(false) * 1.0f /*+ square.potentialEnemiesPrepareAttackdX * 1.0f*/;
                }
                if(score > highScore){
                    highScore = score;
                    best = square;
                }
            }
        }
        if(best != null) {
            //make sure the move isnt outright harmful, or too stupid
            if (rc.canMove(rc.getLocation().directionTo(best.location))) {
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
                if(square.enemiesAttackRangedX > 0)
                    continue;
                float score = square.enemiesAttackRangedX * -6 + square.enemiesVisiondX * 1.5f + square.alliesVisiondX + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -3 + square.hasTrap.compareTo(false) * 3.5f + square.potentialEnemiesPrepareAttackdX * -0.25f;
                if(score > highScore){
                    highScore = score;
                    best = square;
                }
            }
        }
        if(best != null) {
            if (rc.canMove(rc.getLocation().directionTo(best.location))) {
                rc.move(rc.getLocation().directionTo(best.location));
            }
        }
    }
    //try and get out of lost fights
    public static void retreat(RobotController rc) throws GameActionException {
        engagementMicroSquare[] options = new engagementMicroSquare[8];
        populateMicroArray(rc, options);
        engagementMicroSquare best = null;
        float highScore = Integer.MIN_VALUE;
        for(engagementMicroSquare square : options){
            if(square.passable){
                if(square.enemiesAttackRangedX > 0)
                    continue;
                float score = square.enemiesAttackRangedX * -3 + square.enemiesVisiondX * -4.0f + square.alliesVisiondX + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -3.0f + square.hasTrap.compareTo(false) * 5.5f + square.potentialEnemiesPrepareAttackdX * -3.0f;
                if(score > highScore){
                    highScore = score;
                    best = square;
                }
            }
        }
        if(best != null) {
            if (rc.canMove(rc.getLocation().directionTo(best.location))) {
                rc.move(rc.getLocation().directionTo(best.location));
            }
        }
    }
    //for when we can't attack now, but we can next turn - try to avoid moving into enemy movement range, but also avoid losing sight of enemies
    public static void runMicroPrepare(RobotController rc) throws GameActionException {
        engagementMicroSquare[] options = new engagementMicroSquare[8];
        populateMicroArray(rc, options);
        engagementMicroSquare best = null;
        float highScore = Integer.MIN_VALUE;
        for(engagementMicroSquare square : options){
            if(square.passable){
                float score = square.enemiesAttackRangedX * -4 + square.enemiesVisiondX * 1.3f + square.alliesVisiondX * 1.0f + square.alliesHealRangedX * 0.25f + square.potentialEnemiesAttackRangedX * 2 + square.potentialEnemiesPrepareAttackdX * 1.0f;
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
                    if(rc.senseMapInfo(tempSquare).getTrapType() != null)
                        options[index].hasTrap = true;
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    RobotInfo[] potentialEnemiesPrepareAttackNewLoc = rc.senseNearbyRobots(tempSquare, 16, rc.getTeam().opponent());
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = potentialEnemiesAttackRangeNewLoc.length - potentialEnemiesAttackRange.length;
                    options[index].potentialEnemiesPrepareAttackdX = potentialEnemiesPrepareAttackNewLoc.length - potentialEnemiesPrepareAttack.length;
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
                    if(rc.senseMapInfo(tempSquare).getTrapType() != null)
                        options[index].hasTrap = true;
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    RobotInfo[] potentialEnemiesPrepareAttackNewLoc = rc.senseNearbyRobots(tempSquare, 16, rc.getTeam().opponent());
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = potentialEnemiesAttackRangeNewLoc.length - potentialEnemiesAttackRange.length;
                    options[index].potentialEnemiesPrepareAttackdX = potentialEnemiesPrepareAttackNewLoc.length - potentialEnemiesPrepareAttack.length;
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
                    if(rc.senseMapInfo(tempSquare).getTrapType() != null)
                        options[index].hasTrap = true;
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    RobotInfo[] potentialEnemiesPrepareAttackNewLoc = rc.senseNearbyRobots(tempSquare, 16, rc.getTeam().opponent());
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = potentialEnemiesAttackRangeNewLoc.length - potentialEnemiesAttackRange.length;
                    options[index].potentialEnemiesPrepareAttackdX = potentialEnemiesPrepareAttackNewLoc.length - potentialEnemiesPrepareAttack.length;
                }
            } else {
                options[index] = new engagementMicroSquare(false);
            }
            index++;
        }
    }

    public static void attemptAttack(RobotController rc) throws GameActionException {
        MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
        if (toAttack != null && rc.canAttack(toAttack)) {
            rc.attack(toAttack);
        }
        if(rc.isActionReady()){
            if (toAttack != null && rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        }
    }
    public static float calculatePowerIndexEnemies(RobotController rc){
        float ret = 0;
        for(RobotInfo robot : enemyRobots){
            ret += robot.getHealth();
        }
        for(RobotInfo robot : enemyRobotsAttackRange){
            ret += robot.getHealth() / POWER_PROXIMITY_CONSTANT;
        }
        ret *= (enemyRobots.length / POWER_CONSTANT);
        //return ret/maxPower;
        return ret;
    }

    public static float calculatePowerIndexAllies(RobotController rc){
        float ret = 0;
        for(RobotInfo robot : allyRobots){
            ret += robot.getHealth();
        }
        ret += rc.getHealth();
        for(RobotInfo robot : allyRobotsHealRange){
            ret += robot.getHealth() / POWER_PROXIMITY_CONSTANT;
        }
        ret += rc.getHealth() / POWER_PROXIMITY_CONSTANT;
        ret *= ((allyRobots.length + 1) / POWER_CONSTANT);
        //return ret/maxPower;
        return ret;
    }

    //returns power of enemies divided by power of allies
    public static float calculatePowerIndexQuotient(RobotController rc){
        return calculatePowerIndexEnemies(rc) / calculatePowerIndexAllies(rc);
    }

    //attempts to heal lowest health nearby ally, but only does so if they are below the given health
    public static void attemptHealConditional(RobotController rc, int maxHealth) throws GameActionException{
        RobotInfo toHeal = bestHeal(rc, allyRobotsHealRange);
        if(toHeal != null && rc.canHeal(toHeal.getLocation()) && toHeal.health < maxHealth)
            rc.heal(toHeal.getLocation());
    }

    public static void attemptHeal(RobotController rc) throws GameActionException{
        RobotInfo toHeal = bestHeal(rc, allyRobotsHealRange);
        if(toHeal != null && rc.canHeal(toHeal.getLocation()) && toHeal.health < (1000 - rc.getHealAmount()))
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
        MapLocation toReturn = null;
        if(index3 != 0){
            //we can see it, but couldnt sense any flags earlier... its been removed
            if(rc.canSenseLocation(enemyFlag1) && !seesFlag(enemyFlag1)) {
                eraseEnemyFlag(rc, enemyFlag1);
            }
            else{
                toReturn = enemyFlag1;
            }
        }
        if(index4 != 0){
            //we can see it, but couldnt sense any flags earlier... its been removed
            if(rc.canSenseLocation(enemyFlag2) && !seesFlag(enemyFlag2)) {
                eraseEnemyFlag(rc, enemyFlag2);
            }
            else{
                if(toReturn == null)
                    toReturn = enemyFlag2;
            }
        }
        if(index5 != 0){
            //we can see it, but couldnt sense any flags earlier... its been removed
            if(rc.canSenseLocation(enemyFlag3) && !seesFlag(enemyFlag3)) {
                eraseEnemyFlag(rc, enemyFlag3);
            }
            else{
                if(toReturn == null)
                    toReturn = enemyFlag3;
            }
        }
        return toReturn;
    }

    //used to check if we can see an enemy flag
    public static boolean seesFlag(MapLocation flag){
        for(FlagInfo f : nearbyFlagsEnemy){
            if(f.getLocation().equals(flag))
                return true;
        }
        return false;
    }
    //returns the total health of an array of robots
    public static int totalHealth(RobotInfo[] robots) {
        int ret = 0;
        for (RobotInfo robot : robots) {
            ret += robot.health;
        }
        return ret;
    }
    public static MapLocation generateTargetLoc(RobotController rc) {
        int x = rng.nextInt(rc.getMapWidth());
        int y = rng.nextInt(rc.getMapHeight());
        return new MapLocation(x, y);
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
    public int potentialEnemiesPrepareAttackdX;
    public Boolean hasTrap;
    public engagementMicroSquare(){

    }
    public engagementMicroSquare(boolean p){
        passable = p;
        hasTrap = false;
    }
    public engagementMicroSquare(int x, int y){
        location = new MapLocation(x, y);
        hasTrap = false;
    }
    public engagementMicroSquare(boolean passability, int x, int y){
        passable = passability;
        this.enemiesAttackRangedX = 0;
        this.enemiesVisiondX = 0;
        location = new MapLocation(x, y);
        hasTrap = false;

    }
//    public engagementMicroSquare(boolean passabillity, int enemiesAttackRangedX, int enemiesVisiondX, int x, int y){
//        passable = passabillity;
//        this.enemiesAttackRangedX = enemiesAttackRangedX;
//        this.enemiesVisiondX = enemiesVisiondX;
//        this.x = x;
//        this.y = y;
//    }

}

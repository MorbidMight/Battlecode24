package Version15;

import battlecode.common.*;

import java.nio.file.Path;
import java.util.ArrayList;

import static Version15.RobotPlayer.*;
import static Version15.Utilities.*;

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
    //looks at enemies in a radius of sqrt(16), the enemies that we could attack in two moves
    static RobotInfo[] potentialEnemiesPrepareAttack;
    static FlagInfo[] nearbyFlagsAlly;
    static FlagInfo[] nearbyFlagsEnemy;
    static RobotInfo escortee;
    static RobotInfo lastSeenEnemy;
    final static float STOLEN_FLAG_CONSTANT = 2.7f;
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
        if(state == states.escort && enemyRobots.length == 0 && rc.getLocation().distanceSquaredTo(findClosestSpawnLocation(rc)) < 25)
            return states.attack;
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
            if (flag.isPickedUp() && (enemyRobots.length > 0 ||rc.getLocation().distanceSquaredTo(findClosestSpawnLocation(rc)) >= 25)) {
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
            MapLocation target = rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots)));
            if(!isTrapAdjacent(rc, target, toBeBuilt) && rc.canBuild(toBeBuilt, target)){
                rc.build(toBeBuilt, target);
            }
            else if(!isTrapAdjacent(rc, rc.getLocation(), toBeBuilt) && rc.canBuild(toBeBuilt, rc.getLocation())){
                rc.build(toBeBuilt, rc.getLocation());
            }
            if(rc.isActionReady() && rc.getCrumbs() > 2500){
                toBeBuilt = TrapType.EXPLOSIVE;if(!isTrapAdjacent(rc, target, toBeBuilt) && rc.canBuild(toBeBuilt, target)){
                    rc.build(toBeBuilt, target);
                }
                else if(!isTrapAdjacent(rc, rc.getLocation(), toBeBuilt) && rc.canBuild(toBeBuilt, rc.getLocation())){
                    rc.build(toBeBuilt, rc.getLocation());
                }

            }
        }
        if(rc.isActionReady()){
            attemptAttack(rc);
        }
        if(enemyRobots.length != 0) {
            //only kite for the first five rounds following the dam break
//            if(rc.getRoundNum() >= 200 && rc.getRoundNum() <= 205 && enemyRobots.length > 2){
//                runMicroKite(rc);
//                attemptAttack(rc);
//                attemptHeal(rc);
//            }
            if(((float) totalHealth(enemyRobots) / ((totalHealth(allyRobots) + rc.getHealth())) > 2.0f && enemyRobots.length > allyRobots.length + 3)){
                retreat(rc);
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
            //try and move into attack range of any nearby enemies
            else if ((rc.isActionReady() /*|| ((allyRobots.length - enemyRobots.length > 6) && enemyRobotsAttackRange.length == 0)) */&& rc.getHealth() > 150)){
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
            MapLocation target;
            if(knowFlag(rc)){
                target = findClosestActualFlag(rc);
                if(target != null){
                    if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target)))){
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(target)));
                    }
                    Pathfinding.combinedPathfinding(rc, target);
                }
            }
            else if(getClosestCluster(rc) != null){
                target = getClosestCluster(rc);
                //Pathfinding.combinedPathfinding(rc, lastSeenEnemy.getLocation());
                if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target))))
                    rc.fill(rc.getLocation().add(rc.getLocation().directionTo(target)));
                Pathfinding.bellmanFord5x5(rc, target);
                return;
            }
            else{
                //MapLocation target = findCoordinatedBroadcastFlag(rc);
                target = findClosestBroadcastFlags(rc);
                if(target != null) {
                    if (rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target)))) {
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(target)));
                    }
                    Pathfinding.combinedPathfinding(rc, findCoordinatedBroadcastFlag(rc));
                }
            }
            //          if(target == null) System.out.println(rc.getLocation() + " : lame");
//            else System.out.println(rc.getLocation() + " reporting to " + target);
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
        float aggressionIndex = rc.getHealth() / 9000f;
        float highScore = Integer.MIN_VALUE;
        for(engagementMicroSquare square : options){
            if(square.passable){
                float score;
                if(square.enemiesAttackRangedX == 1){
                    score = 1000000 + square.alliesHealRangedX * 0.25f + square.potentialEnemiesAttackRangedX * -5.0f + square.potentialKill.compareTo(false) * 10.0f;
                }
                else if(square.enemiesAttackRangedX == 2){
                    if(square.potentialKill){
                        score = 5000 + square.enemiesVisiondX + square.alliesVisiondX * -1 + square.alliesHealRangedX * 0.25f + square.potentialEnemiesAttackRangedX * -5.5f;
                    }
                    else{
                        score = square.enemiesAttackRangedX * 4 * aggressionIndex + square.enemiesVisiondX + square.alliesVisiondX * -1 + square.alliesHealRangedX * 1.25f + square.potentialEnemiesAttackRangedX * -3.65f;
                    }
                }
                else if(square.enemiesAttackRangedX == 0) {
                    score = square.enemiesVisiondX + square.alliesVisiondX * -1 + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -4.5f + square.potentialEnemiesPrepareAttackdX * 4.5f;
                }
                else{
                    score = Integer.MIN_VALUE;
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
    public static void runMicroAttackVersion2(RobotController rc) throws GameActionException {
        engagementMicroSquareVersion2[] options = new engagementMicroSquareVersion2[9];
        populateMicroArrayVersion2(rc, options);
        engagementMicroSquareVersion2 best = options[8];
        for(int i = options.length - 2; i >= 0; i--){
            if(isBetterAttack(options[i], best)){
                best = options[i];
            }
        }
        if(best.enemiesAttackRange >= enemyRobotsAttackRange.length && best.passable && !best.location.equals(rc.getLocation()) && rc.canMove(rc.getLocation().directionTo(best.location))){
            rc.move(rc.getLocation().directionTo(best.location));
        }
    }
    //returns true if the square is better than the current best square
    public static boolean isBetterAttack(engagementMicroSquareVersion2 square, engagementMicroSquareVersion2 curBest){
        if(!square.passable)
            return false;
        if(!curBest.passable)
            return true;
        if(square.enemiesAttackRange > 2 && curBest.enemiesAttackRange <= 2)
            return false;
        if(curBest.enemiesAttackRange == 1){
            if(square.enemiesAttackRange != 1)
                return false;
            if (curBest.potentialKill && !square.potentialKill)
                return false;
            if(square.potentialKill && !curBest.potentialKill)
                return true;
            return square.totalHealthEnemiesAttackRange <= curBest.totalHealthEnemiesAttackRange;
        }
        else{
            if(square.enemiesAttackRange == 1)
                return true;
            if((curBest.enemiesAttackRange > 2 || curBest.enemiesAttackRange == 0) && square.enemiesAttackRange == 2)
                return true;
            if(curBest.enemiesAttackRange == 2 && square.enemiesAttackRange == 2){
                if(curBest.potentialKill && !square.potentialKill)
                    return false;
                else if(square.potentialKill && !curBest.potentialKill)
                    return true;
                return square.totalHealthEnemiesAttackRange <= curBest.totalHealthEnemiesAttackRange;
            }
            if(square.enemiesAttackRange == 0 && curBest.enemiesAttackRange == 0){
                if(square.potentialEnemiesAttackRange < curBest.potentialEnemiesAttackRange)
                    return true;
                else if(curBest.potentialEnemiesAttackRange < square.potentialEnemiesAttackRange)
                    return false;
                else{
                    return square.potentialEnemiesPrepareAttack >= curBest.potentialEnemiesPrepareAttack;
                }
            }
        }
        return false;
    }
    //find the location that will be in attack range of the least amount of enemies, and move there
    //tiebreaker between multiple locations is location in vision range of least amount of enemies
    public static void runMicroKite(RobotController rc) throws GameActionException {
        engagementMicroSquare[] options = new engagementMicroSquare[8];
        populateMicroArray(rc, options);
        engagementMicroSquare best = null;
        float highScore = Integer.MIN_VALUE;
        float score;
        for(engagementMicroSquare square : options){
            if(square.passable){
                if(square.enemiesAttackRangedX * -1 == enemyRobotsAttackRange.length){
                    score = 10000 + square.potentialEnemiesAttackRangedX * -7.0f + square.alliesHealRangedX * 1.5f + square.alliesVisiondX + square.enemiesVisiondX * 1.25f + square.hasTrap.compareTo(false) * 3.5f + square.potentialEnemiesPrepareAttackdX * 3.25f;
                }
                else if(square.enemiesAttackRangedX < 0){
                    score = 5000 + square.potentialEnemiesAttackRangedX * -5.0f + square.alliesHealRangedX * 2.0f + square.alliesVisiondX + square.enemiesVisiondX + square.hasTrap.compareTo(false) * 5.5f + square.potentialEnemiesPrepareAttackdX * 1.5f;
                }
                else{
                    score = square.enemiesVisiondX * 1.25f + square.alliesVisiondX + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -4.0f + square.hasTrap.compareTo(false) * 5.5f + square.potentialEnemiesPrepareAttackdX * 2.55f;

                }
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
    public static void runMicroKiteVersion2(RobotController rc) throws GameActionException {
        engagementMicroSquareVersion2[] options = new engagementMicroSquareVersion2[9];
        populateMicroArrayVersion2(rc, options);
        engagementMicroSquareVersion2 best = options[8];
        for(int i = options.length - 2; i >= 0; i--){
            if(isBetterKite(options[i], best)){
                best = options[i];
            }
        }
        if(best.passable && !best.location.equals(rc.getLocation()) && rc.canMove(rc.getLocation().directionTo(best.location))){
            rc.move(rc.getLocation().directionTo(best.location));
        }
    }
    public static boolean isBetterKite(engagementMicroSquareVersion2 square, engagementMicroSquareVersion2 curBest){
        if(!square.passable)
            return false;
        if(!curBest.passable)
            return true;
        if(square.enemiesAttackRange > curBest.enemiesAttackRange)
            return false;
        else if(square.enemiesAttackRange < curBest.enemiesAttackRange)
            return true;
        else{
            if(square.potentialEnemiesAttackRange < curBest.potentialEnemiesAttackRange)
                return true;
            else if(square.potentialEnemiesAttackRange > curBest.potentialEnemiesAttackRange)
                return false;
            else{
                if(square.hasTrap && !curBest.hasTrap)
                    return true;
                else if(!square.hasTrap && curBest.hasTrap)
                    return false;
                else{
                    if(square.alliesHealRange > curBest.alliesHealRange)
                        return true;
                    else if(square.alliesHealRange < curBest.alliesHealRange)
                        return false;
                    else{
                        return square.totalHealthEnemies <= curBest.totalHealthEnemies;
                    }
                }
            }
        }
    }
    public static void retreat(RobotController rc) throws GameActionException {
        engagementMicroSquare[] options = new engagementMicroSquare[8];
        populateMicroArray(rc, options);
        engagementMicroSquare best = null;
        float highScore = Integer.MIN_VALUE;
        for(engagementMicroSquare square : options){
            if(square.passable){
                Direction desiredDirection = rc.getLocation().directionTo(findClosestSpawnLocation(rc));
                Direction actualDirection = rc.getLocation().directionTo(square.location);
                float score = square.enemiesAttackRangedX * -5.0f + square.enemiesVisiondX * -3.0f + square.alliesVisiondX + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -3 + square.hasTrap.compareTo(false) * 6.0f + square.potentialEnemiesPrepareAttackdX * -1.5f;
//                if(actualDirection == desiredDirection || actualDirection == desiredDirection.rotateLeft() || actualDirection == desiredDirection.rotateRight())
//                    score += 2.5f;
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
                    options[index].potentialKill = isKillable(rc, enemyRobotsAttackRangeNewLoc);
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = (potentialEnemiesAttackRangeNewLoc.length - enemyRobotsAttackRangeNewLoc.length) - (potentialEnemiesAttackRange.length - enemyRobotsAttackRange.length);
                    options[index].potentialEnemiesPrepareAttackdX = (potentialEnemiesPrepareAttackNewLoc.length - potentialEnemiesAttackRangeNewLoc.length) - (potentialEnemiesPrepareAttack.length - potentialEnemiesAttackRange.length);
                    //options[index].totalHealthAlliesdX = totalHealth(allyRobotsNewLoc) - totalHealth(allyRobots);
                    //options[index].totalHealthEnemiesdX = totalHealth(enemyRobotsNewLoc) - totalHealth(enemyRobots);
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
                    options[index].potentialKill = isKillable(rc, enemyRobotsAttackRangeNewLoc);
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = (potentialEnemiesAttackRangeNewLoc.length - enemyRobotsAttackRangeNewLoc.length) - (potentialEnemiesAttackRange.length - enemyRobotsAttackRange.length);
                    options[index].potentialEnemiesPrepareAttackdX = (potentialEnemiesPrepareAttackNewLoc.length - potentialEnemiesAttackRangeNewLoc.length) - (potentialEnemiesPrepareAttack.length - potentialEnemiesAttackRange.length);
                    //options[index].totalHealthAlliesdX = totalHealth(allyRobotsNewLoc) - totalHealth(allyRobots);
                    //options[index].totalHealthEnemiesdX = totalHealth(enemyRobotsNewLoc) - totalHealth(enemyRobots);
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
                    if (rc.senseMapInfo(tempSquare).getTrapType() != null)
                        options[index].hasTrap = true;
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    RobotInfo[] potentialEnemiesPrepareAttackNewLoc = rc.senseNearbyRobots(tempSquare, 16, rc.getTeam().opponent());
                    options[index].potentialKill = isKillable(rc, enemyRobotsAttackRangeNewLoc);
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = (potentialEnemiesAttackRangeNewLoc.length - enemyRobotsAttackRangeNewLoc.length) - (potentialEnemiesAttackRange.length - enemyRobotsAttackRange.length);
                    options[index].potentialEnemiesPrepareAttackdX = (potentialEnemiesPrepareAttackNewLoc.length - potentialEnemiesAttackRangeNewLoc.length) - (potentialEnemiesPrepareAttack.length - potentialEnemiesAttackRange.length);
                    //options[index].totalHealthAlliesdX = totalHealth(allyRobotsNewLoc) - totalHealth(allyRobots);
                    //options[index].totalHealthEnemiesdX = totalHealth(enemyRobotsNewLoc) - totalHealth(enemyRobots);
                }
            } else {
                options[index] = new engagementMicroSquare(false);
            }
            index++;
        }
    }
    public static void populateMicroArrayVersion2(RobotController rc, engagementMicroSquareVersion2[] options) throws GameActionException {
        int index = 0;
        MapLocation curLoc = rc.getLocation();
        int mapWidth = rc.getMapWidth();
        int mapHeight = rc.getMapHeight();
        int curX = curLoc.x;
        int curY = curLoc.y;
        int newX;
        int newY;
        for (int k = -1; k <= 1; k++) {
//            if (-1 == 0 && k == 0)
//                continue;
            newX = curX + -1;
            newY = curY + k;
            if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
                options[index] = new engagementMicroSquareVersion2(newX, newY);
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
                    //RobotInfo[] potentialEnemiesPrepareAttackNewLoc = rc.senseNearbyRobots(tempSquare, 16, rc.getTeam().opponent());
                    options[index].potentialKill = isKillable(rc, enemyRobotsAttackRangeNewLoc);
                    options[index].enemiesVision = enemyRobotsNewLoc.length;
                    options[index].enemiesAttackRange = enemyRobotsAttackRangeNewLoc.length;
                    options[index].alliesVision = allyRobotsNewLoc.length;
                    options[index].alliesHealRange = allyRobotsHealRangeNewLoc.length;
                    options[index].potentialEnemiesAttackRange = potentialEnemiesAttackRangeNewLoc.length;
                    //options[index].potentialEnemiesPrepareAttack = potentialEnemiesPrepareAttackNewLoc.length;
                    options[index].totalHealthAllies = totalHealth(allyRobotsNewLoc) + rc.getHealth();
                    options[index].totalHealthEnemies = totalHealth(enemyRobotsNewLoc);
                    options[index].totalHealthEnemiesAttackRange = totalHealth(enemyRobotsAttackRange);
                    //options[index].closestEnemy = closestEnemyDistance(rc, enemyRobotsNewLoc, enemyRobotsAttackRangeNewLoc);
                }
            } else {
                options[index] = new engagementMicroSquareVersion2(false);
            }
            index++;
        }
        for (int k = -1; k <= 1; k++) {
            newX = curX + 0;
            newY = curY + k;
            if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
                options[index] = new engagementMicroSquareVersion2(newX, newY);
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
                    //RobotInfo[] potentialEnemiesPrepareAttackNewLoc = rc.senseNearbyRobots(tempSquare, 16, rc.getTeam().opponent());
                    options[index].potentialKill = isKillable(rc, enemyRobotsAttackRangeNewLoc);
                    options[index].enemiesVision = enemyRobotsNewLoc.length;
                    options[index].enemiesAttackRange = enemyRobotsAttackRangeNewLoc.length;
                    options[index].alliesVision = allyRobotsNewLoc.length;
                    options[index].alliesHealRange = allyRobotsHealRangeNewLoc.length;
                    options[index].potentialEnemiesAttackRange = potentialEnemiesAttackRangeNewLoc.length;
                    //options[index].potentialEnemiesPrepareAttack = potentialEnemiesPrepareAttackNewLoc.length;
                    options[index].totalHealthAllies = totalHealth(allyRobotsNewLoc) + rc.getHealth();
                    options[index].totalHealthEnemies = totalHealth(enemyRobotsNewLoc);
                    options[index].totalHealthEnemiesAttackRange = totalHealth(enemyRobotsAttackRange);
                    //options[index].closestEnemy = closestEnemyDistance(rc, enemyRobotsNewLoc, enemyRobotsAttackRangeNewLoc);
                }
            } else {
                options[index] = new engagementMicroSquareVersion2(false);
            }
            index++;
        }
        for (int k = -1; k <= 1; k++) {
//            if (1 == 0 && k == 0)
//                continue;
            newX = curX + 1;
            newY = curY + k;
            if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
                options[index] = new engagementMicroSquareVersion2(newX, newY);
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
                    //RobotInfo[] potentialEnemiesPrepareAttackNewLoc = rc.senseNearbyRobots(tempSquare, 16, rc.getTeam().opponent());
                    options[index].potentialKill = isKillable(rc, enemyRobotsAttackRangeNewLoc);
                    options[index].enemiesVision = enemyRobotsNewLoc.length;
                    options[index].enemiesAttackRange = enemyRobotsAttackRangeNewLoc.length;
                    options[index].alliesVision = allyRobotsNewLoc.length;
                    options[index].alliesHealRange = allyRobotsHealRangeNewLoc.length;
                    options[index].potentialEnemiesAttackRange = potentialEnemiesAttackRangeNewLoc.length;
                    //options[index].potentialEnemiesPrepareAttack = potentialEnemiesPrepareAttackNewLoc.length;
                    options[index].totalHealthAllies = totalHealth(allyRobotsNewLoc) + rc.getHealth();
                    options[index].totalHealthEnemies = totalHealth(enemyRobotsNewLoc);
                    options[index].totalHealthEnemiesAttackRange = totalHealth(enemyRobotsAttackRange);
                    //options[index].closestEnemy = closestEnemyDistance(rc, enemyRobotsNewLoc, enemyRobotsAttackRangeNewLoc);
                }
            } else {
                options[index] = new engagementMicroSquareVersion2(false);
            }
            index++;
        }
    }
    public static boolean isKillable(RobotController rc, RobotInfo[] enemies){
        int i = rc.getAttackDamage();
        for(RobotInfo enemy : enemies){
            if(i >= enemy.getHealth())
                return true;
        }
        return false;
    }

    public static void attemptAttack(RobotController rc) throws GameActionException {
        MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
        if (toAttack != null && rc.canAttack(toAttack)) {
            if (rc.senseRobotAtLocation(toAttack).health <= rc.getAttackDamage()&&!rc.senseRobotAtLocation(toAttack).team.isPlayer())
                Utilities.addKillToKillsArray(rc, turnsWithKills);
            rc.attack(toAttack);
        }
        if(rc.isActionReady()){
            if (toAttack != null && rc.canAttack(toAttack)) {
                if (rc.senseRobotAtLocation(toAttack).health <= rc.getAttackDamage()&&!rc.senseRobotAtLocation(toAttack).team.isPlayer())
                    Utilities.addKillToKillsArray(rc, turnsWithKills);
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

    public static int totalHealth(RobotInfo[] robots) {
        int ret = 0;
        for (RobotInfo robot : robots) {
            ret += robot.health;
        }
        return ret;
    }
    //returns if any of the spaces next to this one are a trap, or if the space itself is a trap
    public static boolean isTrapAdjacent(RobotController rc, MapLocation location) throws GameActionException {
        MapInfo[] adjacents = rc.senseNearbyMapInfos(location, 2);
        for(MapInfo square : adjacents){
            if(square.getTrapType() != TrapType.NONE)
                return true;
        }
        return false;
    }
    public static boolean isTrapAdjacent(RobotController rc, MapLocation location, TrapType t) throws GameActionException {
        MapInfo[] adjacents = rc.senseNearbyMapInfos(location, 2);
        for(MapInfo square : adjacents){
            if(square.getTrapType() == t)
                return true;
        }
        return false;
    }
    public static int closestEnemyDistance(RobotController rc, RobotInfo[] enemyRobots, RobotInfo[] enemyRobotsAttackRange){
        if(enemyRobots.length == 0)
            return -1;
        int lowIndex = -1;
        int lowDist = Integer.MAX_VALUE;
        if(enemyRobotsAttackRange.length > 0){
            for(int i = 0; i < enemyRobotsAttackRange.length; i++){
                int dist = rc.getLocation().distanceSquaredTo(enemyRobotsAttackRange[i].getLocation());
                if(dist < lowDist){
                    lowDist = dist;
                    lowIndex = i;
                }
            }
        }
        else {
            for (int i = 0; i < enemyRobots.length; i++) {
                int dist = rc.getLocation().distanceSquaredTo(enemyRobots[i].getLocation());
                if(dist < lowDist){
                    lowDist = dist;
                    lowIndex = i;
                }
            }
        }
        if(lowIndex == -1)
            return -1;
        else
            return lowDist;
    }
    public static int closestEnemyDistance(RobotController rc){
        if(enemyRobots.length == 0)
            return -1;
        int lowIndex = -1;
        int lowDist = Integer.MAX_VALUE;
        if(enemyRobotsAttackRange.length > 0){
            for(int i = 0; i < enemyRobotsAttackRange.length; i++){
                int dist = rc.getLocation().distanceSquaredTo(enemyRobotsAttackRange[i].getLocation());
                if(dist < lowDist){
                    lowDist = dist;
                    lowIndex = i;
                }
            }
        }
        else {
            for (int i = 0; i < enemyRobots.length; i++) {
                int dist = rc.getLocation().distanceSquaredTo(enemyRobots[i].getLocation());
                if(dist < lowDist){
                    lowDist = dist;
                    lowIndex = i;
                }
            }
        }
        if(lowIndex == -1)
            return -1;
        else
            return lowDist;
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
    public Boolean potentialKill;
    public int totalHealthEnemiesdX;
    public int totalHealthAlliesdX;

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

class engagementMicroSquareVersion2{
    public MapLocation location;
    public boolean passable;
    public int enemiesAttackRange;
    public int enemiesVision;
    public int alliesVision;
    public int alliesHealRange;
    public int potentialEnemiesAttackRange;
    public int potentialEnemiesPrepareAttack;
    public Boolean hasTrap;
    public Boolean potentialKill;
    public int totalHealthEnemies;
    public int totalHealthAllies;
    public int totalHealthEnemiesAttackRange;
    public int closestEnemy;

    public engagementMicroSquareVersion2(){

    }
    public engagementMicroSquareVersion2(boolean p){
        passable = p;
        hasTrap = false;
    }
    public engagementMicroSquareVersion2(int x, int y){
        location = new MapLocation(x, y);
        hasTrap = false;
    }
    public engagementMicroSquareVersion2(boolean passability, int x, int y){
        passable = passability;
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

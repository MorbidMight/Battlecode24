package Version15MovingFlags;

import battlecode.common.*;
import static Version15MovingFlags.Utilities.*;
import static Version15MovingFlags.RobotPlayer.*;


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
    //records the enemies seen la st round, to create the stunlist
    static float aggresionIndex;
    static float RETREAT_HEALTH = 150;
    static boolean enemyAttackUpgrade;



    static states state;
    public static void runSoldier(RobotController rc) throws GameActionException {
        if (!rc.isSpawned()) {
            lastSeenEnemy = null;
            return;
        }
        int numJailed = rc.readSharedArray(6);
        int numEnemyJailed = rc.readSharedArray(8);
        //numenemyjailed has 0.01 added to avoid divion by 0
        aggresionIndex = rc.getHealth() / 750f * ((float) numEnemyJailed / (numJailed + 1));
        //System.out.println(aggresionIndex);
        if(rc.readSharedArray(53) != 0 && rc.canSenseLocation(Utilities.convertIntToLocation(53)) && rc.senseRobotAtLocation(Utilities.convertIntToLocation(53)) == null){
            rc.writeSharedArray(53, 0);
        }
        else if(rc.readSharedArray(54) != 0 && rc.canSenseLocation(Utilities.convertIntToLocation(54)) && rc.senseRobotAtLocation(Utilities.convertIntToLocation(54)) == null){
            rc.writeSharedArray(54, 0);
        }
        tryGetCrumbs(rc);
        updateInfo(rc);
        determineRetreatHealth(rc);
        findCoordinatedActualFlag(rc);
        //used to update which flags we know are real or not
        for(FlagInfo flag : nearbyFlagsEnemy){
            checkRecordEnemyFlag(rc, flag);
        }
        for(FlagInfo flag : nearbyFlagsEnemy){
            if(rc.canPickupFlag(flag.getLocation())){
                rc.pickupFlag(flag.getLocation());
                state = states.flagCarrier;
            }
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
            if (flag.isPickedUp() || (rc.canSenseLocation(flag.getLocation()) && (!Utilities.isDefaultLocation(rc, flag.getLocation()))))
                if(rc.senseMapInfo(flag.getLocation()).getSpawnZoneTeamObject() != rc.getTeam() || flag.isPickedUp())
                    return states.defense;
        }
        for (FlagInfo flag : nearbyFlagsEnemy) {
            if (flag.isPickedUp() && (enemyRobots.length > 0 ||rc.getLocation().distanceSquaredTo(findClosestSpawnLocation(rc)) >= 36)) {
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
                //Pathfinding.bellmanFord5x5(rc, closestFlag.location);
                BFSKernel.BFS(rc, closestFlag.location);
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
            if(rc.isMovementReady()) runMicroAttack(rc);
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
            if(rc.isActionReady() && rc.getCrumbs() > 1500 && rc.getRoundNum()>300){
                toBeBuilt = TrapType.EXPLOSIVE;
                if(rc.canBuild(toBeBuilt, target)){
                    rc.build(toBeBuilt, target);
                }
                else if(rc.canBuild(toBeBuilt, rc.getLocation())){
                    rc.build(toBeBuilt, rc.getLocation());
                }

            }
        }
        if(rc.isActionReady()){
            attemptAttack(rc);
            //if(potentialEnemiesAttackRange.length == 0) attemptHealConditional(rc, (int) RETREAT_HEALTH);
        }
        if(enemyRobots.length != 0){
            float healthRatio = (float) totalHealth(enemyRobots) / (totalHealth(allyRobots) + rc.getHealth());
            //MapLocation averageEnemy = Utilities.averageRobotLocation(enemyRobots);
//            if(Utilities.locationIsBehindWall(rc, averageEnemy) && enemyRobotsAttackRange.length == 0 && healthRatio < 2.0f && rc.getHealth() > 150){
//                Pathfinding.combinedPathfinding(rc, averageEnemy);
//                updateInfo(rc);
//                attemptAttack(rc);
//                attemptHeal(rc);
//            }
            /*else*/ if((healthRatio > 2.0f && enemyRobots.length > allyRobots.length + 3)){
                retreat(rc);
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
            else if(healthRatio < 0.25f && (allyRobots.length > enemyRobots.length + 1) || healthRatio < 0.125f){
                //Pathfinding.bellmanFord5x5(rc, lowestHealth(enemyRobots));
                BFSKernel.BFS(rc, lowestHealth(enemyRobots));
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
            //try and move into attack range of any nearby enemies
            else if (((rc.isActionReady() || aggresionIndex > 10) /*|| ((allyRobots.length - enemyRobots.length > 6) && enemyRobotsAttackRange.length == 0)) */&& rc.getHealth() >= RETREAT_HEALTH)){
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
                    if(nearbyFlagsAlly.length == 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target)))){
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(target)));
                    }
                    Pathfinding.combinedPathfinding(rc, target);
                }
            }
            else if(getClosestCluster(rc) != null){
                target = getClosestCluster(rc);
                //Pathfinding.combinedPathfinding(rc, lastSeenEnemy.getLocation());
                if(nearbyFlagsAlly.length == 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target))))
                    rc.fill(rc.getLocation().add(rc.getLocation().directionTo(target)));
                //Pathfinding.bellmanFord5x5(rc, target);
                BFSKernel.BFS(rc,target);
                return;
            }
            else{
                //MapLocation target = findCoordinatedBroadcastFlag(rc);
                target = findClosestBroadcastFlags(rc);
                if(target != null) {
                    if (nearbyFlagsAlly.length == 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(target)))) {
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
                //Pathfinding.bellmanFord5x5(rc, targetFlag.getLocation());
                BFSKernel.BFS(rc, targetFlag.getLocation());
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
            if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(findClosestSpawnLocation(rc))))){
                rc.fill(rc.getLocation().add(rc.getLocation().directionTo(findClosestSpawnLocation(rc))));
            }
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
                float score;
                if(square.enemiesAttackRangedX == 1){
                    score = 1000000 + square.alliesHealRangedX * 0.25f + square.potentialEnemiesAttackRangedX * -5.0f + square.potentialKill.compareTo(false) * 10.0f;
                }
                else if(square.enemiesAttackRangedX == 2){
                    if(square.potentialKill){
                        score = 5000 + square.enemiesVisiondX + square.alliesVisiondX * -1 + square.alliesHealRangedX * 0.25f + square.potentialEnemiesAttackRangedX * -5.5f - (float)square.totalDPS;
                    }
                    else{
                        score = square.enemiesAttackRangedX * 2 * aggresionIndex + square.enemiesVisiondX + square.alliesVisiondX * -1 + square.alliesHealRangedX * 1.25f + square.potentialEnemiesAttackRangedX * -3.65f;
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
        float score;
        for(engagementMicroSquare square : options){
            if(square.passable){
                if((square.enemiesAttackRangedX + square.potentialEnemiesAttackRangedX) * -1 == potentialEnemiesAttackRange.length){
                    score = 15000 + square.alliesHealRangedX * 1.5f + square.alliesVisiondX + square.enemiesVisiondX * 1.25f + square.hasTrap.compareTo(false) + square.potentialEnemiesPrepareAttackdX * 3.25f;
                }
                else if(square.enemiesAttackRangedX * -1 == enemyRobotsAttackRange.length){
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
    public static void retreat(RobotController rc) throws GameActionException {
        engagementMicroSquare[] options = new engagementMicroSquare[8];
        populateMicroArray(rc, options);
        engagementMicroSquare best = null;
        float highScore = Integer.MIN_VALUE;
        for(engagementMicroSquare square : options){
            if(square.passable){
                float score = square.enemiesAttackRangedX * -5.0f + square.enemiesVisiondX * -3.0f + square.alliesVisiondX + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -3 + square.hasTrap.compareTo(false) * 6.0f + square.potentialEnemiesPrepareAttackdX * -1.5f;
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
//\\                continue;
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
                    if(options[index].enemiesAttackRangedX == 2){
                        options[index].totalDPS = (DPSEnemy(enemyRobotsAttackRangeNewLoc[0]) + DPSEnemy(enemyRobotsAttackRangeNewLoc[1]));
                    }
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = (potentialEnemiesAttackRangeNewLoc.length - enemyRobotsAttackRangeNewLoc.length) - (potentialEnemiesAttackRange.length - enemyRobotsAttackRange.length);
                    options[index].potentialEnemiesPrepareAttackdX = (potentialEnemiesPrepareAttackNewLoc.length - potentialEnemiesAttackRangeNewLoc.length) - (potentialEnemiesPrepareAttack.length - potentialEnemiesAttackRange.length);
                    //options[index].potentialDeath = potentialDeath(rc, potentialEnemiesAttackRangeNewLoc);
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
                    if(options[index].enemiesAttackRangedX == 2){
                        options[index].totalDPS = (DPSEnemy(enemyRobotsAttackRangeNewLoc[0]) + DPSEnemy(enemyRobotsAttackRangeNewLoc[1]));
                    }
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = (potentialEnemiesAttackRangeNewLoc.length - enemyRobotsAttackRangeNewLoc.length) - (potentialEnemiesAttackRange.length - enemyRobotsAttackRange.length);
                    options[index].potentialEnemiesPrepareAttackdX = (potentialEnemiesPrepareAttackNewLoc.length - potentialEnemiesAttackRangeNewLoc.length) - (potentialEnemiesPrepareAttack.length - potentialEnemiesAttackRange.length);
                    //options[index].potentialDeath = potentialDeath(rc, potentialEnemiesAttackRangeNewLoc);
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
                    if(options[index].enemiesAttackRangedX == 2){
                        options[index].totalDPS = (DPSEnemy(enemyRobotsAttackRangeNewLoc[0]) + DPSEnemy(enemyRobotsAttackRangeNewLoc[1]));
                    }
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = (potentialEnemiesAttackRangeNewLoc.length - enemyRobotsAttackRangeNewLoc.length) - (potentialEnemiesAttackRange.length - enemyRobotsAttackRange.length);
                    options[index].potentialEnemiesPrepareAttackdX = (potentialEnemiesPrepareAttackNewLoc.length - potentialEnemiesAttackRangeNewLoc.length) - (potentialEnemiesPrepareAttack.length - potentialEnemiesAttackRange.length);
                    //options[index].potentialDeath = potentialDeath(rc, potentialEnemiesAttackRangeNewLoc);
                    //options[index].totalHealthAlliesdX = totalHealth(allyRobotsNewLoc) - totalHealth(allyRobots);
                    //options[index].totalHealthEnemiesdX = totalHealth(enemyRobotsNewLoc) - totalHealth(enemyRobots);
                }
            } else {
                options[index] = new engagementMicroSquare(false);
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
    public static boolean potentialDeath(RobotController rc, RobotInfo[] enemies){
        int i = rc.getHealth();
        double totalDPS = 0;
        for(RobotInfo r : enemies){
            totalDPS += DPSEnemy(r);
        }
        return i <= totalDPS;
    }

    public static void attemptAttack(RobotController rc) throws GameActionException {
        if(rc.readSharedArray(53) != 0 && rc.canAttack(Utilities.convertIntToLocation(rc.readSharedArray(53)))){
            rc.attack(Utilities.convertIntToLocation(rc.readSharedArray(53)));
            rc.setIndicatorLine(rc.getLocation(), Utilities.convertIntToLocation(rc.readSharedArray(53)), 100, 200, 100);
            if(rc.senseRobotAtLocation(Utilities.convertIntToLocation(rc.readSharedArray(53))) == null){
                rc.writeSharedArray(53, 0);
            }
            return;
        }
        else if(rc.readSharedArray(54) != 0 && rc.canAttack(Utilities.convertIntToLocation(rc.readSharedArray(54)))){
            rc.attack(Utilities.convertIntToLocation(rc.readSharedArray(54)));
            //rc.setIndicatorLine(rc.getLocation(), Utilities.convertIntToLocation(rc.readSharedArray(54)), 100, 200, 100);
            //System.out.println("Attacked: " + Utilities.convertIntToLocation(rc.readSharedArray(54)));
            if(rc.senseRobotAtLocation(Utilities.convertIntToLocation(rc.readSharedArray(54))) == null){
                rc.writeSharedArray(54, 0);
            }
            return;
        }
        MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
        if (toAttack != null && rc.canAttack(toAttack)) {
            if(enemyRobots.length + allyRobots.length >= 15 && enemyRobots.length > 5) {
                if(rc.readSharedArray(53) == 0) {
                    rc.writeSharedArray(53, Utilities.convertLocationToInt(toAttack));
                    //rc.setIndicatorDot(toAttack, 255, 100, 50);
                    //System.out.println("Everyone get: " + toAttack);
                }
                else if(rc.readSharedArray(54) == 0 && rc.getLocation().distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(53))) > 15){
                    rc.writeSharedArray(54, Utilities.convertLocationToInt(toAttack));
                    //rc.setIndicatorDot(toAttack, 100, 255, 50);
                    //System.out.println("Everyone get: " + toAttack);
                }
            }
            if (rc.senseRobotAtLocation(toAttack).health <= rc.getAttackDamage())
                Utilities.addKillToKillsArray(rc, turnsWithKills);
            rc.attack(toAttack);
        }
        if(rc.isActionReady()){
            if (toAttack != null && rc.canAttack(toAttack)) {
                if (rc.senseRobotAtLocation(toAttack).health <= rc.getAttackDamage())
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
//        if(potentialEnemiesPrepareAttack.length != 0 && rc.getLevel(SkillType.HEAL) == 3 && rc.getLevel(SkillType.ATTACK) < 4)
//            return;
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
    public static boolean isTrapAdjacentSpawn(RobotController rc, MapLocation location, TrapType t) throws GameActionException {
        MapInfo[] adjacents = rc.senseNearbyMapInfos(location, 2);
        for(MapInfo square : adjacents){
            if(square.getMapLocation().equals(rc.getLocation())) continue;
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
    //    public static void updateRetreatHealth(RobotController rc){
//        if(rc.getRoundNum() <= 905)
//            RETREAT_HEALTH += .03f;
//        if(!enemyAttackUpgrade) {
//            GlobalUpgrade[] upgrades = rc.getGlobalUpgrades(rc.getTeam().opponent());
//            for (GlobalUpgrade g : upgrades) {
//                if (g == GlobalUpgrade.ATTACK) {
//                    enemyAttackUpgrade = true;
//                    RETREAT_HEALTH += 60;
//                }
//            }
//        }
//    }
    public static void determineRetreatHealth(RobotController rc){
        if(!enemyAttackUpgrade){
            GlobalUpgrade[] g = rc.getGlobalUpgrades(rc.getTeam().opponent());
            for(GlobalUpgrade upgrade : g){
                if(upgrade == GlobalUpgrade.ATTACK){
                    enemyAttackUpgrade = true;
                    break;
                }
            }
        }
        int high = 0;
        if(potentialEnemiesPrepareAttack.length != 0){
            for(RobotInfo unit : potentialEnemiesPrepareAttack){
                double dps = DPSEnemy(unit);
                if(dps > high) high = (int) dps;
            }
            RETREAT_HEALTH = high;
        }
        else return;
    }
    public static double DPSAlly(RobotInfo unit, RobotController rc){
        int base = (rc.getRoundNum() >= 600) ? 210 : 150;
        switch(unit.attackLevel){
            case 0:
                return base;
            case 1:
                return base + base * 0.05;
            case 2:
                return base + base * 0.07;
            case 3:
                return base + base * 0.1;
            case 4:
                return base + base * 0.3;
            case 5:
                return base + base * 0.35;
            case 6:
                return base + base * 0.6;
            default:
                return base;
        }
    }

    public static double DPSEnemy(RobotInfo unit){
        int base = (Soldier.enemyAttackUpgrade) ? 210 : 150;
        switch(unit.attackLevel){
            case 0:
                return base;
            case 1:
                return base + base * 0.05;
            case 2:
                return base + base * 0.07;
            case 3:
                return base + base * 0.1;
            case 4:
                return base + base * 0.3;
            case 5:
                return base + base * 0.35;
            case 6:
                return base + base * 0.6;
            default:
                return base;
        }
    }
}

//used to keep track of the eight squares around a robot during engagement, only considers relevant details
//note - passability only takes into account if this robot can move there, so a square can be impassable due
//to either terrain, or just a robot being there, makes no difference to us - false passability could also indicate that
//the location is not on the map
class engagementMicroSquare {
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
    public double totalDPS;
    public Boolean potentialDeath;

    public engagementMicroSquare() {

    }

    public engagementMicroSquare(boolean p) {
        passable = p;
        hasTrap = false;
    }

    public engagementMicroSquare(int x, int y) {
        location = new MapLocation(x, y);
        hasTrap = false;
    }

    public engagementMicroSquare(boolean passability, int x, int y) {
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

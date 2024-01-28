package Version16;

import battlecode.common.*;
import static Version16.Utilities.*;
import static Version16.RobotPlayer.*;


enum states{
    defense, attack, heist, escort, flagCarrier
}
public class Soldier
{
    static Micro micro = null;
    //might be better to switching to using more variables up here, can be accessed/changed across various states - also persist across turns
    static RobotInfo[] enemyRobots;
    static RobotInfo[] enemyRobotsAttackRange;
    static RobotInfo[] allyRobots;
    static RobotInfo[] allyRobotsHealRange;
    //looks at enemies in a radius of sqrt(10), the radius from which any enemy can move and then attack you in the same turn
    //static RobotInfo[] potentialEnemiesAttackRange;
    //looks at enemies in a radius of sqrt(16), the enemies that we could attack in two moves
    //static RobotInfo[] potentialEnemiesPrepareAttack;
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
        if(micro == null){
            micro = new Micro(rc);
        }
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
        findCoordinatedActualFlag(rc);
        //used to update which flags we know are real or not
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
            if (flag.isPickedUp() || (rc.canSenseLocation(flag.getLocation()) && rc.senseMapInfo(flag.getLocation()).getSpawnZoneTeamObject() != rc.getTeam()))
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
        //potentialEnemiesAttackRange = rc.senseNearbyRobots(10, rc.getTeam().opponent());
        //potentialEnemiesPrepareAttack = rc.senseNearbyRobots(16, rc.getTeam().opponent());
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
            if(rc.isMovementReady()) micro.doMicro();//runMicroAttack(rc);
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
        }
        if(enemyRobots.length != 0){
            micro.doMicro();
            updateInfo(rc);
            if(rc.isActionReady()){
                attemptAttack(rc);
                attemptHeal(rc);
            }
//            float healthRatio = (float) totalHealth(enemyRobots) / (totalHealth(allyRobots) + rc.getHealth());
//            /*else*/ if((healthRatio > 2.0f && enemyRobots.length > allyRobots.length + 3)){
//                retreat(rc);
//                updateInfo(rc);
//                attemptAttack(rc);
//                attemptHeal(rc);
//            }
//            else if(healthRatio < 0.25f && (allyRobots.length > enemyRobots.length + 1) || healthRatio < 0.125f){
//                //Pathfinding.bellmanFord5x5(rc, lowestHealth(enemyRobots));
//                BFSKernel.BFS(rc, lowestHealth(enemyRobots));
//                updateInfo(rc);
//                attemptAttack(rc);
//                attemptHeal(rc);
//            }
//            //try and move into attack range of any nearby enemies
//            else if (((rc.isActionReady() || aggresionIndex > 10) /*|| ((allyRobots.length - enemyRobots.length > 6) && enemyRobotsAttackRange.length == 0)) */&& rc.getHealth() >= RETREAT_HEALTH)){
//                runMicroAttack(rc);
//                updateInfo(rc);
//                attemptAttack(rc);
//                attemptHeal(rc);
//            }
//            //try and kite backwards, wait for cooldown to refresh
//            else {
//                runMicroKite(rc);
//                updateInfo(rc);
//                attemptAttack(rc);
//                attemptHeal(rc);
//            }
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
                target = getClosestCluster(rc).location;
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
                micro.doMicro();
                updateInfo(rc);
                attemptAttack(rc);
                attemptHeal(rc);
            }
//            else{
//                runMicroKite(rc);
//                updateInfo(rc);
//                attemptAttack(rc);
//                attemptHeal(rc);
//            }
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
                //runMicroAttack(rc);
                micro.doMicro();
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
                //runMicroKite(rc);
                micro.doMicro();
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
    public static void updateRetreatHealth(RobotController rc){
        if(rc.getRoundNum() <= 905)
            RETREAT_HEALTH += .03f;
        if(!enemyAttackUpgrade) {
            GlobalUpgrade[] upgrades = rc.getGlobalUpgrades(rc.getTeam().opponent());
            for (GlobalUpgrade g : upgrades) {
                if (g == GlobalUpgrade.ATTACK) {
                    enemyAttackUpgrade = true;
                    RETREAT_HEALTH += 60;
                }
            }
        }
    }
}


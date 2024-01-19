package Version10SoldierStates;

import battlecode.common.*;

import static Version10SoldierStates.RobotPlayer.findCoordinatedBroadcastFlag;
import static Version10SoldierStates.RobotPlayer.lowestHealth;
import static Version10SoldierStates.Utilities.averageRobotLocation;
import static Version10SoldierStates.Utilities.bestHeal;

public class Soldier
{
    //higher means more likely to help
    public static final float STOLEN_FLAG_CONSTANT = 1.0f;
    public static void runSoldier(RobotController rc) throws GameActionException {
        if (!rc.isSpawned())
            return;
        //update where we want soldiers to spawn
        if(!Utilities.readBitSharedArray(rc, 1021)){
            int x;
            if(knowFlag(rc))
                x = RobotPlayer.findClosestSpawnLocationToCoordinatedTarget(rc);
            else {
                x = RobotPlayer.findClosestSpawnLocationToCoordinatedBroadcast(rc);
            }
            if (x != -1){
                //00
                if(x == 0 && (Utilities.readBitSharedArray(rc, 1023) || Utilities.readBitSharedArray(rc, 1023))){
                    Utilities.editBitSharedArray(rc, 1023, false);
                    Utilities.editBitSharedArray(rc, 1022, false);
                }
                //01
                else if(x == 1 && (!Utilities.readBitSharedArray(rc, 1023) || Utilities.readBitSharedArray(rc, 1023))){
                    Utilities.editBitSharedArray(rc, 1022, false);
                    Utilities.editBitSharedArray(rc, 1023, true);
                }
                //x == 2, desire 10
                else if (x == 2 && (Utilities.readBitSharedArray(rc, 1023) || !Utilities.readBitSharedArray(rc, 1023))){
                    Utilities.editBitSharedArray(rc, 1023, false);
                    Utilities.editBitSharedArray(rc, 1022, true);
                }
            }
        }
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
            Pathfinding.bugNav2(rc, targetCrumb);
        }
        //dont you dare move
        if(rc.getRoundNum() >= 200 && rc.getRoundNum() <= 205){
            //try and attack the best target
            MapLocation toAttack = lowestHealth(rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent()));
            if(toAttack != null && rc.canAttack(toAttack)){
                rc.attack(toAttack);
            }
            //try to heal if nothing else can be done
            RobotInfo toHeal = bestHeal(rc, rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam()));
            if(toHeal != null && rc.canHeal(toHeal.getLocation())){
                rc.heal(toHeal.getLocation());
            }
        }
        else {

            //support any flag heist or defense
            StolenFlag closestFlag = Utilities.getClosestFlag(rc);
            //make this change based on the map
            if (closestFlag != null && rc.getLocation().distanceSquaredTo(closestFlag.location) < (rc.getMapWidth() + rc.getMapHeight()) * STOLEN_FLAG_CONSTANT)
            {
                    Pathfinding.tryToMoveTowardsFlag(rc, closestFlag.location, closestFlag);
                    rc.setIndicatorString("Helping teammate @ " + closestFlag.location);
            }
            if(rc.senseNearbyFlags(-1, rc.getTeam().opponent()).length != 0){
                checkRecordEnemyFlag(rc, rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0]);
                //store location just in case we move out of vision radius before second part
                MapLocation loc = rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation();
                //move towards the flag
                Pathfinding.tryToMove(rc, loc);
                //try to pick up that flag
                if (rc.canPickupFlag(loc) && rc.getRoundNum() > GameConstants.SETUP_ROUNDS) {
                    rc.pickupFlag(loc);
                }
            }
            //if you have the flag, just run back, and maybe fill in water on the way
            if (rc.hasFlag()) {
                Carrier.runCarrier(rc);
            }
            //take in important info around you
            RobotInfo[] enemyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
            RobotInfo[] enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
            RobotInfo[] allyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam());
            RobotInfo[] allyRobotsHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
            MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
            if(toAttack != null && rc.canAttack(toAttack)){
                rc.attack(toAttack);
            }
            //potentially attack again
            if(rc.isActionReady()){
                toAttack = lowestHealth(enemyRobotsAttackRange);
                if(toAttack != null && rc.canAttack(toAttack)){
                    rc.attack(toAttack);
                }
            }

            RobotInfo toHeal = bestHeal(rc, allyRobotsHealRange);
            //immediately try to heal your ally if they have a flag
            if (toHeal != null && toHeal.hasFlag() && rc.canHeal(toHeal.getLocation())) {
                rc.heal(toHeal.getLocation());
            }
            //high density area, try and place a bomb! - place forward if possible, but if its super high density we'll settle for placing on ourselves
            if(enemyRobots.length > 5 && enemyRobotsAttackRange.length >= 1){
                if(rc.canBuild(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
                    rc.build(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
                    System.out.println("I built a bomb!: " + rc.getLocation());
                }
            }
            if(rc.isActionReady() && enemyRobots.length > 6 && enemyRobotsAttackRange.length >= 3){
                if(rc.canBuild(TrapType.STUN, rc.getLocation())){
                    rc.build(TrapType.STUN, rc.getLocation());
                    System.out.println("I built a high density bomb!: " + rc.getLocation());
                }
            }


            //if we have more allies, or equal allies, to amount of enemies, and havent attacked yet, lets be aggressive
            //note: would enemyRobots.length == 0 be true when allyRobots.length >= (enemyRobots.length - 1)
            if (allyRobots.length >= (enemyRobots.length - 1) && (rc.isActionReady() || enemyRobots.length == 0) && rc.getHealth() > 50) {
                //can sense an enemy flag - move towards the flag!
                if (rc.senseNearbyFlags(-1, rc.getTeam().opponent()).length != 0) {
                    if (rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation()))))
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation())));
                    if (rc.isMovementReady())
                        Pathfinding.tryToMove(rc, rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation());
                }
                //otherwise, if we can see enemies, just move towards their average location
                else if (enemyRobots.length != 0 && enemyRobotsAttackRange.length == 0) {
                    MapLocation toChase = lowestHealth(enemyRobots);
                    if(rc.senseNearbyRobots(toChase, -1,rc.getTeam().opponent()).length <= rc.senseNearbyRobots(toChase, -1, rc.getTeam()).length)
                        if(rc.isMovementReady()) Pathfinding.tryToMove(rc, toChase);
                    else
                        if (rc.isMovementReady()) Pathfinding.tryToMove(rc, averageRobotLocation(enemyRobots));
                }
                //we know of at least one flag location, so lets move towards that
                else if(knowFlag(rc)){
                    MapLocation target = findCoordinatedActualFlag(rc);
                    if(target != null){
                        Pathfinding.bugNav2(rc, target);
                    }
                }
                //finally, we cant see enemies or a flag, so lets move towawrds closest broadcast location!
                else {
                    //will be used for a variety of different movement goals
                    MapLocation target = findCoordinatedBroadcastFlag(rc);
                    if (enemyRobots.length == 0 && target != null && rc.canFill(rc.adjacentLocation(rc.getLocation().directionTo(target)))) {
                        rc.fill(rc.adjacentLocation(rc.getLocation().directionTo(target)));
                    }
                    MapLocation targetBroadcast = findCoordinatedBroadcastFlag(rc);
                    if(rc.isMovementReady()) {
                        //note: what is empty if for?
                        if (rc.getLocation().equals(targetBroadcast)) {
                        }
                        else Pathfinding.bugNav2(rc, findCoordinatedBroadcastFlag(rc));
                    }
                }
            }
            //there are enemies than allies, or we've already attacked this turn
            else {
                if(rc.isMovementReady()) {
                    //lets try to lure them into traps
                    MapLocation target = Builder.getAverageTrapLocation(rc, rc.senseNearbyMapInfos(-1));
                    if (target != null) {
                        Pathfinding.tryToMove(rc, target);
                    }
                    //otherwise, move towards opposite of average of enemies
                    else if (enemyRobots.length != 0) {
                            Pathfinding.tryToMove(rc, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots)).opposite()));
                    }
                    //if we can see any allies, move towards their average location
                    else if (allyRobots.length != 0) {
                        Pathfinding.tryToMove(rc, averageRobotLocation(allyRobots));
                    }
                }
            }
            if (rc.isActionReady()) {
                //now, we try to attack again - re-sense robots because we've probably moved
                toAttack = lowestHealth(rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent()));
                if(toAttack != null && rc.canAttack(toAttack)){
                    rc.attack(toAttack);
                }
                //potentially attack again
                if(rc.isActionReady()){
                    toAttack = lowestHealth(rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent()));
                    if(toAttack != null && rc.canAttack(toAttack)){
                        rc.attack(toAttack);
                    }
                }
                //finally, we try to heal if nothing else can be done
                toHeal = bestHeal(rc, rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam()));
                if (toHeal != null && rc.canHeal(toHeal.getLocation())) {
                    rc.heal(toHeal.getLocation());
                }
            }
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

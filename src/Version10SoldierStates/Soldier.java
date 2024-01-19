package Version10SoldierStates;

import battlecode.common.*;

import static Version10SoldierStates.RobotPlayer.findCoordinatedBroadcastFlag;
import static Version10SoldierStates.RobotPlayer.lowestHealth;
import static Version10SoldierStates.Utilities.averageRobotLocation;
import static Version10SoldierStates.Utilities.bestHeal;

enum states{
    defense, attack, heist, escort, flagCarrier
}
public class Soldier
{
    //higher means more likely to help
    public static final float STOLEN_FLAG_CONSTANT = 1.0f;
    static states state;
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
                    Utilities.editBitSharedArray(rc, 1023, true); }
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
        //take in important info around you
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
        RobotInfo[] enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
        RobotInfo[] allyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam());
        RobotInfo[] allyRobotsHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
        if(rc.hasFlag())
            state = states.flagCarrier;
        if(enemyRobots.length > 0)
            state = states.attack;
        //if flag stolen/ enemy attacking flag state = defense
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
        }
    }
    public static void defense(RobotController rc)
    {

    }
    public static void attack(RobotController rc) throws GameActionException{
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
        RobotInfo[] enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
        RobotInfo[] allyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam());
        RobotInfo[] allyRobotsHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
        if (allyRobots.length > enemyRobots.length - 1 && enemyRobots.length == 0) {
            //can sense an enemy flag - move towards the flag!
            if (rc.senseNearbyFlags(-1, rc.getTeam().opponent()).length != 0) {
                if (rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation()))))
                    rc.fill(rc.getLocation().add(rc.getLocation().directionTo(rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation())));
                if (rc.isMovementReady())
                    Pathfinding.tryToMove(rc, rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation());
            }
            //otherwise, if we can see enemies, just move towards their average location
            else if (enemyRobots.length != 0 && enemyRobotsAttackRange.length == 0) {
                MapLocation toChase = RobotPlayer.lowestHealth(enemyRobots);
                if (rc.senseNearbyRobots(toChase, -1, rc.getTeam().opponent()).length <= rc.senseNearbyRobots(toChase, -1, rc.getTeam()).length)
                    if (rc.isMovementReady()) Pathfinding.tryToMove(rc, toChase);
                    else if (rc.isMovementReady())
                        Pathfinding.tryToMove(rc, Utilities.averageRobotLocation(enemyRobots));
            }
            //finally, we cant see enemies or a flag, so lets move towawrds closest broadcast location!
            else {
                //will be used for a variety of different movement goals
                MapLocation target = RobotPlayer.findCoordinatedBroadcastFlag(rc);
                if (enemyRobots.length == 0 && target != null && rc.canFill(rc.adjacentLocation(rc.getLocation().directionTo(target)))) {
                    rc.fill(rc.adjacentLocation(rc.getLocation().directionTo(target)));
                }
                MapLocation targetBroadcast = RobotPlayer.findCoordinatedBroadcastFlag(rc);
                if (rc.isMovementReady()) {
                    if (rc.getLocation().equals(targetBroadcast)) {
                    } else Pathfinding.bugNav2(rc, RobotPlayer.findCoordinatedBroadcastFlag(rc));
                }
            }
            if (rc.isActionReady()) {
                //now, we try to attack again - re-sense robots because we've probably moved
                MapLocation toAttack = RobotPlayer.lowestHealth(rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent()));
                if (toAttack != null && rc.canAttack(toAttack)) {
                    rc.attack(toAttack);
                }
            }
            //finally, we try to heal if nothing else can be done
            RobotInfo toHeal = Utilities.bestHeal(rc, rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam()));
            if (toHeal != null && rc.canHeal(toHeal.getLocation())) {
                rc.heal(toHeal.getLocation());
            }
        }
        else {
            if (allyRobots.length != 0) {
                Pathfinding.tryToMove(rc, Utilities.averageRobotLocation(allyRobots));
            }
        }
    }
    public static void heist(RobotController rc)
    {

    }
    public static void escort(RobotController rc)
    {

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

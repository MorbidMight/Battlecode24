package Version9;

import battlecode.common.*;

import static Version9.RobotPlayer.*;
import static Version9.Utilities.averageRobotLocation;
import static Version9.Utilities.bestHeal;

public class Soldier
{
    public static void runSoldier(RobotController rc) throws GameActionException {
        if (!rc.isSpawned())
            return;
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
            if (closestFlag != null) {
                Pathfinding.tryToMoveTowardsFlag(rc, closestFlag.location, closestFlag);
                rc.setIndicatorString("Helping teammate @ " + closestFlag.location);
            }
            if(rc.senseNearbyFlags(-1, rc.getTeam().opponent()).length != 0){
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
            RobotInfo toHeal = bestHeal(rc, allyRobotsHealRange);
            //immediately try to heal your ally if they have a flag
            if (toHeal != null && toHeal.hasFlag() && rc.canHeal(toHeal.getLocation())) {
                rc.heal(toHeal.getLocation());
            }
            //high density area, try and place a bomb! - place forward if possible, but if its super high density we'll settle for placing on ourselves
            if(enemyRobots.length > 6 && enemyRobotsAttackRange.length >= 1){
                if(rc.canBuild(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
                    rc.build(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
                    System.out.println("I built a bomb");
                }
            }
            if(rc.isActionReady() && enemyRobots.length > 6 && enemyRobotsAttackRange.length >= 3){
                if(rc.canBuild(TrapType.STUN, rc.getLocation())){
                    rc.build(TrapType.STUN, rc.getLocation());
                    System.out.println("I built a bomb");
                }
            }
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

            //if we have more allies, or equal allies, to amount of enemies, and havent attacked yet, lets be aggressive
            if (allyRobots.length >= enemyRobots.length && (rc.isActionReady() || enemyRobots.length == 0) && rc.getHealth() > 100) {
                //can sense an enemy flag - move towards the flag!
                if (rc.senseNearbyFlags(-1, rc.getTeam().opponent()).length != 0) {
                    if (rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation()))))
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation())));
                    if (rc.isMovementReady())
                        Pathfinding.tryToMove(rc, rc.senseNearbyFlags(-1, rc.getTeam().opponent())[0].getLocation());
                }
                //otherwise, if we can see enemies, just move towards their average location
                else if (enemyRobots.length != 0 && enemyRobotsAttackRange.length == 0) {
                    if (rc.isMovementReady()) Pathfinding.tryToMove(rc, averageRobotLocation(enemyRobots));
                }
                //finally, we cant see enemies or a flag, so lets move towawrds closest broadcast location!
                else {
                    //if (rc.isMovementReady()) Pathfinding.tryToMove(rc, findClosestBroadcastFlags(rc));
                    MapLocation target = findCoordinatedBroadcastFlag(rc);
                    if (enemyRobots.length == 0 && target != null && rc.canFill(rc.adjacentLocation(rc.getLocation().directionTo(target)))) {
                        rc.fill(rc.adjacentLocation(rc.getLocation().directionTo(target)));
                    }
                    if (rc.isMovementReady()) Pathfinding.bugNav2(rc, findCoordinatedBroadcastFlag(rc));
                }
            }
            //there are enemies than allies, or we've already attacked this turn
            else {
                //if we can see any allies, move towards their average location
                if (allyRobots.length != 0) {
                    if (rc.isMovementReady()) Pathfinding.tryToMove(rc, averageRobotLocation(allyRobots));
                }
                //otherwise, move towards opposite of average of enemies
                else if (enemyRobots.length != 0) {
                    if (rc.isMovementReady()) Pathfinding.tryToMove(rc, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots)).opposite()));
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
}

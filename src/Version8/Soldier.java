package Version8;

import battlecode.common.*;

import static Version8.RobotPlayer.*;
import static Version8.Utilities.averageRobotLocation;
import static Version8.Utilities.bestHeal;

public class Soldier
{
    public static void runSoldier(RobotController rc) throws GameActionException {
        if (!rc.isSpawned())
            Clock.yield();
        //tries to get neary crumbs
        MapLocation[] nearbyCrumbs = rc.senseNearbyCrumbs(-1);
        MapLocation targetCrumb = null;
        if (nearbyCrumbs.length > 0)
            targetCrumb = Explorer.chooseTargetCrumb(rc, nearbyCrumbs);
        if (targetCrumb != null) {
            MapInfo targetLoc = rc.senseMapInfo(targetCrumb);
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
            if (rc.readSharedArray(58) != 0) {
                Pathfinding.tryToMove(rc, Utilities.convertIntToLocation(rc.readSharedArray(58)));
                rc.setIndicatorString("Helping teammate @ " + Utilities.convertIntToLocation(rc.readSharedArray(58)));
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
            //high density area, try and place a bomb!
            if(enemyRobots.length > 6 && enemyRobotsAttackRange.length >= 1){
                if(rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))))){
                    rc.build(TrapType.EXPLOSIVE, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots))));
                    System.out.println("I built a bomb!");
                }
            }
            //try and attack the best target
            MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
            if (toAttack != null && rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }

            //if we have more allies, or equal allies, to amount of enemies, and havent attacked yet, lets be aggressive
            if (allyRobots.length >= enemyRobots.length && rc.getActionCooldownTurns() < 10 && rc.getHealth() > 100) {
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
                    if (rc.isMovementReady()) Pathfinding.tryToMove(rc, findCoordinatedBroadcastFlag(rc));
                }
            }
            //there are enemies than allies, or we've already attacked this turn
            else {
                //try to lure them into our traps - need uravs trap thing
//                MapLocation destination = Builder.getAverageTrapLocation(rc, rc.senseNearbyMapInfos(-1));
//                if(destination != null){
//                    if(rc.isMovementReady()) Pathfinding.tryToMove(rc, destination);
//                }
                //if we can see any allies, move towards their average location
                if (allyRobots.length != 0) {
                    if (rc.isMovementReady()) Pathfinding.tryToMove(rc, averageRobotLocation(allyRobots));
                }
                //otherwise, move towards opposite of average of enemies
                else if (enemyRobots.length != 0) {
                    if (rc.isMovementReady())
                        Pathfinding.tryToMove(rc, rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots)).opposite()));
                }
            }
            //now, we try to attack again - re-sense robots because we've probably moved
            if (rc.getActionCooldownTurns() < 10) {
                toAttack = lowestHealth(rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent()));
                if (toAttack != null && rc.canAttack(toAttack)) {
                    rc.attack(toAttack);
                }
                //finally, we try to heal if nothing else can be done
                toHeal = bestHeal(rc, rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam()));
                if (toHeal != null && rc.canHeal(toHeal.getLocation())) {
                    rc.heal(toHeal.getLocation());
                }
            }
        }
        //old soldier code
        //tries to get neary crumbs
//        MapLocation[] nearbyCrumbs = rc.senseNearbyCrumbs(-1);
//        MapLocation targetCrumb = null;
//        if (nearbyCrumbs.length > 0)
//            targetCrumb = Explorer.chooseTargetCrumb(rc, nearbyCrumbs);
//        if (targetCrumb != null) {
//            MapInfo targetLoc = rc.senseMapInfo(targetCrumb);
//            //check if crumb is on water
//            if (!targetLoc.isPassable() && rc.canFill(targetCrumb)) {
//                rc.fill(targetCrumb);
//            }
//            Pathfinding.bugNav2(rc, targetCrumb);
//        }
//        //blank declaration, will be set by something
//        Direction dir; // = Pathfinding.basicPathfinding(rc, new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2), false);
//        //try to move towards any seen opponent flags
//        if(rc.readSharedArray(58) != 0)
//        {
//            Pathfinding.tryToMove(rc, Utilities.convertIntToLocation(rc.readSharedArray(58)));
//            rc.setIndicatorString("Helping teammate @ " + Utilities.convertIntToLocation(rc.readSharedArray(58)));
//        }
//        FlagInfo[] nearbyOppFlags = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
//        if(nearbyOppFlags.length != 0 && !nearbyOppFlags[0].isPickedUp()){
//            if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(nearbyOppFlags[0].getLocation()))))
//                rc.fill(rc.getLocation().add(rc.getLocation().directionTo(nearbyOppFlags[0].getLocation())));
//            Pathfinding.bugNav2(rc, nearbyOppFlags[0].getLocation());
//        }
//        //pickup enemy flag if we can
//        if (rc.canPickupFlag(rc.getLocation()) && rc.getRoundNum() > GameConstants.SETUP_ROUNDS) {
//            rc.pickupFlag(rc.getLocation());
//        }
//        if (rc.hasFlag()) {
//            MapLocation closestSpawnLoc = findClosestSpawnLocation(rc);
//            Direction d = rc.getLocation().directionTo(closestSpawnLoc);
//            if(rc.canFill(rc.getLocation().add(d))){
//                rc.fill(rc.getLocation().add(d));
//            }
//            rc.setIndicatorString("moving flag");
//            Pathfinding.bugNav2(rc, closestSpawnLoc);
//        }
//
//        //attack
//        RobotInfo[] enemyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
//        RobotInfo[] enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
//        RobotInfo[] allyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam());
//        RobotInfo[] allyRobotsHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
//        RobotInfo toHeal = bestHeal(rc, allyRobotsHealRange);
//        //immediately try to heal if they have a flag
//        if(toHeal != null && toHeal.hasFlag() && rc.canHeal(toHeal.getLocation())){
//            rc.heal(toHeal.getLocation());
//        }
//        //no enemy in view
//        if (enemyRobots.length == 0) {
//            //Task System/ Broadcast locations
//            MapLocation closestBroadcasted = findClosestBroadcastFlags(rc);
//            if (closestBroadcasted != null) {
//                if(rc.isMovementReady()) Pathfinding.bugNav2(rc, closestBroadcasted);
//            }
//            if(allyRobotsHealRange.length > 0)
//            {
//                //Heal Allies if possible
//                if(toHeal != null && rc.canHeal(toHeal.getLocation())){
//                    rc.heal(toHeal.getLocation());
//                }
//                if(rc.isMovementReady()) Pathfinding.bugNav2(rc, Utilities.newGetClosestEnemy(rc));
//            }
//        }
//        //enemy in view
//        else {
//            //More Enemies
//            if (enemyRobots.length < allyRobots.length) {
//                //Can Attack
//                MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
//                if (toAttack != null && rc.canAttack(toAttack))
//                {
//                    rc.attack(toAttack);
//
//                }
//                //Can't Attack
//                else {
//                    //Move
//                    //Task System/ Broadcast locations
//                    MapLocation closestBroadcasted = findClosestBroadcastFlags(rc);
//                    if (closestBroadcasted != null) {
//                        if(rc.isMovementReady()) Pathfinding.bugNav2(rc, closestBroadcasted);
//                    }
//                    //Can Heal
//                    if (allyRobotsHealRange.length > 0) {
//                        if(toHeal != null && rc.canHeal(toHeal.getLocation())){
//                            rc.heal(toHeal.getLocation());
//                        }
//                    }
//                }
//            }
//            //Less Enemies
//            else
//            {
//                MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
//                if (toAttack != null && rc.canAttack(toAttack))
//                    rc.attack(toAttack);
//                if(toAttack != null)
//                    Pathfinding.tryToMove(rc,toAttack);
//            }
//        }
//        }
    }
}

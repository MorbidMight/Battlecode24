package Version3;

import battlecode.common.*;

import static Version2.RobotPlayer.*;

public class Soldier
{
    public static void runSoldier(RobotController rc) throws GameActionException {
        rc.setIndicatorString("Soldier code");
        //blank declaration, will be set by something
        Direction dir; // = Pathfinding.basicPathfinding(rc, new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2), false);
        //if we have an enemy flag, bring it to the closest area
        MapLocation closestSpawnLoc = findClosestSpawnLocation(rc);
        if (closestSpawnLoc != null) {
            dir = rc.getLocation().directionTo(closestSpawnLoc);
            if (rc.canMove(dir)) {
                rc.setIndicatorString("moving flag");
                rc.move(dir);
            }

            //pickup enemy flag if we can
            if (rc.canPickupFlag(rc.getLocation()) && rc.getRoundNum() > GameConstants.SETUP_ROUNDS) {
                rc.pickupFlag(rc.getLocation());
            }
        }
        //attack
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
        RobotInfo[] enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
        RobotInfo[] allyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam());
        RobotInfo[] allyRobotsHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
        //no enemy in view
        if (enemyRobots.length == 0) {
            //Task System/ Broadcast locations
            MapLocation closestBroadcasted = findClosestBroadcastFlags(rc);
            if (closestBroadcasted != null) {
                rc.setIndicatorString("going to broadcast location");
                Pathfinding.tryToMove(rc, closestBroadcasted);
            }
            if(allyRobotsHealRange.length > 0)
            {
                rc.setIndicatorString("trying to heal");
                //Heal Allies if possible
                for (RobotInfo allyRobot : allyRobots) {
                    if (rc.canHeal(allyRobot.getLocation())) {
                        rc.heal(allyRobot.getLocation());
                        break;
                    }
                }
                Pathfinding.tryToMove(rc, Utilities.newGetClosestEnemy(rc));
            }
        }
        //enemy in view
        else {
            //More Enemies
            if (enemyRobots.length < allyRobots.length) {
                //Can Attack
                MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
                if (toAttack != null && rc.canAttack(toAttack))
                {
                    rc.setIndicatorString("attack when there are more enemies");
                    rc.attack(toAttack);

                }
                //Can't Attack
                else {
                    //Move
                    //Task System/ Broadcast locations
                    MapLocation closestBroadcasted = findClosestBroadcastFlags(rc);
                    rc.setIndicatorString("go to broadcast location when there are no enemies to attack ");
                    if (closestBroadcasted != null) {
                        Pathfinding.tryToMove(rc, closestBroadcasted);
                    }
                    //Can Heal
                    if (allyRobotsHealRange.length > 0) {
                        rc.setIndicatorString("try to heal allies if no enemies to attack");
                        for (RobotInfo allyRobot : allyRobots) {
                            if (rc.canHeal(allyRobot.getLocation())) {
                                rc.heal(allyRobot.getLocation());
                                break;
                            }
                        }
                    }
                }
            }
            //Less Enemies
            else
            {
                rc.setIndicatorString("try to attack/ move forward when there are more allies than enemies");
                MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
                if(rc.getLocation() != null && toAttack != null && rc.canMove(rc.getLocation().directionTo(toAttack)))
                    rc.move(rc.getLocation().directionTo(toAttack));
                if (toAttack != null && rc.canAttack(toAttack))
                    rc.attack(toAttack);
            }
        }
        /*
        if (enemyRobots.length > allyRobots.length)
        {
            MapLocation toAttack = lowestHealth(enemyRobots);
            if(rc.canAttack(toAttack))
                rc.attack(toAttack);
        }
        if(enemyRobots.length == 0 && allyRobots.length > 0)
        {
            for (RobotInfo allyRobot : allyRobots) {
                if (rc.canHeal(allyRobot.getLocation())) {
                    rc.heal(allyRobot.getLocation());
                    break;
                }
            }
        }
         */
    }
}

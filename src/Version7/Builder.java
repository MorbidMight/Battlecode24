package Version7;

import battlecode.common.*;

import static Version7.RobotPlayer.*;

public class Builder {
    public static void runBuilder(RobotController rc) throws GameActionException {
        if (turnCount == 150) {
            for (int i = 6; i < 28; i++) {
                Utilities.clearTask(rc, i);
            }
        }
        if (!rc.isSpawned()) {
            Clock.yield();
        }
        Task t = Utilities.readTask(rc);
        if (SittingOnFlag) {
            //sitting where flag should be, but cant see any flags...
            //if we still cant see a flag 50 turns later, then until we do see one we're gonna assume this location should essentially be shut down
            if (rc.senseNearbyFlags(-1, rc.getTeam()).length == 0) {
                //shut down this spawn location for now
                if (countSinceSeenFlag > 40) {
                    rc.setIndicatorString("Dont come help me!");
                    Clock.yield();
                } else {
                    countSinceSeenFlag++;
                }
            } else {
                countSinceSeenFlag = 0;
            }
            if (countSinceLocked != 0) {
                countSinceLocked++;
            }
            if (countSinceLocked >= 20) {
                countSinceLocked = 0;
                Utilities.editBitSharedArray(rc, 1021, false);
            }
            //check if nearby enemies are coming to attack, call for robots to prioritize spawning at ur flag
            if (rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length > rc.senseNearbyRobots(-1, rc.getTeam()).length) {
                //spawn everyone in 0-8 if possible, also lock so it wont cycle
                if (rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(0)))) {
                    Utilities.editBitSharedArray(rc, 1022, false);
                    Utilities.editBitSharedArray(rc, 1023, false);
                    //lock
                    Utilities.editBitSharedArray(rc, 1021, true);
                } else if (rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(1)))) {
                    Utilities.editBitSharedArray(rc, 1022, false);
                    Utilities.editBitSharedArray(rc, 1023, true);
                    //lock
                    Utilities.editBitSharedArray(rc, 1021, true);
                } else {
                    Utilities.editBitSharedArray(rc, 1022, true);
                    Utilities.editBitSharedArray(rc, 1023, false);
                    //lock
                    Utilities.editBitSharedArray(rc, 1021, true);
                }
                countSinceLocked++;
            }
            UpdateExplosionBorder(rc);
            /*if(rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length > 0)
            {
                System.out.println("Sensed!!!!!!!!!!!!");
               if(rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation().add(rc.getLocation().directionTo(rc.senseNearbyRobots(-1, rc.getTeam().opponent())[0].getLocation()))))
                {
                   rc.build(TrapType.EXPLOSIVE, rc.getLocation().add(rc.getLocation().directionTo(rc.senseNearbyRobots(-1, rc.getTeam().opponent())[0].getLocation())));
                }
               else
               {
                   System.out.println("" + rc.getCrumbs());
               }
            }*/
        } else if (t != null) {//there is a task to do
            Pathfinding.bugNav2(rc, t.location);
            TrapType toBeBuilt = TrapType.STUN;
            if (ShouldBuildWater(rc))
                toBeBuilt = TrapType.WATER;
            else if (rc.getCrumbs() >= 3000)
                toBeBuilt = TrapType.EXPLOSIVE;
            if (locationIsActionable(rc, t.location)) {
                if (rc.canBuild(toBeBuilt, t.location))
                    rc.build(toBeBuilt, t.location);


            }
        } else {//there is no task to be done
            //There is no task to be done and all the flags have guys sitting on them
            //Move away from the nearest guys avoiding ops especicially
            if (rc.getLocation().distanceSquaredTo(findClosestSpawnLocation(rc)) > 15) {
                Pathfinding.bugNav2(rc, findClosestSpawnLocation(rc));
            }
            Direction d = directionToMove(rc);
            for (int i = 0; i < 8; i++) {
                if (rc.canMove(d)) {
                    rc.move(d);
                }
                d = d.rotateLeft();
            }

            if (rc.getRoundNum() % 8 == 0)
                UpdateExplosionBorder(rc);

        }
        RobotInfo[] enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
        MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
        if (toAttack != null && rc.canAttack(toAttack))
            rc.attack(toAttack);
    }

    private static boolean ShouldBuildWater(RobotController rc) throws GameActionException {
        int nearestFlag = findClosestSpawnLocation(rc).distanceSquaredTo(rc.getLocation());
        int distanceFromNearestWall = distanceFromNearestSpawnWall(rc);
        int minDistanceFromFlag = (rc.getMapHeight() + rc.getMapWidth()) / 2 - (rc.getMapHeight() + rc.getMapWidth()) / 3;
        return (nearestFlag >= (minDistanceFromFlag) && distanceFromNearestWall > 5);
    }

    private static int distanceFromNearestSpawnWall(RobotController rc) {
        int fromTop = rc.getMapHeight() - rc.getLocation().y;
        int fromBottom = rc.getLocation().y;
        int fromLeft = rc.getLocation().x;
        int fromRight = rc.getMapWidth() - rc.getLocation().x;
        int out = Math.min(fromTop, fromRight);
        out = Math.min(out, fromLeft);
        out = Math.min(out, fromBottom);
        return out;

    }


    private static Direction directionToMove(RobotController rc) throws GameActionException {
        Direction d = directions[rc.getID() % 8];
        MapLocation ops = Utilities.averageRobotLocation(rc.senseNearbyRobots(-1, rc.getTeam().opponent()));
        if (ops != null)
            for (int i = 0; i < 8; i++) {
                if (ops.distanceSquaredTo(rc.getLocation()) > ops.distanceSquaredTo(rc.getLocation().add(d)))
                    break;
                d.rotateLeft();
            }
        return d;

    }


    public static boolean locationIsActionable(RobotController rc, MapLocation m) throws GameActionException {
        MapInfo[] ActionableTiles = rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED);
        for (MapInfo curr : ActionableTiles) {
            if (m.equals(curr.getMapLocation())) {
                return true;
            }

        }
        return false;
    }

    public static void UpdateExplosionBorder(RobotController rc) throws GameActionException {
        for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
            Pathfinding.bugNav2(rc, t.getMapLocation());
            TrapType toBeBuilt = TrapType.STUN;
            if (rc.getCrumbs() >= 3000)
                toBeBuilt = TrapType.EXPLOSIVE;
            if (locationIsActionable(rc, t.getMapLocation())) {
                if (rc.canBuild(toBeBuilt, t.getMapLocation()))
                    rc.build(toBeBuilt, t.getMapLocation());

            }
        }

    }


}

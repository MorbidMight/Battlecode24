package Version3;

import battlecode.common.*;

import static Version3.RobotPlayer.*;

public class Builder {
    public static void runBuilder(RobotController rc) throws GameActionException {
        if (!rc.isSpawned())
            Clock.yield();


        Task t = Utilities.readTask(rc);
        if (SittingOnFlag) {
            if (rc.getRoundNum() % 7 == 0||rc.getRoundNum()>250)
                UpdateExplosionBorder(rc);
        } else if (t != null) {//there is a task to do
            Pathfinding.tryToMove(rc, t.location);
            if (locationIsActionable(rc, t.location)) {
                if (rc.canBuild(TrapType.EXPLOSIVE, t.location)) {
                    rc.build(TrapType.EXPLOSIVE, t.location);
                }
            }

        } else {//there is no task to be done


            //There is no task to be done and all the flags have guys sitting on them
            //Move away from the nearest guys avoiding ops especicially
            Direction d = directionToMove(rc);
            for (int i = 0; i < 8; i++) {
                if (rc.canMove(d)) {
                    rc.move(d);
                }
                d = d.rotateLeft();
            }

            if (rc.getRoundNum() % 8 == 0 && rc.getRoundNum() > 250) { /*&&distanceFromNearestSpawnLocation(rc)>16)*/
                UpdateExplosionBorder(rc);
            }

        }
        RobotInfo[] enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
        MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
        if (toAttack != null && rc.canAttack(toAttack))
            rc.attack(toAttack);
    }


    private static Direction directionToMove(RobotController rc) throws GameActionException {
        MapLocation moveAwayFrom = averageOpLocation(rc);
        if (moveAwayFrom == null)
            return directions[rng.nextInt(8)];
        else
            return moveAwayFrom.directionTo(rc.getLocation());

    }

    private static MapLocation averageOpLocation(RobotController rc) throws GameActionException {
        double x = 0.0;
        double y = 0.0;
        int total = 0;
        for (RobotInfo R : rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent())) {
            x += R.getLocation().x;
            y += R.getLocation().y;
            total++;
        }
        if (total == 0) {
            return null;
        } else return new MapLocation((int) (x / total), (int) (y / total));
    }

    private static int distanceFromNearestSpawnLocation(RobotController rc) {
        return 0;
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
            if (rc.canBuild(TrapType.EXPLOSIVE, t.getMapLocation()) && t.getTrapType().equals(TrapType.NONE)) {
                rc.build(TrapType.EXPLOSIVE, t.getMapLocation());
            }
        }
    }

}

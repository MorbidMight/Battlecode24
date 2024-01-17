package Version9;

import battlecode.common.*;

import static Version9.RobotPlayer.*;

public class Explorer {
    public static void runExplorer(RobotController rc) throws GameActionException {
        if (!rc.isSpawned()) {
            Clock.yield();
        }
        if (isAdjacentToDam(rc) && turnCount < 150 && rc.senseMapInfo(rc.getLocation()).getTrapType() == TrapType.NONE) {
            Task bomb = new Task(rc.getLocation(), false);
            int index = Utilities.openTaskIndex(rc);
            bomb.arrayIndex = index;
            if (index != -1) {
                Utilities.setTaskSharedArray(rc, bomb, index);
            }
        }
        //condense on dam for when it breaks
        MapLocation centerOfMap = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
        MapLocation nearestEnemyFlag = findClosestBroadcastFlags(rc);
        if (rc.getRoundNum() >= 198){
            if(isAdjacentToDam(rc)){
                if(rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite())){
                    rc.move(rc.getLocation().directionTo(centerOfMap).opposite());
                }
            }
        }
        else if (rc.getRoundNum() > 150 && rc.getRoundNum() < 198) {
            if (rc.canFill(rc.adjacentLocation(rc.getLocation().directionTo(centerOfMap)))) {
                rc.fill(rc.adjacentLocation(rc.getLocation().directionTo(centerOfMap)));
            }
            //if adjacent to dam, maybe try and lay a trap?
            if (isAdjacentToDam(rc) && rc.canBuild(TrapType.STUN, rc.getLocation()))
                rc.build(TrapType.STUN, rc.getLocation());
            Pathfinding.bugNav2(rc, centerOfMap);
        } else {
            //tries to get neary crumbs
            MapLocation[] nearbyCrumbs = rc.senseNearbyCrumbs(-1);
            MapLocation targetCrumb = null;
            if (nearbyCrumbs.length > 0)
                targetCrumb = chooseTargetCrumb(rc, nearbyCrumbs);
            if (targetCrumb != null) {
                MapInfo targetLoc = rc.senseMapInfo(targetCrumb);
                //check if crumb is on water
                if (!targetLoc.isPassable() && rc.canFill(targetCrumb)) {
                    rc.fill(targetCrumb);
                }
                Pathfinding.bugNav2(rc, targetCrumb);
            }
            //explore a new area
            else if (turnsSinceLocGen == 20 || turnsSinceLocGen == 0 || rc.getLocation().equals(targetLoc)) {
                targetLoc = generateTargetLoc(rc);
                Pathfinding.bugNav2(rc, targetLoc);
                turnsSinceLocGen = 1;
            } else {
                if (rc.canFill(rc.adjacentLocation(rc.getLocation().directionTo(targetLoc)))) {
                    rc.fill(rc.adjacentLocation(rc.getLocation().directionTo(targetLoc)));
                }
                Pathfinding.bugNav2(rc, targetLoc);
                turnsSinceLocGen++;
            }
        }
    }

    public static MapLocation chooseTargetCrumb(RobotController rc, MapLocation[] nearbyCrumbs) throws GameActionException {
        int highestCrumbVal = 0;
        int highestIndex = -1;
        for (int i = 0; i < nearbyCrumbs.length; i++) {
            if (rc.senseMapInfo(nearbyCrumbs[i]).getCrumbs() > highestCrumbVal) {
                highestIndex = i;
                highestCrumbVal = rc.senseMapInfo(nearbyCrumbs[i]).getCrumbs();
            }
        }
        return nearbyCrumbs[highestIndex];
    }

    public static MapLocation generateTargetLoc(RobotController rc) {
        int x = rng.nextInt(rc.getMapWidth());
        int y = rng.nextInt(rc.getMapHeight());
        return new MapLocation(x, y);
    }

    public static boolean isAdjacentToDam(RobotController rc) throws GameActionException {
        for (Direction dir : Direction.allDirections()) {
            if (rc.canSenseLocation(rc.adjacentLocation(dir)) && rc.senseMapInfo(rc.adjacentLocation(dir)).isDam())
                return true;
        }
        return false;
    }
}

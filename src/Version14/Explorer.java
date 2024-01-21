package Version14;

import battlecode.common.*;

import static Version14.RobotPlayer.*;

public class Explorer {
    static MapLocation dam;
    public static void runExplorer(RobotController rc) throws GameActionException {
        if (!rc.isSpawned()) {
            return;
        }
        if(dam == null)
            dam = canSeeDam(rc);
        if (isAdjacentToDam(rc) && turnCount < 120 && rc.senseMapInfo(rc.getLocation()).getTrapType() == TrapType.NONE) {
            Task bomb = new Task(rc.getLocation(), false);
            int index = Utilities.openTaskIndex(rc);
            bomb.arrayIndex = index;
            if (index != -1) {
                Utilities.setTaskSharedArray(rc, bomb, index);
            }
        }
        //condense on dam for when it breaks
        MapLocation centerOfMap = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
        //MapLocation nearestEnemyFlag = findClosestBroadcastFlags(rc);
//        if (rc.getRoundNum() >= 198){
//            if(isAdjacentToDam(rc)){
//                if(rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite())){
//                    rc.move(rc.getLocation().directionTo(centerOfMap).opposite());
//                }
//            }
//        }
        if (rc.getRoundNum() > 120) {
            if (rc.canFill(rc.adjacentLocation(rc.getLocation().directionTo(centerOfMap)))) {
                rc.fill(rc.adjacentLocation(rc.getLocation().directionTo(centerOfMap)));
            }
            boolean isAdjacent = isAdjacentToDam(rc);
            //if adjacent to dam, maybe try and lay a trap?
            if (isAdjacent && rc.canBuild(TrapType.STUN, rc.getLocation()))
                rc.build(TrapType.STUN, rc.getLocation());
            if(!isAdjacent) {
                if(dam != null){
                    Pathfinding.bellmanFord5x5(rc, dam);
                }
                else {
                    Pathfinding.bellmanFord5x5(rc, centerOfMap);
                }
            }
        } else {
            //tries to get neary crumbs
            MapLocation[] nearbyCrumbs = rc.senseNearbyCrumbs(-1);
            MapLocation targetCrumb = null;
            if (nearbyCrumbs.length > 0)
                targetCrumb = chooseTargetCrumb(rc, nearbyCrumbs);
            if (targetCrumb != null) {
                MapInfo targetLoc = rc.senseMapInfo(targetCrumb);
                if (rc.canFill(rc.adjacentLocation(rc.getLocation().directionTo(targetCrumb))) && targetLoc.getCrumbs() > 30) {
                    rc.fill(rc.adjacentLocation(rc.getLocation().directionTo(targetCrumb)));
                }
                //check if crumb is on water
                if (!targetLoc.isPassable() && rc.canFill(targetCrumb)) {
                    rc.fill(targetCrumb);
                }
                Pathfinding.bellmanFord5x5(rc, targetCrumb);
            }
            //explore a new area
            else if (turnsSinceLocGen == 20 || turnsSinceLocGen == 0 || rc.getLocation().equals(targetLoc)) {
                targetLoc = generateTargetLoc(rc);
                Pathfinding.bugNav2(rc, targetLoc);
                turnsSinceLocGen = 1;
            } else {
                Pathfinding.combinedPathfinding(rc, targetLoc);
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
    //returns a spot on the dam if can see dam, otherwise returns null
    public static MapLocation canSeeDam(RobotController rc) throws GameActionException{
        MapInfo[] spaces = rc.senseNearbyMapInfos(GameConstants.VISION_RADIUS_SQUARED);
        for(MapInfo space : spaces){
            if(space.isDam())
                return space.getMapLocation();
        }
        return null;
    }
}

package Version18;

import battlecode.common.*;

import java.util.HashSet;

import static Version18.RobotPlayer.*;
import static Version18.Utilities.averageRobotLocation;

public class Explorer {
    static HashSet<MapLocation> scored = new HashSet<>();
    static MapLocation dam;
    static MapLocation centerOfMap = null;
    public static void runExplorer(RobotController rc) throws GameActionException {
        if (!rc.isSpawned()) {
            return;
        }
        if(centerOfMap == null) centerOfMap = new MapLocation(rc.getMapWidth()/ 2, rc.getMapHeight() / 2);
        breakFree(rc);

        if(dam == null)
            dam = canSeeDam(rc);
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
        if(rc.getRoundNum() < 65){
            MapInfo[] toScore = rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED);
            int highScore = Integer.MIN_VALUE;
            int highIndex = 0;
            for (int i = 0; i < toScore.length; i++) {
                MapInfo m = toScore[i];
                if(!scored.contains(m.getMapLocation())) {
                    int score = evaluateFlagLocation(m.getMapLocation(), rc);
                    scored.add(m.getMapLocation());
                    if (score > highScore) {
                        highIndex = i;
                        highScore = score;
                    }
                }
            }
            switch(spawnOrigin){
                case -1:
                    break;
                case 0:
                    if(highScore > rc.readSharedArray(44)){
                        rc.writeSharedArray(43, Utilities.convertLocationToInt(toScore[highIndex].getMapLocation()));
                        rc.writeSharedArray(44, highScore);
                    }
                    break;
                case 1:
                    if(highScore > rc.readSharedArray(46)){
                        rc.writeSharedArray(45, Utilities.convertLocationToInt(toScore[highIndex].getMapLocation()));
                        rc.writeSharedArray(46, highScore);
                    }
                    break;
                case 2:
                    if(highScore > rc.readSharedArray(48)){
                        rc.writeSharedArray(47, Utilities.convertLocationToInt(toScore[highIndex].getMapLocation()));
                        rc.writeSharedArray(48, highScore);
                    }
                    break;
            }
        }
        if (rc.getRoundNum() > 130) {
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
            if (rc.canFill(rc.adjacentLocation(rc.getLocation().directionTo(centerOfMap)))) {
                rc.fill(rc.adjacentLocation(rc.getLocation().directionTo(centerOfMap)));
            }
            boolean isAdjacent = isAdjacentToDam(rc);
            //if adjacent to dam, maybe try and lay a trap?
            if (isAdjacent) {
                attemptBuild(rc);
                //Builder.UpdateExplosionBorder2(rc);
            }
            if(!isAdjacent) {
                if(dam != null){
                    Pathfinding.bellmanFord5x5(rc, dam);
                }
                else {
                    Pathfinding.bellmanFord5x5(rc, centerOfMap);
                }
            }
        } else {
            FlagInfo[] nearbyFlags = rc.senseNearbyFlags(-1, rc.getTeam());
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
            else if (turnsSinceLocGen == 20 || turnsSinceLocGen == 0 || rc.getLocation().equals(targetLoc) || (rc.canSenseLocation(targetLoc) && !rc.senseMapInfo(targetLoc).isPassable())) {
                targetLoc = generateTargetLoc(rc);
                if(rc.getRoundNum() > 70 && nearbyFlags.length > 0 && rc.senseNearbyFlags(-1, rc.getTeam())[0].isPickedUp()){
                    Pathfinding.bellmanFordFlag(rc, targetLoc, new StolenFlag(rc.senseNearbyFlags(-1, rc.getTeam())[0].getLocation(), true));
                }
                else
                    BFSKernel9x9.BFS(rc, targetLoc);
                    //Pathfinding.combinedPathfinding(rc, targetLoc);
                turnsSinceLocGen = 1;
            } else {
                if(rc.getRoundNum() > 70 && nearbyFlags.length > 0 && rc.senseNearbyFlags(-1, rc.getTeam())[0].isPickedUp()){
                    Pathfinding.bellmanFordFlag(rc, targetLoc, new StolenFlag(rc.senseNearbyFlags(-1, rc.getTeam())[0].getLocation(), true));
                }
                else
                    BFSKernel9x9.BFS(rc, targetLoc);
                    //Pathfinding.combinedPathfinding(rc, targetLoc);
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
    public static void attemptBuild(RobotController rc) throws GameActionException {
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        if (enemyRobots.length >= 1){
            for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
                if (!Soldier.isTrapAdjacent(rc, t.getMapLocation()) && rc.canBuild(TrapType.STUN, t.getMapLocation()) && rc.getCrumbs() > 100) {
                    rc.build(TrapType.STUN, t.getMapLocation());
                }
            }
        }
    }
    public static void breakFree(RobotController rc) throws GameActionException {
        if(isStuck(rc)){
            for(Direction d : Direction.allDirections()){
                if(rc.canFill(rc.getLocation().add(d))){
                    rc.fill(rc.getLocation().add(d));
                }
            }
        }
    }
    public static boolean isStuck(RobotController rc) throws GameActionException {
        for(MapInfo m : rc.senseNearbyMapInfos(2)){
            if(m.isPassable() && rc.senseRobotAtLocation(m.getMapLocation()) == null){
                return false;
            }
        }
        return true;
    }
    public static int evaluateFlagLocation(MapLocation location, RobotController rc) throws GameActionException {
        int score = 0;
        int height = rc.getMapHeight();
        int width = rc.getMapWidth();
        if (Builder.isMapEdge(rc, location)) {
            score += 300;
        }
        for(Direction dir: Direction.allDirections())
        {
            if(Builder.isMapEdge(rc, location.add( dir)))
            {
                score += 200;
            }
        }

        //decrease to score if close to map
        score += ((location.distanceSquaredTo(centerOfMap) * 1.5) / (Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2)))) * 150;

        if (!rc.senseLegalStartingFlagPlacement(location)) {
            return -1000;
        }
        if (Builder.isMapEdge(rc, location)) {
            score += 100;
        }
        //decrease score if on or near spawn
        for(MapLocation curr: SpawnLocations)
        {
            if (location.equals(curr))
                score -= 600;
            else if(location.isAdjacentTo(curr))
                score -= 300;
        }
        //increase score if near dam
        MapInfo[] nearbyMapInfo = rc.senseNearbyMapInfos();
        for(MapInfo x: nearbyMapInfo)
        {
            if(x.isDam())
                score -= 100;
        }

        //Covered by wall
        if(rc.senseMapInfo(location.add(location.directionTo(centerOfMap))).isWall()) {
            score += 125;
            if(rc.senseMapInfo(location.add(location.directionTo(centerOfMap).rotateLeft())).isWall())
                score+=100;
            if(rc.senseMapInfo(location.add(location.directionTo(centerOfMap).rotateRight())).isWall())
                score+=100;
        }
        if(Utilities.locationIsBehindWall(rc,centerOfMap, location, 2)){
            score += 150;
        }
        for (MapInfo M : rc.senseNearbyMapInfos(location, 8)) {
            MapLocation m = M.getMapLocation();
            if (m.isAdjacentTo(location)) {
                if(M.isWall())
                    score+= 100;
                if(M.isWater())
                    score+= 10;
                if(M.isDam())
                    score-= 700;
            } else {
                if (M.isWall())
                    score += 15;
                if (M.isWater())
                    score += 3;
                if (M.isDam())
                    score -= 190;
            }
        }
        return score;
    }
}

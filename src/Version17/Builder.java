package Version17;

import battlecode.common.*;
import battlecode.world.Flag;
import battlecode.world.Trap;

import java.util.ArrayDeque;
import java.util.Deque;

import java.awt.*;
import java.util.Map;
import java.util.PriorityQueue;

import static Version17.RobotPlayer.*;


//Current Builder strategy
//if a builder is spawned on a flag it sits there and places bombs around
//if there a task to dp the builder goes to the task location and build a different trap based on context
//Otherwise it wanders around and places traps wherever
//in addition if a teammate current has a flag they don't place any bombs so that the bomb carrier can live.
public class Builder {
    //used for flagsitters
    static boolean isActive = true;
    final static int ROUND_TO_BUILD_EXPLOSION_BORDER = 0;
    static MapLocation centerOfMap = null;
    static ArrayDeque<MapLocation> scoredLocations = new ArrayDeque<MapLocation>();
    static PriorityQueue<PotentialFlag> scoredLocationsV2 = new PriorityQueue<>();
    static int bestScore = -1;
    static int radius = 0;
    static MapLocation closestCorner;
    static MapLocation dropped;
    static boolean usingExplorerTarget = false;

    static final int EXPLORE_PERIOD = 65;
    static final int PLACEMENT_PERIOD = 140;
    static MapLocation originalFlagLocation;


    public static void runBuilder(RobotController rc) throws GameActionException {
        if(turnCount < 1)
            moveToSpawn = false;
        if(moveToSpawn)
        {
            rc.setIndicatorDot(rc.getLocation(), 0,100,0);
            Pathfinding.combinedPathfinding(rc, SpawnLocations[0]);
        }
        else
            rc.setIndicatorDot(rc.getLocation(), 0,0,100);
        if (rc.getRoundNum() >= 201) role = roles.flagSitter;
            //role = roles.soldier;
        //pick up flag if it is early game
        if (centerOfMap == null)
            centerOfMap = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
        if(closestCorner == null){
            closestCorner = findClosestCorner(rc);
        }
        if (rc.getRoundNum() < 10 && !rc.hasFlag() && rc.canPickupFlag(rc.getLocation())) {
            rc.pickupFlag(rc.getLocation());
            originalFlagLocation = rc.getLocation();
        }
        if (rc.hasFlag() && rc.getRoundNum() < EXPLORE_PERIOD) {
            MapInfo[] nearbyLocs = rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED);
            for (MapInfo info : nearbyLocs) {
                MapLocation loc = info.getMapLocation();
                if (!scoredLocationsV2.contains(loc)) {
                    int score = evaluateFlagLocation(loc, rc);
                    scoredLocationsV2.add(new PotentialFlag(loc, score));
                }
            }
            //move away from center
            //Pathfinding.bugNav2(rc, rc.getLocation().add(rc.getLocation().directionTo(centerOfMap).opposite()));
            explore(rc);
//            if (rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite())) {
//                rc.move(rc.getLocation().directionTo(centerOfMap).opposite());
//            } else if (rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft())) {
//                rc.move(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft());
//            } else if (rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft())) {
//                rc.move(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft());
//            }
//            else if (rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft().rotateLeft())) {
//                rc.move(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft().rotateLeft());
//            }
//            else if (rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft().rotateLeft().rotateLeft())) {
//                rc.move(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft().rotateLeft().rotateLeft());
//            }
//            else if (rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft())) {
//                rc.move(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft().rotateLeft().rotateLeft().rotateLeft());
//            }
        }

        if (rc.getRoundNum() > EXPLORE_PERIOD && rc.getRoundNum() < 180) {
            assert scoredLocationsV2.peek() != null;
            PotentialFlag temp = scoredLocationsV2.peek();
            MapLocation target = temp.location;
            switch(spawnOrigin){
                case -1:
                    break;
                case 0:
                    if(rc.readSharedArray(44) > temp.score){
                        target = Utilities.convertIntToLocation(rc.readSharedArray(43));
                        //System.out.println(Utilities.convertIntToLocation(rc.readSharedArray(43)));
                        usingExplorerTarget = true;
                    }
                    break;
                case 1:
                    if(rc.readSharedArray(46) > temp.score){
                         target = Utilities.convertIntToLocation(rc.readSharedArray(45));
                        //System.out.println(Utilities.convertIntToLocation(rc.readSharedArray(45)));
                        usingExplorerTarget = true;
                    }
                    break;
                case 2:
                    if(rc.readSharedArray(48) > temp.score){
                        target = Utilities.convertIntToLocation(rc.readSharedArray(47));
                        //System.out.println(Utilities.convertIntToLocation(rc.readSharedArray(47)));
                        usingExplorerTarget = true;
                    }
                    break;
            }
            if (!rc.getLocation().equals(target))
                BFSKernel9x9.BFS(rc, target);
            if (rc.canSenseLocation(target) && rc.senseLegalStartingFlagPlacement(target)) {
                    if (rc.canDropFlag(target)) {
                        rc.dropFlag(target);
                        FlagSitter.home = target;
                        if (rc.readSharedArray(40) == 0) {
                            rc.writeSharedArray(40, Utilities.convertLocationToInt(target));
                            dropped = target;
                        }
                        else if (rc.readSharedArray(41) == 0) {
                            rc.writeSharedArray(41, Utilities.convertLocationToInt(target));
                            dropped = target;
                            //System.out.println(Utilities.convertIntToLocation(rc.readSharedArray(41)));
                        }
                        else if (rc.readSharedArray(42) == 0) {
                            rc.writeSharedArray(42, Utilities.convertLocationToInt(target));
                            dropped = target;
                            //rc.resign();
                            //System.out.println(Utilities.convertIntToLocation(rc.readSharedArray(42)));
                        }
                        //role = roles.explorer;
                        role = roles.moat;
                    }
            } else if(rc.canSenseLocation(target) && !rc.senseLegalStartingFlagPlacement(target)) {
                while (rc.canSenseLocation(target) && !rc.senseLegalStartingFlagPlacement(target)) {
                    if (usingExplorerTarget) {
                        switch (spawnOrigin) {
                            case -1:
                                break;
                            case 0:
                                rc.writeSharedArray(44, 0);
                                break;
                            case 1:
                                rc.writeSharedArray(46, 0);
                                break;
                            case 2:
                                rc.writeSharedArray(48, 0);
                                break;
                        }
                        usingExplorerTarget = false;
                        assert scoredLocationsV2.peek() != null;
                        target = scoredLocationsV2.peek().location;
                    } else {
                        scoredLocationsV2.poll();
                        assert scoredLocationsV2.peek() != null;
                        target = scoredLocationsV2.peek().location;
                    }
                }
            }
            //buildMoat(rc);
        } else if (rc.getRoundNum() >= 170 && rc.hasFlag()) {
            //System.out.println(rc.getLocation());
            if (rc.senseLegalStartingFlagPlacement(rc.getLocation()) && rc.canDropFlag(rc.getLocation())) {
                rc.dropFlag(rc.getLocation());
                FlagSitter.home = rc.getLocation();
                if (rc.readSharedArray(40) == 0) {
                    rc.writeSharedArray(40, Utilities.convertLocationToInt(rc.getLocation()));
                    dropped = rc.getLocation();
                }
                else if (rc.readSharedArray(41) == 0) {
                    rc.writeSharedArray(41, Utilities.convertLocationToInt(rc.getLocation()));
                    dropped = rc.getLocation();
                }
                else if (rc.readSharedArray(42) == 0) {
                    rc.writeSharedArray(42, Utilities.convertLocationToInt(rc.getLocation()));
                    dropped = rc.getLocation();
                    //rc.resign();
                }
                role = roles.moat;
            } else {
                //move away from center
//                if (rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite())) {
//                    rc.move(rc.getLocation().directionTo(centerOfMap).opposite());
//                } else if (rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft())) {
//                    rc.move(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft());
//                } else if (rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft())) {
//                    rc.move(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft().rotateLeft());
//                }
                //explore(rc);
                rc.setIndicatorDot(rc.getLocation(), 0, 100, 0);
                BFSKernel9x9.BFS(rc, SpawnLocations[0]);//rc.getLocation().add(rc.getLocation().directionTo(rc.senseNearbyFlags(-1, rc.getTeam())[0].getLocation()).opposite()));
                /*if(rc.canMove(rc.getLocation().directionTo(rc.senseNearbyFlags(-1, rc.getTeam())[0].getLocation()).opposite()))
                {
                    rc.move(rc.getLocation().directionTo(rc.senseNearbyFlags(-1, rc.getTeam())[0].getLocation()).opposite());
                }*/
            }
        }
    }
    public static void buildMoat (RobotController rc) throws GameActionException {

        if(rc.getRoundNum() > 215)
        {
           //role = roles.explorer;
            role = roles.flagSitter;
            FlagSitter.home = dropped;

        }
        MapLocation centerOfMap = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
        FlagInfo[] flags = rc.senseNearbyFlags(-1);
        if(rc.canBuild(TrapType.WATER, flags[0].getLocation()))
            rc.build(TrapType.WATER, flags[0].getLocation());
        if(rc.getLocation().equals(flags[0].getLocation()))
            if(rc.canBuild(TrapType.WATER, rc.getLocation()))
               rc.build(TrapType.WATER, rc.getLocation());
            rc.setIndicatorDot(rc.getLocation(), 100,100,0);
        //move away from flag
        if(flags.length == 0){
            //role = roles.explorer;
            role = roles.flagSitter;
            FlagSitter.home = dropped;
            return;
        }
        if(rc.getLocation().distanceSquaredTo(flags[0].getLocation()) < 9)
        {
            if(rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation()))
                rc.build(TrapType.EXPLOSIVE, rc.getLocation());
            rc.setIndicatorString("moving away from flag");
            if(rc.canMove(rc.getLocation().directionTo(flags[0].getLocation()).opposite())) {
                rc.move(rc.getLocation().directionTo(flags[0].getLocation()).opposite());
            }
            else if(rc.canMove(rc.getLocation().directionTo(centerOfMap))) {
                rc.move(rc.getLocation().directionTo(centerOfMap));

            }
        }
        //move around flag
        else
        {
            MapLocation bestLoc = rc.getLocation();
            int score = 100;
            for(Direction dir: Direction.allDirections())
            {
                //moving in this direction is within a certain radius of flag
                if(rc.getLocation().add(dir).distanceSquaredTo(flags[0].getLocation()) > 9 && rc.getLocation().add(dir).distanceSquaredTo(flags[0].getLocation()) < 25)
                {
                    if(rc.canMove(dir))
                    {
                        //best location is where it hasn't been
                        if(!alreadyBeen.containsKey(rc.getLocation().add(dir)))
                        {
                           bestLoc = rc.getLocation().add(dir);
                           break;
                        }
                        //best location is replaced if there is a location visited less frequently
                        else
                        {
                            if(alreadyBeen.get(rc.getLocation().add(dir)) < score)
                            {
                                score = alreadyBeen.get(rc.getLocation().add(dir));
                                bestLoc = rc.getLocation().add(dir);
                            }
                        }
                    }
                }
            }
            if(rc.canMove(rc.getLocation().directionTo(bestLoc))) {
                rc.move(rc.getLocation().directionTo(bestLoc));
            }
            updateAlreadyBeen(rc);
        }
        //dig
        for(Direction dir: Direction.allDirections())
        {
            //dig within a certain radius of the flag
            if( rc.getLocation().add(dir).distanceSquaredTo(flags[0].getLocation()) < 10 && rc.getLocation().add(dir).distanceSquaredTo(flags[0].getLocation()) > 3)
            {
                if(rc.canDig(rc.getLocation().add(dir)))
                    rc.dig(rc.getLocation().add(dir));
            }
        }
    }
    public static boolean locationIsActionable(RobotController rc, MapLocation m) throws GameActionException {
        MapInfo[] ActionableTiles = rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED);
        for (MapInfo curr : ActionableTiles)
        {
            if (m.equals(curr.getMapLocation()))
            {
                return true;
            }
        }
        return false;
    }

    public static void UpdateExplosionBorder2(RobotController rc) throws GameActionException {//For flag sitters
        MapInfo[] mapInfos = rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED);
        for (MapInfo t : mapInfos) {
            TrapType toBeBuilt = TrapType.STUN;
            if (!Soldier.isTrapAdjacentSpawn(rc, t.getMapLocation(), TrapType.STUN) && rc.canBuild(toBeBuilt, t.getMapLocation())) {
                rc.build(toBeBuilt, t.getMapLocation());
            }
            if(rc.getCrumbs() > 3000 && !Soldier.isTrapAdjacent(rc, t.getMapLocation(), TrapType.EXPLOSIVE) && rc.canBuild(TrapType.EXPLOSIVE, t.getMapLocation())){
                rc.build(TrapType.EXPLOSIVE, t.getMapLocation());
            }
        }
    }
    public static boolean adjacentSpawnTrap(RobotController rc, MapLocation location) throws GameActionException {
        int x = location.x;
        int y = location.y;
        int width = rc.getMapWidth();
        int height = rc.getMapHeight();
        for(int dx = -1; dx <= 1; dx++){
            for(int dy = -1; dy <= 1; dy++){
                if(dx == 0 && dy == 0 || x + dx >= width || x + dx < 0 || y + dy >= height || y + dy < 0)
                    continue;
                MapLocation temp = new MapLocation(x + dx, y + dy);
                MapInfo tempInfo = rc.senseMapInfo(temp);
                if(tempInfo.getTrapType() != TrapType.NONE && !rc.getLocation().equals(temp))
                    return true;
            }
        }
        return false;
    }

    public static void UpdateExplosionBorder(RobotController rc) throws GameActionException {//For normal
        for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
            if (rc.canBuild(TrapType.EXPLOSIVE, t.getMapLocation())) {
                rc.build(TrapType.EXPLOSIVE, t.getMapLocation());
            }
        }
    }

    public static void runBuild(RobotController rc, RobotInfo[] enemies) throws GameActionException
    {
        PriorityQueue<MapLocationWithDistance> bestTrapLocations = getBestBombLocations(rc, enemies);
        while (!bestTrapLocations.isEmpty())
        {
            MapLocation currentTryLocation = bestTrapLocations.remove().location;
            if (currentTryLocation != null && rc.canBuild(TrapType.STUN, currentTryLocation) && rc.getCrumbs() > 300) {
                rc.build(TrapType.STUN, currentTryLocation);
            }
        }
    }

    public static PriorityQueue<MapLocationWithDistance> getBestBombLocations(RobotController rc, RobotInfo[] enemies)
    {
        PriorityQueue<MapLocationWithDistance> bestLocations = new PriorityQueue<>();
        if(enemies.length == 0)
        {
            for(Direction direction : directions)
            {
                MapLocation tempLocation = rc.adjacentLocation(direction);
                MapLocation closestFlag = findClosestBroadcastFlags(rc);
                if(closestFlag != null && rc.canBuild(TrapType.STUN, tempLocation))
                {
                    bestLocations.add(new MapLocationWithDistance(tempLocation, tempLocation.distanceSquaredTo(closestFlag)));
                }
            }
        }
        else
        {
            MapLocation averageEnemyLocation = Utilities.averageRobotLocation(enemies);
            for(Direction direction : directions)
            {
                MapLocation tempLocation = rc.adjacentLocation(direction);
                if(rc.canBuild(TrapType.STUN, tempLocation))
                {
                    bestLocations.add(new MapLocationWithDistance(tempLocation, tempLocation.distanceSquaredTo(averageEnemyLocation)));
                }
            }
        }
        return bestLocations;
    }
    public static MapLocation getAverageTrapLocation(RobotController rc, MapInfo[] possibleTraps)
    {
        int count = 0;
        int x = 0;
        int y = 0;
        for(MapInfo info : possibleTraps)
        {
            if(!info.getTrapType().equals(TrapType.NONE))
            {
                x += info.getMapLocation().x;
                y += info.getMapLocation().y;
                count++;
            }
        }

        MapLocation averageTrapLocation = null;
        if(count != 0)
        {
            averageTrapLocation = new MapLocation(x / count, y / count);
        }
        return averageTrapLocation;
    }

    public static int evaluateFlagLocation(MapLocation location, RobotController rc) throws GameActionException {
        int score = 0;
        int height = rc.getMapHeight();
        int width = rc.getMapWidth();
        //decrease to score if close to map
        score += ((location.distanceSquaredTo(centerOfMap) * 1.5) / (Math.sqrt(Math.pow(height, 1.5) + Math.pow(width, 1.5)))) * 150;

        if (!rc.senseLegalStartingFlagPlacement(location)) {
            return -1000;
        }
        if (isMapEdge(rc, location)) {
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
                score -= 50;
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
    public static boolean isMapEdge(RobotController rc, MapLocation loc){
        int width = rc.getMapWidth();
        int height = rc.getMapHeight();
        return loc.x == 0 || loc.y == 0 || loc.x == width -1 || loc.y == height - 1;
    }
    public static void explore(RobotController rc) throws GameActionException {
        if(rc.getRoundNum() <= 30){
            MapLocation targetCorner = findClosestCorner(rc);
            if(!rc.getLocation().equals(targetCorner)) {
                Pathfinding.combinedPathfinding(rc, targetCorner);
                return;
            }
        }
        //explore a new area
        if (turnsSinceLocGen == 20 || turnsSinceLocGen == 0 || rc.getLocation().equals(targetLoc) || (rc.canSenseLocation(targetLoc) && rc.senseMapInfo(targetLoc).getTeamTerritory() != rc.getTeam()) || (rc.canSenseLocation(targetLoc) && !rc.senseMapInfo(targetLoc).isPassable())) {
            targetLoc = generateTargetLoc(rc);
            BFSKernel9x9.BFS(rc, targetLoc);
            turnsSinceLocGen = 1;
        } else {
            //Pathfinding.combinedPathfinding(rc, targetLoc);
            BFSKernel9x9.BFS(rc, targetLoc);
            turnsSinceLocGen++;
        }
    }
    public static MapLocation generateTargetLoc(RobotController rc) {
       double t = rng.nextDouble()*2*Math.PI;

        return new MapLocation((int)(5*Math.cos(t)+ originalFlagLocation.x),(int)(5*Math.sin(t)+ originalFlagLocation.y));
    }
    public static MapLocation findClosestCorner(RobotController rc){
        int x = rc.getLocation().x;
        int y = rc.getLocation().y;
        int mapSizeX = rc.getMapWidth();
        int mapSizeY = rc.getMapHeight();
        if(x > (float) mapSizeX / 2){
            x = mapSizeX-1;
        }
        else{
            x = 0;
        }
        // see above, but for y
        if(y > (float)mapSizeY / 2){
            y = mapSizeY-1;
        }
        else{
            y = 0;
        }
        return new MapLocation(x, y);
    }

}


class MapLocationWithDistance implements Comparable
{
    public MapLocation location;
    public int distanceSquared;

    public MapLocationWithDistance(MapLocation ml, int distanceSquared)
    {
        location = ml;
        this.distanceSquared = distanceSquared;
    }
    public int compareTo(Object other)
    {
        return Integer.compare(distanceSquared,((MapLocationWithDistance) other).distanceSquared);
    }
}

class PotentialFlag implements Comparable {
    public int score;
    public MapLocation location;
    public PotentialFlag(MapLocation loc, int s){
        this.score = s;
        this.location = loc;
    }
    @Override
    public int compareTo(Object o) {
        return Integer.compare(((PotentialFlag) o).score, score);
    }
}

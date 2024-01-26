package Version15MovingFlags;

import battlecode.common.*;
import battlecode.world.Flag;
import battlecode.world.Trap;

import java.awt.*;
import java.util.Map;
import java.util.PriorityQueue;

import static Version15MovingFlags.RobotPlayer.*;


//Current Builder strategy
//if a builder is spawned on a flag it sits there and places bombs around
//if there a task to dp the builder goes to the task location and build a different trap based on context
//Otherwise it wanders around and places traps wherever
//in addition if a teammate current has a flag they don't place any bombs so that the bomb carrier can live.
public class Builder {
    //used for flagsitters
    static boolean isActive = true;
    final static int ROUND_TO_BUILD_EXPLOSION_BORDER = 0;
static int radius = 0;
    public static void runBuilder(RobotController rc) throws GameActionException {
        if(turnCount > 180)
        {
            //role = roles.explorer;
        }
        MapLocation centerOfMap = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
        if(turnCount > 1000 && !SittingOnFlag) {
            role = roles.offensiveBuilder;
            return;
        }
        radius = 7;

        if (builderBombCircleCenter == null && rc.getRoundNum() >= 3) {
            int[] distances = new int[3];
            distances[0] = rc.getLocation().distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(0)));
            distances[1] = rc.getLocation().distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(1)));
            distances[2] = rc.getLocation().distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(2)));
            int lowestIndex = 0;
            if (distances[0] > distances[1]) {
                lowestIndex = 1;
            }
            if (distances[lowestIndex] > distances[2]) {
                lowestIndex = 2;
            }
            builderBombCircleCenter = Utilities.convertIntToLocation(rc.readSharedArray(lowestIndex));
        }
        if (SittingOnFlag) {
            if(turnCount > 30)
            {
                //role = roles.explorer;
                if(rc.hasFlag())
                {
                    rc.dropFlag(rc.getLocation());
                    if(rc.readSharedArray(40) == 0)
                        rc.writeSharedArray(40, Utilities.convertLocationToInt(rc.getLocation()));
                    else if(rc.readSharedArray(41) == 0)
                        rc.writeSharedArray(41, Utilities.convertLocationToInt(rc.getLocation()));
                    else if (rc.readSharedArray(42) == 0)
                        rc.writeSharedArray(42, Utilities.convertLocationToInt(rc.getLocation()));
                    role = roles.explorer;
                }
                buildMoat(rc);
            }
            //pick up flag if it is early game
            if(rc.canPickupFlag(rc.getLocation()) && turnCount < 10)
            {
                rc.pickupFlag(rc.getLocation());
            }
            //move away from center
            if(rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite()))
            {
                rc.move(rc.getLocation().directionTo(centerOfMap).opposite());
            }
            else if(rc.canMove(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft())){
               rc.move(rc.getLocation().directionTo(centerOfMap).opposite().rotateLeft());
            }
        }
        else//When there is no active task
        {
            MapLocation center = builderBombCircleCenter;//Will orbit around the flag
            if (center == null) {
                center = findClosestSpawnLocation(rc);
            }


            RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
            if (enemies.length > 0) {
                MapLocation ops = Utilities.averageRobotLocation(enemies);
                Pathfinding.tryToMove(rc, rc.adjacentLocation(rc.getLocation().directionTo(ops).opposite()));
                if(rc.canBuild(TrapType.EXPLOSIVE,rc.adjacentLocation(rc.getLocation().directionTo(ops)))){
                    rc.canBuild(TrapType.EXPLOSIVE,rc.adjacentLocation(rc.getLocation().directionTo(ops)));
                }

            }
            UpdateExplosionBorder(rc);
            rc.setIndicatorLine(rc.getLocation(), center, 255, 255, 255);
            if (rc.getLocation().distanceSquaredTo(center) < Math.pow(radius - 1, 2)) {
                Pathfinding.tryToMove(rc, rc.adjacentLocation(center.directionTo(rc.getLocation())));
            } else if (rc.getLocation().distanceSquaredTo(center) > Math.pow(radius + 1, 2)) {
                Pathfinding.tryToMove(rc, rc.adjacentLocation(center.directionTo(rc.getLocation()).opposite()));

            } else{

                if ((rc.getRoundNum() / 40) % 2 == 0) {
                    if (rc.canFill(rc.adjacentLocation(center.directionTo(rc.getLocation()).rotateLeft().rotateLeft())))
                        rc.fill((rc.adjacentLocation(center.directionTo(rc.getLocation()).rotateLeft().rotateLeft())));
                    Pathfinding.tryToMove(rc, rc.adjacentLocation(center.directionTo(rc.getLocation()).rotateLeft().rotateLeft()));
                } else {
                    if (rc.canFill(rc.adjacentLocation(center.directionTo(rc.getLocation()).rotateRight().rotateRight())))
                        rc.fill((rc.adjacentLocation(center.directionTo(rc.getLocation()).rotateRight().rotateRight())));
                    Pathfinding.tryToMove(rc, rc.adjacentLocation(center.directionTo(rc.getLocation()).rotateRight().rotateRight()));

                }
            }
        }
    }
    public static void buildMoat (RobotController rc) throws GameActionException {
        rc.setIndicatorDot(rc.getLocation(), 100,0,0);
        FlagInfo[] flags = rc.senseNearbyFlags(-1);
        /*if(rc.getLocation().distanceSquaredTo(flags[0].getLocation()) < 2)
        {
            if(rc.canMove(rc.getLocation().directionTo(flags[0].getLocation()).opposite()))
                rc.move(rc.getLocation().directionTo(flags[0].getLocation()).opposite());
        }
        else
        {
            if(rc.canDig(rc.getLocation().add(rc.getLocation().directionTo(flags[0].getLocation())))) {
                rc.dig(rc.getLocation().add(rc.getLocation().directionTo(flags[0].getLocation())));
            }
            if(rc.canDig(rc.getLocation().add(rc.getLocation().directionTo(flags[0].getLocation())).add(rc.getLocation().directionTo(flags[0].getLocation()).rotateRight()))) {
                rc.dig(rc.getLocation().add(rc.getLocation().directionTo(flags[0].getLocation())));
            }
            if(rc.canDig(rc.getLocation().add(rc.getLocation().directionTo(flags[0].getLocation())).add(rc.getLocation().directionTo(flags[0].getLocation()).rotateLeft()))) {
                rc.dig(rc.getLocation().add(rc.getLocation().directionTo(flags[0].getLocation())));
            }
            if(rc.canMove(rc.getLocation().directionTo(flags[0].getLocation()).rotateRight()))
                rc.move(rc.getLocation().directionTo(flags[0].getLocation()).rotateRight());
            else if(rc.canMove(rc.getLocation().directionTo(flags[0].getLocation()).rotateLeft()))
                rc.move(rc.getLocation().directionTo(flags[0].getLocation()).rotateLeft());
            else
                role = roles.explorer;
        }*/
        for(Direction dir: Direction.allDirections())
        {
            MapLocation centerOfMap = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
            //if(!dir.equals(rc.getLocation().directionTo(centerOfMap)))
            //{
            //}
            if(rc.canDig(rc.getLocation().add(dir)))
            {
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
            if (rc.getCrumbs() > 3500)
                toBeBuilt = TrapType.EXPLOSIVE;
            if (!adjacentSpawnTrap(rc, t.getMapLocation()) && rc.canBuild(toBeBuilt, t.getMapLocation())) {
                rc.build(toBeBuilt, t.getMapLocation());
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
                if(tempInfo.isSpawnZone() && tempInfo.getTrapType() != TrapType.NONE)
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

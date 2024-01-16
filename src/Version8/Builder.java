package Version8;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

import static Version8.RobotPlayer.*;

//Current Builder strategy
//if a builder is spawned on a flag it sits there and places bombs around
//if there a task to dp the builder goes to the task location and build a different trap based on context
//Otherwise it wanders around and places traps wherever
//in addition if a teammate current has a flag they don't place any bombs so that the bomb carrier can live.
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
            }
            else{
                countSinceSeenFlag=0;
            }
            if(countSinceLocked !=0){
                countSinceLocked++;
            }
            if(countSinceLocked >= 20){
                countSinceLocked = 0;
                Utilities.editBitSharedArray(rc, 1021, false);
            }
            //check if nearby enemies are coming to attack, call for robots to prioritize spawning at ur flag
            if(rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent()).length > rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam()).length){
                //spawn everyone in 0-8 if possible, also lock so it wont cycle
                if(rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(0)))){
                    Utilities.editBitSharedArray(rc, 1022, false);
                    Utilities.editBitSharedArray(rc, 1023, false);
                    //lock
                    Utilities.editBitSharedArray(rc, 1021, true);
                }
                else if(rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(1)))){
                    Utilities.editBitSharedArray(rc, 1022, false);
                    Utilities.editBitSharedArray(rc, 1023, true);
                    //lock
                    Utilities.editBitSharedArray(rc, 1021, true);
                }
                else{
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
        }
        else if (t != null)
        {//there is a task to do
            Pathfinding.bugNav2(rc, t.location);
            if (locationIsActionable(rc, t.location)) {
                TrapType toBeBuilt = TrapType.STUN;
                if (rc.getCrumbs() > 2000)
                    toBeBuilt = TrapType.EXPLOSIVE;
                if(rc.canBuild(toBeBuilt, t.location))
                    rc.build(toBeBuilt, t.location);
                Utilities.clearTask(rc, t.arrayIndex);
            }
        }
        else
        {
            //there is no task to be done
            //There is no task to be done and all the flags have guys sitting on them
            //Move away from the nearest guys avoiding ops especicially

            //go towards closest broadcast flag


//            if(rc.getLocation().distanceSquaredTo(findClosestSpawnLocation(rc)) > 15){
//                Pathfinding.bugNav2(rc, findClosestSpawnLocation(rc));
//            }
//            Direction d = directionToMove(rc);
//            for(int i = 0;i<8;i++){
//                if(rc.canMove(d)){
//                    rc.move(d);
//                }
//                d = d.rotateLeft();
//            }
//
//            if(rc.getRoundNum()%8==0/*&&distanceFromNearestSpawnLocation(rc)>16*/)
//                UpdateExplosionBorder(rc);


            RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
            MapInfo[] possibleTraps = rc.senseNearbyMapInfos(-1);
            /*int count = 1;
            int x = rc.getLocation().x;
            int y = rc.getLocation().y;
            for(MapInfo info : possibleTraps)
            {
                if(!info.getTrapType().equals(TrapType.NONE))
                {
                    x += info.getMapLocation().x;
                    y += info.getMapLocation().y;
                }

            }*/

            runBuild(rc, enemies);

            if(rc.getLocation().distanceSquaredTo(RobotPlayer.findClosestSpawnLocation(rc)) > 100)
            {
                Pathfinding.bugNav2(rc, RobotPlayer.findClosestSpawnLocation(rc));
            }
            else if (enemies.length == 0)
            {
                MapLocation closestBroadcast = findClosestBroadcastFlags(rc);
                if (closestBroadcast != null) {
                    if(rc.isMovementReady()) Pathfinding.bugNav2(rc, closestBroadcast);
                };
            }
            else
            {
                Pathfinding.tryToMove(rc, rc.adjacentLocation(rc.getLocation().directionTo(Utilities.averageRobotLocation(enemies)).opposite()));
            }

            runBuild(rc, enemies);
        }



    }




    private static Direction directionToMove(RobotController rc) {
        return directions[rng.nextInt(8)];
    }

    private static int distanceFromNearestSpawnLocation(RobotController rc){
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
            if (rc.canBuild(TrapType.STUN, t.getMapLocation())&&t.getTrapType().equals(TrapType.NONE) && rc.getCrumbs() > 300) {
                rc.build(TrapType.STUN, t.getMapLocation());
            }
        }
    }

    public static void runBuild(RobotController rc, RobotInfo[] enemies) throws GameActionException
    {
        PriorityQueue<MapLocationWithDistance> bestTrapLocations = getBestBombLocations(rc, enemies);
        while (!bestTrapLocations.isEmpty())
        {
            MapLocation currentTryLocation = bestTrapLocations.remove().location;
            if (currentTryLocation != null && rc.canBuild(TrapType.STUN, currentTryLocation)) {
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

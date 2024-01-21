package Version8Water;

import battlecode.common.*;

import java.util.PriorityQueue;

import static Version8Water.RobotPlayer.*;

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

        if(rc.readSharedArray(58)!=0){
            runBuilderToAcceptCarrier(rc);
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
                if(damNearby(rc)&&!(rc.getLocation().x<5||rc.getLocation().y<5||rc.getMapWidth()-rc.getLocation().x<5 || rc.getMapHeight()-rc.getLocation().y<5))
                    toBeBuilt = TrapType.WATER;
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
                UpdateExplosionBorder(rc);


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
            /*
            runBuild(rc, enemies);

               */
            if(rc.getRoundNum()<=GameConstants.SETUP_ROUNDS) {
                /*
                if it can see a dam go towards it
                if it's close enough to the dam but far away from the wall place a water bomb
                otherwise go in the opposite direction of the closest spawn location until it hits the damn
                 */
                if (!tryToPlaceDamBombs(rc)) {//it's not in the right position to place a dam bomb
                    Direction d = rc.getLocation().directionTo(findClosestSpawnLocation(rc));
                    Pathfinding.bugNav2(rc,rc.adjacentLocation(d.opposite()));
                }

            }else {
                MapLocation center = findClosestSpawnLocation(rc);//Will orbit around the flag
                if(rc.getLocation().distanceSquaredTo(center)<81)
                    Pathfinding.bugNav2(rc,rc.adjacentLocation(center.directionTo(rc.getLocation())));
                else if (rc.getLocation().distanceSquaredTo(center)>121)
                    Pathfinding.bugNav2(rc,rc.adjacentLocation(center.directionTo(rc.getLocation()).opposite()));
                else{
                    if((rc.getRoundNum()/75)%2==0)
                    Pathfinding.bugNav2(rc,rc.adjacentLocation(center.directionTo(rc.getLocation()).rotateLeft().rotateLeft()));
                    else
                        Pathfinding.bugNav2(rc,rc.adjacentLocation(center.directionTo(rc.getLocation()).rotateRight().rotateRight()));

                }


            }


            /* urav I have literally zero clue what you're code is trying to do, i'm do my own thing
            runBuild(rc, enemies);
            */
             UpdateExplosionBorder(rc);
        }



    }

    private static boolean damNearby(RobotController rc){
        for(MapInfo i:rc.senseNearbyMapInfos())
            if(i.isDam())
                return true;
        return false;
    }

    public static void escortPlaceWaterBombs(RobotController rc) throws GameActionException {
        MapLocation C = Utilities.convertIntToLocation(rc.readSharedArray(58));
        if(findClosestSpawnLocation(rc).distanceSquaredTo(rc.getLocation())>findClosestSpawnLocation(rc,C).distanceSquaredTo(C)&&findClosestSpawnLocation(rc).distanceSquaredTo(rc.getLocation())>100){//if it's behind the carrier
            for(MapInfo i:rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)){
                if (rc.canDig(i.getMapLocation())) {
                    rc.dig(i.getMapLocation());
                    return;
                }

            }
        }
    }
    private static boolean tryToPlaceDamBombs(RobotController rc) throws GameActionException {
        boolean nearADam = true;
        boolean nearAWaterbomb = true;
        for(MapInfo i : rc.senseNearbyMapInfos()){
            if(i.isDam()){
               nearADam = false;
            }
        }
        if(rc.getLocation().x<5||rc.getLocation().y<5||rc.getMapWidth()-rc.getLocation().x<5 || rc.getMapHeight()-rc.getLocation().y<5){
            return false;
        }
        if(nearADam) {
            for (MapInfo AT : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
                if (rc.canBuild(TrapType.WATER,AT.getMapLocation())) {
                    rc.build(TrapType.WATER, AT.getMapLocation());
                    return true;
                }
            }
        }
        return false;
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
        if(rc.getRoundNum()<200||rc.getCrumbs()<300){
            return;
        }
        for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
            TrapType toBeBuilt = TrapType.STUN;
            if(rc.getLocation().distanceSquaredTo(findClosestSpawnLocation(rc))>25&&!(rc.getLocation().x<5||rc.getLocation().y<5||rc.getMapWidth()-rc.getLocation().x<5 || rc.getMapHeight()-rc.getLocation().y<5))
                toBeBuilt = TrapType.WATER;
            else if(rc.getCrumbs()>1500)
                toBeBuilt = TrapType.EXPLOSIVE;
            if(rc.canBuild(toBeBuilt,t.getMapLocation()))
                rc.build(toBeBuilt,t.getMapLocation());

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



    //Code to make builders prepare to accept a carrier by going up towards them and placing a bomb
    public static void runBuilderToAcceptCarrier(RobotController rc) throws GameActionException {
        MapLocation c = Utilities.convertIntToLocation(rc.readSharedArray(58)); // Carrier
        MapLocation s = findClosestSpawnLocation(rc,c);  //Destination spawn location
        MapLocation p = rc.getLocation(); // self
        //this whole block of math nonsense finds the closest point on the line from the spawn location to the carrier
        //Don't try and step trace it's just a bunch of algebra
        double m = (c.y-s.y+0.0)/(c.x-c.y);
        double x = (p.y-s.y+m*s.x + p.x/m)/(m+1/m);
        double y = (x - s.x)*m+s.y;
        MapLocation destination = new MapLocation((int)x,(int)y);
        if(rc.getLocation().distanceSquaredTo(destination)<9)
            UpdateExplosionBorder(rc);
        Pathfinding.bugNav2(rc, destination);



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

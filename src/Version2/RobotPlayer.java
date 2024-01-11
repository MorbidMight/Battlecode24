package Version2;

import battlecode.common.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * RobotPlayer is the class that describes your main robot strategy.
 * The run() method inside this class is like your main function: this is what we'll call once your robot
 * is created!
 */
public strictfp class RobotPlayer {

    /**
     * We will use this variable to count the number of turns this robot has been alive.
     * You can use static variables like this to save any information you want. Keep in mind that even though
     * these variables are static, in Battlecode they aren't actually shared between your robots.
     */
    static int turnCount = 0;
    static MapLocation[] SpawnLocations = new MapLocation[27]; //All the spawn locations. low:close to center high:away from center
    static Direction preferredDirection = null; //For scouts, it's the direction their intending to go in
    static ArrayList<MapLocation> PlacesHaveBeen = new ArrayList<MapLocation>(); //To prevent scouts from backtracking
    static roles role;
    static final int MoatRadius = 9; //Radius of the moat squared

    static HashMap<MapLocation, MapInfo> seenLocations = new HashMap<MapLocation, MapInfo>();

    static HashSet<MapLocation> alreadyBeen = new HashSet<>();

    static boolean flagPlacer = false;
    //used for flagPlacer to target where they would like to place their flag
    static MapLocation flagDestination;

    static final int BombFrequency = 5; //number of turns between defensive builders trying to place a mine

    //used for flag placer only
    static ArrayList<MapLocation> prevDestinations;

    /**
     * A random number generator.
     * We will use this RNG to make some random moves. The Random class is provided by the java.util.Random
     * import at the top of this file. Here, we *seed* the RNG with a constant number (6147); this makes sure
     * we get the same sequence of numbers every time this code is run. This is very useful for debugging!
     */
    static final Random rng = new Random(6147);

    /** Array containing all the possible movement directions. */
    static final Direction[] directions = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
    };

    static enum roles {
        explorer, soldier, builder, healer
    }


    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * It is like the main function for your robot. If this method returns, the robot dies!
     *
     * @param rc  The RobotController object. You use it to perform actions from this robot, and to get
     *            information on its current status. Essentially your portal to interacting with the world.
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        while (true) {
            //changes explorers to soldiers at round 200
            if(rc.getRoundNum() == GameConstants.SETUP_ROUNDS && role == roles.explorer){
                role = roles.soldier;
            }
            //if can buy upgrade, buy an upgrade
            if(rc.canBuyGlobal(GlobalUpgrade.ACTION)){
                rc.buyGlobal(GlobalUpgrade.ACTION);
            }
            else if(rc.canBuyGlobal(GlobalUpgrade.HEALING)) {
                rc.buyGlobal(GlobalUpgrade.HEALING);
            }
            // This code runs during the entire lifespan of the robot, which is why it is in an infinite
            // loop. If we ever leave this loop and return from run(), the robot dies! At the end of the
            // loop, we call Clock.yield(), signifying that we've done everything we want to do.

            turnCount += 1;  // We have now been alive for one more turn!

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode.
            try {
                // Make sure you spawn your robot in before you attempt to take any actions!
                // Robots not spawned in do not have vision of any tiles and cannot perform any actions.
                if(turnCount==1) {//first turn fill the spawn location into the array ranked
                 SpawnLocations = rc.getAllySpawnLocations();
                }
                if (!rc.isSpawned()){
                    // try to spawn in every location asap
                    MapLocation[] spawnLocs = rc.getAllySpawnLocations();
                    int spawnIndex = 0;
                    while(spawnIndex < 26 && !rc.canSpawn(spawnLocs[spawnIndex])) {
                        spawnIndex++;
                    }
                        if (rc.canSpawn(spawnLocs[spawnIndex]) && spawnIndex <= 26) {
                            rc.spawn(spawnLocs[spawnIndex]);
                            //if third to last bit is 0, become a soldier/explorer and figure out which bit to flip
                            if(!Utilities.readBitSharedArray(rc, 1020)){
                                if(rc.getRoundNum() >= 200)
                                    role = roles.soldier;
                                else
                                    role = roles.explorer;
                                if(Utilities.readBitSharedArray(rc, 1023)){
                                    Utilities.editBitSharedArray(rc, 1023, true);
                                }
                                else if(Utilities.readBitSharedArray(rc, 1022)){
                                    Utilities.editBitSharedArray(rc, 1022, true);
                                }
                                else if(Utilities.readBitSharedArray(rc, 1021)){
                                    Utilities.editBitSharedArray(rc,1021, true);
                                }
                                else{
                                    Utilities.editBitSharedArray(rc,1020, true);
                                }
                            }
                            //become a builder, set last three bits to 0
                            else{
                                role = roles.builder;
                                Utilities.editBitSharedArray(rc, 1020, false);
                                Utilities.editBitSharedArray(rc, 1021, false);
                                Utilities.editBitSharedArray(rc, 1022, false);
                                Utilities.editBitSharedArray(rc, 1023, false);
                            }
                        }
                }
                else{
                    updateSeenLocations(rc);
                    switch(role){
                        case builder:
                            Builder.runBuilder(rc);
                            break;
                        case explorer:
                            Explorer.runExplorer(rc);
                            break;
                        case healer:
                            Healer.runHealer(rc);
                            break;
                        case soldier:
                            Soldier.runSoldier(rc);
                            break;
                    }
                    /* this code below is for flag movement, which we no longer do
                    //if spawn on the flag, pick it up and try to get it to the best corner
                    if (rc.canPickupFlag(rc.getLocation()) && rc.getRoundNum() <= 2)
                    {
                        rc.pickupFlag(rc.getLocation());
                        flagPlacer = true;
                        flagDestination = calculateFlagDestination(rc);
                        prevDestinations.add(flagDestination);
                    }
                    //runs a turn trying to place the flag or find a good place to do so
                    if(flagPlacer){
                        //if not a legal place, try other places
                        if(rc.canSenseLocation(flagDestination) && !rc.senseLegalStartingFlagPlacement(flagDestination)){
                            flagDestination = findNextBestFlagDestination(rc, flagDestination, prevDestinations);
                        }
                        Pathfinding.tryToMove(rc,flagDestination);
                        if(Objects.equals(rc.getLocation(), flagDestination) && rc.canDropFlag(flagDestination) && rc.senseLegalStartingFlagPlacement(flagDestination)){
                            rc.dropFlag(flagDestination);
                            flagPlacer = false;
                        }
                    }

                     */
                    //pickup enemy flag after setup phase ends
                    if (rc.canPickupFlag(rc.getLocation()) && rc.getRoundNum() > GameConstants.SETUP_ROUNDS){
                        rc.pickupFlag(rc.getLocation());
                    }
                    //if we have an enemy flag, bring it to the closest area
                    if (rc.hasFlag() && rc.getRoundNum() >= GameConstants.SETUP_ROUNDS){
                        MapLocation[] spawnLocs = rc.getAllySpawnLocations();
                        MapLocation targetLoc;
                        int distance_1 = rc.getLocation().distanceSquaredTo(spawnLocs[5]);
                        int distance_2 = rc.getLocation().distanceSquaredTo(spawnLocs[14]);
                        int distance_3 = rc.getLocation().distanceSquaredTo(spawnLocs[23]);
                        if(distance_1 < distance_2){
                            if(distance_1 < distance_3){
                                targetLoc = spawnLocs[5];
                            }
                            else{
                                targetLoc = spawnLocs[23];
                            }
                        }
                        else{
                            targetLoc = spawnLocs[14];
                        }
                        Direction dir = rc.getLocation().directionTo(targetLoc);
                        Pathfinding.tryToMove(rc, targetLoc);
                    }
                    MoveAwayFromSpawnLocations(rc);
                    Direction dir = directions[rng.nextInt(directions.length)];
                    MapLocation nextLoc = rc.getLocation().add(dir);
                    RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
                    if (enemyRobots.length > 0 && rc.canAttack(enemyRobots[0].location)){
                        rc.attack(enemyRobots[0].location);
                    }

                    // Rarely attempt placing traps behind the robot.
                    MapLocation prevLoc = rc.getLocation().subtract(dir);
                    if (rc.canBuild(TrapType.EXPLOSIVE, prevLoc) && rng.nextInt() % 37 == 1)
                        rc.build(TrapType.EXPLOSIVE, prevLoc);
                }

            } catch (GameActionException e) {
                // Oh no! It looks like we did something illegal in the Battlecode world. You should
                // handle GameActionExceptions judiciously, in case unexpected events occur in the game
                // world. Remember, uncaught exceptions cause your robot to explode!
                System.out.println("GameActionException");
                e.printStackTrace();

            } catch (Exception e) {
                // Oh no! It looks like our code tried to do something bad. This isn't a
                // GameActionException, so it's more likely to be a bug in our code.
                System.out.println("Exception");
                e.printStackTrace();

            } finally {
                // Signify we've done everything we want to do, thereby ending our turn.
                // This will make our code wait until the next turn, and then perform this loop again.
                Clock.yield();
            }
            // End of loop: go back to the top. Clock.yield() has ended, so it's time for another turn!
        }

        // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
    }


    public static int DistanceFromNearestFlag(MapLocation i,RobotController rc) throws GameActionException {
        MapLocation[] f = new MapLocation[3]; // locations of all the flags
        MapLocation cornerFlag = calculateFlagDestination(rc);
        f[0] = cornerFlag;
        if(f[0].x==0)
            f[1] = new MapLocation(6,0);
        else
            f[1] = new MapLocation(rc.getMapWidth()-7,0);

        if(f[0].y==0)
            f[2] = new MapLocation(0,6);
        else
            f[2] = new MapLocation(0,rc.getMapHeight()-7);

        int[] distancesToEach = new int[3];
        distancesToEach[0]=f[0].distanceSquaredTo(i);
        distancesToEach[1]=f[1].distanceSquaredTo(i);
        distancesToEach[2]=f[2].distanceSquaredTo(i);
        int temp =  Math.min(distancesToEach[0],distancesToEach[1]);
        return Math.min(temp,distancesToEach[2]);

    }

    //returns closest spawn location
    public static MapLocation findClosestSpawnLocation(RobotController rc){
        if (rc.hasFlag() && rc.getRoundNum() >= GameConstants.SETUP_ROUNDS){
            MapLocation[] spawnLocs = rc.getAllySpawnLocations();
            MapLocation targetLoc;
            int distance_1 = rc.getLocation().distanceSquaredTo(spawnLocs[5]);
            int distance_2 = rc.getLocation().distanceSquaredTo(spawnLocs[14]);
            int distance_3 = rc.getLocation().distanceSquaredTo(spawnLocs[23]);
            if(distance_1 < distance_2){
                if(distance_1 < distance_3){
                    targetLoc = spawnLocs[5];
                }
                else{
                    targetLoc = spawnLocs[23];
                }
            }
            else{
                targetLoc = spawnLocs[14];
            }
            return targetLoc;
        }
        else{
            return null;
        }
    }

    public static MapLocation closestSeenEnemyFlag(RobotController rc) throws GameActionException {
        FlagInfo[] nearbyFlags = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
        if (nearbyFlags.length > 0) {
            int closestDist = rc.getLocation().distanceSquaredTo(nearbyFlags[0].getLocation());
            int closestIndex = 0;
            for (int i = 1; i < nearbyFlags.length; i++) {
                if (rc.getLocation().distanceSquaredTo(nearbyFlags[i].getLocation()) < closestDist) {
                    closestIndex = i;
                    closestDist = rc.getLocation().distanceSquaredTo(nearbyFlags[i].getLocation());
                }
            }
            return nearbyFlags[closestIndex].getLocation();
        }
        else{
            return null;
        }
    }

    public static MapLocation findClosestBroadcastFlags(RobotController rc){
        MapLocation[] locations = rc.senseBroadcastFlagLocations();
        if (locations.length > 0) {
            int closestDist = rc.getLocation().distanceSquaredTo(locations[0]);
            int closestIndex = 0;
            for (int i = 1; i < locations.length; i++) {
                if (rc.getLocation().distanceSquaredTo(locations[i]) < closestDist) {
                    closestIndex = i;
                    closestDist = rc.getLocation().distanceSquaredTo(locations[i]);
                }
            }
            return locations[closestIndex];
        }
        else{
            return null;
        }
    }

    public static void updateEnemyRobots(RobotController rc) throws GameActionException{
        // Sensing methods can be passed in a radius of -1 to automatically 
        // use the largest possible value.
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        if (enemyRobots.length != 0){
            // Save an array of locations with enemy robots in them for future use.
            MapLocation[] enemyLocations = new MapLocation[enemyRobots.length];
            for (int i = 0; i < enemyRobots.length; i++){
                enemyLocations[i] = enemyRobots[i].getLocation();
            }
            // Let the rest of our team know how many enemy robots we see!
            if (rc.canWriteSharedArray(0, enemyRobots.length)){
                rc.writeSharedArray(0, enemyRobots.length);
                int numEnemies = rc.readSharedArray(0);
            }
            int x = rc.readSharedArray(1);
        }
    }

    static void MoveAwayFromSpawnLocations(RobotController rc) throws GameActionException {
        if(LocIsSpawnLocation(rc.getLocation())) {
            //remove loop for efficiency later
            for (Direction d : directions) {
                for (MapLocation l : SpawnLocations) {
                    if (!rc.getLocation().add(d).equals(l) && rc.canMove(d))
                        rc.move(d);
                }
            }
        }
        int t = rng.nextInt(directions.length);
        //make ducks go to center
        //maybe implement keeping distance
        MapLocation centerOfMap = new MapLocation(rc.getMapWidth()/2, rc.getMapHeight()/2);
        Direction directionTowardsCenter = rc.getLocation().directionTo(centerOfMap);
        if(rc.canMove(directionTowardsCenter) && turnCount >150 && turnCount < 200)
        {
            rc.move(directionTowardsCenter);
        }
        else
        {
            //change direction taken based off robotID
            if(rc.getID() % 2 == 0)
            {
                for(int i = 0;i<8;i++) {
                    Direction dir = directions[(t + i) % 8];
                    if (!LocIsSpawnLocation(rc.getLocation().add(dir)) && rc.canMove(dir)) {
                        rc.move(dir);
                    }
                }
            }
            else
            {
                for(int i = 0;i<8;i++) {
                    Direction dir = directions[8 - ((t + i) % 8) -1];
                    if (!LocIsSpawnLocation(rc.getLocation().add(dir)) && rc.canMove(dir)) {
                        rc.move(dir);
                    }
                }
           }
        }
    }
    static boolean LocIsSpawnLocation(MapLocation l){
        for(MapLocation d:SpawnLocations){
            if(l.equals(d))
                return true;
        }
        return false;
    }
    //finds first open index to write a task too, returns -1 if all 45 slots are filled
    static int openTaskIndex(RobotController rc) throws GameActionException {
        int index = 6;
        //eventually - codegen into list of instructions
        for(int i = index; i < index + 45; i++){
            if(!Utilities.readBitSharedArray(rc, 12)){
                return i;
            }
        }
        return -1;
    }

    //takes the average of the three flag locations, and then returns the coords of the nearest corner
    static MapLocation calculateFlagDestination(RobotController rc){
        MapLocation[] spawnLocs = rc.getAllySpawnLocations();
        MapLocation[] flagOrigins = new MapLocation[3];
        flagOrigins[0] = spawnLocs[5];
        flagOrigins[1] = spawnLocs[14];
        flagOrigins[2] = spawnLocs[23];
        float x = (float) (flagOrigins[0].x + flagOrigins[1].x + flagOrigins[2].x) / 3;
        float y = (float) (flagOrigins[0].y + flagOrigins[1].y + flagOrigins[2].y) / 3;
        int mapSizeX = rc.getMapWidth();
        int mapSizeY = rc.getMapHeight();
        //if x is to the right of the middle, desired x will be equal to mapsize X - else, 0
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
        return new MapLocation((int)x, (int)y);
    }

    public static MapLocation lowestHealth(RobotInfo[] enemies){
        int lowHealth = enemies[0].health;
        MapLocation toAttack = enemies[0].getLocation();
        for(RobotInfo enemy : enemies){
            if(enemy.health < lowHealth){
                lowHealth = enemy.health;
                toAttack = enemy.location;
            }
        }
        return toAttack;
    }

    static void updateSeenLocations(RobotController rc)
    {
        MapInfo[] locations = rc.senseNearbyMapInfos();
        //this might be inefficient maybe switch to for loop
        for (MapInfo info: locations)
        {
            seenLocations.put(info.getMapLocation(), info);
        }
    }

    //takes in a failed flag destination, and tries to find a good one around it - deprecated method
    public static MapLocation findNextBestFlagDestination(RobotController rc, MapLocation currDestination, ArrayList<MapLocation> prevDestinations) throws GameActionException {
        return new MapLocation(0, 0);
    }

}

package Version15MovingFlags;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

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

    static int[] turnsWithKills = new int[25]; //The turns when the robot preformed a kill, compares it to the the current turn count to figure out when a

    //used to unlock spawn locs 20 turns after locking it, if no longer under attack - otherwise, reset count
    static int countSinceLocked = 0;
    //counts number of turns since a flag sitter has seen a friendly flag
    static int countSinceSeenFlag = 0;
    static int turnCount = 0;
    static MapLocation[] SpawnLocations = new MapLocation[27]; //All the spawn locations. low:close to center high:away from center
    static roles role;
static MapLocation builderBombCircleCenter = null;
    static boolean SittingOnFlag = false; //for builders if they sit on the flag and spam explosion bombs
    static final int MoatRadius = 9; //Radius of the moat squared

    static HashMap<MapLocation, MapInfo> seenLocations = new HashMap<MapLocation, MapInfo>();

    static HashMap<MapLocation, Integer> alreadyBeen = new HashMap<MapLocation, Integer>();


    static final int BombFrequency = 5; //number of turns between defensive builders trying to place a mine

    //used for flag placer only
    static ArrayList<MapLocation> prevDestinations;

    //Ratios for spawning
    public static final int NUMSOLDIERS = 46;
    public static final int NUMBUILDERS = 0;
    public static final int OFFENSIVEBUILDERS = 1;

    public static final int NUMHEALERS = 0;
    //offensive builders is 50 - numsoldiers - numbuilders - 3

    //flag sitters will always be 3, heals is 50 - (soldiers + builders + flag sitters)
    static Random rng;

    //used by explorers
    static int turnsSinceLocGen = 0;
    static MapLocation targetLoc;

    static int turnOrder = 0;

    static int MAX_MAP_DIST_SQUARED;

    /**
     * A random number generator.
     * We will use this RNG to make some random moves. The Random class is provided by the java.util.Random
     * import at the top of this file. Here, we *seed* the RNG with a constant number (6147); this makes sure
     * we get the same sequence of numbers every time this code is run. This is very useful for debugging!
     */

    /**
     * Array containing all the possible movement directions.
     */
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

    enum roles {
        explorer, soldier, builder, healer, offensiveBuilder, defensiveBuilder
    }


    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * It is like the main function for your robot. If this method returns, the robot dies!
     *
     * @param rc The RobotController object. You use it to perform actions from this robot, and to get
     *           information on its current status. Essentially your portal to interacting with the world.
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {
        initialize(rc);
        while (true) {
            Utilities.checkForRevivedRobots(rc,turnsWithKills);

            //changes explorers to soldiers at round 200
            if (rc.getRoundNum() >= GameConstants.SETUP_ROUNDS && role == roles.explorer) {
                role = roles.soldier;
            }
            //if can buy upgrade, buy an upgrade
            if (rc.canBuyGlobal(GlobalUpgrade.ACTION)) {
                rc.buyGlobal(GlobalUpgrade.ACTION);
            } else if (rc.canBuyGlobal(GlobalUpgrade.HEALING)) {
                rc.buyGlobal(GlobalUpgrade.HEALING);
            }
            else if(rc.canBuyGlobal(GlobalUpgrade.CAPTURING)){
                rc.buyGlobal(GlobalUpgrade.CAPTURING);
            }
            // This code runs during the entire lifespan of the robot, which is why it is in an infinite
            // loop. If we ever leave this loop and return from run(), the robot dies! At the end of the
            // loop, we call Clock.yield(), signifying that we've done everything we want to do.

            turnCount += 1;  // We have now been alive for one more turn!

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode.
            try {
                if(turnOrder == 0)
                {
                    HeadquarterDuck.runHeadquarterDuck(rc);
                }
                // Make sure you spawn your robot in before you attempt to take any actions!
                // Robots not spawned in do not have vision of any tiles and cannot perform any actions.
                if (turnCount == 1) {//first turn fill the spawn location into the array ranked
                    SpawnLocations = rc.getAllySpawnLocations();
                }
                if (!rc.isSpawned()) {
                    rc.writeSharedArray(7, rc.readSharedArray(7) + 1);
                    //get our robots out onto the map
                    if (turnCount <= 10) {
                        MapLocation[] spawnLocs = rc.getAllySpawnLocations();
                        int spawnIndex = 0;
                        while (spawnIndex < 26 && !rc.canSpawn(spawnLocs[spawnIndex])) {
                            spawnIndex++;
                        }
                        if(rc.canSpawn(SpawnLocations[spawnIndex])){
                            rc.spawn(SpawnLocations[spawnIndex]);
                            if(rc.senseNearbyFlags(-1,rc.getTeam())[0].getLocation().equals(rc.getLocation())){
                                role = roles.builder;
                                SittingOnFlag = true;
                            }
                            else{
                                role = roles.explorer;
                            }
                            //role = roles.explorer;
                        }
                    } else {
                        if (SittingOnFlag) {
                            if (rc.canSpawn(Utilities.convertIntToLocation(rc.readSharedArray(0)))) {
                                rc.spawn(Utilities.convertIntToLocation(rc.readSharedArray(0)));
                            } else if (rc.canSpawn(Utilities.convertIntToLocation(rc.readSharedArray(1)))) {
                                rc.spawn(Utilities.convertIntToLocation(rc.readSharedArray(1)));
                            } else if (rc.canSpawn(Utilities.convertIntToLocation(rc.readSharedArray(2)))) {
                                rc.spawn(Utilities.convertIntToLocation(rc.readSharedArray(2)));
                            }
                        } else {
                            if (!rc.isSpawned()) {

                                //decide which place to spawn at based on last two bits of shared array
                                //loop through spawn locations, try adjacent ones, then try non adjacent ones
                                MapLocation targetSpawn = null;
                                if (Utilities.readBitSharedArray(rc, 1022)) {
                                    targetSpawn = Utilities.convertIntToLocation(rc.readSharedArray(2));
                                } else if (Utilities.readBitSharedArray(rc, 1023)) {
                                    targetSpawn = Utilities.convertIntToLocation(rc.readSharedArray(1));
                                } else {
                                    targetSpawn = Utilities.convertIntToLocation(rc.readSharedArray(0));
                                }
                                //try to spawn at adjacent locations
                                for (int i = 0; i <= 26; i++) {
                                    if ((targetSpawn.isAdjacentTo(SpawnLocations[i]) || targetSpawn.equals(SpawnLocations[i])) && rc.canSpawn(SpawnLocations[i])) {
                                        rc.spawn(SpawnLocations[i]);
                                        break;
                                    }
                                }
                                //try all other spaces if intended ones dont work
                                if (!rc.isSpawned()) {
                                    for (int i = 0; i < 26; i++) {
                                        if (rc.canSpawn(SpawnLocations[i])) {
                                            rc.spawn(SpawnLocations[i]);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    doRoutineTurnTasks(rc);
                    //write our own flag locations to shared array at start
                    if (turnCount == 2 && rc.senseNearbyFlags(-1)[0].getLocation().equals(rc.getLocation())) {
                        int toPush = Utilities.convertLocationToInt(rc.getLocation());
                        if (rc.readSharedArray(0) == 0) {
                            rc.writeSharedArray(0, toPush);
                            rc.setIndicatorString("look at me!!");
                        } else if (rc.readSharedArray(1) == 0 && rc.readSharedArray(0) != toPush) {
                            rc.writeSharedArray(1, toPush);
                            rc.setIndicatorString("look at me!!");
                        } else if (rc.readSharedArray(2) == 0 && rc.readSharedArray(1) != toPush) {
                            rc.writeSharedArray(2, toPush);
                            rc.setIndicatorString("look at me!!");
                        }
                    }

                    if (rc.isSpawned()) {
                        switch (role) {
                            case builder:
                                Builder.runBuilder(rc);
                                rc.setIndicatorString("builder");
                                break;
                            case explorer:
                                Explorer.runExplorer(rc);
                                rc.setIndicatorString("explorer");
                                break;
                            case healer:
                                Healer.runHealer(rc);
                                rc.setIndicatorString("healer");
                                break;
                            case soldier:
                                Soldier.runSoldier(rc);
                                break;
                            case offensiveBuilder:
                                OffensiveBuilder.runOffensiveBuilder(rc);
                                break;
                            case defensiveBuilder:
                                DefensiveBuilder.runDefensiveBuilder(rc);
                                break;
                        }
                    }
//
//                    //pickup enemy flag after setup phase ends
//                    if (rc.canPickupFlag(rc.getLocation()) && rc.getRoundNum() > GameConstants.SETUP_ROUNDS) {
//                        rc.pickupFlag(rc.getLocation());
//                    }
//                    //if we have an enemy flag, bring it to the closest area
//                    if (rc.hasFlag() && rc.getRoundNum() >= GameConstants.SETUP_ROUNDS) {
//                        Carrier.runCarrier(rc);
//                    }
//                    if(rc.getRoundNum() < 10)
//                        MoveAwayFromSpawnLocations(rc);
                    /*
                    switch (role) {
                        case builder:
                            rc.setIndicatorDot(rc.getLocation(), 0, 0, 255);
                            break;
                        case explorer:
                            rc.setIndicatorDot(rc.getLocation(), 255, 0, 0);
                            break;
                        case healer:
                            rc.setIndicatorDot(rc.getLocation(), 0, 255, 0);
                            break;
                        case soldier:
                            rc.setIndicatorDot(rc.getLocation(), 255, 0, 0);
                            break;
                    }*/

                }
                MapLocation[] clusters = Utilities.getLastRoundClusters(rc);
                if(turnOrder == 0)
                {
//                    rc.setIndicatorDot(clusters[0], 255, 0, 0);
//                    rc.setIndicatorDot(clusters[1], 0, 255, 0);
//                    rc.setIndicatorDot(clusters[2], 0, 0, 255);
                }


            } catch (GameActionException e) {
                // Oh no! It looks like we did something illegal in the Battlecode world. You should
                // handle GameActionExceptions judiciously, in case unexpected events occur in the game
                // world. Remember, uncaught exceptions cause your robot to explode!
                System.out.println("GameActionException | " + "Turn Order : " + turnOrder);
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

    //Perform anything that needs to be done before the first turn
    public static void initialize(RobotController rc) throws GameActionException
    {
        rng = new Random(rc.getID());
        turnOrder = rc.readSharedArray(62);
        rc.writeSharedArray(62,turnOrder + 1);
        MAX_MAP_DIST_SQUARED = rc.getMapHeight() * rc.getMapHeight() + rc.getMapWidth() * rc.getMapWidth();
        rc.setIndicatorString(turnOrder + "");
    }

    //Perform tasks every round
    public static void doRoutineTurnTasks(RobotController rc) throws GameActionException
    {
        MapLocation closestCluster = Utilities.getClosestCluster(rc);
        //if(closestCluster != null)
          //  rc.setIndicatorLine(rc.getLocation(), closestCluster, 0, 255, 0);
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
        for (RobotInfo robot : enemyRobots)
        {
            Utilities.updateEnemyCluster(rc, robot.location);
        }
        Utilities.recordEnemies(rc, enemyRobots);
        Utilities.clearObsoleteEnemies(rc);
        Utilities.verifyFlagLocations(rc);
        Utilities.writeFlagLocations(rc);
    }


    public static int DistanceFromNearestFlag(MapLocation i, RobotController rc) throws GameActionException {
        MapLocation[] f = new MapLocation[3]; // locations of all the flags
        MapLocation cornerFlag = calculateFlagDestination(rc);
        f[0] = cornerFlag;
        if (f[0].x == 0)
            f[1] = new MapLocation(6, 0);
        else
            f[1] = new MapLocation(rc.getMapWidth() - 7, 0);

        if (f[0].y == 0)
            f[2] = new MapLocation(0, 6);
        else
            f[2] = new MapLocation(0, rc.getMapHeight() - 7);

        int[] distancesToEach = new int[3];
        distancesToEach[0] = f[0].distanceSquaredTo(i);
        distancesToEach[1] = f[1].distanceSquaredTo(i);
        distancesToEach[2] = f[2].distanceSquaredTo(i);
        int temp = Math.min(distancesToEach[0], distancesToEach[1]);
        return Math.min(temp, distancesToEach[2]);
    }

    //returns closest spawn location
    public static MapLocation findClosestSpawnLocation(RobotController rc) throws GameActionException {
        MapLocation targetLoc = null;
        int distance_1 = rc.getLocation().distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(0)));
        int distance_2 = rc.getLocation().distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(1)));
        int distance_3 = rc.getLocation().distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(2)));
        if (distance_1 < distance_2) {
            if (distance_1 < distance_3) {
                targetLoc = Utilities.convertIntToLocation(rc.readSharedArray(0));
            } else {
                targetLoc = Utilities.convertIntToLocation(rc.readSharedArray(2));
            }
        } else {
            if (distance_2 < distance_3)
                targetLoc = Utilities.convertIntToLocation(rc.readSharedArray(1));
            else
                targetLoc = Utilities.convertIntToLocation(rc.readSharedArray(2));
        }
        return targetLoc;
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
        } else {
            return null;
        }
    }

    public static MapLocation findClosestBroadcastFlags(RobotController rc) {
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
        } else {
            return null;
        }
    }
    public static int findClosestSpawnLocationToCluster(RobotController rc) throws GameActionException {
        MapLocation spawn1 = Utilities.convertIntToLocation(rc.readSharedArray(0));
        MapLocation spawn2 = Utilities.convertIntToLocation(rc.readSharedArray(1));
        MapLocation spawn3 = Utilities.convertIntToLocation(rc.readSharedArray(2));
        int distance_1 = spawn1.distanceSquaredTo(Utilities.getClosestCluster(rc, spawn1));
        int distance_2 = spawn1.distanceSquaredTo(Utilities.getClosestCluster(rc, spawn2));
        int distance_3 = spawn1.distanceSquaredTo(Utilities.getClosestCluster(rc, spawn3));
        if (distance_1 < distance_2) {
            if (distance_1 < distance_3) {
                return 0;
            } else {
                return 2;
            }
        } else {
            if (distance_2 < distance_3)
                return 1;
            else
                return 2;
        }
    }

    //returns broadcastflag at index 0 so that all soldiers will go to the same general area and coordinate
    public static MapLocation findCoordinatedBroadcastFlag(RobotController rc) {
        if (rc.senseBroadcastFlagLocations().length != 0)
            return rc.senseBroadcastFlagLocations()[0];
        return null;
    }


    public static void updateEnemyRobots(RobotController rc) throws GameActionException {
        // Sensing methods can be passed in a radius of -1 to automatically 
        // use the largest possible value.
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        if (enemyRobots.length != 0) {
            // Save an array of locations with enemy robots in them for future use.
            MapLocation[] enemyLocations = new MapLocation[enemyRobots.length];
            for (int i = 0; i < enemyRobots.length; i++) {
                enemyLocations[i] = enemyRobots[i].getLocation();
            }
            // Let the rest of our team know how many enemy robots we see!
            if (rc.canWriteSharedArray(0, enemyRobots.length)) {
                rc.writeSharedArray(0, enemyRobots.length);
                int numEnemies = rc.readSharedArray(0);
            }
            int x = rc.readSharedArray(1);
        }
    }


    static boolean LocIsSpawnLocation(MapLocation l) {
        for (MapLocation d : SpawnLocations) {
            if (l.equals(d))
                return true;
        }
        return false;
    }
    //finds first open index to write a task too, returns -1 if all 45 slots are filled


    //takes the average of the three flag locations, and then returns the coords of the nearest corner
    static MapLocation calculateFlagDestination(RobotController rc) {
        MapLocation[] spawnLocs = rc.getAllySpawnLocations();
        MapLocation[] flagOrigins = new MapLocation[3];
        flagOrigins[0] = spawnLocs[4];
        flagOrigins[1] = spawnLocs[13];
        flagOrigins[2] = spawnLocs[22];
        float x = (float) (flagOrigins[0].x + flagOrigins[1].x + flagOrigins[2].x) / 3;
        float y = (float) (flagOrigins[0].y + flagOrigins[1].y + flagOrigins[2].y) / 3;
        int mapSizeX = rc.getMapWidth();
        int mapSizeY = rc.getMapHeight();
        //if x is to the right of the middle, desired x will be equal to mapsize X - else, 0
        if (x > (float) mapSizeX / 2) {
            x = mapSizeX - 1;
        } else {
            x = 0;
        }
        // see above, but for y
        if (y > (float) mapSizeY / 2) {
            y = mapSizeY - 1;
        } else {
            y = 0;
        }
        return new MapLocation((int) x, (int) y);
    }

    public static MapLocation lowestHealth(RobotInfo[] enemies) {
        if (enemies.length == 0)
            return null;
        int lowHealth = enemies[0].health;
        boolean isSame = true;
        MapLocation toAttack = enemies[0].getLocation();
        for (RobotInfo enemy : enemies) {
            if (enemy.hasFlag) {
                return enemy.getLocation();
            }
            if (enemy.health < lowHealth) {
                lowHealth = enemy.health;
                toAttack = enemy.location;
                isSame = false;
            }
        }
        if (isSame) {
            int lowestID = enemies[0].ID;
            for (RobotInfo enemy : enemies) {
                if (enemy.ID < lowestID) {
                    lowestID = enemy.ID;
                    toAttack = enemy.getLocation();
                }
            }
        }
        return toAttack;
    }

    public static MapLocation lowestHealthOrStunned(RobotInfo[] enemies, ArrayList<RobotInfo> stunList){
        if (enemies.length == 0)
            return null;
        int lowHealth = enemies[0].health;
        boolean isSame = true;
        MapLocation toAttack = enemies[0].getLocation();
        for (RobotInfo enemy : enemies) {
            if (enemy.hasFlag) {
                return enemy.getLocation();
            }
            //give priority to stunned enemies
            if(stunList.contains(enemy)){
                if ((enemy.health - 500) < lowHealth) {
                    lowHealth = enemy.health - 500;
                    toAttack = enemy.location;
                    isSame = false;
                }
            }
            if (enemy.health < lowHealth) {
                lowHealth = enemy.health;
                toAttack = enemy.location;
                isSame = false;
            }
        }
        if (isSame) {
            int lowestID = enemies[0].ID;
            for (RobotInfo enemy : enemies) {
                if (enemy.ID < lowestID) {
                    lowestID = enemy.ID;
                    toAttack = enemy.getLocation();
                }
            }
        }
        return toAttack;
    }

    static void updateSeenLocations(RobotController rc) {
        MapInfo[] locations = rc.senseNearbyMapInfos();
        //this might be inefficient maybe switch to for loop
        for (MapInfo info : locations) {
            seenLocations.put(info.getMapLocation(), info);
        }
    }

    public static MapLocation findClosestActualFlag(RobotController rc) throws GameActionException {
        ArrayList<MapLocation> locations = new ArrayList<>();
        for(int i = 3; i < 6; i++){
            if(rc.readSharedArray(i) != 0){
                locations.add(Utilities.convertIntToLocation(rc.readSharedArray(i)));
            }
        }
        if(locations.isEmpty())
            return null;
        int closestDist = rc.getLocation().distanceSquaredTo(locations.get(0));
        int closestIndex = 0;
        for (int i = 1; i < locations.size(); i++) {
            if (rc.getLocation().distanceSquaredTo(locations.get(i)) < closestDist) {
                closestIndex = i;
                closestDist = rc.getLocation().distanceSquaredTo(locations.get(i));
            }
        }
        return locations.get(closestIndex);
    }

    public static void incrementSoldier(RobotController rc) throws GameActionException {
        rc.writeSharedArray(52, rc.readSharedArray(52) + 1);
    }

    public static void incrementBuilder(RobotController rc) throws GameActionException {
        rc.writeSharedArray(53, rc.readSharedArray(53) + 1);
    }

    public static void incrementHealer(RobotController rc) throws GameActionException {
        rc.writeSharedArray(54, rc.readSharedArray(54) + 1);
    }

    public static void checkFlagDropped(RobotController rc) throws GameActionException {
        MapLocation tempLocation = Utilities.convertIntToLocation(rc.readSharedArray(58));
        if (!rc.canSenseLocation(tempLocation)) return;
        if (rc.readSharedArray(58) == 0) return;
        if (rc.canSenseRobotAtLocation(tempLocation)) {
            if (!rc.senseRobotAtLocation(tempLocation).hasFlag()) {
                rc.writeSharedArray(58, 0);
            }
        } else {
            rc.writeSharedArray(58, 0);
        }
    }

    public static void checkEnemyHasOurFlag(RobotController rc) throws GameActionException {
        RobotInfo[] robots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        for (RobotInfo robot : robots) {
            if (robot.hasFlag()) {
                rc.writeSharedArray(58, Utilities.convertLocationToInt(robot.getLocation()));
            }
        }
    }

    //returns either 0 - 00, 1 - 01, 2 - 10 after determining our closest spawn location to the coordinated broadcast

    public static int findClosestSpawnLocationToCoordinatedBroadcast(RobotController rc) throws GameActionException {
        MapLocation coordinatedTarget = findCoordinatedBroadcastFlag(rc);
        if(coordinatedTarget == null)
            return -1;
        int distance_1 = coordinatedTarget.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(0)));
        int distance_2 = coordinatedTarget.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(1)));
        int distance_3 = coordinatedTarget.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(2)));
        if (distance_1 < distance_2) {
            if (distance_1 < distance_3) {
                return 0;
            } else {
                return 2;
            }
        } else {
            if (distance_2 < distance_3)
                return 1;
            else
                return 2;
        }
    }
    public static int findClosestSpawnLocationToCoordinatedTarget(RobotController rc) throws GameActionException {
        MapLocation coordinatedTarget = Soldier.findCoordinatedActualFlag(rc);
        if(coordinatedTarget == null)
            return -1;
        int distance_1 = coordinatedTarget.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(0)));
        int distance_2 = coordinatedTarget.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(1)));
        int distance_3 = coordinatedTarget.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(2)));
        if (distance_1 < distance_2) {
            if (distance_1 < distance_3) {
                return 0;
            } else {
                return 2;
            }
        } else {
            if (distance_2 < distance_3)
                return 1;
            else
                return 2;
        }
    }
    public static int findClosestSpawnLocationToStolenFlag(RobotController rc, StolenFlag s) throws GameActionException {
        MapLocation target = s.location;
        int distance_1 = target.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(0)));
        int distance_2 = target.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(1)));
        int distance_3 = target.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(2)));
        if (distance_1 < distance_2) {
            if (distance_1 < distance_3) {
                return 0;
            } else {
                return 2;
            }
        } else {
            if (distance_2 < distance_3)
                return 1;
            else
                return 2;
        }
    }

    public static void updateAlreadyBeen(RobotController rc)
    {
        MapLocation location = rc.getLocation();
        if(alreadyBeen.containsKey(rc.getLocation()))
            alreadyBeen.put(location, alreadyBeen.get(location) + 1);
        else
            alreadyBeen.put(location, 1);
    }
}

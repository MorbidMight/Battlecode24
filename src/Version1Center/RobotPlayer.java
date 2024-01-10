package Version1Center;

import battlecode.common.*;

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
    static Direction preferredDirection = null; //For scouts it's the direction their intending to go in
    static ArrayList<MapLocation> PlacesHaveBeen = new ArrayList<MapLocation>(); //To prevent scouts from backtracking
    static roles role;

    static boolean flagPlacer = false;
    //used for flagPlacer to target where they would like to place their flag
    static int[] flagDestination;

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
                    switch(role){
                        case builder:
                            runBuilder(rc);
                            break;
                        case explorer:
                            runExplorer(rc);
                            break;
                        case healer:
                            runHealer(rc);
                            break;
                        case soldier:
                            runSoldier(rc);
                            break;
                    }
                    //if spawn on the flag, pick it up and try to get it to the best corner
                    if (rc.canPickupFlag(rc.getLocation()) && rc.getRoundNum() <= 2){
                        rc.pickupFlag(rc.getLocation());
                        flagPlacer = true;
                        flagDestination = calculateFlagDestination(rc);
                    }
                    //runs a turn trying to place the flag or find a good place to do so
                    if(flagPlacer){
                        FlagInfo[] nearbyFlags = rc.senseNearbyFlags(-1);
                        for(FlagInfo flag : nearbyFlags){
                            if(flag.getLocation() == rc.getLocation())
                                continue;
                            if(flag.getLocation().x == flagDestination[0] && flag.getLocation().y == flagDestination[1]){
                                if(flagDestination[0] == rc.getMapWidth() - 1 || flagDestination[0] == 0){
                                    if(flagDestination[0] == 0){
                                        flagDestination[0] = 7;
                                    }
                                    else{
                                        flagDestination[0] = flagDestination[0] - 7;
                                    }
                                }
                                else{
                                    if(flagDestination[0] == 7){
                                        flagDestination[0] = 0;
                                    }
                                    else{
                                        flagDestination[0] = rc.getMapWidth()-1;
                                    }
                                    if(flagDestination[1] == 0){
                                        flagDestination[1] = 7;
                                    }
                                    else{
                                        flagDestination[1] = flagDestination[1] - 7;
                                    }
                                }
                            }
                        }
                        Direction direction = rc.getLocation().directionTo(new MapLocation(flagDestination[0], flagDestination[1]));
                        if(rc.canMove(direction)){
                            rc.move(direction);
                        }
                        if(Objects.equals(rc.getLocation(), new MapLocation(flagDestination[0], flagDestination[1])) && rc.canDropFlag(new MapLocation(flagDestination[0], flagDestination[1]))){
                            rc.dropFlag(new MapLocation(flagDestination[0], flagDestination[1]));
                            flagPlacer = false;
                        }
                    }
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
                        if (rc.canMove(dir)) rc.move(dir);
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

    public static void runBuilder(RobotController rc) throws GameActionException{
        //Go to flag from array
        //Dig water around the flag
        //build water traps around the flag
        //Head towards the dam to be on standby


    }

    public static void runHealer(RobotController rc) throws GameActionException{

    }

    public static void runSoldier(RobotController rc) throws GameActionException{
        boolean hasDirection = false;
        //blank declaration, will be set by something
        Direction dir = Direction.CENTER;
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
            dir = rc.getLocation().directionTo(targetLoc);
            if (rc.canMove(dir)) {
                rc.move(dir);
                hasDirection = true;
            }
        }
        if(!hasDirection) {
            //if we can see a flag, go towards it
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
                dir = rc.getLocation().directionTo(nearbyFlags[closestIndex].getLocation());
                if (rc.canMove(dir)) {
                    hasDirection = true;
                }
            }
        }
        if(!hasDirection) {
            //finally, find the closest enemy broadcasted flag
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
                dir = rc.getLocation().directionTo(locations[closestIndex]);
                if (rc.canMove(dir)) {
                    hasDirection = true;
                }
            }
        }
        if(hasDirection && rc.senseNearbyRobots(-1, rc.getTeam()).length > rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length){
            if(rc.canMove(dir))
                rc.move(dir);
        }
        else if(hasDirection){
            if(rc.canMove(dir.opposite()))
                rc.move(dir.opposite());
        }
        //pickup enemy flag if we can
        if (rc.canPickupFlag(rc.getLocation()) && rc.getRoundNum() > GameConstants.SETUP_ROUNDS){
            rc.pickupFlag(rc.getLocation());
        }
        //attack
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        RobotInfo[] allyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        if (enemyRobots.length > 0){
            MapLocation toAttack = lowestHealth(enemyRobots);
            if(rc.canAttack(toAttack))
                rc.attack(toAttack);
        }
        if(enemyRobots.length == 0 && allyRobots.length > 0){
            for (RobotInfo allyRobot : allyRobots) {
                if (rc.canHeal(allyRobot.getLocation())) {
                    rc.heal(allyRobot.getLocation());
                    break;
                }
            }
        }
    }

    public static void runExplorer(RobotController rc) throws GameActionException{
        int cornerToGoTo = rc.getID()%4; //0 is bottom left, increases clockwise
        PlacesHaveBeen.add(rc.getLocation());
        if (turnCount < 5) {
            if (cornerToGoTo == 0)
                preferredDirection = Direction.SOUTHWEST;
            else if (cornerToGoTo == 1)
                preferredDirection = Direction.NORTHWEST;
            else if (cornerToGoTo == 2)
                preferredDirection = Direction.NORTHEAST;
            else if (cornerToGoTo == 3)
                preferredDirection = Direction.SOUTHEAST;
        }


        Direction tempDir = preferredDirection;
        MapLocation[] LocationsWithCrumbs = rc.senseNearbyCrumbs(GameConstants.VISION_RADIUS_SQUARED);
        if(LocationsWithCrumbs.length!=0){
            tempDir = rc.getLocation().directionTo(LocationsWithCrumbs[0]);
        }
        boolean MovedThisTurn = false;
        outerLoop:
        for(int i = 0; i<8;i++){
            for(MapLocation L:PlacesHaveBeen){
                if(L.equals(rc.getLocation().add(tempDir)))
                    System.out.println(L);
                    continue outerLoop;
            }
            if(rc.canMove(tempDir)){
                rc.move(tempDir);
                MovedThisTurn = true;
                break;
            }
            tempDir = tempDir.rotateLeft();
        }

        if(!MovedThisTurn){//unable to move anymmore
            preferredDirection = preferredDirection.rotateLeft();
            preferredDirection = preferredDirection.rotateLeft();
            preferredDirection = preferredDirection.rotateLeft();
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
        MapLocation centerOfMap = new MapLocation(rc.getMapWidth()/2, rc.getMapHeight()/2);
        Direction directionTowardsCenter = rc.getLocation().directionTo(centerOfMap);
        if(rc.canMove(directionTowardsCenter) && turnCount < 50)
        {
            rc.move(directionTowardsCenter);
        }
        else
        {
            for(int i = 0;i<8;i++) {
                Direction dir = directions[(t + i) % 8];
                if (!LocIsSpawnLocation(rc.getLocation().add(dir)) && rc.canMove(dir)) {
                    rc.move(dir);
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
    static int[] calculateFlagDestination(RobotController rc){
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
        int[] coords = {(int)x, (int)y};
        return coords;
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

}

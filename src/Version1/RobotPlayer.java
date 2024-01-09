package Version1;

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

    static roles role;

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
        //changes explorers to soldiers at round 200
        if(rc.getRoundNum() == GameConstants.SETUP_ROUNDS && role == roles.explorer){
            role = roles.soldier;
        }
        //if can buy upgrade, buy an upgrade
        if(rc.getRoundNum() == GameConstants.GLOBAL_UPGRADE_ROUNDS || rc.getRoundNum() == GameConstants.GLOBAL_UPGRADE_ROUNDS * 2){
            if(rc.canBuyGlobal(GlobalUpgrade.ACTION)){
                rc.buyGlobal(GlobalUpgrade.ACTION);
            }
            else if(rc.canBuyGlobal(GlobalUpgrade.HEALING)){
                rc.buyGlobal(GlobalUpgrade.HEALING);
            }
        }


        while (true) {
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
                            if(!Utilities.readBitSharedArray(rc, 1021)){
                                if(rc.getRoundNum() > 200)
                                    role = roles.soldier;
                                else
                                    role = roles.explorer;
                                if(Utilities.readBitSharedArray(rc, 1023)){
                                    Utilities.editBitSharedArray(rc, 1023, true);
                                }
                                else if(Utilities.readBitSharedArray(rc, 1022)){
                                    Utilities.editBitSharedArray(rc, 1022, true);
                                }
                                else{
                                    Utilities.editBitSharedArray(rc,1021, true);
                                }
                            }
                            //become a builder, set last three bits to 0
                            else{
                                role = roles.builder;
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
                    if (rc.canPickupFlag(rc.getLocation())){
                        rc.pickupFlag(rc.getLocation());
                        rc.setIndicatorString("Holding a flag!");
                    }
                    // If we are holding an enemy flag, singularly focus on moving towards
                    // an ally spawn zone to capture it! We use the check roundNum >= SETUP_ROUNDS
                    // to make sure setup phase has ended.
                    if (rc.hasFlag() && rc.getRoundNum() >= GameConstants.SETUP_ROUNDS){
                        MapLocation[] spawnLocs = rc.getAllySpawnLocations();
                        MapLocation firstLoc = spawnLocs[0];
                        Direction dir = rc.getLocation().directionTo(firstLoc);
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


    }

    public static void runHealer(RobotController rc) throws GameActionException{

    }

    public static void runSoldier(RobotController rc) throws GameActionException{

    }

    public static void runExplorer(RobotController rc) throws GameActionException{

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
            for (Direction d : directions) {
                for (MapLocation l : SpawnLocations) {
                    if (!rc.getLocation().add(d).equals(l) && rc.canMove(d))
                        rc.move(d);
                }
            }
        }
        int t = rng.nextInt(directions.length);
        for(int i = 0;i<8;i++){
            Direction dir = directions[(t+i)%8];
            if(!LocIsSpawnLocation(rc.getLocation().add(dir)) && rc.canMove(dir)){
                rc.move(dir);
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
}

package Version15;

import battlecode.common.*;

import java.util.Arrays;

public class Utilities
{
    static final int LAST_ROUND_LOCATION_1_INDEX = 28;
    static final int CURRENT_ROUND_X_1_INDEX = 29;
    static final int CURRENT_ROUND_Y_1_INDEX = 30;
    static final int CURRENT_ROUND_TOTAL_1_INDEX = 31;
    static final int LAST_ROUND_LOCATION_2_INDEX = 32;
    static final int CURRENT_ROUND_X_2_INDEX = 33;
    static final int CURRENT_ROUND_Y_2_INDEX = 34;
    static final int CURRENT_ROUND_TOTAL_2_INDEX = 35;
    static final int LAST_ROUND_LOCATION_3_INDEX = 36;
    static final int CURRENT_ROUND_X_3_INDEX = 37;
    static final int CURRENT_ROUND_Y_3_INDEX = 38;
    static final int CURRENT_ROUND_TOTAL_3_INDEX = 39;



    /*Edits the Nth bit from the shared array and sets it to value.
    bitIndex must be between 0 and 1023.
    */
    public static void editBitSharedArray(RobotController rc, int bitIndex, boolean value) throws GameActionException
    {
        int bitValue = value ? 1 : 0;
        int arrayIndex = bitIndex / 16;
        int bitInArrayIndex = bitIndex % 16;
        int readArrayValue = rc.readSharedArray(arrayIndex);
        int mask = 1 << bitInArrayIndex;
        rc.writeSharedArray(arrayIndex, (readArrayValue & ~mask) | (bitValue << bitInArrayIndex));
    }

    /*Reads the Nth bit from the shared array.
    bitIndex must be between 0 and 1023.
    */
    public static boolean readBitSharedArray(RobotController rc, int bitIndex) throws GameActionException
    {
        int arrayIndex = bitIndex / 16;
        int bitInArrayIndex = bitIndex % 16;
        int readArrayValue = rc.readSharedArray(arrayIndex);
        return ((1 << bitInArrayIndex) & readArrayValue) != 0;
    }

    /*Edits the Nth bit from a passed array (size 64 and max value 2^16) and sets it to value.
    bitIndex must be between 0 and 1023.
    */
    public static void editBitPassedArray(int bitIndex, boolean value, int[] array)
    {
        int bitValue = value ? 1 : 0;
        int arrayIndex = bitIndex / 16;
        int bitInArrayIndex = bitIndex % 16;
        int readArrayValue = array[arrayIndex];
        int mask = 1 << bitInArrayIndex;
        array[arrayIndex] = (readArrayValue & ~mask) | (bitValue << bitInArrayIndex);
    }

    /*Reads the Nth bit from a passed array (size 64 and max value 2^16).
    bitIndex must be between 0 and 1023.
    */
    public static boolean readBitPassedArray(int bitIndex, int[] array)
    {
        int arrayIndex = bitIndex / 16;
        int bitInArrayIndex = bitIndex % 16;
        int readArrayValue = array[arrayIndex];
        return ((1 << bitInArrayIndex) & readArrayValue) != 0;
    }







    public static int openFlagIndex(RobotController rc) throws GameActionException
    {
        int index1 = rc.readSharedArray(58);
        if(index1 == 0) return 58;
        int index2 = rc.readSharedArray(59);
        if(index2 == 0) return 59;
        int index3 = rc.readSharedArray(60);
        if(index3 == 0) return 60;
        return -1;
    }

    public static StolenFlag readFlag(RobotController rc, int index) throws GameActionException
    {
        int value = rc.readSharedArray(index);
        return new StolenFlag(convertIntToLocation(value & 4095), (value & 8192) != 0);
    }


    public static void storeLocationAtBitIndexShared(MapLocation mapLocation, int bitIndex)
    {
        boolean willUseMultipleIndices = (bitIndex % 16 + 12) > 16;
        int toStore = convertLocationToInt(mapLocation);
        for(int x = bitIndex; x < bitIndex + 12; x++)
        {

        }
    }

    public static boolean getBitAtPosition(int value, int position)
    {
        return (value & ( 1 << position )) >> position != 0;
    }

    //Takes a MapLocation and converts it to an Int (12 bits)
    public static int convertLocationToInt(MapLocation mapLocation)
    {
        return (mapLocation.x << 6) | mapLocation.y;
    }

    //Takes an Int and converts it to a MapLocation
    public static MapLocation convertIntToLocation(int intLocation)
    {
        return new MapLocation((intLocation & 4032) >> 6, (intLocation & 63));
    }

    public static int openTaskIndex(RobotController rc) throws GameActionException
    {
        //builders have tasks 6-28, soldiers have tasks 28 - 51
        //eventually - codegen into list of instructions
        for(int i = 6; i < 28 ; i++)
        {
            if(rc.readSharedArray(i) == 0)
            {
                return i;
            }
        }
        return -1;
    }

    public static int activeTaskIndex(RobotController rc) throws GameActionException {
        //builders have tasks 6-28, soldiers have tasks 28 - 51
        //eventually - codegen into list of instructions
        for(int i = 6; i < 28 ; i++)
        {
            if(rc.readSharedArray(i) != 0)
            {
                return i;
            }
        }
        return -1;
    }

    public static int openEnemyIndex(RobotController rc) throws GameActionException
    {
        //builders have tasks 6-28, soldiers have tasks 28 - 51
        //eventually - codegen into list of instructions
        for(int i = 28; i < 51 ; i++)
        {
            if(rc.readSharedArray(i) == 0)
            {
                return i;
            }
        }
        return -1;
    }

    public static void reportEnemy(RobotController rc, MapLocation enemy) throws GameActionException {
        for (int x = 40; x < 51; x++)
        {
            if (rc.readSharedArray(x) == 0)
            {
                rc.writeSharedArray(x, Utilities.convertLocationToInt(enemy));
            }
        }
    }

    public static void clearObsoleteEnemies(RobotController rc) throws GameActionException {
        if (rc.canWriteSharedArray(0, 0)) {
            for (int i = 40; i < 51; i++) {
                int readLocation = rc.readSharedArray(i);
                if (readLocation != 0) {
                    MapLocation location = Utilities.convertIntToLocation(readLocation);
                    if (rc.canSenseLocation(location) && !rc.canSenseRobotAtLocation(location)) {
                        rc.writeSharedArray(i, 0);
                    }
                }
            }
        }
    }

    public static void recordEnemies(RobotController rc, RobotInfo[] enemies) throws GameActionException
    {
        for(RobotInfo enemy: enemies)
        {
            Utilities.reportEnemy(rc, enemy.getLocation());
        }
    }
    public static MapLocation newGetClosestEnemy(RobotController rc) throws GameActionException {
        MapLocation closest = null;
        for(int x = 40; x < 51; x++)
        {
            if(rc.readSharedArray(x) == 0) continue;
            if(closest == null)
            {
                closest = Utilities.convertIntToLocation(rc.readSharedArray(x));
                continue;
            }
            MapLocation tempLocation = Utilities.convertIntToLocation(rc.readSharedArray(x));
            if(rc.getLocation().distanceSquaredTo(tempLocation) < closest.distanceSquaredTo(tempLocation))
            {
                closest = new MapLocation(tempLocation.x, tempLocation.y);
            }
            return closest;
        }
        return closest;
    }

    //array must be greater than 0
    public static MapLocation averageRobotLocation(RobotInfo[] robots)
    {
        int x = 0;
        int y = 0;
        for(RobotInfo robot : robots)
        {
            x += robot.location.x;
            y += robot.location.y;
        }

        return new MapLocation(x / robots.length, y/robots.length);
    }

    //returns the lowest health nearby ally
    //returns null if empty array
    public static RobotInfo bestHeal(RobotController rc, RobotInfo[] possibleHeals) {
        if(possibleHeals.length == 0){
            return null;
        }
        int lowHealth = possibleHeals[0].health;
        int lowIndex = 0;
        if(possibleHeals[0].hasFlag()){
            return possibleHeals[0];
        }
        for(int i = 1; i < possibleHeals.length; i++){
            if(possibleHeals[i].hasFlag()){
                return possibleHeals[i];
            }
            if(possibleHeals[i].health < lowHealth){
                lowIndex = i;
                lowHealth = possibleHeals[i].getHealth();
            }
        }
        return possibleHeals[lowIndex];
    }

    public static void writeFlagToSharedArray(RobotController rc, StolenFlag flag, int flagIndex) throws GameActionException
    {
        int value = Utilities.convertLocationToInt(flag.location);
        value = value | (flag.team ? 1: 0) << 13;
        rc.writeSharedArray(flagIndex, value);
    }

    public static void writeFlagLocations(RobotController rc) throws GameActionException
    {
        RobotInfo[] robots = rc.senseNearbyRobots(-1);
        for(RobotInfo robot : robots)
        {
            if(robot.hasFlag && robot.getTeam() == rc.getTeam().opponent())
            {
                int openIndex = Utilities.openFlagIndex(rc);
                if(openIndex != -1) Utilities.writeFlagToSharedArray(rc, new StolenFlag(robot.location, !robot.getTeam().equals(rc.getTeam())) , openIndex);
            }
        }
    }

    public static void verifyFlagLocations(RobotController rc) throws GameActionException
    {
        if(rc.getRoundNum()%100 == 0){
            rc.writeSharedArray(58, 0);
            rc.writeSharedArray(59, 0);
            rc.writeSharedArray(60,0);
            return;
        }
        StolenFlag index1 = Utilities.readFlag(rc, 58);
        if(!index1.location.equals(new MapLocation(0,0)) && rc.canSenseLocation(index1.location))
        {
            if(!rc.canSenseRobotAtLocation(index1.location) || !rc.senseRobotAtLocation(index1.location).hasFlag)
            {
                rc.writeSharedArray(58, 0);
            }
        }
        StolenFlag index2 = Utilities.readFlag(rc, 59);
        if(!index2.location.equals(new MapLocation(0,0)) && rc.canSenseLocation(index2.location))
        {
            if(!rc.canSenseRobotAtLocation(index2.location) || !rc.senseRobotAtLocation(index2.location).hasFlag)
            {
                rc.writeSharedArray(59, 0);
            }
        }
        StolenFlag index3 = Utilities.readFlag(rc, 60);
        if(!index3.location.equals(new MapLocation(0,0)) && rc.canSenseLocation(index3.location))
        {
            if(!rc.canSenseRobotAtLocation(index3.location) || !rc.senseRobotAtLocation(index3.location).hasFlag)
            {
                rc.writeSharedArray(60, 0);
            }
        }
    }

    public static StolenFlag getClosestFlag(RobotController rc) throws GameActionException
    {
        StolenFlag closest = null;
        int closestDistance = Integer.MAX_VALUE;
        for(int i = 58; i <= 60; i++)
        {
            if(rc.readSharedArray(i) != 0)
            {
                StolenFlag tempFlag = Utilities.readFlag(rc, i);
                int tempDistance = rc.getLocation().distanceSquaredTo(tempFlag.location);
                if(tempDistance < closestDistance)
                {
                    closest = tempFlag;
                    closestDistance = tempDistance;
                }
            }
        }
        return closest;
    }

    public static MapLocation randomMapLocation(RobotController rc)
    {
        return new MapLocation(RobotPlayer.rng.nextInt(rc.getMapWidth()), RobotPlayer.rng.nextInt(rc.getMapHeight()));
    }

    //returns if you are between loc1 and loc2
    public static boolean isBetween(MapLocation self, MapLocation loc1, MapLocation loc2){
        Direction direction1 = self.directionTo(loc1);
        Direction direction2 = self.directionTo(loc2);
        return direction1 == direction2.opposite() || direction1 == direction2.opposite().rotateLeft() || direction1 == direction2.opposite().rotateRight();
    }

    public static void updateEnemyCluster(RobotController rc, MapLocation location) throws GameActionException
    {
        int numClusters = 0;
        for (int i = CURRENT_ROUND_TOTAL_1_INDEX; i <= CURRENT_ROUND_TOTAL_3_INDEX; i += 4) {
            if (rc.readSharedArray(i) != 0) {
                numClusters++;
            }
        }
        MapLocation[] currAvgClusters = getCurrentEnemyClusters(rc);
        int closestClusterIndex = -1;
        int shortestDistance = Integer.MAX_VALUE;
        for(int i = 0; i < currAvgClusters.length; i++)
        {
            MapLocation tempLocation = currAvgClusters[i];
            int tempDistance = location.distanceSquaredTo(tempLocation);
            if(tempDistance < shortestDistance)
            {
                shortestDistance = tempDistance;
                closestClusterIndex = i;
            }
        }
        if(closestClusterIndex == -1) return;

        if(shortestDistance > RobotPlayer.MAX_MAP_DIST_SQUARED / 9 && numClusters < 3)
        {
            closestClusterIndex = numClusters;
        }

        int xIndex = CURRENT_ROUND_X_1_INDEX + 4 * closestClusterIndex;
        int yIndex = CURRENT_ROUND_Y_1_INDEX + 4 * closestClusterIndex;
        int numEnemiesIndex = CURRENT_ROUND_TOTAL_1_INDEX + 4 * closestClusterIndex;
        int xTotal = rc.readSharedArray(xIndex);
        int yTotal = rc.readSharedArray(yIndex);
        int numEnemies = rc.readSharedArray(numEnemiesIndex);
        rc.writeSharedArray(xIndex, xTotal + location.x);
        rc.writeSharedArray(yIndex, yTotal + location.y);
        rc.writeSharedArray(numEnemiesIndex, numEnemies + 1);
    }

    public static MapLocation[] getCurrentEnemyClusters(RobotController rc) throws GameActionException
    {
        MapLocation[] locs = new MapLocation[3];
        locs[0] = generateAverageLocation(
                rc.readSharedArray(CURRENT_ROUND_X_1_INDEX),
                rc.readSharedArray(CURRENT_ROUND_Y_1_INDEX),
                rc.readSharedArray(CURRENT_ROUND_TOTAL_1_INDEX));
        locs[1] = generateAverageLocation(
                rc.readSharedArray(CURRENT_ROUND_X_2_INDEX),
                rc.readSharedArray(CURRENT_ROUND_Y_2_INDEX),
                rc.readSharedArray(CURRENT_ROUND_TOTAL_2_INDEX));
        locs[2] = generateAverageLocation(
                rc.readSharedArray(CURRENT_ROUND_X_3_INDEX),
                rc.readSharedArray(CURRENT_ROUND_Y_3_INDEX),
                rc.readSharedArray(CURRENT_ROUND_TOTAL_3_INDEX));
        return locs;
    }

    public static MapLocation[] getLastRoundClusters(RobotController rc) throws GameActionException
    {
        MapLocation[] locs = new MapLocation[3];
        locs[0] = Utilities.convertIntToLocation(rc.readSharedArray(LAST_ROUND_LOCATION_1_INDEX));
        locs[1] = Utilities.convertIntToLocation(rc.readSharedArray(LAST_ROUND_LOCATION_2_INDEX));
        locs[2] = Utilities.convertIntToLocation(rc.readSharedArray(LAST_ROUND_LOCATION_3_INDEX));
        return locs;
    }

    public static MapLocation generateAverageLocation(int totalX, int totalY, int total)
    {
        if(total == 0)
        {
            return new MapLocation(-1, -1);
        }
        else
        {
            return new MapLocation(totalX / total, totalY / total);
        }
    }

    public static void resetAvgEnemyLoc(RobotController rc) throws GameActionException {
        MapLocation[] clusters = getCurrentEnemyClusters(rc);
        rc.writeSharedArray(LAST_ROUND_LOCATION_1_INDEX,
                clusters[0].x != -1 ? convertLocationToInt(clusters[0]) : 0);
        rc.writeSharedArray(CURRENT_ROUND_X_1_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_Y_1_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_TOTAL_1_INDEX, 0);

        rc.writeSharedArray(LAST_ROUND_LOCATION_2_INDEX,
                clusters[1].x != -1 ? convertLocationToInt(clusters[1]) : 0);
        rc.writeSharedArray(CURRENT_ROUND_X_2_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_Y_2_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_TOTAL_2_INDEX, 0);

        rc.writeSharedArray(LAST_ROUND_LOCATION_3_INDEX,
                clusters[2].x != -1 ? convertLocationToInt(clusters[2]) : 0);
        rc.writeSharedArray(CURRENT_ROUND_X_3_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_Y_3_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_TOTAL_3_INDEX, 0);
    }

    public static MapLocation getClosestCluster(RobotController rc) throws GameActionException
    {
        MapLocation closestCluster = null;
        int lowestDistance = Integer.MAX_VALUE;
        for(int i = 0; i < 3; i++)
        {
            MapLocation tempLocation = Utilities.convertIntToLocation(rc.readSharedArray(LAST_ROUND_LOCATION_1_INDEX + 4 * i));
            int tempDistance = rc.getLocation().distanceSquaredTo(tempLocation);
            if(!tempLocation.equals(new MapLocation(0,0)) &&  tempDistance < lowestDistance)
            {
                closestCluster = tempLocation;
                lowestDistance = tempDistance;
            }
        }
        return closestCluster;
    }

    public static void addKillToKillsArray(RobotController rc,int[] killsArray) throws GameActionException {
        for(int i = 0;i<killsArray.length;i++){
            if(killsArray[i]==0){
                killsArray[i] = rc.getRoundNum();
                rc.writeSharedArray(8,rc.readSharedArray(8)+1);

                return;
            }
        }
    }

    public static void checkForRevivedRobots(RobotController rc, int[] killsArray) throws GameActionException {
        for(int i = 0;i<killsArray.length;i++){
            if(killsArray[i] != 0 && rc.getRoundNum()-killsArray[i]>=25){
                killsArray[i]=0;
                rc.writeSharedArray(8,rc.readSharedArray(8)-1);
            }
        }

    }
}

package Version17;

import Version17.RobotPlayer;
import Version17.Cluster;
import battlecode.common.*;

import java.util.ArrayList;

public class Utilities
{
    public static final int LAST_ROUND_ALLY_LOCATION_1 = 9;
    public static final int LAST_ROUND_ALLY_HEALTH_1 = 10;
    public static final int CURRENT_ROUND_ALLY_X_1 = 11;
    public static final int CURRENT_ROUND_ALLY_Y_1 = 12;
    public static final int CURRENT_ROUND_HEALTH_1 = 13;
    public static final int CURRENT_ROUND_ALLY_TOTAL_1 = 14;
    public static final int LAST_ROUND_ALLY_LOCATION_2 = 15;
    public static final int LAST_ROUND_ALLY_HEALTH_2 = 16;
    public static final int CURRENT_ROUND_ALLY_X_2 = 17;
    public static final int CURRENT_ROUND_ALLY_Y_2 = 18;
    public static final int CURRENT_ROUND_HEALTH_2 = 19;
    public static final int CURRENT_ROUND_ALLY_TOTAL_2 = 20;
    public static final int LAST_ROUND_ALLY_LOCATION_3 = 21;
    public static final int LAST_ROUND_ALLY_HEALTH_3 = 22;
    public static final int CURRENT_ROUND_ALLY_X_3 = 23;
    public static final int CURRENT_ROUND_ALLY_Y_3 = 24;
    public static final int CURRENT_ROUND_HEALTH_3 = 25;
    public static final int CURRENT_ROUND_ALLY_TOTAL_3 = 26;
    public static final int LAST_ROUND_LOCATION_1_INDEX = 28;
    public static final int CURRENT_ROUND_X_1_INDEX = 29;
    public static final int CURRENT_ROUND_Y_1_INDEX = 30;
    public static final int CURRENT_ROUND_TOTAL_1_INDEX = 31;
    public static final int LAST_ROUND_LOCATION_2_INDEX = 32;
    public static final int CURRENT_ROUND_X_2_INDEX = 33;
    public static final int CURRENT_ROUND_Y_2_INDEX = 34;
    public static final int CURRENT_ROUND_TOTAL_2_INDEX = 35;
    public static final int LAST_ROUND_LOCATION_3_INDEX = 36;
    public static final int CURRENT_ROUND_X_3_INDEX = 37;
    public static final int CURRENT_ROUND_Y_3_INDEX = 38;
    public static final int CURRENT_ROUND_TOTAL_3_INDEX = 39;

    public static final MapLocation NULL_MAP_LOCATION = new MapLocation(0,0);



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
            if(tempLocation.equals(new MapLocation(-1, -1))) continue;
            int tempDistance = location.distanceSquaredTo(tempLocation);
            if(tempDistance < shortestDistance)
            {
                shortestDistance = tempDistance;
                closestClusterIndex = i;
            }
        }

        if(closestClusterIndex == -1) closestClusterIndex = numClusters;

        if(shortestDistance > RobotPlayer.MAX_MAP_DIST_SQUARED / 20)
        {
            closestClusterIndex = numClusters;
        }

        int xIndex = CURRENT_ROUND_X_1_INDEX + 4 * closestClusterIndex;
        int yIndex = CURRENT_ROUND_Y_1_INDEX + 4 * closestClusterIndex;
        int numEnemiesIndex = CURRENT_ROUND_TOTAL_1_INDEX + 4 * closestClusterIndex;
        int xTotal = rc.readSharedArray(xIndex);
        int yTotal = rc.readSharedArray(yIndex);
        int numEnemies = rc.readSharedArray(numEnemiesIndex);
        if(RobotPlayer.turnOrder == 0)
            System.out.println(xIndex);
        rc.writeSharedArray(xIndex, xTotal + location.x);
        rc.writeSharedArray(yIndex, yTotal + location.y);
        rc.writeSharedArray(numEnemiesIndex, numEnemies + 1);
    }

    public static void updateAllyCluster(RobotController rc, MapLocation location) throws GameActionException
    {
        int numClusters = 0;
        for (int i = CURRENT_ROUND_ALLY_TOTAL_1; i <= CURRENT_ROUND_ALLY_TOTAL_3; i += 6) {
            if (rc.readSharedArray(i) != 0) {
                numClusters++;
            }
        }

        MapLocation[] currAvgClusters = getCurrentAllyClusters(rc);
        int closestClusterIndex = -1;
        int shortestDistance = Integer.MAX_VALUE;
        for(int i = 0; i < currAvgClusters.length; i++)
        {
            MapLocation tempLocation = currAvgClusters[i];
            if(tempLocation.equals(new MapLocation(-1, -1))) continue;
            int tempDistance = location.distanceSquaredTo(tempLocation);
            if(tempDistance < shortestDistance)
            {
                shortestDistance = tempDistance;
                closestClusterIndex = i;
            }
        }

        if(closestClusterIndex == -1) closestClusterIndex = 0;
        if(shortestDistance > RobotPlayer.MAX_MAP_DIST_SQUARED / 56 && numClusters < 3)
        {
            closestClusterIndex = numClusters;
        }

        int xIndex = CURRENT_ROUND_ALLY_X_1 + 6 * closestClusterIndex;
        int yIndex = CURRENT_ROUND_ALLY_Y_1 + 6 * closestClusterIndex;
        int healthIndex = CURRENT_ROUND_HEALTH_1 + 6 * closestClusterIndex;
        int numAlliesIndex = CURRENT_ROUND_ALLY_TOTAL_1 + 6 * closestClusterIndex;
        int xTotal = rc.readSharedArray(xIndex);
        int yTotal = rc.readSharedArray(yIndex);
        int healthTotal = rc.readSharedArray(healthIndex);
        int numAllies = rc.readSharedArray(numAlliesIndex);
        rc.writeSharedArray(xIndex, xTotal + location.x);
        rc.writeSharedArray(yIndex, yTotal + location.y);
        rc.writeSharedArray(healthIndex, healthTotal + rc.getHealth());
        rc.writeSharedArray(numAlliesIndex, numAllies + 1);
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

    public static MapLocation[] getCurrentAllyClusters(RobotController rc) throws GameActionException
    {
        MapLocation[] locs = new MapLocation[3];
        locs[0] = generateAverageLocation(
                rc.readSharedArray(CURRENT_ROUND_ALLY_X_1),
                rc.readSharedArray(CURRENT_ROUND_ALLY_Y_1),
                rc.readSharedArray(CURRENT_ROUND_ALLY_TOTAL_1));
        locs[1] = generateAverageLocation(
                rc.readSharedArray(CURRENT_ROUND_ALLY_X_2),
                rc.readSharedArray(CURRENT_ROUND_ALLY_Y_2),
                rc.readSharedArray(CURRENT_ROUND_ALLY_TOTAL_2));
        locs[2] = generateAverageLocation(
                rc.readSharedArray(CURRENT_ROUND_ALLY_X_3),
                rc.readSharedArray(CURRENT_ROUND_ALLY_Y_3),
                rc.readSharedArray(CURRENT_ROUND_ALLY_TOTAL_3));
        return locs;
    }

    public static int[] getCurrentAllyHealth(RobotController rc) throws GameActionException
    {
        int[] avgHealth = new int[3];
        avgHealth[0] = rc.readSharedArray(CURRENT_ROUND_HEALTH_1);
        avgHealth[1] = rc.readSharedArray(CURRENT_ROUND_HEALTH_2);
        avgHealth[2] = rc.readSharedArray(CURRENT_ROUND_HEALTH_3);
        return avgHealth;
    }

    public static Cluster[] getLastRoundClusters(RobotController rc) throws GameActionException
    {
        Cluster[] clusters = new Cluster[3];
        clusters[0] = new Cluster(Utilities.convertIntToLocation(rc.readSharedArray(LAST_ROUND_LOCATION_1_INDEX)),
                getEnemyCountFromCluster(rc.readSharedArray(LAST_ROUND_LOCATION_1_INDEX)));
        clusters[1] = new Cluster(Utilities.convertIntToLocation(rc.readSharedArray(LAST_ROUND_LOCATION_2_INDEX)),
                getEnemyCountFromCluster(rc.readSharedArray(LAST_ROUND_LOCATION_2_INDEX)));
        clusters[2] = new Cluster(Utilities.convertIntToLocation(rc.readSharedArray(LAST_ROUND_LOCATION_3_INDEX)),
                getEnemyCountFromCluster(rc.readSharedArray(LAST_ROUND_LOCATION_3_INDEX)));
        return clusters;
    }

    public static int[] getLastRoundAllyHealth(RobotController rc) throws GameActionException
    {
        int[] clusters = new int[3];
        clusters[0] = rc.readSharedArray(LAST_ROUND_ALLY_HEALTH_1);
        clusters[1] = rc.readSharedArray(LAST_ROUND_ALLY_HEALTH_2);
        clusters[2] = rc.readSharedArray(LAST_ROUND_ALLY_HEALTH_3);
        return clusters;
    }




    public static MapLocation[] getLastRoundAllyClusters(RobotController rc) throws GameActionException
    {
        MapLocation[] clusters = new MapLocation[3];
        clusters[0] = convertIntToLocation(rc.readSharedArray(LAST_ROUND_ALLY_LOCATION_1));
        clusters[1] = convertIntToLocation(rc.readSharedArray(LAST_ROUND_ALLY_LOCATION_2));
        clusters[2] = convertIntToLocation(rc.readSharedArray(LAST_ROUND_ALLY_LOCATION_3));
        return clusters;
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

    public static int createClusterInt(MapLocation location, int numEnemies)
    {
        int numToStore = Math.min(300, numEnemies) / 20;
        numToStore = numToStore << 12;
        return convertLocationToInt(location) | numToStore;
    }

    public static int getEnemyCountFromCluster(int cluster)
    {
        return (61440 & cluster) >> 12;
    }

    public static void resetAvgEnemyLoc(RobotController rc) throws GameActionException {
        MapLocation[] clusters = getCurrentEnemyClusters(rc);

        rc.writeSharedArray(LAST_ROUND_LOCATION_1_INDEX,
                clusters[0].x != -1 ? convertLocationToInt(clusters[0]) : 0);
        rc.writeSharedArray(CURRENT_ROUND_X_1_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_Y_1_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_TOTAL_1_INDEX, 0);

        rc.writeSharedArray(LAST_ROUND_LOCATION_2_INDEX,
                clusters[1].x != -1 ? convertLocationToInt(clusters[0]) : 0);
        rc.writeSharedArray(CURRENT_ROUND_X_2_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_Y_2_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_TOTAL_2_INDEX, 0);

        rc.writeSharedArray(LAST_ROUND_LOCATION_3_INDEX,
                clusters[2].x != -1 ? convertLocationToInt(clusters[0]): 0);
        rc.writeSharedArray(CURRENT_ROUND_X_3_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_Y_3_INDEX, 0);
        rc.writeSharedArray(CURRENT_ROUND_TOTAL_3_INDEX, 0);
    }

    public static void resetAvgAllyLoc(RobotController rc) throws GameActionException {
        MapLocation[] clusters = getCurrentAllyClusters(rc);
        int[] averageHealth = getCurrentAllyHealth(rc);

        int total1 = rc.readSharedArray(CURRENT_ROUND_ALLY_TOTAL_1);
        rc.writeSharedArray(LAST_ROUND_ALLY_LOCATION_1,
                clusters[0].x != -1 ? convertLocationToInt(clusters[0]) : 0);
        rc.writeSharedArray(LAST_ROUND_ALLY_HEALTH_1,
                total1 > 0 ? averageHealth[0] / total1 : 0);
        rc.writeSharedArray(CURRENT_ROUND_ALLY_X_1, 0);
        rc.writeSharedArray(CURRENT_ROUND_ALLY_Y_1, 0);
        rc.writeSharedArray(CURRENT_ROUND_HEALTH_1, 0);
        rc.writeSharedArray(CURRENT_ROUND_ALLY_TOTAL_1, 0);

        int total2 = rc.readSharedArray(CURRENT_ROUND_ALLY_TOTAL_2);
        rc.writeSharedArray(LAST_ROUND_ALLY_LOCATION_2,
                clusters[1].x != -1 ? convertLocationToInt(clusters[1]) : 0);
        rc.writeSharedArray(LAST_ROUND_ALLY_HEALTH_2,
                total2 > 0 ? averageHealth[1] / total2 : 0);
        rc.writeSharedArray(CURRENT_ROUND_ALLY_X_2, 0);
        rc.writeSharedArray(CURRENT_ROUND_ALLY_Y_2, 0);
        rc.writeSharedArray(CURRENT_ROUND_HEALTH_2, 0);
        rc.writeSharedArray(CURRENT_ROUND_ALLY_TOTAL_2, 0);

        int total3 = rc.readSharedArray(CURRENT_ROUND_ALLY_TOTAL_3);
        rc.writeSharedArray(LAST_ROUND_ALLY_LOCATION_3,
                clusters[2].x != -1 ? convertLocationToInt(clusters[2]) : 0);
        rc.writeSharedArray(LAST_ROUND_ALLY_HEALTH_3,
                total3 > 0 ? averageHealth[2] / total3 : 0);
        rc.writeSharedArray(CURRENT_ROUND_ALLY_X_3, 0);
        rc.writeSharedArray(CURRENT_ROUND_ALLY_Y_3, 0);
        rc.writeSharedArray(CURRENT_ROUND_HEALTH_3, 0);
        rc.writeSharedArray(CURRENT_ROUND_ALLY_TOTAL_3, 0);
    }

    public static Cluster getClosestCluster(RobotController rc) throws GameActionException
    {
        Cluster[] clusters = Utilities.getLastRoundClusters(rc);
        MapLocation location = rc.getLocation();
        int closestDist = Integer.MAX_VALUE;
        int closestIndex = 0;
        for(int i = 0; i < clusters.length; i++)
        {
            int tempDistance = location.distanceSquaredTo(clusters[i].location);
            if(!clusters[i].location.equals(NULL_MAP_LOCATION) && tempDistance < closestDist)
            {
                closestDist = tempDistance;
                closestIndex = i;
            }
        }
        return clusters[closestIndex];
    }
    public static Cluster getClosestCluster(RobotController rc, MapLocation origin) throws GameActionException
    {
        Cluster[] clusters = Utilities.getLastRoundClusters(rc);
        int closestDist = Integer.MAX_VALUE;
        int closestIndex = 0;
        for(int i = 0; i < clusters.length; i++)
        {
            int tempDistance = origin.distanceSquaredTo(clusters[i].location);
            if(!clusters[i].location.equals(NULL_MAP_LOCATION) && tempDistance < closestDist)
            {
                closestDist = tempDistance;
                closestIndex = i;
            }
        }
        return clusters[closestIndex];
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

    public static boolean angleIsGreaterThan90(Direction a, Direction b){
        while(a!=Direction.SOUTH){
            a = a.rotateLeft();
            b = b.rotateLeft();
        }
        return b==Direction.NORTH || b==Direction.NORTHEAST || b==Direction.NORTHWEST;

    }
    public static boolean locationIsBehindWall(RobotController rc, MapLocation L){
        return locationIsBehindWall(rc,L,rc.getLocation());
    }
    public static boolean locationIsBehindWall(RobotController rc, MapLocation L, MapLocation R){
        double m  = (R.y-L.y+0.0)/(R.x-L.x);
        double c = (m*L.x-L.y);
        for(MapInfo T: rc.senseNearbyMapInfos()){
            MapLocation t = T.getMapLocation();
            if(!angleIsGreaterThan90(t.directionTo(L),t.directionTo(R))){
                /*If the directions point in the same way that means that t
                is not inbetween L and R. If it was inbetween the the
                angle would be obtuse
                 */
                continue;
            }
            double d1 = Math.pow(t.y-m*t.x+c,2);
            /*
            this comparison is kinda weird but basically it uses the distance from a point
            to a line formula* and compares it to 1. However since division and sqrt are expensive,
            I've done some algebra to get rid of them

            * given aX + bY + C = 0 and (P,Q)
            distance = (aP + bQ + c)/sqrt(A^2+B^2)
             */

            if(T.isWall()&&1+m*m>=d1){
                return true;
            }
        }
        return false;
    }
    public static boolean locationIsBehindWall(RobotController rc, MapLocation L, MapLocation R, int radius) throws GameActionException {
        double m  = (R.y-L.y+0.0)/(R.x-L.x);
        double c = (m*L.x-L.y);
        for(MapInfo T: rc.senseNearbyMapInfos(radius)){
            MapLocation t = T.getMapLocation();
            if(!angleIsGreaterThan90(t.directionTo(L),t.directionTo(R))){
                /*If the directions point in the same way that means that t
                is not inbetween L and R. If it was inbetween the the
                angle would be obtuse
                 */
                continue;
            }
            double d1 = Math.pow(t.y-m*t.x+c,2);
            /*
            this comparison is kinda weird but basically it uses the distance from a point
            to a line formula* and compares it to 1. However since division and sqrt are expensive,
            I've done some algebra to get rid of them

            * given aX + bY + C = 0 and (P,Q)
            distance = (aP + bQ + c)/sqrt(A^2+B^2)
             */

            if(T.isWall()&&1+m*m>=d1){
                return true;
            }
        }
        return false;
    }

    //returns false if we dont know any flag locations, true otherwise
    public static boolean knowFlag(RobotController rc) throws GameActionException {
        int index3 = rc.readSharedArray(3);
        int index4 = rc.readSharedArray(4);
        int index5 = rc.readSharedArray(5);
        return index3 != 0 || index4 != 0 || index5 != 0;
    }

    public static MapLocation[] getKnownFlags(RobotController rc) throws GameActionException
    {
        ArrayList<MapLocation> knownFlags = new ArrayList<>();
        int index3 = rc.readSharedArray(3);
        if(index3 != 0) knownFlags.add(convertIntToLocation(index3));
        int index4 = rc.readSharedArray(4);
        if(index4 != 0) knownFlags.add(convertIntToLocation(index4));
        int index5 = rc.readSharedArray(5);
        if(index5 != 0) knownFlags.add(convertIntToLocation(index5));
        return knownFlags.toArray(new MapLocation[0]);
    }

    public static StolenFlag getClosestFlag(RobotController rc, MapLocation loc) throws GameActionException
    {
        StolenFlag closest = null;
        int closestDistance = Integer.MAX_VALUE;
        for(int i = 58; i <= 60; i++)
        {
            if(rc.readSharedArray(i) != 0)
            {
                StolenFlag tempFlag = Utilities.readFlag(rc, i);
                int tempDistance = loc.distanceSquaredTo(tempFlag.location);
                if(tempDistance < closestDistance)
                {
                    closest = tempFlag;
                    closestDistance = tempDistance;
                }
            }
        }
        return closest;
    }
    public static boolean isDefaultLocation(RobotController rc, MapLocation location) throws GameActionException {
        return location.equals(convertIntToLocation(rc.readSharedArray(40))) || location.equals(convertIntToLocation(rc.readSharedArray(41))) || location.equals(convertIntToLocation(rc.readSharedArray(42)));
    }
}

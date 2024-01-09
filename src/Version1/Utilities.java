package Version1;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Utilities {

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

    public static void storeLocationAtBitIndexShared(MapLocation mapLocation)
    {
        int x = mapLocation.x;
        int y = mapLocation.y;


    }

}

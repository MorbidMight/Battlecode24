package Version2;

import battlecode.common.*;

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

    public static void setTaskSharedArray(RobotController rc, Task task, int arrayIndex) throws GameActionException
    {
        int value = convertLocationToInt(task.location);
        value = (value | ((task.used) ? 1: 0) << 13);
        value = (value | ((task.duckType) ? 1: 0) << 14);
        value = (value | ((task.isComing) ? 1: 0) << 15);
        rc.writeSharedArray(arrayIndex, value);
    }

    /*public static void addTask(RobotController rc, Task task) throws GameActionException
    {

        rc.writeSharedArray(arrayIndex, (((convertLocationToInt(task.location) | ((task.used) ? 1: 0) << 13) | ((task.duckType) ? 1: 0) << 14) | ((task.isComing) ? 1: 0) << 15));
    }*/

    public static Task readTaskSharedArray(RobotController rc, int arrayIndex) throws GameActionException
    {
        int value = rc.readSharedArray(arrayIndex);
        Task task = new Task();
        task.location = convertIntToLocation(value & 4095);
        task.used = (value & 8192) != 0;
        task.duckType = (value & 16384) != 0;
        task.isComing = (value & 32768) != 0;
        return task;
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

    public static int openTaskIndex(RobotController rc, boolean duckType) throws GameActionException {
        //builders have tasks 6-28, soldiers have tasks 28 - 51
        int index = duckType ? 6 : 28;
        int finalIndex = duckType ? 28 : 51;
        //eventually - codegen into list of instructions
        for(int i = index; i < finalIndex ; i++)
        {
            if(!Utilities.readBitSharedArray(rc, index * 16 + 12))
            {
                return i;
            }
        }
        return -1;
    }




}

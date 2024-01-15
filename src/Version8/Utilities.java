package Version8;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

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
        value = (value | ((task.builderDispatched) ? 1: 0) << 13);
        for(int i = 6; i < 28; i++){
            if(value == rc.readSharedArray(i)){
                return;
            }
        }
        rc.writeSharedArray(arrayIndex, value);
    }


    public static Task readTask(RobotController rc) throws GameActionException
    {
        int index = activeTaskIndex(rc);
        if(index == -1)
        {
            return null;
        }

        return readTaskSharedArray(rc, index);
    }

    public static void clearTask(RobotController rc, int taskIndex) throws GameActionException {
        rc.writeSharedArray(taskIndex, 0);
    }

    public static Task readTaskSharedArray(RobotController rc, int arrayIndex) throws GameActionException
    {
        int value = rc.readSharedArray(arrayIndex);
        return new Task(convertIntToLocation(value & 4095), (value & 8192) != 0, arrayIndex);
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
        for (int x = 28; x < 51; x++)
        {
            if (rc.readSharedArray(x) == 0)
            {
                rc.writeSharedArray(x, Utilities.convertLocationToInt(enemy));
            }
        }
    }

    public static void clearObsoleteEnemies(RobotController rc) throws GameActionException {
        if (rc.canWriteSharedArray(0, 0)) {
            for (int i = 28; i < 51; i++) {
                int readLocation = rc.readSharedArray(i);
                if (readLocation != 0) {
                    MapLocation location = Utilities.convertIntToLocation(readLocation);
                    if (!rc.canSenseRobotAtLocation(location)) {
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
        for(int x = 28; x < 51; x++)
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

    public static void writeFlagToSharedArray(RobotController rc, Flag flag, int flagIndex) throws GameActionException
    {
        int value = Utilities.convertLocationToInt(flag.location);
        value = value | (flag.isStolen ? 1: 0) << 13;
        rc.writeSharedArray(flagIndex, value);
    }
    public static MapLocation randomMapLocation(RobotController rc)
    {
        return new MapLocation(RobotPlayer.rng.nextInt(rc.getMapWidth()), RobotPlayer.rng.nextInt(rc.getMapHeight()));
    }
}

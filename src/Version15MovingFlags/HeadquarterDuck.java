package Version15MovingFlags;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class HeadquarterDuck
{
    static int lockCount = 0;
    public static void runHeadquarterDuck(RobotController rc) throws GameActionException
    {
        Utilities.resetAvgEnemyLoc(rc);
        rc.writeSharedArray(6, rc.readSharedArray(7));
        rc.writeSharedArray(7, 0);
        spawningStuff(rc);

    }
    public static void spawningStuff(RobotController rc) throws GameActionException {
        if(Utilities.readBitSharedArray(rc, 1021)){
            lockCount++;
        }
        if(lockCount > 25){
            Utilities.editBitSharedArray(rc, 1021, false);
            lockCount = 0;
        }
        if(Utilities.getClosestFlag(rc, new MapLocation(10, 10)) != null){
            int x = RobotPlayer.findClosestSpawnLocationToStolenFlag(rc, Utilities.getClosestFlag(rc, new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2)));
            if (x != -1){
                Utilities.editBitSharedArray(rc, 1021, true);
                //00
                if(x == 0){
                    Utilities.editBitSharedArray(rc, 1023, false);
                    Utilities.editBitSharedArray(rc, 1022, false);
                }
                //01
                else if(x == 1){
                    Utilities.editBitSharedArray(rc, 1022, false);
                    Utilities.editBitSharedArray(rc, 1023, true); }
                //x == 2, desire 10
                else if (x == 2){
                    Utilities.editBitSharedArray(rc, 1022, true);
                    Utilities.editBitSharedArray(rc, 1023, false);
                }
            }
        }
        else if(!Utilities.readBitSharedArray(rc, 1021)){
            int x;
            if(Soldier.knowFlag(rc))
                x = RobotPlayer.findClosestSpawnLocationToCoordinatedTarget(rc);
            else if (Utilities.getClosestCluster(rc, new MapLocation(10, 10)) != null) {
                x = RobotPlayer.findClosestSpawnLocationToCluster(rc);
            } else {
                x = RobotPlayer.findClosestSpawnLocationToCoordinatedBroadcast(rc);
            }
            if (x != -1){
                //00
                if(x == 0){
                    Utilities.editBitSharedArray(rc, 1023, false);
                    Utilities.editBitSharedArray(rc, 1022, false);
                }
                //01
                else if(x == 1){
                    Utilities.editBitSharedArray(rc, 1022, false);
                    Utilities.editBitSharedArray(rc, 1023, true); }
                //x == 2, desire 10
                else if (x == 2){
                    Utilities.editBitSharedArray(rc, 1022, true);
                    Utilities.editBitSharedArray(rc, 1023, false);
                }
            }
        }
    }
}

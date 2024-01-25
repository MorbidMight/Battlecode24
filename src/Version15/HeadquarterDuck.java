package Version15;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class HeadquarterDuck
{
    public static void runHeadquarterDuck(RobotController rc) throws GameActionException
    {
        Utilities.resetAvgEnemyLoc(rc);
        rc.writeSharedArray(6, rc.readSharedArray(7));
        rc.writeSharedArray(7, 0);
        rc.writeSharedArray(53, 0);
        rc.writeSharedArray(54, 0);
    }
}

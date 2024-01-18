package Version9;

import battlecode.common.*;

import java.util.PriorityQueue;

import static Version9.RobotPlayer.*;
public class Carrier {
    public static void runCarrier(RobotController rc) throws GameActionException {
        int index = Utilities.openFlagIndex(rc);
        if(index != -1) Utilities.writeFlagToSharedArray(rc, new StolenFlag(rc.getLocation(), false), index);
        MapLocation closestSpawnLoc = findClosestSpawnLocation(rc);
        Direction d = rc.getLocation().directionTo(closestSpawnLoc);
        if (rc.canFill(rc.getLocation().add(d))) {
            rc.fill(rc.getLocation().add(d));
        }
        Pathfinding.bugNav2(rc, findClosestSpawnLocation(rc));
    }

}

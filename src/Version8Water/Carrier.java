package Version8Water;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import static Version8Water.RobotPlayer.findClosestSpawnLocation;
public class Carrier {
    public static void runCarrier(RobotController rc) throws GameActionException {
        rc.writeSharedArray(58, Utilities.convertLocationToInt(rc.getLocation()));
        if(rc.getHealth()<100 || !rc.hasFlag()) {//Probobly gonna die soon
            rc.writeSharedArray(58,0);
            rc.dropFlag(rc.getLocation());
        }
        MapLocation closestSpawnLoc = findClosestSpawnLocation(rc);
        Direction d = rc.getLocation().directionTo(closestSpawnLoc);
        if (rc.canFill(rc.getLocation().add(d))) {
            rc.fill(rc.getLocation().add(d));
        }
        Pathfinding.tryToMove(rc, findClosestSpawnLocation(rc));
    }

}

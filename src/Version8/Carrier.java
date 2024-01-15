package Version8;

import battlecode.common.*;

import java.util.PriorityQueue;

import static Version8.RobotPlayer.*;
public class Carrier {
    public static void runCarrier(RobotController rc) throws GameActionException {
        rc.writeSharedArray(58, Utilities.convertLocationToInt(rc.getLocation()));
        if(rc.getHealth()<100 || !rc.hasFlag()) {//Probobly gonna die soon
            rc.writeSharedArray(58,0);
            rc.dropFlag(rc.getLocation());
        }
        Pathfinding.tryToMove(rc, findClosestSpawnLocation(rc));
    }

}

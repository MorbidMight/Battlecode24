package Version17;

import Version15MovingFlags.BFSKernel;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import static Version17.RobotPlayer.findClosestSpawnLocation;
public class Carrier {
    public static void runCarrier(RobotController rc) throws GameActionException {
        MapLocation closestSpawnLoc = findClosestSpawnLocation(rc);
        Direction d = rc.getLocation().directionTo(closestSpawnLoc);
//        if(rc.canMove(d) /*&& rc.senseMapInfo(rc.getLocation().add(d)).getSpawnZoneTeamObject().equals(rc.getTeam())*/){
//            rc.move(d);
//        }
        if (rc.canFill(rc.getLocation().add(d))) {
            rc.fill(rc.getLocation().add(d));
        }
        ///BFSKernel9x9.BFS(rc,closestSpawnLoc);
        if(rc.getLocation().distanceSquaredTo(closestSpawnLoc) <= 16) BFSKernel9x9.BFS(rc, closestSpawnLoc);
        else Pathfinding.combinedPathfinding(rc, findClosestSpawnLocation(rc));
    }

}

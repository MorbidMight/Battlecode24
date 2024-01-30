package Version18;

import Version15MovingFlags.BFSKernel;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import static Version18.RobotPlayer.findClosestSpawnLocation;
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
        if(rc.senseNearbyRobots(-1,rc.getTeam().opponent()).length>0){
            Soldier.retreat(rc);
        }
        Pathfinding.combinedPathfinding(rc,closestSpawnLoc);
        //if(rc.getLocation().distanceSquaredTo(closestSpawnLoc) <= 25) BFSKernel.BFS(rc, closestSpawnLoc);
        //else Pathfinding.combinedPathfinding(rc, findClosestSpawnLocation(rc));
    }

}

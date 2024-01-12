package Version2;

import battlecode.common.*;

import java.nio.file.Path;


import static Version2.RobotPlayer.*;

public class Builder {
    public static void runBuilder(RobotController rc) throws GameActionException {
        if(!rc.isSpawned())
            Clock.yield();


        Task t = Utilities.readTask(rc);
        if (SittingOnFlag) {
            System.out.println("A");
            UpdateExplosionBorder(rc);
        } else if (t != null) {//there is a task to do
            Pathfinding.tryToMove(rc, t.location);
            if (locationIsActionable(rc, t.location)) {
                if (rc.canBuild(TrapType.EXPLOSIVE, t.location)) {
                    rc.build(TrapType.EXPLOSIVE, t.location);
                }
            }

        } else {//there is no task to be done


            //There is no task to be done and all the flags have guys sitting on them
            //Move away from the nearest guys avoiding ops especicially
            Direction d = directionToMove(rc);
            for(int i = 0;i<8;i++){
                if(rc.canMove(d)){
                    rc.move(d);
                }
                d= d.rotateLeft();
            }

            if(rc.getRoundNum()%8==0/*&&distanceFromNearestSpawnLocation(rc)>16*/)
            UpdateExplosionBorder(rc);

        }
    }


    private static Direction directionToMove(RobotController rc) {
        return directions[rng.nextInt(8)];
    }

    private static int distanceFromNearestSpawnLocation(RobotController rc){
return 0;
    }

    public static boolean locationIsActionable(RobotController rc, MapLocation m) throws GameActionException {
        MapInfo[] ActionableTiles = rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED);
        for (MapInfo curr : ActionableTiles) {
            if (m.equals(curr.getMapLocation())) {
                return true;
            }

        }
        return false;
    }

    public static void UpdateExplosionBorder(RobotController rc) throws GameActionException {
        for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
            if (rc.canBuild(TrapType.EXPLOSIVE, t.getMapLocation())&&t.getTrapType().equals(TrapType.NONE)) {
                rc.build(TrapType.EXPLOSIVE, t.getMapLocation());
                System.out.println("Building a bomb");
            }
        }
    }

}

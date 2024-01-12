package Version2;

import battlecode.common.*;

import java.nio.file.Path;

//todo, make sure that builders don't dig themselves in

import static Version2.RobotPlayer.*;

public class Builder {
    public static void runBuilder(RobotController rc) throws GameActionException {



        Task t = Utilities.readTask(rc);
        if(SittingOnFlag){
           UpdateExplosionBorder(rc);
        }
        else if (t != null) {//there is a task to do
            Pathfinding.tryToMove(rc,t.location);
            if (locationIsActionable(rc,t.location)){
                if(rc.canBuild(TrapType.EXPLOSIVE,t.location)){
                    rc.build(TrapType.EXPLOSIVE,t.location);
                }
            }

        } else{//there is no task to be done
            MapLocation flag = new MapLocation(0,0);
                  if(!Utilities.readBitSharedArray(rc,BitIndices.soldierSittingOnFlag1)){
                      flag = Utilities.convertIntToLocation(rc.readSharedArray(0)%(int)Math.pow(2,12));
                      Pathfinding.tryToMove(rc,flag);
                      if(rc.getLocation().equals(flag)){
                          Utilities.editBitSharedArray(rc,BitIndices.soldierSittingOnFlag1,true);
                          SittingOnFlag = true;
                      }
            }else if(!Utilities.readBitSharedArray(rc,BitIndices.soldierSittingOnFlag2)){
                      flag = Utilities.convertIntToLocation(rc.readSharedArray(1)%(int)Math.pow(2,12));
                      Pathfinding.tryToMove(rc,flag);
                      if(rc.getLocation().equals(flag)){
                          Utilities.editBitSharedArray(rc,BitIndices.soldierSittingOnFlag2,true);
                          SittingOnFlag = true;
                      }
            }else if(!Utilities.readBitSharedArray(rc,BitIndices.soldierSittingOnFlag3)){
                      flag = Utilities.convertIntToLocation(rc.readSharedArray(2)%(int)Math.pow(2,12));
                      Pathfinding.tryToMove(rc,flag);
                      if(rc.getLocation().equals(flag)){
                          Utilities.editBitSharedArray(rc,BitIndices.soldierSittingOnFlag3,true);
                          SittingOnFlag = true;
                      }
                      //There is no task to be done and all the flags have guys sitting on them
                      RobotInfo[] nearbyRobots = rc.senseNearbyRobots();

            }


        }



    }

    public static MapLocation findNearestFlag(MapLocation[] flags, MapLocation tileToTest) {
        int[] distances = {200, 200, 200};
        for (int i = 0; i < flags.length; i++) {
            distances[i] = tileToTest.distanceSquaredTo(flags[i]);
        }
        int index = 0;
        if (distances[1] < distances[0])
            index = 1;
        if (distances[2] < distances[index])
            index = 2;

        return flags[index];

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
        for(MapInfo t:rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)){
            if(rc.canBuild(TrapType.EXPLOSIVE,t.getMapLocation())){
                rc.canBuild(TrapType.EXPLOSIVE,t.getMapLocation());
            }
        }
    }

}

package Version2;

import battlecode.common.*;

import static Version2.RobotPlayer.*;

public class Builder
{
    public static void runBuilder(RobotController rc) throws GameActionException {
        //Go to flag from array
        if(DistanceFromNearestFlag(rc.getLocation(),rc)>6){
            //URAV PATHFINDING

        }
        else if(true)/*conditional to make them stop and do something else)*/  {//Dig water around the flag
            FlagInfo[] BreadLocation = rc.senseNearbyFlags(GameConstants.VISION_RADIUS_SQUARED);
            if (BreadLocation.length != 0) {
                MapInfo[] actionableTiles = rc.senseNearbyMapInfos(2);

                for (MapInfo i : actionableTiles) {
                    if (DistanceFromNearestFlag(i.getMapLocation(), rc) <= MoatRadius && rc.canDig(i.getMapLocation())) {
                        rc.dig(i.getMapLocation());
                    } else if (DistanceFromNearestFlag(i.getMapLocation(), rc) == MoatRadius + 1 && rc.canBuild(TrapType.WATER, i.getMapLocation())) {
                        rc.build(TrapType.WATER, i.getMapLocation());
                    }
                }
            }else { //builder can't do any moves on it's actionble tiles so it needs to move
                //Urav Pathfinding towards dead center of map (Away from the corner)
            }
        }else{
            //Find Average value of all nearby friendlys and walk away from them
            RobotInfo[] NearbyFriendlys = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED,rc.getTeam());
            int x = 0;
            int y = 0;
            Direction d = directions[rng.nextInt(8)];

            if(NearbyFriendlys.length!=0) {
                for (RobotInfo i : NearbyFriendlys) {
                    x += i.getLocation().x;
                    y += i.getLocation().y;
                }
                x /= NearbyFriendlys.length;
                y /= NearbyFriendlys.length;
                MapLocation AvgOfNearbyFriends = new MapLocation((int) x, (int) y);
                d = AvgOfNearbyFriends.directionTo(rc.getLocation()).rotateRight();
            }
            for(int i = 0;i<8;i++){
                if(rc.canMove(d)) {
                    rc.move(d);
                    break;
                }else{
                    d = d.rotateLeft();
                }
            }

            if(rc.getRoundNum() % BombFrequency==0)
            {
                MapLocation[] ActionableTiles = rc.getAllLocationsWithinRadiusSquared(rc.getLocation(),GameConstants.INTERACT_RADIUS_SQUARED);
                for(MapLocation m: ActionableTiles){
                    if(rc.canBuild(TrapType.EXPLOSIVE,m))
                        rc.build(TrapType.EXPLOSIVE,m);//Don't break because they can place two bombs/turn

                }
            }
        }
    }
}

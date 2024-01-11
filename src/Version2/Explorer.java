package Version2;

import battlecode.common.*;

import static Version2.RobotPlayer.*;

public class Explorer
{
    public static void runExplorer(RobotController rc) throws GameActionException {
        preferredDirection = Direction.NORTH;
        int cornerToGoTo = rc.getID()%4; //0 is bottom left, increases clockwise
        PlacesHaveBeen.add(rc.getLocation());
        if (turnCount < 5) {
            if (cornerToGoTo == 0)
                preferredDirection = Direction.SOUTHWEST;
            else if (cornerToGoTo == 1)
                preferredDirection = Direction.NORTHWEST;
            else if (cornerToGoTo == 2)
                preferredDirection = Direction.NORTHEAST;
            else
                preferredDirection = Direction.SOUTHEAST;
        }


        Direction tempDir = preferredDirection;
        MapLocation[] LocationsWithCrumbs = rc.senseNearbyCrumbs(GameConstants.VISION_RADIUS_SQUARED);
        if(LocationsWithCrumbs.length!=0){
            tempDir = rc.getLocation().directionTo(LocationsWithCrumbs[0]);
        }
        boolean MovedThisTurn = false;
        outerLoop:
        for(int i = 0; i<8;i++){
            for(MapLocation L:PlacesHaveBeen){
                if(L.equals(rc.getLocation().add(tempDir)))
                    System.out.println(L);
                continue outerLoop;
            }
            if(rc.canMove(tempDir)){
                rc.move(tempDir);
                MovedThisTurn = true;
                break;
            }
            tempDir = tempDir.rotateLeft();
        }

        if(!MovedThisTurn){//unable to move anymmore
            preferredDirection = preferredDirection.rotateLeft();
            preferredDirection = preferredDirection.rotateLeft();
            preferredDirection = preferredDirection.rotateLeft();
        }
    }
}

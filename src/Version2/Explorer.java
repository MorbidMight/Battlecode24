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

        //tries to get any nearby crumbs
        MapLocation[] nearbyCrumbs = rc.senseNearbyCrumbs(-1);
        if(nearbyCrumbs.length != 0) {
            MapLocation targetCrumb = chooseTargetCrumb(rc, nearbyCrumbs);
            MapInfo targetLoc = rc.senseMapInfo(targetCrumb);
            //check if crumb is on water
            if(!targetLoc.isPassable() && rc.canFill(targetCrumb)){
                rc.fill(targetCrumb);
            }
            Pathfinding.tryToMove(rc, targetCrumb);
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

    public static MapLocation chooseTargetCrumb(RobotController rc, MapLocation[] nearbyCrumbs) throws GameActionException {
        int highestCrumbVal = rc.senseMapInfo(nearbyCrumbs[0]).getCrumbs();
        int highestIndex = 0;
        for(int i = 1; i < nearbyCrumbs.length; i++){
            if(rc.senseMapInfo(nearbyCrumbs[i]).getCrumbs() > highestCrumbVal){
                highestIndex = i;
                highestCrumbVal = rc.senseMapInfo(nearbyCrumbs[i]).getCrumbs();
            }
        }
        return nearbyCrumbs[highestIndex];
    }
}

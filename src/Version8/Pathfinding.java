package Version8;

import battlecode.common.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Pathfinding
{

    //for bugNav2
    private static MapLocation previousDestination = null;
    private static HashSet<MapLocation> lineLocations = null;
    private static int obstacleStartDistance = 0;
    private static int bugState = 0;
    private static int turnDirection = -1;
    private static Direction bugDirection;


    public static Direction AStar(RobotController rc, MapLocation destination)
    {
        HashMap<MapLocation, AStarNode> nodes = new HashMap<>();
        PriorityQueue<AStarNode> unvisitedNodes = new PriorityQueue<>();
        HashSet<MapLocation> visitedNodes = new HashSet<MapLocation>();

        /*ArrayList<MapInfo> mapInfos = new ArrayList<>(RobotPlayer.seenLocations.values());
        for (MapInfo mapInfo : mapInfos) {
            MapLocation location = mapInfo.getMapLocation();
            nodes.put(location, new AStarNode(rc.getLocation(), destination, null, Integer.MAX_VALUE));
        }*/

        AStarNode currentNode = new AStarNode(rc.getLocation(), destination, null, 0);
        nodes.put(currentNode.location, currentNode);
        unvisitedNodes.add(currentNode);
        int numVisitedNodes = 0;
        do
        {
            int bytecodesStart = Clock.getBytecodeNum();

            currentNode = unvisitedNodes.remove();
            visitedNodes.add(currentNode.location);
            numVisitedNodes++;

            int numNull = 0;
            numNull += HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, new MapLocation(currentNode.location.x, currentNode.location.y + 1));
            numNull += HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, new MapLocation(currentNode.location.x, currentNode.location.y - 1));
            numNull += HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, new MapLocation(currentNode.location.x + 1, currentNode.location.y));
            numNull += HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, new MapLocation(currentNode.location.x - 1, currentNode.location.y));
            numNull += HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, new MapLocation(currentNode.location.x + 1, currentNode.location.y + 1));
            numNull += HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, new MapLocation(currentNode.location.x - 1, currentNode.location.y + 1));
            numNull += HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, new MapLocation(currentNode.location.x + 1, currentNode.location.y - 1));
            numNull += HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, new MapLocation(currentNode.location.x - 1, currentNode.location.y - 1));

            System.out.println("num null " + numNull + " " +(Clock.getBytecodeNum() - bytecodesStart));

        }while(!unvisitedNodes.isEmpty() && !currentNode.location.equals(destination));

        AStarNode finalNode = currentNode.previousNode;
        while (!finalNode.previousNode.location.equals(rc.getLocation()))
        {
            finalNode = finalNode.previousNode;
        }

        //System.out.println("Num Visited Nodes: " + numVisitedNodes + " Bytecodes Taken: ");
        return rc.getLocation().directionTo(finalNode.location);
    }

    private static int HandleDirection(RobotController rc, MapLocation destination, HashMap<MapLocation, AStarNode> nodes, PriorityQueue<AStarNode> unvisitedNodes, HashSet<MapLocation> visitedNodes, AStarNode currentNode, MapLocation location) {
        int numNull = 0;
        if (InBounds(rc, location) && !visitedNodes.contains(location))
        {
            AStarNode node = nodes.get(location);
            if(node == null)
            {
                AStarNode tempNode = new AStarNode(location, destination, currentNode, currentNode.GCost + 1);
                nodes.put(location, tempNode);
                unvisitedNodes.add(tempNode);
                numNull++;
            }else
            {
                int tempDistance = node.GCost + 1;
                if (tempDistance < node.GCost)
                {
                    node.GCost = tempDistance;
                    unvisitedNodes.add(node);
                    node.previousNode = currentNode;
                }
            }
        }
        return numNull;
    }


    public static Direction basicPathfinding(RobotController rc, MapLocation targetLocation, boolean newRoute) throws GameActionException {
        if (newRoute) {
            resetBasicPathfinding();
        }
        if(RobotPlayer.alreadyBeen.size() >= 15)
            resetBasicPathfinding();
        //get current location of the robot
        MapLocation currentLocation = rc.getLocation();
        //assign a score to all possible move locations (null means not passable)
        Float[] possibleMoveDirections = new Float[8];
        //clouds should be given a higher score (worse)
        int numNull = 0;
        int lowestIndex = -1;
        float lowestScore = Float.MAX_VALUE;
        for (int x = 0; x < RobotPlayer.directions.length; x++) {
            boolean skipDir = false;
            if (!rc.canMove(RobotPlayer.directions[x]))
                skipDir = true;
            if (skipDir) continue;
            MapLocation tempLocation = rc.adjacentLocation(RobotPlayer.directions[x]);
            MapInfo locationInfo = rc.senseMapInfo(tempLocation);
            // does the location have these features?

            if (RobotPlayer.alreadyBeen.contains(tempLocation)) {
                continue;
            }
            else if(!rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(58))) && rc.readSharedArray(58) != 0 && tempLocation.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(58))) < 4)
            {
                continue;
            }
            else {
                //distance squared to the target location
                possibleMoveDirections[x] = tempLocation.distanceSquaredTo(targetLocation) * 1.0f;
                if (possibleMoveDirections[x] < lowestScore) {
                    lowestScore = possibleMoveDirections[x];
                    lowestIndex = x;
                }
            }
        }
        if (lowestIndex != -1) {
            if (rc.adjacentLocation(RobotPlayer.directions[lowestIndex]).equals(targetLocation)) {
                RobotPlayer.alreadyBeen.clear();
            }
            RobotPlayer.alreadyBeen.add(rc.adjacentLocation(RobotPlayer.directions[lowestIndex]));
            return RobotPlayer.directions[lowestIndex];
        } else
            return null;
    }

    public static void tryToMove(RobotController rc, MapLocation l) throws GameActionException {//rl is false for right and true for left
        try {
            Direction dir = basicPathfinding(rc, l, false);

            if (dir != null && rc.canMove(dir)) {
                rc.move(dir);
                RobotPlayer.alreadyBeen.add(rc.getLocation());
            }
        } catch (Exception ignored) {
        }
    }

    public static void resetBasicPathfinding() {
        RobotPlayer.alreadyBeen.clear();
    }

    public static void bugNav2(RobotController rc, MapLocation destination) throws GameActionException {
        if(destination == null)
        {
            return;
        }
        if(!destination.equals(previousDestination))
        {
            previousDestination = destination;
            lineLocations = createLine(rc.getLocation(), destination);
        }

        /*for(MapLocation ml : lineLocations)
        {
            rc.setIndicatorDot(ml, 255,0,0);
        }*/

        if(RobotPlayer.turnCount % 10 == 0)
        {
            bugState = 0;
        }
        if(bugState == 0)
        {
            bugDirection = rc.getLocation().directionTo(destination);
            if(rc.canMove(bugDirection))
            {
                rc.move(bugDirection);
            }
            else
            {
                bugState = 1;
                obstacleStartDistance = rc.getLocation().distanceSquaredTo(destination);
                bugDirection = rc.getLocation().directionTo(destination);
            }
        }
        else
        {
            if(lineLocations.contains(rc.getLocation()) && rc.getLocation().distanceSquaredTo(destination) < obstacleStartDistance)
            {
                bugState = 0;
                turnDirection = -1;
            }

            if(turnDirection == -1)
            {
                Direction tempBugDirectionRight = bugDirection;
                for(int i = 0; i < 8; i++)
                {
                    if(rc.canMove(tempBugDirectionRight))
                    {
                        //rc.move(bugDirection);
                        tempBugDirectionRight = tempBugDirectionRight.rotateRight();
                        break;
                    }
                    else
                    {
                        tempBugDirectionRight = bugDirection.rotateLeft();
                    }
                }
                Direction tempBugDirectionLeft = bugDirection;
                for(int i = 0; i < 8; i++)
                {
                    if(rc.canMove(tempBugDirectionLeft))
                    {
                        //rc.move(bugDirection);
                        tempBugDirectionLeft = tempBugDirectionLeft.rotateLeft();
                        break;
                    }
                    else
                    {
                        tempBugDirectionLeft = tempBugDirectionLeft.rotateLeft();
                    }
                }
                if(rc.getLocation().add(tempBugDirectionRight).distanceSquaredTo(destination)
                        < rc.getLocation().add(tempBugDirectionLeft).distanceSquaredTo(destination))
                {
                    turnDirection = 0;
                }else
                {
                    turnDirection = 1;
                }
            }

            if(turnDirection == 0)
            {
                for (int i = 0; i < 8; i++)
                {
                    if (rc.canMove(bugDirection))
                    {
                        rc.move(bugDirection);
                        bugDirection = bugDirection.rotateRight();
                        break;
                    }
                    else
                    {
                        bugDirection = bugDirection.rotateLeft();
                    }
                }
            }
            else
            {
                for (int i = 0; i < 8; i++)
                {
                    if (rc.canMove(bugDirection))
                    {
                        rc.move(bugDirection);
                        bugDirection = bugDirection.rotateLeft();
                        break;
                    }
                    else
                    {
                        bugDirection = bugDirection.rotateRight();
                    }
                }
            }
        }
    }

    public static HashSet<MapLocation> createLine(MapLocation a, MapLocation b)
    {
        HashSet<MapLocation> locations = new HashSet<>();
        int x = a.x, y = a.y;
        int dx = Math.abs(b.x - a.x);
        int dy = Math.abs(b.y - a.y);
        int sx = (int) Math.signum(dx);
        int sy = (int) Math.signum(dy);
        int d = Math.max(dx, dy);
        int r = d/2;
        if(dx > dy)
        {
           for(int i = 0; i < d; i++)
           {
               locations.add(new MapLocation(x,y));
               x += sx;
               r += dy;
               if(r >= dx)
               {
                   locations.add(new MapLocation(x,y));
                   y += sy;
                   r -= dx;
               }
           }
        }else
        {
            for(int i = 0; i < d; i++)
            {
                locations.add(new MapLocation(x,y));
                y += sy;
                r += dx;
                if(r >= dy)
                {
                    locations.add(new MapLocation(x,y));
                    x += sx;
                    r -= dy;
                }
            }
        }
        locations.add(new MapLocation(x,y));
        return  locations;
    }

    public static void combinedPathfinding(RobotController rc, MapLocation destination) throws GameActionException
    {
        if(destination == null) return;
        if(rc.canSenseLocation(destination))
        {
            tryToMove(rc, destination);
        }
        else
        {
            bugNav2(rc, destination);
        }
    }

    public static boolean InBounds(RobotController rc, MapLocation location)
    {
        MapInfo checkedLocation = RobotPlayer.seenLocations.get(location);
        return location.x < rc.getMapWidth() && location.y < rc.getMapHeight() && location.x >= 0 && location.y >= 0
                && (checkedLocation == null || checkedLocation.isPassable());
    }
}

class AStarNode implements Comparable
{
    public MapLocation location;
    public AStarNode previousNode;
    public int GCost;
    public int HCost;

    public AStarNode(MapLocation location, MapLocation destination, AStarNode previousNode, int GCost)
    {
        this.location = location;
        this.previousNode = previousNode;
        this.GCost = GCost;
        this.HCost = location.distanceSquaredTo(destination);
    }

    @Override
    public int compareTo(Object o) {
        AStarNode other = (AStarNode) o;
        return Integer.compare(this.GCost + this.HCost, other.GCost + other.HCost);
    }

    @Override
    public String toString() {
        return "AStarNode{" +
                "location=" + location +
                ", previousNode=" + previousNode.location +
                ", GCost=" + GCost +
                ", HCost=" + HCost +
                '}';
    }
}

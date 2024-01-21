package Version11;

import battlecode.common.*;

import java.util.*;

import static Version11.RobotPlayer.*;

public class Pathfinding
{

    enum PathfindingState{bugNav2, bellmanFord}
    static PathfindingState pathfindingState = PathfindingState.bellmanFord;
    public static final int BUG_NAV_TURNS = 50;
    public static int turnsUsingBugNav = 0;
    public static final int MAX_BYTECODE_USAGE = 10000;
    //for bugNav2
    private static MapLocation previousDestination = null;
    private static HashSet<MapLocation> lineLocations = null;
    private static int obstacleStartDistance = 0;
    private static int bugState = 0;
    private static int turnDirection = -1;
    private static Direction bugDirection;

    public static final int[][] adjacencyMatrix5x5 = new int[][]{
            {0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,1,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,1,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,0,0,0,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,1,1,0,0,0,1,0,0,0,0,1,1,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,1,1,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,0,0,0,0,1,1},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,1,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,1},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,0}};

    public static final int[][] neighborLookup5x5 = new int[][]{
            {1,5,6},
            {0,2,5,6,7},
            {1,3,6,7,8},
            {2,4,7,8,9},
            {3,8,9},
            {0,1,6,10,11},
            {0,1,2,5,7,10,11,12},
            {1,2,3,6,8,11,12,13},
            {2,3,4,7,9,12,13,14},
            {3,4,8,13,14},
            {5,6,11,15,16},
            {5,6,7,10,12,15,16,17},
            {7,8,13,18,17,16,11,6},
            {7,8,9,12,14,17,18,19},
            {8,9,13,18,19},
            {10,11,16,20,21},
            {10,11,12,15,17,20,21,22},
            {11,12,13,16,18,21,22,23},
            {12,13,14,17,19,22,23,24},
            {13,14,18,23,24},
            {15,16,21},
            {15,16,17,20,22},
            {16,17,18,21,23},
            {17,18,19,22,24},
            {18,19,23}};
    public static final int[] BELLMAN_FORD_NODE_RELAX_ORDER_5X5 = {7,6,11,16,17,18,13,8,3,2,1,0,5,10,15,20,21,22,23,24,19,14,9,4,3,2,1,0};

    public static void combinedPathfinding(RobotController rc, MapLocation destination) throws GameActionException {
        switch (pathfindingState)
        {
            case bellmanFord:
                bellmanFord5x5(rc, destination);
                updateAlreadyBeen(rc);
                if(alreadyBeen.get(rc.getLocation()) > 2)
                {
                    alreadyBeen.clear();
                    pathfindingState = PathfindingState.bugNav2;
                }
                break;
            case bugNav2:
                bugNav2(rc, destination);
                turnsUsingBugNav++;
                if(turnsUsingBugNav >= BUG_NAV_TURNS)
                {
                    turnsUsingBugNav = 0;
                    pathfindingState = PathfindingState.bellmanFord;
                }
                break;
        }
        rc.setIndicatorString(pathfindingState.toString());
    }

    public static void bellmanFord5x5(RobotController rc, MapLocation destination) throws GameActionException
    {
        HashSet<Integer> unreachableNodes = new HashSet<>();
        MapLocation center = rc.getLocation();
        int[][] adjacencyMatrix = adjacencyMatrix5x5.clone();
        int[] distanceMatrix = new int[25];
        int count = 0;
        for (int i = center.y + 2; i >= center.y - 2; i--) {
            for (int j = center.x - 2; j <= center.x + 2; j++) {
                MapLocation temp = new MapLocation(j,i);
                if(InBounds(rc, temp) && rc.sensePassability(temp) && !rc.canSenseRobotAtLocation(temp))
                {
                    //distanceMatrix[count / 5][count % 5] = destination.distanceSquaredTo(new MapLocation(j, i)) * 100;
                    distanceMatrix[count] = destination.distanceSquaredTo(new MapLocation(j, i)) * 100;
                }else
                {
                    unreachableNodes.add(count);
                    adjacencyMatrix[count] = new int[25];
                }
                count++;
            }
        }

        if(rc.getID() == 12723)
        {
        }
        int numIterations = 2;
        for(int i = 0; i < numIterations ; i++)
        {
            for (int nodeIndex : BELLMAN_FORD_NODE_RELAX_ORDER_5X5)
            {
                if (unreachableNodes.contains(nodeIndex)) continue;
                int min = distanceMatrix[nodeIndex];
                for (int k = 0; k < neighborLookup5x5[nodeIndex].length; k++)
                {
                    int neighborIndex = neighborLookup5x5[nodeIndex][k];
                    if (adjacencyMatrix[neighborIndex][nodeIndex] == 1)
                    {
                        if (distanceMatrix[neighborIndex] + 1 < min)
                        {
                            min = distanceMatrix[neighborIndex] + 1;
                        }
                    }
                }
                distanceMatrix[nodeIndex] = min;
            }
        }

        if(rc.getID() == 12723)
        {
        }

        int minDistance = Integer.MAX_VALUE;
        int minIndex = 0;
        for(int i = 0; i < 8; i++)
        {
            int nodeIndex = neighborLookup5x5[12][i];
            if(adjacencyMatrix[nodeIndex][12] == 1)
            {
                if(distanceMatrix[nodeIndex] < minDistance)
                {
                    minDistance = distanceMatrix[nodeIndex];
                    minIndex = i;
                }
            }
        }
        if(rc.canMove(RobotPlayer.directions[minIndex]))
        {
            rc.move(RobotPlayer.directions[minIndex]);
        }

    }

    public static void bellmanFordFlag(RobotController rc, MapLocation destination, StolenFlag flag) throws GameActionException
    {
        HashSet<Integer> unreachableNodes = new HashSet<>();
        MapLocation center = rc.getLocation();
        int[][] adjacencyMatrix = adjacencyMatrix5x5.clone();
        int[] distanceMatrix = new int[25];
        int count = 0;
        for (int i = center.y + 2; i >= center.y - 2; i--) {
            for (int j = center.x - 2; j <= center.x + 2; j++) {
                MapLocation temp = new MapLocation(j,i);
                if(InBounds(rc, temp) && rc.sensePassability(temp) && temp.distanceSquaredTo(flag.location) > 9)
                {
                    //distanceMatrix[count / 5][count % 5] = destination.distanceSquaredTo(new MapLocation(j, i)) * 100;
                    distanceMatrix[count] = destination.distanceSquaredTo(new MapLocation(j, i)) * 100;
                }else
                {
                    unreachableNodes.add(count);
                    adjacencyMatrix[count] = new int[25];
                }
                count++;
            }
        }


        int numIterations = 2;
        for(int i = 0; i < numIterations ; i++)
        {
            for (int nodeIndex : BELLMAN_FORD_NODE_RELAX_ORDER_5X5)
            {
                if (unreachableNodes.contains(nodeIndex)) continue;
                int min = distanceMatrix[nodeIndex];
                for (int k = 0; k < neighborLookup5x5[nodeIndex].length; k++)
                {
                    int neighborIndex = neighborLookup5x5[nodeIndex][k];
                    if (adjacencyMatrix[neighborIndex][nodeIndex] == 1)
                    {
                        if (distanceMatrix[neighborIndex] + 1 < min)
                        {
                            min = distanceMatrix[neighborIndex] + 1;
                        }
                    }
                }
                distanceMatrix[nodeIndex] = min;
            }
        }

        int minDistance = Integer.MAX_VALUE;
        int minIndex = 0;
        for(int i = 0; i < 8; i++)
        {
            int nodeIndex = neighborLookup5x5[12][i];
            if(adjacencyMatrix[nodeIndex][12] == 1)
            {
                if(distanceMatrix[nodeIndex] < minDistance)
                {
                    minDistance = distanceMatrix[nodeIndex];
                    minIndex = i;
                }
            }
        }
        if(rc.canMove(RobotPlayer.directions[minIndex]))
        {
            rc.move(RobotPlayer.directions[minIndex]);
        }

    }

    public static String printDistanceMatrix(MapLocation center, int[] distanceMatrix) throws GameActionException {
        String tempStr = "";
        int count = 0;
        for (int i = center.y + 2; i >= center.y - 2; i--) {
            for (int j = center.x - 2; j <= center.x + 2; j++) {
                tempStr += distanceMatrix[count] + " ";
                count++;
            }
            tempStr += "\n";
        }
        return tempStr;
    }

    public static String printAdjacencyMatrix(int[][] matrix)
    {
        String tempStr = "";
        for(int i = 0; i < matrix.length; i++)
        {
            tempStr += "\n";
            for(int j = 0; j < matrix[i].length; j++)
            {
                tempStr += matrix[i][j] + " ";
            }
        }
        return tempStr;
    }

    public static MapLocation nodeIndexToLocation5x5(int nodeIndex, MapLocation center)
    {
        MapLocation corner = new MapLocation(center.x - 2, center.y - 2);
        return new MapLocation(corner.x + nodeIndex % 5, corner.y + 4 - nodeIndex / 5);
    }

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


        }while(!unvisitedNodes.isEmpty() && !currentNode.location.equals(destination));

        AStarNode finalNode = currentNode.previousNode;
        while (!finalNode.previousNode.location.equals(rc.getLocation()))
        {
            finalNode = finalNode.previousNode;
        }

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
        if(RobotPlayer.turnCount % 15 == 0)
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

            if (alreadyBeen.containsKey(tempLocation)) {
                continue;
            }
            //FIX SECOND CONDITION!!
            else if( !rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(58))) && rc.readSharedArray(58) != 0 && tempLocation.distanceSquaredTo(Utilities.convertIntToLocation(rc.readSharedArray(58))) < 9)
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
                alreadyBeen.clear();
            }
            return RobotPlayer.directions[lowestIndex];
        } else
            return null;
    }

    public static void tryToMove(RobotController rc, MapLocation l) throws GameActionException {//rl is false for right and true for left
        try {
            Direction dir = basicPathfinding(rc, l, false);

            if (dir != null && rc.canMove(dir)) {
                rc.move(dir);
            }
        } catch (Exception ignored) {
        }
    }
    public static void tryToMoveTowardsFlag(RobotController rc, MapLocation l, StolenFlag flag) throws GameActionException {//rl is false for right and true for left
        try {
            Direction dir = moveTowardsFlag(rc, l, flag);

            if (dir != null && rc.canMove(dir)) {
                rc.move(dir);
            }
        } catch (Exception ignored) {
        }
    }

    public static Direction moveTowardsFlag(RobotController rc, MapLocation targetLocation, StolenFlag flag) throws GameActionException
    {
        if (RobotPlayer.turnCount % 15 == 0)
            resetBasicPathfinding();
        //get current location of the robot
        MapLocation currentLocation = rc.getLocation();
        //assign a score to all possible move locations (null means not passable)
        Float[] possibleMoveDirections = new Float[8];
        //clouds should be given a higher score (worse)
        int numNull = 0;
        int lowestIndex = -1;
        float lowestScore = Float.MAX_VALUE;
        for (int x = 0; x < RobotPlayer.directions.length; x++)
        {
            boolean skipDir = false;
            if (!rc.canMove(RobotPlayer.directions[x]))
                skipDir = true;
            if (skipDir) continue;
            MapLocation tempLocation = rc.adjacentLocation(RobotPlayer.directions[x]);
            MapInfo locationInfo = rc.senseMapInfo(tempLocation);
            // does the location have these features?

            if (alreadyBeen.containsKey(tempLocation))
            {
                continue;
            }

            else if (!rc.getLocation().equals(flag.location) && (!flag.team) && tempLocation.distanceSquaredTo(flag.location) < 9)
            {
                continue;
            }
            else
            {
                //distance squared to the target location
                possibleMoveDirections[x] = tempLocation.distanceSquaredTo(targetLocation) * 1.0f;
                if (possibleMoveDirections[x] < lowestScore)
                {
                    lowestScore = possibleMoveDirections[x];
                    lowestIndex = x;
                }
            }
        }
        if (lowestIndex != -1) {
            if (rc.adjacentLocation(RobotPlayer.directions[lowestIndex]).equals(targetLocation)) {
                alreadyBeen.clear();
            }
            return RobotPlayer.directions[lowestIndex];
        } else
            return null;
    }

    public static void resetBasicPathfinding() {
        alreadyBeen.clear();
    }

    public static void bugNav0(RobotController rc, MapLocation destination) throws GameActionException {
        Direction bugDir = rc.getLocation().directionTo(destination);
        if(rc.canMove(bugDir))
        {
            rc.move(bugDir);
        }
        else
        {
            for(int i = 0; i < 8; i++)
            {
                if(rc.canMove(bugDir))
                {
                    rc.move(bugDir);
                }
                else
                {
                    bugDir = bugDir.rotateLeft();
                }
            }
        }
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

    public static boolean InBounds(RobotController rc, MapLocation location)
    {
        //MapInfo checkedLocation = RobotPlayer.seenLocations.get(location);
        return location.x < rc.getMapWidth() && location.y < rc.getMapHeight() && location.x >= 0 && location.y >= 0;
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

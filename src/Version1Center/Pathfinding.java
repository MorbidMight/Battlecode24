package Version1Center;

import battlecode.common.Direction;
import battlecode.common.MapInfo;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Pathfinding
{
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

            currentNode = unvisitedNodes.remove();
            visitedNodes.add(currentNode.location);
            numVisitedNodes++;

            MapLocation up = new MapLocation(currentNode.location.x, currentNode.location.y + 1);
            MapLocation down = new MapLocation(currentNode.location.x, currentNode.location.y - 1);
            MapLocation right = new MapLocation(currentNode.location.x + 1, currentNode.location.y);
            MapLocation left = new MapLocation(currentNode.location.x - 1, currentNode.location.y);
            MapLocation topRight = new MapLocation(currentNode.location.x + 1, currentNode.location.y + 1);
            MapLocation topLeft = new MapLocation(currentNode.location.x - 1, currentNode.location.y + 1);
            MapLocation bottomRight = new MapLocation(currentNode.location.x + 1, currentNode.location.y - 1);
            MapLocation bottomLeft = new MapLocation(currentNode.location.x - 1, currentNode.location.y - 1);

            HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, up);
            HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, down);
            HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, right);
            HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, left);
            HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, topRight);
            HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, topLeft);
            HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, bottomRight);
            HandleDirection(rc, destination, nodes, unvisitedNodes, visitedNodes, currentNode, bottomLeft);

        }while(!unvisitedNodes.isEmpty() && !currentNode.location.equals(destination));

        AStarNode finalNode = currentNode.previousNode;
        while (!finalNode.previousNode.location.equals(rc.getLocation()))
        {
            finalNode = finalNode.previousNode;
        }


        return rc.getLocation().directionTo(finalNode.location);
    }

    private static void HandleDirection(RobotController rc, MapLocation destination, HashMap<MapLocation, AStarNode> nodes, PriorityQueue<AStarNode> unvisitedNodes, HashSet<MapLocation> visitedNodes, AStarNode currentNode, MapLocation location) {
        if (InBounds(rc, location) && !visitedNodes.contains(location))
        {
            AStarNode node = nodes.get(location);
            if(node == null)
            {
                AStarNode tempNode = new AStarNode(location, destination, currentNode, currentNode.GCost + 1);
                nodes.put(location, tempNode);
                unvisitedNodes.add(tempNode);
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

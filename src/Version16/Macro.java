package Version16;

import battlecode.common.*;

import java.util.ArrayList;

public class Macro
{
    static int numJailedEnemies;
    static int numJailedAllies;
    static int turnsOnFront;
    static Cluster[] enemyClusters;
    static FlagInfo[] nearbyFlags;
    public static void doMacro(RobotController rc) throws GameActionException
    {
        initializeMacroVariables(rc);

        if(nearbyFlags.length != 0)
        {
            Pathfinding.combinedPathfinding(rc, nearbyFlags[0].getLocation());
        }
        //maybe assign a cost to each possible action, what are the possible actions?
        //moving towards a flag, moving towards a cluster, rotate off a front,

        /*
            first decision is whether to move towards a cluster or a flag
         */
        if(numJailedEnemies + 10 > numJailedAllies)
        {
            MapLocation flagLocationToPursue = getBestFlagLocation(rc);
            Pathfinding.combinedPathfinding(rc, flagLocationToPursue);

        }else
        {
            //move towards cluster, most overwhelmed one
            Cluster mostOverwhelmed = getMostOverwhelmedCluster(rc, enemyClusters);
            Pathfinding.combinedPathfinding(rc, mostOverwhelmed.location);
        }
    }

    public static MapLocation getBestFlagLocation(RobotController rc) throws GameActionException
    {
        MapLocation[] broadcastFlags = rc.senseBroadcastFlagLocations();
        ArrayList<MapLocation> locations = new ArrayList<>();
        for(int i = 3; i < 6; i++){
            if(rc.readSharedArray(i) != 0){
                locations.add(Utilities.convertIntToLocation(rc.readSharedArray(i)));
            }
        }
        if(broadcastFlags.length == 0)
        {
            return Utilities.getClosestCluster(rc).location;
        }
        else if(!locations.isEmpty())
        {
            double bestScore = -1;
            int bestIndex = 0;
            for(int i = 0; i < locations.size(); i++)
            {
                double flagScore = 0;
                for(int j = 0; j < enemyClusters.length; j++)
                {
                    if(enemyClusters[j].location.equals(new MapLocation(0,0)))
                    {
                        continue;
                    }
                    flagScore += Math.sqrt(locations.get(i).distanceSquaredTo(enemyClusters[j].location));
                }
                if(flagScore > bestScore)
                {
                    bestScore = flagScore;
                    bestIndex = i;
                }
            }
            return broadcastFlags[bestIndex];
        }
        else
        {
            //find which flags are most congested with enemies
            double bestScore = -1;
            int bestIndex = 0;
            for(int i = 0; i < broadcastFlags.length; i++)
            {
                double flagScore = 0;
                for(int j = 0; j < enemyClusters.length; j++)
                {
                    if(enemyClusters[j].location.equals(new MapLocation(0,0)))
                    {
                        continue;
                    }
                    flagScore += Math.sqrt(broadcastFlags[i].distanceSquaredTo(enemyClusters[j].location));
                }
                if(flagScore > bestScore)
                {
                    bestScore = flagScore;
                    bestIndex = i;
                }
            }
            return broadcastFlags[bestIndex];
        }
    }

    public static Cluster getMostOverwhelmedCluster(RobotController rc, Cluster[] enemyClusters) throws GameActionException
    {
        int numZeroEnemyClusters = 0;
        for(Cluster cluster : enemyClusters)
        {
            if(cluster.numEnemies == 0)
            {
                numZeroEnemyClusters++;
            }
        }

        if(numZeroEnemyClusters == enemyClusters.length) return Utilities.getClosestCluster(rc);
        int overwhelmFactor = 0;
        int mostOverwhelmedIndex = -1;
        for(int i = 0; i < enemyClusters.length; i++)
        {
            if(enemyClusters[i].numEnemies > overwhelmFactor)
            {
                overwhelmFactor = enemyClusters[i].numEnemies;
                mostOverwhelmedIndex = i;
            }
        }
        return enemyClusters[mostOverwhelmedIndex];

    }

    public static void initializeMacroVariables(RobotController rc) throws GameActionException
    {
        numJailedAllies = rc.readSharedArray(6);
        numJailedEnemies = rc.readSharedArray(8);
        enemyClusters = Utilities.getLastRoundClusters(rc);
        nearbyFlags = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
    }
}

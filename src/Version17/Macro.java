package Version17;

import battlecode.common.*;

import java.util.ArrayList;

import static Version17.Utilities.*;

public class Macro
{
    static int numJailedEnemies;
    static int numJailedAllies;
    static Cluster[] enemyClusters;
    static FlagInfo[] nearbyFlags;
    static MapLocation[] distressedFlags;
    static MapLocation[] broadcastFlags;
    static MapLocation[] knownFlags;
    static MapLocation myLocation;

    static MapLocation[] currentRandLocWithinBroadcast;
    static Flag[] seenFlags;

    public static void doMacro(RobotController rc) throws GameActionException
    {
        initializeMacroVariables(rc);
        MapLocation[] possibleMoveLocations = new MapLocation[6];
        int i = 0;
        for(MapLocation ml : distressedFlags)
        {
            possibleMoveLocations[i] = ml;
            i++;
        }
        if(knownFlags.length > 0)
        {
           for(MapLocation ml : knownFlags)
           {
               possibleMoveLocations[i] = ml;
               i++;
           }
        }
        else
        {
            //we need to activate scouting
            for(int j = 0; j < broadcastFlags.length; j++)
            {
                possibleMoveLocations[i] = broadcastFlags[j];
                i++;
            }
        }

        MapLocation closestPossibleMoveLocation = null;
        int closestDistance = Integer.MAX_VALUE;
        for (MapLocation ml : possibleMoveLocations)
        {
            if(ml == null || ml.equals(NULL_MAP_LOCATION)) continue;
            int tempDistance = ml.distanceSquaredTo(rc.getLocation());
            if (tempDistance < closestDistance)
            {
                closestDistance = tempDistance;
                closestPossibleMoveLocation = ml;
            }
        }

        //we have the last flag
        if(closestPossibleMoveLocation == null)
        {
            MapLocation stolenFlag = Utilities.convertIntToLocation(rc.readSharedArray(58));
            if(!stolenFlag.equals(NULL_MAP_LOCATION))
            {
                StolenFlag temp = new StolenFlag(stolenFlag, false);
                if(Soldier.nearbyFlagsAlly.length == 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(temp.location))))
                    rc.fill(rc.getLocation().add(rc.getLocation().directionTo(temp.location)));
                Pathfinding.bellmanFordFlag(rc, temp.location, temp);
            }
            else
            {
                if(getLowestHealthAllyCluster(rc) != null) {
                    if (Soldier.nearbyFlagsAlly.length == 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(getLowestHealthAllyCluster(rc)))))
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(getLowestHealthAllyCluster(rc))));
                    BFSKernel9x9.BFS(rc, getLowestHealthAllyCluster(rc));
                }
            }
        }
        else
        {
            if(Soldier.nearbyFlagsAlly.length == 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(closestPossibleMoveLocation))))
                rc.fill(rc.getLocation().add(rc.getLocation().directionTo(closestPossibleMoveLocation)));
            BFSKernel9x9.BFS(rc, closestPossibleMoveLocation);
        }
    }

    private static MapLocation generateRandomLocationWithinDistanceSquared(RobotController rc, MapLocation broadcastFlag, int distSquared)
    {
        MapLocation randomLocation = broadcastFlag;
        int x = broadcastFlag.x;
        int y = broadcastFlag.y;

        while(rc.onTheMap(randomLocation) && randomLocation.equals(broadcastFlag) && randomLocation.distanceSquaredTo(broadcastFlag) < 100)
        {
            int randX = x + (int) Math.sqrt((RobotPlayer.rng.nextInt(200) - 100));
            int randY = y + (int) Math.sqrt((RobotPlayer.rng.nextInt(200) - 100));
            randomLocation = new MapLocation(randX, randY);
        }

        return randomLocation;
    }

    public static MapLocation[] getDistressedFlags(RobotController rc) throws GameActionException
    {
        ArrayList<MapLocation> distressedFlags = new ArrayList<>();
        for(int i = 0; i < 2; i++)
        {
            if(Version17.Utilities.readBitSharedArray(rc, 12 + i * 16))
            {
                distressedFlags.add(Version17.Utilities.convertIntToLocation(rc.readSharedArray(i)));
            }
        }
         return distressedFlags.toArray(new MapLocation[0]);
    }

    public static MapLocation getBestFlagLocation(RobotController rc) throws GameActionException
    {
        MapLocation[] broadcastFlags = rc.senseBroadcastFlagLocations();
        ArrayList<MapLocation> locations = new ArrayList<>();
        for(int i = 3; i < 6; i++){
            if(rc.readSharedArray(i) != 0){
                locations.add(Version17.Utilities.convertIntToLocation(rc.readSharedArray(i)));
            }
        }
        if(broadcastFlags.length == 0)
        {
            return Version17.Utilities.getClosestCluster(rc).location;
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

        if(numZeroEnemyClusters == enemyClusters.length) return Version17.Utilities.getClosestCluster(rc);
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

    public static MapLocation getLowestHealthAllyCluster(RobotController rc) throws GameActionException
    {
        int[] allyClusterHealth = Version17.Utilities.getLastRoundAllyHealth(rc);
        int lowest = Integer.MAX_VALUE;
        int lowestIndex = 0;
        for(int i = 0; i < allyClusterHealth.length; i++)
        {
            if(allyClusterHealth[i] < lowest)
            {
                lowest = allyClusterHealth[i];
                lowestIndex = i;
            }
        }
        switch(lowestIndex)
        {
            case 0:
                return Version17.Utilities.convertIntToLocation(rc.readSharedArray(Version17.Utilities.LAST_ROUND_ALLY_LOCATION_1));
            case 1:
                return Version17.Utilities.convertIntToLocation(rc.readSharedArray(Version17.Utilities.LAST_ROUND_ALLY_LOCATION_2));
            case 2:
                return Version17.Utilities.convertIntToLocation(rc.readSharedArray(Version17.Utilities.LAST_ROUND_ALLY_LOCATION_3));
        }
        return null;
    }

    public void checkForEnemyFlags()
    {
        for(FlagInfo flag : nearbyFlags)
        {
        }
    }

    public static void initializeMacroVariables(RobotController rc) throws GameActionException
    {
        numJailedAllies = rc.readSharedArray(6);
        numJailedEnemies = rc.readSharedArray(8);
        enemyClusters = Utilities.getLastRoundClusters(rc);
        nearbyFlags = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
        distressedFlags = getDistressedFlags(rc);
        knownFlags = getKnownFlags(rc);
        broadcastFlags = rc.senseBroadcastFlagLocations();
        myLocation = rc.getLocation();
        currentRandLocWithinBroadcast = new MapLocation[3];
        seenFlags = new Flag[]{new Flag(NULL_MAP_LOCATION, 0), new Flag(NULL_MAP_LOCATION, 0), new Flag(NULL_MAP_LOCATION, 0)};
    }

    static class Flag
    {
       public MapLocation location;
       public int stableRounds;

        public Flag(MapLocation location, int stableRounds)
        {
            this.location = location;
            this.stableRounds = stableRounds;
        }
    }
}

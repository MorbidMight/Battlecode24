package Version18;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Arrays;

import static Version18.Utilities.*;

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
    static final Flag NULL_FLAG = new Macro.Flag(NULL_MAP_LOCATION, 0, -1000);
    public static ArrayList<MapLocation> currentRandLocWithinBroadcast;
    public static Flag[] seenFlags;

    public static void doMacro(RobotController rc) throws GameActionException
    {
        MapLocation[] possibleMoveLocations = new MapLocation[6];
        int[] possibleMoveScores = new int[6];
        int i = 0;
        for(MapLocation ml : distressedFlags)
        {
            possibleMoveLocations[i] = ml;
            possibleMoveScores[i] = ml.distanceSquaredTo(rc.getLocation());
            i++;
        }
        if(knownFlags.length > 0)
        {
            for(MapLocation ml : knownFlags)
            {
                possibleMoveLocations[i] = ml;
                possibleMoveScores[i] = ml.distanceSquaredTo(rc.getLocation());
                i++;
            }
        }
        else
        {
            if(broadcastFlags.length == 0)
            {
                possibleMoveLocations[i] = getClosestCluster(rc).location;
                possibleMoveScores[i] = rc.getLocation().distanceSquaredTo(getClosestCluster(rc).location);
                i++;
            }
            //we need to activate scouting
            for(int k = 0; k < currentRandLocWithinBroadcast.size(); k++)
            {
                possibleMoveLocations[i] = currentRandLocWithinBroadcast.get(k);
                possibleMoveScores[i] = rc.getLocation().distanceSquaredTo(currentRandLocWithinBroadcast.get(k));
                i++;
            }


        }

        MapLocation closestPossibleMoveLocation = null;
        int closestDistance = Integer.MAX_VALUE;
        for (int j = 0; i < possibleMoveScores.length; i++)
        {
            if(possibleMoveLocations[j] == null || possibleMoveLocations[j].equals(NULL_MAP_LOCATION)) continue;
            int tempScore = possibleMoveScores[j];
            if (possibleMoveScores[j] < closestDistance)
            {
                closestDistance = tempScore;
                closestPossibleMoveLocation = possibleMoveLocations[j];
            }
        }

         rc.setIndicatorString(String.valueOf(currentRandLocWithinBroadcast));

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
                if(getClosestCluster(rc).location != null) {
                    if (Soldier.nearbyFlagsAlly.length == 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(getLowestHealthAllyCluster(rc)))))
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(getLowestHealthAllyCluster(rc))));
                    BFSKernel9x9.BFS(rc, getClosestCluster(rc).location);
                }
            }
        }
        else
        {
            if(Soldier.nearbyFlagsAlly.length == 0 && rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(closestPossibleMoveLocation))))
                rc.fill(rc.getLocation().add(rc.getLocation().directionTo(closestPossibleMoveLocation)));
            BFSKernel9x9.BFS(rc, closestPossibleMoveLocation);
        }

        //rc.setIndicatorString("" + closestPossibleMoveLocation + broadcastFlags.length);
    }

    public static void updateRandomLocList(RobotController rc)
    {
        ArrayList<MapLocation> locs = new ArrayList<>();
        for(int i = 0; i < broadcastFlags.length; i++)
        {
            locs.add(generateRandomLocationWithinDistanceSquared(rc, broadcastFlags[i], 100));
        }
        currentRandLocWithinBroadcast = locs;
    }

    private static MapLocation generateRandomLocationWithinDistanceSquared(RobotController rc, MapLocation broadcastFlag, int distSquared)
    {
        MapLocation randomLocation = broadcastFlag;
        int x = broadcastFlag.x;
        int y = broadcastFlag.y;

        while(!rc.onTheMap(randomLocation) || randomLocation.equals(broadcastFlag) || randomLocation.distanceSquaredTo(broadcastFlag) > 100)
        {
            int randX = x + RobotPlayer.rng.nextInt(20) - 10;
            int randY = y + RobotPlayer.rng.nextInt(20) - 10;
            randomLocation = new MapLocation(randX, randY);
        }

        return randomLocation;
    }

    public static MapLocation[] getDistressedFlags(RobotController rc) throws GameActionException
    {
        ArrayList<MapLocation> distressedFlags = new ArrayList<>();
        for(int i = 40; i < 43; i++)
        {
            if(Utilities.readBitSharedArray(rc, 12 + i * 16))
            {
                distressedFlags.add(Utilities.convertIntToLocation(rc.readSharedArray(i)));
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

    public static MapLocation getLowestHealthAllyCluster(RobotController rc) throws GameActionException
    {
        int[] allyClusterHealth = Utilities.getLastRoundAllyHealth(rc);
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
                return Utilities.convertIntToLocation(rc.readSharedArray(Utilities.LAST_ROUND_ALLY_LOCATION_1));
            case 1:
                return Utilities.convertIntToLocation(rc.readSharedArray(Utilities.LAST_ROUND_ALLY_LOCATION_2));
            case 2:
                return Utilities.convertIntToLocation(rc.readSharedArray(Utilities.LAST_ROUND_ALLY_LOCATION_3));
        }
        return null;
    }

    public static void tryWriteFlagToArray(RobotController rc, MapLocation flagLoc) throws GameActionException
    {
        for(int i = 3; i < 6; i++)
        {
           MapLocation tempLoc = convertIntToLocation(rc.readSharedArray(i));
           if(tempLoc.equals(NULL_MAP_LOCATION))
           {
               rc.writeSharedArray(i, convertLocationToInt(flagLoc));
           }
        }
    }

    public static void checkForEnemyFlags(RobotController rc) throws GameActionException
    {
        for(FlagInfo flag : nearbyFlags)
        {
            int currentFlagIndex = -1;
            for(int i = 0; i < seenFlags.length; i++)
            {
                if(seenFlags[i].location.equals(flag.getLocation()))
                {
                    currentFlagIndex = i;
                }
            }
            if(currentFlagIndex != -1)
            {
                seenFlags[currentFlagIndex] =
                        new Flag(seenFlags[currentFlagIndex].location,
                                seenFlags[currentFlagIndex].stableRounds + 1,
                                RobotPlayer.turnCount);
                if(seenFlags[currentFlagIndex].stableRounds > 2)
                {
                    tryWriteFlagToArray(rc, seenFlags[currentFlagIndex].location);
                }
            }
            else
            {
                for(int i = 0; i < seenFlags.length; i++)
                {
                    if(seenFlags[i].turnLastSeen == -1000)
                    {
                       seenFlags[i] = new Flag(flag.getLocation(), 0, RobotPlayer.turnCount);
                    }
                }
            }
        }
    }

    public static void clearEnemyFlagsGlobal(RobotController rc) throws GameActionException
    {
        for(int i = 3; i < 6; i++)
        {
            MapLocation tempLoc = convertIntToLocation(rc.readSharedArray(i));
            if(!tempLoc.equals(NULL_MAP_LOCATION) && rc.canSenseLocation(tempLoc))
            {
                boolean canSeeFlagAtTempLoc = false;
                for(FlagInfo flag : nearbyFlags)
                {
                    if(flag.getLocation().equals(tempLoc))
                    {
                        canSeeFlagAtTempLoc = true;
                    }
                }

                if(!canSeeFlagAtTempLoc)
                {
                    rc.writeSharedArray(i, 0);
                }
            }
        }
    }

    public static void printEnemyFlags(RobotController rc) throws GameActionException
    {
        String str = "";
        for(int i = 3; i < 6; i++)
        {
            str += "" + Utilities.convertIntToLocation(rc.readSharedArray(i)) + " ";
        }
        //System.out.println(str);
    }

    public static void clearEnemyFlagsLocal(RobotController rc)
    {
        for(FlagInfo flag : nearbyFlags)
        {
            for(int i = 0; i < seenFlags.length; i++)
            {
                if(seenFlags[i].location.equals(flag.getLocation()) && RobotPlayer.turnCount - seenFlags[i].turnLastSeen > 6)
                {
                    seenFlags[i] = Macro.NULL_FLAG;
                }
            }
        }
    }
    public static void initializeMacroVariables(RobotController rc) throws GameActionException
    {
        numJailedAllies = rc.readSharedArray(6);
        numJailedEnemies = rc.readSharedArray(8);
        enemyClusters = Utilities.getLastRoundClusters(rc);
        nearbyFlags = rc.senseNearbyFlags(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
        distressedFlags = getDistressedFlags(rc);
        knownFlags = getKnownFlags(rc);
        broadcastFlags = rc.senseBroadcastFlagLocations();
        myLocation = rc.getLocation();
    }

    public static class Flag
    {
        public MapLocation location;
        public int stableRounds;
        public int turnLastSeen;

        public Flag(MapLocation location, int stableRounds, int turnLastSeen)
        {
            this.location = location;
            this.stableRounds = stableRounds;
            this.turnLastSeen = turnLastSeen;
        }
    }
}

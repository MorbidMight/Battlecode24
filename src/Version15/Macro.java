package Version15;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Macro
{
    static int numJailedEnemies;
    static int numJailedAllies;
    static int turnsOnFront;
    static Cluster[] enemyClusters;
    public static void doMacro(RobotController rc) throws GameActionException
    {
        initializeMacroVariables(rc);

        //maybe assign a cost to each possible action, what are the possible actions?
        //moving towards a flag, moving towards a cluster, rotate off a front,

        /*
            first decision is whether to move towards a cluster or a flag
         */
        if(numJailedEnemies > numJailedAllies)
        {
            //move towards flag

        }else
        {
            //move towards cluster, most overwhelmed one
            Cluster mostOverwhelmed = getMostOverwhelmedCluster(enemyClusters);
            BFSKernel.BFS(rc, mostOverwhelmed.location);
        }
    }

    public static Cluster getMostOverwhelmedCluster(Cluster[] enemyClusters)
    {
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
    }
}

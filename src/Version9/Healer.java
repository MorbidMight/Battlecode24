package Version9;

import battlecode.common.*;

import static Version8.RobotPlayer.findClosestBroadcastFlags;

public class Healer
{
    public static void runHealer(RobotController rc) throws GameActionException {
        RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        RobotInfo[] allies = rc.senseNearbyRobots(-1, rc.getTeam());
        RobotInfo[] alliesInHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());

        runHeal(rc, alliesInHealRange);

        if(rc.readSharedArray(58) != 0)
        {
            Pathfinding.tryToMove(rc, Utilities.convertIntToLocation(rc.readSharedArray(58)));
            rc.setIndicatorString("Helping teammate @ " + Utilities.convertIntToLocation(rc.readSharedArray(58)));
        }
        else
        {
            if (enemies.length == 0)
            {
                MapLocation closestBroadcast = findClosestBroadcastFlags(rc);
                if (closestBroadcast != null) {
                    if(rc.isMovementReady()) Pathfinding.bugNav2(rc, closestBroadcast);
                };
            }
            else if (allies.length > enemies.length)
            {
                Pathfinding.tryToMove(rc, rc.adjacentLocation(rc.getLocation().directionTo(Utilities.averageRobotLocation(allies))));
            }
            else
            {
                Pathfinding.tryToMove(rc, rc.adjacentLocation(rc.getLocation().directionTo(Utilities.averageRobotLocation(enemies)).opposite()));
            }
        }

        runHeal(rc, alliesInHealRange);


    }

    public static void runHeal(RobotController rc, RobotInfo[] alliesInHealRange) throws GameActionException
    {
        if(rc.isActionReady())
        {
            RobotInfo toHeal = Utilities.bestHeal(rc, alliesInHealRange);
            if(toHeal != null)
            {
                if(rc.canHeal(toHeal.location))
                {
                    rc.heal(toHeal.location);
                }
            }
        }
    }

}

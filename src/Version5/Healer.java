package Version5;

import battlecode.common.*;

public class Healer
{
    public static void runHealer(RobotController rc) throws GameActionException {

        RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        RobotInfo[] allies = rc.senseNearbyRobots(-1, rc.getTeam());

        if(enemies.length > allies.length)
        {
            Pathfinding.tryToMove(rc, rc.getLocation().add(rc.getLocation().directionTo(Utilities.averageRobotLocation(enemies)).opposite()));
        }
        else
        {
            MapLocation closestEnemy = Utilities.newGetClosestEnemy(rc);
            if(closestEnemy != null)
                Pathfinding.bugNav2(rc, closestEnemy);
        }

        RobotInfo[] alliesInHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
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

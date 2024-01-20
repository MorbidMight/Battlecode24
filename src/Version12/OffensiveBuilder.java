package Version12;

import Version12.Pathfinding;
import Version12.RobotPlayer;
import Version12.Soldier;
import Version12.Utilities;
import battlecode.common.*;

import java.util.ArrayList;

public class OffensiveBuilder {
    static RobotInfo[] enemyRobots;
    static RobotInfo[] enemyRobotsAttackRange;
    static RobotInfo[] allyRobots;
    static RobotInfo[] allyRobotsHealRange;
    static FlagInfo[] enemyFlagsPickedUp;

    public static void runOffensiveBuilder(RobotController rc) throws GameActionException {
        if (!rc.isSpawned())
            return;
        updateInfo(rc);
        if (enemyRobotsAttackRange.length != 0) {
            if (rc.canMove(rc.getLocation().directionTo(enemyRobotsAttackRange[0].getLocation()).opposite())) {
                rc.move(rc.getLocation().directionTo(enemyRobotsAttackRange[0].getLocation()).opposite());
            }
        } else if (enemyRobots.length > 5) {
            if (rc.canMove(rc.getLocation().directionTo(enemyRobots[0].getLocation()).opposite())) {
                rc.move(rc.getLocation().directionTo(enemyRobots[0].getLocation()).opposite());
            }
        } else {
            MapLocation targetLoc;
            if (allyRobots.length >= 2)
                targetLoc = Version10SoldierStates.Utilities.averageRobotLocation(allyRobots);
            else {
                if (Version10SoldierStates.Soldier.knowFlag(rc)) {
                    targetLoc = Soldier.findCoordinatedActualFlag(rc);
                } else {
                    targetLoc = RobotPlayer.findCoordinatedBroadcastFlag(rc);
                }
            }
            if (targetLoc != null)
                Pathfinding.tryToMove(rc, targetLoc);
        }
        attemptBuild(rc);
    }

    public static void attemptBuild(RobotController rc) throws GameActionException {
        if ((enemyRobots.length >= 1 || enemyFlagsPickedUp.length > 0) && rc.getCrumbs() > 300) {
            if ((enemyRobots.length >= 1)) {
                if (rc.canBuild(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(Version10SoldierStates.Utilities.averageRobotLocation(enemyRobots))))) {
                    rc.build(TrapType.STUN, rc.getLocation().add(rc.getLocation().directionTo(Utilities.averageRobotLocation(enemyRobots))));
                }
            }
            for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
                if (rc.canBuild(TrapType.STUN, t.getMapLocation())) {
                    rc.build(TrapType.STUN, t.getMapLocation());
                }
            }
        }
    }


    public static void updateInfo(RobotController rc) throws GameActionException {
        //take in important info around you
        enemyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
        enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
        allyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam());
        allyRobotsHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
        enemyFlagsPickedUp = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
        cleanseFlagsPickedUp();
    }
    //keeps only picked up flags, so we can know if we are escorting a heist
    public static void cleanseFlagsPickedUp(){
        if(enemyFlagsPickedUp.length == 0)
            return;
        ArrayList<FlagInfo> temp = new ArrayList<FlagInfo>();
        for(FlagInfo flag : enemyFlagsPickedUp){
            if(flag.isPickedUp())
                temp.add(flag);
        }
        enemyFlagsPickedUp = temp.toArray(enemyFlagsPickedUp);
    }

}

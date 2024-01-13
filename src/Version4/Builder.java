package Version4;

import battlecode.common.*;

import static Version4.RobotPlayer.*;

public class Builder {
    public static void runBuilder(RobotController rc) throws GameActionException {
        if(!rc.isSpawned()) {
            Clock.yield();
        }
        Task t = Utilities.readTask(rc);
        if (SittingOnFlag) {
            if(countSinceLocked !=0){
                countSinceLocked++;
            }
            if(countSinceLocked >= 20){
                countSinceLocked = 0;
                Utilities.editBitSharedArray(rc, 1021, false);
            }
            //check if nearby enemies are coming to attack, call for robots to prioritize spawning at ur flag
            if(rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length > rc.senseNearbyRobots(-1, rc.getTeam()).length){
                //spawn everyone in 0-8 if possible, also lock so it wont cycle
                if(rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(0)))){
                    Utilities.editBitSharedArray(rc, 1022, false);
                    Utilities.editBitSharedArray(rc, 1023, false);
                    //lock
                    Utilities.editBitSharedArray(rc, 1021, true);
                }
                else if(rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(1)))){
                    Utilities.editBitSharedArray(rc, 1022, false);
                    Utilities.editBitSharedArray(rc, 1023, true);
                    //lock
                    Utilities.editBitSharedArray(rc, 1021, true);
                }
                else{
                    Utilities.editBitSharedArray(rc, 1022, true);
                    Utilities.editBitSharedArray(rc, 1023, false);
                    //lock
                    Utilities.editBitSharedArray(rc, 1021, true);
                }
                countSinceLocked++;
            }
            System.out.println("A");
            UpdateExplosionBorder(rc);
        } else if (t != null) {//there is a task to do
            Pathfinding.tryToMove(rc, t.location);
            if (locationIsActionable(rc, t.location)) {
                if (rc.canBuild(TrapType.EXPLOSIVE, t.location)) {
                    rc.build(TrapType.EXPLOSIVE, t.location);
                }
            }

        } else {//there is no task to be done


            //There is no task to be done and all the flags have guys sitting on them
            //Move away from the nearest guys avoiding ops especicially
            Direction d = directionToMove(rc);
            for(int i = 0;i<8;i++){
                if(rc.canMove(d)){
                    rc.move(d);
                }
                d= d.rotateLeft();
            }

            if(rc.getRoundNum()%8==0/*&&distanceFromNearestSpawnLocation(rc)>16*/)
                UpdateExplosionBorder(rc);

        }
        RobotInfo[] enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
        MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
        if (toAttack != null && rc.canAttack(toAttack))
            rc.attack(toAttack);
    }


    private static Direction directionToMove(RobotController rc) {
        return directions[rng.nextInt(8)];
    }

    private static int distanceFromNearestSpawnLocation(RobotController rc){
return 0;
    }

    public static boolean locationIsActionable(RobotController rc, MapLocation m) throws GameActionException {
        MapInfo[] ActionableTiles = rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED);
        for (MapInfo curr : ActionableTiles) {
            if (m.equals(curr.getMapLocation())) {
                return true;
            }

        }
        return false;
    }

    public static void UpdateExplosionBorder(RobotController rc) throws GameActionException {
        for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
            if (rc.canBuild(TrapType.EXPLOSIVE, t.getMapLocation())&&t.getTrapType().equals(TrapType.NONE)) {
                rc.build(TrapType.EXPLOSIVE, t.getMapLocation());
                System.out.println("Building a bomb");
            }
        }
    }

}

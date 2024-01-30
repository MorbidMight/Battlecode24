package Version18;

import battlecode.common.*;
import battlecode.common.RobotController;
import Version18.RobotPlayer.*;

import static Version18.RobotPlayer.findClosestSpawnLocation;
import static Version18.RobotPlayer.turnsWithKills;

public class FlagSitter {
    static MapLocation home = null;
    static MapLocation tempHome = null;
    static int countSinceSeenFlag = 0;
    static boolean isActive = true;
    static int countSinceLocked;
    static MapLocation refill = null;

    public static void runFlagSitter(RobotController rc) throws GameActionException {
        //if(rc.getRoundNum() == 250) rc.resign();
        if(refill != null && rc.canDig(refill)){
            rc.dig(refill);
            refill = null;
        }
        if (home == null && tempHome == null) {
            System.out.println("Something is wrong");
            findHome(rc);
        }
        if (home == null && tempHome != null) {
            Pathfinding.combinedPathfinding(rc, tempHome);
        }
        if (!rc.getLocation().equals(home)) {
            clearWayHome(rc);
            if(refill == null || rc.getLocation().equals(refill))
                Pathfinding.combinedPathfinding(rc, home);
        } else {
            //update where we want soldiers to spawn
            if (!Utilities.readBitSharedArray(rc, 1021)) {
                int x;
                if (Soldier.knowFlag(rc))
                    x = RobotPlayer.findClosestSpawnLocationToCoordinatedTarget(rc);
                else if (Utilities.getClosestCluster(rc) != null) {
                    x = RobotPlayer.findClosestSpawnLocationToCluster(rc);
                } else {
                    x = RobotPlayer.findClosestSpawnLocationToCoordinatedBroadcast(rc);
                }
                if (x != -1) {
                    //00
                    if (x == 0) {
                        Utilities.editBitSharedArray(rc, 1023, false);
                        Utilities.editBitSharedArray(rc, 1022, false);
                    }
                    //01
                    else if (x == 1) {
                        Utilities.editBitSharedArray(rc, 1022, false);
                        Utilities.editBitSharedArray(rc, 1023, true);
                    }
                    //x == 2, desire 10
                    else if (x == 2) {
                        Utilities.editBitSharedArray(rc, 1022, true);
                        Utilities.editBitSharedArray(rc, 1023, false);
                    }
                }
            }

            if (isActive) {
                if (rc.canBuild(TrapType.STUN, rc.getLocation()))
                    rc.build(TrapType.STUN, rc.getLocation());
                Builder.UpdateExplosionBorder2(rc);
            }
            RobotInfo toHeal = Utilities.bestHeal(rc, rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam()));
            if (toHeal != null && rc.canHeal(toHeal.getLocation())) {
                rc.heal(toHeal.getLocation());
            }
            if (rc.isActionReady()) {
                MapLocation toAttack = RobotPlayer.lowestHealth(rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent()));
                if (toAttack != null && rc.canAttack(toAttack)) {
                    if (rc.senseRobotAtLocation(toAttack).health <= rc.getAttackDamage() && !rc.senseRobotAtLocation(toAttack).team.isPlayer())
                        Utilities.addKillToKillsArray(rc, turnsWithKills);
                    rc.attack(toAttack);
                }
            }
            //sitting where flag should be, but cant see any flags...
            //if we still cant see a flag 50 turns later, then until we do see one we're gonna assume this location should essentially be shut down
            if (rc.senseNearbyFlags(-1, rc.getTeam()).length == 0) {
                //shut down this spawn location for now
                if (countSinceSeenFlag > 30) {
                    rc.setIndicatorString("Dont come help me!");
                    isActive = false;
                    countSinceSeenFlag++;
                    if (countSinceLocked != 0) {
                        Utilities.editBitSharedArray(rc, 1021, false);
                    }
                    if (countSinceSeenFlag > 40) {
                        RobotPlayer.role = RobotPlayer.roles.soldier;
                        return;
                    }
                    int locInt = Utilities.convertLocationToInt(rc.getLocation());
                    if (rc.readSharedArray(0) == locInt) {
                        Utilities.editBitSharedArray(rc, 1018, false);
                    } else if (rc.readSharedArray(1) == locInt) {
                        Utilities.editBitSharedArray(rc, 1019, false);
                    } else if (rc.readSharedArray(2) == locInt) {
                        Utilities.editBitSharedArray(rc, 1020, false);
                    }
                    return;
                } else {
                    countSinceSeenFlag++;
                }
            } else {
                isActive = true;
                countSinceSeenFlag = 0;
                int locInt = Utilities.convertLocationToInt(rc.getLocation());
                if (rc.readSharedArray(0) == locInt) {
                    Utilities.editBitSharedArray(rc, 1018, true);
                } else if (rc.readSharedArray(1) == locInt) {
                    Utilities.editBitSharedArray(rc, 1019, true);
                } else if (rc.readSharedArray(2) == locInt) {
                    Utilities.editBitSharedArray(rc, 1020, true);
                }
            }
            if (countSinceLocked != 0) {
                countSinceLocked++;
            }
            if (countSinceLocked >= 30) {
                countSinceLocked = 0;
                Utilities.editBitSharedArray(rc, 1021, false);
                if (rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(40))))
                    Utilities.editBitSharedArray(rc, 652, false);
                else if (rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(41))))
                    Utilities.editBitSharedArray(rc, 668, false);
                else if (rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(42))))
                    Utilities.editBitSharedArray(rc, 684, false);
            }
            //check if nearby enemies are coming to attack, call for robots to prioritize spawning at ur flag


            if (isActive && rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent()).length > rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam()).length) {
                lockSpawnNearestLocation(rc);

                countSinceLocked++;
            }
            if(isActive && rc.senseNearbyRobots(-1,rc.getTeam().opponent()).length==1){
                for(RobotInfo t: rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
                    if(t.hasFlag){
                        if(rc.canAttack(t.location))
                            rc.attack(t.location);
                        Pathfinding.combinedPathfinding(rc,t.getLocation());
                    }
                }
            }
        }
    }

    public static void lockSpawnNearestLocation(RobotController rc) throws GameActionException {
//        MapLocation spawnLoc1 = Utilities.convertIntToLocation(rc.readSharedArray(0));
//        MapLocation spawnLoc2 = Utilities.convertIntToLocation(rc.readSharedArray(1));
//        MapLocation spawnLoc3 = Utilities.convertIntToLocation(rc.readSharedArray(2));
        MapLocation target = findClosestSpawnLocation(rc);
        if(target.equals(Utilities.convertIntToLocation(0))) {
            Utilities.editBitSharedArray(rc, 1022, false);
            Utilities.editBitSharedArray(rc, 1023, false);
        } else if (target.equals(Utilities.convertIntToLocation(rc.readSharedArray(1)))) {
            Utilities.editBitSharedArray(rc, 1022, false);
            Utilities.editBitSharedArray(rc, 1023, true);
        } else {
            Utilities.editBitSharedArray(rc, 1022, true);
            Utilities.editBitSharedArray(rc, 1023, false);
        }
        if (rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(40)))) {
            Utilities.editBitSharedArray(rc, 652, true);
        } else if (rc.getLocation().equals(Utilities.convertIntToLocation(rc.readSharedArray(41)))) {
            Utilities.editBitSharedArray(rc, 668, true);
        } else {
            Utilities.editBitSharedArray(rc, 684, true);
        }
        Utilities.editBitSharedArray(rc, 1021, true);

    }

    public static void findHome(RobotController rc) throws GameActionException {
        MapLocation spawnLoc1 = Utilities.convertIntToLocation(rc.readSharedArray(0));
        MapLocation spawnLoc2 = Utilities.convertIntToLocation(rc.readSharedArray(1));
        MapLocation spawnLoc3 = Utilities.convertIntToLocation(rc.readSharedArray(2));
        if(rc.canSenseLocation(spawnLoc1) && !rc.canSenseRobotAtLocation(spawnLoc1)){
            home = spawnLoc1;
            tempHome = null;
        }
        else if(rc.canSenseLocation(spawnLoc2) && !rc.canSenseRobotAtLocation(spawnLoc2)){
            home = spawnLoc2;
            tempHome = null;
        }
        else if(rc.canSenseLocation(spawnLoc3) && !rc.canSenseRobotAtLocation(spawnLoc3)){
            home = spawnLoc3;
            tempHome = null;
        }
        else if(!rc.canSenseLocation(spawnLoc1)){
            tempHome = spawnLoc1;
        }
        else if(!rc.canSenseLocation(spawnLoc2)){
            tempHome = spawnLoc2;
        }
        else if(!rc.canSenseLocation(spawnLoc3)){
            tempHome = spawnLoc3;
        }
    }

    public static void clearWayHome(RobotController rc) throws GameActionException {
        Direction d = rc.getLocation().directionTo(home);
        if (!rc.senseMapInfo(rc.getLocation().add(d)).isPassable() && !rc.senseMapInfo(rc.getLocation().add(d.rotateLeft())).isPassable() && !rc.senseMapInfo(rc.getLocation().add(d.rotateRight())).isPassable()) {
            if (rc.canFill(rc.getLocation().add(d))) {
                rc.fill(rc.getLocation().add(d));
                refill = rc.getLocation().add(d);
                if (rc.canMove(d)) rc.move(d);
            } else if (rc.canFill(rc.getLocation().add(d.rotateLeft()))) {
                rc.fill(rc.getLocation().add(d.rotateLeft()));
                refill = rc.getLocation().add(d.rotateLeft());
                if (rc.canMove(d.rotateLeft())) rc.move(d.rotateLeft());
            } else if (rc.canFill(rc.getLocation().add(d.rotateRight()))) {
                rc.fill(rc.getLocation().add(d.rotateRight()));
                refill = rc.getLocation().add(d.rotateRight());
                if (rc.canMove(d.rotateRight())) rc.move(d.rotateRight());
            }
        }
    }
}

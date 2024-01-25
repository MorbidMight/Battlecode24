package Version15;

import battlecode.common.*;

import java.util.ArrayList;

import static Version15.RobotPlayer.*;
import static Version15.Utilities.averageRobotLocation;
import static Version15.Utilities.bestHeal;

public class OffensiveBuilder {
    static RobotInfo[] enemyRobots;
    static RobotInfo[] enemyRobotsAttackRange;
    static RobotInfo[] allyRobots;
    static RobotInfo[] potentialEnemiesAttackRange;
    static RobotInfo[] allyRobotsHealRange;
    static FlagInfo[] enemyFlagsPickedUp;



    public static void runOffensiveBuilder(RobotController rc) throws GameActionException {
        if (!rc.isSpawned())
            return;
        rc.setIndicatorDot(rc.getLocation(), 200, 100, 200);
        updateInfo(rc);
        if(enemyRobots.length > 0)
            runMicroOffensiveBuilder(rc);
        else {
            MapLocation targetLoc;
            targetLoc = RobotPlayer.findClosestActualFlag(rc);
            if(targetLoc == null)
                targetLoc = Utilities.getClosestCluster(rc);
            if(targetLoc == null)
                targetLoc = RobotPlayer.findClosestBroadcastFlags(rc);
            if(targetLoc == null && allyRobots.length > 0)
                targetLoc = Utilities.averageRobotLocation(allyRobots);
            if(targetLoc == null)
                targetLoc = RobotPlayer.findClosestSpawnLocation(rc);
            Pathfinding.combinedPathfinding(rc, targetLoc);
        }
        attemptBuild(rc);
        updateInfo(rc);
        if(rc.isActionReady()){
            MapLocation toAttack = lowestHealth(enemyRobotsAttackRange);
            if (toAttack != null && rc.canAttack(toAttack)) {
                if(rc.senseRobotAtLocation(toAttack).health <= rc.getAttackDamage()&&!rc.senseRobotAtLocation(toAttack).team.equals(rc.getTeam()))
                    Utilities.addKillToKillsArray(rc,turnsWithKills);
                rc.attack(toAttack);
            }
            if(rc.isActionReady()){
                if (toAttack != null && rc.canAttack(toAttack)) {
                    if(rc.senseRobotAtLocation(toAttack).health <= rc.getAttackDamage()&&!rc.senseRobotAtLocation(toAttack).team.equals(rc.getTeam()))
                        Utilities.addKillToKillsArray(rc,turnsWithKills);
                    rc.attack(toAttack);
                }
            }
            RobotInfo toHeal = bestHeal(rc, allyRobotsHealRange);
            if(toHeal != null && rc.canHeal(toHeal.getLocation()) && toHeal.health < 800)
                rc.heal(toHeal.getLocation());
        }
//        if (enemyRobotsAttackRange.length != 0) {
//            if (rc.canMove(rc.getLocation().directionTo(enemyRobotsAttackRange[0].getLocation()).opposite())) {
//                rc.move(rc.getLocation().directionTo(enemyRobotsAttackRange[0].getLocation()).opposite());
//            }
//        } else if (enemyRobots.length > 5) {
//            if (rc.canMove(rc.getLocation().directionTo(enemyRobots[0].getLocation()).opposite())) {
//                rc.move(rc.getLocation().directionTo(enemyRobots[0].getLocation()).opposite());
//            }
//        } else {
//            MapLocation targetLoc;
//            if (allyRobots.length >= 2)
//                targetLoc = Version10SoldierStates.Utilities.averageRobotLocation(allyRobots);
//            else {
//                if (Version10SoldierStates.Soldier.knowFlag(rc)) {
//                    targetLoc = Soldier.findCoordinatedActualFlag(rc);
//                } else {
//                    targetLoc = RobotPlayer.findCoordinatedBroadcastFlag(rc);
//                }
//            }
//            if (targetLoc != null)
//                Pathfinding.tryToMove(rc, targetLoc);
//        }
//        attemptBuild(rc);
    }

    public static void attemptBuild(RobotController rc) throws GameActionException {
        /*if ((enemyRobots.length >= 1 || enemyFlagsPickedUp.length > 0)) {
            if ((enemyRobots.length >= 1)) {
                MapLocation target = rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots)));
                if (!Soldier.isTrapAdjacent(rc, target) && rc.canBuild(TrapType.STUN, target)) {
                    rc.build(TrapType.STUN, target);
                }
            }
            for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
                if (!Soldier.isTrapAdjacent(rc, t.getMapLocation()) && rc.canBuild(TrapType.STUN, t.getMapLocation())) {
                    rc.build(TrapType.STUN, t.getMapLocation());
                }
            }
        }
        else if(rc.getCrumbs() > 3000){
            for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
                if (!Soldier.isTrapAdjacent(rc, t.getMapLocation()) && rc.canBuild(TrapType.STUN, t.getMapLocation())) {
                    rc.build(TrapType.STUN, t.getMapLocation());
                }
            }
        }*/
        if(enemyRobots.length>=1 || enemyFlagsPickedUp.length > 0){
            MapLocation target = rc.getLocation().add(rc.getLocation().directionTo(averageRobotLocation(enemyRobots)));
            if (!Soldier.isTrapAdjacent(rc, target) && rc.canBuild(TrapType.STUN, target)) {
                rc.build(TrapType.STUN, target);
            }
            for(MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)){
                TrapType toBeBuilt = TrapType.STUN;
                if(rc.getCrumbs()>3000 || enemyRobots.length>=1){
                    if(!Soldier.isTrapAdjacent(rc,t.getMapLocation()) && rc.canBuild(toBeBuilt,t.getMapLocation())){
                        rc.build(toBeBuilt,t.getMapLocation());
                    }
                }
            }
        }

    }
    public static void runMicroOffensiveBuilder(RobotController rc) throws GameActionException {
        engagementMicroSquare[] options = new engagementMicroSquare[8];
        populateMicroArray(rc, options);
        engagementMicroSquare best = null;
        float highScore = Integer.MIN_VALUE;
        for(engagementMicroSquare square : options){
            if(square.passable){
                float score = square.enemiesAttackRangedX * -5 + square.enemiesVisiondX * 2 + square.alliesVisiondX * 2.0f + square.alliesHealRangedX + square.potentialEnemiesAttackRangedX * -4;
                if(score > highScore){
                    highScore = score;
                    best = square;
                }
            }
        }
        if(best != null) {
            //make sure the move isnt outright harmful, or too stupid
            if (best.enemiesAttackRangedX == 0 && best.potentialEnemiesAttackRangedX <= 1 && rc.canMove(rc.getLocation().directionTo(best.location))) {
                rc.move(rc.getLocation().directionTo(best.location));
            }
        }
    }
    public static void populateMicroArray(RobotController rc, engagementMicroSquare[] options) throws GameActionException {
        int index = 0;
        MapLocation curLoc = rc.getLocation();
        int mapWidth = rc.getMapWidth();
        int mapHeight = rc.getMapHeight();
        int curX = curLoc.x;
        int curY = curLoc.y;
        int newX = 0;
        int newY = 0;
        for (int k = -1; k <= 1; k++) {
//            if (-1 == 0 && k == 0)
//                continue;
            newX = curX + -1;
            newY = curY + k;
            if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
                options[index] = new engagementMicroSquare(newX, newY);
                MapLocation tempSquare = new MapLocation(newX, newY);
                MapInfo tempInfo = rc.senseMapInfo(tempSquare);
                options[index].passable = (tempInfo.isPassable() && rc.senseRobotAtLocation(tempSquare) == null);
                if (options[index].passable) {
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = potentialEnemiesAttackRangeNewLoc.length - potentialEnemiesAttackRange.length;
                }
            } else {
                options[index] = new engagementMicroSquare(false);
            }
            index++;
        }
        for (int k = -1; k <= 1; k++) {
            if (0 == 0 && k == 0)
                continue;
            newX = curX + 0;
            newY = curY + k;
            if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
                options[index] = new engagementMicroSquare(newX, newY);
                MapLocation tempSquare = new MapLocation(newX, newY);
                MapInfo tempInfo = rc.senseMapInfo(tempSquare);
                options[index].passable = (tempInfo.isPassable() && rc.senseRobotAtLocation(tempSquare) == null);
                if (options[index].passable) {
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = potentialEnemiesAttackRangeNewLoc.length - potentialEnemiesAttackRange.length;
                }
            } else {
                options[index] = new engagementMicroSquare(false);
            }
            index++;
        }
        for (int k = -1; k <= 1; k++) {
//            if (1 == 0 && k == 0)
//                continue;
            newX = curX + 1;
            newY = curY + k;
            if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
                options[index] = new engagementMicroSquare(newX, newY);
                MapLocation tempSquare = new MapLocation(newX, newY);
                MapInfo tempInfo = rc.senseMapInfo(tempSquare);
                options[index].passable = (tempInfo.isPassable() && rc.senseRobotAtLocation(tempSquare) == null);
                if (options[index].passable) {
                    RobotInfo[] enemyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsNewLoc = rc.senseNearbyRobots(tempSquare, -1, rc.getTeam());
                    RobotInfo[] enemyRobotsAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
                    RobotInfo[] allyRobotsHealRangeNewLoc = rc.senseNearbyRobots(tempSquare, GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
                    RobotInfo[] potentialEnemiesAttackRangeNewLoc = rc.senseNearbyRobots(tempSquare, 10, rc.getTeam().opponent());
                    options[index].enemiesVisiondX = enemyRobotsNewLoc.length - enemyRobots.length;
                    options[index].enemiesAttackRangedX = enemyRobotsAttackRangeNewLoc.length - enemyRobotsAttackRange.length;
                    options[index].alliesVisiondX = allyRobotsNewLoc.length - allyRobots.length;
                    options[index].alliesHealRangedX = allyRobotsHealRangeNewLoc.length - allyRobotsHealRange.length;
                    options[index].potentialEnemiesAttackRangedX = potentialEnemiesAttackRangeNewLoc.length - potentialEnemiesAttackRange.length;
                }
            } else {
                options[index] = new engagementMicroSquare(false);
            }
            index++;
        }
    }


    public static void updateInfo(RobotController rc) throws GameActionException {
        //take in important info around you
        enemyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam().opponent());
        enemyRobotsAttackRange = rc.senseNearbyRobots(GameConstants.ATTACK_RADIUS_SQUARED, rc.getTeam().opponent());
        allyRobots = rc.senseNearbyRobots(GameConstants.VISION_RADIUS_SQUARED, rc.getTeam());
        allyRobotsHealRange = rc.senseNearbyRobots(GameConstants.HEAL_RADIUS_SQUARED, rc.getTeam());
        enemyFlagsPickedUp = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
        potentialEnemiesAttackRange = rc.senseNearbyRobots(10, rc.getTeam().opponent());
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

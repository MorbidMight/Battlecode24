package Version15;

import Version15.Pathfinding;
import Version15.RobotPlayer;
import Version15.Utilities;
import battlecode.common.*;

import java.util.HashSet;

public class DefensiveBuilder {
    static boolean hasGeneratedDefensivePositions = false;
    //array of all the defensive trap positions we want - if a location is found to be impassable, or it doesnt exist, or it doesnt seem favorable - set to null
    static MapLocation[] defensivePositions;
    //keeps track of defensive positions with a trap - when we are an offensive builder, and we die, reset this and then try to build up the positions
    static HashSet<MapLocation> hasTrap = new HashSet<>();
    //stores the trap we are trying to build
    static MapLocation targetTrap;
    static boolean timeToRefresh = false;
    static int turnsRefreshing = 0;

    //rest of file is for defensive builders
    public static void runDefensiveBuilder(RobotController rc) throws GameActionException {
        if(rc.getRoundNum() > 1000){
            RobotPlayer.role = RobotPlayer.roles.offensiveBuilder;
        }
        if(!hasGeneratedDefensivePositions && rc.readSharedArray(0) != 0 && rc.readSharedArray(1) != 0 && rc.readSharedArray(2) != 0) {
            generateDefensivePositions(rc);
            hasGeneratedDefensivePositions = true;
        }
        if(!hasGeneratedDefensivePositions)
            return;
        clearFalseIndices(rc);
        if(rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length != 0){
            if(rc.canMove(rc.getLocation().directionTo(Utilities.averageRobotLocation(rc.senseNearbyRobots(-1, rc.getTeam().opponent()))).opposite())){
                rc.move(rc.getLocation().directionTo(Utilities.averageRobotLocation(rc.senseNearbyRobots(-1, rc.getTeam().opponent()))).opposite());
            }
        }
        if(targetTrap != null){
            if(hasTrap.contains(targetTrap))
                targetTrap = getTargetTrap(rc);
            if(targetTrap != null && tryBuildTargetTrap(rc)) {
                targetTrap = getTargetTrap(rc);
            }
            else {
                if(rc.getLocation() != targetTrap && targetTrap != null) {
                    if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(targetTrap))))
                        rc.fill(rc.getLocation().add(rc.getLocation().directionTo(targetTrap)));
                    Pathfinding.bellmanFord5x5(rc, targetTrap);
                }
                if(targetTrap != null && tryBuildTargetTrap(rc))
                    targetTrap = getTargetTrap(rc);
            }
        }
        else{
            targetTrap = getTargetTrap(rc);
            if(targetTrap != null) {
                if(rc.canFill(rc.getLocation().add(rc.getLocation().directionTo(targetTrap))))
                    rc.fill(rc.getLocation().add(rc.getLocation().directionTo(targetTrap)));
                Pathfinding.bellmanFord5x5(rc, targetTrap);
            }
        }
        if(targetTrap == null){
            hasTrap.clear();
        }
    }
    public static boolean tryBuildTargetTrap(RobotController rc) throws GameActionException {
        if(rc.canBuild(TrapType.STUN, targetTrap)){
            rc.build(TrapType.STUN, targetTrap);
            hasTrap.add(targetTrap);
            return true;
        }
        else{
            return false;
        }
    }

    //could be unrolled
    public static MapLocation getTargetTrap(RobotController rc){
        int closestDistance = Integer.MAX_VALUE;
        int bestIndex = -1;
        for(int i = 0; i < defensivePositions.length; i++){
            if(defensivePositions[i] != null && !hasTrap.contains(defensivePositions[i]) && rc.getLocation().distanceSquaredTo(defensivePositions[i]) < closestDistance){
                bestIndex = i;
                closestDistance = rc.getLocation().distanceSquaredTo(defensivePositions[i]);
            }
        }
        if(bestIndex != -1){
            return defensivePositions[bestIndex];
        }
        else{
            return null;
        }
    }

    //fills the array of every single defensive trap we want to build, while also setting the ones we decide against to empty
    public static void generateDefensivePositions(RobotController rc) throws GameActionException {
        MapLocation flag1 = Utilities.convertIntToLocation(rc.readSharedArray(0));
        MapLocation flag2 = Utilities.convertIntToLocation(rc.readSharedArray(1));
        MapLocation flag3 = Utilities.convertIntToLocation(rc.readSharedArray(2));
        MapLocation centerOfMap = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
        int ind1 = 0;
        int ind2 = 28;
        int ind3 = 56;
        //indicides 0-26 correspond to flag1, 27-53 flag2, 54-80 flag3
        defensivePositions = new MapLocation[84];
        for(int dx = -2; dx <= 2; dx++){
            for(int dy = -2; dy <= 2; dy++){
                if(!((dx % 2 == 0 && dy % 2 == 0) || (Math.abs(dx) == 1 && Math.abs(dy) == 1))) {
                    continue;
                }
                else if(dx == 0 && dy == 0)
                    continue;
                if(modifiedInBounds(rc, flag1.x + dx, flag1.y + dy)){
                    defensivePositions[ind1] = new MapLocation(flag1.x + dx, flag1.y + dy);
                    ind1++;
                }
                else{
                    defensivePositions[ind1] = null;
                    ind1++;
                }
                if(modifiedInBounds(rc, flag2.x + dx, flag2.y + dy)){
                    defensivePositions[ind2] = new MapLocation(flag2.x + dx, flag2.y + dy);
                    ind2++;
                }
                else{
                    defensivePositions[ind2] = null;
                    ind2++;
                }
                if(modifiedInBounds(rc, flag3.x + dx, flag3.y + dy)){
                    defensivePositions[ind3] = new MapLocation(flag3.x + dx, flag3.y + dy);
                    ind3++;
                }
                else{
                    defensivePositions[ind3] = null;
                    ind3++;
                }
            }
        }
    }
    //adds any traps we can see into the hashset, and makes any ones we can see that aren't passable null
    //to do - unroll in future
    public static void clearFalseIndices(RobotController rc) throws GameActionException {
        for(int i = 0; i < defensivePositions.length; i++){
            if(defensivePositions[i] != null && rc.canSenseLocation(defensivePositions[i])){
                MapInfo temp = rc.senseMapInfo(defensivePositions[i]);
                if(temp.getTrapType() != TrapType.NONE){
                    hasTrap.add(defensivePositions[i]);
                    continue;
                }
                if(!temp.isPassable() && !temp.isWater()){
                    defensivePositions[i] = null;
                }
                if(temp.isDam() || temp.getTeamTerritory() != rc.getTeam())
                    defensivePositions[i] = null;
                if(hasTrap.contains(defensivePositions[i]) && temp.getTrapType() == TrapType.NONE){
                    hasTrap.remove(defensivePositions[i]);
                }
            }
        }
    }
    public static boolean inBounds(RobotController rc, int x, int y){
        return x >= 0 && x < rc.getMapWidth() && y >= 0 && y < rc.getMapHeight();
    }
    public static boolean modifiedInBounds(RobotController rc, int x, int y){
        return x > 0 && x < rc.getMapWidth()-1 && y > 0 && y < rc.getMapHeight() - 1;
    }
    public static MapLocation getTargetTrapOneSpawnLocation(RobotController rc) throws GameActionException {
        int closestDistance = Integer.MAX_VALUE;
        int bestIndex = -1;
        int lowBound;
        int upperBound;
        MapLocation flag1 = Utilities.convertIntToLocation(rc.readSharedArray(0));
        MapLocation flag2 = Utilities.convertIntToLocation(rc.readSharedArray(1));
        MapLocation flag3 = Utilities.convertIntToLocation(rc.readSharedArray(2));
        if(rc.getLocation().equals(flag1) || rc.getLocation().isAdjacentTo(flag1)){
            lowBound = 0;
            upperBound = 27;
        }
        else if(rc.getLocation().equals(flag2) || rc.getLocation().isAdjacentTo(flag2)){
            lowBound = 27;
            upperBound = 54;
        }
        else{
            lowBound = 54;
            upperBound = 81;
        }
        for(int i = lowBound; i < upperBound; i++){
            if(defensivePositions[i] != null && !hasTrap.contains(defensivePositions[i]) && rc.getLocation().distanceSquaredTo(defensivePositions[i]) < closestDistance){
                bestIndex = i;
                closestDistance = rc.getLocation().distanceSquaredTo(defensivePositions[i]);
            }
        }
        if(bestIndex != -1){
            return defensivePositions[bestIndex];
        }
        else{
            return null;
        }
    }
    public static void attemptBuild(RobotController rc) throws GameActionException {
        for (MapInfo t : rc.senseNearbyMapInfos(GameConstants.INTERACT_RADIUS_SQUARED)) {
            if (rc.getCrumbs() > 300 && !isTrapAdjacent(rc, t.getMapLocation()) && rc.canBuild(TrapType.STUN, t.getMapLocation())) {
                rc.build(TrapType.STUN, t.getMapLocation());
            }
        }
    }
    public static boolean isTrapAdjacent(RobotController rc, MapLocation location) throws GameActionException {
        MapInfo[] adjacents = rc.senseNearbyMapInfos(location, 2);
        for(MapInfo square : adjacents){
            if(square.getTrapType() != TrapType.NONE)
                return true;
        }
        return false;
    }

}

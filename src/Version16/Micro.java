package Version16;
import battlecode.common.*;
public class Micro {
    static RobotController rc;
    static boolean nearDeath = false;
    static int damage;
    static boolean canAttack;
    public Micro(RobotController rc){
        Micro.rc = rc;
    }
    public boolean doMicro() throws GameActionException {
        if(!rc.isMovementReady()) return false;
        if(rc.getHealth() < Soldier.RETREAT_HEALTH)
            nearDeath = true;
        damage = rc.getAttackDamage();
        canAttack = rc.isActionReady();
        engagementMicroSquare[] options = new engagementMicroSquare[9];
        populateMicroArray(options);
        RobotInfo[] robots = rc.senseNearbyRobots(17, rc.getTeam().opponent());
        for(RobotInfo unit : robots){
            for(engagementMicroSquare o : options){
                if(o.passable) {
                    o.updateEnemy(unit);
                    o.potentialKill = unit.health <= damage;
                }
            }
        }
        robots = rc.senseNearbyRobots(10, rc.getTeam());
        for(RobotInfo unit : robots) {
            for (engagementMicroSquare o : options) {
                if (o.passable)
                    o.updateAlly(unit);
            }
        }
        engagementMicroSquare target = findBest(options);
        //System.out.println(rc.getLocation() + " : " + canAttack + " : " + target.minDistanceToEnemy + " : " + target.enemiesAttackRange + " : " + target.potentialEnemiesAttackRange);
        if(target.passable) {
            Direction temp = rc.getLocation().directionTo(target.location);
            if (rc.canMove(temp)) {
                rc.move(temp);
                return true;
            }
        }
        return false;
    }
    public static engagementMicroSquare findBest(engagementMicroSquare[] options){
        engagementMicroSquare best = options[options.length - 1];
        if(canAttack && !nearDeath) {
            for (int i = 0; i < options.length; i++) {
                if (options[i].isBetterAttack(best)) {
                    best = options[i];
                }
            }
        }
        else{
            for (int i = 0; i < options.length; i++) {
                if (options[i].isBetterKite(best)) {
                    best = options[i];
                }
            }
        }
        return best;
    }

    public static void populateMicroArray(engagementMicroSquare[] options) throws GameActionException {
        int mapWidth = rc.getMapWidth();
        int mapHeight = rc.getMapHeight();
        int curX = rc.getLocation().x;
        int curY = rc.getLocation().y;
        int newX;
        int newY;
        int index = 0;
        newX = curX + -1;
        newY = curY + -1;
        if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
            options[index] = new engagementMicroSquare(newX, newY);
            MapInfo temp = rc.senseMapInfo(new MapLocation(newX, newY));
            if (temp.isPassable() && rc.senseRobotAtLocation(new MapLocation(newX, newY)) == null) {
                options[index].passable = true;
            } else {
                options[index].passable = false;
            }
            if (temp.getTrapType() != TrapType.NONE) {
                options[index].hasTrap = true;
            }
        } else {
            options[index] = new engagementMicroSquare(false);
        }
        index++;
        newX = curX + -1;
        newY = curY + 0;
        if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
            options[index] = new engagementMicroSquare(newX, newY);
            MapInfo temp = rc.senseMapInfo(new MapLocation(newX, newY));
            if (temp.isPassable() && rc.senseRobotAtLocation(new MapLocation(newX, newY)) == null) {
                options[index].passable = true;
            } else {
                options[index].passable = false;
            }
            if (temp.getTrapType() != TrapType.NONE) {
                options[index].hasTrap = true;
            }
        } else {
            options[index] = new engagementMicroSquare(false);
        }
        index++;
        newX = curX + -1;
        newY = curY + 1;
        if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
            options[index] = new engagementMicroSquare(newX, newY);
            MapInfo temp = rc.senseMapInfo(new MapLocation(newX, newY));
            if (temp.isPassable() && rc.senseRobotAtLocation(new MapLocation(newX, newY)) == null) {
                options[index].passable = true;
            } else {
                options[index].passable = false;
            }
            if (temp.getTrapType() != TrapType.NONE) {
                options[index].hasTrap = true;
            }
        } else {
            options[index] = new engagementMicroSquare(false);
        }
        index++;
        newX = curX + 0;
        newY = curY + -1;
        if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
            options[index] = new engagementMicroSquare(newX, newY);
            MapInfo temp = rc.senseMapInfo(new MapLocation(newX, newY));
            if (temp.isPassable() && rc.senseRobotAtLocation(new MapLocation(newX, newY)) == null) {
                options[index].passable = true;
            } else {
                options[index].passable = false;
            }
            if (temp.getTrapType() != TrapType.NONE) {
                options[index].hasTrap = true;
            }
        } else {
            options[index] = new engagementMicroSquare(false);
        }
        index++;
        newX = curX + 0;
        newY = curY + 0;
        if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
            options[index] = new engagementMicroSquare(newX, newY);
            MapInfo temp = rc.senseMapInfo(new MapLocation(newX, newY));
            if (temp.isPassable() && rc.senseRobotAtLocation(new MapLocation(newX, newY)) == null) {
                options[index].passable = true;
            } else {
                options[index].passable = false;
            }
            if (temp.getTrapType() != TrapType.NONE) {
                options[index].hasTrap = true;
            }
        } else {
            options[index] = new engagementMicroSquare(false);
        }
        index++;
        newX = curX + 0;
        newY = curY + 1;
        if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
            options[index] = new engagementMicroSquare(newX, newY);
            MapInfo temp = rc.senseMapInfo(new MapLocation(newX, newY));
            if (temp.isPassable() && rc.senseRobotAtLocation(new MapLocation(newX, newY)) == null) {
                options[index].passable = true;
            } else {
                options[index].passable = false;
            }
            if (temp.getTrapType() != TrapType.NONE) {
                options[index].hasTrap = true;
            }
        } else {
            options[index] = new engagementMicroSquare(false);
        }
        index++;
        newX = curX + 1;
        newY = curY + -1;
        if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
            options[index] = new engagementMicroSquare(newX, newY);
            MapInfo temp = rc.senseMapInfo(new MapLocation(newX, newY));
            if (temp.isPassable() && rc.senseRobotAtLocation(new MapLocation(newX, newY)) == null) {
                options[index].passable = true;
            } else {
                options[index].passable = false;
            }
            if (temp.getTrapType() != TrapType.NONE) {
                options[index].hasTrap = true;
            }
        } else {
            options[index] = new engagementMicroSquare(false);
        }
        index++;
        newX = curX + 1;
        newY = curY + 0;
        if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
            options[index] = new engagementMicroSquare(newX, newY);
            MapInfo temp = rc.senseMapInfo(new MapLocation(newX, newY));
            if (temp.isPassable() && rc.senseRobotAtLocation(new MapLocation(newX, newY)) == null) {
                options[index].passable = true;
            } else {
                options[index].passable = false;
            }
            if (temp.getTrapType() != TrapType.NONE) {
                options[index].hasTrap = true;
            }
        } else {
            options[index] = new engagementMicroSquare(false);
        }
        index++;
        newX = curX + 1;
        newY = curY + 1;
        if (newX < mapWidth && newX >= 0 && newY >= 0 && newY < mapHeight) {
            options[index] = new engagementMicroSquare(newX, newY);
            MapInfo temp = rc.senseMapInfo(new MapLocation(newX, newY));
            if (temp.isPassable() && rc.senseRobotAtLocation(new MapLocation(newX, newY)) == null) {
                options[index].passable = true;
            } else {
                options[index].passable = false;
            }
            if (temp.getTrapType() != TrapType.NONE) {
                options[index].hasTrap = true;
            }
        } else {
            options[index] = new engagementMicroSquare(false);
        }
        index++;
    }

    public static boolean isKillable(RobotController rc, RobotInfo[] enemies){
        int i = rc.getAttackDamage();
        for(RobotInfo enemy : enemies){
            if(i >= enemy.getHealth())
                return true;
        }
        return false;
    }
}


//used to keep track of the eight squares around a robot during engagement, only considers relevant details
//note - passability only takes into account if this robot can move there, so a square can be impassable due
//to either terrain, or just a robot being there, makes no difference to us - false passability could also indicate that
//the location is not on the map
class engagementMicroSquare {
    public MapLocation location;
    public boolean passable;
    public int enemiesAttackRange;
    public int enemiesVision;
    public int alliesVision;
    public int alliesHealRange;
    public int potentialEnemiesAttackRange;
    public int potentialEnemiesPrepareAttack;
    public int potentialAlliesAttackRange;
    public Boolean hasTrap;
    public Boolean potentialKill;
    public int totalHealthEnemiesdX;
    public int totalHealthAlliesdX;
    public int minDistanceToEnemy = Integer.MAX_VALUE;

    public engagementMicroSquare() {

    }

    public engagementMicroSquare(boolean p) {
        passable = p;
        hasTrap = false;
        potentialKill = false;
    }

    public engagementMicroSquare(int x, int y) {
        location = new MapLocation(x, y);
        hasTrap = false;
        potentialKill = false;
    }

    public engagementMicroSquare(boolean passability, int x, int y) {
        passable = passability;
        location = new MapLocation(x, y);
        hasTrap = false;
        potentialKill = false;
    }

    public void updateEnemy(RobotInfo unit) {
        int dist = location.distanceSquaredTo(unit.getLocation());
        if(dist < minDistanceToEnemy) minDistanceToEnemy = dist;
        if (dist <= 17) {
            potentialEnemiesPrepareAttack++;
            if (dist <= 10) {
                potentialEnemiesAttackRange++;
                if (dist <= GameConstants.ATTACK_RADIUS_SQUARED) enemiesAttackRange++;
            }
        }
    }

    public void updateAlly(RobotInfo unit) {
        int dist = location.distanceSquaredTo(unit.getLocation());
        if(dist <= GameConstants.VISION_RADIUS_SQUARED){
            alliesVision++;
            if (dist <= 10) {
                potentialAlliesAttackRange++;
                if (dist <= GameConstants.HEAL_RADIUS_SQUARED) alliesHealRange++;
            }
        }
    }

    public int combatSafe(){
        if(enemiesAttackRange > 2) return -1;
        if(enemiesAttackRange == 2 && !potentialKill) return 0;
        if(enemiesAttackRange == 1 && !potentialKill) return 1;
        if(enemiesAttackRange == 2) return 1;
        if(enemiesAttackRange == 1) return 2;
        return 0;
    }

    public int kiteSafe(){
        if(enemiesAttackRange > 0) return -1;
        if(potentialEnemiesAttackRange > potentialAlliesAttackRange) return 0;
        return 1;
    }

    public boolean nearFight(){
        return minDistanceToEnemy >= 5 && minDistanceToEnemy <= 10;
    }

    public boolean inRange(){
        return enemiesAttackRange >= 0;
    }

    public boolean isBetterKite(engagementMicroSquare other){
        if(location == other.location){
            return false;
        }
        if(!passable) return false;
        if(!other.passable) return true;

        if(kiteSafe() > other.kiteSafe()) return true;
        if(kiteSafe() < other.kiteSafe()) return false;

        if(nearFight() && !other.nearFight()) return true;
        if(!nearFight() && other.nearFight()) return false;

        if(!inRange()) return minDistanceToEnemy <= other.minDistanceToEnemy;
        return potentialEnemiesAttackRange <= other.potentialEnemiesAttackRange;
    }
    public boolean isBetterAttack(engagementMicroSquare other){
        if(location == other.location){
            return false;
        }
        if(!passable) return false;
        if(!other.passable) return true;

        if(combatSafe() > other.combatSafe()) return true;
        if(combatSafe() < other.combatSafe()) return false;

        if(inRange() && !other.inRange()) return true;
        if(!inRange() && other.inRange()) return false;

        if(potentialKill && !other.potentialKill) return true;
        if(!potentialKill && other.potentialKill) return false;

        if(potentialAlliesAttackRange > other.potentialAlliesAttackRange) return true;
        if(potentialAlliesAttackRange < other.potentialAlliesAttackRange) return false;

        if(inRange()) return minDistanceToEnemy >= other.minDistanceToEnemy;
        return minDistanceToEnemy <= other.minDistanceToEnemy;
    }
}
package Version17;
import battlecode.common.*;
import static Version17.RobotPlayer.*;
public class BFSKernel11x11
{
    static Direction[][] lookup;
    static MapLocation closestFlag;
    static int closestDistance;
    static int closestIndex;

    static MapLocation l0;
    static int dist0;
    static int h0;
    static boolean exists0;

    static MapLocation l1;
    static int dist1;
    static int h1;
    static boolean exists1;

    static MapLocation l2;
    static int dist2;
    static int h2;
    static boolean exists2;

    static MapLocation l3;
    static int dist3;
    static int h3;
    static boolean exists3;

    static MapLocation l4;
    static int dist4;
    static int h4;
    static boolean exists4;

    static MapLocation l5;
    static int dist5;
    static int h5;
    static boolean exists5;

    static MapLocation l6;
    static int dist6;
    static int h6;
    static boolean exists6;

    static MapLocation l7;
    static int dist7;
    static int h7;
    static boolean exists7;

    static MapLocation l8;
    static int dist8;
    static int h8;
    static boolean exists8;

    static MapLocation l9;
    static int dist9;
    static int h9;
    static boolean exists9;

    static MapLocation l10;
    static int dist10;
    static int h10;
    static boolean exists10;

    static MapLocation l11;
    static int dist11;
    static int h11;
    static boolean exists11;

    static MapLocation l12;
    static int dist12;
    static int h12;
    static boolean exists12;

    static MapLocation l13;
    static int dist13;
    static int h13;
    static boolean exists13;

    static MapLocation l14;
    static int dist14;
    static int h14;
    static boolean exists14;

    static MapLocation l15;
    static int dist15;
    static int h15;
    static boolean exists15;

    static MapLocation l16;
    static int dist16;
    static int h16;
    static boolean exists16;

    static MapLocation l17;
    static int dist17;
    static int h17;
    static boolean exists17;

    static MapLocation l18;
    static int dist18;
    static int h18;
    static boolean exists18;

    static MapLocation l19;
    static int dist19;
    static int h19;
    static boolean exists19;

    static MapLocation l20;
    static int dist20;
    static int h20;
    static boolean exists20;

    static MapLocation l21;
    static int dist21;
    static int h21;
    static boolean exists21;

    static MapLocation l22;
    static int dist22;
    static int h22;
    static boolean exists22;

    static MapLocation l23;
    static int dist23;
    static int h23;
    static boolean exists23;

    static MapLocation l24;
    static int dist24;
    static int h24;
    static boolean exists24;

    static MapLocation l25;
    static int dist25;
    static int h25;
    static boolean exists25;

    static MapLocation l26;
    static int dist26;
    static int h26;
    static boolean exists26;

    static MapLocation l27;
    static int dist27;
    static int h27;
    static boolean exists27;

    static MapLocation l28;
    static int dist28;
    static int h28;
    static boolean exists28;

    static MapLocation l29;
    static int dist29;
    static int h29;
    static boolean exists29;

    static MapLocation l30;
    static int dist30;
    static int h30;
    static boolean exists30;

    static MapLocation l31;
    static int dist31;
    static int h31;
    static boolean exists31;

    static MapLocation l32;
    static int dist32;
    static int h32;
    static boolean exists32;

    static MapLocation l33;
    static int dist33;
    static int h33;
    static boolean exists33;

    static MapLocation l34;
    static int dist34;
    static int h34;
    static boolean exists34;

    static MapLocation l35;
    static int dist35;
    static int h35;
    static boolean exists35;

    static MapLocation l36;
    static int dist36;
    static int h36;
    static boolean exists36;

    static MapLocation l37;
    static int dist37;
    static int h37;
    static boolean exists37;

    static MapLocation l38;
    static int dist38;
    static int h38;
    static boolean exists38;

    static MapLocation l39;
    static int dist39;
    static int h39;
    static boolean exists39;

    static MapLocation l40;
    static int dist40;
    static int h40;
    static boolean exists40;

    static MapLocation l41;
    static int dist41;
    static int h41;
    static boolean exists41;

    static MapLocation l42;
    static int dist42;
    static int h42;
    static boolean exists42;

    static MapLocation l43;
    static int dist43;
    static int h43;
    static boolean exists43;

    static MapLocation l44;
    static int dist44;
    static int h44;
    static boolean exists44;

    static MapLocation l45;
    static int dist45;
    static int h45;
    static boolean exists45;

    static MapLocation l46;
    static int dist46;
    static int h46;
    static boolean exists46;

    static MapLocation l47;
    static int dist47;
    static int h47;
    static boolean exists47;

    static MapLocation l48;
    static int dist48;
    static int h48;
    static boolean exists48;

    static MapLocation l49;
    static int dist49;
    static int h49;
    static boolean exists49;

    static MapLocation l50;
    static int dist50;
    static int h50;
    static boolean exists50;

    static MapLocation l51;
    static int dist51;
    static int h51;
    static boolean exists51;

    static MapLocation l52;
    static int dist52;
    static int h52;
    static boolean exists52;

    static MapLocation l53;
    static int dist53;
    static int h53;
    static boolean exists53;

    static MapLocation l54;
    static int dist54;
    static int h54;
    static boolean exists54;

    static MapLocation l55;
    static int dist55;
    static int h55;
    static boolean exists55;

    static MapLocation l56;
    static int dist56;
    static int h56;
    static boolean exists56;

    static MapLocation l57;
    static int dist57;
    static int h57;
    static boolean exists57;

    static MapLocation l58;
    static int dist58;
    static int h58;
    static boolean exists58;

    static MapLocation l59;
    static int dist59;
    static int h59;
    static boolean exists59;

    static MapLocation l60;
    static int dist60;
    static int h60;
    static boolean exists60;

    static MapLocation l61;
    static int dist61;
    static int h61;
    static boolean exists61;

    static MapLocation l62;
    static int dist62;
    static int h62;
    static boolean exists62;

    static MapLocation l63;
    static int dist63;
    static int h63;
    static boolean exists63;

    static MapLocation l64;
    static int dist64;
    static int h64;
    static boolean exists64;

    static MapLocation l65;
    static int dist65;
    static int h65;
    static boolean exists65;

    static MapLocation l66;
    static int dist66;
    static int h66;
    static boolean exists66;

    static MapLocation l67;
    static int dist67;
    static int h67;
    static boolean exists67;

    static MapLocation l68;
    static int dist68;
    static int h68;
    static boolean exists68;

    static MapLocation l69;
    static int dist69;
    static int h69;
    static boolean exists69;

    static MapLocation l70;
    static int dist70;
    static int h70;
    static boolean exists70;

    static MapLocation l71;
    static int dist71;
    static int h71;
    static boolean exists71;

    static MapLocation l72;
    static int dist72;
    static int h72;
    static boolean exists72;

    static MapLocation l73;
    static int dist73;
    static int h73;
    static boolean exists73;

    static MapLocation l74;
    static int dist74;
    static int h74;
    static boolean exists74;

    static MapLocation l75;
    static int dist75;
    static int h75;
    static boolean exists75;

    static MapLocation l76;
    static int dist76;
    static int h76;
    static boolean exists76;

    static MapLocation l77;
    static int dist77;
    static int h77;
    static boolean exists77;

    static MapLocation l78;
    static int dist78;
    static int h78;
    static boolean exists78;

    static MapLocation l79;
    static int dist79;
    static int h79;
    static boolean exists79;

    static MapLocation l80;
    static int dist80;
    static int h80;
    static boolean exists80;

    static MapLocation l81;
    static int dist81;
    static int h81;
    static boolean exists81;

    static MapLocation l82;
    static int dist82;
    static int h82;
    static boolean exists82;

    static MapLocation l83;
    static int dist83;
    static int h83;
    static boolean exists83;

    static MapLocation l84;
    static int dist84;
    static int h84;
    static boolean exists84;

    static MapLocation l85;
    static int dist85;
    static int h85;
    static boolean exists85;

    static MapLocation l86;
    static int dist86;
    static int h86;
    static boolean exists86;

    static MapLocation l87;
    static int dist87;
    static int h87;
    static boolean exists87;

    static MapLocation l88;
    static int dist88;
    static int h88;
    static boolean exists88;

    static MapLocation l89;
    static int dist89;
    static int h89;
    static boolean exists89;

    static MapLocation l90;
    static int dist90;
    static int h90;
    static boolean exists90;

    static MapLocation l91;
    static int dist91;
    static int h91;
    static boolean exists91;

    static MapLocation l92;
    static int dist92;
    static int h92;
    static boolean exists92;

    static MapLocation l93;
    static int dist93;
    static int h93;
    static boolean exists93;

    static MapLocation l94;
    static int dist94;
    static int h94;
    static boolean exists94;

    static MapLocation l95;
    static int dist95;
    static int h95;
    static boolean exists95;

    static MapLocation l96;
    static int dist96;
    static int h96;
    static boolean exists96;

    static MapLocation l97;
    static int dist97;
    static int h97;
    static boolean exists97;

    static MapLocation l98;
    static int dist98;
    static int h98;
    static boolean exists98;

    static MapLocation l99;
    static int dist99;
    static int h99;
    static boolean exists99;

    static MapLocation l100;
    static int dist100;
    static int h100;
    static boolean exists100;

    static MapLocation l101;
    static int dist101;
    static int h101;
    static boolean exists101;

    static MapLocation l102;
    static int dist102;
    static int h102;
    static boolean exists102;

    static MapLocation l103;
    static int dist103;
    static int h103;
    static boolean exists103;

    static MapLocation l104;
    static int dist104;
    static int h104;
    static boolean exists104;

    static MapLocation l105;
    static int dist105;
    static int h105;
    static boolean exists105;

    static MapLocation l106;
    static int dist106;
    static int h106;
    static boolean exists106;

    static MapLocation l107;
    static int dist107;
    static int h107;
    static boolean exists107;

    static MapLocation l108;
    static int dist108;
    static int h108;
    static boolean exists108;

    static MapLocation l109;
    static int dist109;
    static int h109;
    static boolean exists109;

    static MapLocation l110;
    static int dist110;
    static int h110;
    static boolean exists110;

    static MapLocation l111;
    static int dist111;
    static int h111;
    static boolean exists111;

    static MapLocation l112;
    static int dist112;
    static int h112;
    static boolean exists112;

    static MapLocation l113;
    static int dist113;
    static int h113;
    static boolean exists113;

    static MapLocation l114;
    static int dist114;
    static int h114;
    static boolean exists114;

    static MapLocation l115;
    static int dist115;
    static int h115;
    static boolean exists115;

    static MapLocation l116;
    static int dist116;
    static int h116;
    static boolean exists116;

    static MapLocation l117;
    static int dist117;
    static int h117;
    static boolean exists117;

    static MapLocation l118;
    static int dist118;
    static int h118;
    static boolean exists118;

    static MapLocation l119;
    static int dist119;
    static int h119;
    static boolean exists119;

    static MapLocation l120;
    static int dist120;
    static int h120;
    static boolean exists120;

    public static MapLocation getLocationFromIndex(int nodeIndex, MapLocation center)
    {
        MapLocation corner = new MapLocation(center.x - 5, center.y - 5);
        return new MapLocation(corner.x + nodeIndex % 11, corner.y + 10 - nodeIndex / 11);
    }

    public static void setClosestFlag(RobotController rc) throws GameActionException
    {
        FlagInfo[] flags = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
        for(int i = 0; i < flags.length; i++)
        {
            if(flags[i].isPickedUp())
            {
                closestFlag = flags[i].getLocation();
            }
        }
    }

    public static String printDirectionMatrix(Direction[][] matrix)
    {
        String tempStr = "";
        for(int i = 0; i < matrix.length; i++)
        {
            tempStr += "\n";
            for(int j = 0; j < matrix[i].length; j++)
            {
                tempStr += matrix[i][j] + " ";
            }
        }
        return tempStr;
    }

    public static int indexToLocalX(int nodeIndex)
    {
        return nodeIndex % 11;
    }

    public static int indexToLocalY(int nodeIndex)
    {
        return nodeIndex / 11;
    }

    public static Direction dirToMove(int nodeIndex)
    {
        int currentNodeIndex = nodeIndex;
        Direction currentDirection = lookup[indexToLocalY(nodeIndex)][indexToLocalX(nodeIndex)];
        while (currentDirection != null)
        {
            Direction tempDirection = currentDirection;
            switch(currentDirection)
            {
                case NORTH:
                    currentNodeIndex = currentNodeIndex - 11;
                    break;
                case NORTHEAST:
                    currentNodeIndex = currentNodeIndex - 10;
                    break;
                case EAST:
                    currentNodeIndex = currentNodeIndex + 1;
                    break;
                case SOUTHEAST:
                    currentNodeIndex = currentNodeIndex + 12;
                    break;
                case SOUTH:
                    currentNodeIndex = currentNodeIndex + 11;
                    break;
                case SOUTHWEST:
                    currentNodeIndex = currentNodeIndex + 10;
                    break;
                case WEST:
                    currentNodeIndex = currentNodeIndex - 1;
                    break;
                case NORTHWEST:
                    currentNodeIndex = currentNodeIndex - 12;
                    break;

            }
            currentDirection = lookup[currentNodeIndex / 11][currentNodeIndex % 11];
            if(currentDirection == null) return tempDirection.opposite();
        }
        return null;
    }

    public static void BFS(RobotController rc, MapLocation destination) throws GameActionException
    {
        MapLocation start = rc.getLocation();
        lookup = new Direction[11][11];
        closestDistance = Integer.MAX_VALUE;
        closestIndex = -1;

        l0 = getLocationFromIndex(0, start);
        dist0 = 1000000;
        h0 = l0.distanceSquaredTo(destination);
        exists0 = rc.onTheMap(l0) && seenLocations[l0.y][l0.x].isPassable() && !rc.canSenseRobotAtLocation(l0);

        l1 = getLocationFromIndex(1, start);
        dist1 = 1000000;
        h1 = l1.distanceSquaredTo(destination);
        exists1 = rc.onTheMap(l1) && seenLocations[l1.y][l1.x].isPassable() && !rc.canSenseRobotAtLocation(l1);

        l2 = getLocationFromIndex(2, start);
        dist2 = 1000000;
        h2 = l2.distanceSquaredTo(destination);
        exists2 = rc.onTheMap(l2) && seenLocations[l2.y][l2.x].isPassable() && !rc.canSenseRobotAtLocation(l2);

        l3 = getLocationFromIndex(3, start);
        dist3 = 1000000;
        h3 = l3.distanceSquaredTo(destination);
        exists3 = rc.onTheMap(l3) && seenLocations[l3.y][l3.x].isPassable() && !rc.canSenseRobotAtLocation(l3);

        l4 = getLocationFromIndex(4, start);
        dist4 = 1000000;
        h4 = l4.distanceSquaredTo(destination);
        exists4 = rc.onTheMap(l4) && seenLocations[l4.y][l4.x].isPassable() && !rc.canSenseRobotAtLocation(l4);

        l5 = getLocationFromIndex(5, start);
        dist5 = 1000000;
        h5 = l5.distanceSquaredTo(destination);
        exists5 = rc.onTheMap(l5) && seenLocations[l5.y][l5.x].isPassable() && !rc.canSenseRobotAtLocation(l5);

        l6 = getLocationFromIndex(6, start);
        dist6 = 1000000;
        h6 = l6.distanceSquaredTo(destination);
        exists6 = rc.onTheMap(l6) && seenLocations[l6.y][l6.x].isPassable() && !rc.canSenseRobotAtLocation(l6);

        l7 = getLocationFromIndex(7, start);
        dist7 = 1000000;
        h7 = l7.distanceSquaredTo(destination);
        exists7 = rc.onTheMap(l7) && seenLocations[l7.y][l7.x].isPassable() && !rc.canSenseRobotAtLocation(l7);

        l8 = getLocationFromIndex(8, start);
        dist8 = 1000000;
        h8 = l8.distanceSquaredTo(destination);
        exists8 = rc.onTheMap(l8) && seenLocations[l8.y][l8.x].isPassable() && !rc.canSenseRobotAtLocation(l8);

        l9 = getLocationFromIndex(9, start);
        dist9 = 1000000;
        h9 = l9.distanceSquaredTo(destination);
        exists9 = rc.onTheMap(l9) && seenLocations[l9.y][l9.x].isPassable() && !rc.canSenseRobotAtLocation(l9);

        l10 = getLocationFromIndex(10, start);
        dist10 = 1000000;
        h10 = l10.distanceSquaredTo(destination);
        exists10 = rc.onTheMap(l10) && seenLocations[l10.y][l10.x].isPassable() && !rc.canSenseRobotAtLocation(l10);

        l11 = getLocationFromIndex(11, start);
        dist11 = 1000000;
        h11 = l11.distanceSquaredTo(destination);
        exists11 = rc.onTheMap(l11) && seenLocations[l11.y][l11.x].isPassable() && !rc.canSenseRobotAtLocation(l11);

        l12 = getLocationFromIndex(12, start);
        dist12 = 1000000;
        h12 = l12.distanceSquaredTo(destination);
        exists12 = rc.onTheMap(l12) && seenLocations[l12.y][l12.x].isPassable() && !rc.canSenseRobotAtLocation(l12);

        l13 = getLocationFromIndex(13, start);
        dist13 = 1000000;
        h13 = l13.distanceSquaredTo(destination);
        exists13 = rc.onTheMap(l13) && seenLocations[l13.y][l13.x].isPassable() && !rc.canSenseRobotAtLocation(l13);

        l14 = getLocationFromIndex(14, start);
        dist14 = 1000000;
        h14 = l14.distanceSquaredTo(destination);
        exists14 = rc.onTheMap(l14) && seenLocations[l14.y][l14.x].isPassable() && !rc.canSenseRobotAtLocation(l14);

        l15 = getLocationFromIndex(15, start);
        dist15 = 1000000;
        h15 = l15.distanceSquaredTo(destination);
        exists15 = rc.onTheMap(l15) && seenLocations[l15.y][l15.x].isPassable() && !rc.canSenseRobotAtLocation(l15);

        l16 = getLocationFromIndex(16, start);
        dist16 = 1000000;
        h16 = l16.distanceSquaredTo(destination);
        exists16 = rc.onTheMap(l16) && seenLocations[l16.y][l16.x].isPassable() && !rc.canSenseRobotAtLocation(l16);

        l17 = getLocationFromIndex(17, start);
        dist17 = 1000000;
        h17 = l17.distanceSquaredTo(destination);
        exists17 = rc.onTheMap(l17) && seenLocations[l17.y][l17.x].isPassable() && !rc.canSenseRobotAtLocation(l17);

        l18 = getLocationFromIndex(18, start);
        dist18 = 1000000;
        h18 = l18.distanceSquaredTo(destination);
        exists18 = rc.onTheMap(l18) && seenLocations[l18.y][l18.x].isPassable() && !rc.canSenseRobotAtLocation(l18);

        l19 = getLocationFromIndex(19, start);
        dist19 = 1000000;
        h19 = l19.distanceSquaredTo(destination);
        exists19 = rc.onTheMap(l19) && seenLocations[l19.y][l19.x].isPassable() && !rc.canSenseRobotAtLocation(l19);

        l20 = getLocationFromIndex(20, start);
        dist20 = 1000000;
        h20 = l20.distanceSquaredTo(destination);
        exists20 = rc.onTheMap(l20) && seenLocations[l20.y][l20.x].isPassable() && !rc.canSenseRobotAtLocation(l20);

        l21 = getLocationFromIndex(21, start);
        dist21 = 1000000;
        h21 = l21.distanceSquaredTo(destination);
        exists21 = rc.onTheMap(l21) && seenLocations[l21.y][l21.x].isPassable() && !rc.canSenseRobotAtLocation(l21);

        l22 = getLocationFromIndex(22, start);
        dist22 = 1000000;
        h22 = l22.distanceSquaredTo(destination);
        exists22 = rc.onTheMap(l22) && seenLocations[l22.y][l22.x].isPassable() && !rc.canSenseRobotAtLocation(l22);

        l23 = getLocationFromIndex(23, start);
        dist23 = 1000000;
        h23 = l23.distanceSquaredTo(destination);
        exists23 = rc.onTheMap(l23) && seenLocations[l23.y][l23.x].isPassable() && !rc.canSenseRobotAtLocation(l23);

        l24 = getLocationFromIndex(24, start);
        dist24 = 1000000;
        h24 = l24.distanceSquaredTo(destination);
        exists24 = rc.onTheMap(l24) && seenLocations[l24.y][l24.x].isPassable() && !rc.canSenseRobotAtLocation(l24);

        l25 = getLocationFromIndex(25, start);
        dist25 = 1000000;
        h25 = l25.distanceSquaredTo(destination);
        exists25 = rc.onTheMap(l25) && seenLocations[l25.y][l25.x].isPassable() && !rc.canSenseRobotAtLocation(l25);

        l26 = getLocationFromIndex(26, start);
        dist26 = 1000000;
        h26 = l26.distanceSquaredTo(destination);
        exists26 = rc.onTheMap(l26) && seenLocations[l26.y][l26.x].isPassable() && !rc.canSenseRobotAtLocation(l26);

        l27 = getLocationFromIndex(27, start);
        dist27 = 1000000;
        h27 = l27.distanceSquaredTo(destination);
        exists27 = rc.onTheMap(l27) && seenLocations[l27.y][l27.x].isPassable() && !rc.canSenseRobotAtLocation(l27);

        l28 = getLocationFromIndex(28, start);
        dist28 = 1000000;
        h28 = l28.distanceSquaredTo(destination);
        exists28 = rc.onTheMap(l28) && seenLocations[l28.y][l28.x].isPassable() && !rc.canSenseRobotAtLocation(l28);

        l29 = getLocationFromIndex(29, start);
        dist29 = 1000000;
        h29 = l29.distanceSquaredTo(destination);
        exists29 = rc.onTheMap(l29) && seenLocations[l29.y][l29.x].isPassable() && !rc.canSenseRobotAtLocation(l29);

        l30 = getLocationFromIndex(30, start);
        dist30 = 1000000;
        h30 = l30.distanceSquaredTo(destination);
        exists30 = rc.onTheMap(l30) && seenLocations[l30.y][l30.x].isPassable() && !rc.canSenseRobotAtLocation(l30);

        l31 = getLocationFromIndex(31, start);
        dist31 = 1000000;
        h31 = l31.distanceSquaredTo(destination);
        exists31 = rc.onTheMap(l31) && seenLocations[l31.y][l31.x].isPassable() && !rc.canSenseRobotAtLocation(l31);

        l32 = getLocationFromIndex(32, start);
        dist32 = 1000000;
        h32 = l32.distanceSquaredTo(destination);
        exists32 = rc.onTheMap(l32) && seenLocations[l32.y][l32.x].isPassable() && !rc.canSenseRobotAtLocation(l32);

        l33 = getLocationFromIndex(33, start);
        dist33 = 1000000;
        h33 = l33.distanceSquaredTo(destination);
        exists33 = rc.onTheMap(l33) && seenLocations[l33.y][l33.x].isPassable() && !rc.canSenseRobotAtLocation(l33);

        l34 = getLocationFromIndex(34, start);
        dist34 = 1000000;
        h34 = l34.distanceSquaredTo(destination);
        exists34 = rc.onTheMap(l34) && seenLocations[l34.y][l34.x].isPassable() && !rc.canSenseRobotAtLocation(l34);

        l35 = getLocationFromIndex(35, start);
        dist35 = 1000000;
        h35 = l35.distanceSquaredTo(destination);
        exists35 = rc.onTheMap(l35) && seenLocations[l35.y][l35.x].isPassable() && !rc.canSenseRobotAtLocation(l35);

        l36 = getLocationFromIndex(36, start);
        dist36 = 1000000;
        h36 = l36.distanceSquaredTo(destination);
        exists36 = rc.onTheMap(l36) && seenLocations[l36.y][l36.x].isPassable() && !rc.canSenseRobotAtLocation(l36);

        l37 = getLocationFromIndex(37, start);
        dist37 = 1000000;
        h37 = l37.distanceSquaredTo(destination);
        exists37 = rc.onTheMap(l37) && seenLocations[l37.y][l37.x].isPassable() && !rc.canSenseRobotAtLocation(l37);

        l38 = getLocationFromIndex(38, start);
        dist38 = 1000000;
        h38 = l38.distanceSquaredTo(destination);
        exists38 = rc.onTheMap(l38) && seenLocations[l38.y][l38.x].isPassable() && !rc.canSenseRobotAtLocation(l38);

        l39 = getLocationFromIndex(39, start);
        dist39 = 1000000;
        h39 = l39.distanceSquaredTo(destination);
        exists39 = rc.onTheMap(l39) && seenLocations[l39.y][l39.x].isPassable() && !rc.canSenseRobotAtLocation(l39);

        l40 = getLocationFromIndex(40, start);
        dist40 = 1000000;
        h40 = l40.distanceSquaredTo(destination);
        exists40 = rc.onTheMap(l40) && seenLocations[l40.y][l40.x].isPassable() && !rc.canSenseRobotAtLocation(l40);

        l41 = getLocationFromIndex(41, start);
        dist41 = 1000000;
        h41 = l41.distanceSquaredTo(destination);
        exists41 = rc.onTheMap(l41) && seenLocations[l41.y][l41.x].isPassable() && !rc.canSenseRobotAtLocation(l41);

        l42 = getLocationFromIndex(42, start);
        dist42 = 1000000;
        h42 = l42.distanceSquaredTo(destination);
        exists42 = rc.onTheMap(l42) && seenLocations[l42.y][l42.x].isPassable() && !rc.canSenseRobotAtLocation(l42);

        l43 = getLocationFromIndex(43, start);
        dist43 = 1000000;
        h43 = l43.distanceSquaredTo(destination);
        exists43 = rc.onTheMap(l43) && seenLocations[l43.y][l43.x].isPassable() && !rc.canSenseRobotAtLocation(l43);

        l44 = getLocationFromIndex(44, start);
        dist44 = 1000000;
        h44 = l44.distanceSquaredTo(destination);
        exists44 = rc.onTheMap(l44) && seenLocations[l44.y][l44.x].isPassable() && !rc.canSenseRobotAtLocation(l44);

        l45 = getLocationFromIndex(45, start);
        dist45 = 1000000;
        h45 = l45.distanceSquaredTo(destination);
        exists45 = rc.onTheMap(l45) && seenLocations[l45.y][l45.x].isPassable() && !rc.canSenseRobotAtLocation(l45);

        l46 = getLocationFromIndex(46, start);
        dist46 = 1000000;
        h46 = l46.distanceSquaredTo(destination);
        exists46 = rc.onTheMap(l46) && seenLocations[l46.y][l46.x].isPassable() && !rc.canSenseRobotAtLocation(l46);

        l47 = getLocationFromIndex(47, start);
        dist47 = 1000000;
        h47 = l47.distanceSquaredTo(destination);
        exists47 = rc.onTheMap(l47) && seenLocations[l47.y][l47.x].isPassable() && !rc.canSenseRobotAtLocation(l47);

        l48 = getLocationFromIndex(48, start);
        dist48 = 1000000;
        h48 = l48.distanceSquaredTo(destination);
        exists48 = rc.onTheMap(l48) && seenLocations[l48.y][l48.x].isPassable() && !rc.canSenseRobotAtLocation(l48);

        l49 = getLocationFromIndex(49, start);
        dist49 = 1000000;
        h49 = l49.distanceSquaredTo(destination);
        exists49 = rc.onTheMap(l49) && seenLocations[l49.y][l49.x].isPassable() && !rc.canSenseRobotAtLocation(l49);

        l50 = getLocationFromIndex(50, start);
        dist50 = 1000000;
        h50 = l50.distanceSquaredTo(destination);
        exists50 = rc.onTheMap(l50) && seenLocations[l50.y][l50.x].isPassable() && !rc.canSenseRobotAtLocation(l50);

        l51 = getLocationFromIndex(51, start);
        dist51 = 1000000;
        h51 = l51.distanceSquaredTo(destination);
        exists51 = rc.onTheMap(l51) && seenLocations[l51.y][l51.x].isPassable() && !rc.canSenseRobotAtLocation(l51);

        l52 = getLocationFromIndex(52, start);
        dist52 = 1000000;
        h52 = l52.distanceSquaredTo(destination);
        exists52 = rc.onTheMap(l52) && seenLocations[l52.y][l52.x].isPassable() && !rc.canSenseRobotAtLocation(l52);

        l53 = getLocationFromIndex(53, start);
        dist53 = 1000000;
        h53 = l53.distanceSquaredTo(destination);
        exists53 = rc.onTheMap(l53) && seenLocations[l53.y][l53.x].isPassable() && !rc.canSenseRobotAtLocation(l53);

        l54 = getLocationFromIndex(54, start);
        dist54 = 1000000;
        h54 = l54.distanceSquaredTo(destination);
        exists54 = rc.onTheMap(l54) && seenLocations[l54.y][l54.x].isPassable() && !rc.canSenseRobotAtLocation(l54);

        l55 = getLocationFromIndex(55, start);
        dist55 = 1000000;
        h55 = l55.distanceSquaredTo(destination);
        exists55 = rc.onTheMap(l55) && seenLocations[l55.y][l55.x].isPassable() && !rc.canSenseRobotAtLocation(l55);

        l56 = getLocationFromIndex(56, start);
        dist56 = 1000000;
        h56 = l56.distanceSquaredTo(destination);
        exists56 = rc.onTheMap(l56) && seenLocations[l56.y][l56.x].isPassable() && !rc.canSenseRobotAtLocation(l56);

        l57 = getLocationFromIndex(57, start);
        dist57 = 1000000;
        h57 = l57.distanceSquaredTo(destination);
        exists57 = rc.onTheMap(l57) && seenLocations[l57.y][l57.x].isPassable() && !rc.canSenseRobotAtLocation(l57);

        l58 = getLocationFromIndex(58, start);
        dist58 = 1000000;
        h58 = l58.distanceSquaredTo(destination);
        exists58 = rc.onTheMap(l58) && seenLocations[l58.y][l58.x].isPassable() && !rc.canSenseRobotAtLocation(l58);

        l59 = getLocationFromIndex(59, start);
        dist59 = 1000000;
        h59 = l59.distanceSquaredTo(destination);
        exists59 = rc.onTheMap(l59) && seenLocations[l59.y][l59.x].isPassable() && !rc.canSenseRobotAtLocation(l59);

        l60 = getLocationFromIndex(60, start);
        dist60 = 0;
        h60 = start.distanceSquaredTo(destination);
        exists60 = true;

        l61 = getLocationFromIndex(61, start);
        dist61 = 1000000;
        h61 = l61.distanceSquaredTo(destination);
        exists61 = rc.onTheMap(l61) && seenLocations[l61.y][l61.x].isPassable() && !rc.canSenseRobotAtLocation(l61);

        l62 = getLocationFromIndex(62, start);
        dist62 = 1000000;
        h62 = l62.distanceSquaredTo(destination);
        exists62 = rc.onTheMap(l62) && seenLocations[l62.y][l62.x].isPassable() && !rc.canSenseRobotAtLocation(l62);

        l63 = getLocationFromIndex(63, start);
        dist63 = 1000000;
        h63 = l63.distanceSquaredTo(destination);
        exists63 = rc.onTheMap(l63) && seenLocations[l63.y][l63.x].isPassable() && !rc.canSenseRobotAtLocation(l63);

        l64 = getLocationFromIndex(64, start);
        dist64 = 1000000;
        h64 = l64.distanceSquaredTo(destination);
        exists64 = rc.onTheMap(l64) && seenLocations[l64.y][l64.x].isPassable() && !rc.canSenseRobotAtLocation(l64);

        l65 = getLocationFromIndex(65, start);
        dist65 = 1000000;
        h65 = l65.distanceSquaredTo(destination);
        exists65 = rc.onTheMap(l65) && seenLocations[l65.y][l65.x].isPassable() && !rc.canSenseRobotAtLocation(l65);

        l66 = getLocationFromIndex(66, start);
        dist66 = 1000000;
        h66 = l66.distanceSquaredTo(destination);
        exists66 = rc.onTheMap(l66) && seenLocations[l66.y][l66.x].isPassable() && !rc.canSenseRobotAtLocation(l66);

        l67 = getLocationFromIndex(67, start);
        dist67 = 1000000;
        h67 = l67.distanceSquaredTo(destination);
        exists67 = rc.onTheMap(l67) && seenLocations[l67.y][l67.x].isPassable() && !rc.canSenseRobotAtLocation(l67);

        l68 = getLocationFromIndex(68, start);
        dist68 = 1000000;
        h68 = l68.distanceSquaredTo(destination);
        exists68 = rc.onTheMap(l68) && seenLocations[l68.y][l68.x].isPassable() && !rc.canSenseRobotAtLocation(l68);

        l69 = getLocationFromIndex(69, start);
        dist69 = 1000000;
        h69 = l69.distanceSquaredTo(destination);
        exists69 = rc.onTheMap(l69) && seenLocations[l69.y][l69.x].isPassable() && !rc.canSenseRobotAtLocation(l69);

        l70 = getLocationFromIndex(70, start);
        dist70 = 1000000;
        h70 = l70.distanceSquaredTo(destination);
        exists70 = rc.onTheMap(l70) && seenLocations[l70.y][l70.x].isPassable() && !rc.canSenseRobotAtLocation(l70);

        l71 = getLocationFromIndex(71, start);
        dist71 = 1000000;
        h71 = l71.distanceSquaredTo(destination);
        exists71 = rc.onTheMap(l71) && seenLocations[l71.y][l71.x].isPassable() && !rc.canSenseRobotAtLocation(l71);

        l72 = getLocationFromIndex(72, start);
        dist72 = 1000000;
        h72 = l72.distanceSquaredTo(destination);
        exists72 = rc.onTheMap(l72) && seenLocations[l72.y][l72.x].isPassable() && !rc.canSenseRobotAtLocation(l72);

        l73 = getLocationFromIndex(73, start);
        dist73 = 1000000;
        h73 = l73.distanceSquaredTo(destination);
        exists73 = rc.onTheMap(l73) && seenLocations[l73.y][l73.x].isPassable() && !rc.canSenseRobotAtLocation(l73);

        l74 = getLocationFromIndex(74, start);
        dist74 = 1000000;
        h74 = l74.distanceSquaredTo(destination);
        exists74 = rc.onTheMap(l74) && seenLocations[l74.y][l74.x].isPassable() && !rc.canSenseRobotAtLocation(l74);

        l75 = getLocationFromIndex(75, start);
        dist75 = 1000000;
        h75 = l75.distanceSquaredTo(destination);
        exists75 = rc.onTheMap(l75) && seenLocations[l75.y][l75.x].isPassable() && !rc.canSenseRobotAtLocation(l75);

        l76 = getLocationFromIndex(76, start);
        dist76 = 1000000;
        h76 = l76.distanceSquaredTo(destination);
        exists76 = rc.onTheMap(l76) && seenLocations[l76.y][l76.x].isPassable() && !rc.canSenseRobotAtLocation(l76);

        l77 = getLocationFromIndex(77, start);
        dist77 = 1000000;
        h77 = l77.distanceSquaredTo(destination);
        exists77 = rc.onTheMap(l77) && seenLocations[l77.y][l77.x].isPassable() && !rc.canSenseRobotAtLocation(l77);

        l78 = getLocationFromIndex(78, start);
        dist78 = 1000000;
        h78 = l78.distanceSquaredTo(destination);
        exists78 = rc.onTheMap(l78) && seenLocations[l78.y][l78.x].isPassable() && !rc.canSenseRobotAtLocation(l78);

        l79 = getLocationFromIndex(79, start);
        dist79 = 1000000;
        h79 = l79.distanceSquaredTo(destination);
        exists79 = rc.onTheMap(l79) && seenLocations[l79.y][l79.x].isPassable() && !rc.canSenseRobotAtLocation(l79);

        l80 = getLocationFromIndex(80, start);
        dist80 = 1000000;
        h80 = l80.distanceSquaredTo(destination);
        exists80 = rc.onTheMap(l80) && seenLocations[l80.y][l80.x].isPassable() && !rc.canSenseRobotAtLocation(l80);

        l81 = getLocationFromIndex(81, start);
        dist81 = 1000000;
        h81 = l81.distanceSquaredTo(destination);
        exists81 = rc.onTheMap(l81) && seenLocations[l81.y][l81.x].isPassable() && !rc.canSenseRobotAtLocation(l81);

        l82 = getLocationFromIndex(82, start);
        dist82 = 1000000;
        h82 = l82.distanceSquaredTo(destination);
        exists82 = rc.onTheMap(l82) && seenLocations[l82.y][l82.x].isPassable() && !rc.canSenseRobotAtLocation(l82);

        l83 = getLocationFromIndex(83, start);
        dist83 = 1000000;
        h83 = l83.distanceSquaredTo(destination);
        exists83 = rc.onTheMap(l83) && seenLocations[l83.y][l83.x].isPassable() && !rc.canSenseRobotAtLocation(l83);

        l84 = getLocationFromIndex(84, start);
        dist84 = 1000000;
        h84 = l84.distanceSquaredTo(destination);
        exists84 = rc.onTheMap(l84) && seenLocations[l84.y][l84.x].isPassable() && !rc.canSenseRobotAtLocation(l84);

        l85 = getLocationFromIndex(85, start);
        dist85 = 1000000;
        h85 = l85.distanceSquaredTo(destination);
        exists85 = rc.onTheMap(l85) && seenLocations[l85.y][l85.x].isPassable() && !rc.canSenseRobotAtLocation(l85);

        l86 = getLocationFromIndex(86, start);
        dist86 = 1000000;
        h86 = l86.distanceSquaredTo(destination);
        exists86 = rc.onTheMap(l86) && seenLocations[l86.y][l86.x].isPassable() && !rc.canSenseRobotAtLocation(l86);

        l87 = getLocationFromIndex(87, start);
        dist87 = 1000000;
        h87 = l87.distanceSquaredTo(destination);
        exists87 = rc.onTheMap(l87) && seenLocations[l87.y][l87.x].isPassable() && !rc.canSenseRobotAtLocation(l87);

        l88 = getLocationFromIndex(88, start);
        dist88 = 1000000;
        h88 = l88.distanceSquaredTo(destination);
        exists88 = rc.onTheMap(l88) && seenLocations[l88.y][l88.x].isPassable() && !rc.canSenseRobotAtLocation(l88);

        l89 = getLocationFromIndex(89, start);
        dist89 = 1000000;
        h89 = l89.distanceSquaredTo(destination);
        exists89 = rc.onTheMap(l89) && seenLocations[l89.y][l89.x].isPassable() && !rc.canSenseRobotAtLocation(l89);

        l90 = getLocationFromIndex(90, start);
        dist90 = 1000000;
        h90 = l90.distanceSquaredTo(destination);
        exists90 = rc.onTheMap(l90) && seenLocations[l90.y][l90.x].isPassable() && !rc.canSenseRobotAtLocation(l90);

        l91 = getLocationFromIndex(91, start);
        dist91 = 1000000;
        h91 = l91.distanceSquaredTo(destination);
        exists91 = rc.onTheMap(l91) && seenLocations[l91.y][l91.x].isPassable() && !rc.canSenseRobotAtLocation(l91);

        l92 = getLocationFromIndex(92, start);
        dist92 = 1000000;
        h92 = l92.distanceSquaredTo(destination);
        exists92 = rc.onTheMap(l92) && seenLocations[l92.y][l92.x].isPassable() && !rc.canSenseRobotAtLocation(l92);

        l93 = getLocationFromIndex(93, start);
        dist93 = 1000000;
        h93 = l93.distanceSquaredTo(destination);
        exists93 = rc.onTheMap(l93) && seenLocations[l93.y][l93.x].isPassable() && !rc.canSenseRobotAtLocation(l93);

        l94 = getLocationFromIndex(94, start);
        dist94 = 1000000;
        h94 = l94.distanceSquaredTo(destination);
        exists94 = rc.onTheMap(l94) && seenLocations[l94.y][l94.x].isPassable() && !rc.canSenseRobotAtLocation(l94);

        l95 = getLocationFromIndex(95, start);
        dist95 = 1000000;
        h95 = l95.distanceSquaredTo(destination);
        exists95 = rc.onTheMap(l95) && seenLocations[l95.y][l95.x].isPassable() && !rc.canSenseRobotAtLocation(l95);

        l96 = getLocationFromIndex(96, start);
        dist96 = 1000000;
        h96 = l96.distanceSquaredTo(destination);
        exists96 = rc.onTheMap(l96) && seenLocations[l96.y][l96.x].isPassable() && !rc.canSenseRobotAtLocation(l96);

        l97 = getLocationFromIndex(97, start);
        dist97 = 1000000;
        h97 = l97.distanceSquaredTo(destination);
        exists97 = rc.onTheMap(l97) && seenLocations[l97.y][l97.x].isPassable() && !rc.canSenseRobotAtLocation(l97);

        l98 = getLocationFromIndex(98, start);
        dist98 = 1000000;
        h98 = l98.distanceSquaredTo(destination);
        exists98 = rc.onTheMap(l98) && seenLocations[l98.y][l98.x].isPassable() && !rc.canSenseRobotAtLocation(l98);

        l99 = getLocationFromIndex(99, start);
        dist99 = 1000000;
        h99 = l99.distanceSquaredTo(destination);
        exists99 = rc.onTheMap(l99) && seenLocations[l99.y][l99.x].isPassable() && !rc.canSenseRobotAtLocation(l99);

        l100 = getLocationFromIndex(100, start);
        dist100 = 1000000;
        h100 = l100.distanceSquaredTo(destination);
        exists100 = rc.onTheMap(l100) && seenLocations[l100.y][l100.x].isPassable() && !rc.canSenseRobotAtLocation(l100);

        l101 = getLocationFromIndex(101, start);
        dist101 = 1000000;
        h101 = l101.distanceSquaredTo(destination);
        exists101 = rc.onTheMap(l101) && seenLocations[l101.y][l101.x].isPassable() && !rc.canSenseRobotAtLocation(l101);

        l102 = getLocationFromIndex(102, start);
        dist102 = 1000000;
        h102 = l102.distanceSquaredTo(destination);
        exists102 = rc.onTheMap(l102) && seenLocations[l102.y][l102.x].isPassable() && !rc.canSenseRobotAtLocation(l102);

        l103 = getLocationFromIndex(103, start);
        dist103 = 1000000;
        h103 = l103.distanceSquaredTo(destination);
        exists103 = rc.onTheMap(l103) && seenLocations[l103.y][l103.x].isPassable() && !rc.canSenseRobotAtLocation(l103);

        l104 = getLocationFromIndex(104, start);
        dist104 = 1000000;
        h104 = l104.distanceSquaredTo(destination);
        exists104 = rc.onTheMap(l104) && seenLocations[l104.y][l104.x].isPassable() && !rc.canSenseRobotAtLocation(l104);

        l105 = getLocationFromIndex(105, start);
        dist105 = 1000000;
        h105 = l105.distanceSquaredTo(destination);
        exists105 = rc.onTheMap(l105) && seenLocations[l105.y][l105.x].isPassable() && !rc.canSenseRobotAtLocation(l105);

        l106 = getLocationFromIndex(106, start);
        dist106 = 1000000;
        h106 = l106.distanceSquaredTo(destination);
        exists106 = rc.onTheMap(l106) && seenLocations[l106.y][l106.x].isPassable() && !rc.canSenseRobotAtLocation(l106);

        l107 = getLocationFromIndex(107, start);
        dist107 = 1000000;
        h107 = l107.distanceSquaredTo(destination);
        exists107 = rc.onTheMap(l107) && seenLocations[l107.y][l107.x].isPassable() && !rc.canSenseRobotAtLocation(l107);

        l108 = getLocationFromIndex(108, start);
        dist108 = 1000000;
        h108 = l108.distanceSquaredTo(destination);
        exists108 = rc.onTheMap(l108) && seenLocations[l108.y][l108.x].isPassable() && !rc.canSenseRobotAtLocation(l108);

        l109 = getLocationFromIndex(109, start);
        dist109 = 1000000;
        h109 = l109.distanceSquaredTo(destination);
        exists109 = rc.onTheMap(l109) && seenLocations[l109.y][l109.x].isPassable() && !rc.canSenseRobotAtLocation(l109);

        l110 = getLocationFromIndex(110, start);
        dist110 = 1000000;
        h110 = l110.distanceSquaredTo(destination);
        exists110 = rc.onTheMap(l110) && seenLocations[l110.y][l110.x].isPassable() && !rc.canSenseRobotAtLocation(l110);

        l111 = getLocationFromIndex(111, start);
        dist111 = 1000000;
        h111 = l111.distanceSquaredTo(destination);
        exists111 = rc.onTheMap(l111) && seenLocations[l111.y][l111.x].isPassable() && !rc.canSenseRobotAtLocation(l111);

        l112 = getLocationFromIndex(112, start);
        dist112 = 1000000;
        h112 = l112.distanceSquaredTo(destination);
        exists112 = rc.onTheMap(l112) && seenLocations[l112.y][l112.x].isPassable() && !rc.canSenseRobotAtLocation(l112);

        l113 = getLocationFromIndex(113, start);
        dist113 = 1000000;
        h113 = l113.distanceSquaredTo(destination);
        exists113 = rc.onTheMap(l113) && seenLocations[l113.y][l113.x].isPassable() && !rc.canSenseRobotAtLocation(l113);

        l114 = getLocationFromIndex(114, start);
        dist114 = 1000000;
        h114 = l114.distanceSquaredTo(destination);
        exists114 = rc.onTheMap(l114) && seenLocations[l114.y][l114.x].isPassable() && !rc.canSenseRobotAtLocation(l114);

        l115 = getLocationFromIndex(115, start);
        dist115 = 1000000;
        h115 = l115.distanceSquaredTo(destination);
        exists115 = rc.onTheMap(l115) && seenLocations[l115.y][l115.x].isPassable() && !rc.canSenseRobotAtLocation(l115);

        l116 = getLocationFromIndex(116, start);
        dist116 = 1000000;
        h116 = l116.distanceSquaredTo(destination);
        exists116 = rc.onTheMap(l116) && seenLocations[l116.y][l116.x].isPassable() && !rc.canSenseRobotAtLocation(l116);

        l117 = getLocationFromIndex(117, start);
        dist117 = 1000000;
        h117 = l117.distanceSquaredTo(destination);
        exists117 = rc.onTheMap(l117) && seenLocations[l117.y][l117.x].isPassable() && !rc.canSenseRobotAtLocation(l117);

        l118 = getLocationFromIndex(118, start);
        dist118 = 1000000;
        h118 = l118.distanceSquaredTo(destination);
        exists118 = rc.onTheMap(l118) && seenLocations[l118.y][l118.x].isPassable() && !rc.canSenseRobotAtLocation(l118);

        l119 = getLocationFromIndex(119, start);
        dist119 = 1000000;
        h119 = l119.distanceSquaredTo(destination);
        exists119 = rc.onTheMap(l119) && seenLocations[l119.y][l119.x].isPassable() && !rc.canSenseRobotAtLocation(l119);

        l120 = getLocationFromIndex(120, start);
        dist120 = 1000000;
        h120 = l120.distanceSquaredTo(destination);
        exists120 = rc.onTheMap(l120) && seenLocations[l120.y][l120.x].isPassable() && !rc.canSenseRobotAtLocation(l120);

       method1();
       method2();
       method3();
       method4();

        if(exists106)
        {
            if(exists94 && dist106 + 1 < dist94)
            {
                dist94 = dist106 + 1;
                lookup[indexToLocalY(94)][indexToLocalX(94)] = Direction.SOUTHEAST;
                if(h94 < closestDistance)
                {
                    closestDistance = h94;
                    closestIndex = 94;
                }
            }
            if(exists95 && dist106 + 1 < dist95)
            {
                dist95 = dist106 + 1;
                lookup[indexToLocalY(95)][indexToLocalX(95)] = Direction.SOUTH;
                if(h95 < closestDistance)
                {
                    closestDistance = h95;
                    closestIndex = 95;
                }
            }
            if(exists96 && dist106 + 1 < dist96)
            {
                dist96 = dist106 + 1;
                lookup[indexToLocalY(96)][indexToLocalX(96)] = Direction.SOUTHWEST;
                if(h96 < closestDistance)
                {
                    closestDistance = h96;
                    closestIndex = 96;
                }
            }
            if(exists105 && dist106 + 1 < dist105)
            {
                dist105 = dist106 + 1;
                lookup[indexToLocalY(105)][indexToLocalX(105)] = Direction.EAST;
                if(h105 < closestDistance)
                {
                    closestDistance = h105;
                    closestIndex = 105;
                }
            }
            if(exists107 && dist106 + 1 < dist107)
            {
                dist107 = dist106 + 1;
                lookup[indexToLocalY(107)][indexToLocalX(107)] = Direction.WEST;
                if(h107 < closestDistance)
                {
                    closestDistance = h107;
                    closestIndex = 107;
                }
            }
            if(exists116 && dist106 + 1 < dist116)
            {
                dist116 = dist106 + 1;
                lookup[indexToLocalY(116)][indexToLocalX(116)] = Direction.NORTHEAST;
                if(h116 < closestDistance)
                {
                    closestDistance = h116;
                    closestIndex = 116;
                }
            }
            if(exists117 && dist106 + 1 < dist117)
            {
                dist117 = dist106 + 1;
                lookup[indexToLocalY(117)][indexToLocalX(117)] = Direction.NORTH;
                if(h117 < closestDistance)
                {
                    closestDistance = h117;
                    closestIndex = 117;
                }
            }
            if(exists118 && dist106 + 1 < dist118)
            {
                dist118 = dist106 + 1;
                lookup[indexToLocalY(118)][indexToLocalX(118)] = Direction.NORTHWEST;
                if(h118 < closestDistance)
                {
                    closestDistance = h118;
                    closestIndex = 118;
                }
            }
        }
        if(exists107)
        {
            if(exists95 && dist107 + 1 < dist95)
            {
                dist95 = dist107 + 1;
                lookup[indexToLocalY(95)][indexToLocalX(95)] = Direction.SOUTHEAST;
                if(h95 < closestDistance)
                {
                    closestDistance = h95;
                    closestIndex = 95;
                }
            }
            if(exists96 && dist107 + 1 < dist96)
            {
                dist96 = dist107 + 1;
                lookup[indexToLocalY(96)][indexToLocalX(96)] = Direction.SOUTH;
                if(h96 < closestDistance)
                {
                    closestDistance = h96;
                    closestIndex = 96;
                }
            }
            if(exists97 && dist107 + 1 < dist97)
            {
                dist97 = dist107 + 1;
                lookup[indexToLocalY(97)][indexToLocalX(97)] = Direction.SOUTHWEST;
                if(h97 < closestDistance)
                {
                    closestDistance = h97;
                    closestIndex = 97;
                }
            }
            if(exists106 && dist107 + 1 < dist106)
            {
                dist106 = dist107 + 1;
                lookup[indexToLocalY(106)][indexToLocalX(106)] = Direction.EAST;
                if(h106 < closestDistance)
                {
                    closestDistance = h106;
                    closestIndex = 106;
                }
            }
            if(exists108 && dist107 + 1 < dist108)
            {
                dist108 = dist107 + 1;
                lookup[indexToLocalY(108)][indexToLocalX(108)] = Direction.WEST;
                if(h108 < closestDistance)
                {
                    closestDistance = h108;
                    closestIndex = 108;
                }
            }
            if(exists117 && dist107 + 1 < dist117)
            {
                dist117 = dist107 + 1;
                lookup[indexToLocalY(117)][indexToLocalX(117)] = Direction.NORTHEAST;
                if(h117 < closestDistance)
                {
                    closestDistance = h117;
                    closestIndex = 117;
                }
            }
            if(exists118 && dist107 + 1 < dist118)
            {
                dist118 = dist107 + 1;
                lookup[indexToLocalY(118)][indexToLocalX(118)] = Direction.NORTH;
                if(h118 < closestDistance)
                {
                    closestDistance = h118;
                    closestIndex = 118;
                }
            }
            if(exists119 && dist107 + 1 < dist119)
            {
                dist119 = dist107 + 1;
                lookup[indexToLocalY(119)][indexToLocalX(119)] = Direction.NORTHWEST;
                if(h119 < closestDistance)
                {
                    closestDistance = h119;
                    closestIndex = 119;
                }
            }
        }
        if(exists108)
        {
            if(exists96 && dist108 + 1 < dist96)
            {
                dist96 = dist108 + 1;
                lookup[indexToLocalY(96)][indexToLocalX(96)] = Direction.SOUTHEAST;
                if(h96 < closestDistance)
                {
                    closestDistance = h96;
                    closestIndex = 96;
                }
            }
            if(exists97 && dist108 + 1 < dist97)
            {
                dist97 = dist108 + 1;
                lookup[indexToLocalY(97)][indexToLocalX(97)] = Direction.SOUTH;
                if(h97 < closestDistance)
                {
                    closestDistance = h97;
                    closestIndex = 97;
                }
            }
            if(exists98 && dist108 + 1 < dist98)
            {
                dist98 = dist108 + 1;
                lookup[indexToLocalY(98)][indexToLocalX(98)] = Direction.SOUTHWEST;
                if(h98 < closestDistance)
                {
                    closestDistance = h98;
                    closestIndex = 98;
                }
            }
            if(exists107 && dist108 + 1 < dist107)
            {
                dist107 = dist108 + 1;
                lookup[indexToLocalY(107)][indexToLocalX(107)] = Direction.EAST;
                if(h107 < closestDistance)
                {
                    closestDistance = h107;
                    closestIndex = 107;
                }
            }
            if(exists109 && dist108 + 1 < dist109)
            {
                dist109 = dist108 + 1;
                lookup[indexToLocalY(109)][indexToLocalX(109)] = Direction.WEST;
                if(h109 < closestDistance)
                {
                    closestDistance = h109;
                    closestIndex = 109;
                }
            }
            if(exists118 && dist108 + 1 < dist118)
            {
                dist118 = dist108 + 1;
                lookup[indexToLocalY(118)][indexToLocalX(118)] = Direction.NORTHEAST;
                if(h118 < closestDistance)
                {
                    closestDistance = h118;
                    closestIndex = 118;
                }
            }
            if(exists119 && dist108 + 1 < dist119)
            {
                dist119 = dist108 + 1;
                lookup[indexToLocalY(119)][indexToLocalX(119)] = Direction.NORTH;
                if(h119 < closestDistance)
                {
                    closestDistance = h119;
                    closestIndex = 119;
                }
            }
            if(exists120 && dist108 + 1 < dist120)
            {
                dist120 = dist108 + 1;
                lookup[indexToLocalY(120)][indexToLocalX(120)] = Direction.NORTHWEST;
                if(h120 < closestDistance)
                {
                    closestDistance = h120;
                    closestIndex = 120;
                }
            }
        }
        if(exists0)
        {
            if(exists1 && dist0 + 1 < dist1)
            {
                dist1 = dist0 + 1;
                lookup[indexToLocalY(1)][indexToLocalX(1)] = Direction.WEST;
                if(h1 < closestDistance)
                {
                    closestDistance = h1;
                    closestIndex = 1;
                }
            }
            if(exists11 && dist0 + 1 < dist11)
            {
                dist11 = dist0 + 1;
                lookup[indexToLocalY(11)][indexToLocalX(11)] = Direction.NORTH;
                if(h11 < closestDistance)
                {
                    closestDistance = h11;
                    closestIndex = 11;
                }
            }
            if(exists12 && dist0 + 1 < dist12)
            {
                dist12 = dist0 + 1;
                lookup[indexToLocalY(12)][indexToLocalX(12)] = Direction.NORTHWEST;
                if(h12 < closestDistance)
                {
                    closestDistance = h12;
                    closestIndex = 12;
                }
            }
        }
        if(exists1)
        {
            if(exists0 && dist1 + 1 < dist0)
            {
                dist0 = dist1 + 1;
                lookup[indexToLocalY(0)][indexToLocalX(0)] = Direction.EAST;
                if(h0 < closestDistance)
                {
                    closestDistance = h0;
                    closestIndex = 0;
                }
            }
            if(exists2 && dist1 + 1 < dist2)
            {
                dist2 = dist1 + 1;
                lookup[indexToLocalY(2)][indexToLocalX(2)] = Direction.WEST;
                if(h2 < closestDistance)
                {
                    closestDistance = h2;
                    closestIndex = 2;
                }
            }
            if(exists11 && dist1 + 1 < dist11)
            {
                dist11 = dist1 + 1;
                lookup[indexToLocalY(11)][indexToLocalX(11)] = Direction.NORTHEAST;
                if(h11 < closestDistance)
                {
                    closestDistance = h11;
                    closestIndex = 11;
                }
            }
            if(exists12 && dist1 + 1 < dist12)
            {
                dist12 = dist1 + 1;
                lookup[indexToLocalY(12)][indexToLocalX(12)] = Direction.NORTH;
                if(h12 < closestDistance)
                {
                    closestDistance = h12;
                    closestIndex = 12;
                }
            }
            if(exists13 && dist1 + 1 < dist13)
            {
                dist13 = dist1 + 1;
                lookup[indexToLocalY(13)][indexToLocalX(13)] = Direction.NORTHWEST;
                if(h13 < closestDistance)
                {
                    closestDistance = h13;
                    closestIndex = 13;
                }
            }
        }
        if(exists2)
        {
            if(exists1 && dist2 + 1 < dist1)
            {
                dist1 = dist2 + 1;
                lookup[indexToLocalY(1)][indexToLocalX(1)] = Direction.EAST;
                if(h1 < closestDistance)
                {
                    closestDistance = h1;
                    closestIndex = 1;
                }
            }
            if(exists3 && dist2 + 1 < dist3)
            {
                dist3 = dist2 + 1;
                lookup[indexToLocalY(3)][indexToLocalX(3)] = Direction.WEST;
                if(h3 < closestDistance)
                {
                    closestDistance = h3;
                    closestIndex = 3;
                }
            }
            if(exists12 && dist2 + 1 < dist12)
            {
                dist12 = dist2 + 1;
                lookup[indexToLocalY(12)][indexToLocalX(12)] = Direction.NORTHEAST;
                if(h12 < closestDistance)
                {
                    closestDistance = h12;
                    closestIndex = 12;
                }
            }
            if(exists13 && dist2 + 1 < dist13)
            {
                dist13 = dist2 + 1;
                lookup[indexToLocalY(13)][indexToLocalX(13)] = Direction.NORTH;
                if(h13 < closestDistance)
                {
                    closestDistance = h13;
                    closestIndex = 13;
                }
            }
            if(exists14 && dist2 + 1 < dist14)
            {
                dist14 = dist2 + 1;
                lookup[indexToLocalY(14)][indexToLocalX(14)] = Direction.NORTHWEST;
                if(h14 < closestDistance)
                {
                    closestDistance = h14;
                    closestIndex = 14;
                }
            }
        }
        if(exists11)
        {
            if(exists0 && dist11 + 1 < dist0)
            {
                dist0 = dist11 + 1;
                lookup[indexToLocalY(0)][indexToLocalX(0)] = Direction.SOUTH;
                if(h0 < closestDistance)
                {
                    closestDistance = h0;
                    closestIndex = 0;
                }
            }
            if(exists1 && dist11 + 1 < dist1)
            {
                dist1 = dist11 + 1;
                lookup[indexToLocalY(1)][indexToLocalX(1)] = Direction.SOUTHWEST;
                if(h1 < closestDistance)
                {
                    closestDistance = h1;
                    closestIndex = 1;
                }
            }
            if(exists12 && dist11 + 1 < dist12)
            {
                dist12 = dist11 + 1;
                lookup[indexToLocalY(12)][indexToLocalX(12)] = Direction.WEST;
                if(h12 < closestDistance)
                {
                    closestDistance = h12;
                    closestIndex = 12;
                }
            }
            if(exists22 && dist11 + 1 < dist22)
            {
                dist22 = dist11 + 1;
                lookup[indexToLocalY(22)][indexToLocalX(22)] = Direction.NORTH;
                if(h22 < closestDistance)
                {
                    closestDistance = h22;
                    closestIndex = 22;
                }
            }
            if(exists23 && dist11 + 1 < dist23)
            {
                dist23 = dist11 + 1;
                lookup[indexToLocalY(23)][indexToLocalX(23)] = Direction.NORTHWEST;
                if(h23 < closestDistance)
                {
                    closestDistance = h23;
                    closestIndex = 23;
                }
            }
        }
        if(exists22)
        {
            if(exists11 && dist22 + 1 < dist11)
            {
                dist11 = dist22 + 1;
                lookup[indexToLocalY(11)][indexToLocalX(11)] = Direction.SOUTH;
                if(h11 < closestDistance)
                {
                    closestDistance = h11;
                    closestIndex = 11;
                }
            }
            if(exists12 && dist22 + 1 < dist12)
            {
                dist12 = dist22 + 1;
                lookup[indexToLocalY(12)][indexToLocalX(12)] = Direction.SOUTHWEST;
                if(h12 < closestDistance)
                {
                    closestDistance = h12;
                    closestIndex = 12;
                }
            }
            if(exists23 && dist22 + 1 < dist23)
            {
                dist23 = dist22 + 1;
                lookup[indexToLocalY(23)][indexToLocalX(23)] = Direction.WEST;
                if(h23 < closestDistance)
                {
                    closestDistance = h23;
                    closestIndex = 23;
                }
            }
            if(exists33 && dist22 + 1 < dist33)
            {
                dist33 = dist22 + 1;
                lookup[indexToLocalY(33)][indexToLocalX(33)] = Direction.NORTH;
                if(h33 < closestDistance)
                {
                    closestDistance = h33;
                    closestIndex = 33;
                }
            }
            if(exists34 && dist22 + 1 < dist34)
            {
                dist34 = dist22 + 1;
                lookup[indexToLocalY(34)][indexToLocalX(34)] = Direction.NORTHWEST;
                if(h34 < closestDistance)
                {
                    closestDistance = h34;
                    closestIndex = 34;
                }
            }
        }
        if(exists3)
        {
            if(exists2 && dist3 + 1 < dist2)
            {
                dist2 = dist3 + 1;
                lookup[indexToLocalY(2)][indexToLocalX(2)] = Direction.EAST;
                if(h2 < closestDistance)
                {
                    closestDistance = h2;
                    closestIndex = 2;
                }
            }
            if(exists4 && dist3 + 1 < dist4)
            {
                dist4 = dist3 + 1;
                lookup[indexToLocalY(4)][indexToLocalX(4)] = Direction.WEST;
                if(h4 < closestDistance)
                {
                    closestDistance = h4;
                    closestIndex = 4;
                }
            }
            if(exists13 && dist3 + 1 < dist13)
            {
                dist13 = dist3 + 1;
                lookup[indexToLocalY(13)][indexToLocalX(13)] = Direction.NORTHEAST;
                if(h13 < closestDistance)
                {
                    closestDistance = h13;
                    closestIndex = 13;
                }
            }
            if(exists14 && dist3 + 1 < dist14)
            {
                dist14 = dist3 + 1;
                lookup[indexToLocalY(14)][indexToLocalX(14)] = Direction.NORTH;
                if(h14 < closestDistance)
                {
                    closestDistance = h14;
                    closestIndex = 14;
                }
            }
            if(exists15 && dist3 + 1 < dist15)
            {
                dist15 = dist3 + 1;
                lookup[indexToLocalY(15)][indexToLocalX(15)] = Direction.NORTHWEST;
                if(h15 < closestDistance)
                {
                    closestDistance = h15;
                    closestIndex = 15;
                }
            }
        }
        if(exists4)
        {
            if(exists3 && dist4 + 1 < dist3)
            {
                dist3 = dist4 + 1;
                lookup[indexToLocalY(3)][indexToLocalX(3)] = Direction.EAST;
                if(h3 < closestDistance)
                {
                    closestDistance = h3;
                    closestIndex = 3;
                }
            }
            if(exists5 && dist4 + 1 < dist5)
            {
                dist5 = dist4 + 1;
                lookup[indexToLocalY(5)][indexToLocalX(5)] = Direction.WEST;
                if(h5 < closestDistance)
                {
                    closestDistance = h5;
                    closestIndex = 5;
                }
            }
            if(exists14 && dist4 + 1 < dist14)
            {
                dist14 = dist4 + 1;
                lookup[indexToLocalY(14)][indexToLocalX(14)] = Direction.NORTHEAST;
                if(h14 < closestDistance)
                {
                    closestDistance = h14;
                    closestIndex = 14;
                }
            }
            if(exists15 && dist4 + 1 < dist15)
            {
                dist15 = dist4 + 1;
                lookup[indexToLocalY(15)][indexToLocalX(15)] = Direction.NORTH;
                if(h15 < closestDistance)
                {
                    closestDistance = h15;
                    closestIndex = 15;
                }
            }
            if(exists16 && dist4 + 1 < dist16)
            {
                dist16 = dist4 + 1;
                lookup[indexToLocalY(16)][indexToLocalX(16)] = Direction.NORTHWEST;
                if(h16 < closestDistance)
                {
                    closestDistance = h16;
                    closestIndex = 16;
                }
            }
        }
        if(exists33)
        {
            if(exists22 && dist33 + 1 < dist22)
            {
                dist22 = dist33 + 1;
                lookup[indexToLocalY(22)][indexToLocalX(22)] = Direction.SOUTH;
                if(h22 < closestDistance)
                {
                    closestDistance = h22;
                    closestIndex = 22;
                }
            }
            if(exists23 && dist33 + 1 < dist23)
            {
                dist23 = dist33 + 1;
                lookup[indexToLocalY(23)][indexToLocalX(23)] = Direction.SOUTHWEST;
                if(h23 < closestDistance)
                {
                    closestDistance = h23;
                    closestIndex = 23;
                }
            }
            if(exists34 && dist33 + 1 < dist34)
            {
                dist34 = dist33 + 1;
                lookup[indexToLocalY(34)][indexToLocalX(34)] = Direction.WEST;
                if(h34 < closestDistance)
                {
                    closestDistance = h34;
                    closestIndex = 34;
                }
            }
            if(exists44 && dist33 + 1 < dist44)
            {
                dist44 = dist33 + 1;
                lookup[indexToLocalY(44)][indexToLocalX(44)] = Direction.NORTH;
                if(h44 < closestDistance)
                {
                    closestDistance = h44;
                    closestIndex = 44;
                }
            }
            if(exists45 && dist33 + 1 < dist45)
            {
                dist45 = dist33 + 1;
                lookup[indexToLocalY(45)][indexToLocalX(45)] = Direction.NORTHWEST;
                if(h45 < closestDistance)
                {
                    closestDistance = h45;
                    closestIndex = 45;
                }
            }
        }
        if(exists44)
        {
            if(exists33 && dist44 + 1 < dist33)
            {
                dist33 = dist44 + 1;
                lookup[indexToLocalY(33)][indexToLocalX(33)] = Direction.SOUTH;
                if(h33 < closestDistance)
                {
                    closestDistance = h33;
                    closestIndex = 33;
                }
            }
            if(exists34 && dist44 + 1 < dist34)
            {
                dist34 = dist44 + 1;
                lookup[indexToLocalY(34)][indexToLocalX(34)] = Direction.SOUTHWEST;
                if(h34 < closestDistance)
                {
                    closestDistance = h34;
                    closestIndex = 34;
                }
            }
            if(exists45 && dist44 + 1 < dist45)
            {
                dist45 = dist44 + 1;
                lookup[indexToLocalY(45)][indexToLocalX(45)] = Direction.WEST;
                if(h45 < closestDistance)
                {
                    closestDistance = h45;
                    closestIndex = 45;
                }
            }
            if(exists55 && dist44 + 1 < dist55)
            {
                dist55 = dist44 + 1;
                lookup[indexToLocalY(55)][indexToLocalX(55)] = Direction.NORTH;
                if(h55 < closestDistance)
                {
                    closestDistance = h55;
                    closestIndex = 55;
                }
            }
            if(exists56 && dist44 + 1 < dist56)
            {
                dist56 = dist44 + 1;
                lookup[indexToLocalY(56)][indexToLocalX(56)] = Direction.NORTHWEST;
                if(h56 < closestDistance)
                {
                    closestDistance = h56;
                    closestIndex = 56;
                }
            }
        }
        if(exists5)
        {
            if(exists4 && dist5 + 1 < dist4)
            {
                dist4 = dist5 + 1;
                lookup[indexToLocalY(4)][indexToLocalX(4)] = Direction.EAST;
                if(h4 < closestDistance)
                {
                    closestDistance = h4;
                    closestIndex = 4;
                }
            }
            if(exists6 && dist5 + 1 < dist6)
            {
                dist6 = dist5 + 1;
                lookup[indexToLocalY(6)][indexToLocalX(6)] = Direction.WEST;
                if(h6 < closestDistance)
                {
                    closestDistance = h6;
                    closestIndex = 6;
                }
            }
            if(exists15 && dist5 + 1 < dist15)
            {
                dist15 = dist5 + 1;
                lookup[indexToLocalY(15)][indexToLocalX(15)] = Direction.NORTHEAST;
                if(h15 < closestDistance)
                {
                    closestDistance = h15;
                    closestIndex = 15;
                }
            }
            if(exists16 && dist5 + 1 < dist16)
            {
                dist16 = dist5 + 1;
                lookup[indexToLocalY(16)][indexToLocalX(16)] = Direction.NORTH;
                if(h16 < closestDistance)
                {
                    closestDistance = h16;
                    closestIndex = 16;
                }
            }
            if(exists17 && dist5 + 1 < dist17)
            {
                dist17 = dist5 + 1;
                lookup[indexToLocalY(17)][indexToLocalX(17)] = Direction.NORTHWEST;
                if(h17 < closestDistance)
                {
                    closestDistance = h17;
                    closestIndex = 17;
                }
            }
        }
        if(exists6)
        {
            if(exists5 && dist6 + 1 < dist5)
            {
                dist5 = dist6 + 1;
                lookup[indexToLocalY(5)][indexToLocalX(5)] = Direction.EAST;
                if(h5 < closestDistance)
                {
                    closestDistance = h5;
                    closestIndex = 5;
                }
            }
            if(exists7 && dist6 + 1 < dist7)
            {
                dist7 = dist6 + 1;
                lookup[indexToLocalY(7)][indexToLocalX(7)] = Direction.WEST;
                if(h7 < closestDistance)
                {
                    closestDistance = h7;
                    closestIndex = 7;
                }
            }
            if(exists16 && dist6 + 1 < dist16)
            {
                dist16 = dist6 + 1;
                lookup[indexToLocalY(16)][indexToLocalX(16)] = Direction.NORTHEAST;
                if(h16 < closestDistance)
                {
                    closestDistance = h16;
                    closestIndex = 16;
                }
            }
            if(exists17 && dist6 + 1 < dist17)
            {
                dist17 = dist6 + 1;
                lookup[indexToLocalY(17)][indexToLocalX(17)] = Direction.NORTH;
                if(h17 < closestDistance)
                {
                    closestDistance = h17;
                    closestIndex = 17;
                }
            }
            if(exists18 && dist6 + 1 < dist18)
            {
                dist18 = dist6 + 1;
                lookup[indexToLocalY(18)][indexToLocalX(18)] = Direction.NORTHWEST;
                if(h18 < closestDistance)
                {
                    closestDistance = h18;
                    closestIndex = 18;
                }
            }
        }
        if(exists55)
        {
            if(exists44 && dist55 + 1 < dist44)
            {
                dist44 = dist55 + 1;
                lookup[indexToLocalY(44)][indexToLocalX(44)] = Direction.SOUTH;
                if(h44 < closestDistance)
                {
                    closestDistance = h44;
                    closestIndex = 44;
                }
            }
            if(exists45 && dist55 + 1 < dist45)
            {
                dist45 = dist55 + 1;
                lookup[indexToLocalY(45)][indexToLocalX(45)] = Direction.SOUTHWEST;
                if(h45 < closestDistance)
                {
                    closestDistance = h45;
                    closestIndex = 45;
                }
            }
            if(exists56 && dist55 + 1 < dist56)
            {
                dist56 = dist55 + 1;
                lookup[indexToLocalY(56)][indexToLocalX(56)] = Direction.WEST;
                if(h56 < closestDistance)
                {
                    closestDistance = h56;
                    closestIndex = 56;
                }
            }
            if(exists66 && dist55 + 1 < dist66)
            {
                dist66 = dist55 + 1;
                lookup[indexToLocalY(66)][indexToLocalX(66)] = Direction.NORTH;
                if(h66 < closestDistance)
                {
                    closestDistance = h66;
                    closestIndex = 66;
                }
            }
            if(exists67 && dist55 + 1 < dist67)
            {
                dist67 = dist55 + 1;
                lookup[indexToLocalY(67)][indexToLocalX(67)] = Direction.NORTHWEST;
                if(h67 < closestDistance)
                {
                    closestDistance = h67;
                    closestIndex = 67;
                }
            }
        }
        if(exists66)
        {
            if(exists55 && dist66 + 1 < dist55)
            {
                dist55 = dist66 + 1;
                lookup[indexToLocalY(55)][indexToLocalX(55)] = Direction.SOUTH;
                if(h55 < closestDistance)
                {
                    closestDistance = h55;
                    closestIndex = 55;
                }
            }
            if(exists56 && dist66 + 1 < dist56)
            {
                dist56 = dist66 + 1;
                lookup[indexToLocalY(56)][indexToLocalX(56)] = Direction.SOUTHWEST;
                if(h56 < closestDistance)
                {
                    closestDistance = h56;
                    closestIndex = 56;
                }
            }
            if(exists67 && dist66 + 1 < dist67)
            {
                dist67 = dist66 + 1;
                lookup[indexToLocalY(67)][indexToLocalX(67)] = Direction.WEST;
                if(h67 < closestDistance)
                {
                    closestDistance = h67;
                    closestIndex = 67;
                }
            }
            if(exists77 && dist66 + 1 < dist77)
            {
                dist77 = dist66 + 1;
                lookup[indexToLocalY(77)][indexToLocalX(77)] = Direction.NORTH;
                if(h77 < closestDistance)
                {
                    closestDistance = h77;
                    closestIndex = 77;
                }
            }
            if(exists78 && dist66 + 1 < dist78)
            {
                dist78 = dist66 + 1;
                lookup[indexToLocalY(78)][indexToLocalX(78)] = Direction.NORTHWEST;
                if(h78 < closestDistance)
                {
                    closestDistance = h78;
                    closestIndex = 78;
                }
            }
        }
        if(exists7)
        {
            if(exists6 && dist7 + 1 < dist6)
            {
                dist6 = dist7 + 1;
                lookup[indexToLocalY(6)][indexToLocalX(6)] = Direction.EAST;
                if(h6 < closestDistance)
                {
                    closestDistance = h6;
                    closestIndex = 6;
                }
            }
            if(exists8 && dist7 + 1 < dist8)
            {
                dist8 = dist7 + 1;
                lookup[indexToLocalY(8)][indexToLocalX(8)] = Direction.WEST;
                if(h8 < closestDistance)
                {
                    closestDistance = h8;
                    closestIndex = 8;
                }
            }
            if(exists17 && dist7 + 1 < dist17)
            {
                dist17 = dist7 + 1;
                lookup[indexToLocalY(17)][indexToLocalX(17)] = Direction.NORTHEAST;
                if(h17 < closestDistance)
                {
                    closestDistance = h17;
                    closestIndex = 17;
                }
            }
            if(exists18 && dist7 + 1 < dist18)
            {
                dist18 = dist7 + 1;
                lookup[indexToLocalY(18)][indexToLocalX(18)] = Direction.NORTH;
                if(h18 < closestDistance)
                {
                    closestDistance = h18;
                    closestIndex = 18;
                }
            }
            if(exists19 && dist7 + 1 < dist19)
            {
                dist19 = dist7 + 1;
                lookup[indexToLocalY(19)][indexToLocalX(19)] = Direction.NORTHWEST;
                if(h19 < closestDistance)
                {
                    closestDistance = h19;
                    closestIndex = 19;
                }
            }
        }
        if(exists8)
        {
            if(exists7 && dist8 + 1 < dist7)
            {
                dist7 = dist8 + 1;
                lookup[indexToLocalY(7)][indexToLocalX(7)] = Direction.EAST;
                if(h7 < closestDistance)
                {
                    closestDistance = h7;
                    closestIndex = 7;
                }
            }
            if(exists9 && dist8 + 1 < dist9)
            {
                dist9 = dist8 + 1;
                lookup[indexToLocalY(9)][indexToLocalX(9)] = Direction.WEST;
                if(h9 < closestDistance)
                {
                    closestDistance = h9;
                    closestIndex = 9;
                }
            }
            if(exists18 && dist8 + 1 < dist18)
            {
                dist18 = dist8 + 1;
                lookup[indexToLocalY(18)][indexToLocalX(18)] = Direction.NORTHEAST;
                if(h18 < closestDistance)
                {
                    closestDistance = h18;
                    closestIndex = 18;
                }
            }
            if(exists19 && dist8 + 1 < dist19)
            {
                dist19 = dist8 + 1;
                lookup[indexToLocalY(19)][indexToLocalX(19)] = Direction.NORTH;
                if(h19 < closestDistance)
                {
                    closestDistance = h19;
                    closestIndex = 19;
                }
            }
            if(exists20 && dist8 + 1 < dist20)
            {
                dist20 = dist8 + 1;
                lookup[indexToLocalY(20)][indexToLocalX(20)] = Direction.NORTHWEST;
                if(h20 < closestDistance)
                {
                    closestDistance = h20;
                    closestIndex = 20;
                }
            }
        }
        if(exists77)
        {
            if(exists66 && dist77 + 1 < dist66)
            {
                dist66 = dist77 + 1;
                lookup[indexToLocalY(66)][indexToLocalX(66)] = Direction.SOUTH;
                if(h66 < closestDistance)
                {
                    closestDistance = h66;
                    closestIndex = 66;
                }
            }
            if(exists67 && dist77 + 1 < dist67)
            {
                dist67 = dist77 + 1;
                lookup[indexToLocalY(67)][indexToLocalX(67)] = Direction.SOUTHWEST;
                if(h67 < closestDistance)
                {
                    closestDistance = h67;
                    closestIndex = 67;
                }
            }
            if(exists78 && dist77 + 1 < dist78)
            {
                dist78 = dist77 + 1;
                lookup[indexToLocalY(78)][indexToLocalX(78)] = Direction.WEST;
                if(h78 < closestDistance)
                {
                    closestDistance = h78;
                    closestIndex = 78;
                }
            }
            if(exists88 && dist77 + 1 < dist88)
            {
                dist88 = dist77 + 1;
                lookup[indexToLocalY(88)][indexToLocalX(88)] = Direction.NORTH;
                if(h88 < closestDistance)
                {
                    closestDistance = h88;
                    closestIndex = 88;
                }
            }
            if(exists89 && dist77 + 1 < dist89)
            {
                dist89 = dist77 + 1;
                lookup[indexToLocalY(89)][indexToLocalX(89)] = Direction.NORTHWEST;
                if(h89 < closestDistance)
                {
                    closestDistance = h89;
                    closestIndex = 89;
                }
            }
        }
        if(exists88)
        {
            if(exists77 && dist88 + 1 < dist77)
            {
                dist77 = dist88 + 1;
                lookup[indexToLocalY(77)][indexToLocalX(77)] = Direction.SOUTH;
                if(h77 < closestDistance)
                {
                    closestDistance = h77;
                    closestIndex = 77;
                }
            }
            if(exists78 && dist88 + 1 < dist78)
            {
                dist78 = dist88 + 1;
                lookup[indexToLocalY(78)][indexToLocalX(78)] = Direction.SOUTHWEST;
                if(h78 < closestDistance)
                {
                    closestDistance = h78;
                    closestIndex = 78;
                }
            }
            if(exists89 && dist88 + 1 < dist89)
            {
                dist89 = dist88 + 1;
                lookup[indexToLocalY(89)][indexToLocalX(89)] = Direction.WEST;
                if(h89 < closestDistance)
                {
                    closestDistance = h89;
                    closestIndex = 89;
                }
            }
            if(exists99 && dist88 + 1 < dist99)
            {
                dist99 = dist88 + 1;
                lookup[indexToLocalY(99)][indexToLocalX(99)] = Direction.NORTH;
                if(h99 < closestDistance)
                {
                    closestDistance = h99;
                    closestIndex = 99;
                }
            }
            if(exists100 && dist88 + 1 < dist100)
            {
                dist100 = dist88 + 1;
                lookup[indexToLocalY(100)][indexToLocalX(100)] = Direction.NORTHWEST;
                if(h100 < closestDistance)
                {
                    closestDistance = h100;
                    closestIndex = 100;
                }
            }
        }
        if(exists9)
        {
            if(exists8 && dist9 + 1 < dist8)
            {
                dist8 = dist9 + 1;
                lookup[indexToLocalY(8)][indexToLocalX(8)] = Direction.EAST;
                if(h8 < closestDistance)
                {
                    closestDistance = h8;
                    closestIndex = 8;
                }
            }
            if(exists10 && dist9 + 1 < dist10)
            {
                dist10 = dist9 + 1;
                lookup[indexToLocalY(10)][indexToLocalX(10)] = Direction.WEST;
                if(h10 < closestDistance)
                {
                    closestDistance = h10;
                    closestIndex = 10;
                }
            }
            if(exists19 && dist9 + 1 < dist19)
            {
                dist19 = dist9 + 1;
                lookup[indexToLocalY(19)][indexToLocalX(19)] = Direction.NORTHEAST;
                if(h19 < closestDistance)
                {
                    closestDistance = h19;
                    closestIndex = 19;
                }
            }
            if(exists20 && dist9 + 1 < dist20)
            {
                dist20 = dist9 + 1;
                lookup[indexToLocalY(20)][indexToLocalX(20)] = Direction.NORTH;
                if(h20 < closestDistance)
                {
                    closestDistance = h20;
                    closestIndex = 20;
                }
            }
            if(exists21 && dist9 + 1 < dist21)
            {
                dist21 = dist9 + 1;
                lookup[indexToLocalY(21)][indexToLocalX(21)] = Direction.NORTHWEST;
                if(h21 < closestDistance)
                {
                    closestDistance = h21;
                    closestIndex = 21;
                }
            }
        }
        if(exists10)
        {
            if(exists9 && dist10 + 1 < dist9)
            {
                dist9 = dist10 + 1;
                lookup[indexToLocalY(9)][indexToLocalX(9)] = Direction.EAST;
                if(h9 < closestDistance)
                {
                    closestDistance = h9;
                    closestIndex = 9;
                }
            }
            if(exists20 && dist10 + 1 < dist20)
            {
                dist20 = dist10 + 1;
                lookup[indexToLocalY(20)][indexToLocalX(20)] = Direction.NORTHEAST;
                if(h20 < closestDistance)
                {
                    closestDistance = h20;
                    closestIndex = 20;
                }
            }
            if(exists21 && dist10 + 1 < dist21)
            {
                dist21 = dist10 + 1;
                lookup[indexToLocalY(21)][indexToLocalX(21)] = Direction.NORTH;
                if(h21 < closestDistance)
                {
                    closestDistance = h21;
                    closestIndex = 21;
                }
            }
        }
        if(exists21)
        {
            if(exists9 && dist21 + 1 < dist9)
            {
                dist9 = dist21 + 1;
                lookup[indexToLocalY(9)][indexToLocalX(9)] = Direction.SOUTHEAST;
                if(h9 < closestDistance)
                {
                    closestDistance = h9;
                    closestIndex = 9;
                }
            }
            if(exists10 && dist21 + 1 < dist10)
            {
                dist10 = dist21 + 1;
                lookup[indexToLocalY(10)][indexToLocalX(10)] = Direction.SOUTH;
                if(h10 < closestDistance)
                {
                    closestDistance = h10;
                    closestIndex = 10;
                }
            }
            if(exists20 && dist21 + 1 < dist20)
            {
                dist20 = dist21 + 1;
                lookup[indexToLocalY(20)][indexToLocalX(20)] = Direction.EAST;
                if(h20 < closestDistance)
                {
                    closestDistance = h20;
                    closestIndex = 20;
                }
            }
            if(exists31 && dist21 + 1 < dist31)
            {
                dist31 = dist21 + 1;
                lookup[indexToLocalY(31)][indexToLocalX(31)] = Direction.NORTHEAST;
                if(h31 < closestDistance)
                {
                    closestDistance = h31;
                    closestIndex = 31;
                }
            }
            if(exists32 && dist21 + 1 < dist32)
            {
                dist32 = dist21 + 1;
                lookup[indexToLocalY(32)][indexToLocalX(32)] = Direction.NORTH;
                if(h32 < closestDistance)
                {
                    closestDistance = h32;
                    closestIndex = 32;
                }
            }
        }
        if(exists32)
        {
            if(exists20 && dist32 + 1 < dist20)
            {
                dist20 = dist32 + 1;
                lookup[indexToLocalY(20)][indexToLocalX(20)] = Direction.SOUTHEAST;
                if(h20 < closestDistance)
                {
                    closestDistance = h20;
                    closestIndex = 20;
                }
            }
            if(exists21 && dist32 + 1 < dist21)
            {
                dist21 = dist32 + 1;
                lookup[indexToLocalY(21)][indexToLocalX(21)] = Direction.SOUTH;
                if(h21 < closestDistance)
                {
                    closestDistance = h21;
                    closestIndex = 21;
                }
            }
            if(exists31 && dist32 + 1 < dist31)
            {
                dist31 = dist32 + 1;
                lookup[indexToLocalY(31)][indexToLocalX(31)] = Direction.EAST;
                if(h31 < closestDistance)
                {
                    closestDistance = h31;
                    closestIndex = 31;
                }
            }
            if(exists42 && dist32 + 1 < dist42)
            {
                dist42 = dist32 + 1;
                lookup[indexToLocalY(42)][indexToLocalX(42)] = Direction.NORTHEAST;
                if(h42 < closestDistance)
                {
                    closestDistance = h42;
                    closestIndex = 42;
                }
            }
            if(exists43 && dist32 + 1 < dist43)
            {
                dist43 = dist32 + 1;
                lookup[indexToLocalY(43)][indexToLocalX(43)] = Direction.NORTH;
                if(h43 < closestDistance)
                {
                    closestDistance = h43;
                    closestIndex = 43;
                }
            }
        }
        if(exists43)
        {
            if(exists31 && dist43 + 1 < dist31)
            {
                dist31 = dist43 + 1;
                lookup[indexToLocalY(31)][indexToLocalX(31)] = Direction.SOUTHEAST;
                if(h31 < closestDistance)
                {
                    closestDistance = h31;
                    closestIndex = 31;
                }
            }
            if(exists32 && dist43 + 1 < dist32)
            {
                dist32 = dist43 + 1;
                lookup[indexToLocalY(32)][indexToLocalX(32)] = Direction.SOUTH;
                if(h32 < closestDistance)
                {
                    closestDistance = h32;
                    closestIndex = 32;
                }
            }
            if(exists42 && dist43 + 1 < dist42)
            {
                dist42 = dist43 + 1;
                lookup[indexToLocalY(42)][indexToLocalX(42)] = Direction.EAST;
                if(h42 < closestDistance)
                {
                    closestDistance = h42;
                    closestIndex = 42;
                }
            }
            if(exists53 && dist43 + 1 < dist53)
            {
                dist53 = dist43 + 1;
                lookup[indexToLocalY(53)][indexToLocalX(53)] = Direction.NORTHEAST;
                if(h53 < closestDistance)
                {
                    closestDistance = h53;
                    closestIndex = 53;
                }
            }
            if(exists54 && dist43 + 1 < dist54)
            {
                dist54 = dist43 + 1;
                lookup[indexToLocalY(54)][indexToLocalX(54)] = Direction.NORTH;
                if(h54 < closestDistance)
                {
                    closestDistance = h54;
                    closestIndex = 54;
                }
            }
        }
        if(exists54)
        {
            if(exists42 && dist54 + 1 < dist42)
            {
                dist42 = dist54 + 1;
                lookup[indexToLocalY(42)][indexToLocalX(42)] = Direction.SOUTHEAST;
                if(h42 < closestDistance)
                {
                    closestDistance = h42;
                    closestIndex = 42;
                }
            }
            if(exists43 && dist54 + 1 < dist43)
            {
                dist43 = dist54 + 1;
                lookup[indexToLocalY(43)][indexToLocalX(43)] = Direction.SOUTH;
                if(h43 < closestDistance)
                {
                    closestDistance = h43;
                    closestIndex = 43;
                }
            }
            if(exists53 && dist54 + 1 < dist53)
            {
                dist53 = dist54 + 1;
                lookup[indexToLocalY(53)][indexToLocalX(53)] = Direction.EAST;
                if(h53 < closestDistance)
                {
                    closestDistance = h53;
                    closestIndex = 53;
                }
            }
            if(exists64 && dist54 + 1 < dist64)
            {
                dist64 = dist54 + 1;
                lookup[indexToLocalY(64)][indexToLocalX(64)] = Direction.NORTHEAST;
                if(h64 < closestDistance)
                {
                    closestDistance = h64;
                    closestIndex = 64;
                }
            }
            if(exists65 && dist54 + 1 < dist65)
            {
                dist65 = dist54 + 1;
                lookup[indexToLocalY(65)][indexToLocalX(65)] = Direction.NORTH;
                if(h65 < closestDistance)
                {
                    closestDistance = h65;
                    closestIndex = 65;
                }
            }
        }
        if(exists65)
        {
            if(exists53 && dist65 + 1 < dist53)
            {
                dist53 = dist65 + 1;
                lookup[indexToLocalY(53)][indexToLocalX(53)] = Direction.SOUTHEAST;
                if(h53 < closestDistance)
                {
                    closestDistance = h53;
                    closestIndex = 53;
                }
            }
            if(exists54 && dist65 + 1 < dist54)
            {
                dist54 = dist65 + 1;
                lookup[indexToLocalY(54)][indexToLocalX(54)] = Direction.SOUTH;
                if(h54 < closestDistance)
                {
                    closestDistance = h54;
                    closestIndex = 54;
                }
            }
            if(exists64 && dist65 + 1 < dist64)
            {
                dist64 = dist65 + 1;
                lookup[indexToLocalY(64)][indexToLocalX(64)] = Direction.EAST;
                if(h64 < closestDistance)
                {
                    closestDistance = h64;
                    closestIndex = 64;
                }
            }
            if(exists75 && dist65 + 1 < dist75)
            {
                dist75 = dist65 + 1;
                lookup[indexToLocalY(75)][indexToLocalX(75)] = Direction.NORTHEAST;
                if(h75 < closestDistance)
                {
                    closestDistance = h75;
                    closestIndex = 75;
                }
            }
            if(exists76 && dist65 + 1 < dist76)
            {
                dist76 = dist65 + 1;
                lookup[indexToLocalY(76)][indexToLocalX(76)] = Direction.NORTH;
                if(h76 < closestDistance)
                {
                    closestDistance = h76;
                    closestIndex = 76;
                }
            }
        }
        if(exists76)
        {
            if(exists64 && dist76 + 1 < dist64)
            {
                dist64 = dist76 + 1;
                lookup[indexToLocalY(64)][indexToLocalX(64)] = Direction.SOUTHEAST;
                if(h64 < closestDistance)
                {
                    closestDistance = h64;
                    closestIndex = 64;
                }
            }
            if(exists65 && dist76 + 1 < dist65)
            {
                dist65 = dist76 + 1;
                lookup[indexToLocalY(65)][indexToLocalX(65)] = Direction.SOUTH;
                if(h65 < closestDistance)
                {
                    closestDistance = h65;
                    closestIndex = 65;
                }
            }
            if(exists75 && dist76 + 1 < dist75)
            {
                dist75 = dist76 + 1;
                lookup[indexToLocalY(75)][indexToLocalX(75)] = Direction.EAST;
                if(h75 < closestDistance)
                {
                    closestDistance = h75;
                    closestIndex = 75;
                }
            }
            if(exists86 && dist76 + 1 < dist86)
            {
                dist86 = dist76 + 1;
                lookup[indexToLocalY(86)][indexToLocalX(86)] = Direction.NORTHEAST;
                if(h86 < closestDistance)
                {
                    closestDistance = h86;
                    closestIndex = 86;
                }
            }
            if(exists87 && dist76 + 1 < dist87)
            {
                dist87 = dist76 + 1;
                lookup[indexToLocalY(87)][indexToLocalX(87)] = Direction.NORTH;
                if(h87 < closestDistance)
                {
                    closestDistance = h87;
                    closestIndex = 87;
                }
            }
        }
        if(exists87)
        {
            if(exists75 && dist87 + 1 < dist75)
            {
                dist75 = dist87 + 1;
                lookup[indexToLocalY(75)][indexToLocalX(75)] = Direction.SOUTHEAST;
                if(h75 < closestDistance)
                {
                    closestDistance = h75;
                    closestIndex = 75;
                }
            }
            if(exists76 && dist87 + 1 < dist76)
            {
                dist76 = dist87 + 1;
                lookup[indexToLocalY(76)][indexToLocalX(76)] = Direction.SOUTH;
                if(h76 < closestDistance)
                {
                    closestDistance = h76;
                    closestIndex = 76;
                }
            }
            if(exists86 && dist87 + 1 < dist86)
            {
                dist86 = dist87 + 1;
                lookup[indexToLocalY(86)][indexToLocalX(86)] = Direction.EAST;
                if(h86 < closestDistance)
                {
                    closestDistance = h86;
                    closestIndex = 86;
                }
            }
            if(exists97 && dist87 + 1 < dist97)
            {
                dist97 = dist87 + 1;
                lookup[indexToLocalY(97)][indexToLocalX(97)] = Direction.NORTHEAST;
                if(h97 < closestDistance)
                {
                    closestDistance = h97;
                    closestIndex = 97;
                }
            }
            if(exists98 && dist87 + 1 < dist98)
            {
                dist98 = dist87 + 1;
                lookup[indexToLocalY(98)][indexToLocalX(98)] = Direction.NORTH;
                if(h98 < closestDistance)
                {
                    closestDistance = h98;
                    closestIndex = 98;
                }
            }
        }
        if(exists98)
        {
            if(exists86 && dist98 + 1 < dist86)
            {
                dist86 = dist98 + 1;
                lookup[indexToLocalY(86)][indexToLocalX(86)] = Direction.SOUTHEAST;
                if(h86 < closestDistance)
                {
                    closestDistance = h86;
                    closestIndex = 86;
                }
            }
            if(exists87 && dist98 + 1 < dist87)
            {
                dist87 = dist98 + 1;
                lookup[indexToLocalY(87)][indexToLocalX(87)] = Direction.SOUTH;
                if(h87 < closestDistance)
                {
                    closestDistance = h87;
                    closestIndex = 87;
                }
            }
            if(exists97 && dist98 + 1 < dist97)
            {
                dist97 = dist98 + 1;
                lookup[indexToLocalY(97)][indexToLocalX(97)] = Direction.EAST;
                if(h97 < closestDistance)
                {
                    closestDistance = h97;
                    closestIndex = 97;
                }
            }
            if(exists108 && dist98 + 1 < dist108)
            {
                dist108 = dist98 + 1;
                lookup[indexToLocalY(108)][indexToLocalX(108)] = Direction.NORTHEAST;
                if(h108 < closestDistance)
                {
                    closestDistance = h108;
                    closestIndex = 108;
                }
            }
            if(exists109 && dist98 + 1 < dist109)
            {
                dist109 = dist98 + 1;
                lookup[indexToLocalY(109)][indexToLocalX(109)] = Direction.NORTH;
                if(h109 < closestDistance)
                {
                    closestDistance = h109;
                    closestIndex = 109;
                }
            }
        }
        if(exists99)
        {
            if(exists88 && dist99 + 1 < dist88)
            {
                dist88 = dist99 + 1;
                lookup[indexToLocalY(88)][indexToLocalX(88)] = Direction.SOUTH;
                if(h88 < closestDistance)
                {
                    closestDistance = h88;
                    closestIndex = 88;
                }
            }
            if(exists89 && dist99 + 1 < dist89)
            {
                dist89 = dist99 + 1;
                lookup[indexToLocalY(89)][indexToLocalX(89)] = Direction.SOUTHWEST;
                if(h89 < closestDistance)
                {
                    closestDistance = h89;
                    closestIndex = 89;
                }
            }
            if(exists100 && dist99 + 1 < dist100)
            {
                dist100 = dist99 + 1;
                lookup[indexToLocalY(100)][indexToLocalX(100)] = Direction.WEST;
                if(h100 < closestDistance)
                {
                    closestDistance = h100;
                    closestIndex = 100;
                }
            }
            if(exists110 && dist99 + 1 < dist110)
            {
                dist110 = dist99 + 1;
                lookup[indexToLocalY(110)][indexToLocalX(110)] = Direction.NORTH;
                if(h110 < closestDistance)
                {
                    closestDistance = h110;
                    closestIndex = 110;
                }
            }
            if(exists111 && dist99 + 1 < dist111)
            {
                dist111 = dist99 + 1;
                lookup[indexToLocalY(111)][indexToLocalX(111)] = Direction.NORTHWEST;
                if(h111 < closestDistance)
                {
                    closestDistance = h111;
                    closestIndex = 111;
                }
            }
        }
        if(exists109)
        {
            if(exists97 && dist109 + 1 < dist97)
            {
                dist97 = dist109 + 1;
                lookup[indexToLocalY(97)][indexToLocalX(97)] = Direction.SOUTHEAST;
                if(h97 < closestDistance)
                {
                    closestDistance = h97;
                    closestIndex = 97;
                }
            }
            if(exists98 && dist109 + 1 < dist98)
            {
                dist98 = dist109 + 1;
                lookup[indexToLocalY(98)][indexToLocalX(98)] = Direction.SOUTH;
                if(h98 < closestDistance)
                {
                    closestDistance = h98;
                    closestIndex = 98;
                }
            }
            if(exists108 && dist109 + 1 < dist108)
            {
                dist108 = dist109 + 1;
                lookup[indexToLocalY(108)][indexToLocalX(108)] = Direction.EAST;
                if(h108 < closestDistance)
                {
                    closestDistance = h108;
                    closestIndex = 108;
                }
            }
            if(exists119 && dist109 + 1 < dist119)
            {
                dist119 = dist109 + 1;
                lookup[indexToLocalY(119)][indexToLocalX(119)] = Direction.NORTHEAST;
                if(h119 < closestDistance)
                {
                    closestDistance = h119;
                    closestIndex = 119;
                }
            }
            if(exists120 && dist109 + 1 < dist120)
            {
                dist120 = dist109 + 1;
                lookup[indexToLocalY(120)][indexToLocalX(120)] = Direction.NORTH;
                if(h120 < closestDistance)
                {
                    closestDistance = h120;
                    closestIndex = 120;
                }
            }
        }
        if(exists110)
        {
            if(exists99 && dist110 + 1 < dist99)
            {
                dist99 = dist110 + 1;
                lookup[indexToLocalY(99)][indexToLocalX(99)] = Direction.SOUTH;
                if(h99 < closestDistance)
                {
                    closestDistance = h99;
                    closestIndex = 99;
                }
            }
            if(exists100 && dist110 + 1 < dist100)
            {
                dist100 = dist110 + 1;
                lookup[indexToLocalY(100)][indexToLocalX(100)] = Direction.SOUTHWEST;
                if(h100 < closestDistance)
                {
                    closestDistance = h100;
                    closestIndex = 100;
                }
            }
            if(exists111 && dist110 + 1 < dist111)
            {
                dist111 = dist110 + 1;
                lookup[indexToLocalY(111)][indexToLocalX(111)] = Direction.WEST;
                if(h111 < closestDistance)
                {
                    closestDistance = h111;
                    closestIndex = 111;
                }
            }
        }
        if(exists111)
        {
            if(exists99 && dist111 + 1 < dist99)
            {
                dist99 = dist111 + 1;
                lookup[indexToLocalY(99)][indexToLocalX(99)] = Direction.SOUTHEAST;
                if(h99 < closestDistance)
                {
                    closestDistance = h99;
                    closestIndex = 99;
                }
            }
            if(exists100 && dist111 + 1 < dist100)
            {
                dist100 = dist111 + 1;
                lookup[indexToLocalY(100)][indexToLocalX(100)] = Direction.SOUTH;
                if(h100 < closestDistance)
                {
                    closestDistance = h100;
                    closestIndex = 100;
                }
            }
            if(exists101 && dist111 + 1 < dist101)
            {
                dist101 = dist111 + 1;
                lookup[indexToLocalY(101)][indexToLocalX(101)] = Direction.SOUTHWEST;
                if(h101 < closestDistance)
                {
                    closestDistance = h101;
                    closestIndex = 101;
                }
            }
            if(exists110 && dist111 + 1 < dist110)
            {
                dist110 = dist111 + 1;
                lookup[indexToLocalY(110)][indexToLocalX(110)] = Direction.EAST;
                if(h110 < closestDistance)
                {
                    closestDistance = h110;
                    closestIndex = 110;
                }
            }
            if(exists112 && dist111 + 1 < dist112)
            {
                dist112 = dist111 + 1;
                lookup[indexToLocalY(112)][indexToLocalX(112)] = Direction.WEST;
                if(h112 < closestDistance)
                {
                    closestDistance = h112;
                    closestIndex = 112;
                }
            }
        }
        if(exists112)
        {
            if(exists100 && dist112 + 1 < dist100)
            {
                dist100 = dist112 + 1;
                lookup[indexToLocalY(100)][indexToLocalX(100)] = Direction.SOUTHEAST;
                if(h100 < closestDistance)
                {
                    closestDistance = h100;
                    closestIndex = 100;
                }
            }
            if(exists101 && dist112 + 1 < dist101)
            {
                dist101 = dist112 + 1;
                lookup[indexToLocalY(101)][indexToLocalX(101)] = Direction.SOUTH;
                if(h101 < closestDistance)
                {
                    closestDistance = h101;
                    closestIndex = 101;
                }
            }
            if(exists102 && dist112 + 1 < dist102)
            {
                dist102 = dist112 + 1;
                lookup[indexToLocalY(102)][indexToLocalX(102)] = Direction.SOUTHWEST;
                if(h102 < closestDistance)
                {
                    closestDistance = h102;
                    closestIndex = 102;
                }
            }
            if(exists111 && dist112 + 1 < dist111)
            {
                dist111 = dist112 + 1;
                lookup[indexToLocalY(111)][indexToLocalX(111)] = Direction.EAST;
                if(h111 < closestDistance)
                {
                    closestDistance = h111;
                    closestIndex = 111;
                }
            }
            if(exists113 && dist112 + 1 < dist113)
            {
                dist113 = dist112 + 1;
                lookup[indexToLocalY(113)][indexToLocalX(113)] = Direction.WEST;
                if(h113 < closestDistance)
                {
                    closestDistance = h113;
                    closestIndex = 113;
                }
            }
        }
        if(exists113)
        {
            if(exists101 && dist113 + 1 < dist101)
            {
                dist101 = dist113 + 1;
                lookup[indexToLocalY(101)][indexToLocalX(101)] = Direction.SOUTHEAST;
                if(h101 < closestDistance)
                {
                    closestDistance = h101;
                    closestIndex = 101;
                }
            }
            if(exists102 && dist113 + 1 < dist102)
            {
                dist102 = dist113 + 1;
                lookup[indexToLocalY(102)][indexToLocalX(102)] = Direction.SOUTH;
                if(h102 < closestDistance)
                {
                    closestDistance = h102;
                    closestIndex = 102;
                }
            }
            if(exists103 && dist113 + 1 < dist103)
            {
                dist103 = dist113 + 1;
                lookup[indexToLocalY(103)][indexToLocalX(103)] = Direction.SOUTHWEST;
                if(h103 < closestDistance)
                {
                    closestDistance = h103;
                    closestIndex = 103;
                }
            }
            if(exists112 && dist113 + 1 < dist112)
            {
                dist112 = dist113 + 1;
                lookup[indexToLocalY(112)][indexToLocalX(112)] = Direction.EAST;
                if(h112 < closestDistance)
                {
                    closestDistance = h112;
                    closestIndex = 112;
                }
            }
            if(exists114 && dist113 + 1 < dist114)
            {
                dist114 = dist113 + 1;
                lookup[indexToLocalY(114)][indexToLocalX(114)] = Direction.WEST;
                if(h114 < closestDistance)
                {
                    closestDistance = h114;
                    closestIndex = 114;
                }
            }
        }
        if(exists114)
        {
            if(exists102 && dist114 + 1 < dist102)
            {
                dist102 = dist114 + 1;
                lookup[indexToLocalY(102)][indexToLocalX(102)] = Direction.SOUTHEAST;
                if(h102 < closestDistance)
                {
                    closestDistance = h102;
                    closestIndex = 102;
                }
            }
            if(exists103 && dist114 + 1 < dist103)
            {
                dist103 = dist114 + 1;
                lookup[indexToLocalY(103)][indexToLocalX(103)] = Direction.SOUTH;
                if(h103 < closestDistance)
                {
                    closestDistance = h103;
                    closestIndex = 103;
                }
            }
            if(exists104 && dist114 + 1 < dist104)
            {
                dist104 = dist114 + 1;
                lookup[indexToLocalY(104)][indexToLocalX(104)] = Direction.SOUTHWEST;
                if(h104 < closestDistance)
                {
                    closestDistance = h104;
                    closestIndex = 104;
                }
            }
            if(exists113 && dist114 + 1 < dist113)
            {
                dist113 = dist114 + 1;
                lookup[indexToLocalY(113)][indexToLocalX(113)] = Direction.EAST;
                if(h113 < closestDistance)
                {
                    closestDistance = h113;
                    closestIndex = 113;
                }
            }
            if(exists115 && dist114 + 1 < dist115)
            {
                dist115 = dist114 + 1;
                lookup[indexToLocalY(115)][indexToLocalX(115)] = Direction.WEST;
                if(h115 < closestDistance)
                {
                    closestDistance = h115;
                    closestIndex = 115;
                }
            }
        }
        if(exists115)
        {
            if(exists103 && dist115 + 1 < dist103)
            {
                dist103 = dist115 + 1;
                lookup[indexToLocalY(103)][indexToLocalX(103)] = Direction.SOUTHEAST;
                if(h103 < closestDistance)
                {
                    closestDistance = h103;
                    closestIndex = 103;
                }
            }
            if(exists104 && dist115 + 1 < dist104)
            {
                dist104 = dist115 + 1;
                lookup[indexToLocalY(104)][indexToLocalX(104)] = Direction.SOUTH;
                if(h104 < closestDistance)
                {
                    closestDistance = h104;
                    closestIndex = 104;
                }
            }
            if(exists105 && dist115 + 1 < dist105)
            {
                dist105 = dist115 + 1;
                lookup[indexToLocalY(105)][indexToLocalX(105)] = Direction.SOUTHWEST;
                if(h105 < closestDistance)
                {
                    closestDistance = h105;
                    closestIndex = 105;
                }
            }
            if(exists114 && dist115 + 1 < dist114)
            {
                dist114 = dist115 + 1;
                lookup[indexToLocalY(114)][indexToLocalX(114)] = Direction.EAST;
                if(h114 < closestDistance)
                {
                    closestDistance = h114;
                    closestIndex = 114;
                }
            }
            if(exists116 && dist115 + 1 < dist116)
            {
                dist116 = dist115 + 1;
                lookup[indexToLocalY(116)][indexToLocalX(116)] = Direction.WEST;
                if(h116 < closestDistance)
                {
                    closestDistance = h116;
                    closestIndex = 116;
                }
            }
        }
        if(exists116)
        {
            if(exists104 && dist116 + 1 < dist104)
            {
                dist104 = dist116 + 1;
                lookup[indexToLocalY(104)][indexToLocalX(104)] = Direction.SOUTHEAST;
                if(h104 < closestDistance)
                {
                    closestDistance = h104;
                    closestIndex = 104;
                }
            }
            if(exists105 && dist116 + 1 < dist105)
            {
                dist105 = dist116 + 1;
                lookup[indexToLocalY(105)][indexToLocalX(105)] = Direction.SOUTH;
                if(h105 < closestDistance)
                {
                    closestDistance = h105;
                    closestIndex = 105;
                }
            }
            if(exists106 && dist116 + 1 < dist106)
            {
                dist106 = dist116 + 1;
                lookup[indexToLocalY(106)][indexToLocalX(106)] = Direction.SOUTHWEST;
                if(h106 < closestDistance)
                {
                    closestDistance = h106;
                    closestIndex = 106;
                }
            }
            if(exists115 && dist116 + 1 < dist115)
            {
                dist115 = dist116 + 1;
                lookup[indexToLocalY(115)][indexToLocalX(115)] = Direction.EAST;
                if(h115 < closestDistance)
                {
                    closestDistance = h115;
                    closestIndex = 115;
                }
            }
            if(exists117 && dist116 + 1 < dist117)
            {
                dist117 = dist116 + 1;
                lookup[indexToLocalY(117)][indexToLocalX(117)] = Direction.WEST;
                if(h117 < closestDistance)
                {
                    closestDistance = h117;
                    closestIndex = 117;
                }
            }
        }
        if(exists117)
        {
            if(exists105 && dist117 + 1 < dist105)
            {
                dist105 = dist117 + 1;
                lookup[indexToLocalY(105)][indexToLocalX(105)] = Direction.SOUTHEAST;
                if(h105 < closestDistance)
                {
                    closestDistance = h105;
                    closestIndex = 105;
                }
            }
            if(exists106 && dist117 + 1 < dist106)
            {
                dist106 = dist117 + 1;
                lookup[indexToLocalY(106)][indexToLocalX(106)] = Direction.SOUTH;
                if(h106 < closestDistance)
                {
                    closestDistance = h106;
                    closestIndex = 106;
                }
            }
            if(exists107 && dist117 + 1 < dist107)
            {
                dist107 = dist117 + 1;
                lookup[indexToLocalY(107)][indexToLocalX(107)] = Direction.SOUTHWEST;
                if(h107 < closestDistance)
                {
                    closestDistance = h107;
                    closestIndex = 107;
                }
            }
            if(exists116 && dist117 + 1 < dist116)
            {
                dist116 = dist117 + 1;
                lookup[indexToLocalY(116)][indexToLocalX(116)] = Direction.EAST;
                if(h116 < closestDistance)
                {
                    closestDistance = h116;
                    closestIndex = 116;
                }
            }
            if(exists118 && dist117 + 1 < dist118)
            {
                dist118 = dist117 + 1;
                lookup[indexToLocalY(118)][indexToLocalX(118)] = Direction.WEST;
                if(h118 < closestDistance)
                {
                    closestDistance = h118;
                    closestIndex = 118;
                }
            }
        }
        if(exists118)
        {
            if(exists106 && dist118 + 1 < dist106)
            {
                dist106 = dist118 + 1;
                lookup[indexToLocalY(106)][indexToLocalX(106)] = Direction.SOUTHEAST;
                if(h106 < closestDistance)
                {
                    closestDistance = h106;
                    closestIndex = 106;
                }
            }
            if(exists107 && dist118 + 1 < dist107)
            {
                dist107 = dist118 + 1;
                lookup[indexToLocalY(107)][indexToLocalX(107)] = Direction.SOUTH;
                if(h107 < closestDistance)
                {
                    closestDistance = h107;
                    closestIndex = 107;
                }
            }
            if(exists108 && dist118 + 1 < dist108)
            {
                dist108 = dist118 + 1;
                lookup[indexToLocalY(108)][indexToLocalX(108)] = Direction.SOUTHWEST;
                if(h108 < closestDistance)
                {
                    closestDistance = h108;
                    closestIndex = 108;
                }
            }
            if(exists117 && dist118 + 1 < dist117)
            {
                dist117 = dist118 + 1;
                lookup[indexToLocalY(117)][indexToLocalX(117)] = Direction.EAST;
                if(h117 < closestDistance)
                {
                    closestDistance = h117;
                    closestIndex = 117;
                }
            }
            if(exists119 && dist118 + 1 < dist119)
            {
                dist119 = dist118 + 1;
                lookup[indexToLocalY(119)][indexToLocalX(119)] = Direction.WEST;
                if(h119 < closestDistance)
                {
                    closestDistance = h119;
                    closestIndex = 119;
                }
            }
        }
        if(exists119)
        {
            if(exists107 && dist119 + 1 < dist107)
            {
                dist107 = dist119 + 1;
                lookup[indexToLocalY(107)][indexToLocalX(107)] = Direction.SOUTHEAST;
                if(h107 < closestDistance)
                {
                    closestDistance = h107;
                    closestIndex = 107;
                }
            }
            if(exists108 && dist119 + 1 < dist108)
            {
                dist108 = dist119 + 1;
                lookup[indexToLocalY(108)][indexToLocalX(108)] = Direction.SOUTH;
                if(h108 < closestDistance)
                {
                    closestDistance = h108;
                    closestIndex = 108;
                }
            }
            if(exists109 && dist119 + 1 < dist109)
            {
                dist109 = dist119 + 1;
                lookup[indexToLocalY(109)][indexToLocalX(109)] = Direction.SOUTHWEST;
                if(h109 < closestDistance)
                {
                    closestDistance = h109;
                    closestIndex = 109;
                }
            }
            if(exists118 && dist119 + 1 < dist118)
            {
                dist118 = dist119 + 1;
                lookup[indexToLocalY(118)][indexToLocalX(118)] = Direction.EAST;
                if(h118 < closestDistance)
                {
                    closestDistance = h118;
                    closestIndex = 118;
                }
            }
            if(exists120 && dist119 + 1 < dist120)
            {
                dist120 = dist119 + 1;
                lookup[indexToLocalY(120)][indexToLocalX(120)] = Direction.WEST;
                if(h120 < closestDistance)
                {
                    closestDistance = h120;
                    closestIndex = 120;
                }
            }
        }
        if(exists120)
        {
            if(exists108 && dist120 + 1 < dist108)
            {
                dist108 = dist120 + 1;
                lookup[indexToLocalY(108)][indexToLocalX(108)] = Direction.SOUTHEAST;
                if(h108 < closestDistance)
                {
                    closestDistance = h108;
                    closestIndex = 108;
                }
            }
            if(exists109 && dist120 + 1 < dist109)
            {
                dist109 = dist120 + 1;
                lookup[indexToLocalY(109)][indexToLocalX(109)] = Direction.SOUTH;
                if(h109 < closestDistance)
                {
                    closestDistance = h109;
                    closestIndex = 109;
                }
            }
            if(exists119 && dist120 + 1 < dist119)
            {
                dist119 = dist120 + 1;
                lookup[indexToLocalY(119)][indexToLocalX(119)] = Direction.EAST;
                if(h119 < closestDistance)
                {
                    closestDistance = h119;
                    closestIndex = 119;
                }
            }
        }

        switch(closestIndex)
        {
            case 0:
            {
                Direction dirToMove = dirToMove(0);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 1:
            {
                Direction dirToMove = dirToMove(1);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 2:
            {
                Direction dirToMove = dirToMove(2);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 3:
            {
                Direction dirToMove = dirToMove(3);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 4:
            {
                Direction dirToMove = dirToMove(4);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 5:
            {
                Direction dirToMove = dirToMove(5);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 6:
            {
                Direction dirToMove = dirToMove(6);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 7:
            {
                Direction dirToMove = dirToMove(7);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 8:
            {
                Direction dirToMove = dirToMove(8);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 9:
            {
                Direction dirToMove = dirToMove(9);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 10:
            {
                Direction dirToMove = dirToMove(10);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 11:
            {
                Direction dirToMove = dirToMove(11);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 12:
            {
                Direction dirToMove = dirToMove(12);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 13:
            {
                Direction dirToMove = dirToMove(13);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 14:
            {
                Direction dirToMove = dirToMove(14);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 15:
            {
                Direction dirToMove = dirToMove(15);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 16:
            {
                Direction dirToMove = dirToMove(16);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 17:
            {
                Direction dirToMove = dirToMove(17);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 18:
            {
                Direction dirToMove = dirToMove(18);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 19:
            {
                Direction dirToMove = dirToMove(19);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 20:
            {
                Direction dirToMove = dirToMove(20);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 21:
            {
                Direction dirToMove = dirToMove(21);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 22:
            {
                Direction dirToMove = dirToMove(22);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 23:
            {
                Direction dirToMove = dirToMove(23);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 24:
            {
                Direction dirToMove = dirToMove(24);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 25:
            {
                Direction dirToMove = dirToMove(25);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 26:
            {
                Direction dirToMove = dirToMove(26);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 27:
            {
                Direction dirToMove = dirToMove(27);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 28:
            {
                Direction dirToMove = dirToMove(28);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 29:
            {
                Direction dirToMove = dirToMove(29);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 30:
            {
                Direction dirToMove = dirToMove(30);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 31:
            {
                Direction dirToMove = dirToMove(31);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 32:
            {
                Direction dirToMove = dirToMove(32);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 33:
            {
                Direction dirToMove = dirToMove(33);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 34:
            {
                Direction dirToMove = dirToMove(34);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 35:
            {
                Direction dirToMove = dirToMove(35);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 36:
            {
                Direction dirToMove = dirToMove(36);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 37:
            {
                Direction dirToMove = dirToMove(37);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 38:
            {
                Direction dirToMove = dirToMove(38);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 39:
            {
                Direction dirToMove = dirToMove(39);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 40:
            {
                Direction dirToMove = dirToMove(40);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 41:
            {
                Direction dirToMove = dirToMove(41);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 42:
            {
                Direction dirToMove = dirToMove(42);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 43:
            {
                Direction dirToMove = dirToMove(43);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 44:
            {
                Direction dirToMove = dirToMove(44);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 45:
            {
                Direction dirToMove = dirToMove(45);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 46:
            {
                Direction dirToMove = dirToMove(46);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 47:
            {
                Direction dirToMove = dirToMove(47);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 48:
            {
                Direction dirToMove = dirToMove(48);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 49:
            {
                Direction dirToMove = dirToMove(49);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 50:
            {
                Direction dirToMove = dirToMove(50);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 51:
            {
                Direction dirToMove = dirToMove(51);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 52:
            {
                Direction dirToMove = dirToMove(52);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 53:
            {
                Direction dirToMove = dirToMove(53);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 54:
            {
                Direction dirToMove = dirToMove(54);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 55:
            {
                Direction dirToMove = dirToMove(55);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 56:
            {
                Direction dirToMove = dirToMove(56);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 57:
            {
                Direction dirToMove = dirToMove(57);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 58:
            {
                Direction dirToMove = dirToMove(58);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 59:
            {
                Direction dirToMove = dirToMove(59);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 60:
            {
                Direction dirToMove = dirToMove(60);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 61:
            {
                Direction dirToMove = dirToMove(61);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 62:
            {
                Direction dirToMove = dirToMove(62);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 63:
            {
                Direction dirToMove = dirToMove(63);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 64:
            {
                Direction dirToMove = dirToMove(64);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 65:
            {
                Direction dirToMove = dirToMove(65);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 66:
            {
                Direction dirToMove = dirToMove(66);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 67:
            {
                Direction dirToMove = dirToMove(67);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 68:
            {
                Direction dirToMove = dirToMove(68);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 69:
            {
                Direction dirToMove = dirToMove(69);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 70:
            {
                Direction dirToMove = dirToMove(70);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 71:
            {
                Direction dirToMove = dirToMove(71);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 72:
            {
                Direction dirToMove = dirToMove(72);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 73:
            {
                Direction dirToMove = dirToMove(73);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 74:
            {
                Direction dirToMove = dirToMove(74);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 75:
            {
                Direction dirToMove = dirToMove(75);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 76:
            {
                Direction dirToMove = dirToMove(76);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 77:
            {
                Direction dirToMove = dirToMove(77);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 78:
            {
                Direction dirToMove = dirToMove(78);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 79:
            {
                Direction dirToMove = dirToMove(79);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 80:
            {
                Direction dirToMove = dirToMove(80);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 81:
            {
                Direction dirToMove = dirToMove(81);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 82:
            {
                Direction dirToMove = dirToMove(82);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 83:
            {
                Direction dirToMove = dirToMove(83);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 84:
            {
                Direction dirToMove = dirToMove(84);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 85:
            {
                Direction dirToMove = dirToMove(85);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 86:
            {
                Direction dirToMove = dirToMove(86);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 87:
            {
                Direction dirToMove = dirToMove(87);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 88:
            {
                Direction dirToMove = dirToMove(88);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 89:
            {
                Direction dirToMove = dirToMove(89);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 90:
            {
                Direction dirToMove = dirToMove(90);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 91:
            {
                Direction dirToMove = dirToMove(91);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 92:
            {
                Direction dirToMove = dirToMove(92);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 93:
            {
                Direction dirToMove = dirToMove(93);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 94:
            {
                Direction dirToMove = dirToMove(94);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 95:
            {
                Direction dirToMove = dirToMove(95);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 96:
            {
                Direction dirToMove = dirToMove(96);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 97:
            {
                Direction dirToMove = dirToMove(97);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 98:
            {
                Direction dirToMove = dirToMove(98);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 99:
            {
                Direction dirToMove = dirToMove(99);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 100:
            {
                Direction dirToMove = dirToMove(100);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 101:
            {
                Direction dirToMove = dirToMove(101);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 102:
            {
                Direction dirToMove = dirToMove(102);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 103:
            {
                Direction dirToMove = dirToMove(103);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 104:
            {
                Direction dirToMove = dirToMove(104);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 105:
            {
                Direction dirToMove = dirToMove(105);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 106:
            {
                Direction dirToMove = dirToMove(106);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 107:
            {
                Direction dirToMove = dirToMove(107);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 108:
            {
                Direction dirToMove = dirToMove(108);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 109:
            {
                Direction dirToMove = dirToMove(109);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 110:
            {
                Direction dirToMove = dirToMove(110);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 111:
            {
                Direction dirToMove = dirToMove(111);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 112:
            {
                Direction dirToMove = dirToMove(112);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 113:
            {
                Direction dirToMove = dirToMove(113);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 114:
            {
                Direction dirToMove = dirToMove(114);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 115:
            {
                Direction dirToMove = dirToMove(115);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 116:
            {
                Direction dirToMove = dirToMove(116);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 117:
            {
                Direction dirToMove = dirToMove(117);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 118:
            {
                Direction dirToMove = dirToMove(118);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 119:
            {
                Direction dirToMove = dirToMove(119);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

            case 120:
            {
                Direction dirToMove = dirToMove(120);
                if(rc.canMove(dirToMove))
                {
                    rc.move(dirToMove);
                }
                break;
            }

        }
    }

    private static void method4()
    {
        if(exists56)
        {
            if(exists44 && dist56 + 1 < dist44)
            {
                dist44 = dist56 + 1;
                lookup[indexToLocalY(44)][indexToLocalX(44)] = Direction.SOUTHEAST;
                if(h44 < closestDistance)
                {
                    closestDistance = h44;
                    closestIndex = 44;
                }
            }
            if(exists45 && dist56 + 1 < dist45)
            {
                dist45 = dist56 + 1;
                lookup[indexToLocalY(45)][indexToLocalX(45)] = Direction.SOUTH;
                if(h45 < closestDistance)
                {
                    closestDistance = h45;
                    closestIndex = 45;
                }
            }
            if(exists46 && dist56 + 1 < dist46)
            {
                dist46 = dist56 + 1;
                lookup[indexToLocalY(46)][indexToLocalX(46)] = Direction.SOUTHWEST;
                if(h46 < closestDistance)
                {
                    closestDistance = h46;
                    closestIndex = 46;
                }
            }
            if(exists55 && dist56 + 1 < dist55)
            {
                dist55 = dist56 + 1;
                lookup[indexToLocalY(55)][indexToLocalX(55)] = Direction.EAST;
                if(h55 < closestDistance)
                {
                    closestDistance = h55;
                    closestIndex = 55;
                }
            }
            if(exists57 && dist56 + 1 < dist57)
            {
                dist57 = dist56 + 1;
                lookup[indexToLocalY(57)][indexToLocalX(57)] = Direction.WEST;
                if(h57 < closestDistance)
                {
                    closestDistance = h57;
                    closestIndex = 57;
                }
            }
            if(exists66 && dist56 + 1 < dist66)
            {
                dist66 = dist56 + 1;
                lookup[indexToLocalY(66)][indexToLocalX(66)] = Direction.NORTHEAST;
                if(h66 < closestDistance)
                {
                    closestDistance = h66;
                    closestIndex = 66;
                }
            }
            if(exists67 && dist56 + 1 < dist67)
            {
                dist67 = dist56 + 1;
                lookup[indexToLocalY(67)][indexToLocalX(67)] = Direction.NORTH;
                if(h67 < closestDistance)
                {
                    closestDistance = h67;
                    closestIndex = 67;
                }
            }
            if(exists68 && dist56 + 1 < dist68)
            {
                dist68 = dist56 + 1;
                lookup[indexToLocalY(68)][indexToLocalX(68)] = Direction.NORTHWEST;
                if(h68 < closestDistance)
                {
                    closestDistance = h68;
                    closestIndex = 68;
                }
            }
        }
        if(exists17)
        {
            if(exists5 && dist17 + 1 < dist5)
            {
                dist5 = dist17 + 1;
                lookup[indexToLocalY(5)][indexToLocalX(5)] = Direction.SOUTHEAST;
                if(h5 < closestDistance)
                {
                    closestDistance = h5;
                    closestIndex = 5;
                }
            }
            if(exists6 && dist17 + 1 < dist6)
            {
                dist6 = dist17 + 1;
                lookup[indexToLocalY(6)][indexToLocalX(6)] = Direction.SOUTH;
                if(h6 < closestDistance)
                {
                    closestDistance = h6;
                    closestIndex = 6;
                }
            }
            if(exists7 && dist17 + 1 < dist7)
            {
                dist7 = dist17 + 1;
                lookup[indexToLocalY(7)][indexToLocalX(7)] = Direction.SOUTHWEST;
                if(h7 < closestDistance)
                {
                    closestDistance = h7;
                    closestIndex = 7;
                }
            }
            if(exists16 && dist17 + 1 < dist16)
            {
                dist16 = dist17 + 1;
                lookup[indexToLocalY(16)][indexToLocalX(16)] = Direction.EAST;
                if(h16 < closestDistance)
                {
                    closestDistance = h16;
                    closestIndex = 16;
                }
            }
            if(exists18 && dist17 + 1 < dist18)
            {
                dist18 = dist17 + 1;
                lookup[indexToLocalY(18)][indexToLocalX(18)] = Direction.WEST;
                if(h18 < closestDistance)
                {
                    closestDistance = h18;
                    closestIndex = 18;
                }
            }
            if(exists27 && dist17 + 1 < dist27)
            {
                dist27 = dist17 + 1;
                lookup[indexToLocalY(27)][indexToLocalX(27)] = Direction.NORTHEAST;
                if(h27 < closestDistance)
                {
                    closestDistance = h27;
                    closestIndex = 27;
                }
            }
            if(exists28 && dist17 + 1 < dist28)
            {
                dist28 = dist17 + 1;
                lookup[indexToLocalY(28)][indexToLocalX(28)] = Direction.NORTH;
                if(h28 < closestDistance)
                {
                    closestDistance = h28;
                    closestIndex = 28;
                }
            }
            if(exists29 && dist17 + 1 < dist29)
            {
                dist29 = dist17 + 1;
                lookup[indexToLocalY(29)][indexToLocalX(29)] = Direction.NORTHWEST;
                if(h29 < closestDistance)
                {
                    closestDistance = h29;
                    closestIndex = 29;
                }
            }
        }
        if(exists18)
        {
            if(exists6 && dist18 + 1 < dist6)
            {
                dist6 = dist18 + 1;
                lookup[indexToLocalY(6)][indexToLocalX(6)] = Direction.SOUTHEAST;
                if(h6 < closestDistance)
                {
                    closestDistance = h6;
                    closestIndex = 6;
                }
            }
            if(exists7 && dist18 + 1 < dist7)
            {
                dist7 = dist18 + 1;
                lookup[indexToLocalY(7)][indexToLocalX(7)] = Direction.SOUTH;
                if(h7 < closestDistance)
                {
                    closestDistance = h7;
                    closestIndex = 7;
                }
            }
            if(exists8 && dist18 + 1 < dist8)
            {
                dist8 = dist18 + 1;
                lookup[indexToLocalY(8)][indexToLocalX(8)] = Direction.SOUTHWEST;
                if(h8 < closestDistance)
                {
                    closestDistance = h8;
                    closestIndex = 8;
                }
            }
            if(exists17 && dist18 + 1 < dist17)
            {
                dist17 = dist18 + 1;
                lookup[indexToLocalY(17)][indexToLocalX(17)] = Direction.EAST;
                if(h17 < closestDistance)
                {
                    closestDistance = h17;
                    closestIndex = 17;
                }
            }
            if(exists19 && dist18 + 1 < dist19)
            {
                dist19 = dist18 + 1;
                lookup[indexToLocalY(19)][indexToLocalX(19)] = Direction.WEST;
                if(h19 < closestDistance)
                {
                    closestDistance = h19;
                    closestIndex = 19;
                }
            }
            if(exists28 && dist18 + 1 < dist28)
            {
                dist28 = dist18 + 1;
                lookup[indexToLocalY(28)][indexToLocalX(28)] = Direction.NORTHEAST;
                if(h28 < closestDistance)
                {
                    closestDistance = h28;
                    closestIndex = 28;
                }
            }
            if(exists29 && dist18 + 1 < dist29)
            {
                dist29 = dist18 + 1;
                lookup[indexToLocalY(29)][indexToLocalX(29)] = Direction.NORTH;
                if(h29 < closestDistance)
                {
                    closestDistance = h29;
                    closestIndex = 29;
                }
            }
            if(exists30 && dist18 + 1 < dist30)
            {
                dist30 = dist18 + 1;
                lookup[indexToLocalY(30)][indexToLocalX(30)] = Direction.NORTHWEST;
                if(h30 < closestDistance)
                {
                    closestDistance = h30;
                    closestIndex = 30;
                }
            }
        }
        if(exists67)
        {
            if(exists55 && dist67 + 1 < dist55)
            {
                dist55 = dist67 + 1;
                lookup[indexToLocalY(55)][indexToLocalX(55)] = Direction.SOUTHEAST;
                if(h55 < closestDistance)
                {
                    closestDistance = h55;
                    closestIndex = 55;
                }
            }
            if(exists56 && dist67 + 1 < dist56)
            {
                dist56 = dist67 + 1;
                lookup[indexToLocalY(56)][indexToLocalX(56)] = Direction.SOUTH;
                if(h56 < closestDistance)
                {
                    closestDistance = h56;
                    closestIndex = 56;
                }
            }
            if(exists57 && dist67 + 1 < dist57)
            {
                dist57 = dist67 + 1;
                lookup[indexToLocalY(57)][indexToLocalX(57)] = Direction.SOUTHWEST;
                if(h57 < closestDistance)
                {
                    closestDistance = h57;
                    closestIndex = 57;
                }
            }
            if(exists66 && dist67 + 1 < dist66)
            {
                dist66 = dist67 + 1;
                lookup[indexToLocalY(66)][indexToLocalX(66)] = Direction.EAST;
                if(h66 < closestDistance)
                {
                    closestDistance = h66;
                    closestIndex = 66;
                }
            }
            if(exists68 && dist67 + 1 < dist68)
            {
                dist68 = dist67 + 1;
                lookup[indexToLocalY(68)][indexToLocalX(68)] = Direction.WEST;
                if(h68 < closestDistance)
                {
                    closestDistance = h68;
                    closestIndex = 68;
                }
            }
            if(exists77 && dist67 + 1 < dist77)
            {
                dist77 = dist67 + 1;
                lookup[indexToLocalY(77)][indexToLocalX(77)] = Direction.NORTHEAST;
                if(h77 < closestDistance)
                {
                    closestDistance = h77;
                    closestIndex = 77;
                }
            }
            if(exists78 && dist67 + 1 < dist78)
            {
                dist78 = dist67 + 1;
                lookup[indexToLocalY(78)][indexToLocalX(78)] = Direction.NORTH;
                if(h78 < closestDistance)
                {
                    closestDistance = h78;
                    closestIndex = 78;
                }
            }
            if(exists79 && dist67 + 1 < dist79)
            {
                dist79 = dist67 + 1;
                lookup[indexToLocalY(79)][indexToLocalX(79)] = Direction.NORTHWEST;
                if(h79 < closestDistance)
                {
                    closestDistance = h79;
                    closestIndex = 79;
                }
            }
        }
        if(exists78)
        {
            if(exists66 && dist78 + 1 < dist66)
            {
                dist66 = dist78 + 1;
                lookup[indexToLocalY(66)][indexToLocalX(66)] = Direction.SOUTHEAST;
                if(h66 < closestDistance)
                {
                    closestDistance = h66;
                    closestIndex = 66;
                }
            }
            if(exists67 && dist78 + 1 < dist67)
            {
                dist67 = dist78 + 1;
                lookup[indexToLocalY(67)][indexToLocalX(67)] = Direction.SOUTH;
                if(h67 < closestDistance)
                {
                    closestDistance = h67;
                    closestIndex = 67;
                }
            }
            if(exists68 && dist78 + 1 < dist68)
            {
                dist68 = dist78 + 1;
                lookup[indexToLocalY(68)][indexToLocalX(68)] = Direction.SOUTHWEST;
                if(h68 < closestDistance)
                {
                    closestDistance = h68;
                    closestIndex = 68;
                }
            }
            if(exists77 && dist78 + 1 < dist77)
            {
                dist77 = dist78 + 1;
                lookup[indexToLocalY(77)][indexToLocalX(77)] = Direction.EAST;
                if(h77 < closestDistance)
                {
                    closestDistance = h77;
                    closestIndex = 77;
                }
            }
            if(exists79 && dist78 + 1 < dist79)
            {
                dist79 = dist78 + 1;
                lookup[indexToLocalY(79)][indexToLocalX(79)] = Direction.WEST;
                if(h79 < closestDistance)
                {
                    closestDistance = h79;
                    closestIndex = 79;
                }
            }
            if(exists88 && dist78 + 1 < dist88)
            {
                dist88 = dist78 + 1;
                lookup[indexToLocalY(88)][indexToLocalX(88)] = Direction.NORTHEAST;
                if(h88 < closestDistance)
                {
                    closestDistance = h88;
                    closestIndex = 88;
                }
            }
            if(exists89 && dist78 + 1 < dist89)
            {
                dist89 = dist78 + 1;
                lookup[indexToLocalY(89)][indexToLocalX(89)] = Direction.NORTH;
                if(h89 < closestDistance)
                {
                    closestDistance = h89;
                    closestIndex = 89;
                }
            }
            if(exists90 && dist78 + 1 < dist90)
            {
                dist90 = dist78 + 1;
                lookup[indexToLocalY(90)][indexToLocalX(90)] = Direction.NORTHWEST;
                if(h90 < closestDistance)
                {
                    closestDistance = h90;
                    closestIndex = 90;
                }
            }
        }
        if(exists19)
        {
            if(exists7 && dist19 + 1 < dist7)
            {
                dist7 = dist19 + 1;
                lookup[indexToLocalY(7)][indexToLocalX(7)] = Direction.SOUTHEAST;
                if(h7 < closestDistance)
                {
                    closestDistance = h7;
                    closestIndex = 7;
                }
            }
            if(exists8 && dist19 + 1 < dist8)
            {
                dist8 = dist19 + 1;
                lookup[indexToLocalY(8)][indexToLocalX(8)] = Direction.SOUTH;
                if(h8 < closestDistance)
                {
                    closestDistance = h8;
                    closestIndex = 8;
                }
            }
            if(exists9 && dist19 + 1 < dist9)
            {
                dist9 = dist19 + 1;
                lookup[indexToLocalY(9)][indexToLocalX(9)] = Direction.SOUTHWEST;
                if(h9 < closestDistance)
                {
                    closestDistance = h9;
                    closestIndex = 9;
                }
            }
            if(exists18 && dist19 + 1 < dist18)
            {
                dist18 = dist19 + 1;
                lookup[indexToLocalY(18)][indexToLocalX(18)] = Direction.EAST;
                if(h18 < closestDistance)
                {
                    closestDistance = h18;
                    closestIndex = 18;
                }
            }
            if(exists20 && dist19 + 1 < dist20)
            {
                dist20 = dist19 + 1;
                lookup[indexToLocalY(20)][indexToLocalX(20)] = Direction.WEST;
                if(h20 < closestDistance)
                {
                    closestDistance = h20;
                    closestIndex = 20;
                }
            }
            if(exists29 && dist19 + 1 < dist29)
            {
                dist29 = dist19 + 1;
                lookup[indexToLocalY(29)][indexToLocalX(29)] = Direction.NORTHEAST;
                if(h29 < closestDistance)
                {
                    closestDistance = h29;
                    closestIndex = 29;
                }
            }
            if(exists30 && dist19 + 1 < dist30)
            {
                dist30 = dist19 + 1;
                lookup[indexToLocalY(30)][indexToLocalX(30)] = Direction.NORTH;
                if(h30 < closestDistance)
                {
                    closestDistance = h30;
                    closestIndex = 30;
                }
            }
            if(exists31 && dist19 + 1 < dist31)
            {
                dist31 = dist19 + 1;
                lookup[indexToLocalY(31)][indexToLocalX(31)] = Direction.NORTHWEST;
                if(h31 < closestDistance)
                {
                    closestDistance = h31;
                    closestIndex = 31;
                }
            }
        }
        if(exists20)
        {
            if(exists8 && dist20 + 1 < dist8)
            {
                dist8 = dist20 + 1;
                lookup[indexToLocalY(8)][indexToLocalX(8)] = Direction.SOUTHEAST;
                if(h8 < closestDistance)
                {
                    closestDistance = h8;
                    closestIndex = 8;
                }
            }
            if(exists9 && dist20 + 1 < dist9)
            {
                dist9 = dist20 + 1;
                lookup[indexToLocalY(9)][indexToLocalX(9)] = Direction.SOUTH;
                if(h9 < closestDistance)
                {
                    closestDistance = h9;
                    closestIndex = 9;
                }
            }
            if(exists10 && dist20 + 1 < dist10)
            {
                dist10 = dist20 + 1;
                lookup[indexToLocalY(10)][indexToLocalX(10)] = Direction.SOUTHWEST;
                if(h10 < closestDistance)
                {
                    closestDistance = h10;
                    closestIndex = 10;
                }
            }
            if(exists19 && dist20 + 1 < dist19)
            {
                dist19 = dist20 + 1;
                lookup[indexToLocalY(19)][indexToLocalX(19)] = Direction.EAST;
                if(h19 < closestDistance)
                {
                    closestDistance = h19;
                    closestIndex = 19;
                }
            }
            if(exists21 && dist20 + 1 < dist21)
            {
                dist21 = dist20 + 1;
                lookup[indexToLocalY(21)][indexToLocalX(21)] = Direction.WEST;
                if(h21 < closestDistance)
                {
                    closestDistance = h21;
                    closestIndex = 21;
                }
            }
            if(exists30 && dist20 + 1 < dist30)
            {
                dist30 = dist20 + 1;
                lookup[indexToLocalY(30)][indexToLocalX(30)] = Direction.NORTHEAST;
                if(h30 < closestDistance)
                {
                    closestDistance = h30;
                    closestIndex = 30;
                }
            }
            if(exists31 && dist20 + 1 < dist31)
            {
                dist31 = dist20 + 1;
                lookup[indexToLocalY(31)][indexToLocalX(31)] = Direction.NORTH;
                if(h31 < closestDistance)
                {
                    closestDistance = h31;
                    closestIndex = 31;
                }
            }
            if(exists32 && dist20 + 1 < dist32)
            {
                dist32 = dist20 + 1;
                lookup[indexToLocalY(32)][indexToLocalX(32)] = Direction.NORTHWEST;
                if(h32 < closestDistance)
                {
                    closestDistance = h32;
                    closestIndex = 32;
                }
            }
        }
        if(exists31)
        {
            if(exists19 && dist31 + 1 < dist19)
            {
                dist19 = dist31 + 1;
                lookup[indexToLocalY(19)][indexToLocalX(19)] = Direction.SOUTHEAST;
                if(h19 < closestDistance)
                {
                    closestDistance = h19;
                    closestIndex = 19;
                }
            }
            if(exists20 && dist31 + 1 < dist20)
            {
                dist20 = dist31 + 1;
                lookup[indexToLocalY(20)][indexToLocalX(20)] = Direction.SOUTH;
                if(h20 < closestDistance)
                {
                    closestDistance = h20;
                    closestIndex = 20;
                }
            }
            if(exists21 && dist31 + 1 < dist21)
            {
                dist21 = dist31 + 1;
                lookup[indexToLocalY(21)][indexToLocalX(21)] = Direction.SOUTHWEST;
                if(h21 < closestDistance)
                {
                    closestDistance = h21;
                    closestIndex = 21;
                }
            }
            if(exists30 && dist31 + 1 < dist30)
            {
                dist30 = dist31 + 1;
                lookup[indexToLocalY(30)][indexToLocalX(30)] = Direction.EAST;
                if(h30 < closestDistance)
                {
                    closestDistance = h30;
                    closestIndex = 30;
                }
            }
            if(exists32 && dist31 + 1 < dist32)
            {
                dist32 = dist31 + 1;
                lookup[indexToLocalY(32)][indexToLocalX(32)] = Direction.WEST;
                if(h32 < closestDistance)
                {
                    closestDistance = h32;
                    closestIndex = 32;
                }
            }
            if(exists41 && dist31 + 1 < dist41)
            {
                dist41 = dist31 + 1;
                lookup[indexToLocalY(41)][indexToLocalX(41)] = Direction.NORTHEAST;
                if(h41 < closestDistance)
                {
                    closestDistance = h41;
                    closestIndex = 41;
                }
            }
            if(exists42 && dist31 + 1 < dist42)
            {
                dist42 = dist31 + 1;
                lookup[indexToLocalY(42)][indexToLocalX(42)] = Direction.NORTH;
                if(h42 < closestDistance)
                {
                    closestDistance = h42;
                    closestIndex = 42;
                }
            }
            if(exists43 && dist31 + 1 < dist43)
            {
                dist43 = dist31 + 1;
                lookup[indexToLocalY(43)][indexToLocalX(43)] = Direction.NORTHWEST;
                if(h43 < closestDistance)
                {
                    closestDistance = h43;
                    closestIndex = 43;
                }
            }
        }
        if(exists42)
        {
            if(exists30 && dist42 + 1 < dist30)
            {
                dist30 = dist42 + 1;
                lookup[indexToLocalY(30)][indexToLocalX(30)] = Direction.SOUTHEAST;
                if(h30 < closestDistance)
                {
                    closestDistance = h30;
                    closestIndex = 30;
                }
            }
            if(exists31 && dist42 + 1 < dist31)
            {
                dist31 = dist42 + 1;
                lookup[indexToLocalY(31)][indexToLocalX(31)] = Direction.SOUTH;
                if(h31 < closestDistance)
                {
                    closestDistance = h31;
                    closestIndex = 31;
                }
            }
            if(exists32 && dist42 + 1 < dist32)
            {
                dist32 = dist42 + 1;
                lookup[indexToLocalY(32)][indexToLocalX(32)] = Direction.SOUTHWEST;
                if(h32 < closestDistance)
                {
                    closestDistance = h32;
                    closestIndex = 32;
                }
            }
            if(exists41 && dist42 + 1 < dist41)
            {
                dist41 = dist42 + 1;
                lookup[indexToLocalY(41)][indexToLocalX(41)] = Direction.EAST;
                if(h41 < closestDistance)
                {
                    closestDistance = h41;
                    closestIndex = 41;
                }
            }
            if(exists43 && dist42 + 1 < dist43)
            {
                dist43 = dist42 + 1;
                lookup[indexToLocalY(43)][indexToLocalX(43)] = Direction.WEST;
                if(h43 < closestDistance)
                {
                    closestDistance = h43;
                    closestIndex = 43;
                }
            }
            if(exists52 && dist42 + 1 < dist52)
            {
                dist52 = dist42 + 1;
                lookup[indexToLocalY(52)][indexToLocalX(52)] = Direction.NORTHEAST;
                if(h52 < closestDistance)
                {
                    closestDistance = h52;
                    closestIndex = 52;
                }
            }
            if(exists53 && dist42 + 1 < dist53)
            {
                dist53 = dist42 + 1;
                lookup[indexToLocalY(53)][indexToLocalX(53)] = Direction.NORTH;
                if(h53 < closestDistance)
                {
                    closestDistance = h53;
                    closestIndex = 53;
                }
            }
            if(exists54 && dist42 + 1 < dist54)
            {
                dist54 = dist42 + 1;
                lookup[indexToLocalY(54)][indexToLocalX(54)] = Direction.NORTHWEST;
                if(h54 < closestDistance)
                {
                    closestDistance = h54;
                    closestIndex = 54;
                }
            }
        }
        if(exists53)
        {
            if(exists41 && dist53 + 1 < dist41)
            {
                dist41 = dist53 + 1;
                lookup[indexToLocalY(41)][indexToLocalX(41)] = Direction.SOUTHEAST;
                if(h41 < closestDistance)
                {
                    closestDistance = h41;
                    closestIndex = 41;
                }
            }
            if(exists42 && dist53 + 1 < dist42)
            {
                dist42 = dist53 + 1;
                lookup[indexToLocalY(42)][indexToLocalX(42)] = Direction.SOUTH;
                if(h42 < closestDistance)
                {
                    closestDistance = h42;
                    closestIndex = 42;
                }
            }
            if(exists43 && dist53 + 1 < dist43)
            {
                dist43 = dist53 + 1;
                lookup[indexToLocalY(43)][indexToLocalX(43)] = Direction.SOUTHWEST;
                if(h43 < closestDistance)
                {
                    closestDistance = h43;
                    closestIndex = 43;
                }
            }
            if(exists52 && dist53 + 1 < dist52)
            {
                dist52 = dist53 + 1;
                lookup[indexToLocalY(52)][indexToLocalX(52)] = Direction.EAST;
                if(h52 < closestDistance)
                {
                    closestDistance = h52;
                    closestIndex = 52;
                }
            }
            if(exists54 && dist53 + 1 < dist54)
            {
                dist54 = dist53 + 1;
                lookup[indexToLocalY(54)][indexToLocalX(54)] = Direction.WEST;
                if(h54 < closestDistance)
                {
                    closestDistance = h54;
                    closestIndex = 54;
                }
            }
            if(exists63 && dist53 + 1 < dist63)
            {
                dist63 = dist53 + 1;
                lookup[indexToLocalY(63)][indexToLocalX(63)] = Direction.NORTHEAST;
                if(h63 < closestDistance)
                {
                    closestDistance = h63;
                    closestIndex = 63;
                }
            }
            if(exists64 && dist53 + 1 < dist64)
            {
                dist64 = dist53 + 1;
                lookup[indexToLocalY(64)][indexToLocalX(64)] = Direction.NORTH;
                if(h64 < closestDistance)
                {
                    closestDistance = h64;
                    closestIndex = 64;
                }
            }
            if(exists65 && dist53 + 1 < dist65)
            {
                dist65 = dist53 + 1;
                lookup[indexToLocalY(65)][indexToLocalX(65)] = Direction.NORTHWEST;
                if(h65 < closestDistance)
                {
                    closestDistance = h65;
                    closestIndex = 65;
                }
            }
        }
        if(exists64)
        {
            if(exists52 && dist64 + 1 < dist52)
            {
                dist52 = dist64 + 1;
                lookup[indexToLocalY(52)][indexToLocalX(52)] = Direction.SOUTHEAST;
                if(h52 < closestDistance)
                {
                    closestDistance = h52;
                    closestIndex = 52;
                }
            }
            if(exists53 && dist64 + 1 < dist53)
            {
                dist53 = dist64 + 1;
                lookup[indexToLocalY(53)][indexToLocalX(53)] = Direction.SOUTH;
                if(h53 < closestDistance)
                {
                    closestDistance = h53;
                    closestIndex = 53;
                }
            }
            if(exists54 && dist64 + 1 < dist54)
            {
                dist54 = dist64 + 1;
                lookup[indexToLocalY(54)][indexToLocalX(54)] = Direction.SOUTHWEST;
                if(h54 < closestDistance)
                {
                    closestDistance = h54;
                    closestIndex = 54;
                }
            }
            if(exists63 && dist64 + 1 < dist63)
            {
                dist63 = dist64 + 1;
                lookup[indexToLocalY(63)][indexToLocalX(63)] = Direction.EAST;
                if(h63 < closestDistance)
                {
                    closestDistance = h63;
                    closestIndex = 63;
                }
            }
            if(exists65 && dist64 + 1 < dist65)
            {
                dist65 = dist64 + 1;
                lookup[indexToLocalY(65)][indexToLocalX(65)] = Direction.WEST;
                if(h65 < closestDistance)
                {
                    closestDistance = h65;
                    closestIndex = 65;
                }
            }
            if(exists74 && dist64 + 1 < dist74)
            {
                dist74 = dist64 + 1;
                lookup[indexToLocalY(74)][indexToLocalX(74)] = Direction.NORTHEAST;
                if(h74 < closestDistance)
                {
                    closestDistance = h74;
                    closestIndex = 74;
                }
            }
            if(exists75 && dist64 + 1 < dist75)
            {
                dist75 = dist64 + 1;
                lookup[indexToLocalY(75)][indexToLocalX(75)] = Direction.NORTH;
                if(h75 < closestDistance)
                {
                    closestDistance = h75;
                    closestIndex = 75;
                }
            }
            if(exists76 && dist64 + 1 < dist76)
            {
                dist76 = dist64 + 1;
                lookup[indexToLocalY(76)][indexToLocalX(76)] = Direction.NORTHWEST;
                if(h76 < closestDistance)
                {
                    closestDistance = h76;
                    closestIndex = 76;
                }
            }
        }
        if(exists75)
        {
            if(exists63 && dist75 + 1 < dist63)
            {
                dist63 = dist75 + 1;
                lookup[indexToLocalY(63)][indexToLocalX(63)] = Direction.SOUTHEAST;
                if(h63 < closestDistance)
                {
                    closestDistance = h63;
                    closestIndex = 63;
                }
            }
            if(exists64 && dist75 + 1 < dist64)
            {
                dist64 = dist75 + 1;
                lookup[indexToLocalY(64)][indexToLocalX(64)] = Direction.SOUTH;
                if(h64 < closestDistance)
                {
                    closestDistance = h64;
                    closestIndex = 64;
                }
            }
            if(exists65 && dist75 + 1 < dist65)
            {
                dist65 = dist75 + 1;
                lookup[indexToLocalY(65)][indexToLocalX(65)] = Direction.SOUTHWEST;
                if(h65 < closestDistance)
                {
                    closestDistance = h65;
                    closestIndex = 65;
                }
            }
            if(exists74 && dist75 + 1 < dist74)
            {
                dist74 = dist75 + 1;
                lookup[indexToLocalY(74)][indexToLocalX(74)] = Direction.EAST;
                if(h74 < closestDistance)
                {
                    closestDistance = h74;
                    closestIndex = 74;
                }
            }
            if(exists76 && dist75 + 1 < dist76)
            {
                dist76 = dist75 + 1;
                lookup[indexToLocalY(76)][indexToLocalX(76)] = Direction.WEST;
                if(h76 < closestDistance)
                {
                    closestDistance = h76;
                    closestIndex = 76;
                }
            }
            if(exists85 && dist75 + 1 < dist85)
            {
                dist85 = dist75 + 1;
                lookup[indexToLocalY(85)][indexToLocalX(85)] = Direction.NORTHEAST;
                if(h85 < closestDistance)
                {
                    closestDistance = h85;
                    closestIndex = 85;
                }
            }
            if(exists86 && dist75 + 1 < dist86)
            {
                dist86 = dist75 + 1;
                lookup[indexToLocalY(86)][indexToLocalX(86)] = Direction.NORTH;
                if(h86 < closestDistance)
                {
                    closestDistance = h86;
                    closestIndex = 86;
                }
            }
            if(exists87 && dist75 + 1 < dist87)
            {
                dist87 = dist75 + 1;
                lookup[indexToLocalY(87)][indexToLocalX(87)] = Direction.NORTHWEST;
                if(h87 < closestDistance)
                {
                    closestDistance = h87;
                    closestIndex = 87;
                }
            }
        }
        if(exists86)
        {
            if(exists74 && dist86 + 1 < dist74)
            {
                dist74 = dist86 + 1;
                lookup[indexToLocalY(74)][indexToLocalX(74)] = Direction.SOUTHEAST;
                if(h74 < closestDistance)
                {
                    closestDistance = h74;
                    closestIndex = 74;
                }
            }
            if(exists75 && dist86 + 1 < dist75)
            {
                dist75 = dist86 + 1;
                lookup[indexToLocalY(75)][indexToLocalX(75)] = Direction.SOUTH;
                if(h75 < closestDistance)
                {
                    closestDistance = h75;
                    closestIndex = 75;
                }
            }
            if(exists76 && dist86 + 1 < dist76)
            {
                dist76 = dist86 + 1;
                lookup[indexToLocalY(76)][indexToLocalX(76)] = Direction.SOUTHWEST;
                if(h76 < closestDistance)
                {
                    closestDistance = h76;
                    closestIndex = 76;
                }
            }
            if(exists85 && dist86 + 1 < dist85)
            {
                dist85 = dist86 + 1;
                lookup[indexToLocalY(85)][indexToLocalX(85)] = Direction.EAST;
                if(h85 < closestDistance)
                {
                    closestDistance = h85;
                    closestIndex = 85;
                }
            }
            if(exists87 && dist86 + 1 < dist87)
            {
                dist87 = dist86 + 1;
                lookup[indexToLocalY(87)][indexToLocalX(87)] = Direction.WEST;
                if(h87 < closestDistance)
                {
                    closestDistance = h87;
                    closestIndex = 87;
                }
            }
            if(exists96 && dist86 + 1 < dist96)
            {
                dist96 = dist86 + 1;
                lookup[indexToLocalY(96)][indexToLocalX(96)] = Direction.NORTHEAST;
                if(h96 < closestDistance)
                {
                    closestDistance = h96;
                    closestIndex = 96;
                }
            }
            if(exists97 && dist86 + 1 < dist97)
            {
                dist97 = dist86 + 1;
                lookup[indexToLocalY(97)][indexToLocalX(97)] = Direction.NORTH;
                if(h97 < closestDistance)
                {
                    closestDistance = h97;
                    closestIndex = 97;
                }
            }
            if(exists98 && dist86 + 1 < dist98)
            {
                dist98 = dist86 + 1;
                lookup[indexToLocalY(98)][indexToLocalX(98)] = Direction.NORTHWEST;
                if(h98 < closestDistance)
                {
                    closestDistance = h98;
                    closestIndex = 98;
                }
            }
        }
        if(exists89)
        {
            if(exists77 && dist89 + 1 < dist77)
            {
                dist77 = dist89 + 1;
                lookup[indexToLocalY(77)][indexToLocalX(77)] = Direction.SOUTHEAST;
                if(h77 < closestDistance)
                {
                    closestDistance = h77;
                    closestIndex = 77;
                }
            }
            if(exists78 && dist89 + 1 < dist78)
            {
                dist78 = dist89 + 1;
                lookup[indexToLocalY(78)][indexToLocalX(78)] = Direction.SOUTH;
                if(h78 < closestDistance)
                {
                    closestDistance = h78;
                    closestIndex = 78;
                }
            }
            if(exists79 && dist89 + 1 < dist79)
            {
                dist79 = dist89 + 1;
                lookup[indexToLocalY(79)][indexToLocalX(79)] = Direction.SOUTHWEST;
                if(h79 < closestDistance)
                {
                    closestDistance = h79;
                    closestIndex = 79;
                }
            }
            if(exists88 && dist89 + 1 < dist88)
            {
                dist88 = dist89 + 1;
                lookup[indexToLocalY(88)][indexToLocalX(88)] = Direction.EAST;
                if(h88 < closestDistance)
                {
                    closestDistance = h88;
                    closestIndex = 88;
                }
            }
            if(exists90 && dist89 + 1 < dist90)
            {
                dist90 = dist89 + 1;
                lookup[indexToLocalY(90)][indexToLocalX(90)] = Direction.WEST;
                if(h90 < closestDistance)
                {
                    closestDistance = h90;
                    closestIndex = 90;
                }
            }
            if(exists99 && dist89 + 1 < dist99)
            {
                dist99 = dist89 + 1;
                lookup[indexToLocalY(99)][indexToLocalX(99)] = Direction.NORTHEAST;
                if(h99 < closestDistance)
                {
                    closestDistance = h99;
                    closestIndex = 99;
                }
            }
            if(exists100 && dist89 + 1 < dist100)
            {
                dist100 = dist89 + 1;
                lookup[indexToLocalY(100)][indexToLocalX(100)] = Direction.NORTH;
                if(h100 < closestDistance)
                {
                    closestDistance = h100;
                    closestIndex = 100;
                }
            }
            if(exists101 && dist89 + 1 < dist101)
            {
                dist101 = dist89 + 1;
                lookup[indexToLocalY(101)][indexToLocalX(101)] = Direction.NORTHWEST;
                if(h101 < closestDistance)
                {
                    closestDistance = h101;
                    closestIndex = 101;
                }
            }
        }
        if(exists97)
        {
            if(exists85 && dist97 + 1 < dist85)
            {
                dist85 = dist97 + 1;
                lookup[indexToLocalY(85)][indexToLocalX(85)] = Direction.SOUTHEAST;
                if(h85 < closestDistance)
                {
                    closestDistance = h85;
                    closestIndex = 85;
                }
            }
            if(exists86 && dist97 + 1 < dist86)
            {
                dist86 = dist97 + 1;
                lookup[indexToLocalY(86)][indexToLocalX(86)] = Direction.SOUTH;
                if(h86 < closestDistance)
                {
                    closestDistance = h86;
                    closestIndex = 86;
                }
            }
            if(exists87 && dist97 + 1 < dist87)
            {
                dist87 = dist97 + 1;
                lookup[indexToLocalY(87)][indexToLocalX(87)] = Direction.SOUTHWEST;
                if(h87 < closestDistance)
                {
                    closestDistance = h87;
                    closestIndex = 87;
                }
            }
            if(exists96 && dist97 + 1 < dist96)
            {
                dist96 = dist97 + 1;
                lookup[indexToLocalY(96)][indexToLocalX(96)] = Direction.EAST;
                if(h96 < closestDistance)
                {
                    closestDistance = h96;
                    closestIndex = 96;
                }
            }
            if(exists98 && dist97 + 1 < dist98)
            {
                dist98 = dist97 + 1;
                lookup[indexToLocalY(98)][indexToLocalX(98)] = Direction.WEST;
                if(h98 < closestDistance)
                {
                    closestDistance = h98;
                    closestIndex = 98;
                }
            }
            if(exists107 && dist97 + 1 < dist107)
            {
                dist107 = dist97 + 1;
                lookup[indexToLocalY(107)][indexToLocalX(107)] = Direction.NORTHEAST;
                if(h107 < closestDistance)
                {
                    closestDistance = h107;
                    closestIndex = 107;
                }
            }
            if(exists108 && dist97 + 1 < dist108)
            {
                dist108 = dist97 + 1;
                lookup[indexToLocalY(108)][indexToLocalX(108)] = Direction.NORTH;
                if(h108 < closestDistance)
                {
                    closestDistance = h108;
                    closestIndex = 108;
                }
            }
            if(exists109 && dist97 + 1 < dist109)
            {
                dist109 = dist97 + 1;
                lookup[indexToLocalY(109)][indexToLocalX(109)] = Direction.NORTHWEST;
                if(h109 < closestDistance)
                {
                    closestDistance = h109;
                    closestIndex = 109;
                }
            }
        }
        if(exists100)
        {
            if(exists88 && dist100 + 1 < dist88)
            {
                dist88 = dist100 + 1;
                lookup[indexToLocalY(88)][indexToLocalX(88)] = Direction.SOUTHEAST;
                if(h88 < closestDistance)
                {
                    closestDistance = h88;
                    closestIndex = 88;
                }
            }
            if(exists89 && dist100 + 1 < dist89)
            {
                dist89 = dist100 + 1;
                lookup[indexToLocalY(89)][indexToLocalX(89)] = Direction.SOUTH;
                if(h89 < closestDistance)
                {
                    closestDistance = h89;
                    closestIndex = 89;
                }
            }
            if(exists90 && dist100 + 1 < dist90)
            {
                dist90 = dist100 + 1;
                lookup[indexToLocalY(90)][indexToLocalX(90)] = Direction.SOUTHWEST;
                if(h90 < closestDistance)
                {
                    closestDistance = h90;
                    closestIndex = 90;
                }
            }
            if(exists99 && dist100 + 1 < dist99)
            {
                dist99 = dist100 + 1;
                lookup[indexToLocalY(99)][indexToLocalX(99)] = Direction.EAST;
                if(h99 < closestDistance)
                {
                    closestDistance = h99;
                    closestIndex = 99;
                }
            }
            if(exists101 && dist100 + 1 < dist101)
            {
                dist101 = dist100 + 1;
                lookup[indexToLocalY(101)][indexToLocalX(101)] = Direction.WEST;
                if(h101 < closestDistance)
                {
                    closestDistance = h101;
                    closestIndex = 101;
                }
            }
            if(exists110 && dist100 + 1 < dist110)
            {
                dist110 = dist100 + 1;
                lookup[indexToLocalY(110)][indexToLocalX(110)] = Direction.NORTHEAST;
                if(h110 < closestDistance)
                {
                    closestDistance = h110;
                    closestIndex = 110;
                }
            }
            if(exists111 && dist100 + 1 < dist111)
            {
                dist111 = dist100 + 1;
                lookup[indexToLocalY(111)][indexToLocalX(111)] = Direction.NORTH;
                if(h111 < closestDistance)
                {
                    closestDistance = h111;
                    closestIndex = 111;
                }
            }
            if(exists112 && dist100 + 1 < dist112)
            {
                dist112 = dist100 + 1;
                lookup[indexToLocalY(112)][indexToLocalX(112)] = Direction.NORTHWEST;
                if(h112 < closestDistance)
                {
                    closestDistance = h112;
                    closestIndex = 112;
                }
            }
        }
        if(exists101)
        {
            if(exists89 && dist101 + 1 < dist89)
            {
                dist89 = dist101 + 1;
                lookup[indexToLocalY(89)][indexToLocalX(89)] = Direction.SOUTHEAST;
                if(h89 < closestDistance)
                {
                    closestDistance = h89;
                    closestIndex = 89;
                }
            }
            if(exists90 && dist101 + 1 < dist90)
            {
                dist90 = dist101 + 1;
                lookup[indexToLocalY(90)][indexToLocalX(90)] = Direction.SOUTH;
                if(h90 < closestDistance)
                {
                    closestDistance = h90;
                    closestIndex = 90;
                }
            }
            if(exists91 && dist101 + 1 < dist91)
            {
                dist91 = dist101 + 1;
                lookup[indexToLocalY(91)][indexToLocalX(91)] = Direction.SOUTHWEST;
                if(h91 < closestDistance)
                {
                    closestDistance = h91;
                    closestIndex = 91;
                }
            }
            if(exists100 && dist101 + 1 < dist100)
            {
                dist100 = dist101 + 1;
                lookup[indexToLocalY(100)][indexToLocalX(100)] = Direction.EAST;
                if(h100 < closestDistance)
                {
                    closestDistance = h100;
                    closestIndex = 100;
                }
            }
            if(exists102 && dist101 + 1 < dist102)
            {
                dist102 = dist101 + 1;
                lookup[indexToLocalY(102)][indexToLocalX(102)] = Direction.WEST;
                if(h102 < closestDistance)
                {
                    closestDistance = h102;
                    closestIndex = 102;
                }
            }
            if(exists111 && dist101 + 1 < dist111)
            {
                dist111 = dist101 + 1;
                lookup[indexToLocalY(111)][indexToLocalX(111)] = Direction.NORTHEAST;
                if(h111 < closestDistance)
                {
                    closestDistance = h111;
                    closestIndex = 111;
                }
            }
            if(exists112 && dist101 + 1 < dist112)
            {
                dist112 = dist101 + 1;
                lookup[indexToLocalY(112)][indexToLocalX(112)] = Direction.NORTH;
                if(h112 < closestDistance)
                {
                    closestDistance = h112;
                    closestIndex = 112;
                }
            }
            if(exists113 && dist101 + 1 < dist113)
            {
                dist113 = dist101 + 1;
                lookup[indexToLocalY(113)][indexToLocalX(113)] = Direction.NORTHWEST;
                if(h113 < closestDistance)
                {
                    closestDistance = h113;
                    closestIndex = 113;
                }
            }
        }
        if(exists102)
        {
            if(exists90 && dist102 + 1 < dist90)
            {
                dist90 = dist102 + 1;
                lookup[indexToLocalY(90)][indexToLocalX(90)] = Direction.SOUTHEAST;
                if(h90 < closestDistance)
                {
                    closestDistance = h90;
                    closestIndex = 90;
                }
            }
            if(exists91 && dist102 + 1 < dist91)
            {
                dist91 = dist102 + 1;
                lookup[indexToLocalY(91)][indexToLocalX(91)] = Direction.SOUTH;
                if(h91 < closestDistance)
                {
                    closestDistance = h91;
                    closestIndex = 91;
                }
            }
            if(exists92 && dist102 + 1 < dist92)
            {
                dist92 = dist102 + 1;
                lookup[indexToLocalY(92)][indexToLocalX(92)] = Direction.SOUTHWEST;
                if(h92 < closestDistance)
                {
                    closestDistance = h92;
                    closestIndex = 92;
                }
            }
            if(exists101 && dist102 + 1 < dist101)
            {
                dist101 = dist102 + 1;
                lookup[indexToLocalY(101)][indexToLocalX(101)] = Direction.EAST;
                if(h101 < closestDistance)
                {
                    closestDistance = h101;
                    closestIndex = 101;
                }
            }
            if(exists103 && dist102 + 1 < dist103)
            {
                dist103 = dist102 + 1;
                lookup[indexToLocalY(103)][indexToLocalX(103)] = Direction.WEST;
                if(h103 < closestDistance)
                {
                    closestDistance = h103;
                    closestIndex = 103;
                }
            }
            if(exists112 && dist102 + 1 < dist112)
            {
                dist112 = dist102 + 1;
                lookup[indexToLocalY(112)][indexToLocalX(112)] = Direction.NORTHEAST;
                if(h112 < closestDistance)
                {
                    closestDistance = h112;
                    closestIndex = 112;
                }
            }
            if(exists113 && dist102 + 1 < dist113)
            {
                dist113 = dist102 + 1;
                lookup[indexToLocalY(113)][indexToLocalX(113)] = Direction.NORTH;
                if(h113 < closestDistance)
                {
                    closestDistance = h113;
                    closestIndex = 113;
                }
            }
            if(exists114 && dist102 + 1 < dist114)
            {
                dist114 = dist102 + 1;
                lookup[indexToLocalY(114)][indexToLocalX(114)] = Direction.NORTHWEST;
                if(h114 < closestDistance)
                {
                    closestDistance = h114;
                    closestIndex = 114;
                }
            }
        }
        if(exists103)
        {
            if(exists91 && dist103 + 1 < dist91)
            {
                dist91 = dist103 + 1;
                lookup[indexToLocalY(91)][indexToLocalX(91)] = Direction.SOUTHEAST;
                if(h91 < closestDistance)
                {
                    closestDistance = h91;
                    closestIndex = 91;
                }
            }
            if(exists92 && dist103 + 1 < dist92)
            {
                dist92 = dist103 + 1;
                lookup[indexToLocalY(92)][indexToLocalX(92)] = Direction.SOUTH;
                if(h92 < closestDistance)
                {
                    closestDistance = h92;
                    closestIndex = 92;
                }
            }
            if(exists93 && dist103 + 1 < dist93)
            {
                dist93 = dist103 + 1;
                lookup[indexToLocalY(93)][indexToLocalX(93)] = Direction.SOUTHWEST;
                if(h93 < closestDistance)
                {
                    closestDistance = h93;
                    closestIndex = 93;
                }
            }
            if(exists102 && dist103 + 1 < dist102)
            {
                dist102 = dist103 + 1;
                lookup[indexToLocalY(102)][indexToLocalX(102)] = Direction.EAST;
                if(h102 < closestDistance)
                {
                    closestDistance = h102;
                    closestIndex = 102;
                }
            }
            if(exists104 && dist103 + 1 < dist104)
            {
                dist104 = dist103 + 1;
                lookup[indexToLocalY(104)][indexToLocalX(104)] = Direction.WEST;
                if(h104 < closestDistance)
                {
                    closestDistance = h104;
                    closestIndex = 104;
                }
            }
            if(exists113 && dist103 + 1 < dist113)
            {
                dist113 = dist103 + 1;
                lookup[indexToLocalY(113)][indexToLocalX(113)] = Direction.NORTHEAST;
                if(h113 < closestDistance)
                {
                    closestDistance = h113;
                    closestIndex = 113;
                }
            }
            if(exists114 && dist103 + 1 < dist114)
            {
                dist114 = dist103 + 1;
                lookup[indexToLocalY(114)][indexToLocalX(114)] = Direction.NORTH;
                if(h114 < closestDistance)
                {
                    closestDistance = h114;
                    closestIndex = 114;
                }
            }
            if(exists115 && dist103 + 1 < dist115)
            {
                dist115 = dist103 + 1;
                lookup[indexToLocalY(115)][indexToLocalX(115)] = Direction.NORTHWEST;
                if(h115 < closestDistance)
                {
                    closestDistance = h115;
                    closestIndex = 115;
                }
            }
        }
        if(exists104)
        {
            if(exists92 && dist104 + 1 < dist92)
            {
                dist92 = dist104 + 1;
                lookup[indexToLocalY(92)][indexToLocalX(92)] = Direction.SOUTHEAST;
                if(h92 < closestDistance)
                {
                    closestDistance = h92;
                    closestIndex = 92;
                }
            }
            if(exists93 && dist104 + 1 < dist93)
            {
                dist93 = dist104 + 1;
                lookup[indexToLocalY(93)][indexToLocalX(93)] = Direction.SOUTH;
                if(h93 < closestDistance)
                {
                    closestDistance = h93;
                    closestIndex = 93;
                }
            }
            if(exists94 && dist104 + 1 < dist94)
            {
                dist94 = dist104 + 1;
                lookup[indexToLocalY(94)][indexToLocalX(94)] = Direction.SOUTHWEST;
                if(h94 < closestDistance)
                {
                    closestDistance = h94;
                    closestIndex = 94;
                }
            }
            if(exists103 && dist104 + 1 < dist103)
            {
                dist103 = dist104 + 1;
                lookup[indexToLocalY(103)][indexToLocalX(103)] = Direction.EAST;
                if(h103 < closestDistance)
                {
                    closestDistance = h103;
                    closestIndex = 103;
                }
            }
            if(exists105 && dist104 + 1 < dist105)
            {
                dist105 = dist104 + 1;
                lookup[indexToLocalY(105)][indexToLocalX(105)] = Direction.WEST;
                if(h105 < closestDistance)
                {
                    closestDistance = h105;
                    closestIndex = 105;
                }
            }
            if(exists114 && dist104 + 1 < dist114)
            {
                dist114 = dist104 + 1;
                lookup[indexToLocalY(114)][indexToLocalX(114)] = Direction.NORTHEAST;
                if(h114 < closestDistance)
                {
                    closestDistance = h114;
                    closestIndex = 114;
                }
            }
            if(exists115 && dist104 + 1 < dist115)
            {
                dist115 = dist104 + 1;
                lookup[indexToLocalY(115)][indexToLocalX(115)] = Direction.NORTH;
                if(h115 < closestDistance)
                {
                    closestDistance = h115;
                    closestIndex = 115;
                }
            }
            if(exists116 && dist104 + 1 < dist116)
            {
                dist116 = dist104 + 1;
                lookup[indexToLocalY(116)][indexToLocalX(116)] = Direction.NORTHWEST;
                if(h116 < closestDistance)
                {
                    closestDistance = h116;
                    closestIndex = 116;
                }
            }
        }
        if(exists105)
        {
            if(exists93 && dist105 + 1 < dist93)
            {
                dist93 = dist105 + 1;
                lookup[indexToLocalY(93)][indexToLocalX(93)] = Direction.SOUTHEAST;
                if(h93 < closestDistance)
                {
                    closestDistance = h93;
                    closestIndex = 93;
                }
            }
            if(exists94 && dist105 + 1 < dist94)
            {
                dist94 = dist105 + 1;
                lookup[indexToLocalY(94)][indexToLocalX(94)] = Direction.SOUTH;
                if(h94 < closestDistance)
                {
                    closestDistance = h94;
                    closestIndex = 94;
                }
            }
            if(exists95 && dist105 + 1 < dist95)
            {
                dist95 = dist105 + 1;
                lookup[indexToLocalY(95)][indexToLocalX(95)] = Direction.SOUTHWEST;
                if(h95 < closestDistance)
                {
                    closestDistance = h95;
                    closestIndex = 95;
                }
            }
            if(exists104 && dist105 + 1 < dist104)
            {
                dist104 = dist105 + 1;
                lookup[indexToLocalY(104)][indexToLocalX(104)] = Direction.EAST;
                if(h104 < closestDistance)
                {
                    closestDistance = h104;
                    closestIndex = 104;
                }
            }
            if(exists106 && dist105 + 1 < dist106)
            {
                dist106 = dist105 + 1;
                lookup[indexToLocalY(106)][indexToLocalX(106)] = Direction.WEST;
                if(h106 < closestDistance)
                {
                    closestDistance = h106;
                    closestIndex = 106;
                }
            }
            if(exists115 && dist105 + 1 < dist115)
            {
                dist115 = dist105 + 1;
                lookup[indexToLocalY(115)][indexToLocalX(115)] = Direction.NORTHEAST;
                if(h115 < closestDistance)
                {
                    closestDistance = h115;
                    closestIndex = 115;
                }
            }
            if(exists116 && dist105 + 1 < dist116)
            {
                dist116 = dist105 + 1;
                lookup[indexToLocalY(116)][indexToLocalX(116)] = Direction.NORTH;
                if(h116 < closestDistance)
                {
                    closestDistance = h116;
                    closestIndex = 116;
                }
            }
            if(exists117 && dist105 + 1 < dist117)
            {
                dist117 = dist105 + 1;
                lookup[indexToLocalY(117)][indexToLocalX(117)] = Direction.NORTHWEST;
                if(h117 < closestDistance)
                {
                    closestDistance = h117;
                    closestIndex = 117;
                }
            }
        }
    }

    private static void method3()
    {
        if(exists41)
        {
            if(exists29 && dist41 + 1 < dist29)
            {
                dist29 = dist41 + 1;
                lookup[indexToLocalY(29)][indexToLocalX(29)] = Direction.SOUTHEAST;
                if(h29 < closestDistance)
                {
                    closestDistance = h29;
                    closestIndex = 29;
                }
            }
            if(exists30 && dist41 + 1 < dist30)
            {
                dist30 = dist41 + 1;
                lookup[indexToLocalY(30)][indexToLocalX(30)] = Direction.SOUTH;
                if(h30 < closestDistance)
                {
                    closestDistance = h30;
                    closestIndex = 30;
                }
            }
            if(exists31 && dist41 + 1 < dist31)
            {
                dist31 = dist41 + 1;
                lookup[indexToLocalY(31)][indexToLocalX(31)] = Direction.SOUTHWEST;
                if(h31 < closestDistance)
                {
                    closestDistance = h31;
                    closestIndex = 31;
                }
            }
            if(exists40 && dist41 + 1 < dist40)
            {
                dist40 = dist41 + 1;
                lookup[indexToLocalY(40)][indexToLocalX(40)] = Direction.EAST;
                if(h40 < closestDistance)
                {
                    closestDistance = h40;
                    closestIndex = 40;
                }
            }
            if(exists42 && dist41 + 1 < dist42)
            {
                dist42 = dist41 + 1;
                lookup[indexToLocalY(42)][indexToLocalX(42)] = Direction.WEST;
                if(h42 < closestDistance)
                {
                    closestDistance = h42;
                    closestIndex = 42;
                }
            }
            if(exists51 && dist41 + 1 < dist51)
            {
                dist51 = dist41 + 1;
                lookup[indexToLocalY(51)][indexToLocalX(51)] = Direction.NORTHEAST;
                if(h51 < closestDistance)
                {
                    closestDistance = h51;
                    closestIndex = 51;
                }
            }
            if(exists52 && dist41 + 1 < dist52)
            {
                dist52 = dist41 + 1;
                lookup[indexToLocalY(52)][indexToLocalX(52)] = Direction.NORTH;
                if(h52 < closestDistance)
                {
                    closestDistance = h52;
                    closestIndex = 52;
                }
            }
            if(exists53 && dist41 + 1 < dist53)
            {
                dist53 = dist41 + 1;
                lookup[indexToLocalY(53)][indexToLocalX(53)] = Direction.NORTHWEST;
                if(h53 < closestDistance)
                {
                    closestDistance = h53;
                    closestIndex = 53;
                }
            }
        }
        if(exists52)
        {
            if(exists40 && dist52 + 1 < dist40)
            {
                dist40 = dist52 + 1;
                lookup[indexToLocalY(40)][indexToLocalX(40)] = Direction.SOUTHEAST;
                if(h40 < closestDistance)
                {
                    closestDistance = h40;
                    closestIndex = 40;
                }
            }
            if(exists41 && dist52 + 1 < dist41)
            {
                dist41 = dist52 + 1;
                lookup[indexToLocalY(41)][indexToLocalX(41)] = Direction.SOUTH;
                if(h41 < closestDistance)
                {
                    closestDistance = h41;
                    closestIndex = 41;
                }
            }
            if(exists42 && dist52 + 1 < dist42)
            {
                dist42 = dist52 + 1;
                lookup[indexToLocalY(42)][indexToLocalX(42)] = Direction.SOUTHWEST;
                if(h42 < closestDistance)
                {
                    closestDistance = h42;
                    closestIndex = 42;
                }
            }
            if(exists51 && dist52 + 1 < dist51)
            {
                dist51 = dist52 + 1;
                lookup[indexToLocalY(51)][indexToLocalX(51)] = Direction.EAST;
                if(h51 < closestDistance)
                {
                    closestDistance = h51;
                    closestIndex = 51;
                }
            }
            if(exists53 && dist52 + 1 < dist53)
            {
                dist53 = dist52 + 1;
                lookup[indexToLocalY(53)][indexToLocalX(53)] = Direction.WEST;
                if(h53 < closestDistance)
                {
                    closestDistance = h53;
                    closestIndex = 53;
                }
            }
            if(exists62 && dist52 + 1 < dist62)
            {
                dist62 = dist52 + 1;
                lookup[indexToLocalY(62)][indexToLocalX(62)] = Direction.NORTHEAST;
                if(h62 < closestDistance)
                {
                    closestDistance = h62;
                    closestIndex = 62;
                }
            }
            if(exists63 && dist52 + 1 < dist63)
            {
                dist63 = dist52 + 1;
                lookup[indexToLocalY(63)][indexToLocalX(63)] = Direction.NORTH;
                if(h63 < closestDistance)
                {
                    closestDistance = h63;
                    closestIndex = 63;
                }
            }
            if(exists64 && dist52 + 1 < dist64)
            {
                dist64 = dist52 + 1;
                lookup[indexToLocalY(64)][indexToLocalX(64)] = Direction.NORTHWEST;
                if(h64 < closestDistance)
                {
                    closestDistance = h64;
                    closestIndex = 64;
                }
            }
        }
        if(exists63)
        {
            if(exists51 && dist63 + 1 < dist51)
            {
                dist51 = dist63 + 1;
                lookup[indexToLocalY(51)][indexToLocalX(51)] = Direction.SOUTHEAST;
                if(h51 < closestDistance)
                {
                    closestDistance = h51;
                    closestIndex = 51;
                }
            }
            if(exists52 && dist63 + 1 < dist52)
            {
                dist52 = dist63 + 1;
                lookup[indexToLocalY(52)][indexToLocalX(52)] = Direction.SOUTH;
                if(h52 < closestDistance)
                {
                    closestDistance = h52;
                    closestIndex = 52;
                }
            }
            if(exists53 && dist63 + 1 < dist53)
            {
                dist53 = dist63 + 1;
                lookup[indexToLocalY(53)][indexToLocalX(53)] = Direction.SOUTHWEST;
                if(h53 < closestDistance)
                {
                    closestDistance = h53;
                    closestIndex = 53;
                }
            }
            if(exists62 && dist63 + 1 < dist62)
            {
                dist62 = dist63 + 1;
                lookup[indexToLocalY(62)][indexToLocalX(62)] = Direction.EAST;
                if(h62 < closestDistance)
                {
                    closestDistance = h62;
                    closestIndex = 62;
                }
            }
            if(exists64 && dist63 + 1 < dist64)
            {
                dist64 = dist63 + 1;
                lookup[indexToLocalY(64)][indexToLocalX(64)] = Direction.WEST;
                if(h64 < closestDistance)
                {
                    closestDistance = h64;
                    closestIndex = 64;
                }
            }
            if(exists73 && dist63 + 1 < dist73)
            {
                dist73 = dist63 + 1;
                lookup[indexToLocalY(73)][indexToLocalX(73)] = Direction.NORTHEAST;
                if(h73 < closestDistance)
                {
                    closestDistance = h73;
                    closestIndex = 73;
                }
            }
            if(exists74 && dist63 + 1 < dist74)
            {
                dist74 = dist63 + 1;
                lookup[indexToLocalY(74)][indexToLocalX(74)] = Direction.NORTH;
                if(h74 < closestDistance)
                {
                    closestDistance = h74;
                    closestIndex = 74;
                }
            }
            if(exists75 && dist63 + 1 < dist75)
            {
                dist75 = dist63 + 1;
                lookup[indexToLocalY(75)][indexToLocalX(75)] = Direction.NORTHWEST;
                if(h75 < closestDistance)
                {
                    closestDistance = h75;
                    closestIndex = 75;
                }
            }
        }
        if(exists74)
        {
            if(exists62 && dist74 + 1 < dist62)
            {
                dist62 = dist74 + 1;
                lookup[indexToLocalY(62)][indexToLocalX(62)] = Direction.SOUTHEAST;
                if(h62 < closestDistance)
                {
                    closestDistance = h62;
                    closestIndex = 62;
                }
            }
            if(exists63 && dist74 + 1 < dist63)
            {
                dist63 = dist74 + 1;
                lookup[indexToLocalY(63)][indexToLocalX(63)] = Direction.SOUTH;
                if(h63 < closestDistance)
                {
                    closestDistance = h63;
                    closestIndex = 63;
                }
            }
            if(exists64 && dist74 + 1 < dist64)
            {
                dist64 = dist74 + 1;
                lookup[indexToLocalY(64)][indexToLocalX(64)] = Direction.SOUTHWEST;
                if(h64 < closestDistance)
                {
                    closestDistance = h64;
                    closestIndex = 64;
                }
            }
            if(exists73 && dist74 + 1 < dist73)
            {
                dist73 = dist74 + 1;
                lookup[indexToLocalY(73)][indexToLocalX(73)] = Direction.EAST;
                if(h73 < closestDistance)
                {
                    closestDistance = h73;
                    closestIndex = 73;
                }
            }
            if(exists75 && dist74 + 1 < dist75)
            {
                dist75 = dist74 + 1;
                lookup[indexToLocalY(75)][indexToLocalX(75)] = Direction.WEST;
                if(h75 < closestDistance)
                {
                    closestDistance = h75;
                    closestIndex = 75;
                }
            }
            if(exists84 && dist74 + 1 < dist84)
            {
                dist84 = dist74 + 1;
                lookup[indexToLocalY(84)][indexToLocalX(84)] = Direction.NORTHEAST;
                if(h84 < closestDistance)
                {
                    closestDistance = h84;
                    closestIndex = 84;
                }
            }
            if(exists85 && dist74 + 1 < dist85)
            {
                dist85 = dist74 + 1;
                lookup[indexToLocalY(85)][indexToLocalX(85)] = Direction.NORTH;
                if(h85 < closestDistance)
                {
                    closestDistance = h85;
                    closestIndex = 85;
                }
            }
            if(exists86 && dist74 + 1 < dist86)
            {
                dist86 = dist74 + 1;
                lookup[indexToLocalY(86)][indexToLocalX(86)] = Direction.NORTHWEST;
                if(h86 < closestDistance)
                {
                    closestDistance = h86;
                    closestIndex = 86;
                }
            }
        }
        if(exists79)
        {
            if(exists67 && dist79 + 1 < dist67)
            {
                dist67 = dist79 + 1;
                lookup[indexToLocalY(67)][indexToLocalX(67)] = Direction.SOUTHEAST;
                if(h67 < closestDistance)
                {
                    closestDistance = h67;
                    closestIndex = 67;
                }
            }
            if(exists68 && dist79 + 1 < dist68)
            {
                dist68 = dist79 + 1;
                lookup[indexToLocalY(68)][indexToLocalX(68)] = Direction.SOUTH;
                if(h68 < closestDistance)
                {
                    closestDistance = h68;
                    closestIndex = 68;
                }
            }
            if(exists69 && dist79 + 1 < dist69)
            {
                dist69 = dist79 + 1;
                lookup[indexToLocalY(69)][indexToLocalX(69)] = Direction.SOUTHWEST;
                if(h69 < closestDistance)
                {
                    closestDistance = h69;
                    closestIndex = 69;
                }
            }
            if(exists78 && dist79 + 1 < dist78)
            {
                dist78 = dist79 + 1;
                lookup[indexToLocalY(78)][indexToLocalX(78)] = Direction.EAST;
                if(h78 < closestDistance)
                {
                    closestDistance = h78;
                    closestIndex = 78;
                }
            }
            if(exists80 && dist79 + 1 < dist80)
            {
                dist80 = dist79 + 1;
                lookup[indexToLocalY(80)][indexToLocalX(80)] = Direction.WEST;
                if(h80 < closestDistance)
                {
                    closestDistance = h80;
                    closestIndex = 80;
                }
            }
            if(exists89 && dist79 + 1 < dist89)
            {
                dist89 = dist79 + 1;
                lookup[indexToLocalY(89)][indexToLocalX(89)] = Direction.NORTHEAST;
                if(h89 < closestDistance)
                {
                    closestDistance = h89;
                    closestIndex = 89;
                }
            }
            if(exists90 && dist79 + 1 < dist90)
            {
                dist90 = dist79 + 1;
                lookup[indexToLocalY(90)][indexToLocalX(90)] = Direction.NORTH;
                if(h90 < closestDistance)
                {
                    closestDistance = h90;
                    closestIndex = 90;
                }
            }
            if(exists91 && dist79 + 1 < dist91)
            {
                dist91 = dist79 + 1;
                lookup[indexToLocalY(91)][indexToLocalX(91)] = Direction.NORTHWEST;
                if(h91 < closestDistance)
                {
                    closestDistance = h91;
                    closestIndex = 91;
                }
            }
        }
        if(exists85)
        {
            if(exists73 && dist85 + 1 < dist73)
            {
                dist73 = dist85 + 1;
                lookup[indexToLocalY(73)][indexToLocalX(73)] = Direction.SOUTHEAST;
                if(h73 < closestDistance)
                {
                    closestDistance = h73;
                    closestIndex = 73;
                }
            }
            if(exists74 && dist85 + 1 < dist74)
            {
                dist74 = dist85 + 1;
                lookup[indexToLocalY(74)][indexToLocalX(74)] = Direction.SOUTH;
                if(h74 < closestDistance)
                {
                    closestDistance = h74;
                    closestIndex = 74;
                }
            }
            if(exists75 && dist85 + 1 < dist75)
            {
                dist75 = dist85 + 1;
                lookup[indexToLocalY(75)][indexToLocalX(75)] = Direction.SOUTHWEST;
                if(h75 < closestDistance)
                {
                    closestDistance = h75;
                    closestIndex = 75;
                }
            }
            if(exists84 && dist85 + 1 < dist84)
            {
                dist84 = dist85 + 1;
                lookup[indexToLocalY(84)][indexToLocalX(84)] = Direction.EAST;
                if(h84 < closestDistance)
                {
                    closestDistance = h84;
                    closestIndex = 84;
                }
            }
            if(exists86 && dist85 + 1 < dist86)
            {
                dist86 = dist85 + 1;
                lookup[indexToLocalY(86)][indexToLocalX(86)] = Direction.WEST;
                if(h86 < closestDistance)
                {
                    closestDistance = h86;
                    closestIndex = 86;
                }
            }
            if(exists95 && dist85 + 1 < dist95)
            {
                dist95 = dist85 + 1;
                lookup[indexToLocalY(95)][indexToLocalX(95)] = Direction.NORTHEAST;
                if(h95 < closestDistance)
                {
                    closestDistance = h95;
                    closestIndex = 95;
                }
            }
            if(exists96 && dist85 + 1 < dist96)
            {
                dist96 = dist85 + 1;
                lookup[indexToLocalY(96)][indexToLocalX(96)] = Direction.NORTH;
                if(h96 < closestDistance)
                {
                    closestDistance = h96;
                    closestIndex = 96;
                }
            }
            if(exists97 && dist85 + 1 < dist97)
            {
                dist97 = dist85 + 1;
                lookup[indexToLocalY(97)][indexToLocalX(97)] = Direction.NORTHWEST;
                if(h97 < closestDistance)
                {
                    closestDistance = h97;
                    closestIndex = 97;
                }
            }
        }
        if(exists90)
        {
            if(exists78 && dist90 + 1 < dist78)
            {
                dist78 = dist90 + 1;
                lookup[indexToLocalY(78)][indexToLocalX(78)] = Direction.SOUTHEAST;
                if(h78 < closestDistance)
                {
                    closestDistance = h78;
                    closestIndex = 78;
                }
            }
            if(exists79 && dist90 + 1 < dist79)
            {
                dist79 = dist90 + 1;
                lookup[indexToLocalY(79)][indexToLocalX(79)] = Direction.SOUTH;
                if(h79 < closestDistance)
                {
                    closestDistance = h79;
                    closestIndex = 79;
                }
            }
            if(exists80 && dist90 + 1 < dist80)
            {
                dist80 = dist90 + 1;
                lookup[indexToLocalY(80)][indexToLocalX(80)] = Direction.SOUTHWEST;
                if(h80 < closestDistance)
                {
                    closestDistance = h80;
                    closestIndex = 80;
                }
            }
            if(exists89 && dist90 + 1 < dist89)
            {
                dist89 = dist90 + 1;
                lookup[indexToLocalY(89)][indexToLocalX(89)] = Direction.EAST;
                if(h89 < closestDistance)
                {
                    closestDistance = h89;
                    closestIndex = 89;
                }
            }
            if(exists91 && dist90 + 1 < dist91)
            {
                dist91 = dist90 + 1;
                lookup[indexToLocalY(91)][indexToLocalX(91)] = Direction.WEST;
                if(h91 < closestDistance)
                {
                    closestDistance = h91;
                    closestIndex = 91;
                }
            }
            if(exists100 && dist90 + 1 < dist100)
            {
                dist100 = dist90 + 1;
                lookup[indexToLocalY(100)][indexToLocalX(100)] = Direction.NORTHEAST;
                if(h100 < closestDistance)
                {
                    closestDistance = h100;
                    closestIndex = 100;
                }
            }
            if(exists101 && dist90 + 1 < dist101)
            {
                dist101 = dist90 + 1;
                lookup[indexToLocalY(101)][indexToLocalX(101)] = Direction.NORTH;
                if(h101 < closestDistance)
                {
                    closestDistance = h101;
                    closestIndex = 101;
                }
            }
            if(exists102 && dist90 + 1 < dist102)
            {
                dist102 = dist90 + 1;
                lookup[indexToLocalY(102)][indexToLocalX(102)] = Direction.NORTHWEST;
                if(h102 < closestDistance)
                {
                    closestDistance = h102;
                    closestIndex = 102;
                }
            }
        }
        if(exists91)
        {
            if(exists79 && dist91 + 1 < dist79)
            {
                dist79 = dist91 + 1;
                lookup[indexToLocalY(79)][indexToLocalX(79)] = Direction.SOUTHEAST;
                if(h79 < closestDistance)
                {
                    closestDistance = h79;
                    closestIndex = 79;
                }
            }
            if(exists80 && dist91 + 1 < dist80)
            {
                dist80 = dist91 + 1;
                lookup[indexToLocalY(80)][indexToLocalX(80)] = Direction.SOUTH;
                if(h80 < closestDistance)
                {
                    closestDistance = h80;
                    closestIndex = 80;
                }
            }
            if(exists81 && dist91 + 1 < dist81)
            {
                dist81 = dist91 + 1;
                lookup[indexToLocalY(81)][indexToLocalX(81)] = Direction.SOUTHWEST;
                if(h81 < closestDistance)
                {
                    closestDistance = h81;
                    closestIndex = 81;
                }
            }
            if(exists90 && dist91 + 1 < dist90)
            {
                dist90 = dist91 + 1;
                lookup[indexToLocalY(90)][indexToLocalX(90)] = Direction.EAST;
                if(h90 < closestDistance)
                {
                    closestDistance = h90;
                    closestIndex = 90;
                }
            }
            if(exists92 && dist91 + 1 < dist92)
            {
                dist92 = dist91 + 1;
                lookup[indexToLocalY(92)][indexToLocalX(92)] = Direction.WEST;
                if(h92 < closestDistance)
                {
                    closestDistance = h92;
                    closestIndex = 92;
                }
            }
            if(exists101 && dist91 + 1 < dist101)
            {
                dist101 = dist91 + 1;
                lookup[indexToLocalY(101)][indexToLocalX(101)] = Direction.NORTHEAST;
                if(h101 < closestDistance)
                {
                    closestDistance = h101;
                    closestIndex = 101;
                }
            }
            if(exists102 && dist91 + 1 < dist102)
            {
                dist102 = dist91 + 1;
                lookup[indexToLocalY(102)][indexToLocalX(102)] = Direction.NORTH;
                if(h102 < closestDistance)
                {
                    closestDistance = h102;
                    closestIndex = 102;
                }
            }
            if(exists103 && dist91 + 1 < dist103)
            {
                dist103 = dist91 + 1;
                lookup[indexToLocalY(103)][indexToLocalX(103)] = Direction.NORTHWEST;
                if(h103 < closestDistance)
                {
                    closestDistance = h103;
                    closestIndex = 103;
                }
            }
        }
        if(exists92)
        {
            if(exists80 && dist92 + 1 < dist80)
            {
                dist80 = dist92 + 1;
                lookup[indexToLocalY(80)][indexToLocalX(80)] = Direction.SOUTHEAST;
                if(h80 < closestDistance)
                {
                    closestDistance = h80;
                    closestIndex = 80;
                }
            }
            if(exists81 && dist92 + 1 < dist81)
            {
                dist81 = dist92 + 1;
                lookup[indexToLocalY(81)][indexToLocalX(81)] = Direction.SOUTH;
                if(h81 < closestDistance)
                {
                    closestDistance = h81;
                    closestIndex = 81;
                }
            }
            if(exists82 && dist92 + 1 < dist82)
            {
                dist82 = dist92 + 1;
                lookup[indexToLocalY(82)][indexToLocalX(82)] = Direction.SOUTHWEST;
                if(h82 < closestDistance)
                {
                    closestDistance = h82;
                    closestIndex = 82;
                }
            }
            if(exists91 && dist92 + 1 < dist91)
            {
                dist91 = dist92 + 1;
                lookup[indexToLocalY(91)][indexToLocalX(91)] = Direction.EAST;
                if(h91 < closestDistance)
                {
                    closestDistance = h91;
                    closestIndex = 91;
                }
            }
            if(exists93 && dist92 + 1 < dist93)
            {
                dist93 = dist92 + 1;
                lookup[indexToLocalY(93)][indexToLocalX(93)] = Direction.WEST;
                if(h93 < closestDistance)
                {
                    closestDistance = h93;
                    closestIndex = 93;
                }
            }
            if(exists102 && dist92 + 1 < dist102)
            {
                dist102 = dist92 + 1;
                lookup[indexToLocalY(102)][indexToLocalX(102)] = Direction.NORTHEAST;
                if(h102 < closestDistance)
                {
                    closestDistance = h102;
                    closestIndex = 102;
                }
            }
            if(exists103 && dist92 + 1 < dist103)
            {
                dist103 = dist92 + 1;
                lookup[indexToLocalY(103)][indexToLocalX(103)] = Direction.NORTH;
                if(h103 < closestDistance)
                {
                    closestDistance = h103;
                    closestIndex = 103;
                }
            }
            if(exists104 && dist92 + 1 < dist104)
            {
                dist104 = dist92 + 1;
                lookup[indexToLocalY(104)][indexToLocalX(104)] = Direction.NORTHWEST;
                if(h104 < closestDistance)
                {
                    closestDistance = h104;
                    closestIndex = 104;
                }
            }
        }
        if(exists93)
        {
            if(exists81 && dist93 + 1 < dist81)
            {
                dist81 = dist93 + 1;
                lookup[indexToLocalY(81)][indexToLocalX(81)] = Direction.SOUTHEAST;
                if(h81 < closestDistance)
                {
                    closestDistance = h81;
                    closestIndex = 81;
                }
            }
            if(exists82 && dist93 + 1 < dist82)
            {
                dist82 = dist93 + 1;
                lookup[indexToLocalY(82)][indexToLocalX(82)] = Direction.SOUTH;
                if(h82 < closestDistance)
                {
                    closestDistance = h82;
                    closestIndex = 82;
                }
            }
            if(exists83 && dist93 + 1 < dist83)
            {
                dist83 = dist93 + 1;
                lookup[indexToLocalY(83)][indexToLocalX(83)] = Direction.SOUTHWEST;
                if(h83 < closestDistance)
                {
                    closestDistance = h83;
                    closestIndex = 83;
                }
            }
            if(exists92 && dist93 + 1 < dist92)
            {
                dist92 = dist93 + 1;
                lookup[indexToLocalY(92)][indexToLocalX(92)] = Direction.EAST;
                if(h92 < closestDistance)
                {
                    closestDistance = h92;
                    closestIndex = 92;
                }
            }
            if(exists94 && dist93 + 1 < dist94)
            {
                dist94 = dist93 + 1;
                lookup[indexToLocalY(94)][indexToLocalX(94)] = Direction.WEST;
                if(h94 < closestDistance)
                {
                    closestDistance = h94;
                    closestIndex = 94;
                }
            }
            if(exists103 && dist93 + 1 < dist103)
            {
                dist103 = dist93 + 1;
                lookup[indexToLocalY(103)][indexToLocalX(103)] = Direction.NORTHEAST;
                if(h103 < closestDistance)
                {
                    closestDistance = h103;
                    closestIndex = 103;
                }
            }
            if(exists104 && dist93 + 1 < dist104)
            {
                dist104 = dist93 + 1;
                lookup[indexToLocalY(104)][indexToLocalX(104)] = Direction.NORTH;
                if(h104 < closestDistance)
                {
                    closestDistance = h104;
                    closestIndex = 104;
                }
            }
            if(exists105 && dist93 + 1 < dist105)
            {
                dist105 = dist93 + 1;
                lookup[indexToLocalY(105)][indexToLocalX(105)] = Direction.NORTHWEST;
                if(h105 < closestDistance)
                {
                    closestDistance = h105;
                    closestIndex = 105;
                }
            }
        }
        if(exists94)
        {
            if(exists82 && dist94 + 1 < dist82)
            {
                dist82 = dist94 + 1;
                lookup[indexToLocalY(82)][indexToLocalX(82)] = Direction.SOUTHEAST;
                if(h82 < closestDistance)
                {
                    closestDistance = h82;
                    closestIndex = 82;
                }
            }
            if(exists83 && dist94 + 1 < dist83)
            {
                dist83 = dist94 + 1;
                lookup[indexToLocalY(83)][indexToLocalX(83)] = Direction.SOUTH;
                if(h83 < closestDistance)
                {
                    closestDistance = h83;
                    closestIndex = 83;
                }
            }
            if(exists84 && dist94 + 1 < dist84)
            {
                dist84 = dist94 + 1;
                lookup[indexToLocalY(84)][indexToLocalX(84)] = Direction.SOUTHWEST;
                if(h84 < closestDistance)
                {
                    closestDistance = h84;
                    closestIndex = 84;
                }
            }
            if(exists93 && dist94 + 1 < dist93)
            {
                dist93 = dist94 + 1;
                lookup[indexToLocalY(93)][indexToLocalX(93)] = Direction.EAST;
                if(h93 < closestDistance)
                {
                    closestDistance = h93;
                    closestIndex = 93;
                }
            }
            if(exists95 && dist94 + 1 < dist95)
            {
                dist95 = dist94 + 1;
                lookup[indexToLocalY(95)][indexToLocalX(95)] = Direction.WEST;
                if(h95 < closestDistance)
                {
                    closestDistance = h95;
                    closestIndex = 95;
                }
            }
            if(exists104 && dist94 + 1 < dist104)
            {
                dist104 = dist94 + 1;
                lookup[indexToLocalY(104)][indexToLocalX(104)] = Direction.NORTHEAST;
                if(h104 < closestDistance)
                {
                    closestDistance = h104;
                    closestIndex = 104;
                }
            }
            if(exists105 && dist94 + 1 < dist105)
            {
                dist105 = dist94 + 1;
                lookup[indexToLocalY(105)][indexToLocalX(105)] = Direction.NORTH;
                if(h105 < closestDistance)
                {
                    closestDistance = h105;
                    closestIndex = 105;
                }
            }
            if(exists106 && dist94 + 1 < dist106)
            {
                dist106 = dist94 + 1;
                lookup[indexToLocalY(106)][indexToLocalX(106)] = Direction.NORTHWEST;
                if(h106 < closestDistance)
                {
                    closestDistance = h106;
                    closestIndex = 106;
                }
            }
        }
        if(exists95)
        {
            if(exists83 && dist95 + 1 < dist83)
            {
                dist83 = dist95 + 1;
                lookup[indexToLocalY(83)][indexToLocalX(83)] = Direction.SOUTHEAST;
                if(h83 < closestDistance)
                {
                    closestDistance = h83;
                    closestIndex = 83;
                }
            }
            if(exists84 && dist95 + 1 < dist84)
            {
                dist84 = dist95 + 1;
                lookup[indexToLocalY(84)][indexToLocalX(84)] = Direction.SOUTH;
                if(h84 < closestDistance)
                {
                    closestDistance = h84;
                    closestIndex = 84;
                }
            }
            if(exists85 && dist95 + 1 < dist85)
            {
                dist85 = dist95 + 1;
                lookup[indexToLocalY(85)][indexToLocalX(85)] = Direction.SOUTHWEST;
                if(h85 < closestDistance)
                {
                    closestDistance = h85;
                    closestIndex = 85;
                }
            }
            if(exists94 && dist95 + 1 < dist94)
            {
                dist94 = dist95 + 1;
                lookup[indexToLocalY(94)][indexToLocalX(94)] = Direction.EAST;
                if(h94 < closestDistance)
                {
                    closestDistance = h94;
                    closestIndex = 94;
                }
            }
            if(exists96 && dist95 + 1 < dist96)
            {
                dist96 = dist95 + 1;
                lookup[indexToLocalY(96)][indexToLocalX(96)] = Direction.WEST;
                if(h96 < closestDistance)
                {
                    closestDistance = h96;
                    closestIndex = 96;
                }
            }
            if(exists105 && dist95 + 1 < dist105)
            {
                dist105 = dist95 + 1;
                lookup[indexToLocalY(105)][indexToLocalX(105)] = Direction.NORTHEAST;
                if(h105 < closestDistance)
                {
                    closestDistance = h105;
                    closestIndex = 105;
                }
            }
            if(exists106 && dist95 + 1 < dist106)
            {
                dist106 = dist95 + 1;
                lookup[indexToLocalY(106)][indexToLocalX(106)] = Direction.NORTH;
                if(h106 < closestDistance)
                {
                    closestDistance = h106;
                    closestIndex = 106;
                }
            }
            if(exists107 && dist95 + 1 < dist107)
            {
                dist107 = dist95 + 1;
                lookup[indexToLocalY(107)][indexToLocalX(107)] = Direction.NORTHWEST;
                if(h107 < closestDistance)
                {
                    closestDistance = h107;
                    closestIndex = 107;
                }
            }
        }
        if(exists96)
        {
            if(exists84 && dist96 + 1 < dist84)
            {
                dist84 = dist96 + 1;
                lookup[indexToLocalY(84)][indexToLocalX(84)] = Direction.SOUTHEAST;
                if(h84 < closestDistance)
                {
                    closestDistance = h84;
                    closestIndex = 84;
                }
            }
            if(exists85 && dist96 + 1 < dist85)
            {
                dist85 = dist96 + 1;
                lookup[indexToLocalY(85)][indexToLocalX(85)] = Direction.SOUTH;
                if(h85 < closestDistance)
                {
                    closestDistance = h85;
                    closestIndex = 85;
                }
            }
            if(exists86 && dist96 + 1 < dist86)
            {
                dist86 = dist96 + 1;
                lookup[indexToLocalY(86)][indexToLocalX(86)] = Direction.SOUTHWEST;
                if(h86 < closestDistance)
                {
                    closestDistance = h86;
                    closestIndex = 86;
                }
            }
            if(exists95 && dist96 + 1 < dist95)
            {
                dist95 = dist96 + 1;
                lookup[indexToLocalY(95)][indexToLocalX(95)] = Direction.EAST;
                if(h95 < closestDistance)
                {
                    closestDistance = h95;
                    closestIndex = 95;
                }
            }
            if(exists97 && dist96 + 1 < dist97)
            {
                dist97 = dist96 + 1;
                lookup[indexToLocalY(97)][indexToLocalX(97)] = Direction.WEST;
                if(h97 < closestDistance)
                {
                    closestDistance = h97;
                    closestIndex = 97;
                }
            }
            if(exists106 && dist96 + 1 < dist106)
            {
                dist106 = dist96 + 1;
                lookup[indexToLocalY(106)][indexToLocalX(106)] = Direction.NORTHEAST;
                if(h106 < closestDistance)
                {
                    closestDistance = h106;
                    closestIndex = 106;
                }
            }
            if(exists107 && dist96 + 1 < dist107)
            {
                dist107 = dist96 + 1;
                lookup[indexToLocalY(107)][indexToLocalX(107)] = Direction.NORTH;
                if(h107 < closestDistance)
                {
                    closestDistance = h107;
                    closestIndex = 107;
                }
            }
            if(exists108 && dist96 + 1 < dist108)
            {
                dist108 = dist96 + 1;
                lookup[indexToLocalY(108)][indexToLocalX(108)] = Direction.NORTHWEST;
                if(h108 < closestDistance)
                {
                    closestDistance = h108;
                    closestIndex = 108;
                }
            }
        }
        if(exists12)
        {
            if(exists0 && dist12 + 1 < dist0)
            {
                dist0 = dist12 + 1;
                lookup[indexToLocalY(0)][indexToLocalX(0)] = Direction.SOUTHEAST;
                if(h0 < closestDistance)
                {
                    closestDistance = h0;
                    closestIndex = 0;
                }
            }
            if(exists1 && dist12 + 1 < dist1)
            {
                dist1 = dist12 + 1;
                lookup[indexToLocalY(1)][indexToLocalX(1)] = Direction.SOUTH;
                if(h1 < closestDistance)
                {
                    closestDistance = h1;
                    closestIndex = 1;
                }
            }
            if(exists2 && dist12 + 1 < dist2)
            {
                dist2 = dist12 + 1;
                lookup[indexToLocalY(2)][indexToLocalX(2)] = Direction.SOUTHWEST;
                if(h2 < closestDistance)
                {
                    closestDistance = h2;
                    closestIndex = 2;
                }
            }
            if(exists11 && dist12 + 1 < dist11)
            {
                dist11 = dist12 + 1;
                lookup[indexToLocalY(11)][indexToLocalX(11)] = Direction.EAST;
                if(h11 < closestDistance)
                {
                    closestDistance = h11;
                    closestIndex = 11;
                }
            }
            if(exists13 && dist12 + 1 < dist13)
            {
                dist13 = dist12 + 1;
                lookup[indexToLocalY(13)][indexToLocalX(13)] = Direction.WEST;
                if(h13 < closestDistance)
                {
                    closestDistance = h13;
                    closestIndex = 13;
                }
            }
            if(exists22 && dist12 + 1 < dist22)
            {
                dist22 = dist12 + 1;
                lookup[indexToLocalY(22)][indexToLocalX(22)] = Direction.NORTHEAST;
                if(h22 < closestDistance)
                {
                    closestDistance = h22;
                    closestIndex = 22;
                }
            }
            if(exists23 && dist12 + 1 < dist23)
            {
                dist23 = dist12 + 1;
                lookup[indexToLocalY(23)][indexToLocalX(23)] = Direction.NORTH;
                if(h23 < closestDistance)
                {
                    closestDistance = h23;
                    closestIndex = 23;
                }
            }
            if(exists24 && dist12 + 1 < dist24)
            {
                dist24 = dist12 + 1;
                lookup[indexToLocalY(24)][indexToLocalX(24)] = Direction.NORTHWEST;
                if(h24 < closestDistance)
                {
                    closestDistance = h24;
                    closestIndex = 24;
                }
            }
        }
        if(exists13)
        {
            if(exists1 && dist13 + 1 < dist1)
            {
                dist1 = dist13 + 1;
                lookup[indexToLocalY(1)][indexToLocalX(1)] = Direction.SOUTHEAST;
                if(h1 < closestDistance)
                {
                    closestDistance = h1;
                    closestIndex = 1;
                }
            }
            if(exists2 && dist13 + 1 < dist2)
            {
                dist2 = dist13 + 1;
                lookup[indexToLocalY(2)][indexToLocalX(2)] = Direction.SOUTH;
                if(h2 < closestDistance)
                {
                    closestDistance = h2;
                    closestIndex = 2;
                }
            }
            if(exists3 && dist13 + 1 < dist3)
            {
                dist3 = dist13 + 1;
                lookup[indexToLocalY(3)][indexToLocalX(3)] = Direction.SOUTHWEST;
                if(h3 < closestDistance)
                {
                    closestDistance = h3;
                    closestIndex = 3;
                }
            }
            if(exists12 && dist13 + 1 < dist12)
            {
                dist12 = dist13 + 1;
                lookup[indexToLocalY(12)][indexToLocalX(12)] = Direction.EAST;
                if(h12 < closestDistance)
                {
                    closestDistance = h12;
                    closestIndex = 12;
                }
            }
            if(exists14 && dist13 + 1 < dist14)
            {
                dist14 = dist13 + 1;
                lookup[indexToLocalY(14)][indexToLocalX(14)] = Direction.WEST;
                if(h14 < closestDistance)
                {
                    closestDistance = h14;
                    closestIndex = 14;
                }
            }
            if(exists23 && dist13 + 1 < dist23)
            {
                dist23 = dist13 + 1;
                lookup[indexToLocalY(23)][indexToLocalX(23)] = Direction.NORTHEAST;
                if(h23 < closestDistance)
                {
                    closestDistance = h23;
                    closestIndex = 23;
                }
            }
            if(exists24 && dist13 + 1 < dist24)
            {
                dist24 = dist13 + 1;
                lookup[indexToLocalY(24)][indexToLocalX(24)] = Direction.NORTH;
                if(h24 < closestDistance)
                {
                    closestDistance = h24;
                    closestIndex = 24;
                }
            }
            if(exists25 && dist13 + 1 < dist25)
            {
                dist25 = dist13 + 1;
                lookup[indexToLocalY(25)][indexToLocalX(25)] = Direction.NORTHWEST;
                if(h25 < closestDistance)
                {
                    closestDistance = h25;
                    closestIndex = 25;
                }
            }
        }
        if(exists14)
        {
            if(exists2 && dist14 + 1 < dist2)
            {
                dist2 = dist14 + 1;
                lookup[indexToLocalY(2)][indexToLocalX(2)] = Direction.SOUTHEAST;
                if(h2 < closestDistance)
                {
                    closestDistance = h2;
                    closestIndex = 2;
                }
            }
            if(exists3 && dist14 + 1 < dist3)
            {
                dist3 = dist14 + 1;
                lookup[indexToLocalY(3)][indexToLocalX(3)] = Direction.SOUTH;
                if(h3 < closestDistance)
                {
                    closestDistance = h3;
                    closestIndex = 3;
                }
            }
            if(exists4 && dist14 + 1 < dist4)
            {
                dist4 = dist14 + 1;
                lookup[indexToLocalY(4)][indexToLocalX(4)] = Direction.SOUTHWEST;
                if(h4 < closestDistance)
                {
                    closestDistance = h4;
                    closestIndex = 4;
                }
            }
            if(exists13 && dist14 + 1 < dist13)
            {
                dist13 = dist14 + 1;
                lookup[indexToLocalY(13)][indexToLocalX(13)] = Direction.EAST;
                if(h13 < closestDistance)
                {
                    closestDistance = h13;
                    closestIndex = 13;
                }
            }
            if(exists15 && dist14 + 1 < dist15)
            {
                dist15 = dist14 + 1;
                lookup[indexToLocalY(15)][indexToLocalX(15)] = Direction.WEST;
                if(h15 < closestDistance)
                {
                    closestDistance = h15;
                    closestIndex = 15;
                }
            }
            if(exists24 && dist14 + 1 < dist24)
            {
                dist24 = dist14 + 1;
                lookup[indexToLocalY(24)][indexToLocalX(24)] = Direction.NORTHEAST;
                if(h24 < closestDistance)
                {
                    closestDistance = h24;
                    closestIndex = 24;
                }
            }
            if(exists25 && dist14 + 1 < dist25)
            {
                dist25 = dist14 + 1;
                lookup[indexToLocalY(25)][indexToLocalX(25)] = Direction.NORTH;
                if(h25 < closestDistance)
                {
                    closestDistance = h25;
                    closestIndex = 25;
                }
            }
            if(exists26 && dist14 + 1 < dist26)
            {
                dist26 = dist14 + 1;
                lookup[indexToLocalY(26)][indexToLocalX(26)] = Direction.NORTHWEST;
                if(h26 < closestDistance)
                {
                    closestDistance = h26;
                    closestIndex = 26;
                }
            }
        }
        if(exists23)
        {
            if(exists11 && dist23 + 1 < dist11)
            {
                dist11 = dist23 + 1;
                lookup[indexToLocalY(11)][indexToLocalX(11)] = Direction.SOUTHEAST;
                if(h11 < closestDistance)
                {
                    closestDistance = h11;
                    closestIndex = 11;
                }
            }
            if(exists12 && dist23 + 1 < dist12)
            {
                dist12 = dist23 + 1;
                lookup[indexToLocalY(12)][indexToLocalX(12)] = Direction.SOUTH;
                if(h12 < closestDistance)
                {
                    closestDistance = h12;
                    closestIndex = 12;
                }
            }
            if(exists13 && dist23 + 1 < dist13)
            {
                dist13 = dist23 + 1;
                lookup[indexToLocalY(13)][indexToLocalX(13)] = Direction.SOUTHWEST;
                if(h13 < closestDistance)
                {
                    closestDistance = h13;
                    closestIndex = 13;
                }
            }
            if(exists22 && dist23 + 1 < dist22)
            {
                dist22 = dist23 + 1;
                lookup[indexToLocalY(22)][indexToLocalX(22)] = Direction.EAST;
                if(h22 < closestDistance)
                {
                    closestDistance = h22;
                    closestIndex = 22;
                }
            }
            if(exists24 && dist23 + 1 < dist24)
            {
                dist24 = dist23 + 1;
                lookup[indexToLocalY(24)][indexToLocalX(24)] = Direction.WEST;
                if(h24 < closestDistance)
                {
                    closestDistance = h24;
                    closestIndex = 24;
                }
            }
            if(exists33 && dist23 + 1 < dist33)
            {
                dist33 = dist23 + 1;
                lookup[indexToLocalY(33)][indexToLocalX(33)] = Direction.NORTHEAST;
                if(h33 < closestDistance)
                {
                    closestDistance = h33;
                    closestIndex = 33;
                }
            }
            if(exists34 && dist23 + 1 < dist34)
            {
                dist34 = dist23 + 1;
                lookup[indexToLocalY(34)][indexToLocalX(34)] = Direction.NORTH;
                if(h34 < closestDistance)
                {
                    closestDistance = h34;
                    closestIndex = 34;
                }
            }
            if(exists35 && dist23 + 1 < dist35)
            {
                dist35 = dist23 + 1;
                lookup[indexToLocalY(35)][indexToLocalX(35)] = Direction.NORTHWEST;
                if(h35 < closestDistance)
                {
                    closestDistance = h35;
                    closestIndex = 35;
                }
            }
        }
        if(exists34)
        {
            if(exists22 && dist34 + 1 < dist22)
            {
                dist22 = dist34 + 1;
                lookup[indexToLocalY(22)][indexToLocalX(22)] = Direction.SOUTHEAST;
                if(h22 < closestDistance)
                {
                    closestDistance = h22;
                    closestIndex = 22;
                }
            }
            if(exists23 && dist34 + 1 < dist23)
            {
                dist23 = dist34 + 1;
                lookup[indexToLocalY(23)][indexToLocalX(23)] = Direction.SOUTH;
                if(h23 < closestDistance)
                {
                    closestDistance = h23;
                    closestIndex = 23;
                }
            }
            if(exists24 && dist34 + 1 < dist24)
            {
                dist24 = dist34 + 1;
                lookup[indexToLocalY(24)][indexToLocalX(24)] = Direction.SOUTHWEST;
                if(h24 < closestDistance)
                {
                    closestDistance = h24;
                    closestIndex = 24;
                }
            }
            if(exists33 && dist34 + 1 < dist33)
            {
                dist33 = dist34 + 1;
                lookup[indexToLocalY(33)][indexToLocalX(33)] = Direction.EAST;
                if(h33 < closestDistance)
                {
                    closestDistance = h33;
                    closestIndex = 33;
                }
            }
            if(exists35 && dist34 + 1 < dist35)
            {
                dist35 = dist34 + 1;
                lookup[indexToLocalY(35)][indexToLocalX(35)] = Direction.WEST;
                if(h35 < closestDistance)
                {
                    closestDistance = h35;
                    closestIndex = 35;
                }
            }
            if(exists44 && dist34 + 1 < dist44)
            {
                dist44 = dist34 + 1;
                lookup[indexToLocalY(44)][indexToLocalX(44)] = Direction.NORTHEAST;
                if(h44 < closestDistance)
                {
                    closestDistance = h44;
                    closestIndex = 44;
                }
            }
            if(exists45 && dist34 + 1 < dist45)
            {
                dist45 = dist34 + 1;
                lookup[indexToLocalY(45)][indexToLocalX(45)] = Direction.NORTH;
                if(h45 < closestDistance)
                {
                    closestDistance = h45;
                    closestIndex = 45;
                }
            }
            if(exists46 && dist34 + 1 < dist46)
            {
                dist46 = dist34 + 1;
                lookup[indexToLocalY(46)][indexToLocalX(46)] = Direction.NORTHWEST;
                if(h46 < closestDistance)
                {
                    closestDistance = h46;
                    closestIndex = 46;
                }
            }
        }
        if(exists15)
        {
            if(exists3 && dist15 + 1 < dist3)
            {
                dist3 = dist15 + 1;
                lookup[indexToLocalY(3)][indexToLocalX(3)] = Direction.SOUTHEAST;
                if(h3 < closestDistance)
                {
                    closestDistance = h3;
                    closestIndex = 3;
                }
            }
            if(exists4 && dist15 + 1 < dist4)
            {
                dist4 = dist15 + 1;
                lookup[indexToLocalY(4)][indexToLocalX(4)] = Direction.SOUTH;
                if(h4 < closestDistance)
                {
                    closestDistance = h4;
                    closestIndex = 4;
                }
            }
            if(exists5 && dist15 + 1 < dist5)
            {
                dist5 = dist15 + 1;
                lookup[indexToLocalY(5)][indexToLocalX(5)] = Direction.SOUTHWEST;
                if(h5 < closestDistance)
                {
                    closestDistance = h5;
                    closestIndex = 5;
                }
            }
            if(exists14 && dist15 + 1 < dist14)
            {
                dist14 = dist15 + 1;
                lookup[indexToLocalY(14)][indexToLocalX(14)] = Direction.EAST;
                if(h14 < closestDistance)
                {
                    closestDistance = h14;
                    closestIndex = 14;
                }
            }
            if(exists16 && dist15 + 1 < dist16)
            {
                dist16 = dist15 + 1;
                lookup[indexToLocalY(16)][indexToLocalX(16)] = Direction.WEST;
                if(h16 < closestDistance)
                {
                    closestDistance = h16;
                    closestIndex = 16;
                }
            }
            if(exists25 && dist15 + 1 < dist25)
            {
                dist25 = dist15 + 1;
                lookup[indexToLocalY(25)][indexToLocalX(25)] = Direction.NORTHEAST;
                if(h25 < closestDistance)
                {
                    closestDistance = h25;
                    closestIndex = 25;
                }
            }
            if(exists26 && dist15 + 1 < dist26)
            {
                dist26 = dist15 + 1;
                lookup[indexToLocalY(26)][indexToLocalX(26)] = Direction.NORTH;
                if(h26 < closestDistance)
                {
                    closestDistance = h26;
                    closestIndex = 26;
                }
            }
            if(exists27 && dist15 + 1 < dist27)
            {
                dist27 = dist15 + 1;
                lookup[indexToLocalY(27)][indexToLocalX(27)] = Direction.NORTHWEST;
                if(h27 < closestDistance)
                {
                    closestDistance = h27;
                    closestIndex = 27;
                }
            }
        }
        if(exists16)
        {
            if(exists4 && dist16 + 1 < dist4)
            {
                dist4 = dist16 + 1;
                lookup[indexToLocalY(4)][indexToLocalX(4)] = Direction.SOUTHEAST;
                if(h4 < closestDistance)
                {
                    closestDistance = h4;
                    closestIndex = 4;
                }
            }
            if(exists5 && dist16 + 1 < dist5)
            {
                dist5 = dist16 + 1;
                lookup[indexToLocalY(5)][indexToLocalX(5)] = Direction.SOUTH;
                if(h5 < closestDistance)
                {
                    closestDistance = h5;
                    closestIndex = 5;
                }
            }
            if(exists6 && dist16 + 1 < dist6)
            {
                dist6 = dist16 + 1;
                lookup[indexToLocalY(6)][indexToLocalX(6)] = Direction.SOUTHWEST;
                if(h6 < closestDistance)
                {
                    closestDistance = h6;
                    closestIndex = 6;
                }
            }
            if(exists15 && dist16 + 1 < dist15)
            {
                dist15 = dist16 + 1;
                lookup[indexToLocalY(15)][indexToLocalX(15)] = Direction.EAST;
                if(h15 < closestDistance)
                {
                    closestDistance = h15;
                    closestIndex = 15;
                }
            }
            if(exists17 && dist16 + 1 < dist17)
            {
                dist17 = dist16 + 1;
                lookup[indexToLocalY(17)][indexToLocalX(17)] = Direction.WEST;
                if(h17 < closestDistance)
                {
                    closestDistance = h17;
                    closestIndex = 17;
                }
            }
            if(exists26 && dist16 + 1 < dist26)
            {
                dist26 = dist16 + 1;
                lookup[indexToLocalY(26)][indexToLocalX(26)] = Direction.NORTHEAST;
                if(h26 < closestDistance)
                {
                    closestDistance = h26;
                    closestIndex = 26;
                }
            }
            if(exists27 && dist16 + 1 < dist27)
            {
                dist27 = dist16 + 1;
                lookup[indexToLocalY(27)][indexToLocalX(27)] = Direction.NORTH;
                if(h27 < closestDistance)
                {
                    closestDistance = h27;
                    closestIndex = 27;
                }
            }
            if(exists28 && dist16 + 1 < dist28)
            {
                dist28 = dist16 + 1;
                lookup[indexToLocalY(28)][indexToLocalX(28)] = Direction.NORTHWEST;
                if(h28 < closestDistance)
                {
                    closestDistance = h28;
                    closestIndex = 28;
                }
            }
        }
        if(exists45)
        {
            if(exists33 && dist45 + 1 < dist33)
            {
                dist33 = dist45 + 1;
                lookup[indexToLocalY(33)][indexToLocalX(33)] = Direction.SOUTHEAST;
                if(h33 < closestDistance)
                {
                    closestDistance = h33;
                    closestIndex = 33;
                }
            }
            if(exists34 && dist45 + 1 < dist34)
            {
                dist34 = dist45 + 1;
                lookup[indexToLocalY(34)][indexToLocalX(34)] = Direction.SOUTH;
                if(h34 < closestDistance)
                {
                    closestDistance = h34;
                    closestIndex = 34;
                }
            }
            if(exists35 && dist45 + 1 < dist35)
            {
                dist35 = dist45 + 1;
                lookup[indexToLocalY(35)][indexToLocalX(35)] = Direction.SOUTHWEST;
                if(h35 < closestDistance)
                {
                    closestDistance = h35;
                    closestIndex = 35;
                }
            }
            if(exists44 && dist45 + 1 < dist44)
            {
                dist44 = dist45 + 1;
                lookup[indexToLocalY(44)][indexToLocalX(44)] = Direction.EAST;
                if(h44 < closestDistance)
                {
                    closestDistance = h44;
                    closestIndex = 44;
                }
            }
            if(exists46 && dist45 + 1 < dist46)
            {
                dist46 = dist45 + 1;
                lookup[indexToLocalY(46)][indexToLocalX(46)] = Direction.WEST;
                if(h46 < closestDistance)
                {
                    closestDistance = h46;
                    closestIndex = 46;
                }
            }
            if(exists55 && dist45 + 1 < dist55)
            {
                dist55 = dist45 + 1;
                lookup[indexToLocalY(55)][indexToLocalX(55)] = Direction.NORTHEAST;
                if(h55 < closestDistance)
                {
                    closestDistance = h55;
                    closestIndex = 55;
                }
            }
            if(exists56 && dist45 + 1 < dist56)
            {
                dist56 = dist45 + 1;
                lookup[indexToLocalY(56)][indexToLocalX(56)] = Direction.NORTH;
                if(h56 < closestDistance)
                {
                    closestDistance = h56;
                    closestIndex = 56;
                }
            }
            if(exists57 && dist45 + 1 < dist57)
            {
                dist57 = dist45 + 1;
                lookup[indexToLocalY(57)][indexToLocalX(57)] = Direction.NORTHWEST;
                if(h57 < closestDistance)
                {
                    closestDistance = h57;
                    closestIndex = 57;
                }
            }
        }
    }

    private static void method2()
    {
        if(exists62)
        {
            if(exists50 && dist62 + 1 < dist50)
            {
                dist50 = dist62 + 1;
                lookup[indexToLocalY(50)][indexToLocalX(50)] = Direction.SOUTHEAST;
                if(h50 < closestDistance)
                {
                    closestDistance = h50;
                    closestIndex = 50;
                }
            }
            if(exists51 && dist62 + 1 < dist51)
            {
                dist51 = dist62 + 1;
                lookup[indexToLocalY(51)][indexToLocalX(51)] = Direction.SOUTH;
                if(h51 < closestDistance)
                {
                    closestDistance = h51;
                    closestIndex = 51;
                }
            }
            if(exists52 && dist62 + 1 < dist52)
            {
                dist52 = dist62 + 1;
                lookup[indexToLocalY(52)][indexToLocalX(52)] = Direction.SOUTHWEST;
                if(h52 < closestDistance)
                {
                    closestDistance = h52;
                    closestIndex = 52;
                }
            }
            if(exists61 && dist62 + 1 < dist61)
            {
                dist61 = dist62 + 1;
                lookup[indexToLocalY(61)][indexToLocalX(61)] = Direction.EAST;
                if(h61 < closestDistance)
                {
                    closestDistance = h61;
                    closestIndex = 61;
                }
            }
            if(exists63 && dist62 + 1 < dist63)
            {
                dist63 = dist62 + 1;
                lookup[indexToLocalY(63)][indexToLocalX(63)] = Direction.WEST;
                if(h63 < closestDistance)
                {
                    closestDistance = h63;
                    closestIndex = 63;
                }
            }
            if(exists72 && dist62 + 1 < dist72)
            {
                dist72 = dist62 + 1;
                lookup[indexToLocalY(72)][indexToLocalX(72)] = Direction.NORTHEAST;
                if(h72 < closestDistance)
                {
                    closestDistance = h72;
                    closestIndex = 72;
                }
            }
            if(exists73 && dist62 + 1 < dist73)
            {
                dist73 = dist62 + 1;
                lookup[indexToLocalY(73)][indexToLocalX(73)] = Direction.NORTH;
                if(h73 < closestDistance)
                {
                    closestDistance = h73;
                    closestIndex = 73;
                }
            }
            if(exists74 && dist62 + 1 < dist74)
            {
                dist74 = dist62 + 1;
                lookup[indexToLocalY(74)][indexToLocalX(74)] = Direction.NORTHWEST;
                if(h74 < closestDistance)
                {
                    closestDistance = h74;
                    closestIndex = 74;
                }
            }
        }
        if(exists69)
        {
            if(exists57 && dist69 + 1 < dist57)
            {
                dist57 = dist69 + 1;
                lookup[indexToLocalY(57)][indexToLocalX(57)] = Direction.SOUTHEAST;
                if(h57 < closestDistance)
                {
                    closestDistance = h57;
                    closestIndex = 57;
                }
            }
            if(exists58 && dist69 + 1 < dist58)
            {
                dist58 = dist69 + 1;
                lookup[indexToLocalY(58)][indexToLocalX(58)] = Direction.SOUTH;
                if(h58 < closestDistance)
                {
                    closestDistance = h58;
                    closestIndex = 58;
                }
            }
            if(exists59 && dist69 + 1 < dist59)
            {
                dist59 = dist69 + 1;
                lookup[indexToLocalY(59)][indexToLocalX(59)] = Direction.SOUTHWEST;
                if(h59 < closestDistance)
                {
                    closestDistance = h59;
                    closestIndex = 59;
                }
            }
            if(exists68 && dist69 + 1 < dist68)
            {
                dist68 = dist69 + 1;
                lookup[indexToLocalY(68)][indexToLocalX(68)] = Direction.EAST;
                if(h68 < closestDistance)
                {
                    closestDistance = h68;
                    closestIndex = 68;
                }
            }
            if(exists70 && dist69 + 1 < dist70)
            {
                dist70 = dist69 + 1;
                lookup[indexToLocalY(70)][indexToLocalX(70)] = Direction.WEST;
                if(h70 < closestDistance)
                {
                    closestDistance = h70;
                    closestIndex = 70;
                }
            }
            if(exists79 && dist69 + 1 < dist79)
            {
                dist79 = dist69 + 1;
                lookup[indexToLocalY(79)][indexToLocalX(79)] = Direction.NORTHEAST;
                if(h79 < closestDistance)
                {
                    closestDistance = h79;
                    closestIndex = 79;
                }
            }
            if(exists80 && dist69 + 1 < dist80)
            {
                dist80 = dist69 + 1;
                lookup[indexToLocalY(80)][indexToLocalX(80)] = Direction.NORTH;
                if(h80 < closestDistance)
                {
                    closestDistance = h80;
                    closestIndex = 80;
                }
            }
            if(exists81 && dist69 + 1 < dist81)
            {
                dist81 = dist69 + 1;
                lookup[indexToLocalY(81)][indexToLocalX(81)] = Direction.NORTHWEST;
                if(h81 < closestDistance)
                {
                    closestDistance = h81;
                    closestIndex = 81;
                }
            }
        }
        if(exists73)
        {
            if(exists61 && dist73 + 1 < dist61)
            {
                dist61 = dist73 + 1;
                lookup[indexToLocalY(61)][indexToLocalX(61)] = Direction.SOUTHEAST;
                if(h61 < closestDistance)
                {
                    closestDistance = h61;
                    closestIndex = 61;
                }
            }
            if(exists62 && dist73 + 1 < dist62)
            {
                dist62 = dist73 + 1;
                lookup[indexToLocalY(62)][indexToLocalX(62)] = Direction.SOUTH;
                if(h62 < closestDistance)
                {
                    closestDistance = h62;
                    closestIndex = 62;
                }
            }
            if(exists63 && dist73 + 1 < dist63)
            {
                dist63 = dist73 + 1;
                lookup[indexToLocalY(63)][indexToLocalX(63)] = Direction.SOUTHWEST;
                if(h63 < closestDistance)
                {
                    closestDistance = h63;
                    closestIndex = 63;
                }
            }
            if(exists72 && dist73 + 1 < dist72)
            {
                dist72 = dist73 + 1;
                lookup[indexToLocalY(72)][indexToLocalX(72)] = Direction.EAST;
                if(h72 < closestDistance)
                {
                    closestDistance = h72;
                    closestIndex = 72;
                }
            }
            if(exists74 && dist73 + 1 < dist74)
            {
                dist74 = dist73 + 1;
                lookup[indexToLocalY(74)][indexToLocalX(74)] = Direction.WEST;
                if(h74 < closestDistance)
                {
                    closestDistance = h74;
                    closestIndex = 74;
                }
            }
            if(exists83 && dist73 + 1 < dist83)
            {
                dist83 = dist73 + 1;
                lookup[indexToLocalY(83)][indexToLocalX(83)] = Direction.NORTHEAST;
                if(h83 < closestDistance)
                {
                    closestDistance = h83;
                    closestIndex = 83;
                }
            }
            if(exists84 && dist73 + 1 < dist84)
            {
                dist84 = dist73 + 1;
                lookup[indexToLocalY(84)][indexToLocalX(84)] = Direction.NORTH;
                if(h84 < closestDistance)
                {
                    closestDistance = h84;
                    closestIndex = 84;
                }
            }
            if(exists85 && dist73 + 1 < dist85)
            {
                dist85 = dist73 + 1;
                lookup[indexToLocalY(85)][indexToLocalX(85)] = Direction.NORTHWEST;
                if(h85 < closestDistance)
                {
                    closestDistance = h85;
                    closestIndex = 85;
                }
            }
        }
        if(exists80)
        {
            if(exists68 && dist80 + 1 < dist68)
            {
                dist68 = dist80 + 1;
                lookup[indexToLocalY(68)][indexToLocalX(68)] = Direction.SOUTHEAST;
                if(h68 < closestDistance)
                {
                    closestDistance = h68;
                    closestIndex = 68;
                }
            }
            if(exists69 && dist80 + 1 < dist69)
            {
                dist69 = dist80 + 1;
                lookup[indexToLocalY(69)][indexToLocalX(69)] = Direction.SOUTH;
                if(h69 < closestDistance)
                {
                    closestDistance = h69;
                    closestIndex = 69;
                }
            }
            if(exists70 && dist80 + 1 < dist70)
            {
                dist70 = dist80 + 1;
                lookup[indexToLocalY(70)][indexToLocalX(70)] = Direction.SOUTHWEST;
                if(h70 < closestDistance)
                {
                    closestDistance = h70;
                    closestIndex = 70;
                }
            }
            if(exists79 && dist80 + 1 < dist79)
            {
                dist79 = dist80 + 1;
                lookup[indexToLocalY(79)][indexToLocalX(79)] = Direction.EAST;
                if(h79 < closestDistance)
                {
                    closestDistance = h79;
                    closestIndex = 79;
                }
            }
            if(exists81 && dist80 + 1 < dist81)
            {
                dist81 = dist80 + 1;
                lookup[indexToLocalY(81)][indexToLocalX(81)] = Direction.WEST;
                if(h81 < closestDistance)
                {
                    closestDistance = h81;
                    closestIndex = 81;
                }
            }
            if(exists90 && dist80 + 1 < dist90)
            {
                dist90 = dist80 + 1;
                lookup[indexToLocalY(90)][indexToLocalX(90)] = Direction.NORTHEAST;
                if(h90 < closestDistance)
                {
                    closestDistance = h90;
                    closestIndex = 90;
                }
            }
            if(exists91 && dist80 + 1 < dist91)
            {
                dist91 = dist80 + 1;
                lookup[indexToLocalY(91)][indexToLocalX(91)] = Direction.NORTH;
                if(h91 < closestDistance)
                {
                    closestDistance = h91;
                    closestIndex = 91;
                }
            }
            if(exists92 && dist80 + 1 < dist92)
            {
                dist92 = dist80 + 1;
                lookup[indexToLocalY(92)][indexToLocalX(92)] = Direction.NORTHWEST;
                if(h92 < closestDistance)
                {
                    closestDistance = h92;
                    closestIndex = 92;
                }
            }
        }
        if(exists81)
        {
            if(exists69 && dist81 + 1 < dist69)
            {
                dist69 = dist81 + 1;
                lookup[indexToLocalY(69)][indexToLocalX(69)] = Direction.SOUTHEAST;
                if(h69 < closestDistance)
                {
                    closestDistance = h69;
                    closestIndex = 69;
                }
            }
            if(exists70 && dist81 + 1 < dist70)
            {
                dist70 = dist81 + 1;
                lookup[indexToLocalY(70)][indexToLocalX(70)] = Direction.SOUTH;
                if(h70 < closestDistance)
                {
                    closestDistance = h70;
                    closestIndex = 70;
                }
            }
            if(exists71 && dist81 + 1 < dist71)
            {
                dist71 = dist81 + 1;
                lookup[indexToLocalY(71)][indexToLocalX(71)] = Direction.SOUTHWEST;
                if(h71 < closestDistance)
                {
                    closestDistance = h71;
                    closestIndex = 71;
                }
            }
            if(exists80 && dist81 + 1 < dist80)
            {
                dist80 = dist81 + 1;
                lookup[indexToLocalY(80)][indexToLocalX(80)] = Direction.EAST;
                if(h80 < closestDistance)
                {
                    closestDistance = h80;
                    closestIndex = 80;
                }
            }
            if(exists82 && dist81 + 1 < dist82)
            {
                dist82 = dist81 + 1;
                lookup[indexToLocalY(82)][indexToLocalX(82)] = Direction.WEST;
                if(h82 < closestDistance)
                {
                    closestDistance = h82;
                    closestIndex = 82;
                }
            }
            if(exists91 && dist81 + 1 < dist91)
            {
                dist91 = dist81 + 1;
                lookup[indexToLocalY(91)][indexToLocalX(91)] = Direction.NORTHEAST;
                if(h91 < closestDistance)
                {
                    closestDistance = h91;
                    closestIndex = 91;
                }
            }
            if(exists92 && dist81 + 1 < dist92)
            {
                dist92 = dist81 + 1;
                lookup[indexToLocalY(92)][indexToLocalX(92)] = Direction.NORTH;
                if(h92 < closestDistance)
                {
                    closestDistance = h92;
                    closestIndex = 92;
                }
            }
            if(exists93 && dist81 + 1 < dist93)
            {
                dist93 = dist81 + 1;
                lookup[indexToLocalY(93)][indexToLocalX(93)] = Direction.NORTHWEST;
                if(h93 < closestDistance)
                {
                    closestDistance = h93;
                    closestIndex = 93;
                }
            }
        }
        if(exists82)
        {
            if(exists70 && dist82 + 1 < dist70)
            {
                dist70 = dist82 + 1;
                lookup[indexToLocalY(70)][indexToLocalX(70)] = Direction.SOUTHEAST;
                if(h70 < closestDistance)
                {
                    closestDistance = h70;
                    closestIndex = 70;
                }
            }
            if(exists71 && dist82 + 1 < dist71)
            {
                dist71 = dist82 + 1;
                lookup[indexToLocalY(71)][indexToLocalX(71)] = Direction.SOUTH;
                if(h71 < closestDistance)
                {
                    closestDistance = h71;
                    closestIndex = 71;
                }
            }
            if(exists72 && dist82 + 1 < dist72)
            {
                dist72 = dist82 + 1;
                lookup[indexToLocalY(72)][indexToLocalX(72)] = Direction.SOUTHWEST;
                if(h72 < closestDistance)
                {
                    closestDistance = h72;
                    closestIndex = 72;
                }
            }
            if(exists81 && dist82 + 1 < dist81)
            {
                dist81 = dist82 + 1;
                lookup[indexToLocalY(81)][indexToLocalX(81)] = Direction.EAST;
                if(h81 < closestDistance)
                {
                    closestDistance = h81;
                    closestIndex = 81;
                }
            }
            if(exists83 && dist82 + 1 < dist83)
            {
                dist83 = dist82 + 1;
                lookup[indexToLocalY(83)][indexToLocalX(83)] = Direction.WEST;
                if(h83 < closestDistance)
                {
                    closestDistance = h83;
                    closestIndex = 83;
                }
            }
            if(exists92 && dist82 + 1 < dist92)
            {
                dist92 = dist82 + 1;
                lookup[indexToLocalY(92)][indexToLocalX(92)] = Direction.NORTHEAST;
                if(h92 < closestDistance)
                {
                    closestDistance = h92;
                    closestIndex = 92;
                }
            }
            if(exists93 && dist82 + 1 < dist93)
            {
                dist93 = dist82 + 1;
                lookup[indexToLocalY(93)][indexToLocalX(93)] = Direction.NORTH;
                if(h93 < closestDistance)
                {
                    closestDistance = h93;
                    closestIndex = 93;
                }
            }
            if(exists94 && dist82 + 1 < dist94)
            {
                dist94 = dist82 + 1;
                lookup[indexToLocalY(94)][indexToLocalX(94)] = Direction.NORTHWEST;
                if(h94 < closestDistance)
                {
                    closestDistance = h94;
                    closestIndex = 94;
                }
            }
        }
        if(exists83)
        {
            if(exists71 && dist83 + 1 < dist71)
            {
                dist71 = dist83 + 1;
                lookup[indexToLocalY(71)][indexToLocalX(71)] = Direction.SOUTHEAST;
                if(h71 < closestDistance)
                {
                    closestDistance = h71;
                    closestIndex = 71;
                }
            }
            if(exists72 && dist83 + 1 < dist72)
            {
                dist72 = dist83 + 1;
                lookup[indexToLocalY(72)][indexToLocalX(72)] = Direction.SOUTH;
                if(h72 < closestDistance)
                {
                    closestDistance = h72;
                    closestIndex = 72;
                }
            }
            if(exists73 && dist83 + 1 < dist73)
            {
                dist73 = dist83 + 1;
                lookup[indexToLocalY(73)][indexToLocalX(73)] = Direction.SOUTHWEST;
                if(h73 < closestDistance)
                {
                    closestDistance = h73;
                    closestIndex = 73;
                }
            }
            if(exists82 && dist83 + 1 < dist82)
            {
                dist82 = dist83 + 1;
                lookup[indexToLocalY(82)][indexToLocalX(82)] = Direction.EAST;
                if(h82 < closestDistance)
                {
                    closestDistance = h82;
                    closestIndex = 82;
                }
            }
            if(exists84 && dist83 + 1 < dist84)
            {
                dist84 = dist83 + 1;
                lookup[indexToLocalY(84)][indexToLocalX(84)] = Direction.WEST;
                if(h84 < closestDistance)
                {
                    closestDistance = h84;
                    closestIndex = 84;
                }
            }
            if(exists93 && dist83 + 1 < dist93)
            {
                dist93 = dist83 + 1;
                lookup[indexToLocalY(93)][indexToLocalX(93)] = Direction.NORTHEAST;
                if(h93 < closestDistance)
                {
                    closestDistance = h93;
                    closestIndex = 93;
                }
            }
            if(exists94 && dist83 + 1 < dist94)
            {
                dist94 = dist83 + 1;
                lookup[indexToLocalY(94)][indexToLocalX(94)] = Direction.NORTH;
                if(h94 < closestDistance)
                {
                    closestDistance = h94;
                    closestIndex = 94;
                }
            }
            if(exists95 && dist83 + 1 < dist95)
            {
                dist95 = dist83 + 1;
                lookup[indexToLocalY(95)][indexToLocalX(95)] = Direction.NORTHWEST;
                if(h95 < closestDistance)
                {
                    closestDistance = h95;
                    closestIndex = 95;
                }
            }
        }
        if(exists84)
        {
            if(exists72 && dist84 + 1 < dist72)
            {
                dist72 = dist84 + 1;
                lookup[indexToLocalY(72)][indexToLocalX(72)] = Direction.SOUTHEAST;
                if(h72 < closestDistance)
                {
                    closestDistance = h72;
                    closestIndex = 72;
                }
            }
            if(exists73 && dist84 + 1 < dist73)
            {
                dist73 = dist84 + 1;
                lookup[indexToLocalY(73)][indexToLocalX(73)] = Direction.SOUTH;
                if(h73 < closestDistance)
                {
                    closestDistance = h73;
                    closestIndex = 73;
                }
            }
            if(exists74 && dist84 + 1 < dist74)
            {
                dist74 = dist84 + 1;
                lookup[indexToLocalY(74)][indexToLocalX(74)] = Direction.SOUTHWEST;
                if(h74 < closestDistance)
                {
                    closestDistance = h74;
                    closestIndex = 74;
                }
            }
            if(exists83 && dist84 + 1 < dist83)
            {
                dist83 = dist84 + 1;
                lookup[indexToLocalY(83)][indexToLocalX(83)] = Direction.EAST;
                if(h83 < closestDistance)
                {
                    closestDistance = h83;
                    closestIndex = 83;
                }
            }
            if(exists85 && dist84 + 1 < dist85)
            {
                dist85 = dist84 + 1;
                lookup[indexToLocalY(85)][indexToLocalX(85)] = Direction.WEST;
                if(h85 < closestDistance)
                {
                    closestDistance = h85;
                    closestIndex = 85;
                }
            }
            if(exists94 && dist84 + 1 < dist94)
            {
                dist94 = dist84 + 1;
                lookup[indexToLocalY(94)][indexToLocalX(94)] = Direction.NORTHEAST;
                if(h94 < closestDistance)
                {
                    closestDistance = h94;
                    closestIndex = 94;
                }
            }
            if(exists95 && dist84 + 1 < dist95)
            {
                dist95 = dist84 + 1;
                lookup[indexToLocalY(95)][indexToLocalX(95)] = Direction.NORTH;
                if(h95 < closestDistance)
                {
                    closestDistance = h95;
                    closestIndex = 95;
                }
            }
            if(exists96 && dist84 + 1 < dist96)
            {
                dist96 = dist84 + 1;
                lookup[indexToLocalY(96)][indexToLocalX(96)] = Direction.NORTHWEST;
                if(h96 < closestDistance)
                {
                    closestDistance = h96;
                    closestIndex = 96;
                }
            }
        }
        if(exists24)
        {
            if(exists12 && dist24 + 1 < dist12)
            {
                dist12 = dist24 + 1;
                lookup[indexToLocalY(12)][indexToLocalX(12)] = Direction.SOUTHEAST;
                if(h12 < closestDistance)
                {
                    closestDistance = h12;
                    closestIndex = 12;
                }
            }
            if(exists13 && dist24 + 1 < dist13)
            {
                dist13 = dist24 + 1;
                lookup[indexToLocalY(13)][indexToLocalX(13)] = Direction.SOUTH;
                if(h13 < closestDistance)
                {
                    closestDistance = h13;
                    closestIndex = 13;
                }
            }
            if(exists14 && dist24 + 1 < dist14)
            {
                dist14 = dist24 + 1;
                lookup[indexToLocalY(14)][indexToLocalX(14)] = Direction.SOUTHWEST;
                if(h14 < closestDistance)
                {
                    closestDistance = h14;
                    closestIndex = 14;
                }
            }
            if(exists23 && dist24 + 1 < dist23)
            {
                dist23 = dist24 + 1;
                lookup[indexToLocalY(23)][indexToLocalX(23)] = Direction.EAST;
                if(h23 < closestDistance)
                {
                    closestDistance = h23;
                    closestIndex = 23;
                }
            }
            if(exists25 && dist24 + 1 < dist25)
            {
                dist25 = dist24 + 1;
                lookup[indexToLocalY(25)][indexToLocalX(25)] = Direction.WEST;
                if(h25 < closestDistance)
                {
                    closestDistance = h25;
                    closestIndex = 25;
                }
            }
            if(exists34 && dist24 + 1 < dist34)
            {
                dist34 = dist24 + 1;
                lookup[indexToLocalY(34)][indexToLocalX(34)] = Direction.NORTHEAST;
                if(h34 < closestDistance)
                {
                    closestDistance = h34;
                    closestIndex = 34;
                }
            }
            if(exists35 && dist24 + 1 < dist35)
            {
                dist35 = dist24 + 1;
                lookup[indexToLocalY(35)][indexToLocalX(35)] = Direction.NORTH;
                if(h35 < closestDistance)
                {
                    closestDistance = h35;
                    closestIndex = 35;
                }
            }
            if(exists36 && dist24 + 1 < dist36)
            {
                dist36 = dist24 + 1;
                lookup[indexToLocalY(36)][indexToLocalX(36)] = Direction.NORTHWEST;
                if(h36 < closestDistance)
                {
                    closestDistance = h36;
                    closestIndex = 36;
                }
            }
        }
        if(exists25)
        {
            if(exists13 && dist25 + 1 < dist13)
            {
                dist13 = dist25 + 1;
                lookup[indexToLocalY(13)][indexToLocalX(13)] = Direction.SOUTHEAST;
                if(h13 < closestDistance)
                {
                    closestDistance = h13;
                    closestIndex = 13;
                }
            }
            if(exists14 && dist25 + 1 < dist14)
            {
                dist14 = dist25 + 1;
                lookup[indexToLocalY(14)][indexToLocalX(14)] = Direction.SOUTH;
                if(h14 < closestDistance)
                {
                    closestDistance = h14;
                    closestIndex = 14;
                }
            }
            if(exists15 && dist25 + 1 < dist15)
            {
                dist15 = dist25 + 1;
                lookup[indexToLocalY(15)][indexToLocalX(15)] = Direction.SOUTHWEST;
                if(h15 < closestDistance)
                {
                    closestDistance = h15;
                    closestIndex = 15;
                }
            }
            if(exists24 && dist25 + 1 < dist24)
            {
                dist24 = dist25 + 1;
                lookup[indexToLocalY(24)][indexToLocalX(24)] = Direction.EAST;
                if(h24 < closestDistance)
                {
                    closestDistance = h24;
                    closestIndex = 24;
                }
            }
            if(exists26 && dist25 + 1 < dist26)
            {
                dist26 = dist25 + 1;
                lookup[indexToLocalY(26)][indexToLocalX(26)] = Direction.WEST;
                if(h26 < closestDistance)
                {
                    closestDistance = h26;
                    closestIndex = 26;
                }
            }
            if(exists35 && dist25 + 1 < dist35)
            {
                dist35 = dist25 + 1;
                lookup[indexToLocalY(35)][indexToLocalX(35)] = Direction.NORTHEAST;
                if(h35 < closestDistance)
                {
                    closestDistance = h35;
                    closestIndex = 35;
                }
            }
            if(exists36 && dist25 + 1 < dist36)
            {
                dist36 = dist25 + 1;
                lookup[indexToLocalY(36)][indexToLocalX(36)] = Direction.NORTH;
                if(h36 < closestDistance)
                {
                    closestDistance = h36;
                    closestIndex = 36;
                }
            }
            if(exists37 && dist25 + 1 < dist37)
            {
                dist37 = dist25 + 1;
                lookup[indexToLocalY(37)][indexToLocalX(37)] = Direction.NORTHWEST;
                if(h37 < closestDistance)
                {
                    closestDistance = h37;
                    closestIndex = 37;
                }
            }
        }
        if(exists26)
        {
            if(exists14 && dist26 + 1 < dist14)
            {
                dist14 = dist26 + 1;
                lookup[indexToLocalY(14)][indexToLocalX(14)] = Direction.SOUTHEAST;
                if(h14 < closestDistance)
                {
                    closestDistance = h14;
                    closestIndex = 14;
                }
            }
            if(exists15 && dist26 + 1 < dist15)
            {
                dist15 = dist26 + 1;
                lookup[indexToLocalY(15)][indexToLocalX(15)] = Direction.SOUTH;
                if(h15 < closestDistance)
                {
                    closestDistance = h15;
                    closestIndex = 15;
                }
            }
            if(exists16 && dist26 + 1 < dist16)
            {
                dist16 = dist26 + 1;
                lookup[indexToLocalY(16)][indexToLocalX(16)] = Direction.SOUTHWEST;
                if(h16 < closestDistance)
                {
                    closestDistance = h16;
                    closestIndex = 16;
                }
            }
            if(exists25 && dist26 + 1 < dist25)
            {
                dist25 = dist26 + 1;
                lookup[indexToLocalY(25)][indexToLocalX(25)] = Direction.EAST;
                if(h25 < closestDistance)
                {
                    closestDistance = h25;
                    closestIndex = 25;
                }
            }
            if(exists27 && dist26 + 1 < dist27)
            {
                dist27 = dist26 + 1;
                lookup[indexToLocalY(27)][indexToLocalX(27)] = Direction.WEST;
                if(h27 < closestDistance)
                {
                    closestDistance = h27;
                    closestIndex = 27;
                }
            }
            if(exists36 && dist26 + 1 < dist36)
            {
                dist36 = dist26 + 1;
                lookup[indexToLocalY(36)][indexToLocalX(36)] = Direction.NORTHEAST;
                if(h36 < closestDistance)
                {
                    closestDistance = h36;
                    closestIndex = 36;
                }
            }
            if(exists37 && dist26 + 1 < dist37)
            {
                dist37 = dist26 + 1;
                lookup[indexToLocalY(37)][indexToLocalX(37)] = Direction.NORTH;
                if(h37 < closestDistance)
                {
                    closestDistance = h37;
                    closestIndex = 37;
                }
            }
            if(exists38 && dist26 + 1 < dist38)
            {
                dist38 = dist26 + 1;
                lookup[indexToLocalY(38)][indexToLocalX(38)] = Direction.NORTHWEST;
                if(h38 < closestDistance)
                {
                    closestDistance = h38;
                    closestIndex = 38;
                }
            }
        }
        if(exists35)
        {
            if(exists23 && dist35 + 1 < dist23)
            {
                dist23 = dist35 + 1;
                lookup[indexToLocalY(23)][indexToLocalX(23)] = Direction.SOUTHEAST;
                if(h23 < closestDistance)
                {
                    closestDistance = h23;
                    closestIndex = 23;
                }
            }
            if(exists24 && dist35 + 1 < dist24)
            {
                dist24 = dist35 + 1;
                lookup[indexToLocalY(24)][indexToLocalX(24)] = Direction.SOUTH;
                if(h24 < closestDistance)
                {
                    closestDistance = h24;
                    closestIndex = 24;
                }
            }
            if(exists25 && dist35 + 1 < dist25)
            {
                dist25 = dist35 + 1;
                lookup[indexToLocalY(25)][indexToLocalX(25)] = Direction.SOUTHWEST;
                if(h25 < closestDistance)
                {
                    closestDistance = h25;
                    closestIndex = 25;
                }
            }
            if(exists34 && dist35 + 1 < dist34)
            {
                dist34 = dist35 + 1;
                lookup[indexToLocalY(34)][indexToLocalX(34)] = Direction.EAST;
                if(h34 < closestDistance)
                {
                    closestDistance = h34;
                    closestIndex = 34;
                }
            }
            if(exists36 && dist35 + 1 < dist36)
            {
                dist36 = dist35 + 1;
                lookup[indexToLocalY(36)][indexToLocalX(36)] = Direction.WEST;
                if(h36 < closestDistance)
                {
                    closestDistance = h36;
                    closestIndex = 36;
                }
            }
            if(exists45 && dist35 + 1 < dist45)
            {
                dist45 = dist35 + 1;
                lookup[indexToLocalY(45)][indexToLocalX(45)] = Direction.NORTHEAST;
                if(h45 < closestDistance)
                {
                    closestDistance = h45;
                    closestIndex = 45;
                }
            }
            if(exists46 && dist35 + 1 < dist46)
            {
                dist46 = dist35 + 1;
                lookup[indexToLocalY(46)][indexToLocalX(46)] = Direction.NORTH;
                if(h46 < closestDistance)
                {
                    closestDistance = h46;
                    closestIndex = 46;
                }
            }
            if(exists47 && dist35 + 1 < dist47)
            {
                dist47 = dist35 + 1;
                lookup[indexToLocalY(47)][indexToLocalX(47)] = Direction.NORTHWEST;
                if(h47 < closestDistance)
                {
                    closestDistance = h47;
                    closestIndex = 47;
                }
            }
        }
        if(exists46)
        {
            if(exists34 && dist46 + 1 < dist34)
            {
                dist34 = dist46 + 1;
                lookup[indexToLocalY(34)][indexToLocalX(34)] = Direction.SOUTHEAST;
                if(h34 < closestDistance)
                {
                    closestDistance = h34;
                    closestIndex = 34;
                }
            }
            if(exists35 && dist46 + 1 < dist35)
            {
                dist35 = dist46 + 1;
                lookup[indexToLocalY(35)][indexToLocalX(35)] = Direction.SOUTH;
                if(h35 < closestDistance)
                {
                    closestDistance = h35;
                    closestIndex = 35;
                }
            }
            if(exists36 && dist46 + 1 < dist36)
            {
                dist36 = dist46 + 1;
                lookup[indexToLocalY(36)][indexToLocalX(36)] = Direction.SOUTHWEST;
                if(h36 < closestDistance)
                {
                    closestDistance = h36;
                    closestIndex = 36;
                }
            }
            if(exists45 && dist46 + 1 < dist45)
            {
                dist45 = dist46 + 1;
                lookup[indexToLocalY(45)][indexToLocalX(45)] = Direction.EAST;
                if(h45 < closestDistance)
                {
                    closestDistance = h45;
                    closestIndex = 45;
                }
            }
            if(exists47 && dist46 + 1 < dist47)
            {
                dist47 = dist46 + 1;
                lookup[indexToLocalY(47)][indexToLocalX(47)] = Direction.WEST;
                if(h47 < closestDistance)
                {
                    closestDistance = h47;
                    closestIndex = 47;
                }
            }
            if(exists56 && dist46 + 1 < dist56)
            {
                dist56 = dist46 + 1;
                lookup[indexToLocalY(56)][indexToLocalX(56)] = Direction.NORTHEAST;
                if(h56 < closestDistance)
                {
                    closestDistance = h56;
                    closestIndex = 56;
                }
            }
            if(exists57 && dist46 + 1 < dist57)
            {
                dist57 = dist46 + 1;
                lookup[indexToLocalY(57)][indexToLocalX(57)] = Direction.NORTH;
                if(h57 < closestDistance)
                {
                    closestDistance = h57;
                    closestIndex = 57;
                }
            }
            if(exists58 && dist46 + 1 < dist58)
            {
                dist58 = dist46 + 1;
                lookup[indexToLocalY(58)][indexToLocalX(58)] = Direction.NORTHWEST;
                if(h58 < closestDistance)
                {
                    closestDistance = h58;
                    closestIndex = 58;
                }
            }
        }
        if(exists27)
        {
            if(exists15 && dist27 + 1 < dist15)
            {
                dist15 = dist27 + 1;
                lookup[indexToLocalY(15)][indexToLocalX(15)] = Direction.SOUTHEAST;
                if(h15 < closestDistance)
                {
                    closestDistance = h15;
                    closestIndex = 15;
                }
            }
            if(exists16 && dist27 + 1 < dist16)
            {
                dist16 = dist27 + 1;
                lookup[indexToLocalY(16)][indexToLocalX(16)] = Direction.SOUTH;
                if(h16 < closestDistance)
                {
                    closestDistance = h16;
                    closestIndex = 16;
                }
            }
            if(exists17 && dist27 + 1 < dist17)
            {
                dist17 = dist27 + 1;
                lookup[indexToLocalY(17)][indexToLocalX(17)] = Direction.SOUTHWEST;
                if(h17 < closestDistance)
                {
                    closestDistance = h17;
                    closestIndex = 17;
                }
            }
            if(exists26 && dist27 + 1 < dist26)
            {
                dist26 = dist27 + 1;
                lookup[indexToLocalY(26)][indexToLocalX(26)] = Direction.EAST;
                if(h26 < closestDistance)
                {
                    closestDistance = h26;
                    closestIndex = 26;
                }
            }
            if(exists28 && dist27 + 1 < dist28)
            {
                dist28 = dist27 + 1;
                lookup[indexToLocalY(28)][indexToLocalX(28)] = Direction.WEST;
                if(h28 < closestDistance)
                {
                    closestDistance = h28;
                    closestIndex = 28;
                }
            }
            if(exists37 && dist27 + 1 < dist37)
            {
                dist37 = dist27 + 1;
                lookup[indexToLocalY(37)][indexToLocalX(37)] = Direction.NORTHEAST;
                if(h37 < closestDistance)
                {
                    closestDistance = h37;
                    closestIndex = 37;
                }
            }
            if(exists38 && dist27 + 1 < dist38)
            {
                dist38 = dist27 + 1;
                lookup[indexToLocalY(38)][indexToLocalX(38)] = Direction.NORTH;
                if(h38 < closestDistance)
                {
                    closestDistance = h38;
                    closestIndex = 38;
                }
            }
            if(exists39 && dist27 + 1 < dist39)
            {
                dist39 = dist27 + 1;
                lookup[indexToLocalY(39)][indexToLocalX(39)] = Direction.NORTHWEST;
                if(h39 < closestDistance)
                {
                    closestDistance = h39;
                    closestIndex = 39;
                }
            }
        }
        if(exists28)
        {
            if(exists16 && dist28 + 1 < dist16)
            {
                dist16 = dist28 + 1;
                lookup[indexToLocalY(16)][indexToLocalX(16)] = Direction.SOUTHEAST;
                if(h16 < closestDistance)
                {
                    closestDistance = h16;
                    closestIndex = 16;
                }
            }
            if(exists17 && dist28 + 1 < dist17)
            {
                dist17 = dist28 + 1;
                lookup[indexToLocalY(17)][indexToLocalX(17)] = Direction.SOUTH;
                if(h17 < closestDistance)
                {
                    closestDistance = h17;
                    closestIndex = 17;
                }
            }
            if(exists18 && dist28 + 1 < dist18)
            {
                dist18 = dist28 + 1;
                lookup[indexToLocalY(18)][indexToLocalX(18)] = Direction.SOUTHWEST;
                if(h18 < closestDistance)
                {
                    closestDistance = h18;
                    closestIndex = 18;
                }
            }
            if(exists27 && dist28 + 1 < dist27)
            {
                dist27 = dist28 + 1;
                lookup[indexToLocalY(27)][indexToLocalX(27)] = Direction.EAST;
                if(h27 < closestDistance)
                {
                    closestDistance = h27;
                    closestIndex = 27;
                }
            }
            if(exists29 && dist28 + 1 < dist29)
            {
                dist29 = dist28 + 1;
                lookup[indexToLocalY(29)][indexToLocalX(29)] = Direction.WEST;
                if(h29 < closestDistance)
                {
                    closestDistance = h29;
                    closestIndex = 29;
                }
            }
            if(exists38 && dist28 + 1 < dist38)
            {
                dist38 = dist28 + 1;
                lookup[indexToLocalY(38)][indexToLocalX(38)] = Direction.NORTHEAST;
                if(h38 < closestDistance)
                {
                    closestDistance = h38;
                    closestIndex = 38;
                }
            }
            if(exists39 && dist28 + 1 < dist39)
            {
                dist39 = dist28 + 1;
                lookup[indexToLocalY(39)][indexToLocalX(39)] = Direction.NORTH;
                if(h39 < closestDistance)
                {
                    closestDistance = h39;
                    closestIndex = 39;
                }
            }
            if(exists40 && dist28 + 1 < dist40)
            {
                dist40 = dist28 + 1;
                lookup[indexToLocalY(40)][indexToLocalX(40)] = Direction.NORTHWEST;
                if(h40 < closestDistance)
                {
                    closestDistance = h40;
                    closestIndex = 40;
                }
            }
        }
        if(exists57)
        {
            if(exists45 && dist57 + 1 < dist45)
            {
                dist45 = dist57 + 1;
                lookup[indexToLocalY(45)][indexToLocalX(45)] = Direction.SOUTHEAST;
                if(h45 < closestDistance)
                {
                    closestDistance = h45;
                    closestIndex = 45;
                }
            }
            if(exists46 && dist57 + 1 < dist46)
            {
                dist46 = dist57 + 1;
                lookup[indexToLocalY(46)][indexToLocalX(46)] = Direction.SOUTH;
                if(h46 < closestDistance)
                {
                    closestDistance = h46;
                    closestIndex = 46;
                }
            }
            if(exists47 && dist57 + 1 < dist47)
            {
                dist47 = dist57 + 1;
                lookup[indexToLocalY(47)][indexToLocalX(47)] = Direction.SOUTHWEST;
                if(h47 < closestDistance)
                {
                    closestDistance = h47;
                    closestIndex = 47;
                }
            }
            if(exists56 && dist57 + 1 < dist56)
            {
                dist56 = dist57 + 1;
                lookup[indexToLocalY(56)][indexToLocalX(56)] = Direction.EAST;
                if(h56 < closestDistance)
                {
                    closestDistance = h56;
                    closestIndex = 56;
                }
            }
            if(exists58 && dist57 + 1 < dist58)
            {
                dist58 = dist57 + 1;
                lookup[indexToLocalY(58)][indexToLocalX(58)] = Direction.WEST;
                if(h58 < closestDistance)
                {
                    closestDistance = h58;
                    closestIndex = 58;
                }
            }
            if(exists67 && dist57 + 1 < dist67)
            {
                dist67 = dist57 + 1;
                lookup[indexToLocalY(67)][indexToLocalX(67)] = Direction.NORTHEAST;
                if(h67 < closestDistance)
                {
                    closestDistance = h67;
                    closestIndex = 67;
                }
            }
            if(exists68 && dist57 + 1 < dist68)
            {
                dist68 = dist57 + 1;
                lookup[indexToLocalY(68)][indexToLocalX(68)] = Direction.NORTH;
                if(h68 < closestDistance)
                {
                    closestDistance = h68;
                    closestIndex = 68;
                }
            }
            if(exists69 && dist57 + 1 < dist69)
            {
                dist69 = dist57 + 1;
                lookup[indexToLocalY(69)][indexToLocalX(69)] = Direction.NORTHWEST;
                if(h69 < closestDistance)
                {
                    closestDistance = h69;
                    closestIndex = 69;
                }
            }
        }
        if(exists68)
        {
            if(exists56 && dist68 + 1 < dist56)
            {
                dist56 = dist68 + 1;
                lookup[indexToLocalY(56)][indexToLocalX(56)] = Direction.SOUTHEAST;
                if(h56 < closestDistance)
                {
                    closestDistance = h56;
                    closestIndex = 56;
                }
            }
            if(exists57 && dist68 + 1 < dist57)
            {
                dist57 = dist68 + 1;
                lookup[indexToLocalY(57)][indexToLocalX(57)] = Direction.SOUTH;
                if(h57 < closestDistance)
                {
                    closestDistance = h57;
                    closestIndex = 57;
                }
            }
            if(exists58 && dist68 + 1 < dist58)
            {
                dist58 = dist68 + 1;
                lookup[indexToLocalY(58)][indexToLocalX(58)] = Direction.SOUTHWEST;
                if(h58 < closestDistance)
                {
                    closestDistance = h58;
                    closestIndex = 58;
                }
            }
            if(exists67 && dist68 + 1 < dist67)
            {
                dist67 = dist68 + 1;
                lookup[indexToLocalY(67)][indexToLocalX(67)] = Direction.EAST;
                if(h67 < closestDistance)
                {
                    closestDistance = h67;
                    closestIndex = 67;
                }
            }
            if(exists69 && dist68 + 1 < dist69)
            {
                dist69 = dist68 + 1;
                lookup[indexToLocalY(69)][indexToLocalX(69)] = Direction.WEST;
                if(h69 < closestDistance)
                {
                    closestDistance = h69;
                    closestIndex = 69;
                }
            }
            if(exists78 && dist68 + 1 < dist78)
            {
                dist78 = dist68 + 1;
                lookup[indexToLocalY(78)][indexToLocalX(78)] = Direction.NORTHEAST;
                if(h78 < closestDistance)
                {
                    closestDistance = h78;
                    closestIndex = 78;
                }
            }
            if(exists79 && dist68 + 1 < dist79)
            {
                dist79 = dist68 + 1;
                lookup[indexToLocalY(79)][indexToLocalX(79)] = Direction.NORTH;
                if(h79 < closestDistance)
                {
                    closestDistance = h79;
                    closestIndex = 79;
                }
            }
            if(exists80 && dist68 + 1 < dist80)
            {
                dist80 = dist68 + 1;
                lookup[indexToLocalY(80)][indexToLocalX(80)] = Direction.NORTHWEST;
                if(h80 < closestDistance)
                {
                    closestDistance = h80;
                    closestIndex = 80;
                }
            }
        }
        if(exists29)
        {
            if(exists17 && dist29 + 1 < dist17)
            {
                dist17 = dist29 + 1;
                lookup[indexToLocalY(17)][indexToLocalX(17)] = Direction.SOUTHEAST;
                if(h17 < closestDistance)
                {
                    closestDistance = h17;
                    closestIndex = 17;
                }
            }
            if(exists18 && dist29 + 1 < dist18)
            {
                dist18 = dist29 + 1;
                lookup[indexToLocalY(18)][indexToLocalX(18)] = Direction.SOUTH;
                if(h18 < closestDistance)
                {
                    closestDistance = h18;
                    closestIndex = 18;
                }
            }
            if(exists19 && dist29 + 1 < dist19)
            {
                dist19 = dist29 + 1;
                lookup[indexToLocalY(19)][indexToLocalX(19)] = Direction.SOUTHWEST;
                if(h19 < closestDistance)
                {
                    closestDistance = h19;
                    closestIndex = 19;
                }
            }
            if(exists28 && dist29 + 1 < dist28)
            {
                dist28 = dist29 + 1;
                lookup[indexToLocalY(28)][indexToLocalX(28)] = Direction.EAST;
                if(h28 < closestDistance)
                {
                    closestDistance = h28;
                    closestIndex = 28;
                }
            }
            if(exists30 && dist29 + 1 < dist30)
            {
                dist30 = dist29 + 1;
                lookup[indexToLocalY(30)][indexToLocalX(30)] = Direction.WEST;
                if(h30 < closestDistance)
                {
                    closestDistance = h30;
                    closestIndex = 30;
                }
            }
            if(exists39 && dist29 + 1 < dist39)
            {
                dist39 = dist29 + 1;
                lookup[indexToLocalY(39)][indexToLocalX(39)] = Direction.NORTHEAST;
                if(h39 < closestDistance)
                {
                    closestDistance = h39;
                    closestIndex = 39;
                }
            }
            if(exists40 && dist29 + 1 < dist40)
            {
                dist40 = dist29 + 1;
                lookup[indexToLocalY(40)][indexToLocalX(40)] = Direction.NORTH;
                if(h40 < closestDistance)
                {
                    closestDistance = h40;
                    closestIndex = 40;
                }
            }
            if(exists41 && dist29 + 1 < dist41)
            {
                dist41 = dist29 + 1;
                lookup[indexToLocalY(41)][indexToLocalX(41)] = Direction.NORTHWEST;
                if(h41 < closestDistance)
                {
                    closestDistance = h41;
                    closestIndex = 41;
                }
            }
        }
        if(exists30)
        {
            if(exists18 && dist30 + 1 < dist18)
            {
                dist18 = dist30 + 1;
                lookup[indexToLocalY(18)][indexToLocalX(18)] = Direction.SOUTHEAST;
                if(h18 < closestDistance)
                {
                    closestDistance = h18;
                    closestIndex = 18;
                }
            }
            if(exists19 && dist30 + 1 < dist19)
            {
                dist19 = dist30 + 1;
                lookup[indexToLocalY(19)][indexToLocalX(19)] = Direction.SOUTH;
                if(h19 < closestDistance)
                {
                    closestDistance = h19;
                    closestIndex = 19;
                }
            }
            if(exists20 && dist30 + 1 < dist20)
            {
                dist20 = dist30 + 1;
                lookup[indexToLocalY(20)][indexToLocalX(20)] = Direction.SOUTHWEST;
                if(h20 < closestDistance)
                {
                    closestDistance = h20;
                    closestIndex = 20;
                }
            }
            if(exists29 && dist30 + 1 < dist29)
            {
                dist29 = dist30 + 1;
                lookup[indexToLocalY(29)][indexToLocalX(29)] = Direction.EAST;
                if(h29 < closestDistance)
                {
                    closestDistance = h29;
                    closestIndex = 29;
                }
            }
            if(exists31 && dist30 + 1 < dist31)
            {
                dist31 = dist30 + 1;
                lookup[indexToLocalY(31)][indexToLocalX(31)] = Direction.WEST;
                if(h31 < closestDistance)
                {
                    closestDistance = h31;
                    closestIndex = 31;
                }
            }
            if(exists40 && dist30 + 1 < dist40)
            {
                dist40 = dist30 + 1;
                lookup[indexToLocalY(40)][indexToLocalX(40)] = Direction.NORTHEAST;
                if(h40 < closestDistance)
                {
                    closestDistance = h40;
                    closestIndex = 40;
                }
            }
            if(exists41 && dist30 + 1 < dist41)
            {
                dist41 = dist30 + 1;
                lookup[indexToLocalY(41)][indexToLocalX(41)] = Direction.NORTH;
                if(h41 < closestDistance)
                {
                    closestDistance = h41;
                    closestIndex = 41;
                }
            }
            if(exists42 && dist30 + 1 < dist42)
            {
                dist42 = dist30 + 1;
                lookup[indexToLocalY(42)][indexToLocalX(42)] = Direction.NORTHWEST;
                if(h42 < closestDistance)
                {
                    closestDistance = h42;
                    closestIndex = 42;
                }
            }
        }
    }

    private static void method1()
    {
        if(exists60)
        {
            if(exists48 && dist60 + 1 < dist48)
            {
                dist48 = dist60 + 1;
                lookup[indexToLocalY(48)][indexToLocalX(48)] = Direction.SOUTHEAST;
                if(h48 < closestDistance)
                {
                    closestDistance = h48;
                    closestIndex = 48;
                }
            }
            if(exists49 && dist60 + 1 < dist49)
            {
                dist49 = dist60 + 1;
                lookup[indexToLocalY(49)][indexToLocalX(49)] = Direction.SOUTH;
                if(h49 < closestDistance)
                {
                    closestDistance = h49;
                    closestIndex = 49;
                }
            }
            if(exists50 && dist60 + 1 < dist50)
            {
                dist50 = dist60 + 1;
                lookup[indexToLocalY(50)][indexToLocalX(50)] = Direction.SOUTHWEST;
                if(h50 < closestDistance)
                {
                    closestDistance = h50;
                    closestIndex = 50;
                }
            }
            if(exists59 && dist60 + 1 < dist59)
            {
                dist59 = dist60 + 1;
                lookup[indexToLocalY(59)][indexToLocalX(59)] = Direction.EAST;
                if(h59 < closestDistance)
                {
                    closestDistance = h59;
                    closestIndex = 59;
                }
            }
            if(exists61 && dist60 + 1 < dist61)
            {
                dist61 = dist60 + 1;
                lookup[indexToLocalY(61)][indexToLocalX(61)] = Direction.WEST;
                if(h61 < closestDistance)
                {
                    closestDistance = h61;
                    closestIndex = 61;
                }
            }
            if(exists70 && dist60 + 1 < dist70)
            {
                dist70 = dist60 + 1;
                lookup[indexToLocalY(70)][indexToLocalX(70)] = Direction.NORTHEAST;
                if(h70 < closestDistance)
                {
                    closestDistance = h70;
                    closestIndex = 70;
                }
            }
            if(exists71 && dist60 + 1 < dist71)
            {
                dist71 = dist60 + 1;
                lookup[indexToLocalY(71)][indexToLocalX(71)] = Direction.NORTH;
                if(h71 < closestDistance)
                {
                    closestDistance = h71;
                    closestIndex = 71;
                }
            }
            if(exists72 && dist60 + 1 < dist72)
            {
                dist72 = dist60 + 1;
                lookup[indexToLocalY(72)][indexToLocalX(72)] = Direction.NORTHWEST;
                if(h72 < closestDistance)
                {
                    closestDistance = h72;
                    closestIndex = 72;
                }
            }
        }
        if(exists48)
        {
            if(exists36 && dist48 + 1 < dist36)
            {
                dist36 = dist48 + 1;
                lookup[indexToLocalY(36)][indexToLocalX(36)] = Direction.SOUTHEAST;
                if(h36 < closestDistance)
                {
                    closestDistance = h36;
                    closestIndex = 36;
                }
            }
            if(exists37 && dist48 + 1 < dist37)
            {
                dist37 = dist48 + 1;
                lookup[indexToLocalY(37)][indexToLocalX(37)] = Direction.SOUTH;
                if(h37 < closestDistance)
                {
                    closestDistance = h37;
                    closestIndex = 37;
                }
            }
            if(exists38 && dist48 + 1 < dist38)
            {
                dist38 = dist48 + 1;
                lookup[indexToLocalY(38)][indexToLocalX(38)] = Direction.SOUTHWEST;
                if(h38 < closestDistance)
                {
                    closestDistance = h38;
                    closestIndex = 38;
                }
            }
            if(exists47 && dist48 + 1 < dist47)
            {
                dist47 = dist48 + 1;
                lookup[indexToLocalY(47)][indexToLocalX(47)] = Direction.EAST;
                if(h47 < closestDistance)
                {
                    closestDistance = h47;
                    closestIndex = 47;
                }
            }
            if(exists49 && dist48 + 1 < dist49)
            {
                dist49 = dist48 + 1;
                lookup[indexToLocalY(49)][indexToLocalX(49)] = Direction.WEST;
                if(h49 < closestDistance)
                {
                    closestDistance = h49;
                    closestIndex = 49;
                }
            }
            if(exists58 && dist48 + 1 < dist58)
            {
                dist58 = dist48 + 1;
                lookup[indexToLocalY(58)][indexToLocalX(58)] = Direction.NORTHEAST;
                if(h58 < closestDistance)
                {
                    closestDistance = h58;
                    closestIndex = 58;
                }
            }
            if(exists59 && dist48 + 1 < dist59)
            {
                dist59 = dist48 + 1;
                lookup[indexToLocalY(59)][indexToLocalX(59)] = Direction.NORTH;
                if(h59 < closestDistance)
                {
                    closestDistance = h59;
                    closestIndex = 59;
                }
            }
            if(exists60 && dist48 + 1 < dist60)
            {
                dist60 = dist48 + 1;
                lookup[indexToLocalY(60)][indexToLocalX(60)] = Direction.NORTHWEST;
                if(h60 < closestDistance)
                {
                    closestDistance = h60;
                    closestIndex = 60;
                }
            }
        }
        if(exists49)
        {
            if(exists37 && dist49 + 1 < dist37)
            {
                dist37 = dist49 + 1;
                lookup[indexToLocalY(37)][indexToLocalX(37)] = Direction.SOUTHEAST;
                if(h37 < closestDistance)
                {
                    closestDistance = h37;
                    closestIndex = 37;
                }
            }
            if(exists38 && dist49 + 1 < dist38)
            {
                dist38 = dist49 + 1;
                lookup[indexToLocalY(38)][indexToLocalX(38)] = Direction.SOUTH;
                if(h38 < closestDistance)
                {
                    closestDistance = h38;
                    closestIndex = 38;
                }
            }
            if(exists39 && dist49 + 1 < dist39)
            {
                dist39 = dist49 + 1;
                lookup[indexToLocalY(39)][indexToLocalX(39)] = Direction.SOUTHWEST;
                if(h39 < closestDistance)
                {
                    closestDistance = h39;
                    closestIndex = 39;
                }
            }
            if(exists48 && dist49 + 1 < dist48)
            {
                dist48 = dist49 + 1;
                lookup[indexToLocalY(48)][indexToLocalX(48)] = Direction.EAST;
                if(h48 < closestDistance)
                {
                    closestDistance = h48;
                    closestIndex = 48;
                }
            }
            if(exists50 && dist49 + 1 < dist50)
            {
                dist50 = dist49 + 1;
                lookup[indexToLocalY(50)][indexToLocalX(50)] = Direction.WEST;
                if(h50 < closestDistance)
                {
                    closestDistance = h50;
                    closestIndex = 50;
                }
            }
            if(exists59 && dist49 + 1 < dist59)
            {
                dist59 = dist49 + 1;
                lookup[indexToLocalY(59)][indexToLocalX(59)] = Direction.NORTHEAST;
                if(h59 < closestDistance)
                {
                    closestDistance = h59;
                    closestIndex = 59;
                }
            }
            if(exists60 && dist49 + 1 < dist60)
            {
                dist60 = dist49 + 1;
                lookup[indexToLocalY(60)][indexToLocalX(60)] = Direction.NORTH;
                if(h60 < closestDistance)
                {
                    closestDistance = h60;
                    closestIndex = 60;
                }
            }
            if(exists61 && dist49 + 1 < dist61)
            {
                dist61 = dist49 + 1;
                lookup[indexToLocalY(61)][indexToLocalX(61)] = Direction.NORTHWEST;
                if(h61 < closestDistance)
                {
                    closestDistance = h61;
                    closestIndex = 61;
                }
            }
        }
        if(exists50)
        {
            if(exists38 && dist50 + 1 < dist38)
            {
                dist38 = dist50 + 1;
                lookup[indexToLocalY(38)][indexToLocalX(38)] = Direction.SOUTHEAST;
                if(h38 < closestDistance)
                {
                    closestDistance = h38;
                    closestIndex = 38;
                }
            }
            if(exists39 && dist50 + 1 < dist39)
            {
                dist39 = dist50 + 1;
                lookup[indexToLocalY(39)][indexToLocalX(39)] = Direction.SOUTH;
                if(h39 < closestDistance)
                {
                    closestDistance = h39;
                    closestIndex = 39;
                }
            }
            if(exists40 && dist50 + 1 < dist40)
            {
                dist40 = dist50 + 1;
                lookup[indexToLocalY(40)][indexToLocalX(40)] = Direction.SOUTHWEST;
                if(h40 < closestDistance)
                {
                    closestDistance = h40;
                    closestIndex = 40;
                }
            }
            if(exists49 && dist50 + 1 < dist49)
            {
                dist49 = dist50 + 1;
                lookup[indexToLocalY(49)][indexToLocalX(49)] = Direction.EAST;
                if(h49 < closestDistance)
                {
                    closestDistance = h49;
                    closestIndex = 49;
                }
            }
            if(exists51 && dist50 + 1 < dist51)
            {
                dist51 = dist50 + 1;
                lookup[indexToLocalY(51)][indexToLocalX(51)] = Direction.WEST;
                if(h51 < closestDistance)
                {
                    closestDistance = h51;
                    closestIndex = 51;
                }
            }
            if(exists60 && dist50 + 1 < dist60)
            {
                dist60 = dist50 + 1;
                lookup[indexToLocalY(60)][indexToLocalX(60)] = Direction.NORTHEAST;
                if(h60 < closestDistance)
                {
                    closestDistance = h60;
                    closestIndex = 60;
                }
            }
            if(exists61 && dist50 + 1 < dist61)
            {
                dist61 = dist50 + 1;
                lookup[indexToLocalY(61)][indexToLocalX(61)] = Direction.NORTH;
                if(h61 < closestDistance)
                {
                    closestDistance = h61;
                    closestIndex = 61;
                }
            }
            if(exists62 && dist50 + 1 < dist62)
            {
                dist62 = dist50 + 1;
                lookup[indexToLocalY(62)][indexToLocalX(62)] = Direction.NORTHWEST;
                if(h62 < closestDistance)
                {
                    closestDistance = h62;
                    closestIndex = 62;
                }
            }
        }
        if(exists59)
        {
            if(exists47 && dist59 + 1 < dist47)
            {
                dist47 = dist59 + 1;
                lookup[indexToLocalY(47)][indexToLocalX(47)] = Direction.SOUTHEAST;
                if(h47 < closestDistance)
                {
                    closestDistance = h47;
                    closestIndex = 47;
                }
            }
            if(exists48 && dist59 + 1 < dist48)
            {
                dist48 = dist59 + 1;
                lookup[indexToLocalY(48)][indexToLocalX(48)] = Direction.SOUTH;
                if(h48 < closestDistance)
                {
                    closestDistance = h48;
                    closestIndex = 48;
                }
            }
            if(exists49 && dist59 + 1 < dist49)
            {
                dist49 = dist59 + 1;
                lookup[indexToLocalY(49)][indexToLocalX(49)] = Direction.SOUTHWEST;
                if(h49 < closestDistance)
                {
                    closestDistance = h49;
                    closestIndex = 49;
                }
            }
            if(exists58 && dist59 + 1 < dist58)
            {
                dist58 = dist59 + 1;
                lookup[indexToLocalY(58)][indexToLocalX(58)] = Direction.EAST;
                if(h58 < closestDistance)
                {
                    closestDistance = h58;
                    closestIndex = 58;
                }
            }
            if(exists60 && dist59 + 1 < dist60)
            {
                dist60 = dist59 + 1;
                lookup[indexToLocalY(60)][indexToLocalX(60)] = Direction.WEST;
                if(h60 < closestDistance)
                {
                    closestDistance = h60;
                    closestIndex = 60;
                }
            }
            if(exists69 && dist59 + 1 < dist69)
            {
                dist69 = dist59 + 1;
                lookup[indexToLocalY(69)][indexToLocalX(69)] = Direction.NORTHEAST;
                if(h69 < closestDistance)
                {
                    closestDistance = h69;
                    closestIndex = 69;
                }
            }
            if(exists70 && dist59 + 1 < dist70)
            {
                dist70 = dist59 + 1;
                lookup[indexToLocalY(70)][indexToLocalX(70)] = Direction.NORTH;
                if(h70 < closestDistance)
                {
                    closestDistance = h70;
                    closestIndex = 70;
                }
            }
            if(exists71 && dist59 + 1 < dist71)
            {
                dist71 = dist59 + 1;
                lookup[indexToLocalY(71)][indexToLocalX(71)] = Direction.NORTHWEST;
                if(h71 < closestDistance)
                {
                    closestDistance = h71;
                    closestIndex = 71;
                }
            }
        }
        if(exists61)
        {
            if(exists49 && dist61 + 1 < dist49)
            {
                dist49 = dist61 + 1;
                lookup[indexToLocalY(49)][indexToLocalX(49)] = Direction.SOUTHEAST;
                if(h49 < closestDistance)
                {
                    closestDistance = h49;
                    closestIndex = 49;
                }
            }
            if(exists50 && dist61 + 1 < dist50)
            {
                dist50 = dist61 + 1;
                lookup[indexToLocalY(50)][indexToLocalX(50)] = Direction.SOUTH;
                if(h50 < closestDistance)
                {
                    closestDistance = h50;
                    closestIndex = 50;
                }
            }
            if(exists51 && dist61 + 1 < dist51)
            {
                dist51 = dist61 + 1;
                lookup[indexToLocalY(51)][indexToLocalX(51)] = Direction.SOUTHWEST;
                if(h51 < closestDistance)
                {
                    closestDistance = h51;
                    closestIndex = 51;
                }
            }
            if(exists60 && dist61 + 1 < dist60)
            {
                dist60 = dist61 + 1;
                lookup[indexToLocalY(60)][indexToLocalX(60)] = Direction.EAST;
                if(h60 < closestDistance)
                {
                    closestDistance = h60;
                    closestIndex = 60;
                }
            }
            if(exists62 && dist61 + 1 < dist62)
            {
                dist62 = dist61 + 1;
                lookup[indexToLocalY(62)][indexToLocalX(62)] = Direction.WEST;
                if(h62 < closestDistance)
                {
                    closestDistance = h62;
                    closestIndex = 62;
                }
            }
            if(exists71 && dist61 + 1 < dist71)
            {
                dist71 = dist61 + 1;
                lookup[indexToLocalY(71)][indexToLocalX(71)] = Direction.NORTHEAST;
                if(h71 < closestDistance)
                {
                    closestDistance = h71;
                    closestIndex = 71;
                }
            }
            if(exists72 && dist61 + 1 < dist72)
            {
                dist72 = dist61 + 1;
                lookup[indexToLocalY(72)][indexToLocalX(72)] = Direction.NORTH;
                if(h72 < closestDistance)
                {
                    closestDistance = h72;
                    closestIndex = 72;
                }
            }
            if(exists73 && dist61 + 1 < dist73)
            {
                dist73 = dist61 + 1;
                lookup[indexToLocalY(73)][indexToLocalX(73)] = Direction.NORTHWEST;
                if(h73 < closestDistance)
                {
                    closestDistance = h73;
                    closestIndex = 73;
                }
            }
        }
        if(exists70)
        {
            if(exists58 && dist70 + 1 < dist58)
            {
                dist58 = dist70 + 1;
                lookup[indexToLocalY(58)][indexToLocalX(58)] = Direction.SOUTHEAST;
                if(h58 < closestDistance)
                {
                    closestDistance = h58;
                    closestIndex = 58;
                }
            }
            if(exists59 && dist70 + 1 < dist59)
            {
                dist59 = dist70 + 1;
                lookup[indexToLocalY(59)][indexToLocalX(59)] = Direction.SOUTH;
                if(h59 < closestDistance)
                {
                    closestDistance = h59;
                    closestIndex = 59;
                }
            }
            if(exists60 && dist70 + 1 < dist60)
            {
                dist60 = dist70 + 1;
                lookup[indexToLocalY(60)][indexToLocalX(60)] = Direction.SOUTHWEST;
                if(h60 < closestDistance)
                {
                    closestDistance = h60;
                    closestIndex = 60;
                }
            }
            if(exists69 && dist70 + 1 < dist69)
            {
                dist69 = dist70 + 1;
                lookup[indexToLocalY(69)][indexToLocalX(69)] = Direction.EAST;
                if(h69 < closestDistance)
                {
                    closestDistance = h69;
                    closestIndex = 69;
                }
            }
            if(exists71 && dist70 + 1 < dist71)
            {
                dist71 = dist70 + 1;
                lookup[indexToLocalY(71)][indexToLocalX(71)] = Direction.WEST;
                if(h71 < closestDistance)
                {
                    closestDistance = h71;
                    closestIndex = 71;
                }
            }
            if(exists80 && dist70 + 1 < dist80)
            {
                dist80 = dist70 + 1;
                lookup[indexToLocalY(80)][indexToLocalX(80)] = Direction.NORTHEAST;
                if(h80 < closestDistance)
                {
                    closestDistance = h80;
                    closestIndex = 80;
                }
            }
            if(exists81 && dist70 + 1 < dist81)
            {
                dist81 = dist70 + 1;
                lookup[indexToLocalY(81)][indexToLocalX(81)] = Direction.NORTH;
                if(h81 < closestDistance)
                {
                    closestDistance = h81;
                    closestIndex = 81;
                }
            }
            if(exists82 && dist70 + 1 < dist82)
            {
                dist82 = dist70 + 1;
                lookup[indexToLocalY(82)][indexToLocalX(82)] = Direction.NORTHWEST;
                if(h82 < closestDistance)
                {
                    closestDistance = h82;
                    closestIndex = 82;
                }
            }
        }
        if(exists71)
        {
            if(exists59 && dist71 + 1 < dist59)
            {
                dist59 = dist71 + 1;
                lookup[indexToLocalY(59)][indexToLocalX(59)] = Direction.SOUTHEAST;
                if(h59 < closestDistance)
                {
                    closestDistance = h59;
                    closestIndex = 59;
                }
            }
            if(exists60 && dist71 + 1 < dist60)
            {
                dist60 = dist71 + 1;
                lookup[indexToLocalY(60)][indexToLocalX(60)] = Direction.SOUTH;
                if(h60 < closestDistance)
                {
                    closestDistance = h60;
                    closestIndex = 60;
                }
            }
            if(exists61 && dist71 + 1 < dist61)
            {
                dist61 = dist71 + 1;
                lookup[indexToLocalY(61)][indexToLocalX(61)] = Direction.SOUTHWEST;
                if(h61 < closestDistance)
                {
                    closestDistance = h61;
                    closestIndex = 61;
                }
            }
            if(exists70 && dist71 + 1 < dist70)
            {
                dist70 = dist71 + 1;
                lookup[indexToLocalY(70)][indexToLocalX(70)] = Direction.EAST;
                if(h70 < closestDistance)
                {
                    closestDistance = h70;
                    closestIndex = 70;
                }
            }
            if(exists72 && dist71 + 1 < dist72)
            {
                dist72 = dist71 + 1;
                lookup[indexToLocalY(72)][indexToLocalX(72)] = Direction.WEST;
                if(h72 < closestDistance)
                {
                    closestDistance = h72;
                    closestIndex = 72;
                }
            }
            if(exists81 && dist71 + 1 < dist81)
            {
                dist81 = dist71 + 1;
                lookup[indexToLocalY(81)][indexToLocalX(81)] = Direction.NORTHEAST;
                if(h81 < closestDistance)
                {
                    closestDistance = h81;
                    closestIndex = 81;
                }
            }
            if(exists82 && dist71 + 1 < dist82)
            {
                dist82 = dist71 + 1;
                lookup[indexToLocalY(82)][indexToLocalX(82)] = Direction.NORTH;
                if(h82 < closestDistance)
                {
                    closestDistance = h82;
                    closestIndex = 82;
                }
            }
            if(exists83 && dist71 + 1 < dist83)
            {
                dist83 = dist71 + 1;
                lookup[indexToLocalY(83)][indexToLocalX(83)] = Direction.NORTHWEST;
                if(h83 < closestDistance)
                {
                    closestDistance = h83;
                    closestIndex = 83;
                }
            }
        }
        if(exists72)
        {
            if(exists60 && dist72 + 1 < dist60)
            {
                dist60 = dist72 + 1;
                lookup[indexToLocalY(60)][indexToLocalX(60)] = Direction.SOUTHEAST;
                if(h60 < closestDistance)
                {
                    closestDistance = h60;
                    closestIndex = 60;
                }
            }
            if(exists61 && dist72 + 1 < dist61)
            {
                dist61 = dist72 + 1;
                lookup[indexToLocalY(61)][indexToLocalX(61)] = Direction.SOUTH;
                if(h61 < closestDistance)
                {
                    closestDistance = h61;
                    closestIndex = 61;
                }
            }
            if(exists62 && dist72 + 1 < dist62)
            {
                dist62 = dist72 + 1;
                lookup[indexToLocalY(62)][indexToLocalX(62)] = Direction.SOUTHWEST;
                if(h62 < closestDistance)
                {
                    closestDistance = h62;
                    closestIndex = 62;
                }
            }
            if(exists71 && dist72 + 1 < dist71)
            {
                dist71 = dist72 + 1;
                lookup[indexToLocalY(71)][indexToLocalX(71)] = Direction.EAST;
                if(h71 < closestDistance)
                {
                    closestDistance = h71;
                    closestIndex = 71;
                }
            }
            if(exists73 && dist72 + 1 < dist73)
            {
                dist73 = dist72 + 1;
                lookup[indexToLocalY(73)][indexToLocalX(73)] = Direction.WEST;
                if(h73 < closestDistance)
                {
                    closestDistance = h73;
                    closestIndex = 73;
                }
            }
            if(exists82 && dist72 + 1 < dist82)
            {
                dist82 = dist72 + 1;
                lookup[indexToLocalY(82)][indexToLocalX(82)] = Direction.NORTHEAST;
                if(h82 < closestDistance)
                {
                    closestDistance = h82;
                    closestIndex = 82;
                }
            }
            if(exists83 && dist72 + 1 < dist83)
            {
                dist83 = dist72 + 1;
                lookup[indexToLocalY(83)][indexToLocalX(83)] = Direction.NORTH;
                if(h83 < closestDistance)
                {
                    closestDistance = h83;
                    closestIndex = 83;
                }
            }
            if(exists84 && dist72 + 1 < dist84)
            {
                dist84 = dist72 + 1;
                lookup[indexToLocalY(84)][indexToLocalX(84)] = Direction.NORTHWEST;
                if(h84 < closestDistance)
                {
                    closestDistance = h84;
                    closestIndex = 84;
                }
            }
        }
        if(exists36)
        {
            if(exists24 && dist36 + 1 < dist24)
            {
                dist24 = dist36 + 1;
                lookup[indexToLocalY(24)][indexToLocalX(24)] = Direction.SOUTHEAST;
                if(h24 < closestDistance)
                {
                    closestDistance = h24;
                    closestIndex = 24;
                }
            }
            if(exists25 && dist36 + 1 < dist25)
            {
                dist25 = dist36 + 1;
                lookup[indexToLocalY(25)][indexToLocalX(25)] = Direction.SOUTH;
                if(h25 < closestDistance)
                {
                    closestDistance = h25;
                    closestIndex = 25;
                }
            }
            if(exists26 && dist36 + 1 < dist26)
            {
                dist26 = dist36 + 1;
                lookup[indexToLocalY(26)][indexToLocalX(26)] = Direction.SOUTHWEST;
                if(h26 < closestDistance)
                {
                    closestDistance = h26;
                    closestIndex = 26;
                }
            }
            if(exists35 && dist36 + 1 < dist35)
            {
                dist35 = dist36 + 1;
                lookup[indexToLocalY(35)][indexToLocalX(35)] = Direction.EAST;
                if(h35 < closestDistance)
                {
                    closestDistance = h35;
                    closestIndex = 35;
                }
            }
            if(exists37 && dist36 + 1 < dist37)
            {
                dist37 = dist36 + 1;
                lookup[indexToLocalY(37)][indexToLocalX(37)] = Direction.WEST;
                if(h37 < closestDistance)
                {
                    closestDistance = h37;
                    closestIndex = 37;
                }
            }
            if(exists46 && dist36 + 1 < dist46)
            {
                dist46 = dist36 + 1;
                lookup[indexToLocalY(46)][indexToLocalX(46)] = Direction.NORTHEAST;
                if(h46 < closestDistance)
                {
                    closestDistance = h46;
                    closestIndex = 46;
                }
            }
            if(exists47 && dist36 + 1 < dist47)
            {
                dist47 = dist36 + 1;
                lookup[indexToLocalY(47)][indexToLocalX(47)] = Direction.NORTH;
                if(h47 < closestDistance)
                {
                    closestDistance = h47;
                    closestIndex = 47;
                }
            }
            if(exists48 && dist36 + 1 < dist48)
            {
                dist48 = dist36 + 1;
                lookup[indexToLocalY(48)][indexToLocalX(48)] = Direction.NORTHWEST;
                if(h48 < closestDistance)
                {
                    closestDistance = h48;
                    closestIndex = 48;
                }
            }
        }
        if(exists37)
        {
            if(exists25 && dist37 + 1 < dist25)
            {
                dist25 = dist37 + 1;
                lookup[indexToLocalY(25)][indexToLocalX(25)] = Direction.SOUTHEAST;
                if(h25 < closestDistance)
                {
                    closestDistance = h25;
                    closestIndex = 25;
                }
            }
            if(exists26 && dist37 + 1 < dist26)
            {
                dist26 = dist37 + 1;
                lookup[indexToLocalY(26)][indexToLocalX(26)] = Direction.SOUTH;
                if(h26 < closestDistance)
                {
                    closestDistance = h26;
                    closestIndex = 26;
                }
            }
            if(exists27 && dist37 + 1 < dist27)
            {
                dist27 = dist37 + 1;
                lookup[indexToLocalY(27)][indexToLocalX(27)] = Direction.SOUTHWEST;
                if(h27 < closestDistance)
                {
                    closestDistance = h27;
                    closestIndex = 27;
                }
            }
            if(exists36 && dist37 + 1 < dist36)
            {
                dist36 = dist37 + 1;
                lookup[indexToLocalY(36)][indexToLocalX(36)] = Direction.EAST;
                if(h36 < closestDistance)
                {
                    closestDistance = h36;
                    closestIndex = 36;
                }
            }
            if(exists38 && dist37 + 1 < dist38)
            {
                dist38 = dist37 + 1;
                lookup[indexToLocalY(38)][indexToLocalX(38)] = Direction.WEST;
                if(h38 < closestDistance)
                {
                    closestDistance = h38;
                    closestIndex = 38;
                }
            }
            if(exists47 && dist37 + 1 < dist47)
            {
                dist47 = dist37 + 1;
                lookup[indexToLocalY(47)][indexToLocalX(47)] = Direction.NORTHEAST;
                if(h47 < closestDistance)
                {
                    closestDistance = h47;
                    closestIndex = 47;
                }
            }
            if(exists48 && dist37 + 1 < dist48)
            {
                dist48 = dist37 + 1;
                lookup[indexToLocalY(48)][indexToLocalX(48)] = Direction.NORTH;
                if(h48 < closestDistance)
                {
                    closestDistance = h48;
                    closestIndex = 48;
                }
            }
            if(exists49 && dist37 + 1 < dist49)
            {
                dist49 = dist37 + 1;
                lookup[indexToLocalY(49)][indexToLocalX(49)] = Direction.NORTHWEST;
                if(h49 < closestDistance)
                {
                    closestDistance = h49;
                    closestIndex = 49;
                }
            }
        }
        if(exists38)
        {
            if(exists26 && dist38 + 1 < dist26)
            {
                dist26 = dist38 + 1;
                lookup[indexToLocalY(26)][indexToLocalX(26)] = Direction.SOUTHEAST;
                if(h26 < closestDistance)
                {
                    closestDistance = h26;
                    closestIndex = 26;
                }
            }
            if(exists27 && dist38 + 1 < dist27)
            {
                dist27 = dist38 + 1;
                lookup[indexToLocalY(27)][indexToLocalX(27)] = Direction.SOUTH;
                if(h27 < closestDistance)
                {
                    closestDistance = h27;
                    closestIndex = 27;
                }
            }
            if(exists28 && dist38 + 1 < dist28)
            {
                dist28 = dist38 + 1;
                lookup[indexToLocalY(28)][indexToLocalX(28)] = Direction.SOUTHWEST;
                if(h28 < closestDistance)
                {
                    closestDistance = h28;
                    closestIndex = 28;
                }
            }
            if(exists37 && dist38 + 1 < dist37)
            {
                dist37 = dist38 + 1;
                lookup[indexToLocalY(37)][indexToLocalX(37)] = Direction.EAST;
                if(h37 < closestDistance)
                {
                    closestDistance = h37;
                    closestIndex = 37;
                }
            }
            if(exists39 && dist38 + 1 < dist39)
            {
                dist39 = dist38 + 1;
                lookup[indexToLocalY(39)][indexToLocalX(39)] = Direction.WEST;
                if(h39 < closestDistance)
                {
                    closestDistance = h39;
                    closestIndex = 39;
                }
            }
            if(exists48 && dist38 + 1 < dist48)
            {
                dist48 = dist38 + 1;
                lookup[indexToLocalY(48)][indexToLocalX(48)] = Direction.NORTHEAST;
                if(h48 < closestDistance)
                {
                    closestDistance = h48;
                    closestIndex = 48;
                }
            }
            if(exists49 && dist38 + 1 < dist49)
            {
                dist49 = dist38 + 1;
                lookup[indexToLocalY(49)][indexToLocalX(49)] = Direction.NORTH;
                if(h49 < closestDistance)
                {
                    closestDistance = h49;
                    closestIndex = 49;
                }
            }
            if(exists50 && dist38 + 1 < dist50)
            {
                dist50 = dist38 + 1;
                lookup[indexToLocalY(50)][indexToLocalX(50)] = Direction.NORTHWEST;
                if(h50 < closestDistance)
                {
                    closestDistance = h50;
                    closestIndex = 50;
                }
            }
        }
        if(exists47)
        {
            if(exists35 && dist47 + 1 < dist35)
            {
                dist35 = dist47 + 1;
                lookup[indexToLocalY(35)][indexToLocalX(35)] = Direction.SOUTHEAST;
                if(h35 < closestDistance)
                {
                    closestDistance = h35;
                    closestIndex = 35;
                }
            }
            if(exists36 && dist47 + 1 < dist36)
            {
                dist36 = dist47 + 1;
                lookup[indexToLocalY(36)][indexToLocalX(36)] = Direction.SOUTH;
                if(h36 < closestDistance)
                {
                    closestDistance = h36;
                    closestIndex = 36;
                }
            }
            if(exists37 && dist47 + 1 < dist37)
            {
                dist37 = dist47 + 1;
                lookup[indexToLocalY(37)][indexToLocalX(37)] = Direction.SOUTHWEST;
                if(h37 < closestDistance)
                {
                    closestDistance = h37;
                    closestIndex = 37;
                }
            }
            if(exists46 && dist47 + 1 < dist46)
            {
                dist46 = dist47 + 1;
                lookup[indexToLocalY(46)][indexToLocalX(46)] = Direction.EAST;
                if(h46 < closestDistance)
                {
                    closestDistance = h46;
                    closestIndex = 46;
                }
            }
            if(exists48 && dist47 + 1 < dist48)
            {
                dist48 = dist47 + 1;
                lookup[indexToLocalY(48)][indexToLocalX(48)] = Direction.WEST;
                if(h48 < closestDistance)
                {
                    closestDistance = h48;
                    closestIndex = 48;
                }
            }
            if(exists57 && dist47 + 1 < dist57)
            {
                dist57 = dist47 + 1;
                lookup[indexToLocalY(57)][indexToLocalX(57)] = Direction.NORTHEAST;
                if(h57 < closestDistance)
                {
                    closestDistance = h57;
                    closestIndex = 57;
                }
            }
            if(exists58 && dist47 + 1 < dist58)
            {
                dist58 = dist47 + 1;
                lookup[indexToLocalY(58)][indexToLocalX(58)] = Direction.NORTH;
                if(h58 < closestDistance)
                {
                    closestDistance = h58;
                    closestIndex = 58;
                }
            }
            if(exists59 && dist47 + 1 < dist59)
            {
                dist59 = dist47 + 1;
                lookup[indexToLocalY(59)][indexToLocalX(59)] = Direction.NORTHWEST;
                if(h59 < closestDistance)
                {
                    closestDistance = h59;
                    closestIndex = 59;
                }
            }
        }
        if(exists58)
        {
            if(exists46 && dist58 + 1 < dist46)
            {
                dist46 = dist58 + 1;
                lookup[indexToLocalY(46)][indexToLocalX(46)] = Direction.SOUTHEAST;
                if(h46 < closestDistance)
                {
                    closestDistance = h46;
                    closestIndex = 46;
                }
            }
            if(exists47 && dist58 + 1 < dist47)
            {
                dist47 = dist58 + 1;
                lookup[indexToLocalY(47)][indexToLocalX(47)] = Direction.SOUTH;
                if(h47 < closestDistance)
                {
                    closestDistance = h47;
                    closestIndex = 47;
                }
            }
            if(exists48 && dist58 + 1 < dist48)
            {
                dist48 = dist58 + 1;
                lookup[indexToLocalY(48)][indexToLocalX(48)] = Direction.SOUTHWEST;
                if(h48 < closestDistance)
                {
                    closestDistance = h48;
                    closestIndex = 48;
                }
            }
            if(exists57 && dist58 + 1 < dist57)
            {
                dist57 = dist58 + 1;
                lookup[indexToLocalY(57)][indexToLocalX(57)] = Direction.EAST;
                if(h57 < closestDistance)
                {
                    closestDistance = h57;
                    closestIndex = 57;
                }
            }
            if(exists59 && dist58 + 1 < dist59)
            {
                dist59 = dist58 + 1;
                lookup[indexToLocalY(59)][indexToLocalX(59)] = Direction.WEST;
                if(h59 < closestDistance)
                {
                    closestDistance = h59;
                    closestIndex = 59;
                }
            }
            if(exists68 && dist58 + 1 < dist68)
            {
                dist68 = dist58 + 1;
                lookup[indexToLocalY(68)][indexToLocalX(68)] = Direction.NORTHEAST;
                if(h68 < closestDistance)
                {
                    closestDistance = h68;
                    closestIndex = 68;
                }
            }
            if(exists69 && dist58 + 1 < dist69)
            {
                dist69 = dist58 + 1;
                lookup[indexToLocalY(69)][indexToLocalX(69)] = Direction.NORTH;
                if(h69 < closestDistance)
                {
                    closestDistance = h69;
                    closestIndex = 69;
                }
            }
            if(exists70 && dist58 + 1 < dist70)
            {
                dist70 = dist58 + 1;
                lookup[indexToLocalY(70)][indexToLocalX(70)] = Direction.NORTHWEST;
                if(h70 < closestDistance)
                {
                    closestDistance = h70;
                    closestIndex = 70;
                }
            }
        }
        if(exists39)
        {
            if(exists27 && dist39 + 1 < dist27)
            {
                dist27 = dist39 + 1;
                lookup[indexToLocalY(27)][indexToLocalX(27)] = Direction.SOUTHEAST;
                if(h27 < closestDistance)
                {
                    closestDistance = h27;
                    closestIndex = 27;
                }
            }
            if(exists28 && dist39 + 1 < dist28)
            {
                dist28 = dist39 + 1;
                lookup[indexToLocalY(28)][indexToLocalX(28)] = Direction.SOUTH;
                if(h28 < closestDistance)
                {
                    closestDistance = h28;
                    closestIndex = 28;
                }
            }
            if(exists29 && dist39 + 1 < dist29)
            {
                dist29 = dist39 + 1;
                lookup[indexToLocalY(29)][indexToLocalX(29)] = Direction.SOUTHWEST;
                if(h29 < closestDistance)
                {
                    closestDistance = h29;
                    closestIndex = 29;
                }
            }
            if(exists38 && dist39 + 1 < dist38)
            {
                dist38 = dist39 + 1;
                lookup[indexToLocalY(38)][indexToLocalX(38)] = Direction.EAST;
                if(h38 < closestDistance)
                {
                    closestDistance = h38;
                    closestIndex = 38;
                }
            }
            if(exists40 && dist39 + 1 < dist40)
            {
                dist40 = dist39 + 1;
                lookup[indexToLocalY(40)][indexToLocalX(40)] = Direction.WEST;
                if(h40 < closestDistance)
                {
                    closestDistance = h40;
                    closestIndex = 40;
                }
            }
            if(exists49 && dist39 + 1 < dist49)
            {
                dist49 = dist39 + 1;
                lookup[indexToLocalY(49)][indexToLocalX(49)] = Direction.NORTHEAST;
                if(h49 < closestDistance)
                {
                    closestDistance = h49;
                    closestIndex = 49;
                }
            }
            if(exists50 && dist39 + 1 < dist50)
            {
                dist50 = dist39 + 1;
                lookup[indexToLocalY(50)][indexToLocalX(50)] = Direction.NORTH;
                if(h50 < closestDistance)
                {
                    closestDistance = h50;
                    closestIndex = 50;
                }
            }
            if(exists51 && dist39 + 1 < dist51)
            {
                dist51 = dist39 + 1;
                lookup[indexToLocalY(51)][indexToLocalX(51)] = Direction.NORTHWEST;
                if(h51 < closestDistance)
                {
                    closestDistance = h51;
                    closestIndex = 51;
                }
            }
        }
        if(exists40)
        {
            if(exists28 && dist40 + 1 < dist28)
            {
                dist28 = dist40 + 1;
                lookup[indexToLocalY(28)][indexToLocalX(28)] = Direction.SOUTHEAST;
                if(h28 < closestDistance)
                {
                    closestDistance = h28;
                    closestIndex = 28;
                }
            }
            if(exists29 && dist40 + 1 < dist29)
            {
                dist29 = dist40 + 1;
                lookup[indexToLocalY(29)][indexToLocalX(29)] = Direction.SOUTH;
                if(h29 < closestDistance)
                {
                    closestDistance = h29;
                    closestIndex = 29;
                }
            }
            if(exists30 && dist40 + 1 < dist30)
            {
                dist30 = dist40 + 1;
                lookup[indexToLocalY(30)][indexToLocalX(30)] = Direction.SOUTHWEST;
                if(h30 < closestDistance)
                {
                    closestDistance = h30;
                    closestIndex = 30;
                }
            }
            if(exists39 && dist40 + 1 < dist39)
            {
                dist39 = dist40 + 1;
                lookup[indexToLocalY(39)][indexToLocalX(39)] = Direction.EAST;
                if(h39 < closestDistance)
                {
                    closestDistance = h39;
                    closestIndex = 39;
                }
            }
            if(exists41 && dist40 + 1 < dist41)
            {
                dist41 = dist40 + 1;
                lookup[indexToLocalY(41)][indexToLocalX(41)] = Direction.WEST;
                if(h41 < closestDistance)
                {
                    closestDistance = h41;
                    closestIndex = 41;
                }
            }
            if(exists50 && dist40 + 1 < dist50)
            {
                dist50 = dist40 + 1;
                lookup[indexToLocalY(50)][indexToLocalX(50)] = Direction.NORTHEAST;
                if(h50 < closestDistance)
                {
                    closestDistance = h50;
                    closestIndex = 50;
                }
            }
            if(exists51 && dist40 + 1 < dist51)
            {
                dist51 = dist40 + 1;
                lookup[indexToLocalY(51)][indexToLocalX(51)] = Direction.NORTH;
                if(h51 < closestDistance)
                {
                    closestDistance = h51;
                    closestIndex = 51;
                }
            }
            if(exists52 && dist40 + 1 < dist52)
            {
                dist52 = dist40 + 1;
                lookup[indexToLocalY(52)][indexToLocalX(52)] = Direction.NORTHWEST;
                if(h52 < closestDistance)
                {
                    closestDistance = h52;
                    closestIndex = 52;
                }
            }
        }
        if(exists51)
        {
            if(exists39 && dist51 + 1 < dist39)
            {
                dist39 = dist51 + 1;
                lookup[indexToLocalY(39)][indexToLocalX(39)] = Direction.SOUTHEAST;
                if(h39 < closestDistance)
                {
                    closestDistance = h39;
                    closestIndex = 39;
                }
            }
            if(exists40 && dist51 + 1 < dist40)
            {
                dist40 = dist51 + 1;
                lookup[indexToLocalY(40)][indexToLocalX(40)] = Direction.SOUTH;
                if(h40 < closestDistance)
                {
                    closestDistance = h40;
                    closestIndex = 40;
                }
            }
            if(exists41 && dist51 + 1 < dist41)
            {
                dist41 = dist51 + 1;
                lookup[indexToLocalY(41)][indexToLocalX(41)] = Direction.SOUTHWEST;
                if(h41 < closestDistance)
                {
                    closestDistance = h41;
                    closestIndex = 41;
                }
            }
            if(exists50 && dist51 + 1 < dist50)
            {
                dist50 = dist51 + 1;
                lookup[indexToLocalY(50)][indexToLocalX(50)] = Direction.EAST;
                if(h50 < closestDistance)
                {
                    closestDistance = h50;
                    closestIndex = 50;
                }
            }
            if(exists52 && dist51 + 1 < dist52)
            {
                dist52 = dist51 + 1;
                lookup[indexToLocalY(52)][indexToLocalX(52)] = Direction.WEST;
                if(h52 < closestDistance)
                {
                    closestDistance = h52;
                    closestIndex = 52;
                }
            }
            if(exists61 && dist51 + 1 < dist61)
            {
                dist61 = dist51 + 1;
                lookup[indexToLocalY(61)][indexToLocalX(61)] = Direction.NORTHEAST;
                if(h61 < closestDistance)
                {
                    closestDistance = h61;
                    closestIndex = 61;
                }
            }
            if(exists62 && dist51 + 1 < dist62)
            {
                dist62 = dist51 + 1;
                lookup[indexToLocalY(62)][indexToLocalX(62)] = Direction.NORTH;
                if(h62 < closestDistance)
                {
                    closestDistance = h62;
                    closestIndex = 62;
                }
            }
            if(exists63 && dist51 + 1 < dist63)
            {
                dist63 = dist51 + 1;
                lookup[indexToLocalY(63)][indexToLocalX(63)] = Direction.NORTHWEST;
                if(h63 < closestDistance)
                {
                    closestDistance = h63;
                    closestIndex = 63;
                }
            }
        }
    }
}

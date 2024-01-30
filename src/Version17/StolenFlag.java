package Version17;

import battlecode.common.MapLocation;

public class StolenFlag
{
    public MapLocation location;
    //false is our team true is the enemy team
    public boolean team;

    public StolenFlag(MapLocation location, boolean team)
    {
        this.location = location;
        this.team = team;
    }

    @Override
    public String toString()
    {
        return "Flag{" +
                "location=" + location +
                ", team=" + (!team ?  "US" : "ENEMY")  +
                '}';
    }
}

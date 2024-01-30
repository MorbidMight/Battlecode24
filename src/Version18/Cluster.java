package Version18;

import battlecode.common.MapLocation;

public class Cluster
{
    public MapLocation location;
    public int numEnemies;
    public Cluster(MapLocation location, int numEnemies)
    {
        this.location = location;
        this.numEnemies = 22 * numEnemies;
    }

    @Override
    public String toString()
    {
        return "Location: " + location + ", NumEnemies: " + numEnemies;
    }
}

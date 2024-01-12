package Version3;

import battlecode.common.MapLocation;

public class Task
{
    public MapLocation location;
    public boolean builderDispatched;

    public int arrayIndex;

    public Task(MapLocation location, boolean builderDispatched, int arrayIndex ) {
        this.location = location;
        this.builderDispatched = builderDispatched;
        this.arrayIndex = arrayIndex;
    }

    public Task(MapLocation location, boolean builderDispatched)
    {
        this.location = location;
        this.builderDispatched = builderDispatched;
    }

    @Override
    public String toString() {
        return "Task{" +
                "location=" + location +
                ", builderDispatched=" + builderDispatched +
                ", arrayIndex=" + arrayIndex +
                '}';
    }
}

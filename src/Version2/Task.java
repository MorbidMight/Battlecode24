package Version2;

import battlecode.common.MapLocation;

public class Task
{
    public MapLocation location;
    public boolean used;
    public boolean duckType;
    public boolean isComing;

    public Task(MapLocation location, boolean taskActive, boolean duckType, boolean isComing) {
        this.location = location;
        this.used = taskActive;
        this.duckType = duckType;
        this.isComing = isComing;
    }

    public Task(){}

    @Override
    public String toString() {
        return "Task{" +
                "location=" + location +
                ", used=" + used +
                ", duckType=" + duckType +
                ", isComing=" + isComing +
                '}';
    }
}

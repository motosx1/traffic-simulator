package pl.zablocki.core.roadobjects;

public class Obstacle extends  RoadObject {
    public Obstacle() {
        this.setMaxSpeed(0);
        this.setSpeed(0);
        this.setObjectType(ObjectType.OBSTACLE);
    }
}

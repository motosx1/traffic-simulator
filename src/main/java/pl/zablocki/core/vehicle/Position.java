package pl.zablocki.core.vehicle;

import lombok.ToString;
import pl.zablocki.core.roadnetwork.Road;

@ToString
public class Position {

    private Road currentRoad = new Road();
    private double distance = 0;

    public Position() {
    }

    public Position(Position position) {
        this.currentRoad = position.getCurrentRoad();
        this.distance = position.getDistance();
    }

    public Position(Road currentRoad, double distance) {
        this.currentRoad = currentRoad;
        this.distance = distance;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    double getDistance() {
        return distance;
    }

    void setDistance(double distance) {
        this.distance = distance;
    }
}
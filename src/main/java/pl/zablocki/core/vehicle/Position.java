package pl.zablocki.core.vehicle;

import lombok.ToString;
import pl.zablocki.core.roadnetwork.Road;

@ToString
public class Position {

    private Road currentRoad = null;
    private double distance = 0;

    public Position() {
    }

    public Position(Position position) {
        this.currentRoad = null;
        this.distance = position.getDistance();
    }

    Road getCurrentRoad() {
        return currentRoad;
    }

    double getDistance() {
        return distance;
    }

    void setDistance(double distance) {
        this.distance = distance;
    }
}
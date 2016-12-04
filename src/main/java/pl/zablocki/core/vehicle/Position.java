package pl.zablocki.core.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.zablocki.core.roadnetwork.Road;

@ToString
public class Position {

    @Getter
    private Road currentRoad = new Road();
    @Getter @Setter
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

}
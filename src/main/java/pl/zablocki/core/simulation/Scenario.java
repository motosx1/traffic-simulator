package pl.zablocki.core.simulation;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.road.RoadObject;
import pl.zablocki.core.vehicle.StopLight;

@ToString
public class Scenario {
    private static int idCounter = 0;
    @Getter
    private final int id;
    @Getter @Setter
    private double carsPerHour;
    @Getter @Setter
    private RoadObject typicalVehicle;
    @Getter @Setter
    private StopLight stopLight;
    @Getter @Setter
    private Road road;

    public Scenario() {
        this.id = idCounter++;
    }
}
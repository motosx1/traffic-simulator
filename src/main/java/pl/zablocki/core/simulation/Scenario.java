package pl.zablocki.core.simulation;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.roadobjects.RoadObject;

@ToString
public class Scenario {
    private static int vehicleCounter = 0;
    @Getter
    private final int id;
    @Getter @Setter
    private RoadObject typicalVehicle;
    @Getter @Setter
    private Road road;

    public Scenario() {
        this.id = vehicleCounter++;
    }
}
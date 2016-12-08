package pl.zablocki.core.road;

import lombok.Getter;
import lombok.Setter;
import pl.zablocki.core.vehicle.StopLight;
import pl.zablocki.core.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Line {
    @Getter
    private int id;
    public int vehicleCounter;
    @Getter
    private List<Vehicle> vehicles = new ArrayList<>();
    @Getter
    @Setter
    StopLight stopLight;

    public Line(int id) {
        this.id = id;
    }
}

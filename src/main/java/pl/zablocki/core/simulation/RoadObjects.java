package pl.zablocki.core.simulation;

import lombok.Getter;
import lombok.Setter;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.Vehicle;

import java.util.List;

public class RoadObjects {
    @Getter
    @Setter
    List<Vehicle> vehicles;
    @Getter
    @Setter
    StopLights stopLights;
    @Getter
    @Setter
    double elapsedTime;

}

package pl.zablocki.core.simulation;

import lombok.Getter;
import lombok.Setter;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class RoadObjects {
    @Setter
    List<Vehicle> vehicles;
    @Getter
    @Setter
    List<StopLights> stopLights;
    @Getter
    @Setter
    double elapsedTime;

    public List<Vehicle> getVehicles() {
        if (vehicles == null) {
            this.vehicles = new ArrayList<>();
        }
        return vehicles;
    }
}

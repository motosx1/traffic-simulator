package pl.zablocki.core.vehicle;

import java.util.List;

public interface VehicleDataListener {
    public void updateVehicles(List<Vehicle> vehicles);
    public void updateStopLights(StopLights stopLights);
}
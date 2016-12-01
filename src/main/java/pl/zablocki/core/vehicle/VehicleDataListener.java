package pl.zablocki.core.vehicle;

import pl.zablocki.core.simulation.RoadObjects;

import java.util.List;

public interface VehicleDataListener {
    public void updateRoadObjects(RoadObjects roadObjects);
}
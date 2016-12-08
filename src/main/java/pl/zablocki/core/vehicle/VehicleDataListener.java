package pl.zablocki.core.vehicle;

import pl.zablocki.core.simulation.RoadData;

public interface VehicleDataListener {
    void updateRoadObjects(RoadData roadData);
}
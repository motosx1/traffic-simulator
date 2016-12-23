package pl.zablocki.core.roadobjects;

import pl.zablocki.core.simulation.RoadData;

public interface VehicleDataListener {
    void updateRoadObjects(RoadData roadData);
}
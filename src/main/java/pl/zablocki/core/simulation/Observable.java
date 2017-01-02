package pl.zablocki.core.simulation;

import pl.zablocki.core.roadobjects.VehicleDataListener;

public interface Observable {
    void addListener(VehicleDataListener listener);
    void notifyListeners(RoadData roadData);
    void notifyListenersEnd();
}

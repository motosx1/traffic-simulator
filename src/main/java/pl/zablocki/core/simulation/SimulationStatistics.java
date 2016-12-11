package pl.zablocki.core.simulation;

import lombok.Getter;
import lombok.Setter;
import pl.zablocki.core.road.Road;

import java.util.HashMap;
import java.util.Map;

public class SimulationStatistics {
    @Getter
    @Setter
    private double elapsedTime;

    @Setter
    @Getter
    private Map<Road, Double> averageSpeed = new HashMap<>();
    @Setter
    @Getter
    private Map<Road, Integer> stoppedVehicles = new HashMap<>();
}

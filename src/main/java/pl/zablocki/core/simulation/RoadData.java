package pl.zablocki.core.simulation;

import lombok.Getter;
import lombok.Setter;
import pl.zablocki.core.road.Road;

import java.util.ArrayList;
import java.util.List;

public class RoadData {
    @Getter
    @Setter
    private List<Road> roads = new ArrayList<>();
    @Getter
    private SimulationStatistics simulationStatistics = new SimulationStatistics();
    @Getter @Setter
    private Double simulationDuration;
}

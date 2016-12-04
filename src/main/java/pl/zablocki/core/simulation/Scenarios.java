package pl.zablocki.core.simulation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Scenarios {
    @Getter @Setter
    private List<Scenario> scenarios;
    @Getter @Setter
    private double simulationDuration;

}

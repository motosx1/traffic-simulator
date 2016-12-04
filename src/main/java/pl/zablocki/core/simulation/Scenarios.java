package pl.zablocki.core.simulation;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Scenarios {
    @Getter @Setter
    private List<Scenario> scenarios = new ArrayList<>();
    @Getter @Setter
    private double simulationDuration;

}

package pl.zablocki.xml;

import lombok.Getter;
import lombok.Setter;

class Scenario {
    @Getter
    private int id;
    @Getter
    @Setter
    private TypicalVehicle typicalVehicle;
    @Getter
    @Setter
    private Road road;
}

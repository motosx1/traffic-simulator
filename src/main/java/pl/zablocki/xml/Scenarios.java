package pl.zablocki.xml;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
class Scenarios {
    @Setter
    private List<Scenario> scenarios = new ArrayList<>();
    @Getter
    @Setter
    private double simulationDuration;

    @XmlElement(name = "scenario")
    public List<Scenario> getScenarios() {
        return scenarios;
    }

}
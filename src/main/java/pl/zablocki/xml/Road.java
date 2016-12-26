package pl.zablocki.xml;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

class Road {
    @Getter
    @XmlAttribute
    private int id;
    private List<Line> lines = new ArrayList<>();
    @Getter
    @Setter
    private double autonomousPercentage;

    public Road() {}

    public Road(int id) {
        this.id = id;
    }

    @XmlElement(name = "line")
    public List<Line> getLines() {
        return lines;
    }
}

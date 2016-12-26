package pl.zablocki.xml;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

class Line {
    @Getter
    @XmlAttribute
    private int id;
    @Getter
    @Setter
    private StopLight stopLight;
    @Getter
    @Setter
    private double carsPerHour;
    private List<Obstacle> obstacles = new ArrayList<>();

    public Line() {
    }

    public Line(int id) {
        this.id = id;
    }

    @XmlElement(name = "obstacle")
    public List<Obstacle> getObstacles() {
        return obstacles;
    }
}

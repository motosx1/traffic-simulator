package pl.zablocki.core.road;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Road{

    @Getter
    private int id;
    @Getter
    private List<Line> lines = new ArrayList<>();
    public int vehicleCounter;
    @Getter
    @Setter
    private double autonomousPercentage;

    @Getter @Setter
    private int vehiclesDeleted;

    public Road(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Road road = (Road) o;

        return id == road.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
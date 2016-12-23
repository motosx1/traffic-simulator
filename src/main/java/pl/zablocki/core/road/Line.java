package pl.zablocki.core.road;

import lombok.Getter;
import lombok.Setter;
import pl.zablocki.core.roadobjects.StopLight;
import pl.zablocki.core.roadobjects.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Line {
    @Getter
    private int id;
    @Getter
    private List<Vehicle> vehicles = new ArrayList<>();
    @Getter
    @Setter
    private StopLight stopLight;
    @Getter
    @Setter
    private double carsPerHour;


    public Line(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (id != line.id) return false;
        return stopLight != null ? stopLight.equals(line.stopLight) : line.stopLight == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (stopLight != null ? stopLight.hashCode() : 0);
        return result;
    }
}

package pl.zablocki.core.road;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.zablocki.core.vehicle.StopLight;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Road {

    @Getter
    private int id;
    @Getter
    private List<Line> lines = new ArrayList<>();
    @Getter
    @Setter
    StopLight stopLight;

    public Road(int id) {
        this.id = id;
    }
}
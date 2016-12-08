package pl.zablocki.core.simulation;

import lombok.Getter;
import lombok.Setter;
import pl.zablocki.core.road.Road;

import java.util.ArrayList;
import java.util.List;

public class RoadData {
    @Getter
    @Setter
    List<Road> roads = new ArrayList<>();
    @Getter
    @Setter
    double elapsedTime;
}

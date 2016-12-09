package pl.zablocki.core.road;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Road {

    @Getter
    private int id;
    @Getter
    private List<Line> lines = new ArrayList<>();
    public int vehicleCounter;

    public Road(int id) {
        this.id = id;
    }
}
package pl.zablocki.core.roadnetwork;

import lombok.Getter;

public class Road {

    @Getter
    private int id = 10;
    private int priority;

    public Road() {
    }

    public Road(int id) {
        this.id=id;
    }
}
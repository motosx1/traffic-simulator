package pl.zablocki.core.roadnetwork;

import lombok.Getter;

public abstract class Road {

    @Getter
    private int id;
    private int priority;

    public Road() {

    }

}
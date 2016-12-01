package pl.zablocki.core.roadnetwork;

public abstract class Road {

    private int id;
    private int priority;

    public Road() {

    }

    public int getNewPosition(int timeElapsed) {
        return 0;
    }

}
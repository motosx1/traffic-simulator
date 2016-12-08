package pl.zablocki.core.vehicle;

import lombok.Getter;
import pl.zablocki.core.road.RoadObject;

import java.awt.*;

public class StopLight extends RoadObject{

    @Getter
    private int notifyRadius = 100;
    @Getter
    private boolean broadcastingRed = true;
    @Getter
    private boolean broadcastingGreen = false;
    @Getter
    private Color color = Color.RED;
    @Getter
    private int greenLightTimeSec;
    @Getter
    private int redLightTimeSec;

    public StopLight() {
        greenLightTimeSec = 40;
        redLightTimeSec = 10;
        setPosition(1600);
    }


    public boolean isRed() {
        return color == Color.RED;
    }

    public boolean isGreen() {
        return color == Color.GREEN;
    }

    public void setRed() {
        this.color = Color.RED;
    }

    public void setGreen() {
        this.color = Color.GREEN;
    }

    public void setBroadcastingRed(boolean broadcastingRed) {
        this.broadcastingRed = broadcastingRed;
        broadcastingGreen = false;
    }

    public void setBroadcastingGreen(boolean broadcastingGreen) {
        this.broadcastingGreen = broadcastingGreen;
        broadcastingRed = false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StopLight that = (StopLight) o;

        return getPosition() == that.getPosition();

    }

    @Override
    public int hashCode() {
        return 0;
    }
}

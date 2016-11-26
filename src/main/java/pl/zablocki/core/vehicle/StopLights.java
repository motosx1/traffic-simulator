package pl.zablocki.core.vehicle;

import lombok.Getter;

import java.awt.*;

public class StopLights {

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
    private Position position;

    public StopLights() {
        position = new Position(null, 800);
        greenLightTimeSec = 15;
        redLightTimeSec = 60;
    }


    public boolean isRed() {
        if( color == Color.RED ){
            return true;
        }
        return false;
    }
    public boolean isGreen() {
        if( color == Color.GREEN ){
            return true;
        }
        return false;
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

    Position getPosition() {
        return position;
    }

    void setPosition(Position position) {
        this.position = position;
    }

    void setDistance(double pos) {
        this.getPosition().setDistance(pos);
    }

    public double getDistance() {
        return this.getPosition().getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StopLights that = (StopLights) o;

        return position.equals(that.position);

    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}

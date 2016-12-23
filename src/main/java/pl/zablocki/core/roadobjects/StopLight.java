package pl.zablocki.core.roadobjects;

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

    public StopLight(double position) {
        greenLightTimeSec = 10;
        redLightTimeSec = 10;
        setPosition(position);
    }

    public boolean isVehicleInRange(Vehicle vehicle) {
        double notificationPoint = getPosition() - getNotifyRadius();
        double vehicleDistance = vehicle.getPosition();
        return notificationPoint >= vehicleDistance && notificationPoint - vehicleDistance < vehicle.getSpeed();
    }

    public void changeLight(double dt, double elapsedTime) {
        boolean hasChanged = false;

        if (isRed() && (elapsedTime % getRedLightTimeSec() < dt)) {
            setGreen();
            setBroadcastingGreen(true);
            hasChanged = true;
        }
        if (!hasChanged && isGreen() && (elapsedTime % getGreenLightTimeSec() < dt)) {
            setRed();
            setBroadcastingRed(true);
        }
    }

    private boolean isRed() {
        return color == Color.RED;
    }

    private boolean isGreen() {
        return color == Color.GREEN;
    }

    private void setRed() {
        this.color = Color.RED;
    }

    private void setGreen() {
        this.color = Color.GREEN;
    }

    public void setBroadcastingRed(boolean broadcastingRed) {
        this.broadcastingRed = broadcastingRed;
        broadcastingGreen = false;
    }

    private void setBroadcastingGreen(boolean broadcastingGreen) {
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
}

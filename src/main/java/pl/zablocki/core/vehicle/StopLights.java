package pl.zablocki.core.vehicle;

import lombok.Getter;

import java.awt.*;

public class StopLights extends Raoadable {

    @Getter
    private int notifyRadius = 100;
    @Getter
    private boolean broadcastingRed = true;
    @Getter
    private boolean broadcastingGreen = false;
    @Getter
    private Color color = Color.RED;
    @Getter
    private int greenLightTime;
    @Getter
    private int redLightTime;

    public StopLights() {
        VehicleData vehicleData = new VehicleData();
        vehicleData.setPosition(new Position(null, 600));

        VehicleParams params = new VehicleParams();
        params.setSpeed(0);
        params.setAcceleration(0);
        params.setDesiredSpeed(0);
        vehicleData.setParams(params);
        this.setVehicleData(vehicleData);

        greenLightTime = 3;
        redLightTime = 3;
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
    }

    public void setBroadcastingGreen(boolean broadcastingGreen) {
        this.broadcastingGreen = broadcastingGreen;
    }
}

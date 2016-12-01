package pl.zablocki.core.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class VehicleParams {

    @Setter
    @Getter
    private double acceleration = 0.6;
    @Setter
    @Getter
    private double desiredSpeed = 30;
    @Setter
    @Getter
    private int length = 20;
    @Setter
    @Getter
    private double speed = 15;

    public VehicleParams() {
    }

    public VehicleParams(VehicleParams params) {
        this.acceleration = params.getAcceleration();
        this.desiredSpeed = params.getDesiredSpeed();
        this.length = params.getLength();
        this.speed = params.getSpeed();
    }
}
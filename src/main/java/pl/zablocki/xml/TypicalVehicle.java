package pl.zablocki.xml;

import lombok.Getter;
import lombok.Setter;

class TypicalVehicle {
    @Getter
    @Setter
    private double acceleration = 2.2;
    @Getter
    @Setter
    private double maxAcceleration = 2.2;
    @Getter
    @Setter
    private double speed = 40;
    @Getter
    @Setter
    private double maxSpeed = 50;
    @Getter
    @Setter
    private double position = 0;
    @Getter
    @Setter
    private double length = 20;
    @Getter
    @Setter
    private AccelerationModelType accelerationModelType = AccelerationModelType.IDM;
}

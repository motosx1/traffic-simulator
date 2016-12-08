package pl.zablocki.core.road;

import lombok.Getter;
import lombok.Setter;

public class RoadObject {
    @Getter
    @Setter
    private double acceleration;
    @Getter
    @Setter
    private double maxAcceleration;
    @Getter
    @Setter
    private double speed;
    @Getter
    @Setter
    private double maxSpeed;
    @Getter
    @Setter
    private double position = 0;
    @Getter
    @Setter
    private double length = 20;
    @Getter
    @Setter
    private double breakingRappidness;
}

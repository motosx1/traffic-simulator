package pl.zablocki.core.roadobjects;

import lombok.Getter;
import lombok.Setter;
import pl.zablocki.core.model.AccelerationModel;

public abstract class RoadObject {
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
    @Setter
    private double breakingRapidness;
    @Getter
    @Setter
    private ObjectType objectType;
    @Setter
    @Getter
    private AccelerationModel accelerationModel;

    public double getBreakingRapidness() {
        if( objectType == ObjectType.AUTONOMOUS) {
            return 1;
        }

        return 35;
    }
}

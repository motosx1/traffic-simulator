package pl.zablocki.core.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.zablocki.core.model.AccelerationModel;
import pl.zablocki.core.model.GippsModel;
import pl.zablocki.core.road.RoadObject;

import java.util.List;

@ToString
public class Vehicle extends RoadObject {

    @Getter
    private Integer id;
    @Setter
    private RoadObject objectInFront;
    @Setter
    @Getter
    private AccelerationModel accelerationModel;
    @Getter
    private final VehicleType type;

    public Vehicle(Integer id, RoadObject vehicleParams, Vehicle objectInFront, VehicleType type) {
        this.id = id;
        this.objectInFront = objectInFront;
        this.accelerationModel = new GippsModel();
        setVehicleParams(vehicleParams);
        this.type = type;
    }

    private void setVehicleParams(RoadObject vehicleParams) {
        setAcceleration(vehicleParams.getAcceleration());
        setMaxAcceleration(vehicleParams.getMaxAcceleration());
        setSpeed(vehicleParams.getSpeed());
        setMaxSpeed(vehicleParams.getMaxSpeed());
        setBreakingRappidness(vehicleParams.getBreakingRappidness());
    }

    public void updateParameters(double timeElapsed) {
        double calculatedNewAcc = calcAcc();
        setAcceleration(calculatedNewAcc);
        double speed = getSpeed() + getAcceleration() * timeElapsed;
        speed = validateSpeed(speed);
        setSpeed(speed);
        double newPosition =  getPosition() + (getSpeed() * timeElapsed) + (getAcceleration() * Math.sqrt(timeElapsed) * 0.5);
        setPosition(newPosition);
    }

    private double calcAcc() {
        double s = getDistanceToFrontObject();
        double v = getSpeed();
        double dv = getRelativeSpeed();
        double accLead = getObjectsInFrontAcc();
        double tLocal = 1;
        double v0Local = getMaxSpeed();
        double aLocal = getMaxAcceleration();

        // actual Gipps formula
        return accelerationModel.acc(s, v, dv, accLead, tLocal, v0Local, aLocal, getBreakingRappidness(), getMinimumGap());

    }

    private double validateSpeed(double speed) {
        if (speed < 0) {
            speed = 0;
        } else if (speed > getMaxSpeed()) {
            speed = getMaxSpeed();
        }
        return speed;
    }

    private double getRelativeSpeed() {
        if (objectInFront != null) {
            return getSpeed() - objectInFront.getSpeed();
        }
        return 0;
    }

    private double getObjectsInFrontAcc() {
        return objectInFront == null ? getAcceleration() : objectInFront.getAcceleration();
    }

    private double getDistanceToFrontObject() {
        if (objectInFront != null) {
            return Math.abs(objectInFront.getPosition() - this.getPosition()) - objectInFront.getLength();
        }
        return 10000;
    }

    private double getMinimumGap() {
        return 2;
    }

    public RoadObject getObjectInFront() {
        return objectInFront != null ? objectInFront : null;
    }

    public Vehicle findVehicleInFront(List<Vehicle> vehiclesInTheLine) {
        return vehiclesInTheLine.stream()
                .filter(vehicle -> vehicle.getPosition() > this.getPosition())
                .min((o1, o2) -> (int) (o1.getPosition() - o2.getPosition()))
                .orElse(null);
    }
}
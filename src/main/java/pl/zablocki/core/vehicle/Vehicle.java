package pl.zablocki.core.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.zablocki.core.model.AccelerationModel;
import pl.zablocki.core.model.GippsModel;
import pl.zablocki.core.road.RoadObject;

@ToString
public class Vehicle extends RoadObject {

    @Getter
    private Integer id;
    @Setter
    private Vehicle vehicleInFront;
    @Setter
    @Getter
    private AccelerationModel accelerationModel;

    public Vehicle(Integer id, RoadObject vehicleParams, Vehicle vehicleInFront) {
        this.id = id;
        this.vehicleInFront = vehicleInFront; //reference
        this.accelerationModel = new GippsModel();
        setVehicleParams(vehicleParams);
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
        if (vehicleInFront != null) {
            return getSpeed() - vehicleInFront.getSpeed();
        }
        return 0;
    }

    private double getObjectsInFrontAcc() {
        return vehicleInFront == null ? getAcceleration() : vehicleInFront.getAcceleration();
    }

    private double getDistanceToFrontObject() {
        if (vehicleInFront != null) {
            return Math.abs(vehicleInFront.getPosition() - this.getPosition()) - vehicleInFront.getLength();
        }
        return 10000;
    }

    private double getMinimumGap() {
        return 2;
    }

    public Vehicle getVehicleInFront() {
        return vehicleInFront != null ? vehicleInFront : null;
    }

}
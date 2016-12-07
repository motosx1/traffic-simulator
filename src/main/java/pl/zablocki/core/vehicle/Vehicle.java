package pl.zablocki.core.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.zablocki.core.model.AccelerationModel;
import pl.zablocki.core.model.GippsModel;
import pl.zablocki.core.roadnetwork.Road;

@ToString
public class Vehicle {

    @Getter
    private Integer id;
    @Setter
    private Vehicle vehicleInFront;
    @Setter
    @Getter
    private StopLights stopLights;
    private VehicleData vehicleData;
    private AccelerationModel accelerationModel;

    public Vehicle(Integer id, VehicleData typicalVehicle, Vehicle vehicleInFront) {
        this.id = id;
        this.vehicleData = new VehicleData(typicalVehicle);
        this.vehicleInFront = vehicleInFront; //reference
        this.accelerationModel = new GippsModel();
    }

    public void updateParameters(double timeElapsed) {
        double calculatedNewAcc = calcAcc();
        setAcceleration(calculatedNewAcc);
        double speed = getSpeed() + getAcceleration() * timeElapsed;
        speed = validateSpeed(speed);
        setSpeed(speed);
        setDistance(getDistance() + (getSpeed() * timeElapsed) + (getAcceleration() * Math.sqrt(timeElapsed) * 0.5));
    }

    private double calcAcc() {
        double s = getDistanceToFrontObject();
        double v = getSpeed();
        double dv = getRelativeSpeed();
        double accLead = getObjectsInFrontAcc();
        double tLocal = 1;
        double v0Local = getDesiredSpeed();
        double aLocal = getMaxAcceleration();

        // actual Gipps formula
        return accelerationModel.acc(s, v, dv, accLead, tLocal, v0Local, aLocal, getParams().getBParam(), getMinimumGap());

    }

    private double validateSpeed(double speed) {
        if (speed < 0) {
            speed = 0;
        } else if (speed > getDesiredSpeed()) {
            speed = getDesiredSpeed();
        }
        return speed;
    }

    private double getRelativeSpeed() {
        if (stopLights != null) {
            return getSpeed();
        }
        if (vehicleInFront != null) {
            return getSpeed() - vehicleInFront.getSpeed();
        }
        return 0;
    }

    private double getObjectsInFrontAcc() {
        if (stopLights != null) {
            return 0;
        }
        return vehicleInFront == null ? getAcceleration() : vehicleInFront.getAcceleration();
    }

    private double getDistanceToFrontObject() {
        if (stopLights != null) {
            return stopLights.getDistance() - this.getDistance();
        }
        if (vehicleInFront != null) {
            return Math.abs(vehicleInFront.getDistance() - this.getDistance()) - vehicleInFront.getLength();
        }
        return 10000;
    }

    private double getMinimumGap() {
        return 2;
    }

    public VehicleData getVehicleInFront() {
        return vehicleInFront != null ? vehicleInFront.vehicleData : null;
    }


    Position getPosition() {
        return vehicleData.position;
    }

    void setPosition(Position position) {
        this.vehicleData.position = position;
    }

    VehicleParams getParams() {
        return vehicleData.params;
    }

    void setParams(VehicleParams params) {
        this.vehicleData.params = params;
    }

    void setAcceleration(double acc) {
        this.getParams().setAcceleration(acc);
    }

    void setDistance(double pos) {
        this.getPosition().setDistance(pos);
    }

    public double getSpeed() {
        return this.getParams().getSpeed();
    }

    public double getLength() {
        return this.getParams().getLength();
    }

    public double getDesiredSpeed() {
        return this.getParams().getDesiredSpeed();
    }

    public void setSpeed(double speed) {
        this.getParams().setSpeed(speed);
    }

    public double getMaxAcceleration() {
        return this.getParams().getMaxAcceleration();
    }

    public double getAcceleration() {
        return this.getParams().getAcceleration();
    }

    public double getDistance() {
        return this.getPosition().getDistance();
    }

    public Road getRoad() {
        return this.getPosition().getCurrentRoad();
    }
}
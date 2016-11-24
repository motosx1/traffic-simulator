package pl.zablocki.core.vehicle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

abstract class Raoadable {
    @Getter @Setter(AccessLevel.PACKAGE)
    private VehicleData vehicleData;

    Raoadable(){}

    Raoadable(VehicleData vehicleData) {
        this.vehicleData = vehicleData;
    }

    void setAcceleration(double acc) {
        this.getVehicleData().getParams().setAcceleration(acc);
    }

    void setDistance(double pos) {
        this.getVehicleData().getPosition().setDistance(pos);
    }

    public double getSpeed() {
        return this.getVehicleData().getParams().getSpeed();
    }

    public double getLength() {
        return this.getVehicleData().getParams().getLength();
    }

    public double getDesiredSpeed() {
        return this.getVehicleData().getParams().getDesiredSpeed();
    }

    public void setSpeed(double speed) {
        this.getVehicleData().getParams().setSpeed(speed);
    }

    public double getAcceleration() {
        return this.getVehicleData().getParams().getAcceleration();
    }

    public double getDistance() {
        return this.getVehicleData().getPosition().getDistance();
    }
}

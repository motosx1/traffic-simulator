package pl.zablocki.core.vehicle;

import lombok.ToString;

@ToString
public class Vehicle {

    private Vehicle vehicleInFront;
    private VehicleData vehicleData;

    public Vehicle(VehicleData typicalVehicle, Vehicle vehicleInFront) {
        this.vehicleData = new VehicleData(typicalVehicle);;
        this.vehicleInFront = vehicleInFront; //reference
    }

    public void updateParameters(double timeElapsed) {
        double calculatedNewAcc = calcAcc();
        setAcceleration(calculatedNewAcc > 0 ? calculatedNewAcc : 0);
        setSpeed(getSpeed() + getAcceleration() * timeElapsed);
        setDistance(getDistance() + (getSpeed() * timeElapsed) + (getAcceleration() * Math.sqrt(timeElapsed) * 0.5));
    }


    private double calcAcc() {

        double s = getDistanceToFrontVehicle();
        double v = getSpeed();
        double dv = getRelativeSpeed();
        double aLead = vehicleInFront == null ? getAcceleration() : vehicleInFront.getAcceleration();

        double tLocal = 1;
        double v0Local = getDesiredSpeed();
        double aLocal = 2.2;

        // actual Gipps formula
        return acc(s, v, dv, aLead, tLocal, v0Local, aLocal);

    }

    private double acc(double s, double v, double dv, double aLead, double TLocal, double v0Local, double aLocal) {
        // treat special case of v0=0 (standing obstacle)
        if (v0Local == 0) {
            return 0;
        }

        double s1 = 0;
        double delta = 4;
        double b = 2;
        double coolness = 1;


        final double sstar = getMinimumGap() + Math.max(TLocal * v + s1 * Math.sqrt((v + 0.00001) / v0Local) + 0.5 * v * dv / Math.sqrt(aLocal * b), 0.);
        final double z = sstar / Math.max(s, 0.01);
        final double accEmpty = (v <= v0Local) ? aLocal * (1 - Math.pow((v / v0Local), delta)) : -b * (1 - Math.pow((v0Local / v), aLocal * delta / b));
        final double accPos = accEmpty * (1. - Math.pow(z, Math.min(2 * aLocal / accEmpty, 100.)));
        final double accInt = aLocal * (1 - z * z);

        final double accIIDM = (v < v0Local) ? (z < 1) ? accPos : accInt : (z < 1) ? accEmpty : accInt + accEmpty;

        // constant-acceleration heuristic (CAH)

        final double aLeadRestricted = Math.min(aLead, aLocal);
        final double dvp = Math.max(dv, 0.0);
        final double vLead = v - dvp;
        final double denomCAH = vLead * vLead - 2 * s * aLeadRestricted;

        final double accCAH = ((vLead * dvp < -2 * s * aLeadRestricted) && (denomCAH != 0)) ?
                v * v * aLeadRestricted / denomCAH :
                aLeadRestricted - 0.5 * dvp * dvp / Math.max(s, 0.0001);
        //accACC_IIDM
        return (accIIDM > accCAH) ? accIIDM : (1 - coolness) * accIIDM + coolness * (accCAH + b * Math.tanh((accIIDM - accCAH) / b));
    }

    private double getRelativeSpeed() {
        if (vehicleInFront != null) {
            return getSpeed() - vehicleInFront.getSpeed();
        }
        return 0;
    }

    private double getDistanceToFrontVehicle() {
        if (vehicleInFront != null) {
            return Math.abs(vehicleInFront.getDistance() - this.getDistance()) - vehicleInFront.getLength();
        }
        return 10000;
    }

    private double getMinimumGap() {
        return 2;
    }


    private VehicleData getVehicleData() {
        return vehicleData;
    }


    private void setAcceleration(double acc) {
        this.vehicleData.getParams().setAcceleration(acc);
    }

    private void setDistance(double pos) {
        this.vehicleData.getPosition().setDistance(pos);
    }

    private double getSpeed() {
        return this.getVehicleData().getParams().getSpeed();
    }

    private double getLength() {
        return this.getVehicleData().getParams().getLength();
    }

    private double getDesiredSpeed() {
        return this.getVehicleData().getParams().getDesiredSpeed();
    }

    private void setSpeed(double speed) {
        this.getVehicleData().getParams().setSpeed(speed);
    }

    private double getAcceleration() {
        return this.getVehicleData().getParams().getAcceleration();
    }

    public double getDistance() {
        return this.getVehicleData().getPosition().getDistance();
    }


}
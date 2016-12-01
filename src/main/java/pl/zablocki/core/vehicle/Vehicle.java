package pl.zablocki.core.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    public Vehicle(Integer id, VehicleData typicalVehicle, Vehicle vehicleInFront) {
        this.id = id;
        this.vehicleData = new VehicleData(typicalVehicle);
        this.vehicleInFront = vehicleInFront; //reference
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
        double aLocal = 5.2;

        // actual Gipps formula
        return acc(s, v, dv, accLead, tLocal, v0Local, aLocal);

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

    public double getAcceleration() {
        return this.getParams().getAcceleration();
    }

    public double getDistance() {
        return this.getPosition().getDistance();
    }
}
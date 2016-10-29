package pl.zablocki.core;

import lombok.Data;

@Data
public class Vehicle {
    Vehicle vehicleBefore;
    double position = 0;

    private double speed = 1;
    private double acc = 2.2;
    double l = 10;
    private double desiredSpeed = 100;

    public Vehicle(int position) {
        this.position = position;
    }

    public void getNewPosition(double timeElapsed) {
        double calculatedNewAcc = calcAcc();
        this.acc = calculatedNewAcc > 0 ? calculatedNewAcc : 0;
        this.speed = this.speed + acc * timeElapsed;
        this.position = this.position + (speed * timeElapsed) + (acc * Math.sqrt(timeElapsed) * 0.5);
    }


    public double getDistanceToFrontVehicle() {
        if (vehicleBefore != null) {
            return Math.abs(vehicleBefore.getPosition() - this.position) - vehicleBefore.getL();
        }
        return 10000;
    }


    private double getRelativeSpeed() {
        if (vehicleBefore != null) {
            return speed - vehicleBefore.getSpeed();
        }
        return 0;
    }

    private double calcAcc() {

        double s = getDistanceToFrontVehicle();
        double v = speed;
        double dv = getRelativeSpeed();
        double aLead = vehicleBefore == null ? acc : vehicleBefore.getAcc();

        double tLocal = 1;
        double v0Local = desiredSpeed;
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

    private double getMinimumGap() {
        return 2;
    }

}

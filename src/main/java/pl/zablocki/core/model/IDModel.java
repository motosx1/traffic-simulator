package pl.zablocki.core.model;

public class IDModel implements AccelerationModel {
    @Override
    public double acc(double s, double v, double dv, double aLead, double v0Local, double aLocal, double bParam2, double minimumGap) {
        // treat special case of v0=0 (standing obstacle)
        if (v0Local == 0) {
            return 0;
        }

        double s1 = 0;
        double delta = 4;
        double bParam = bParam2;  // im wieksze tym ostrzejsze hamowanie :)
        double coolness = 1;


        final double sstar = minimumGap + Math.max(v + s1 * Math.sqrt((v + 0.00001) / v0Local) + 0.5 * v * dv / Math.sqrt(aLocal * bParam), 0.);
        final double z = sstar / Math.max(s, 0.01);
        final double accEmpty;

        if (v <= v0Local) {
            accEmpty = aLocal * (1 - Math.pow((v / v0Local), delta));
        } else {
            accEmpty = -bParam * (1 - Math.pow((v0Local / v), aLocal * delta / bParam));
        }

        final double accPos = accEmpty * (1. - Math.pow(z, Math.min(2 * aLocal / accEmpty, 100.)));
        final double accInt = aLocal * (1 - z * z);

        final double accIIDM = (v < v0Local) ? (z < 1) ? accPos : accInt : (z < 1) ? accEmpty : accInt + accEmpty;

        // constant-acceleration heuristic (CAH)

        final double aLeadRestricted = Math.min(aLead, aLocal);
        final double dvp = Math.max(dv, 0.0);
        final double vLead = v - dvp;
        final double denomCAH = vLead * vLead - 2 * s * aLeadRestricted;

        final double accCAH;
        if ((vLead * dvp < -2 * s * aLeadRestricted) && (denomCAH != 0)) {
            accCAH = v * v * aLeadRestricted / denomCAH;
        } else {
            accCAH = aLeadRestricted - 0.5 * dvp * dvp / Math.max(s, 0.0001);
        }
        //accACC_IIDM
        if (accIIDM > accCAH) {
            return accIIDM;
        } else {
            return (1 - coolness) * accIIDM + coolness * (accCAH + bParam * Math.tanh((accIIDM - accCAH) / bParam));
        }
    }
}

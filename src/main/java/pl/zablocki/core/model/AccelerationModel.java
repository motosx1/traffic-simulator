package pl.zablocki.core.model;

public interface AccelerationModel {
    public double acc(double s, double v, double dv, double aLead, double TLocal, double v0Local, double aLocal, double bParam, double minimumGap);
}

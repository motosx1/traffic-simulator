package pl.zablocki.core.model;

public interface AccelerationModel {
    double acc(double distanceToObject, double v, double dv, double accLeadObject, double vMax, double acc, double bParam, double minimumGap);
}

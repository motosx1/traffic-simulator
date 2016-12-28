package pl.zablocki.filewriter;

import lombok.Data;

@Data
class VehicleFileParams {
    double vehPos;
    double vehSpeed;
    double vehAcc;

    @Override
    public String toString() {
        return vehPos + ";" +
                vehSpeed + ";" +
                vehAcc + ";";
    }
}

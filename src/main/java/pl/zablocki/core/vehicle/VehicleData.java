package pl.zablocki.core.vehicle;

import lombok.Getter;
import lombok.ToString;

@ToString
public class VehicleData {

    @Getter
    Position position = new Position();
    @Getter
    VehicleParams params = new VehicleParams();

    public VehicleData() {
    }

    public VehicleData(VehicleData typicalVehicle) {
        this.position = new Position(typicalVehicle.position);
        this.params = new VehicleParams(typicalVehicle.params);
    }


    public VehicleData(Position position) {
        this.position = position;
    }
}
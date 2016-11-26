package pl.zablocki.core.vehicle;

import lombok.ToString;

@ToString
public class VehicleData {

	Position position = new Position();
	VehicleParams params = new VehicleParams();

	public VehicleData() {
	}

	public VehicleData(VehicleData typicalVehicle) {
		this.position = new Position(typicalVehicle.position);
		this.params = new VehicleParams(typicalVehicle.params);
	}


}
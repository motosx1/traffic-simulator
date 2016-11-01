package pl.zablocki.core.vehicle;

import lombok.ToString;

@ToString
public class VehicleData {

	Position position = new Position();
	VehicleParams params = new VehicleParams();

	public VehicleData() {
	}

	public VehicleData(VehicleData typicalVehicle) {
		this.position = new Position(typicalVehicle.getPosition());
		this.params = new VehicleParams(typicalVehicle.getParams());
	}

	Position getPosition() {
		return position;
	}

	void setPosition(Position position) {
		this.position = position;
	}

	VehicleParams getParams() {
		return params;
	}

	void setParams(VehicleParams params) {
		this.params = params;
	}
}
package pl.zablocki.core.vehicle;

import lombok.Getter;
import lombok.ToString;
import pl.zablocki.core.roadnetwork.Position;

@ToString
public class VehicleData {

	@Getter
	private Position position = new Position();
	@Getter
	private VehicleParams params = new VehicleParams();

}
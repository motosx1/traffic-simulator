package pl.zablocki.core.simulation;


import lombok.AllArgsConstructor;
import lombok.ToString;
import pl.zablocki.core.vehicle.VehicleData;

@AllArgsConstructor
@ToString
public class Scenario {

	private final double carsPerHour;
	private final double simulationDuration;
	private final VehicleData typicalVehicle;


}
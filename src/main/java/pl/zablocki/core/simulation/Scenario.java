package pl.zablocki.core.simulation;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.VehicleData;

@AllArgsConstructor
@ToString
public class Scenario {

    @Getter
    private final double carsPerHour;
    @Getter
    private final double simulationDuration;
    @Getter
    private final VehicleData typicalVehicle;
    @Getter
    private final StopLights stopLights;

}
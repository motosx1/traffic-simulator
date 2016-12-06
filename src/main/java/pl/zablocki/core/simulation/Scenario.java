package pl.zablocki.core.simulation;


import lombok.Getter;
import lombok.ToString;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.VehicleData;

@ToString
public class Scenario {
    public static int idCounter = 0;
    @Getter
    private final int id;
    @Getter
    private final double carsPerHour;
    @Getter
    private final double simulationDuration;
    @Getter
    private final VehicleData typicalVehicle;
    @Getter
    private final StopLights stopLights;

    public Scenario(double carsPerHour, double simulationDuration, VehicleData typicalVehicle, StopLights stopLights) {
        this.id = idCounter++;
        this.carsPerHour = carsPerHour;
        this.simulationDuration = simulationDuration;
        this.typicalVehicle = typicalVehicle;
        this.stopLights = stopLights;
    }
}
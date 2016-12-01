package pl.zablocki.core.vehicle;

import pl.zablocki.core.simulation.Scenario;

import java.util.List;

public class VehicleFactory {

    private static int counter = 0;

    public static Vehicle createNewVehicle(Scenario scenario, List<Vehicle> activeVehicles) {
        Vehicle firstInList = findFirstVehicle(activeVehicles);
        return new Vehicle(VehicleFactory.counter++, scenario.getTypicalVehicle(), firstInList);
    }

    public static Vehicle createStopLight(Scenario scenario, List<Vehicle> activeVehicles) {
        Vehicle firstInList = findFirstVehicle(activeVehicles);
        return new Vehicle(VehicleFactory.counter++, scenario.getTypicalVehicle(), firstInList);
    }

    private static Vehicle findFirstVehicle(List<Vehicle> activeVehicles) {
        if (activeVehicles.isEmpty()) {
            return null;
        }

        Vehicle theClosestVehicle = activeVehicles.get(0);
        double theClosestVehicleDistance = theClosestVehicle.getDistance();

        for (Vehicle vehicle : activeVehicles) {
            double vehicleDistance = vehicle.getDistance();
            if (theClosestVehicleDistance > vehicleDistance) {
                theClosestVehicle = vehicle;
            }
        }

        return theClosestVehicle;
    }

}
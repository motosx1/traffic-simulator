package pl.zablocki.core.vehicle;

import pl.zablocki.core.simulation.Scenario;

import java.util.List;

public class VehicleFactory {

    private static int counter = 0;

    public static Vehicle createNewVehicle(Scenario scenario, List<Vehicle> activeVehicles) {
        Vehicle firstInList = findFirstVehicle(activeVehicles, scenario.getTypicalVehicle().getPosition().getCurrentRoad().getId());
        return new Vehicle(VehicleFactory.counter++, scenario.getTypicalVehicle(), firstInList);
    }

    public static Vehicle createStopLight(Scenario scenario, List<Vehicle> activeVehicles) {
        Vehicle firstInList = findFirstVehicle(activeVehicles, scenario.getTypicalVehicle().getPosition().getCurrentRoad().getId());
        return new Vehicle(VehicleFactory.counter++, scenario.getTypicalVehicle(), firstInList);
    }

    private static Vehicle findFirstVehicle(List<Vehicle> activeVehicles, int roadId) {
        if (activeVehicles.isEmpty()) {
            return null;
        }

        Vehicle theClosestVehicle = null;//activeVehicles.get(0);
        double theClosestVehicleDistance = 10000000;//theClosestVehicle.getDistance();

        for (Vehicle vehicle : activeVehicles) {
            if( vehicle.getRoad().getId() == roadId ) {
                double vehicleDistance = vehicle.getDistance();
                if (theClosestVehicleDistance > vehicleDistance) {
                    theClosestVehicle = vehicle;
                }
            }
        }

        return theClosestVehicle;
    }

}
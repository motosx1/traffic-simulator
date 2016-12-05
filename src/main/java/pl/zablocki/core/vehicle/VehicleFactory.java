package pl.zablocki.core.vehicle;

import pl.zablocki.core.simulation.Scenario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleFactory {

    private static int counter = 0;
    private static Map<Integer, Integer> mapCounter = new HashMap<>();

    public static Vehicle createNewVehicle(Scenario scenario, List<Vehicle> activeVehicles) {
        int id = getNextId(scenario);
        Vehicle firstInList = findFirstVehicle(activeVehicles, scenario.getTypicalVehicle().getPosition().getCurrentRoad().getId());
        return new Vehicle(id, scenario.getTypicalVehicle(), firstInList);
    }

    private static int getNextId(Scenario scenario) {
        int scenarioId = scenario.getId();
        if( mapCounter.get(scenarioId) == null ){
            mapCounter.put(scenarioId, 0);
            return 0;
        } else {
            Integer vehicleId = mapCounter.get(scenarioId);
            mapCounter.put(scenarioId, ++vehicleId);
            return vehicleId;
        }
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
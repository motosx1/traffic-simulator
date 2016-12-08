package pl.zablocki.core.vehicle;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.RoadObject;
import pl.zablocki.core.simulation.Scenario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleFactory {

    private static int counter = 0;
    private static Map<Integer, Integer> mapCounter = new HashMap<>();


    public static Vehicle createNewVehicle(int id, Line line, RoadObject roadObject) {
//        int id = getNextId(scenario);
        Vehicle firstInList = findFirstVehicle(line.getVehicles());
        return new Vehicle(id, roadObject, firstInList, roadObject.getVehicleType() == null ? VehicleType.NORMAL : VehicleType.AUTONOMUS);
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

    private static Vehicle findFirstVehicle(List<Vehicle> vehiclesInLine) {
        if (vehiclesInLine.isEmpty()) {
            return null;
        }

        Vehicle theClosestVehicle = null;
        double theClosestVehicleDistance = 10000000;

        for (Vehicle vehicle : vehiclesInLine) {
                double vehicleDistance = vehicle.getPosition();
                if (theClosestVehicleDistance > vehicleDistance) {
                    theClosestVehicle = vehicle;
                }
        }

        return theClosestVehicle;
    }

}
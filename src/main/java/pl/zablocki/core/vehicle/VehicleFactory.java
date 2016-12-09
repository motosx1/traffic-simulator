package pl.zablocki.core.vehicle;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.RoadObject;

import java.util.List;

public class VehicleFactory {

    public static Vehicle createNewVehicle(int id, Line line, RoadObject roadObject) {
        Vehicle firstInList = findFirstVehicle(line.getVehicles());
        return new Vehicle(id, roadObject, firstInList);
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
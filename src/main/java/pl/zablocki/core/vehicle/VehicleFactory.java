package pl.zablocki.core.vehicle;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.road.RoadObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleFactory {
    private static Map<Road, Integer> carsOnRoadAmount = new HashMap<>();
    private static Map<Road, Integer> autonomousOnRoadAmount = new HashMap<>();

    public static Vehicle createNewVehicle(int id, Line line, RoadObject roadObject, Road road) {
        ObjectType carType = decideObjectTypeToCreate(road);
        roadObject.setObjectType(carType);
        Vehicle firstInList = findFirstVehicle(line.getVehicles());
        return new Vehicle(id, roadObject, firstInList);
    }

    private static ObjectType decideObjectTypeToCreate(Road road) {
        initHashMaps(road);
        Integer carsAmount = carsOnRoadAmount.get(road);
        Integer autonomousCarsAmount = autonomousOnRoadAmount.get(road);
        if( carsAmount == 0 ){
            carsOnRoadAmount.put(road, 1);
            return ObjectType.NORMAL;
        }

        double autonomousPercentage = autonomousCarsAmount/(double)carsAmount;
        if( autonomousPercentage < road.getAutonomousPercentage()/100 ){
            carsOnRoadAmount.put(road, carsAmount+1);
            autonomousOnRoadAmount.put(road, autonomousCarsAmount+1);
            return ObjectType.AUTONOMOUS;
        }
        carsOnRoadAmount.put(road, carsAmount+1);
        return ObjectType.NORMAL;
    }

    private static void initHashMaps(Road road) {
        Integer carsAmount = carsOnRoadAmount.get(road);
        Integer autonomousCarsAmount = autonomousOnRoadAmount.get(road);
        if(carsAmount == null ){
            carsOnRoadAmount.put(road,0);
        }
        if(autonomousCarsAmount == null ){
            autonomousOnRoadAmount.put(road,0);
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
package pl.zablocki.core.simulation;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Simulation {

    private Scenarios scenarios;

    public Simulation(Scenarios scenarios) {
        this.scenarios = scenarios;
    }

    private void deleteNotActiveVehicles() {

    }

    RoadData doStep(double dt, double elapsedTime) {
        for (Scenario scenario : scenarios.getScenarios()) {
            Road road = scenario.getRoad();

            for (Line line : road.getLines()) {
                List<Vehicle> vehiclesInTheLine = line.getVehicles();

                for (Vehicle vehicle : vehiclesInTheLine) {
                    vehicle.updateParameters(dt);
                }

                Vehicle newVehicle = createNewVehicle(dt, elapsedTime, scenario, line);
                if (newVehicle != null) {
                    line.getVehicles().add(newVehicle);
                }
            }

        }

        RoadData roadData = new RoadData();
        List<Road> roads = scenarios.getScenarios().stream().map(Scenario::getRoad).collect(Collectors.toList());
        roadData.getRoads().addAll(roads);
        return roadData;
    }

    private Vehicle createNewVehicle(double dt, double elapsedTime, Scenario scenario, Line line) {
        if (isTimeTo(scenario.getCarsPerHour(), dt, elapsedTime)) {
            if (isPossibleToCreateNewVehicles(line.getVehicles())) {
                return VehicleFactory.createNewVehicle(line.vehicleCounter++, line, scenario.getTypicalVehicle());
            }
        }
        return null;
    }

    private boolean isPossibleToCreateNewVehicles(List<Vehicle> vehiclesInTheLine) {
        List<Vehicle> vehiclesAtStart = vehiclesInTheLine.stream()
                .filter(v -> (v.getPosition() >= 0) && (v.getPosition() < v.getLength() + 10))
                .collect(Collectors.toList());
        return vehiclesAtStart.size() == 0;
    }

//    private void getNewVehicle(double dt, double elapsedTime, Scenario scenario) {
//        return VehicleFactory.createNewVehicle(scenario, activeVehicles.get(scenario.getId()));
//        activeVehicles.get(scenario.getId()).add(newVehicle);
//    }

//    RoadData doStep(double dt, double elapsedTime) {
//        List<StopLight> stopLightList = new ArrayList<>();
//        for (Scenario scenario : scenarios.getScenarios()) {
//
//            StopLight stopLight = scenario.getStopLights();
//
//            StopLightsEngine.changeLight(stopLight, dt, elapsedTime);
//
//            activeVehicles.get(scenario.getId()).forEach(vehicle -> {
//                vehicle.updateParameters(dt);
//                assignStopLightToVehicle(dt, stopLight, vehicle);
//            });
//
//
//            getNewVehicle(dt, elapsedTime, scenario);
//            stopLightList.add(stopLight);
//            deleteNotActiveVehicles();
//        }
//
//        List<Vehicle> vehicles = new ArrayList<>();
//        activeVehicles.entrySet().forEach(entry -> entry.getValue().forEach(vehicles::add));
//        roadData.setVehicles(vehicles);
//        roadData.setStopLights(stopLightList);
//        return roadData;
//    }

//    private void assignStopLightToVehicle(double dt, StopLight stopLight, Vehicle vehicle) {
//        if (stopLight.isBroadcastingRed() && StopLightsEngine.isVehicleInRange(vehicle, stopLight)) {
//            vehicle.setStopLights(stopLight);
//            stopLight.setBroadcastingRed(false);
//        }
//
//        if (stopLight.isBroadcastingGreen()) {
//            if (vehicle.getStopLight() != null && vehicle.getStopLight().equals(stopLight)) {
//                vehicle.setStopLights(null);
//                vehicle.updateParameters(dt);
//            }
//        }
//    }


    private boolean isTimeTo(double frequencyPerHour, double dt, double elapsedTime) {
        return elapsedTime % (3600 / frequencyPerHour) < dt;
    }


}
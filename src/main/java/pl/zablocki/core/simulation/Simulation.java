package pl.zablocki.core.simulation;

import pl.zablocki.core.roadnetwork.Road;
import pl.zablocki.core.roadnetwork.StopLightsEngine;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Simulation {

    private RoadObjects roadObjects = new RoadObjects();
//    private List<Vehicle> activeVehicles = new ArrayList<>();
    private Map<Integer, List<Vehicle>> activeVehicles = new HashMap<>();
    private Scenarios scenarios;

    public Simulation(Scenarios scenarios) {
        this.scenarios = scenarios;
        scenarios.getScenarios().forEach(scenario -> {
            activeVehicles.put(scenario.getId(), new ArrayList<>());
        });
    }

    private void createNewVehicles(Scenario scenario) {
        Vehicle newVehicle = VehicleFactory.createNewVehicle(scenario, activeVehicles.get(scenario.getId()));
        activeVehicles.get(scenario.getId()).add(newVehicle);
    }

    private void deleteNotActiveVehicles() {

    }

    RoadObjects doStep(double dt, double elapsedTime) {
        List<StopLights> stopLightsList = new ArrayList<>();
        for (Scenario scenario : scenarios.getScenarios()) {

            StopLights stopLight = scenario.getStopLights();

            StopLightsEngine.changeLight(stopLight, dt, elapsedTime);

            activeVehicles.get(scenario.getId()).forEach(vehicle -> {
                vehicle.updateParameters(dt);
                assignStopLightToVehicle(dt, stopLight, vehicle);
            });


            createAndAddnewVehicles(dt, elapsedTime, scenario);
            stopLightsList.add(stopLight);
            deleteNotActiveVehicles();
        }

        List<Vehicle> vehicles = new ArrayList<>();
        activeVehicles.entrySet().forEach(entry -> entry.getValue().forEach(vehicles::add));
        roadObjects.setVehicles(vehicles);
        roadObjects.setStopLights(stopLightsList);
        return roadObjects;
    }

    private void assignStopLightToVehicle(double dt, StopLights stopLight, Vehicle vehicle) {
        if (stopLight.isBroadcastingRed() && StopLightsEngine.isVehicleInRange(vehicle, stopLight)) {
            vehicle.setStopLights(stopLight);
            stopLight.setBroadcastingRed(false);
        }

        if (stopLight.isBroadcastingGreen()) {
            if (vehicle.getStopLights() != null && vehicle.getStopLights().equals(stopLight)) {
                vehicle.setStopLights(null);
                vehicle.updateParameters(dt);
            }
        }
    }

    private void createAndAddnewVehicles(double dt, double elapsedTime, Scenario scenario) {
        if (isTimeTo(scenario.getCarsPerHour(), dt, elapsedTime)) {
            if (isPossibleToCreateNewVehicles(scenario.getTypicalVehicle().getPosition().getCurrentRoad(), scenario.getId())) {
                createNewVehicles(scenario);
            }
        }
    }

    private boolean isPossibleToCreateNewVehicles(Road currentRoad, int scenarioId) {
        List<Vehicle> vehiclesAtStart = activeVehicles.get(scenarioId).stream()
                .filter(v -> (v.getRoad().getId() == currentRoad.getId()) && (v.getDistance() >= 0) && (v.getDistance() < v.getLength() + 10))
                .collect(Collectors.toList());
        return vehiclesAtStart.size() == 0;
    }

    private boolean isTimeTo(double frequencyPerHour, double dt, double elapsedTime) {
        return elapsedTime % (3600 / frequencyPerHour) < dt;
    }


}
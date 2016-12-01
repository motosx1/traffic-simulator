package pl.zablocki.core.simulation;

import pl.zablocki.core.roadnetwork.StopLightsEngine;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Simulation {

    private RoadObjects roadObjects = new RoadObjects();
    private List<Vehicle> activeVehicles = new ArrayList<>();
    private Scenario scenario;

    public Simulation(Scenario scenario) {
        this.scenario = scenario;
    }

    private void createNewVehicles() {
        Vehicle newVehicle = VehicleFactory.createNewVehicle(scenario, activeVehicles);
        activeVehicles.add(newVehicle);
    }

    private void deleteNotActiveVehicles() {

    }

    RoadObjects doStep(double dt, double elapsedTime) {
        StopLights stopLight = scenario.getStopLights();

        StopLightsEngine.changeLight(stopLight, dt, elapsedTime);

        activeVehicles.forEach(vehicle -> {
            vehicle.updateParameters(dt);

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
        });


        if (isTimeTo(scenario.getCarsPerHour(), dt, elapsedTime)) {
            if (isPossibleToCreateNewVehicles()) {
                createNewVehicles();
            }
        }

        deleteNotActiveVehicles();

        roadObjects.setVehicles(activeVehicles);
        roadObjects.setStopLights(scenario.getStopLights());
        return roadObjects;
    }

    private boolean isPossibleToCreateNewVehicles() {
        List<Vehicle> vehiclesAtStart = activeVehicles.stream()
                .filter(v -> v.getDistance() < v.getLength())
                .collect(Collectors.toList());
        return vehiclesAtStart.size() == 0;
    }

    private boolean isTimeTo(double frequencyPerHour, double dt, double elapsedTime) {
        return elapsedTime % (3600 / frequencyPerHour) < dt;
    }


}
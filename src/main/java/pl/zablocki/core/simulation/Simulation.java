package pl.zablocki.core.simulation;

import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleFactory;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

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

    List<Vehicle> doStep(double dt, double elapsedTime) {
        activeVehicles.forEach(vehicle -> {
            vehicle.updateParameters(dt);

            StopLights stopLights = scenario.getStopLights();
            if (stopLights.isBroadcastingRed() && (stopLights.getDistance() - stopLights.getNotifyRadius() - vehicle.getDistance() < vehicle.getSpeed())) {
                stopLights.setBroadcastingRed(false);
                System.out.println("samochód o id " + vehicle.getId() + " w zasiegu swiatel (distance: " + vehicle.getDistance() + ")");
                vehicle.setVehicleInFront(stopLights);
            }
        });

        changeLights(dt, elapsedTime);

        // TODO co jeśli powinny się stworzyć 2 samochody w jednym momencie?
        if (elapsedTime % (3600 / scenario.getCarsPerHour()) < dt) {
            createNewVehicles();
        }

        deleteNotActiveVehicles();

        return activeVehicles;
    }

    private void changeLights(double dt, double elapsedTime) {
        StopLights stopLights = scenario.getStopLights();

        if( stopLights.isRed() && (elapsedTime % stopLights.getRedLightTime() < dt) ){
            stopLights.setGreen();
        }
        if( stopLights.isGreen() && (elapsedTime % stopLights.getGreenLightTime() < dt) ){
            stopLights.setRed();
        }
    }


}
package pl.zablocki.core.simulation;

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
        });

        // TODO co jeśli powinny się stworzyć 2 samochody w danym momencie?
        if (elapsedTime % (3600 / scenario.getCarsPerHour()) < dt) {
            createNewVehicles();
        }

        deleteNotActiveVehicles();

        return activeVehicles;
    }


}
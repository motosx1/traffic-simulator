package pl.zablocki.core.simulation;

import pl.zablocki.core.model.LineChangeModel;
import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.road.RoadObject;
import pl.zablocki.core.vehicle.StopLight;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleFactory;

import java.util.List;
import java.util.stream.Collectors;

class Simulation {

    private Scenarios scenarios;

    Simulation(Scenarios scenarios) {
        this.scenarios = scenarios;
    }

    private void deleteNotActiveVehicles(Line line) {
        List<Vehicle> notActiveVehicles = line.getVehicles().stream().filter(vehicle -> vehicle.getPosition() > 1800).collect(Collectors.toList());
        line.getVehicles().removeAll(notActiveVehicles);
    }

    RoadData doStep(double dt, double elapsedTime) {
        for (Scenario scenario : scenarios.getScenarios()) {
            Road road = scenario.getRoad();

            for (Line line : road.getLines()) {
                List<Vehicle> vehiclesInTheLine = line.getVehicles();
                StopLight stopLight = line.getStopLight();

                changeStopLight(dt, elapsedTime, vehiclesInTheLine, stopLight);
                deleteNotActiveVehicles(line);
                decideToChangeLine(elapsedTime, road);
                updateVehiclesParameters(dt, vehiclesInTheLine);
                createAndAddToLineNewVehicle(dt, elapsedTime, scenario, line, road);
            }

        }

        RoadData roadData = new RoadData();
        List<Road> roads = scenarios.getScenarios().stream().map(Scenario::getRoad).collect(Collectors.toList());
        roadData.getRoads().addAll(roads);
        return roadData;
    }

    private void decideToChangeLine(double elapsedTime, Road road) {
        LineChangeModel.decideToChangeLine(elapsedTime, road);
    }

    private void updateVehiclesParameters(double dt, List<Vehicle> vehiclesInTheLine) {
        for (Vehicle vehicle : vehiclesInTheLine) {
            vehicle.updateParameters(dt);
        }
    }

    private void changeStopLight(double dt, double elapsedTime, List<Vehicle> vehiclesInTheLine, StopLight stopLight) {
        if (stopLight != null) {
            stopLight.changeLight(dt, elapsedTime);
            checkBroadcastingRed(vehiclesInTheLine, stopLight);
            checkBroadcastingGreen(vehiclesInTheLine, stopLight);
        }
    }

    private void checkBroadcastingGreen(List<Vehicle> vehiclesInTheLine, StopLight stopLight) {
        if (stopLight.isBroadcastingGreen()) {
            assignGreen(vehiclesInTheLine, stopLight);
        }
    }

    private void assignGreen(List<Vehicle> vehiclesInTheLine, StopLight stopLight) {
        boolean found = false;
        for (Vehicle vehicle : vehiclesInTheLine) {
            if (!found) {
                RoadObject objectInFront = vehicle.getObjectInFront();
                if (objectInFront != null) {
                    if (objectInFront.equals(stopLight)) {
                        vehicle.setObjectInFront(vehicle.findVehicleInFront(vehiclesInTheLine));
                        found = true;
                    }
                }
            }
        }
    }

    private void checkBroadcastingRed(List<Vehicle> vehiclesInTheLine, StopLight stopLight) {
        for (Vehicle vehicle : vehiclesInTheLine) {
            if (stopLight.isBroadcastingRed()) {
                assignRed(stopLight, vehicle);
            }
        }
    }

    private void assignRed(StopLight stopLight, Vehicle vehicle) {
        if (stopLight.isVehicleInRange(vehicle)) {
            vehicle.setObjectInFront(stopLight);
            stopLight.setBroadcastingRed(false);
        }
    }

    private boolean forceCreate = false;
    private void createAndAddToLineNewVehicle(double dt, double elapsedTime, Scenario scenario, Line line, Road road) {
        Vehicle newVehicle = null;
        if (forceCreate || isTimeTo(line.getCarsPerHour(), dt, elapsedTime)) {
            forceCreate = true;
            if (isPossibleToCreateNewVehicles(line.getVehicles())) {
                newVehicle = VehicleFactory.createNewVehicle(road.vehicleCounter++, line, scenario.getTypicalVehicle(), road);
                forceCreate = false;
            }
        }
        if (newVehicle != null) {
            line.getVehicles().add(newVehicle);
        }
    }

    private boolean isPossibleToCreateNewVehicles(List<Vehicle> vehiclesInTheLine) {
        List<Vehicle> vehiclesAtStart = vehiclesInTheLine.stream()
                .filter(v -> (v.getPosition() >= 0) && (v.getPosition() < v.getLength() + 10))
                .collect(Collectors.toList());
        return vehiclesAtStart.size() == 0;
    }

    private boolean isTimeTo(double frequencyPerHour, double dt, double elapsedTime) {
        return elapsedTime % (3600 / frequencyPerHour) < dt;
    }


}
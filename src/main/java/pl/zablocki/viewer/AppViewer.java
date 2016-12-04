package pl.zablocki.viewer;

import pl.zablocki.core.roadnetwork.Road;
import pl.zablocki.core.simulation.Scenario;
import pl.zablocki.core.simulation.Scenarios;
import pl.zablocki.core.simulation.SimulationRunnable;
import pl.zablocki.core.vehicle.Position;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.VehicleData;
import pl.zablocki.viewer.panels.MainFrame;

public class AppViewer {

    private Scenarios scenarios;
    private MainFrame mainFrame;

    public static void main(String[] args) {
        AppViewer appViewer = new AppViewer();
        appViewer.init();
        appViewer.run();
    }

    private void run() {
        SimulationRunnable simulationRunnable = new SimulationRunnable(scenarios);
        simulationRunnable.addListener(mainFrame.getCanvas());
        simulationRunnable.run();
    }

    private void init() {
        this.scenarios = loadScenarios();
        initFrame();
    }

    private Scenarios loadScenarios() {
        //TODO init with xml
        Road road = new Road(0);
        Position position = new Position(road, 0);
        VehicleData typicalVehicle = new VehicleData(position);
        StopLights stopLights = new StopLights(road);
        Scenario scenario1 = new Scenario(0, 2000, 100 * 60, typicalVehicle, stopLights);

        Road road2 = new Road(10);
        Position position2 = new Position(road2, 0);
        VehicleData typicalVehicle2 = new VehicleData(position2);
        StopLights stopLights2 = new StopLights(road2);
        Scenario scenario2 = new Scenario(1, 2000, 100 * 60, typicalVehicle2, stopLights2);

        Scenarios scenarios = new Scenarios();
        scenarios.setSimulationDuration(100 * 60);
        scenarios.getScenarios().add(scenario1);
        scenarios.getScenarios().add(scenario2);

        return scenarios;
    }

    private void initFrame() {
        this.mainFrame = new MainFrame();
    }

}

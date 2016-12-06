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
        Scenario scenario1 = createNewScenario(0, 35);
        Scenario scenario3 = createNewScenario(1, 35);
        Scenario scenario2 = createNewScenario(10, 1);


        Scenarios scenarios = new Scenarios();
        scenarios.setSimulationDuration(100 * 60);
        scenarios.getScenarios().add(scenario1);
        scenarios.getScenarios().add(scenario2);
        scenarios.getScenarios().add(scenario3);

        return scenarios;
    }

    private Scenario createNewScenario(int roadId, int bParam) {
        Road road = new Road(roadId);
        Position position = new Position(road, 0);
        VehicleData typicalVehicle = new VehicleData(position);
        typicalVehicle.getParams().setBParam(bParam);
        StopLights stopLights = new StopLights(road);
        return new Scenario(2000, 100 * 60, typicalVehicle, stopLights);
    }

    private void initFrame() {
        this.mainFrame = new MainFrame();
    }

}

package pl.zablocki.viewer;

import pl.zablocki.core.roadnetwork.Road;
import pl.zablocki.core.simulation.Scenario;
import pl.zablocki.core.simulation.SimulationRunnable;
import pl.zablocki.core.vehicle.Position;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.VehicleData;
import pl.zablocki.viewer.panels.MainFrame;

public class AppViewer {

    private Scenario scenario;
    private MainFrame mainFrame;

    public static void main(String[] args) {
        AppViewer appViewer = new AppViewer();
        appViewer.init();
        appViewer.run();
    }

    private void run() {
        SimulationRunnable simulationRunnable = new SimulationRunnable(scenario);
        simulationRunnable.addListener(mainFrame.getCanvas());
        simulationRunnable.run();
    }

    private void init() {
        this.scenario = loadScenario();
        initFrame();
    }

    private Scenario loadScenario() {
        //TODO init with xml
        Road road = new Road(20);
        Position position = new Position(road, 0);
        VehicleData typicalVehicle = new VehicleData(position);
        StopLights stopLights = new StopLights(road);
        Scenario scenario1 = new Scenario(2000, 100 * 60, typicalVehicle, stopLights);

//        VehicleData typicalVehicle2 = new VehicleData();
//        StopLights stopLights2 = new StopLights();
//        Scenario scenario2 = new Scenario(2000, 100 * 60, typicalVehicle2, stopLights2);

        return scenario1;
    }

    private void initFrame() {
        this.mainFrame = new MainFrame();
    }

}

package pl.zablocki.viewer;

import pl.zablocki.core.simulation.Scenario;
import pl.zablocki.core.simulation.SimulationRunnable;
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
        VehicleData typicalVehicle = new VehicleData();
        Scenario scenario = new Scenario(4000, 10*60, typicalVehicle);

        return scenario;
    }

    private void initFrame() {
        this.mainFrame = new MainFrame();
    }

}

package pl.zablocki.core.simulation;

import pl.zablocki.core.model.ParamsSingleton;
import pl.zablocki.core.vehicle.VehicleDataListener;
import pl.zablocki.viewer.panels.CanvasPanel;
import pl.zablocki.viewer.panels.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class SimulationRunnable implements Runnable {

    private Scenarios scenarios;
    private MainFrame mainFrame;
    private Simulation simulation;
    private List<VehicleDataListener> listeners = new ArrayList<VehicleDataListener>();
    private ParamsSingleton params = ParamsSingleton.getInstance();


    public SimulationRunnable(Scenarios scenarios) {
        this.scenarios = scenarios;
        prepareSimulation();
    }

    public void run() {
        double dt = params.getDt();
        double elapsedTime = 0;

        while (elapsedTime < scenarios.getSimulationDuration()) {
            RoadObjects roadObjects = simulation.doStep(dt, elapsedTime);
            roadObjects.setElapsedTime(elapsedTime);
            notifyListeners(roadObjects);
            sleep();
            elapsedTime += dt;
        }
    }

    public void addListener(VehicleDataListener vehicleDataListener) {
        listeners.add(vehicleDataListener);
    }

    public CanvasPanel getCanvas() {
        return mainFrame.getCanvas();
    }

    private void notifyListeners(RoadObjects roadObjects) {
        listeners.forEach(listener ->
                listener.updateRoadObjects(roadObjects)
        );
    }

    private void prepareSimulation() {
        this.simulation = new Simulation(scenarios);
    }

    private void sleep() {
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

}
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
    private List<VehicleDataListener> listeners = new ArrayList<>();
    private ParamsSingleton params = ParamsSingleton.getInstance();


    public SimulationRunnable(Scenarios scenarios) {
        this.scenarios = scenarios;
        prepareSimulation();
    }

    public void run() {
        double elapsedTime = 0;

        while (elapsedTime < scenarios.getSimulationDuration()) {
            double dt = params.getDt();
            RoadData roadData = simulation.doStep(dt, elapsedTime);
            roadData.getSimulationStatistics().setElapsedTime(elapsedTime);
            notifyListeners(roadData);
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

    private void notifyListeners(RoadData roadData) {
        listeners.forEach(listener ->
                listener.updateRoadObjects(roadData)
        );
    }

    private void prepareSimulation() {
        this.simulation = new Simulation(scenarios);
    }

    private void sleep() {
        try {
            Thread.sleep((long) params.getThreadSleep());
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

}
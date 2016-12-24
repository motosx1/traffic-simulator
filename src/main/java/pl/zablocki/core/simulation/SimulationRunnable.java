package pl.zablocki.core.simulation;

import pl.zablocki.core.model.ParamsSingleton;
import pl.zablocki.core.roadobjects.VehicleDataListener;

import java.util.ArrayList;
import java.util.List;

public class SimulationRunnable implements Runnable {

    private Scenarios scenarios;
    private Simulation simulation;
    private List<VehicleDataListener> listeners = new ArrayList<>();
    private ParamsSingleton params = ParamsSingleton.getInstance();


    public SimulationRunnable(Scenarios scenarios) {
        this.scenarios = scenarios;
        prepareSimulation();
    }

    @Override
    public void run() {
        double elapsedTime = 0;
        double dt = params.getDt();

        while (elapsedTime < scenarios.getSimulationDuration()) {
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
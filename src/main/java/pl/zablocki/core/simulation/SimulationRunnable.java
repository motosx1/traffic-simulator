package pl.zablocki.core.simulation;

import pl.zablocki.core.model.ParamsSingleton;
import pl.zablocki.core.roadobjects.VehicleDataListener;

import java.util.ArrayList;
import java.util.List;

public class SimulationRunnable implements Runnable, Observable {

    private Scenarios scenarios;
    private Simulation simulation;
    private List<VehicleDataListener> listeners = new ArrayList<>();
    private ParamsSingleton params = ParamsSingleton.getInstance();
    private double threadSleepTime = params.getThreadSleep();


    public SimulationRunnable(Scenarios scenarios) {
        this.scenarios = scenarios;
        prepareSimulation();
    }

    public SimulationRunnable(Scenarios scenarios, double sleepTime) {
        this.scenarios = scenarios;
        threadSleepTime = sleepTime;
        prepareSimulation();
    }

    @Override
    public void run() {
        double elapsedTime = 0;
        double dt = params.getDt();

        while (elapsedTime < scenarios.getSimulationDuration()) {
            RoadData roadData = simulation.doStep(dt, elapsedTime);
            roadData.getSimulationStatistics().setElapsedTime(elapsedTime);
            roadData.setSimulationDuration(scenarios.getSimulationDuration());
            notifyListeners(roadData);
            sleep();
            elapsedTime += dt;
        }
        notifyListenersEnd();
    }

    @Override
    public void addListener(VehicleDataListener vehicleDataListener) {
        listeners.add(vehicleDataListener);
    }

    @Override
    public void notifyListeners(RoadData roadData) {
        listeners.forEach(listener ->
                listener.updateRoadObjects(roadData)
        );
    }

    @Override
    public void notifyListenersEnd() {
        listeners.forEach(VehicleDataListener::sendEndSignal);
    }


    private void prepareSimulation() {
        this.simulation = new Simulation(scenarios);
    }

    private void sleep() {
        try {
            Thread.sleep((long) threadSleepTime);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

}
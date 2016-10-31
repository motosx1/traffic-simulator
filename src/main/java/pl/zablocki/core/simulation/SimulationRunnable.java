package pl.zablocki.core.simulation;

import pl.zablocki.core.longitudinalmodel.ParamsSingleton;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleDataListener;
import pl.zablocki.viewer.panels.CanvasPanel;
import pl.zablocki.viewer.panels.MainFrame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimulationRunnable implements Runnable {

    private Scenario scenario;
    private MainFrame mainFrame;
    private Simulation simulation;
    private List<VehicleDataListener> listeners = new ArrayList<VehicleDataListener>();
    ParamsSingleton params;

    public SimulationRunnable(Scenario scenario) {
        this.scenario = scenario;
        params = ParamsSingleton.getInstance();
        prepareSimulation();
    }

    public void run() {
        double dt = params.getDt();

        double totalTime = 0;

        while (totalTime < scenario.getSimulationDuration()){
            //TODO tutaj cala logika
            sleep();
            totalTime += dt;
        }

    }

    public void addListener(VehicleDataListener vehicleDataListener) {
        listeners.add(vehicleDataListener);
    }


    public CanvasPanel getCanvas() {
        return mainFrame.getCanvas();
    }

    public void notifyListeners(Set<Vehicle> vehicles) {
        //TODO przekazać aktualną liste z pojazdami ;)
        listeners.forEach(listener ->
                listener.updateVehicles(new HashSet<Vehicle>())
        );
    }

    private void prepareSimulation() {
        Simulation simulation = new Simulation(scenario);
    }

    private void sleep() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

}
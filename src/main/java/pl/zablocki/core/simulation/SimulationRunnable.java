package pl.zablocki.core.simulation;

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

    public SimulationRunnable(Scenario scenario) {
        this.scenario = scenario;
        prepareSimulation();
    }

    public void run() {
        System.out.println(scenario);
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

}
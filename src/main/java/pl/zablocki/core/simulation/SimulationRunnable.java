package pl.zablocki.core.simulation;

import pl.zablocki.core.longitudinalmodel.ParamsSingleton;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleDataListener;
import pl.zablocki.viewer.panels.CanvasPanel;
import pl.zablocki.viewer.panels.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class SimulationRunnable implements Runnable {

    private Scenario scenario;
    private MainFrame mainFrame;
    private Simulation simulation;
    private List<VehicleDataListener> listeners = new ArrayList<VehicleDataListener>();
    private ParamsSingleton params = ParamsSingleton.getInstance();


    public SimulationRunnable(Scenario scenario) {
        this.scenario = scenario;
        prepareSimulation();
    }

    public void run() {
        double dt = params.getDt();

        double elapsedTime = 0;

        while (elapsedTime < scenario.getSimulationDuration()) {
            List<Vehicle> vehicles = simulation.doStep(dt, elapsedTime);
            notifyListeners(vehicles);
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

    private void notifyListeners(List<Vehicle> vehicles) {
        listeners.forEach(listener ->
                listener.updateVehicles(vehicles)
        );
    }

    private void prepareSimulation() {
        this.simulation = new Simulation(scenario);
    }

    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

}
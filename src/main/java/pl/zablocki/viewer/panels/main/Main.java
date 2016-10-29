package pl.zablocki.viewer.panels.main;

import pl.zablocki.core.Vehicle;
import pl.zablocki.viewer.panels.CanvasPanel;
import pl.zablocki.viewer.panels.MainFrame;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private Main() {
    }

    public static void main(String[] args) throws InterruptedException {
        final Map<Integer, Vehicle> vehicles = generateVehicles();
        MainFrame mainFrame = new MainFrame(vehicles);

        CanvasPanel canvas = mainFrame.getCanvas();

        for (int i = 1; i <= 200; i++) {
            System.out.print("#"+i+" ");
            for (Map.Entry<Integer, Vehicle> vehicleEntry : vehicles.entrySet()) {
                Integer id = vehicleEntry.getKey();
                Vehicle vehicle = vehicleEntry.getValue();
                vehicle.getNewPosition(0.2);

                System.out.print(" \tvehicle#"+id+" "+ "new position: "+vehicle.getPosition());
                canvas.repaint();

                Thread.sleep(20);
            }
            System.out.println();
        }

    }

    private static Map<Integer, Vehicle> generateVehicles() {
        final Vehicle vehicle1 = new Vehicle(24);
        final Vehicle vehicle2 = new Vehicle(12);
        final Vehicle vehicle3 = new Vehicle(0);

        vehicle2.setVehicleBefore(vehicle1);
        vehicle3.setVehicleBefore(vehicle2);

        final Map<Integer, Vehicle> vehicles = new HashMap<Integer, Vehicle>();

        vehicles.put(1, vehicle1);
        vehicles.put(2, vehicle2);
        vehicles.put(3, vehicle3);
        return vehicles;
    }
}


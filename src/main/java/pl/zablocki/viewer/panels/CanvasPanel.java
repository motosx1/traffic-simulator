package pl.zablocki.viewer.panels;

import pl.zablocki.core.roadnetwork.Road;
import pl.zablocki.core.simulation.RoadObjects;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleDataListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CanvasPanel extends JPanel implements VehicleDataListener {

    private List<Vehicle> vehicles = new ArrayList<>();
    private List<StopLights> stopLightsList = new ArrayList<>();
    private RoadObjects roadObjects;
    private List<Road> uniqueRoads = new ArrayList<>();
    NumberFormat decimalFormatter = new DecimalFormat("#0.00");

    CanvasPanel() {
        super();
        setBackground(Color.WHITE);
        repaint();
    }

    public void updateRoadObjects(RoadObjects roadObjects) {
        this.roadObjects = roadObjects;
        this.vehicles = roadObjects.getVehicles();
        this.stopLightsList = roadObjects.getStopLights();
        this.uniqueRoads = this.vehicles.stream().map(Vehicle::getRoad).filter(distinctByKey(Road::getId)).collect(Collectors.toList());
        repaint();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int roadWidth = 14;
        int carHeight = 10;

        Graphics2D g2 = (Graphics2D) g;
        setCanvasCenter(g2);

        if (roadObjects != null) {
            g2.drawString("Elapsed time: " + decimalFormatter.format(roadObjects.getElapsedTime()), 20, -MainFrame.FRAME_HEIGHT / 2 + 20);
        }

        for (StopLights stopLights : stopLightsList) {
            int lightPosition = stopLights.getRoad().getId() * roadWidth - 10;
            g2.setColor(stopLights.getColor());
            g2.fillRect((int) stopLights.getDistance(), lightPosition, 20, 10);
            int ovalDiameter = stopLights.getNotifyRadius();
            g2.fillRect((int) stopLights.getDistance() - ovalDiameter / 2, lightPosition + 4, 3, 26);
            g2.setColor(Color.BLACK);
        }

        for (Road road : uniqueRoads) {
            int topRoadLine = road.getId() * roadWidth;
            int bottomRoadLine = road.getId() * roadWidth + roadWidth;
            g2.drawLine(0, topRoadLine, MainFrame.FRAME_WIDTH, topRoadLine);
            g2.drawLine(0, bottomRoadLine, MainFrame.FRAME_WIDTH, bottomRoadLine);
        }

        for (Vehicle vehicle : vehicles) {
            Color color = getRedGreenScaledColor(vehicle.getSpeed(), vehicle.getDesiredSpeed());
            g2.setColor(color);
            int carPositionY = getCarPositionY(roadWidth, carHeight, vehicle);
            g2.fillRect((int) vehicle.getDistance(), carPositionY, (int) vehicle.getLength(), carHeight);
            g2.setColor(Color.BLACK);
            //+ "/" + (int) vehicle.getSpeed()
            g2.drawString("" + vehicle.getId(), (int) vehicle.getDistance(), carPositionY + carHeight + 14);
        }

    }

    private int getCarPositionY(int roadWidth, int carHeight, Vehicle vehicle) {
        return (vehicle.getRoad().getId() * roadWidth) + (roadWidth - carHeight) / 2;
    }

    private Color getRedGreenScaledColor(double speed, double desiredSpeed) {
        double colorFactor = 255 / desiredSpeed;
        int green = Math.min((int) (speed * colorFactor), 255);
        int red = 255 - green;
        return new Color(red, green, 0);
    }

    private void setCanvasCenter(Graphics2D g2) {
        AffineTransform tx = new AffineTransform();
        tx.translate(20, MainFrame.getCanvasPanelSize().getHeight() / (double) 2);
        g2.setTransform(tx);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
package pl.zablocki.viewer.panels;

import pl.zablocki.core.road.Road;
import pl.zablocki.core.simulation.RoadData;
import pl.zablocki.core.simulation.SimulationStatistics;
import pl.zablocki.core.roadobjects.ObjectType;
import pl.zablocki.core.roadobjects.StopLight;
import pl.zablocki.core.roadobjects.Vehicle;
import pl.zablocki.core.roadobjects.VehicleDataListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class CanvasPanel extends JPanel implements VehicleDataListener {

    private RoadData roadData;
    private NumberFormat decimalFormatter = new DecimalFormat("#0.00");
    private int distanceBetweenRoads = 150;
    private int lineWidth = 14;


    CanvasPanel() {
        super();
        setBackground(Color.WHITE);
        repaint();
    }

    public void updateRoadObjects(RoadData roadData) {
        this.roadData = roadData;
        repaint();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        setCanvasCenter(g2);

        if (roadData == null) {
            return;
        }

        g2.drawString("Elapsed time: " + decimalFormatter.format(roadData.getSimulationStatistics().getElapsedTime()), 20, -180);

        try {
            roadData.getRoads().forEach(road -> {
                drawData(road, g2);
                road.getLines().forEach(line -> {
                    drawStopLight(line.getStopLight(), road.getId(), line.getId(), g2);
                    drawLine(line.getId(), road.getId(), g2);
                    line.getVehicles().forEach(vehicle ->
                            drawVehicle(vehicle, road.getId(), line.getId(), g2)
                    );
                });
            });

            drawStatistics(g2);
        } catch (Exception ignored){}

    }

    private void drawData(Road road, Graphics2D g2) {
        int textPositionY = getLinePostitionY(0, road.getId()) - 5;
        g2.drawString("autonomous vehicles percentage amount: " + road.getAutonomousPercentage() + "%", getDataBoxX(), textPositionY);
    }

    private void drawStatistics(Graphics2D g2) {
        SimulationStatistics simulationStatistics = roadData.getSimulationStatistics();

        drawAverageSpeed(g2, simulationStatistics.getAverageSpeed());
        drawStoppedVehiclesAmount(g2, simulationStatistics.getStoppedVehicles());
    }

    private void drawAverageSpeed(Graphics2D g2, Map<Road, Double> averageSpeedMap) {
        for (Map.Entry<Road, Double> entry : averageSpeedMap.entrySet()) {
            Road road = entry.getKey();
            Double averageSpeed = entry.getValue();

            int textPositionY = getLinePostitionY(0, road.getId()) - 5;
            g2.drawString("average speed: " + decimalFormatter.format(averageSpeed), getStatisticsBoxX(), textPositionY);
        }
    }

    private void drawStoppedVehiclesAmount(Graphics2D g2, Map<Road, Integer> stoppedVehiclesMap) {
        for (Map.Entry<Road, Integer> entry : stoppedVehiclesMap.entrySet()) {
            Road road = entry.getKey();
            Integer stoppedVehicles = entry.getValue();

            int textPositionY = getLinePostitionY(0, road.getId()) - 18;
            g2.drawString("stopped vehicles: " + stoppedVehicles, getStatisticsBoxX(), textPositionY);
        }
    }

    private int getStatisticsBoxX() {
        return 300;
    }

    private int getDataBoxX() {
        return 10;
    }

    private void drawVehicle(Vehicle vehicle, int roadId, int lineId, Graphics2D g2) {
        if (vehicle != null) {
            int carHeight = 10;
            if (vehicle.getObjectType() == ObjectType.OBSTACLE) {
                g2.setColor(Color.BLACK);
            } else {
                Color color = getRedGreenScaledColor(vehicle.getSpeed(), vehicle.getMaxSpeed());
                g2.setColor(color);
            }
            int carPositionY = getCarPositionY(lineWidth, carHeight, roadId, lineId);
            double position = vehicle.getPosition();
            if (vehicle.getObjectType() == ObjectType.AUTONOMOUS) {
                g2.fillOval((int) position, carPositionY, (int) vehicle.getLength(), carHeight);
            } else {
                g2.fillRect((int) position, carPositionY, (int) vehicle.getLength(), carHeight);
            }
            g2.setColor(Color.BLACK);
            //+ "/" + decimalFormatter.format(roadobjects.getSpeed())
            g2.drawString("" + vehicle.getId(), (int) position + 2, carPositionY + 10);
        }

    }

    private void drawStopLight(StopLight stopLight, int roadId, int lineId, Graphics2D g2) {
        if (stopLight != null) {
            int lightPosition = getLinePostitionY(lineId, roadId) + 2;
            g2.setColor(stopLight.getColor());
            g2.fillRect((int) (stopLight.getPosition() + stopLight.getLength()), lightPosition, 10, 10);
            int ovalDiameter = stopLight.getNotifyRadius();
            g2.fillRect((int) stopLight.getPosition() - ovalDiameter / 2, lightPosition - 6, 3, 26);
        }
    }

    private void drawLine(int lineId, int roadId, Graphics2D g2) {
        g2.setColor(Color.BLACK);
        int topRoadLine = getLinePostitionY(lineId, roadId);
        int bottomRoadLine = getLinePostitionY(lineId, roadId) + lineWidth;
        g2.drawLine(0, topRoadLine, MainFrame.FRAME_WIDTH, topRoadLine);
        g2.drawLine(0, bottomRoadLine, MainFrame.FRAME_WIDTH, bottomRoadLine);
    }

    private int getLinePostitionY(int lineId, int roadId) {
        return roadId * distanceBetweenRoads + lineId * lineWidth;
    }

    private int getCarPositionY(int roadWidth, int carHeight, int roadId, int lineId) {
        return (roadId * distanceBetweenRoads + lineId * roadWidth) + (roadWidth - carHeight) / 2;
    }

    private Color getRedGreenScaledColor(double speed, double desiredSpeed) {
        double colorFactor = 255 / desiredSpeed;
        int green = Math.min((int) (speed * colorFactor), 255);
        int red = 255 - green;
        return new Color(red, green, 0);
    }

    private void setCanvasCenter(Graphics2D g2) {
        AffineTransform tx = new AffineTransform();
        tx.translate(20, 200);//MainFrame.getCanvasPanelSize().getHeight() / (double) 4);
        g2.setTransform(tx);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
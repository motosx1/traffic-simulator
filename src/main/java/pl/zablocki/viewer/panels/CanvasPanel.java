package pl.zablocki.viewer.panels;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.simulation.RoadData;
import pl.zablocki.core.vehicle.StopLight;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleDataListener;

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
    private int distanceBetweenRoads = 30;

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
        int roadWidth = 14;

        Graphics2D g2 = (Graphics2D) g;
        setCanvasCenter(g2);

        if (roadData == null) {
            return;
        }

        g2.drawString("Elapsed time: " + decimalFormatter.format(roadData.getElapsedTime()), 20, -180);

        roadData.getRoads().forEach(road -> {
            drawStopLight(road.getStopLight(), road.getId(), g2);
            road.getLines().forEach(line -> {
                drawLine(line, roadWidth, road.getId(), g2);
                line.getVehicles().forEach(vehicle ->
                    drawVehicle(vehicle, road.getId(), line.getId(), roadWidth, g2)
                );
            });
        });


    }

    private void drawVehicle(Vehicle vehicle, int roadId, int lineId, int roadWidth, Graphics2D g2) {
        if (vehicle != null) {
            int carHeight = 10;
            Color color = getRedGreenScaledColor(vehicle.getSpeed(), vehicle.getMaxSpeed());
            g2.setColor(color);
            int carPositionY = getCarPositionY(roadWidth, carHeight, roadId, lineId);
            double position = vehicle.getPosition();
            g2.fillRect((int) position, carPositionY, (int) vehicle.getLength(), carHeight);
            g2.setColor(Color.BLACK);
            //+ "/" + decimalFormatter.format(vehicle.getSpeed())
            g2.drawString("" + vehicle.getId(), (int) position, carPositionY + 10);
        }

    }

    private void drawStopLight(StopLight stopLight, int roadId, Graphics2D g2) {
        if (stopLight != null) {
            int lightPosition = roadId * distanceBetweenRoads - 10;
            g2.setColor(stopLight.getColor());
            g2.fillRect((int) stopLight.getPosition(), lightPosition, 20, 10);
            int ovalDiameter = stopLight.getNotifyRadius();
            g2.fillRect((int) stopLight.getPosition() - ovalDiameter / 2, lightPosition + 4, 3, 26);
        }
    }

    private void drawLine(Line line, int roadWidth, int roadId, Graphics2D g2) {
        g2.setColor(Color.BLACK);
        int topRoadLine = roadId * distanceBetweenRoads + line.getId() * roadWidth;
        int bottomRoadLine = roadId * distanceBetweenRoads + line.getId() * roadWidth + roadWidth;
        g2.drawLine(0, topRoadLine, MainFrame.FRAME_WIDTH, topRoadLine);
        g2.drawLine(0, bottomRoadLine, MainFrame.FRAME_WIDTH, bottomRoadLine);
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
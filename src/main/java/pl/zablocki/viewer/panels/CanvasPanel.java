package pl.zablocki.viewer.panels;

import pl.zablocki.core.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

public class CanvasPanel extends JPanel {

    private Map<Integer, Vehicle> vehicles;

    CanvasPanel(Map<Integer, Vehicle> vehices) {
        super();
        this.vehicles = vehices;
        setBackground(Color.WHITE);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        setCanvasCenter(g2);
        g2.drawLine(0,0,700,0);

        for (Map.Entry<Integer, Vehicle> vehicleEntry : vehicles.entrySet()) {
            Vehicle vehicle = vehicleEntry.getValue();
            g2.fillOval((int)vehicle.getPosition() , 0, 10,10);
        }
    }

    private void setCanvasCenter(Graphics2D g2) {
        AffineTransform tx = new AffineTransform();
        tx.translate(20,MainFrame.getCanvasPanelSize().getHeight()/(double)2);
        g2.setTransform(tx);
    }
}

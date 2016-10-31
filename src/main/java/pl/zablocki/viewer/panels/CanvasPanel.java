package pl.zablocki.viewer.panels;

import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleDataListener;
import pl.zablocki.viewer.structures.VehicleDataForCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Set;

public class CanvasPanel extends JPanel implements VehicleDataListener {

	private VehicleDataForCanvas vehicles;


	CanvasPanel() {
		super();
		setBackground(Color.WHITE);
		repaint();
	}

	public void updateVehicles(Set<Vehicle> vehicles) {

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		setCanvasCenter(g2);
		g2.drawLine(0,0,700,0);

//		for (Map.Entry<Integer, Vehicle> vehicleEntry : vehicles.entrySet()) {
//			Vehicle vehicle = vehicleEntry.getValue();
//			g2.fillOval((int)vehicle.getPosition() , 0, 10,10);
//		}
	}

	private void setCanvasCenter(Graphics2D g2) {
		AffineTransform tx = new AffineTransform();
		tx.translate(20,MainFrame.getCanvasPanelSize().getHeight()/(double)2);
		g2.setTransform(tx);
	}
}
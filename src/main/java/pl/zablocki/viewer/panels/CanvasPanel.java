package pl.zablocki.viewer.panels;

import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleDataListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class CanvasPanel extends JPanel implements VehicleDataListener {

	private List<Vehicle> vehicles = new ArrayList<>();

	CanvasPanel() {
		super();
		setBackground(Color.WHITE);
		repaint();
	}

	public void updateVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		setCanvasCenter(g2);
		g2.drawLine(0,0,700,0);

		for (Vehicle vehicle : vehicles) {
			g2.fillOval((int)vehicle.getDistance() , 0, 10,10);
		}

	}

	private void setCanvasCenter(Graphics2D g2) {
		AffineTransform tx = new AffineTransform();
		tx.translate(20,MainFrame.getCanvasPanelSize().getHeight()/(double)2);
		g2.setTransform(tx);
	}
}
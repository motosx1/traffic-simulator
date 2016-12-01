package pl.zablocki.viewer.panels;

import pl.zablocki.core.simulation.RoadObjects;
import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.VehicleDataListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class CanvasPanel extends JPanel implements VehicleDataListener {

	private List<Vehicle> vehicles = new ArrayList<>();
	private StopLights stopLights;

	CanvasPanel() {
		super();
		setBackground(Color.WHITE);
		repaint();
	}

	public void updateRoadObjects(RoadObjects roadObjects){
		this.vehicles = roadObjects.getVehicles();
		this.stopLights = roadObjects.getStopLights();
		repaint();
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		setCanvasCenter(g2);
		g2.drawLine(0,0,700,0);

		if (stopLights != null) {
			g2.setColor(stopLights.getColor());
			g2.fillRect((int)stopLights.getDistance(), -10, 20, 10);
			int ovalDiameter = stopLights.getNotifyRadius()*2;
			g2.drawOval((int)stopLights.getDistance()-ovalDiameter/2, -10-ovalDiameter/2+10, ovalDiameter, ovalDiameter);
			g2.setColor(Color.BLACK);
		}

		for (Vehicle vehicle : vehicles) {
			Color color = getRedGreenScaledColor(vehicle.getSpeed(), vehicle.getDesiredSpeed());

			g2.setColor(color);
			g2.fillRect((int)vehicle.getDistance(), 0, (int)vehicle.getLength(), 10);
			g2.setColor(Color.BLACK);
			g2.drawString(""+vehicle.getId()+"/"+(int)vehicle.getSpeed(), (int)vehicle.getDistance(), 20 );
		}

	}

	private Color getRedGreenScaledColor(double speed, double desiredSpeed) {
		double colorFactor = 255 / desiredSpeed;
		int green = Math.min((int) (speed * colorFactor),255);
		int red = 255 - green;
		return new Color(red, green, 0);
	}

	private void setCanvasCenter(Graphics2D g2) {
		AffineTransform tx = new AffineTransform();
		tx.translate(20,MainFrame.getCanvasPanelSize().getHeight()/(double)2);
		g2.setTransform(tx);
	}
}
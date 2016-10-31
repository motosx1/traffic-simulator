package pl.zablocki.core.simulation;

import pl.zablocki.core.vehicle.Vehicle;

import java.util.Set;

public class Simulation {

	private Set<Vehicle> activeVehicles;
	private Scenario scenario;

	public Simulation(Scenario scenario){
		this.scenario = scenario;
	}


	/**
	 * VehicleFactory.create(activeVehicles, scenario)
	 */
	private void createNewVehicles(){

	}

	public void deleteNotActiveVehicles(){

	}

	public void doStep(){

	}

	private Set<Vehicle> updateVehicles(){
		return null;
	}

}
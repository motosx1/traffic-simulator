package pl.zablocki.viewer;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.road.RoadObject;
import pl.zablocki.core.simulation.Scenario;
import pl.zablocki.core.simulation.Scenarios;
import pl.zablocki.core.simulation.SimulationRunnable;
import pl.zablocki.core.vehicle.StopLight;
import pl.zablocki.core.vehicle.Vehicle;
import pl.zablocki.core.vehicle.ObjectType;
import pl.zablocki.viewer.panels.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class AppViewer {

    private Scenarios scenarios;
    private MainFrame mainFrame;

    public static void main(String[] args) {
        AppViewer appViewer = new AppViewer();
        appViewer.init();
        appViewer.run();
    }

    private void run() {
        SimulationRunnable simulationRunnable = new SimulationRunnable(scenarios);
        simulationRunnable.addListener(mainFrame.getCanvas());
        simulationRunnable.run();
    }

    private void init() {
        this.scenarios = loadScenarios();
        initFrame();
    }

    private Scenarios loadScenarios() {
        //TODO init with xml
        Scenario scenario1 = createNewScenario(0, 0);
        Scenario scenario3 = createNewScenario(5, 80);


        Scenarios scenarios = new Scenarios();
        scenarios.setSimulationDuration(100 * 60);
        scenarios.getScenarios().add(scenario1);
        scenarios.getScenarios().add(scenario3);

        return scenarios;
    }

    private Scenario createNewScenario(int roadId, int autonomusPercentage) {

        StopLight stopLight1 = new StopLight(1600);
        StopLight stopLight2 = new StopLight(1600);
        stopLight1.setObjectType(ObjectType.STOPLIGHT);
        stopLight2.setObjectType(ObjectType.STOPLIGHT);
        Line line1 = new Line(0);
        Line line2 = new Line(1);
//        line1.setStopLight(stopLight1);
//        line2.setStopLight(stopLight2);
        line1.setCarsPerHour(1000);
        line2.setCarsPerHour(1000);

        List<Line> lines = new ArrayList<>();
        lines.add(line1);
        lines.add(line2);

        // standing obstacle
        RoadObject standingObstacle = new RoadObject();
        standingObstacle.setPosition(1200);
        standingObstacle.setMaxSpeed(0);
        standingObstacle.setSpeed(0);
        standingObstacle.setObjectType(ObjectType.OBSTACLE);
        line2.getVehicles().add(new Vehicle(66, standingObstacle, null));
        //

        RoadObject typicalVehicleData = new RoadObject();
        typicalVehicleData.setAcceleration(2.2);
        typicalVehicleData.setMaxAcceleration(2.2);
        typicalVehicleData.setSpeed(15);
        typicalVehicleData.setMaxSpeed(20);

        Road road = new Road(roadId);
        road.getLines().addAll(lines);
        road.setAutonomusPercentage(autonomusPercentage);

        Scenario scenario = new Scenario();
        scenario.setTypicalVehicle(typicalVehicleData);
        scenario.setRoad(road);

        return scenario;

    }

    private void initFrame() {
        this.mainFrame = new MainFrame();
    }

}

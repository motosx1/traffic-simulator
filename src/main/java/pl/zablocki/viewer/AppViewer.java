package pl.zablocki.viewer;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.road.RoadObject;
import pl.zablocki.core.simulation.Scenario;
import pl.zablocki.core.simulation.Scenarios;
import pl.zablocki.core.simulation.SimulationRunnable;
import pl.zablocki.core.roadobjects.StopLight;
import pl.zablocki.core.roadobjects.Vehicle;
import pl.zablocki.core.roadobjects.ObjectType;
import pl.zablocki.viewer.panels.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class AppViewer {

    private Scenarios scenarios;
    private MainFrame mainFrame;

    public static void main(String[] args) {
        AppViewer appViewer = new AppViewer();
        appViewer.run();
    }

    private void run() {
        init();
        SimulationRunnable simulationRunnable = new SimulationRunnable(scenarios);
        simulationRunnable.addListener(mainFrame.getCanvas());
        Thread simulationThread = new Thread(simulationRunnable);
        simulationThread.start();
    }

    private void init() {
        this.scenarios = loadScenarios();
        initFrame();
    }

    private Scenarios loadScenarios() {
        //TODO init with xml
        Scenario scenario1 = createNewScenario(0, 0);
        Scenario scenario2 = createNewScenario(1, 50);
        Scenario scenario3 = createNewScenario(2, 100);


        Scenarios scenarios = new Scenarios();
        scenarios.setSimulationDuration(100 * 60);
        scenarios.getScenarios().add(scenario1);
        scenarios.getScenarios().add(scenario2);
        scenarios.getScenarios().add(scenario3);

        return scenarios;
    }

    private Scenario createNewScenario(int roadId, int autonomousPercentage) {

        StopLight stopLight1 = new StopLight(1600);
        StopLight stopLight2 = new StopLight(1600);
        StopLight stopLight3 = new StopLight(1600);
        StopLight stopLight4 = new StopLight(1600);
        stopLight1.setObjectType(ObjectType.STOPLIGHT);
        stopLight2.setObjectType(ObjectType.STOPLIGHT);
        stopLight3.setObjectType(ObjectType.STOPLIGHT);
        stopLight4.setObjectType(ObjectType.STOPLIGHT);
        Line line1 = new Line(0);
        Line line2 = new Line(1);
        Line line3 = new Line(2);
        Line line4 = new Line(3);
        line1.setStopLight(stopLight1);
        line2.setStopLight(stopLight2);
        line3.setStopLight(stopLight3);
        line4.setStopLight(stopLight4);
        line1.setCarsPerHour(1000);
        line2.setCarsPerHour(2000);
        line3.setCarsPerHour(1000);
        line4.setCarsPerHour(1000);

        List<Line> lines = new ArrayList<>();
        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        lines.add(line4);

        // standing obstacle
        RoadObject standingObstacle = new RoadObject();
        standingObstacle.setPosition(1200);
        standingObstacle.setMaxSpeed(0);
        standingObstacle.setSpeed(0);
        standingObstacle.setObjectType(ObjectType.OBSTACLE);
        line2.getVehicles().add(new Vehicle(66, standingObstacle, null));
        // standing obstacle
        RoadObject standingObstacle2 = new RoadObject();
        standingObstacle2.setPosition(1175);
        standingObstacle2.setMaxSpeed(0);
        standingObstacle2.setSpeed(0);
        standingObstacle2.setObjectType(ObjectType.OBSTACLE);
        line3.getVehicles().add(new Vehicle(66, standingObstacle2, null));
        //

        RoadObject typicalVehicleData = new RoadObject();
        typicalVehicleData.setAcceleration(2.2);
        typicalVehicleData.setMaxAcceleration(2.2);
        typicalVehicleData.setSpeed(35);
        typicalVehicleData.setMaxSpeed(40);

        Road road = new Road(roadId);
        road.getLines().addAll(lines);
        road.setAutonomousPercentage(autonomousPercentage);

        Scenario scenario = new Scenario();
        scenario.setTypicalVehicle(typicalVehicleData);
        scenario.setRoad(road);

        return scenario;

    }

    private void initFrame() {
        this.mainFrame = new MainFrame();
    }

}

package pl.zablocki.viewer;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.roadobjects.*;
import pl.zablocki.core.simulation.Scenario;
import pl.zablocki.core.simulation.Scenarios;
import pl.zablocki.core.simulation.SimulationRunnable;
import pl.zablocki.viewer.panels.MainFrame;
import pl.zablocki.xml.XmlReader;

import java.util.ArrayList;
import java.util.List;

public class AppViewerMode {

    private Scenarios scenarios;
    private MainFrame mainFrame;

    public static void main(String[] args) {
        AppViewerMode appViewerMode = new AppViewerMode();
        appViewerMode.run();
    }

    private void run() {
        this.scenarios = loadScenarios();

        initFrame();
        SimulationRunnable simulationRunnable = new SimulationRunnable(scenarios);
        simulationRunnable.addListener(mainFrame.getCanvas());
        Thread simulationThread = new Thread(simulationRunnable);
        simulationThread.start();
    }

    private Scenarios loadScenarios() {
        String filePath = "C:\\Users\\Bartosz\\IdeaProjects\\TrafficSimulator\\inputData\\inputData2.xml";
        return XmlReader.getScenariosFromXml(filePath);

    }

    private void initFrame() {
        this.mainFrame = new MainFrame();
    }

}

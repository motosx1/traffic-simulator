package pl.zablocki.filewriter;

import pl.zablocki.core.simulation.Scenarios;
import pl.zablocki.core.simulation.SimulationRunnable;
import pl.zablocki.xml.XmlReader;

public class FileWriterMode {
        private Scenarios scenarios;

        public static void main(String[] args) {
            FileWriterMode fileWriterMode = new FileWriterMode();
            fileWriterMode.run();
        }

        private void run() {
            this.scenarios = loadScenarios();
            double sleepTime = 0;

            SimulationRunnable simulationRunnable = new SimulationRunnable(scenarios, sleepTime);
            FileWriter fileWriter = new FileWriter();
            simulationRunnable.addListener(fileWriter);
            Thread simulationThread = new Thread(simulationRunnable);
            simulationThread.start();
        }

        private Scenarios loadScenarios() {
            String filePath = "C:\\Users\\Bartosz\\IdeaProjects\\TrafficSimulator\\inputData\\thesis1.xml";
            return XmlReader.getScenariosFromXml(filePath);
        }
}

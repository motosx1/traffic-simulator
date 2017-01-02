package pl.zablocki.filewriter;

import pl.zablocki.core.simulation.Scenarios;
import pl.zablocki.core.simulation.SimulationRunnable;
import pl.zablocki.xml.XmlReader;

public class FileSimpleWriterMode {
        private Scenarios scenarios;

        public static void main(String[] args) {
            FileSimpleWriterMode fileWriterMode = new FileSimpleWriterMode();
            fileWriterMode.run();
        }

        private void run() {
            this.scenarios = loadScenarios();
            double sleepTime = 0;

            SimulationRunnable simulationRunnable = new SimulationRunnable(scenarios, sleepTime);
            FileSimpleWriter fileSimpleWriter = new FileSimpleWriter();
            simulationRunnable.addListener(fileSimpleWriter);
            Thread simulationThread = new Thread(simulationRunnable);
            simulationThread.start();
        }

        private Scenarios loadScenarios() {
            String filePath = "C:\\Users\\Bartosz\\IdeaProjects\\TrafficSimulator\\inputData\\thesis1.xml";
            return XmlReader.getScenariosFromXml(filePath);
        }
}

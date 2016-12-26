package pl.zablocki.xml;

import pl.zablocki.core.model.AccelerationModel;
import pl.zablocki.core.model.GippsModel;
import pl.zablocki.core.roadobjects.RoadObject;
import pl.zablocki.core.roadobjects.Vehicle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlReader {
    public static void toXml() {
        try {

            Scenario scenario1 = createNewScenario(0, 0);

            Scenarios scenarios = new Scenarios();
            scenarios.setSimulationDuration(100 * 60);
            scenarios.getScenarios().add(scenario1);


            File file = new File("file.xml");
            System.out.println(file.getAbsolutePath());
            JAXBContext jaxbContext = JAXBContext.newInstance(Scenarios.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();


            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(scenarios, file);
//            jaxbMarshaller.marshal(scenarios, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }

    public static pl.zablocki.core.simulation.Scenarios getScenariosFromXml(String filePath) {

        try {

            File file = new File(filePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(Scenarios.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Scenarios scenarios = (Scenarios) jaxbUnmarshaller.unmarshal(file);
            System.out.println(scenarios);

            return XmlReader.xmlScenariosToCoreScenarios(scenarios);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static pl.zablocki.core.simulation.Scenarios xmlScenariosToCoreScenarios(Scenarios xmlScenarios) {
        pl.zablocki.core.simulation.Scenarios scenarios = new pl.zablocki.core.simulation.Scenarios();

        if (xmlScenarios != null) {
            scenarios.setSimulationDuration(xmlScenarios.getSimulationDuration());

            List<pl.zablocki.core.simulation.Scenario> scenarioList = new ArrayList<>();

            for (Scenario xmlScenario : xmlScenarios.getScenarios()) {
                pl.zablocki.core.simulation.Scenario scenario = new pl.zablocki.core.simulation.Scenario();

                RoadObject typicalVehicleData = new Vehicle();
                typicalVehicleData.setAcceleration(xmlScenario.getTypicalVehicle().getAcceleration());
                typicalVehicleData.setMaxAcceleration(xmlScenario.getTypicalVehicle().getMaxAcceleration());
                typicalVehicleData.setSpeed(xmlScenario.getTypicalVehicle().getSpeed());
                typicalVehicleData.setMaxSpeed(xmlScenario.getTypicalVehicle().getMaxSpeed());
                typicalVehicleData.setAccelerationModel(getAccelerationModel(xmlScenario.getTypicalVehicle().getAccelerationModelType()));
                scenario.setTypicalVehicle(typicalVehicleData);

                pl.zablocki.core.road.Road coreRoad = new pl.zablocki.core.road.Road(xmlScenario.getRoad().getId());
                coreRoad.setAutonomousPercentage(xmlScenario.getRoad().getAutonomousPercentage());
                scenario.setRoad(coreRoad);


                List<pl.zablocki.core.road.Line> coreLines = new ArrayList<>();
                for (Line xmlLine : xmlScenario.getRoad().getLines()) {
                    pl.zablocki.core.road.Line coreLine = new pl.zablocki.core.road.Line(xmlLine.getId());
                    coreLine.setCarsPerHour(xmlLine.getCarsPerHour());

                    StopLight xmlStopLight = xmlLine.getStopLight();
                    if (xmlStopLight != null) {
                        pl.zablocki.core.roadobjects.StopLight coreStopLight = new pl.zablocki.core.roadobjects.StopLight(xmlStopLight.getPosition());
                        coreStopLight.setGreenLightTimeSec(xmlStopLight.getGreenLightTimeSec());
                        coreStopLight.setRedLightTimeSec(xmlStopLight.getRedLightTimeSec());
                        coreLine.setStopLight(coreStopLight);
                    }

                    List<Obstacle> xmlObstacles = xmlLine.getObstacles();
                    List<pl.zablocki.core.roadobjects.Vehicle> coreObstacles = new ArrayList<>();

                    if (xmlObstacles != null) {
                        for (Obstacle xmlObstacle : xmlObstacles) {
                            RoadObject standingObstacle = new pl.zablocki.core.roadobjects.Obstacle();
                            standingObstacle.setPosition(xmlObstacle.getPosition());
                            coreObstacles.add(new Vehicle(null, standingObstacle, null));
                        }
                        coreLine.getVehicles().addAll(coreObstacles);
                    }

                    coreLines.add(coreLine);
                }
                coreRoad.getLines().addAll(coreLines);


                scenarioList.add(scenario);
            }
            scenarios.setScenarios(scenarioList);
        }

        return scenarios;
    }

    private static AccelerationModel getAccelerationModel(AccelerationModelType accelerationModelType) {
        if (accelerationModelType == AccelerationModelType.GIPPS) {
            return new GippsModel();
        }

        return new GippsModel();
    }

    private static Scenario createNewScenario(int roadId, int autonomousPercentage) {

        StopLight stopLight1 = new StopLight(1600);
        StopLight stopLight2 = new StopLight(1600);
        StopLight stopLight3 = new StopLight(1600);
        StopLight stopLight4 = new StopLight(1600);
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
        Obstacle standingObstacle = new Obstacle();
        standingObstacle.setPosition(1200);
        line2.getObstacles().add(standingObstacle);
        // standing obstacle
        Obstacle standingObstacle2 = new Obstacle();
        standingObstacle2.setPosition(1175);
        line3.getObstacles().add(standingObstacle2);
        //

        TypicalVehicle typicalVehicleData = new TypicalVehicle();
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
}

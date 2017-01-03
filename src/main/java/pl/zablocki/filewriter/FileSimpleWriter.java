package pl.zablocki.filewriter;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.roadobjects.ObjectType;
import pl.zablocki.core.roadobjects.Vehicle;
import pl.zablocki.core.roadobjects.VehicleDataListener;
import pl.zablocki.core.simulation.RoadData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class FileSimpleWriter implements VehicleDataListener {

    private Map<Double, RoadData> simulationData = new TreeMap<>();
    private String pathCore;

    @Override
    public void updateRoadObjects(RoadData roadData) {
        RoadData clonedRoadData = cloneRoadData(roadData);

        double elapsedTime = clonedRoadData.getSimulationStatistics().getElapsedTime();
//        lastTime = elapsedTime;
        if (simulationData.get(elapsedTime) != null) {
            System.out.println("ups");
        }
        simulationData.put(elapsedTime, clonedRoadData);
    }

    @Override
    public void sendEndSignal() {
        Map<Road, TreeMap<Double, Road>> finalRoadsData = new HashMap<>();

        for (Map.Entry<Double, RoadData> simulationDataEntry : simulationData.entrySet()) {
            Double timeElapsed = simulationDataEntry.getKey();
            RoadData roadData = simulationDataEntry.getValue();
            List<Road> roads = roadData.getRoads();

            for (Road road : roads) {
                finalRoadsData.putIfAbsent(road, new TreeMap<>());
                Map<Double, Road> doubleRoadMap1 = finalRoadsData.get(road);
                doubleRoadMap1.put(timeElapsed, road);
            }
        }


        long tStart = System.currentTimeMillis();
        System.out.println("Zapisywanie danych do pliku...");
        writeDataToFile(finalRoadsData);
        createCombinedFiles(finalRoadsData);
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        System.out.println(elapsedSeconds);

    }

    private String createFileContent(TreeMap<Double, Road> roadMap) {
        String fileContent = "";

        for (Map.Entry<Double, Road> entry : roadMap.entrySet()) {
            Double elapsedTime = entry.getKey();
            Road road = entry.getValue();

            fileContent += createFileEntry(elapsedTime, road);
        }

        return fileContent;
    }

    private String createFileEntry(Double elapsedTime, Road road) {
        List<Vehicle> allVehicles = road.getLines().stream()
                .flatMap(line -> line.getVehicles().stream())
                .filter(vehicle -> vehicle.getObjectType() == ObjectType.AUTONOMOUS || vehicle.getObjectType() == ObjectType.NORMAL)
                .collect(Collectors.toList());
        List<Vehicle> stoppedVehicles = allVehicles.stream()
                .filter(vehicle -> vehicle.getSpeed() < 0.5 && (vehicle.getObjectType() == ObjectType.AUTONOMOUS || vehicle.getObjectType() == ObjectType.NORMAL))
                .collect(Collectors.toList());
        double sumSpeed = allVehicles.stream().mapToDouble(Vehicle::getSpeed).sum();


        FileLine fileLine = new FileLine();
        fileLine.setTimeElapsed(elapsedTime);
        fileLine.setRoadId(road.getId());
        fileLine.setVehiclesStopped(stoppedVehicles.size());
        fileLine.setVehiclesPassed(road.getVehiclesDeleted());
        fileLine.setAverageSpeed(sumSpeed / (double) allVehicles.size());

        return fileLine.getSimpleCsvString() + "\n";
    }

    private void writeDataToFile(Map<Road, TreeMap<Double, Road>> finalData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formatDateTime = LocalDateTime.now().format(formatter);

        for (Map.Entry<Road, TreeMap<Double, Road>> roadMapEntry : finalData.entrySet()) {
            Road road = roadMapEntry.getKey();
            TreeMap<Double, Road> timeRoadMap = roadMapEntry.getValue();

            pathCore = "results/s3result-" + formatDateTime + "_";
            String path = pathCore + road.getId() + ".csv";

            String text = createFileContent(timeRoadMap);
            text = createSimpleHeader(road.getAutonomousPercentage()) + text;


            try {
                createNewFile(path);
                Files.write(Paths.get(path), text.getBytes());
            } catch (IOException e) {
                System.err.println("Failed to save a file!");
                e.printStackTrace();
            }
        }

    }

    private void createCombinedFiles(Map<Road, TreeMap<Double, Road>> finalData) {
        String pathCombined = pathCore + "combined" + ".csv";
        Map<Integer, String> filesCombined = new TreeMap<>();

        for (Map.Entry<Road, TreeMap<Double, Road>> roadMapEntry : finalData.entrySet()) {
            Road road = roadMapEntry.getKey();


            String pathFirst = pathCore + road.getId() + ".csv";

            try {
                List<String> file = Files.lines(Paths.get(pathFirst)).collect(Collectors.toList());

                for (int i = 0; i < file.size(); i++) {
                    filesCombined.putIfAbsent(i, "");
                    filesCombined.put(i, filesCombined.get(i) + file.get(i));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            createNewFile(pathCombined);
            String res = "";

            for (Map.Entry<Integer, String> entry : filesCombined.entrySet()) {
                String value = entry.getValue();
                res += value + "\n";
            }

            Files.write(Paths.get(pathCombined), res.getBytes());
        } catch (IOException e) {
            System.err.println("Failed to save a file!");
            e.printStackTrace();
        }

    }

    private String createSimpleHeader(double autonomousPercentage) {
        String result = "";

        result += "elapsedTime;roadId;averageSpeed_"+autonomousPercentage+";vehiclesStopped_"+autonomousPercentage+";vehiclesPassed_"+autonomousPercentage+";";

        return result + "\n";
    }


    private void createNewFile(String path) throws IOException {
        File file = new File(Paths.get(path).toAbsolutePath().toString());
        if (file.createNewFile()) {
            System.out.println("Writing to file " + Paths.get(path).toAbsolutePath());
        } else {
            System.out.println("File already exists.");
        }
    }

    private RoadData cloneRoadData(RoadData roadData) {
        RoadData copy = new RoadData();
        copy.getSimulationStatistics().setElapsedTime(roadData.getSimulationStatistics().getElapsedTime());
        List<Road> clonedRoads = new ArrayList<>();

        for (Road road : roadData.getRoads()) {
            Road clonedRoad = new Road(road.getId());
            clonedRoad.setAutonomousPercentage(road.getAutonomousPercentage());
            clonedRoad.vehicleCounter = road.vehicleCounter;
            clonedRoad.setVehiclesDeleted(road.getVehiclesDeleted());

            ArrayList<Line> clonedLines = new ArrayList<>();
            for (Line line : road.getLines()) {
                Line clonedLine = new Line(line.getId());
                clonedLine.setStopLight(null); // nie potrzebne na potrzeby tej klasy
                clonedLine.setCarsPerHour(line.getCarsPerHour());

                ArrayList<Vehicle> clonedVehicles = new ArrayList<>();
                for (Vehicle vehicle : line.getVehicles()) {
                    Vehicle clonedVehicle = new Vehicle();
                    clonedVehicle.setId(vehicle.getId());
                    clonedVehicle.setPosition(vehicle.getPosition());
                    clonedVehicle.setAcceleration(vehicle.getAcceleration());
                    clonedVehicle.setSpeed(vehicle.getSpeed());
                    clonedVehicle.setObjectType(vehicle.getObjectType());

                    clonedVehicles.add(clonedVehicle);
                }

                clonedLine.getVehicles().addAll(clonedVehicles);

                clonedLines.add(clonedLine);
            }
            clonedRoad.getLines().addAll(clonedLines);

            clonedRoads.add(clonedRoad);
        }
        copy.setRoads(clonedRoads);


        return copy;
    }
}
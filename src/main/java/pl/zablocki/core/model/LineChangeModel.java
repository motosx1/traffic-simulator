package pl.zablocki.core.model;

import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.Road;
import pl.zablocki.core.roadobjects.Vehicle;
import pl.zablocki.core.roadobjects.ObjectType;

import java.util.*;
import java.util.stream.Collectors;

public class LineChangeModel {
    public static void decideToChangeLine(double elapsedTime, Road road) {
        Map<Line, List<Vehicle>> newLineVehiclesMap = new HashMap<>();
        for (Line line : road.getLines()) {
            List<Vehicle> vehiclesInTheLine = line.getVehicles();
            List<Line> availableLines = getAvailableLines(line, road.getLines());

            List<Vehicle> carsToDelete = new ArrayList<>();

            for (Vehicle vehicle : vehiclesInTheLine) {
                Line bestLine = vehicle.getBestAvailableLine(line, availableLines);
                if (bestLine != null && !bestLine.equals(line) && passedSomeTimeFromLastLineChange(vehicle.getLastLineChange(), elapsedTime)) {
                    vehicle.setLastLineChange(elapsedTime);
                    carsToDelete.add(vehicle);
                    putVehicleToMap(newLineVehiclesMap, bestLine, vehicle);
                }
            }

            deleteFromList(vehiclesInTheLine, carsToDelete);
        }
        for (Map.Entry<Line, List<Vehicle>> entry : newLineVehiclesMap.entrySet()) {
            Line line = entry.getKey();
            line.getVehicles().addAll(entry.getValue());
        }

        fixObjectsInFrontAfterLineChanges(road);

    }

    private static boolean passedSomeTimeFromLastLineChange(double lastLineChange, double elapsedTime) {
        return Math.abs(elapsedTime-lastLineChange) > 5;
    }

    private static void fixObjectsInFrontAfterLineChanges(Road road) {
        for (Line line : road.getLines()) {
            for (Vehicle vehicle : line.getVehicles()) {
                if( vehicle.getObjectInFront() == null || vehicle.getObjectInFront().getObjectType() != ObjectType.STOPLIGHT ){
                    vehicle.setObjectInFront(vehicle.findVehicleInFront(line.getVehicles()));
                }
            }
        }
    }

    private static void deleteFromList(List<Vehicle> deleteFrom, List<Vehicle> elementsToBeDeleted) {
        deleteFrom.removeAll(elementsToBeDeleted);
    }

    private static void putVehicleToMap(Map<Line, List<Vehicle>> newLineVehiclesMap, Line bestLine, Vehicle vehicle) {
        newLineVehiclesMap.putIfAbsent(bestLine, new ArrayList<>());
        newLineVehiclesMap.get(bestLine).add(vehicle);
    }

    private static List<Line> getAvailableLines(Line currentLine, List<Line> lines) {
        return lines.stream().filter(line -> !line.equals(currentLine) && ( currentLine.getId()+1 == line.getId() || currentLine.getId()-1 == line.getId()) ).collect(Collectors.toList());
    }
}

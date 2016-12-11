package pl.zablocki.core.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.zablocki.core.model.AccelerationModel;
import pl.zablocki.core.model.GippsModel;
import pl.zablocki.core.road.Line;
import pl.zablocki.core.road.RoadObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
public class Vehicle extends RoadObject {

    @Getter
    private Integer id;
    @Setter
    private RoadObject objectInFront;
    @Setter
    @Getter
    private AccelerationModel accelerationModel;
    @Setter
    @Getter
    private double lastLineChange;

    public Vehicle(Integer id, RoadObject vehicleParams, Vehicle objectInFront) {
        this.id = id;
        this.objectInFront = objectInFront;
        this.accelerationModel = new GippsModel();
        setVehicleParams(vehicleParams);
    }

    private void setVehicleParams(RoadObject vehicleParams) {
        setAcceleration(vehicleParams.getAcceleration());
        setMaxAcceleration(vehicleParams.getMaxAcceleration());
        setSpeed(vehicleParams.getSpeed());
        setMaxSpeed(vehicleParams.getMaxSpeed());
        setBreakingRappidness(vehicleParams.getBreakingRappidness());
        setPosition(vehicleParams.getPosition());
        setObjectType(vehicleParams.getObjectType() == null ? ObjectType.NORMAL : vehicleParams.getObjectType());
    }

    public void updateParameters(double timeElapsed) {
        double calculatedNewAcc = calcAcc();
        setAcceleration(calculatedNewAcc);
        double speed = getSpeed() + getAcceleration() * timeElapsed;
        speed = validateSpeed(speed);
        setSpeed(speed);
        double newPosition = getPosition() + (getSpeed() * timeElapsed) + (getAcceleration() * Math.sqrt(timeElapsed) * 0.5);
        setPosition(newPosition);
    }

    private double calcAcc() {
        double s = getDistanceToObject(getObjectInFront());
        double v = getSpeed();
        double dv = getRelativeSpeed();
        double accLead = getObjectsInFrontAcc();
        double tLocal = 1;
        double v0Local = getMaxSpeed();
        double aLocal = getMaxAcceleration();
        double bParam = getBreakingRappidness();

        // actual Gipps formula
        return accelerationModel.acc(s, v, dv, accLead, tLocal, v0Local, aLocal, bParam, getMinimumGap());

    }

    private double validateSpeed(double speed) {
        double result = speed;
        if (speed < 0) {
            result = 0;
        } else if (speed > getMaxSpeed()) {
            result = getMaxSpeed();
        }
        return result;
    }

    private double getRelativeSpeed() {
        if (objectInFront != null) {
            return getSpeed() - objectInFront.getSpeed();
        }
        return 0;
    }

    private double getObjectsInFrontAcc() {
        return objectInFront == null ? getAcceleration() : objectInFront.getAcceleration();
    }

    private double getDistanceToObject(RoadObject objectInFront) {
        if (objectInFront != null) {
            return Math.abs(objectInFront.getPosition() - this.getPosition()) - objectInFront.getLength();
        }
        return 10000;
    }

    private double getMinimumGap() {
        if (getObjectType() == ObjectType.AUTONOMUS) {
            if (isVehicleVeryClose()) {
                return 1;
            }
        }
        return 2;
    }

    private boolean isVehicleVeryClose() {
        return objectInFront != null && getDistanceToObject(objectInFront) < 1;
    }

    public RoadObject getObjectInFront() {
        return objectInFront != null ? objectInFront : null;
    }

    public Vehicle findVehicleInFront(List<Vehicle> vehiclesInTheLine) {
        return vehiclesInTheLine.stream()
                .filter(vehicle -> vehicle.getPosition() > this.getPosition())
                .min((o1, o2) -> (int) (o1.getPosition() - o2.getPosition()))
                .orElse(null);
    }

    public Vehicle findVehicleBehind(List<Vehicle> vehiclesInTheLine) {
        return vehiclesInTheLine.stream()
                .filter(vehicle -> vehicle.getPosition() < this.getPosition())
                .max((o1, o2) -> (int) (o1.getPosition() - o2.getPosition()))
                .orElse(null);
    }

    public Line getBestAvailableLine(Line line, List<Line> availableLines) {
        double currentAcc = simulateCalcAcc(line.getVehicles());
        Map<Line, Double> lineAccDifferenceMap = new HashMap<>();
        for (Line availableLine : availableLines) {
            double accOnDifferentLine = simulateCalcAcc(availableLine.getVehicles());
            double accDifference = accOnDifferentLine - currentAcc;
            if (accDifferenceIsSufficient(accDifference) && canChangeToTheLine(availableLine)) {
                lineAccDifferenceMap.put(availableLine, accDifference);
            }
        }

        Map.Entry<Line, Double> bestLineEntry = lineAccDifferenceMap.entrySet().stream().max((o1, o2) -> (int) (o1.getValue() - o2.getValue())).orElse(null);
        Line bestLine = null;
        if (bestLineEntry != null) {
            bestLine = bestLineEntry.getKey();
        }

        return bestLine;
    }

    private boolean canChangeToTheLine(Line availableLine) {
        Vehicle vehicleBehind = findVehicleBehind(availableLine.getVehicles());
        Vehicle vehicleInFront = findVehicleInFront(availableLine.getVehicles());
        if (canJumpInFrontOfBehindObject(vehicleBehind) && getDistanceToObject(vehicleInFront) > getLength() * 0.1) {
            return true;
        }
        return false;
    }

    private boolean canJumpInFrontOfBehindObject(Vehicle vehicleBehind) {
        if (vehicleBehind == null) {
            return true;
        }
        return getDistanceToObject(vehicleBehind) / Math.abs(vehicleBehind.getSpeed() - this.getSpeed()) > 0.5;
    }

    private boolean accDifferenceIsSufficient(double accDifference) {
        return accDifference > 1;
    }

    private double simulateCalcAcc(List<Vehicle> vehiclesInTheLine) {
        Vehicle vehicleInFront = findVehicleInFront(vehiclesInTheLine);
        double s = simulateGetDistanceToFrontObject(vehicleInFront);
        double v = getSpeed();
        double dv = simulateGetRelativeSpeed(vehicleInFront);
        double accLead = simulateGetObjectsInFrontAcc(vehicleInFront);
        double tLocal = 1;
        double v0Local = getMaxSpeed();
        double aLocal = getMaxAcceleration();

        // actual Gipps formula
        return accelerationModel.acc(s, v, dv, accLead, tLocal, v0Local, aLocal, getBreakingRappidness(), getMinimumGap());

    }


    private double simulateGetDistanceToFrontObject(RoadObject object) {
        if (object != null) {
            return Math.abs(object.getPosition() - this.getPosition()) - object.getLength();
        }
        return 10000;
    }

    private double simulateGetRelativeSpeed(RoadObject object) {
        if (object != null) {
            return getSpeed() - object.getSpeed();
        }
        return 0;
    }

    private double simulateGetObjectsInFrontAcc(RoadObject object) {
        return object == null ? getAcceleration() : object.getAcceleration();
    }

}
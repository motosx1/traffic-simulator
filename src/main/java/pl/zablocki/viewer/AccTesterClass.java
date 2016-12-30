package pl.zablocki.viewer;

import pl.zablocki.core.model.IDModel;
import pl.zablocki.core.roadobjects.ObjectType;
import pl.zablocki.core.roadobjects.RoadObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;


public class AccTesterClass extends RoadObject {

    public static void main(String[] args) {

        AccTesterClass main = new AccTesterClass();
        main.run();
    }

    private void run() {
        double i = 0;
        int iMax = 40;

        setParams();
        Map<Double, Double> speedNormal = new TreeMap<>();
        Map<Double, Double> accNormal = new TreeMap<>();
        Map<Double, Double> positionNormal = new TreeMap<>();
        countParameters(iMax, speedNormal, accNormal, positionNormal);


        Map<Double, Double> speedAutonomus = new TreeMap<>();
        Map<Double, Double> accAutonomus = new TreeMap<>();
        Map<Double, Double> positionAutonomus = new TreeMap<>();
        setObjectType(ObjectType.AUTONOMOUS);
        setParams();

        countParameters(iMax, speedAutonomus, accAutonomus, positionAutonomus);

        writeDataToFile(positionNormal, speedNormal, accNormal, "normal");
        writeDataToFile(positionAutonomus, speedAutonomus, accAutonomus, "autonomous");
        System.out.println("koniec");
    }

    private void countParameters(int iMax, Map<Double, Double> speedNormal, Map<Double, Double> accNormal, Map<Double, Double> positionNormal) {
        double i = 0;
        while (i <= iMax) {
            updateParameters(0.2);
            speedNormal.put(i, getSpeed());
            accNormal.put(i, getAcceleration());
            positionNormal.put(i, getPosition());
            i += 0.2;
        }
    }

    private void updateParameters(double timeElapsed) {
        double calculatedNewAcc = calcAcc();
        setAcceleration(calculatedNewAcc);
        double speed = getSpeed() + getAcceleration() * timeElapsed;
        speed = validateSpeed(speed);
        setSpeed(speed);
        double newPosition = getPosition() + (getSpeed() * timeElapsed) + (getAcceleration() * Math.sqrt(timeElapsed) * 0.5);
        setPosition(newPosition);
    }

    private double calcAcc() {
        double s = getDistanceToObject();
        double v = getSpeed();
        double dv = getSpeed();
        double accLead = 0;
        double v0Local = getMaxSpeed();
        double aLocal = getMaxAcceleration();
        double bParam = getBreakingRapidness();

        return getAccelerationModel().acc(s, v, dv, accLead, v0Local, aLocal, bParam, 2);

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

    private double getDistanceToObject() {
        return Math.abs(300 - this.getPosition());
    }

    private void setParams() {
        setAccelerationModel(new IDModel());
        setAcceleration(2.2);
        setMaxAcceleration(2.2);
        setSpeed(35);
        setMaxSpeed(40);
        setPosition(0);
    }

    private String createFileContent(Map<Double, Double> positionMap, Map<Double, Double> speedMap, Map<Double, Double> accelerationMap) {
        String result = "";

        result += "time;position;speed;acceleration\n";

        for (Map.Entry<Double, Double> entry : positionMap.entrySet()) {
            Double time = entry.getKey();
            Double position = entry.getValue();
            Double speed = speedMap.get(time);
            Double acceleration = accelerationMap.get(time);

            DecimalFormat df = new DecimalFormat("#.##");
            String timeString = df.format(time);

            result += timeString + ";" + position + ";" + speed + ";" + acceleration + "\n";
        }

        return result;
    }

    private void writeDataToFile(Map<Double, Double> position, Map<Double, Double> speed, Map<Double, Double> acceleration, String type) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formatDateTime = LocalDateTime.now().format(formatter);

        String path = "results/idm-result-" + formatDateTime + "_" + type + ".csv";

        String text = createFileContent(position, speed, acceleration);

        try {
            createNewFile(path);
            Files.write(Paths.get(path), text.getBytes());
        } catch (IOException e) {
            System.err.println("Failed to save a file!");
            e.printStackTrace();
        }
    }

    private void createNewFile(String path) throws IOException {
        File file = new File(Paths.get(path).toAbsolutePath().toString());
        if (file.createNewFile()) {
            System.out.println("Writing to file " + Paths.get(path).toAbsolutePath());
        } else {
            System.out.println("File already exists.");
        }
    }

}

package pl.zablocki.core.road;

import pl.zablocki.core.vehicle.StopLight;
import pl.zablocki.core.vehicle.Vehicle;

public class StopLightsEngine {
    public static boolean isVehicleInRange(Vehicle vehicle, StopLight stopLight) {
        double notificationPoint = stopLight.getPosition() - stopLight.getNotifyRadius();
        double vehicleDistance = vehicle.getPosition();
        return notificationPoint >= vehicleDistance && notificationPoint - vehicleDistance < vehicle.getSpeed();
    }

    public static void changeLight(StopLight stopLight, double dt, double elapsedTime) {
        boolean hasChanged = false;

        if (stopLight.isRed() && (elapsedTime % stopLight.getRedLightTimeSec() < dt)) {
            stopLight.setGreen();
            stopLight.setBroadcastingGreen(true);
            hasChanged = true;
        }
        if (!hasChanged && stopLight.isGreen() && (elapsedTime % stopLight.getGreenLightTimeSec() < dt)) {
            stopLight.setRed();
            stopLight.setBroadcastingRed(true);
        }
    }
}

package pl.zablocki.core.roadnetwork;

import pl.zablocki.core.vehicle.StopLights;
import pl.zablocki.core.vehicle.Vehicle;

public class StopLightsEngine {
    public static boolean isVehicleInRange(Vehicle vehicle, StopLights stopLights) {
        double notificationPoint = stopLights.getDistance() - stopLights.getNotifyRadius();
        double vehicleDistance = vehicle.getDistance();
        return notificationPoint >= vehicleDistance && notificationPoint - vehicleDistance < vehicle.getSpeed();
    }

    public static void changeLight(StopLights stopLights, double dt, double elapsedTime) {
        boolean hasChanged = false;

        if( stopLights.isRed() && (elapsedTime % stopLights.getRedLightTimeSec() < dt) ){
            stopLights.setGreen();
            stopLights.setBroadcastingGreen(true);
            hasChanged = true;
        }
        if( !hasChanged && stopLights.isGreen() && (elapsedTime % stopLights.getGreenLightTimeSec() < dt) ){
            stopLights.setRed();
            stopLights.setBroadcastingRed(true);
        }
    }
}

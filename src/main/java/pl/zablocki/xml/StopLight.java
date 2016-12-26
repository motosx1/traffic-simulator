package pl.zablocki.xml;

import lombok.Setter;
import lombok.Getter;

class StopLight {
    @Getter
    @Setter
    private int greenLightTimeSec = 10;
    @Getter
    @Setter
    private int redLightTimeSec = 10;
    @Getter
    @Setter
    private int position;

    public StopLight() {
    }

    public StopLight(int position) {
        this.position = position;
    }
}

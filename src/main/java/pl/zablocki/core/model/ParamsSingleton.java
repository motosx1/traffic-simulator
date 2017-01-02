package pl.zablocki.core.model;


import lombok.Getter;
import lombok.Setter;

public class ParamsSingleton {
    private static ParamsSingleton instance = null;

    @Getter @Setter
    private double threadSleep = 10;
    @Getter @Setter
    private double dt = 0.2;

    private ParamsSingleton() {
    }

    public static ParamsSingleton getInstance() {
        if (instance == null) {
            instance = new ParamsSingleton();
        }
        return instance;
    }

}
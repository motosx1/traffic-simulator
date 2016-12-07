package pl.zablocki.core.model;


import lombok.Getter;

public class ParamsSingleton {

    private static ParamsSingleton instance = null;

    @Getter
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
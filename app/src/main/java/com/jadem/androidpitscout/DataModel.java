package com.jadem.androidpitscout;

/**
 * Created by jadem on 4/1/2018.
 */

public class DataModel {

    private String name;
    private Integer number;

    public DataModel() {
        //Allows usage as Firebase POJO.
    }

    public DataModel(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public String getFormattedString() {
        return number + " - " + name;
    }
}

package com.jadem.androidpitscout;

/**
 * Created by jadem on 4/1/2018.
 */

public class DataModel {

    private String name;
    private Integer teamNumber;

    public DataModel(String name, Integer teamNumber) {
        this.name = name;
        this.teamNumber = teamNumber;
    }

    public String getName() {
        return name;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public String getFormattedString() {
        return teamNumber + " - " + name;
    }
}

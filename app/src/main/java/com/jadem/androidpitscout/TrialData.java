package com.jadem.androidpitscout;

import android.util.Log;

/**
 * Created by niraq on 3/26/2018.
 */

public class TrialData {

    private double time;
    private boolean outcome;

    public TrialData(double time, boolean outcome) {
        this.time = time;
        this.outcome = outcome;
    }

    public void updateData(float newTime, boolean newOutcome) {
        time = newTime;
        outcome = newOutcome;
    }

    public double getTime() {
        return  time;
    }

    public boolean getOutcome() {
        return outcome;
    }

    public String getTimeString() {
        int m = (int) (time / 60);
        int s = (int) (time - m * 60);
        int ms = (int) ((time - m * 60 - s) * 100);
        String mm = m < 10 ? "0"+m: m+"";
        String ss = s < 10 ? "0"+s: s+"";
        String msms = ms < 10 ? "0"+ms: ms+"";
        return (mm+":"+ss+"."+msms);
    }

    public String getOutcomeString() {
        return String.valueOf(outcome);
    }

}
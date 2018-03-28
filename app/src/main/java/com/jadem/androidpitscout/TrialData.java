package com.jadem.androidpitscout;

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
        //TODO: make sure this is a properly formatted string
        int h = (int)(time /3600000);
        int m = (int)(time - h*3600000)/60000;
        int s= (int)(time - h*3600000 - m*60000)/1000 ;
        int ms = (int)(time - h*3600000 - m*60000 - s*1000)/10;
        String mm = m < 10 ? "0"+m: m+"";
        String ss = s < 10 ? "0"+s: s+"";
        String msms = ms < 10 ? "0"+ms: ms+"";
        return (mm+":"+ss+"."+msms);
    }

    public String getOutcomeString() {
        return String.valueOf(outcome);
    }

}
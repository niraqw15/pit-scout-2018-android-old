package com.jadem.androidpitscout;

/**
 * Created by niraq on 3/26/2018.
 */

public class TrialData {

    private float time;
    private boolean outcome;

    public TrialData(float time, boolean outcome) {
        this.time = time;
        this.outcome = outcome;
    }

    public void updateData(float newTime, boolean newOutcome) {
        time = newTime;
        outcome = newOutcome;
    }

    public float getTime() {
        return  time;
    }

    public boolean getOutcome() {
        return outcome;
    }

}
package com.jadem.androidpitscout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by niraq on 3/15/2018.
 */

public class TimerActivity extends AppCompatActivity {

    private boolean isRamp, timerRunning, success;
    private int teamNumber;
    private Button toggleButton;
    private CustomChronometer timerView;
    private Switch timerTypeSwitch;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ValueEventListener trialEventListener;
    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        teamNumber = 1;//TODO: Temporary for testing, remove when done

        timerRunning = false;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Teams").child("" + teamNumber); //TODO: Receive team number before doing this!

        timerView = (CustomChronometer) findViewById(R.id.timerView);
        timerView.setText("00:00.00");
        timerView.setOnChronometerTickListener(new CustomChronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(CustomChronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000 - m*60000)/1000 ;
                int ms = (int)(time - h*3600000 - m*60000 - s*1000)/10;
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                String msms = ms < 10 ? "0"+ms: ms+"";
                cArg.setText(mm+":"+ss+"."+msms);
            }
        });

        toggleButton = (Button) findViewById(R.id.toggleTimerButton);
        timerTypeSwitch = (Switch) findViewById(R.id.timerSwitch);
        isRamp = timerTypeSwitch.isChecked();
        timerTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRamp = isChecked;
                //TODO: Change what the listview displays (and update it?)
            }
        });

        trialEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Messages").getValue().equals(null)) {
                    //TODO: Complete this
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "ChatRoomEventListener Cancelled");
                Toast connectionErrorToast = Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT);
                connectionErrorToast.setGravity(Gravity.CENTER, 0, 0);
                connectionErrorToast.show();
            }
        };
    }

    public void toggleTimer(View view) {
        if(timerRunning) {
            //Turns timer off.
            time =  SystemClock.elapsedRealtime() - timerView.getBase(); //Stores time in milliseconds.
            timerView.stop();
            timerRunning = false;
            toggleButton.setText("Start");
        } else {
            //Turns timer on.
            time = 0;
            timerView.setBase(SystemClock.elapsedRealtime());
            timerView.start();
            timerRunning = true;
            toggleButton.setText("Stop");
        }
    }

    public void confirmTimer(View view) {
        if(!timerRunning && time != 0) {
            isRamp = timerTypeSwitch.isChecked();

            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RelativeLayout confirmDialog = (RelativeLayout) layoutInflater.inflate(R.layout.confirm_dialog, null);
            final TextView questionView = (TextView)confirmDialog.findViewById(R.id.questionView);
            final EditText distanceEditText = (EditText)confirmDialog.findViewById(R.id.distanceView);
            final EditText lengthEditText = (EditText)confirmDialog.findViewById(R.id.lengthView);

            String question = "What was the " + (isRamp ? "ramp " : "drive ") + "distance travelled?";
            questionView.setText(question);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(confirmDialog)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO: Send values to firebase (distances, times, and outcome)
                            //TODO: Add calculation for true or false

                            String distanceString = distanceEditText.getText().toString();
                            String lengthString = lengthEditText.getText().toString();

                            float distance = 0, length = 0;
                            try {
                                distance = Float.parseFloat(distanceString);
                                length = Float.parseFloat(lengthString);

                            } catch (NumberFormatException e) {
                                Toast decimalToast = Toast.makeText(getApplicationContext(), "Invalid numbers (check for extra decimals)", Toast.LENGTH_SHORT);
                                decimalToast.setGravity(Gravity.CENTER, 0, 0);
                                decimalToast.show();
                                return;
                                //TODO: Re-open dialog with data
                            }

                            float deciTime = time;
                            deciTime = deciTime / 1000; //Stores time in seconds.

                            double ratio = 7.4; //This is the treadmill ratio.
                            boolean outcome = distance > (ratio - length);

                            //TODO: write to firebase as an array
                            myRef.child("pit" + (isRamp ? "Ramp" : "Drive") + "Time")/*.child(arrayNum)*/.setValue(deciTime);
                            myRef.child("pit" + (isRamp ? "Ramp" : "Drive") + "TimeOutcome")/*.child(arrayNum)*/.setValue(outcome);

                            time = 0;
                            timerView.setText("00:00.00");

                            Toast successToast = Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT);
                            successToast.setGravity(Gravity.CENTER, 0, 0);
                            successToast.show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }

    public void cancelTimer(View view) {
        timerView.stop();
        time = 0;
        timerView.setText("00:00.00");
        timerRunning = false;
    }

}

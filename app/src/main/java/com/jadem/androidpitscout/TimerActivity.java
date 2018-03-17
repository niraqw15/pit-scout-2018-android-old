package com.jadem.androidpitscout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
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

    private boolean isRamp, timerRunning;
    private int teamNumber;
    private Button toggleButton;
    private Switch timerTypeSwitch;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ValueEventListener trialEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        teamNumber = 1;//TODO: Temporary for testing, remove when done

        timerRunning = false;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Teams").child("" + teamNumber); //TODO: Receive team number before doing this!

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
            toggleButton.setText("Start");
        } else {
            //Turns timer on.
            toggleButton.setText("Stop");
        }
        timerRunning = !timerRunning;
    }

    public void confirmTimer(View view) {
        //TODO: Complete
    }

    public void cancelTimer(View view) {
        //TODO: Complete
    }

}

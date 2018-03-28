package com.jadem.androidpitscout;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by jadem on 3/18/2018.
 */

public class ViewTeam extends AppCompatActivity {

    EditText SEALsNotesEditText;
    Button TimerButton;
    TextView TeamName;
    TextView SEALsNotesTextView;

    DatabaseReference dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SEALsNotesEditText = (EditText) findViewById(R.id.SEALsNotesEditText);
        SEALsNotesTextView = (TextView) findViewById(R.id.SEALsNotesTextView);
        TimerButton = (Button) findViewById(R.id.timer);
        TeamName = (TextView) findViewById(R.id.teamNameAndNumber);

        SEALsNotesEditText.setFocusable(true);



        //TODO: When the TimerActivity is added, uncomment
        /*TimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewTeam.this, TimerActivity.class));
            }
        });
    }*/
    }

}
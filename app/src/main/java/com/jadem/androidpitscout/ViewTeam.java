package com.jadem.androidpitscout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    Context context;

    DatabaseReference dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = this;

        SEALsNotesEditText = (EditText) findViewById(R.id.SEALsNotesEditText);
        SEALsNotesTextView = (TextView) findViewById(R.id.SEALsNotesTextView);
        TimerButton = (Button) findViewById(R.id.timer);
        TeamName = (TextView) findViewById(R.id.teamNameAndNumber);

        SEALsNotesEditText.setFocusable(true);
    }

    public void openTimer(View view) {
        Intent intent = new Intent(context, TimerActivity.class);
        int teamNumber = 1; //TODO: Temp
        intent.putExtra("teamNumber", teamNumber);
        startActivity(intent);
    }
}
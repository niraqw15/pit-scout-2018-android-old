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

    int teamNumber;
    EditText SEALsNotesEditText;
    Button timerButton;
    TextView teamName;
    TextView SEALsNotesTextView;
    Context context;

    DatabaseReference dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = this;
        getExtras();

        SEALsNotesEditText = (EditText) findViewById(R.id.SEALsNotesEditText);
        SEALsNotesTextView = (TextView) findViewById(R.id.SEALsNotesTextView);
        timerButton = (Button) findViewById(R.id.timer);
        teamName = (TextView) findViewById(R.id.teamNameAndNumber);

        String teamNameString = "" + teamNumber;
        teamName.setText(teamNameString);

        SEALsNotesEditText.setFocusable(true);
    }

    public void openTimer(View view) {
        Intent intent = new Intent(context, TimerActivity.class);
        intent.putExtra("teamNumber", teamNumber);
        startActivity(intent);
    }

    private void getExtras() {
        Intent previous = getIntent();
        teamNumber = previous.getIntExtra("teamNumber", 0);
    }
}
package com.jadem.androidpitscout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by niraq on 3/15/2018.
 */

public class TimerActivity extends AppCompatActivity {

    private boolean isRamp, timerRunning, success;
    private int teamNumber;
    private Button toggleButton;
    private CustomChronometer timerView;
    private Switch timerTypeSwitch;
    private ListView timerListView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Map<String, List<TrialData>> trialListMap;
    private Map<String, Long> trialCountMap;
    private ValueEventListener trialEventListener;
    private long time = 0;
    private BaseAdapter timerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getExtras();

        timerRunning = false;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Teams").child("" + teamNumber);

        timerView = (CustomChronometer) findViewById(R.id.timerView);
        timerView.setText("00:00.00");
        timerView.setOnChronometerTickListener(new CustomChronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(CustomChronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int)(time /3600000);
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
                timerAdapter.notifyDataSetChanged();
            }
        });

        //Allows the adapter to work without data
        trialListMap = new HashMap<>();
        trialListMap.put("Ramp", new ArrayList<TrialData>());
        trialListMap.put("Drive", new ArrayList<TrialData>());

        trialCountMap = new HashMap<>();
        trialCountMap.put("Ramp", Long.valueOf(0));
        trialCountMap.put("Drive", Long.valueOf(0));

        timerAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return trialListMap.get(isRamp ? "Ramp" : "Drive").size();
            }

            //TODO: Possibly make these methods actually do something.
            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override //Partially modelled after http://stackoverflow.com/questions/35761897/how-do-i-make-a-relative-layout-an-item-of-my-listview-and-detect-gestures-over
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater layoutInflater;
                ViewHolder listViewHolder;

                if(convertView == null){
                    layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.layout_timer_list_item, parent, false);

                    listViewHolder = new ViewHolder();
                    listViewHolder.trialView = (TextView) convertView.findViewById(R.id.trialView);
                    listViewHolder.timeView = (TextView) convertView.findViewById(R.id.timeView);
                    listViewHolder.outcomeView = (TextView) convertView.findViewById(R.id.outcomeView);
                    convertView.setTag(listViewHolder);
                } else {
                    listViewHolder = (ViewHolder) convertView.getTag();
                }

                String posString = "" + (position + 1);
                listViewHolder.trialView.setText(posString);
                String timeString = trialListMap.get(isRamp ? "Ramp" : "Drive").get(position).getTimeString();
                listViewHolder.timeView.setText(timeString);
                String outcomeString = trialListMap.get(isRamp ? "Ramp" : "Drive").get(position).getOutcomeString();
                listViewHolder.outcomeView.setText(outcomeString);

                return convertView;
            }
        };
        timerListView = (ListView) findViewById(R.id.timesList);
        timerListView.setAdapter(timerAdapter);

        trialEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: Should these be final?
                String dTime = "pitDriveTime";
                String rTime = "pitRampTime";
                String dOut = "pitDriveTimeOutcome";
                String rOut = "pitRampTimeOutcome";

                if((dataSnapshot.hasChild(dTime) && dataSnapshot.hasChild(dOut)) || (dataSnapshot.hasChild(rTime) && dataSnapshot.hasChild(rOut))) {

                    List<TrialData> rampList = new ArrayList<TrialData>();

                    //Fills ramp list.
                    for(int trialNum = 0; trialNum < dataSnapshot.child(rTime).getChildrenCount(); trialNum++) {

                        double time = 0;
                        boolean outcome = false;
                        if(dataSnapshot.child(rTime).hasChild("" + trialNum) && dataSnapshot.child(rOut).hasChild("" + trialNum)) {
                            try {
                                time = (Double) dataSnapshot.child(rTime).child("" + trialNum).getValue();
                                outcome = (Boolean) dataSnapshot.child(rOut).child("" + trialNum).getValue();
                            } catch (NullPointerException npe) {
                                Log.e("(NullPointerException", "Incorrect data type for team: " + teamNumber + ", trial: " + trialNum + ", type: ramp");
                            }
                        }

                        //If time == 0, the data for that trial is invalid.
                        TrialData data = new TrialData(time, outcome);
                        rampList.add(data);

                    }

                    List<TrialData> driveList = new ArrayList<TrialData>();

                    //Fills drive list.
                    for(int trialNum = 0; trialNum < dataSnapshot.child(dTime).getChildrenCount(); trialNum++) {

                        double time = 0;
                        boolean outcome = false;
                        if(dataSnapshot.child(dTime).hasChild("" + trialNum) && dataSnapshot.child(dOut).hasChild("" + trialNum)) {
                            try {
                                time = (Double) dataSnapshot.child(dTime).child("" + trialNum).getValue();
                                outcome = (Boolean) dataSnapshot.child(dOut).child("" + trialNum).getValue();
                            } catch (NullPointerException npe) {
                                Log.e("(NullPointerException", "Incorrect data type for team: " + teamNumber + ", trial: " + trialNum + ", type: drive");
                            }
                        }

                        //If time == 0, the data for that trial is invalid.
                        TrialData data = new TrialData(time, outcome);
                        driveList.add(data);

                    }

                    trialListMap = new HashMap<>();
                    trialListMap.put("Ramp", rampList);
                    trialListMap.put("Drive", driveList);

                    trialCountMap = new HashMap<>();
                    trialCountMap.put("Ramp", dataSnapshot.child(rTime).getChildrenCount());
                    trialCountMap.put("Drive", dataSnapshot.child(dTime).getChildrenCount());

                    timerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "trialEventListener Cancelled");
                Toast connectionErrorToast = Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT);
                connectionErrorToast.setGravity(Gravity.CENTER, 0, 0);
                connectionErrorToast.show();
            }
        };
        myRef.addValueEventListener(trialEventListener);
    }

    public void toggleTimer(View view) {
        if(timerRunning) {
            //Turns timer off.
            time =  SystemClock.elapsedRealtime() - timerView.getBase(); //Stores time in milliseconds.
            timerView.stop();
            timerRunning = false;
            toggleButton.setText("Start");
            int h = (int)(time /3600000);
            int m = (int)(time - h*3600000)/60000;
            int s= (int)(time - h*3600000 - m*60000)/1000 ;
            int ms = (int)(time - h*3600000 - m*60000 - s*1000)/10;
            String mm = m < 10 ? "0"+m: m+"";
            String ss = s < 10 ? "0"+s: s+"";
            String msms = ms < 10 ? "0"+ms: ms+"";
            timerView.setText(mm+":"+ss+"."+msms);
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

                            double deciTime = time;
                            deciTime = deciTime / 1000; //Stores time in seconds.

                            double ratio = 7.4; //This is the treadmill ratio. //TODO: Make this variable
                            boolean outcome = distance > (ratio - length);

                            String typeString = isRamp ? "Ramp" : "Drive";
                            myRef.child("pit" + typeString + "Time").child("" + trialCountMap.get(typeString)).setValue(deciTime);
                            myRef.child("pit" + typeString + "TimeOutcome").child("" + trialCountMap.get(typeString)).setValue(outcome);

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

    private void getExtras() {
        Intent previous = getIntent();
        teamNumber = previous.getIntExtra("teamNumber", 0);
    }

}

//For temporarily holding values of each chat box.
class ViewHolder {
    TextView trialView;
    TextView timeView;
    TextView outcomeView;
}

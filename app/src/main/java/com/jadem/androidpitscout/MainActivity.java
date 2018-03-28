package com.jadem.androidpitscout;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> searchAdapter;
    Context context;
    Button tempButton;
    EditText searchBar;
    ListView listView;
    public static FirebaseDatabase dataBase;
    public static DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference();
        context = this;

        searchBar = (EditText) findViewById(R.id.searchEditText);
        tempButton = (Button) findViewById(R.id.temporaryButton);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewTeam.class);
                intent.putExtra("teamNumber", Integer.parseInt(searchBar.getText().toString())); //TODO: Temporary for testing. Remove when ListView is finished
                startActivity(intent);
            }
        });

        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.timesList);
        listView.setAdapter(searchAdapter);
        updateListView();

    }

      public void getTeams(View view) {
        searchBar.setFocusable(false);
        updateListView();
        searchBar.setFocusableInTouchMode(true);
    }

    private void updateListView() {
        final EditText searchBar = (EditText) findViewById(R.id.searchEditText);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence Register, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchBar.getText().toString().equals("")) {
                    searchAdapter.clear();
                    searchBar.setFocusable(false);
                } else {
                    for (int i = 0; i < searchAdapter.getCount(); ) {
                        if (searchAdapter.getItem(i).startsWith((searchBar.getText().toString()).toUpperCase())){//|| adapter.getItem(i).contains((searchBar.getText().toString()).toUpperCase())) {
                            i++;
                        } else {
                            searchAdapter.remove(searchAdapter.getItem(i));
                        }
                    }
                }
                ;
            }
        });
        searchAdapter.notifyDataSetChanged();
    }
    }



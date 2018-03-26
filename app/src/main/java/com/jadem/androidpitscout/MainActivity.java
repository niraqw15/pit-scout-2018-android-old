package com.jadem.androidpitscout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> searchAdapter;
    ArrayList<String> ListOfTeams;
    ListAdapter adapter;
    String teamName;
    String teamNumber;
    String teamInput;
    Button tempButton;
    EditText searchBar;
    Activity activity;
    public static FirebaseDatabase dataBase;
    public static DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference();

        tempButton = (Button) findViewById(R.id.temporaryButton);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewTeam.class));
            }
        });
    }

    public void getTeams(View view) {
        searchBar = (EditText) findViewById(R.id.searchEditText);
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


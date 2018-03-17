package com.jadem.androidpitscout;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    EditText searchBar;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.teamsList);
        listView.setAdapter(adapter);
        updateListView();
    }

    public void getTeams(View view) {
        searchBar = (EditText) findViewById(R.id.searchEditText);
        searchBar.setFocusable(false);
        updateListView();
        searchBar.setFocusableInTouchMode(true);

    }

    private void updateListView() {

        final EditText searchBar = (EditText) findViewById(R.id.searchEditText);

    }

}




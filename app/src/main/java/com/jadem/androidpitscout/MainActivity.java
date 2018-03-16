package com.jadem.androidpitscout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
ListView listView;
ArrayAdapter<String> adapter;
EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.timesList);
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
        final File dir;
        dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!dir.mkdir()) {
            Log.i("File Info", "Failed to make Directory");

        }
    }
}

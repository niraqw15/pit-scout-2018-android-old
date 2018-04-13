package com.jadem.androidpitscout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context;
    ListView listView;
    public static FirebaseDatabase dataBase;
    public static DatabaseReference ref;
    List<DataModel> dataModelsListOriginal;
    List<DataModel> dataModelsList;
    BaseAdapter adapter;
    private DatabaseReference dataBaseReference;
    EditText searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dataBase = FirebaseDatabase.getInstance();
        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        ref = dataBase.getReference();
        context = this;

        searchBar = (EditText) findViewById(R.id.searchEditText);

        listView = (ListView) findViewById(R.id.teamsList);

        dataModelsListOriginal = new ArrayList<DataModel>();
        dataModelsList = new ArrayList<DataModel>();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return dataModelsList.size();
            }

            //TODO: Definitely make these methods actually do something.
            @Override
            public View getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override //Partially modelled after http://stackoverflow.com/questions/35761897/how-do-i-make-a-relative-layout-an-item-of-my-listview-and-detect-gestures-over
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater layoutInflater;
                TextView teamView;

                if(convertView == null){
                    layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.row_item, parent, false);

                    teamView = (TextView) convertView.findViewById(R.id.teamView);
                    convertView.setTag(teamView);
                } else {
                    teamView = (TextView) convertView.getTag();
                }

                DataModel dataModel = dataModelsList.get(position);
                teamView.setText(dataModel.getFormattedString());

                return convertView;
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                DataModel dataModel = dataModelsList.get(pos);
                String teamName = dataModel.getName();
                Integer teamNumber = dataModel.getTeamNumber();

                Intent intent = new Intent(MainActivity.this,ViewTeam.class);
                intent.putExtra("teamName", teamName);
                intent.putExtra("teamNumber", teamNumber);

                startActivity(intent);
            }
        });

        ValueEventListener teamEventListener = new ValueEventListener() {

            //TODO: This is inefficient. Replace with more efficient method later.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataModelsListOriginal = new ArrayList<DataModel>();

                for (int pos = 0; pos < 10000; pos++) {
                    if (dataSnapshot.child("Teams").hasChild("" + pos)) {
                        String name = dataSnapshot.child("Teams").child("" + pos).child("name").getValue().toString();
                        Integer number = Integer.parseInt(dataSnapshot.child("Teams").child("" + pos).child("number").getValue().toString());
                        DataModel dataModel = new DataModel(name, number);
                        dataModelsListOriginal.add(dataModel);
                    }
                }
                //TODO: Notify adapter (only if there is a new team)
                dataModelsList = new ArrayList<DataModel>(dataModelsListOriginal);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "teamEventListener Cancelled");
                Toast connectionErrorToast = Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT);
                connectionErrorToast.setGravity(Gravity.CENTER, 0, 0);
                connectionErrorToast.show();
            }
        };
        ref.addListenerForSingleValueEvent(teamEventListener);


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchString = charSequence.toString();
                createSearchList(searchString);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public void createSearchList(String string) {

        dataModelsList = new ArrayList<DataModel>(dataModelsListOriginal);
        for (int pos = dataModelsListOriginal.size() - 1; pos >= 0; pos--) {

            if(!dataModelsList.get(pos).getFormattedString().toLowerCase().contains(string.toLowerCase())) {
                dataModelsList.remove(pos);

                Log.d("dataModelsList", dataModelsList.toString());
                Log.d("dataModelsListOriginal", dataModelsListOriginal.toString());
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

}
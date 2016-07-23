package com.madDucks.wheretheulami;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

    // new Medical Science Building ?
    private final String[] ulBuildings = {"Kemmy Building", "CSIS Building", "Library Building",
            "Block A", "Block B", "Block C", "Block D", "Block E", "Sports Arena", "Schrodinger Building",
            "Foundation Building", "Students Union", "Engineering Building", "Languages Building",
            "Schumann Building", "Tierney Building", "Lonsdale Building", "PESS Building",
            "Health Sciences"};

    private String destination;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private Intent call_UL_Map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnSpinnerItemSelection();
    }

    public void addListenerOnSpinnerItemSelection() {

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ulBuildings);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = spinner.getSelectedItemPosition();
                        destination = ulBuildings[+position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );
    }

    // if click "Select Destination" button
    // see android:onClick="selectDestination" /xml
    public void selectDestination(View view) {
        // call UL_Map Activity
        call_UL_Map = new Intent(MainActivity.this, UL_Map.class);
        call_UL_Map.putExtra("destination", destination);
        startActivity(call_UL_Map);
    }

    // if click "View My Location Destination" button
    // see android:onClick="viewMyLocation" /xml
    public void viewMyLocation(View view) {
        // call UL_Map Activity
        destination = null;
        call_UL_Map = new Intent(MainActivity.this, UL_Map.class);
        call_UL_Map.putExtra("destination", destination);
        startActivity(call_UL_Map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

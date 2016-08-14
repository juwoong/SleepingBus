package kr.tamiflus.sleepingbus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BusRouteStationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_station_list);

        ListView view = (ListView) findViewById(R.id.busRouteListView);
        ArrayList<String> list = new ArrayList<>();

        for(int i=0; i<20; i++) {
            list.add("String String");
        }

        view.setAdapter(new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1, list));
    }

}

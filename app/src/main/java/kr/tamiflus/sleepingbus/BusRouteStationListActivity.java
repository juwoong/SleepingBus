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

import kr.tamiflus.sleepingbus.component.BusRouteStationAdapter;
import kr.tamiflus.sleepingbus.structs.BusStation;

public class BusRouteStationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_station_list);

        ListView view = (ListView) findViewById(R.id.busRouteListView);
        ArrayList<BusStation> list = new ArrayList<>();

        for(int i=0; i<20; i++) {
            BusStation st = new BusStation();
            st.setName("한국디지털미디어고등학교");
            st.setRegion("경기도 안산");
            st.setId("18312");
            list.add(st);
        }

        view.setNestedScrollingEnabled(true);
        BusRouteStationAdapter adapter = new BusRouteStationAdapter(getApplicationContext());
        adapter.addAll(list);

        view.setAdapter(adapter);
    }

}

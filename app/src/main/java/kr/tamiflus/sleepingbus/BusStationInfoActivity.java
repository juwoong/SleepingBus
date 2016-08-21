package kr.tamiflus.sleepingbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import kr.tamiflus.sleepingbus.component.BusStationActivityAdapter;
import kr.tamiflus.sleepingbus.structs.Bus;
import kr.tamiflus.sleepingbus.structs.BusRoute;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.utils.BusStationDBHelper;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;

public class BusStationInfoActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private BusStation departStation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("InfoActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_station_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.stationActivityRecyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);

        BusStationActivityAdapter activityAdapter = new BusStationActivityAdapter(getApplicationContext());

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(activityAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        departStation = BusStationToStrArray.arrToList(getIntent().getStringArrayExtra("departStation"));
        List<BusRoute> list = getBusRoutesByStationId(departStation.getCode());
        Log.d("InfoActivity", "list.size() == " + list.size());
        for(int i = 0; i<list.size(); i++) {
            Log.d("InfoActivity", list.get(i).toString());
        }

    }

    public List<BusRoute> getBusRoutesByStationId(String stationCode) {
        return (new BusStationDBHelper(this)).getBusRoutesByStationCode(stationCode);
    }
}

package kr.tamiflus.sleepingbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import kr.tamiflus.sleepingbus.component.HomeAdapter;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.structs.NearStation;
import kr.tamiflus.sleepingbus.structs.NearTwoStation;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView searchButton = (TextView) findViewById(R.id.searchButton);
        recyclerView = (RecyclerView) findViewById(R.id.homeRecyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);

        recyclerView.setLayoutManager(llm);

        BusStation st = new BusStation();
        st.setName("한국디지털미디어고등학교");

        NearStation nearStation = new NearStation(st);
        nearStation.distance=326;

        HomeAdapter adapter = new HomeAdapter(getApplicationContext());
        adapter.add(nearStation);

        BusStation st1 = new BusStation();
        st1.setName("와동중학교 방면");

        BusStation st2 = new BusStation();
        st2.setName("와동주민센터 방면");

        NearTwoStation two = new NearTwoStation(st1, st2);
        two.name = "한국디지털미디어고등학교";
        two.d1 = 326;
        two.d2 = 341;

        adapter.add(two);

        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GetBusStationIDActivity.class);
                startActivity(intent);
            }
        });

    }

}

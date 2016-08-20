package kr.tamiflus.sleepingbus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import kr.tamiflus.sleepingbus.component.HomeAdapter;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.structs.NearStation;
import kr.tamiflus.sleepingbus.structs.NearTwoStation;
import kr.tamiflus.sleepingbus.threads.FindNearStationThread;
import kr.tamiflus.sleepingbus.utils.LocManager;
import kr.tamiflus.sleepingbus.utils.NearestStationParser;

public class HomeActivity extends AppCompatActivity {
    public static final int UPDATE_NEARSTATION_ONE = 1;
    public static final int UPDATE_NEARSTATION_TWO = 2;
    public static final int CANNOT_FIND_CURRENTLOCATION = 3;

    RecyclerView recyclerView;
    HomeAdapter adapter;

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
        st.setName("위치 확인 중..");
        st.setDist("0");

        NearStation nearStation = new NearStation(st);

        adapter = new HomeAdapter(getApplicationContext());
        adapter.add(nearStation);

        //test
        getNearestStationByLocation();

//        BusStation st1 = new BusStation();
//        st1.setName("와동중학교 방면");
//
//        BusStation st2 = new BusStation();
//        st2.setName("와동주민센터 방면");
//
//        NearTwoStation two = new NearTwoStation(st1, st2);
//        two.name = "한국디지털미디어고등학교";
//        two.d1 = 326;
//        two.d2 = 341;
//
//        adapter.add(two);

        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GetBusStationIDActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getNearestStationByLocation() {
        FindNearStationThread thread = new FindNearStationThread(getApplicationContext(), new HomeHandler());
        thread.start();

    }

    private void updateScreenContents(NearTwoStation two) {
        adapter.clear();
        adapter.add(two);
        adapter.notifyDataSetChanged();
    }

    private void updateScreenContents(NearStation one) {
        adapter.clear();
        adapter.add(one);
        adapter.notifyDataSetChanged();
    }

    public class HomeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case UPDATE_NEARSTATION_ONE:
                    updateScreenContents(new NearStation((BusStation)msg.obj));
                    break;
                case UPDATE_NEARSTATION_TWO:
                    List<BusStation> res = (List)msg.obj;
                    NearTwoStation two = new NearTwoStation(res.get(0), res.get(1));
                    updateScreenContents(two);
                    break;
                case CANNOT_FIND_CURRENTLOCATION:
                    Log.d("D", "CANNOT find current location");
                    BusStation temp = new BusStation();
                    temp.setName("현재 위치를 찾을 수 없습니다");
                    updateScreenContents(new NearStation(temp));
                    break;
                default:
                    Log.d("ERROR", "Unexpected Handler Message");
                    System.exit(-1);
                    break;
            }
        }
    }
}

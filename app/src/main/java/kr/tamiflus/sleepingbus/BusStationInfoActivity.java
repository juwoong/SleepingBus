package kr.tamiflus.sleepingbus;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.animations.OnOffChangeListener;
import kr.tamiflus.sleepingbus.component.BusStationActivityAdapter;
import kr.tamiflus.sleepingbus.structs.Bus;
import kr.tamiflus.sleepingbus.structs.BusRoute;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.utils.BusArrivalTimeParser;
import kr.tamiflus.sleepingbus.utils.BusStationDBHelper;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;

public class BusStationInfoActivity extends AppCompatActivity {
    public static final int UPDATE_SCREEN = 0;

    RecyclerView recyclerView;
    private BusStation departStation = null;
    AppBarLayout appBarLayout;
    LinearLayout infoDetail, infoSummary;
    FloatingActionButton fab;
    public BusStationActivityAdapter activityAdapter = null;
    Handler handler = new InfoHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("InfoActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_station_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        infoDetail = (LinearLayout) findViewById(R.id.station_info_detail);
        infoSummary = (LinearLayout) findViewById(R.id.station_info_summary);

        appBarLayout.addOnOffsetChangedListener(new OnOffChangeListener(infoSummary, infoDetail));

        recyclerView = (RecyclerView) findViewById(R.id.stationActivityRecyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);

        activityAdapter = new BusStationActivityAdapter(getApplicationContext());

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(activityAdapter);

        //TODO: 버스정류장 정보 달아줄 것
        ((TextView) findViewById(R.id.stationId)).setText("정류장 ID");
        ((TextView) findViewById(R.id.stationName)).setText("정류장 이름");
        ((TextView) findViewById(R.id.stationRegion)).setText("정류장 지역번호");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아래는 애니메이션이니 지우지 말 것
                final OvershootInterpolator interpolator = new OvershootInterpolator();
                ViewCompat.setRotation(fab, 0f);
                ViewCompat.animate(fab).rotation(-360f).withLayer().setDuration(500).setInterpolator(interpolator).start();
                //TODO : 새로고침 구현하기
            }
        });

        departStation = BusStationToStrArray.arrToList(getIntent().getStringArrayExtra("departStation"));
        List<BusRoute> list = getBusRoutesByStationId(departStation.getCode());

        //debug
        Log.d("InfoActivity", "list.size() == " + list.size());
        for(int i = 0; i<list.size(); i++) {
            Log.d("InfoActivity", list.get(i).toString());
        }
        ParseTask task = new ParseTask();
        task.execute(departStation.getCode(), list, activityAdapter);


        OnOffChangeListener.startAlphaAnimation(infoSummary, 0, View.INVISIBLE);
    }

    public List<BusRoute> getBusRoutesByStationId(String stationCode) {
        return (new BusStationDBHelper(this)).getBusRoutesByStationCode(stationCode);
    }

    public class ParseTask extends AsyncTask<Object, Void, List<BusRoute>> {
        @Override
        protected List<BusRoute> doInBackground(Object... params) {
            Log.d("ASyncTask", "doInBackground()");
            String stationId = (String)params[0];
            List<BusRoute> routeList = (List<BusRoute>)(params[1]);
            BusStationActivityAdapter adapter = (BusStationActivityAdapter)params[2];
            Handler handler = (Handler)params[3];
            List<BusRoute> result;
            try {
                routeList = (new BusArrivalTimeParser()).fillRouteListByStationId(stationId, routeList);
            } catch(Exception e) { e.printStackTrace(); }

            List<Bus> busList = new ArrayList<>();
            //TODO: UI
            for(int i = 0; i<routeList.size(); i++) {
                Log.d("doInBackground", "bus : " + routeList.get(i).getBus1().toString());
                busList.add(routeList.get(i).getBus1());
            }
            adapter.addAll(busList);
//            adapter.notifyDataSetChanged();
            Message msg = new Message();
            msg.what = UPDATE_SCREEN;
            msg.obj = adapter;
            handler.sendMessage(msg);

            return routeList;
        }

    }

    private class InfoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case UPDATE_SCREEN:
                    Log.d("InfoHandler", "UPDATE_SCREEN");
                    BusStationActivityAdapter activityAdapter = (BusStationActivityAdapter)msg.obj;
                    activityAdapter.notifyDataSetChanged();
                    break;
                default:
                    Log.d("InfoHandler", "unexpected msg.what");
                    break;
            }
        }
    }
}

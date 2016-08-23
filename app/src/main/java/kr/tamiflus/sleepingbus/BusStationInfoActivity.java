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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.tamiflus.sleepingbus.animations.OnOffChangeListener;
import kr.tamiflus.sleepingbus.component.BusStationActivityAdapter;
import kr.tamiflus.sleepingbus.structs.Bus;
import kr.tamiflus.sleepingbus.structs.BusRoute;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.structs.BusTag;
import kr.tamiflus.sleepingbus.utils.BusArrivalTimeParser;
import kr.tamiflus.sleepingbus.utils.BusStationDBHelper;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;
import kr.tamiflus.sleepingbus.values.BusRouteType;

public class BusStationInfoActivity extends AppCompatActivity {
    public static final int UPDATE_SCREEN = 0;
    public static final int NEXT_ACTIVITY = 3;

    RecyclerView recyclerView;
    private BusStation departStation = null;
    AppBarLayout appBarLayout;
    LinearLayout infoDetail, infoSummary;
    FloatingActionButton fab;
    public BusStationActivityAdapter activityAdapter = null;
    public Handler handler = new InfoHandler();
    List<BusRoute> list = null;

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

        activityAdapter = new BusStationActivityAdapter(getApplicationContext(), handler);

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(activityAdapter);

        departStation = BusStationToStrArray.arrToList(getIntent().getStringArrayExtra("departStation"));
        list = getBusRoutesByStationId(departStation.getCode());

        ((TextView) findViewById(R.id.stationId)).setText(departStation.getId());
        ((TextView) findViewById(R.id.stationName)).setText(departStation.getName());
        ((TextView) findViewById(R.id.stationRegion)).setText(departStation.getRegion());
        ((TextView) findViewById(R.id.stationSmallName)).setText(departStation.getName());

        //debug
        Log.d("InfoActivity", "list.size() == " + list.size());
        for (int i = 0; i < list.size(); i++) {
            Log.d("InfoActivity", list.get(i).toString());
        }
        ParseTask task = new ParseTask();
        task.execute(departStation.getCode(), list, activityAdapter, handler);

        //Animation
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OvershootInterpolator interpolator = new OvershootInterpolator();
                ViewCompat.setRotation(fab, 0f);
                ViewCompat.animate(fab).rotation(-360f).withLayer().setDuration(500).setInterpolator(interpolator).start();
                ParseTask task = new ParseTask();
                task.execute(departStation.getCode(), list, activityAdapter, handler);
            }
        });

        OnOffChangeListener.startAlphaAnimation(infoSummary, 0, View.INVISIBLE);
    }

    public List<BusRoute> getBusRoutesByStationId(String stationCode) {
        return (new BusStationDBHelper(this)).getBusRoutesByStationCode(stationCode);
    }

    public class ParseTask extends AsyncTask<Object, Void, List<BusRoute>> {
        @Override
        protected List<BusRoute> doInBackground(Object... params) {
            Log.d("ASyncTask", "doInBackground()");
            String stationId = (String) params[0];
            List<BusRoute> routeList = (List<BusRoute>) (params[1]);
            BusStationActivityAdapter adapter = (BusStationActivityAdapter) params[2];
            Handler handler = (Handler) params[3];
            List<BusRoute> result;
            try {
                routeList = (new BusArrivalTimeParser()).fillRouteListByStationId(stationId, routeList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<Bus> busList = new ArrayList<>();
            //TODO: UI
            for (int i = 0; i < routeList.size(); i++) {
//                Log.d("doInBackground", "bus : " + routeList.get(i).getBus1().toString());
                Bus bus1 = routeList.get(i).getBus1();
                bus1.setRouteName(routeList.get(i).getRouteName());
                bus1.setRouteId(routeList.get(i).getRouteId());
                bus1.setRegionName(routeList.get(i).getRegionName());
                bus1.setRouteTypeCd(routeList.get(i).getRouteTypeCd());
                bus1.setRouteTypeName(routeList.get(i).getRouteTypeName());
                busList.add(bus1);
            }

            //debug
            Log.d("InfoActivity", "addTag Start!");
            addBusTagToList(busList);

            adapter.clear();
            adapter.addAll(busList);
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
            switch (msg.what) {
                case UPDATE_SCREEN:
                    Log.d("InfoHandler", "UPDATE_SCREEN");
                    BusStationActivityAdapter activityAdapter = (BusStationActivityAdapter) msg.obj;
                    activityAdapter.notifyDataSetChanged();
                    break;
                case NEXT_ACTIVITY:
                    Log.d("InfoHandler", "NEXT_ACTIVITY");
                    Intent intent = new Intent(BusStationInfoActivity.this, BusRouteStationListActivity.class);
                    intent.putExtra("departBus", (String[])msg.obj);
                    startActivity(intent);
                    break;
                default:
                    Log.d("InfoHandler", "unexpected msg.what");
                    break;
            }
        }
    }

    public List<Bus> addBusTagToList(List<Bus> list) {
        Comparator<Bus> comparator = new Comparator<Bus>() {
            @Override
            public int compare(Bus lhs, Bus rhs) {
                return lhs.getRouteTypeCd().compareTo(rhs.getRouteTypeCd());
            }
        };
        Collections.sort(list, comparator);

        String tmp = "-1";
        for (int i = 0; i < list.size(); i++) {
            Log.d("COMPARATOR", list.get(i).toString());
            if(!tmp.equals(list.get(i).getRouteTypeCd())) {
                String code = list.get(i).getRouteTypeCd();
                tmp = code;
                BusTag tag = new BusTag(BusRouteType.RouteTypeToString(code));
                Log.d("addBusTag", "" + i + "_code, tag : " + code + ", " + tag);
                list.add(i, tag);
                i++;
            }
        }
        return list;
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d("InfoHandler", "SET_WAITINGCIRCLE_INVISIBLE");
//        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("InfoHandler", "SET_WAITINGCIRCLE_INVISIBLE");
//        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//    }
}

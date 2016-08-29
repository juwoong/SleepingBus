package kr.tamiflus.sleepingbus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.animations.OnOffChangeListener;
import kr.tamiflus.sleepingbus.component.BusRouteStationAdapter;
import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.structs.Bus;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.threads.RouteStationListThread;
import kr.tamiflus.sleepingbus.utils.BusStationDBHelper;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;
import kr.tamiflus.sleepingbus.utils.ColorMap;

public class BusRouteStationListActivity extends AppCompatActivity {
    public static final int STATION_LIST_LOADED = 0;
    public static final int NEXT_ACTIVITY = 1;

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    BusStation departStation;
    View infoDetail, infoSummary;
    CollapsingToolbarLayout collapsingToolbarLayout;
    BusRouteStationAdapter adapter;
    Handler handler = new RouteStationListHandler();
    ArrivingBus arrivingBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_station_list);

        RecyclerView view = (RecyclerView) findViewById(R.id.busRouteListView);
        Intent intent = getIntent();
        arrivingBus = ArrivingBus.ArrayToArrivingBus(intent.getStringArrayExtra("departBus"));
        departStation = BusStationToStrArray.arrToList(intent.getStringArrayExtra("departStation"));

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        infoDetail = findViewById(R.id.route_info_detail);
        infoSummary = findViewById(R.id.route_info_summary);

        //TODO: 현 버스 노선의 종류를 받아와서 색 넣어주기.
        // TODO: 노선 종류는 arrivingBus.getRouteTypeCd()로 알 수 있음
        infoDetail.setBackgroundColor(getResources().getColor(ColorMap.byID.get(arrivingBus.getRouteTypeCd())));
        toolbar.setBackgroundColor(getResources().getColor(ColorMap.byID.get(arrivingBus.getRouteTypeCd())));
        collapsingToolbarLayout.setBackgroundColor(getResources().getColor(ColorMap.byID.get(arrivingBus.getRouteTypeCd())));

        appBarLayout.addOnOffsetChangedListener(new OnOffChangeListener(infoSummary, infoDetail));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);

        view.setLayoutManager(llm);

        OnOffChangeListener.startAlphaAnimation(infoSummary, 0, View.INVISIBLE);


        //TODO: 버스 노선 정보 입력해주기
//        ((TextView) findViewById(R.id.BusRouteInfo)).setText("경기도 안산시 시외버스"); //버스 노선 정보
        ((TextView) findViewById(R.id.BusRouteInfo)).setText(arrivingBus.getRegionName()); //버스 노선 정보
        ((TextView) findViewById(R.id.BusRouteName)).setText(arrivingBus.getRouteName()); //버스 노선 번호
        ((TextView) findViewById(R.id.BusHeadingInfo)).setText(arrivingBus.getNumOfStationsToWait() + "정류소전"); //버스 목적지 번호

        // 버스정류장 리스트 셋
//        for(int i=0; i<20; i++) {
//            BusStation st = new BusStation();
//            st.setName("한국디지털미디어고등학교");
//            st.setRegion("경기도 안산");
//            st.setId("18312");
//            list.add(st);
//        }
        String routeId = arrivingBus.getRouteId();
        RouteStationListThread thread = new RouteStationListThread(handler, routeId, this);
        thread.start();
//        List<BusStation> stations = (new BusStationDBHelper(this)).getStationsByRouteId(routeId);
//
//        list = (ArrayList<BusStation>)stations;
//        for(int i = 0; i<stations.size(); i++) {
//            Log.d("filledList", "" + i + " : " + list.get(i).toString());
//
//        }
//
//        list.add(0, stations.get(0));
//
//        //view.setNestedScrollingEnabled(true);
        adapter = new BusRouteStationAdapter(getApplicationContext(), handler);
//        adapter.addAll(list);

        view.setAdapter(adapter);
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    class RouteStationListHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case STATION_LIST_LOADED:
                    Log.d("RouteStationListHandler", "STATION_LIST_LOADED");
                    List<BusStation> list = (List<BusStation>)msg.obj;

                    adapter.clear();

                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    break;
                case NEXT_ACTIVITY:
                    Log.d("RouteStationListHandler", "NEXT_ACTIVITY");
                    BusStation dest = (BusStation)msg.obj;
                    Intent intent = new Intent(BusRouteStationListActivity.this, FinalActivity.class);
                    intent.putExtra("departStation", BusStationToStrArray.listToArr(departStation));
                    intent.putExtra("destStation", BusStationToStrArray.listToArr(dest));
                    intent.putExtra("arrivingBus", ArrivingBus.ArrivingBusToArray(arrivingBus));
                    startActivity(intent);
                    break;
            }
        }
    }

}

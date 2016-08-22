package kr.tamiflus.sleepingbus;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.tamiflus.sleepingbus.animations.OnOffChangeListener;
import kr.tamiflus.sleepingbus.component.BusRouteStationAdapter;
import kr.tamiflus.sleepingbus.structs.BusStation;

public class BusRouteStationListActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    View infoDetail, infoSummary;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_station_list);

        RecyclerView view = (RecyclerView) findViewById(R.id.busRouteListView);
        ArrayList<BusStation> list = new ArrayList<>();

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        infoDetail = findViewById(R.id.route_info_detail);
        infoSummary = findViewById(R.id.route_info_summary);

        //TODO: 현 버스 노선의 종류를 받아와서 색 넣어주기.
        infoDetail.setBackgroundColor(getResources().getColor(R.color.normal));
        toolbar.setBackgroundColor(getResources().getColor(R.color.normal));
        collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.normal));

        appBarLayout.addOnOffsetChangedListener(new OnOffChangeListener(infoSummary, infoDetail));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);

        view.setLayoutManager(llm);

        OnOffChangeListener.startAlphaAnimation(infoSummary, 0, View.INVISIBLE);

        //TODO: 버스 노선 정보 입력해주기
        ((TextView) findViewById(R.id.BusRouteInfo)).setText("경기도 안산시 시외버스"); //버스 노선 정보
        ((TextView) findViewById(R.id.BusRouteName)).setText("320"); //버스 노선 번호
        ((TextView) findViewById(R.id.BusHeadingInfo)).setText("여의도 버스환승센터 방면"); //버스 목적지 번호


        for(int i=0; i<20; i++) {
            BusStation st = new BusStation();
            st.setName("한국디지털미디어고등학교");
            st.setRegion("경기도 안산");
            st.setId("18312");
            list.add(st);
        }

        //view.setNestedScrollingEnabled(true);
        BusRouteStationAdapter adapter = new BusRouteStationAdapter(getApplicationContext());
        adapter.addAll(list);

        view.setAdapter(adapter);
    }

}

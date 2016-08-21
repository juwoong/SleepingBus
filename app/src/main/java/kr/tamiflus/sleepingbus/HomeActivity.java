package kr.tamiflus.sleepingbus;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kr.tamiflus.sleepingbus.component.HomeAdapter;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.structs.NearStation;
import kr.tamiflus.sleepingbus.structs.NearTwoStation;
import kr.tamiflus.sleepingbus.threads.FindNearStationThread;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;

public class HomeActivity extends AppCompatActivity {
    public static final int UPDATE_NEARSTATION_ONE = 1;
    public static final int UPDATE_NEARSTATION_TWO = 2;
    public static final int CANNOT_FIND_CURRENTLOCATION = 3;
    public static final int LOCATION_CHANGED = 4;
    public static final int REQUEST_CODE_LOCATION = 11;


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
        st.setName("한국디지털미디어고등학교");

        st.setDist("1");
        NearStation nearStation = new NearStation(st);
        nearStation.distance = 326;

        adapter = new HomeAdapter(getApplicationContext());
        adapter.add(nearStation);

        //test
        if (isNetworkAvailable()) {
            getNearestStationByLocation();
        } else {
            Toast.makeText(this, "네트워크 연결 불가", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        //TODO: 현재 검색중일 때, TextView에 검색중이라고 넣기
        //TODO: 기본 상태에서 m(빨간 글자) 안보이게 만들기

//        BusStation st1 = new BusStation();
//        st1.setName("와동중학교 방면");
//        st1.setDist("1");
//
//        BusStation st2 = new BusStation();
//        st2.setName("와동주민센터 방면");
//        st2.setDist("2");
//
//        NearTwoStation two = new NearTwoStation(st1, st2);
//        two.name = "한국디지털미디어고등학교";
//        two.d1 = 326;
//        two.d2 = 341;
//
//        ArrivingBus arrivingBus = new ArrivingBus("234000011", "1113-1", "11", "경기77바2075", 9);
//
//        BusStation st3 = new BusStation();
//        st3.setName("동성.현대아파트");
//        BusStation st4 = new BusStation();
//        st4.setName("천호역5번출구.천호사거리");
//
//        BookMark mark = new BookMark("외갓댁 가는 길", st3, st4,arrivingBus);


//        adapter.add(two);
//        adapter.add(mark);

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
        FindNearStationThread thread = new FindNearStationThread(this, new HomeHandler());
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
            switch (msg.what) {
                case UPDATE_NEARSTATION_ONE:
                    updateScreenContents(new NearStation((BusStation) msg.obj));
                    break;
                case UPDATE_NEARSTATION_TWO:
                    updateScreenContents(new NearTwoStation(((List<BusStation>) msg.obj).get(0), ((List<BusStation>) msg.obj).get(1)));

                    //test code
//                    Toast.makeText(HomeActivity.this, "test start!!", Toast.LENGTH_SHORT).show();
//                    List<BusStation> test = new ArrayList<>();
//                    test.add(((List<BusStation>) msg.obj).get(0));
//                    test.add(((List<BusStation>) msg.obj).get(1));
//                    startMapActivity(test);

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

    /**
     * list.size() must be 2
     * 두 개의 이름이 같은 정류장을 구글맵을 통해 구별함.
     *
     * @param list
     */
    private void startMapActivity(List<BusStation> list) {
        Log.d("HomeActivity", "startMapActivity()");
        if (list.size() != 2) {
            Log.d("HomeActivity", "unexpected list size");
            Toast.makeText(this, "unexpected list size", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, SearchBusStationByLocationActivity.class);
            Log.d("Home_list[0]", list.get(0).toString());
            Log.d("Home_list[1]", list.get(1).toString());

            String[] arr1 = BusStationToStrArray.listToArr(list.get(0));
            String[] arr2 = BusStationToStrArray.listToArr(list.get(1));

            intent.putExtra("st1", arr1);
            intent.putExtra("st2", arr2);
            startActivity(intent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}

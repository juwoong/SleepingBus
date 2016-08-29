package kr.tamiflus.sleepingbus;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.component.BusStationActivityAdapter;
import kr.tamiflus.sleepingbus.component.HomeAdapter;
import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.structs.BookMark;
import kr.tamiflus.sleepingbus.structs.Bus;
import kr.tamiflus.sleepingbus.structs.BusRoute;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.structs.HomeObject;
import kr.tamiflus.sleepingbus.structs.NearStation;
import kr.tamiflus.sleepingbus.structs.NearTwoStation;
import kr.tamiflus.sleepingbus.threads.FindNearStationThread;
import kr.tamiflus.sleepingbus.utils.BusArrivalTimeParser;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;
import kr.tamiflus.sleepingbus.utils.InfomationDBHelper;

public class HomeActivity extends AppCompatActivity {
    public static final int UPDATE_NEARSTATION_ONE = 1;
    public static final int UPDATE_NEARSTATION_TWO = 2;
    public static final int CANNOT_FIND_CURRENTLOCATION = 3;
    public static final int LOCATION_CHANGED = 4;
    public static final int INAVAILABLE_LOCATION = 5;
    public static final int REQUEST_CODE_LOCATION = 11;
    public static final String STRING_LOADING_LOCATION = "위치정보 가져오는 중..";
    public static final String STRING_FAILED_NETWORK = "네트워크 연결 실패";
    public static final String STRING_FAILED_LOCATION = "위치 조회 실패";
    InfomationDBHelper infomationDBHelper = null;


    RecyclerView recyclerView;
    HomeAdapter adapter;
    FloatingActionButton fab;


    @Override
    protected void onStart() {
        super.onStart();
        for(int i=adapter.getList().size()-1; i>=1; i--) {
            adapter.removeByPosition(i);
        }
        if(infomationDBHelper != null) {
            adapter.addAll(infomationDBHelper.getBookMarks());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        infomationDBHelper = new InfomationDBHelper(getApplicationContext());

        TextView searchButton = (TextView) findViewById(R.id.searchButton);
        recyclerView = (RecyclerView) findViewById(R.id.homeRecyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);

        recyclerView.setLayoutManager(llm);

        BusStation st = new BusStation();
        st.setName(STRING_LOADING_LOCATION);
        st.setDist(null);
        NearStation nearStation = new NearStation(st);
//        nearStation.distance = -1;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OvershootInterpolator interpolator = new OvershootInterpolator();
                ViewCompat.setRotation(fab, 0f);
                ViewCompat.animate(fab).rotation(-360f).withLayer().setDuration(500).setInterpolator(interpolator).start();
                ParseTask task = new ParseTask();
                task.execute(adapter.getList());
            }
        });

        adapter = new HomeAdapter(getApplicationContext());
        adapter.add(nearStation);

        //test
        if (isNetworkAvailable()) {
            getNearestStationByLocation();
        } else {
            Toast.makeText(this, STRING_FAILED_NETWORK, Toast.LENGTH_SHORT).show();
            BusStation tmp = new BusStation();
            tmp.setName(STRING_FAILED_NETWORK);
            tmp.setDist(null);
            NearStation tmp2 = new NearStation(tmp);
            adapter.setItemByPosition(0, tmp2);
            adapter.notifyDataSetChanged();
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
        adapter.addAll(infomationDBHelper.getBookMarks());
        recyclerView.setAdapter(adapter);

        ParseTask task = new ParseTask();
        task.execute(adapter.getList());

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
        adapter.setItemByPosition(0, two);
        adapter.notifyDataSetChanged();
    }

    private void updateScreenContents(NearStation one) {
        adapter.setItemByPosition(0, one);
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
                    temp.setName(STRING_FAILED_LOCATION);
                    updateScreenContents(new NearStation(temp));
                    break;
                case INAVAILABLE_LOCATION:
                    Toast.makeText(HomeActivity.this, "위치정보 사용 불가 상태입니다.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d("ERROR", "Unexpected Handler Message : " + msg.what);
                    Toast.makeText(HomeActivity.this, "unexpected Handler Message", Toast.LENGTH_SHORT).show();
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

    public class ParseTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params){
            Log.d("ASyncTask", "doInBackground()");
            ArrayList<HomeObject> routeList = (ArrayList<HomeObject>) (params[0]);


            try {
                for(int i=0; i<routeList.size(); i++) {
                    if(!(routeList.get(i) instanceof BookMark)) continue;
                    BookMark mark = (BookMark) routeList.get(i);
                    ArrivingBus bus = ((new BusArrivalTimeParser()).parse(mark.startSt.getCode(), mark.arrivingBus.getRouteId()))[0];

                    mark.arrivingBus.setNumOfStationsToWait(bus.getNumOfStationsToWait());
                    mark.arrivingBus.setPlateNo(bus.getPlateNo());
                    mark.arrivingBus.setTimeToWait(bus.getTimeToWait());

                    Log.d("mark", mark.toString());

                    adapter.setItemByPosition(i, mark);
                }
            }catch(Exception e) {

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
            return null;
        }

    }
}

package kr.tamiflus.sleepingbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;

public class FinalActivity extends AppCompatActivity {
    ArrivingBus arrivingBus;
    BusStation destStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Intent intent = getIntent();
        destStation = BusStationToStrArray.arrToList(intent.getStringArrayExtra("destStation"));
        arrivingBus = ArrivingBus.ArrayToArrivingBus(intent.getStringArrayExtra("arrivingBus"));

        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.coordinator);
        TextView summary = (TextView) findViewById(R.id.summaryText);
        TextView boardBtn = (TextView) findViewById(R.id.boardBtn);

        layout.setBackgroundColor(getResources().getColor(R.color.jikhang));
        boardBtn.setTextColor(getResources().getColor(R.color.jikhang));
//        summary.setText(String.format("%s 행\n%s번 버스", "천호역.천호사거리", "1113-1"));
        summary.setText(String.format("%s 행\n%s번 버스", destStation.getName(), arrivingBus.getRouteName()));



        boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 다 설정되었을 때 알람 실행하기
                Log.d("FinalActivity", arrivingBus.toString());
                Log.d("FinalActivity", destStation.toString());
                Intent intent = new Intent(FinalActivity.this, AlarmService.class);
                intent.putExtra("plateNo", arrivingBus.getPlateNo());
                intent.putExtra("routeId", arrivingBus.getRouteId());
                intent.putExtra("stationId", destStation.getCode());
                Log.d("SetAlarm", "plateNo : " + arrivingBus.getPlateNo());
                Log.d("SetAlarm", "routeId : " + arrivingBus.getRouteId());
                Log.d("SetAlarm", "stationId : " + destStation.getCode());

                startService(intent);
            }
        });
    }

}

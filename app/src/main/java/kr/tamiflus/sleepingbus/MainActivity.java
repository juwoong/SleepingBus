package kr.tamiflus.sleepingbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kr.tamiflus.sleepingbus.utils.BusStationDBHelper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!BusStationDBHelper.isCheckDB(getApplicationContext())) BusStationDBHelper.copyDB(getApplicationContext());

        ((Button) findViewById(R.id.startBussingButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BusRouteStationListActivity.class);
                startActivity(intent);
            }
        });


    }
}

package kr.tamiflus.sleepingbus;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import junit.framework.Assert;

import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.utils.BusArrivalTimeParser;

/**
 * Intent MUST have..
 * String plateNo,
 * int routeId,
 * int stationId
 */

public class AlarmService extends IntentService {
    public static final int WHAT_MINUTE_BEFORE = 2;
    public static final int WHAT_STATIONS_BEFORE = 1;
    public static final int REQUEST_PER_SEC = 60;

    private String plateNo;
    String routeId, stationId;
    BusArrivalTimeParser parser = new BusArrivalTimeParser();

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        plateNo = intent.getStringExtra("plateNo");
        routeId = intent.getStringExtra("routeId");
        stationId = intent.getStringExtra("stationId");
        Assert.assertNotNull(plateNo);
        Assert.assertNotNull(routeId);
        Assert.assertNotNull(stationId);

        while (true) {
            if (check()) {
                Log.i("AlarmService", "Trigger Alarm!!!!");
                triggerAlarm();
                break;
            } else {
                try {
                    Thread.sleep(REQUEST_PER_SEC * 1000);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    private void triggerAlarm() {
        Intent intent = new Intent(getBaseContext(), AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean check() {
        Log.d("AlarmService", "CHECK IT OUT");
        ArrivingBus[] buses = null;
        try {
            buses = parser.parse(stationId, routeId);

            //debug
            Log.d("PARSE FINISHED", "buses : " + buses.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Check()_Exception", "Exception");
        }
        if (plateNo.equals(buses[0].getPlateNo())) {
            if (buses[0].getTimeToWait() <= WHAT_MINUTE_BEFORE || Integer.parseInt(buses[0].getNumOfStationsToWait()) <= WHAT_STATIONS_BEFORE) return true;
        } else if (plateNo.equals(buses[1].getPlateNo())) {
            if (buses[1].getTimeToWait() <= WHAT_MINUTE_BEFORE || Integer.parseInt(buses[1].getNumOfStationsToWait()) <= WHAT_STATIONS_BEFORE) return true;
        }
        return false;
    }
}

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

        while(true) {
            if(check()) {
                triggerAlarm();
                break;
            } else {
                try {
                    Thread.sleep(60000);    // request per ONE minute
                } catch(InterruptedException ie) { }
            }
        }
    }

    private void triggerAlarm() {
        Intent intent = new Intent(getBaseContext(), AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private boolean check() {
        ArrivingBus[] buses = null;
        try {
            buses = parser.parse(stationId, routeId);
        } catch(Exception e) {
            Log.d("ERROR", "ERROR");
        }
        if(plateNo.equals(buses[0].getPlateNo())) {
            if (buses[0].getTimeToWait() <= WHAT_MINUTE_BEFORE) return true;
        } else if(plateNo.equals(buses[2].getPlateNo())) {
            if(buses[2].getTimeToWait() <= WHAT_MINUTE_BEFORE) return true;
        }
        return false;
    }
}

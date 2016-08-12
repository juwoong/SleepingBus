package kr.tamiflus.sleepingbus;

import android.app.IntentService;
import android.content.Intent;

/**
 * Intent MUST have..
 * String plateNo,
 * int routeId,
 * int stationId
 */

public class AlarmService extends IntentService {

    private String plateNo;
    int routeId, stationId;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        plateNo = intent.getStringExtra("plateNo");
        routeId = intent.getIntExtra("routeId", -1);
        stationId = intent.getIntExtra("stationId", -1);

        while(true) {
            if(check()) {
                triggerAlarm();
                break;
            } else {
                try {
                    Thread.sleep(10000);
                } catch(InterruptedException ie) { }
            }
        }
    }

    private void triggerAlarm() {
        // TODO: alarm function
    }

    private boolean check() {
        boolean foo = true;
        // TODO: Check
        return foo;
    }
}

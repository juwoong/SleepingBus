package kr.tamiflus.sleepingbus;

import android.app.IntentService;
import android.content.Intent;

/**
 * Intent MUST have busId, stationId(destination)
 */

public class AlarmService extends IntentService {
    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}

package kr.tamiflus.sleepingbus;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

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
    public static final int NOTIFICATION_ID = 1234;

    private String plateNo;
    String routeId, stationId;
    BusArrivalTimeParser parser = new BusArrivalTimeParser();
    Context context;
    public static volatile boolean shouldContinue = true;

    public AlarmService() {
        super("AlarmService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = getApplicationContext();
        plateNo = intent.getStringExtra("plateNo");
        routeId = intent.getStringExtra("routeId");
        stationId = intent.getStringExtra("stationId");
        Assert.assertNotNull(plateNo);
        Assert.assertNotNull(routeId);
        Assert.assertNotNull(stationId);

        if(!shouldContinue) {
            Log.d("IntentService", "IntentService STOPPED!!!!!!");
            cancelNotification();
            return;
        }
        createNotification();

        while (true) {
            if (check()) {
                Log.i("AlarmService", "Trigger Alarm!!!!");
                cancelNotification();
                triggerAlarm();
                break;
            } else {
                try {
                    for(int i = 0; i<REQUEST_PER_SEC; i++) {
                        Thread.sleep(1000);
                        if(!shouldContinue) {
                            Log.d("IntentService", "IntentService STOPPED!!!!!!");
                            cancelNotification();
                            return;
                        }
                    }
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

    public void createNotification()
    {
        Log.d("createNotification", "createNotification()");
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("SleepingBus")
                .setContentText("알람 설정")
//                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true)
                .setAutoCancel(false);

        Intent intent = new Intent(context, AlarmDisableActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AlarmDisableActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancelNotification()
    {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(NOTIFICATION_ID);
    }
}

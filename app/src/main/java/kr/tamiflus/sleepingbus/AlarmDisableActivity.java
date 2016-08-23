package kr.tamiflus.sleepingbus;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AlarmDisableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_disable);
    }

    public void onClickStopAlarm(View v) {
        if(AlarmService.shouldContinue) {
            AlarmService.shouldContinue = false;
            Toast.makeText(this, "알람이 꺼졌습니다!", Toast.LENGTH_SHORT).show();
            cancelNotification();
        } else {
            Toast.makeText(this, "켜진 알람이 없습니다." , Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelNotification()
    {
        Log.d("cancelNoti", "cancelNoti");
        NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(FinalActivity.NOTIFICATION_ID);
    }
}

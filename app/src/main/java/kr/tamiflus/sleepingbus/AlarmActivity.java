package kr.tamiflus.sleepingbus;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AlarmActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private Vibrator vibe;
    private AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        startAlarm();
    }

    public void onClickStop(View v) {
        Log.d("AlarmActivity", "알람 끄기");
        mp.stop();
        vibe.cancel();

    }

    public void startAlarm() {
        Log.d("AlarmActivity", "StartAlarm()");
        cancelNotification();

        mp = MediaPlayer.create(this, R.raw.alarm);
        am = (AudioManager)getSystemService(AUDIO_SERVICE);
        if(am.isWiredHeadsetOn()) {
            mp.setLooping(true);
            mp.start();
        }
        vibe = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        long[] pattern = new long[] {0, 1000, 500};
        vibe.vibrate(pattern, 0);
    }

    public void cancelNotification()
    {
        Log.d("cancelNoti", "cancelNoti");
        NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(FinalActivity.NOTIFICATION_ID);
    }
}

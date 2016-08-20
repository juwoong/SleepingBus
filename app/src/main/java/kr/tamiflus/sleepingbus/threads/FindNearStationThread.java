package kr.tamiflus.sleepingbus.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.List;

import kr.tamiflus.sleepingbus.HomeActivity;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.utils.LocManager;
import kr.tamiflus.sleepingbus.utils.NearestStationParser;

/**
 * Created by 김정욱 on 2016-08-20.
 */

public class FindNearStationThread extends Thread {
    private Context context;
    private Handler handler;

    public FindNearStationThread(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void run() {
        Log.d("FindNearStationThread", "Thread START");

        List<BusStation> list = null;
        LocManager locManager = new LocManager(context, context);
        NearestStationParser parser = new NearestStationParser(context);

//        locManager.checkIsGPSOn();
        Looper.prepare();
        locManager.loadLocation();
        double x = locManager.getLongitude();
        double y = locManager.getLatitude();
        Log.d("currentX", "" + x);
        Log.d("currentY", "" + y);

        try {
            list = parser.getNearestStationByXY(String.valueOf(x), String.valueOf(y));
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1); // debug
        }

        Message msg = new Message();

        if(list != null) Log.d("NearStationList", list.toString());
        else {
            msg.what = HomeActivity.CANNOT_FIND_CURRENTLOCATION;
        }

        if(list.size() == 1) {
            msg.what = HomeActivity.UPDATE_NEARSTATION_ONE;
            msg.obj = list;
        } else if(list.size() == 2) {
            msg.what = HomeActivity.UPDATE_NEARSTATION_TWO;
            msg.obj = list;
        } else if(list.size() == 0) {
            Log.d("FindNearStationThread", "Unexpected list parameter");
            System.exit(-1);
        }
        handler.sendMessage(msg);

        Log.d("FindNearStationThread", "FINISH");
    }
}

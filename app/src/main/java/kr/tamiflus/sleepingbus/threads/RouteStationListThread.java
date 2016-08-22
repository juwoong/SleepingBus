package kr.tamiflus.sleepingbus.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;

import kr.tamiflus.sleepingbus.BusRouteStationListActivity;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.utils.BusStationDBHelper;

/**
 * Created by 김정욱 on 2016-08-22.
 */

public class RouteStationListThread extends Thread {
    private Handler handler;
    private String routeId;
    private Context context;

    public RouteStationListThread(Handler handler, String routeId, Context context) {
        this.handler = handler;
        this.routeId = routeId;
        this.context = context;
    }

    @Override
    public void run() {
        Log.d("RouteStationListThread", "run() called");
        List<BusStation> stations = (new BusStationDBHelper(context)).getStationsByRouteId(routeId);
        Message msg = new Message();
        msg.what = BusRouteStationListActivity.STATION_LIST_LOADED;
        msg.obj = stations;
        handler.sendMessage(msg);
    }
}

package kr.tamiflus.sleepingbus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import kr.tamiflus.sleepingbus.structs.BusRoute;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.structs.BusStationTag;

/**
 * Created by tamiflus on 2016. 8. 1..
 */
public class BusStationDBHelper extends SQLiteOpenHelper{

    Context context;
    public BusStationDBHelper(Context context) {
        super(context, "bus.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public static boolean isCheckDB(Context context){
        String filePath = "/data/data/" + context.getPackageName() + "/databases/bus.db";
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static void copyDB(Context context){
        Log.d("SleepingBusDBManager", "copyDB : bus.db");
        AssetManager manager = context.getAssets();
        String folderPath = "/data/data/" + context.getPackageName()+ "/databases";
        String filePath = "/data/data/" + context.getPackageName() + "/databases/bus.db";
        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = manager.open("bus.db");
            BufferedInputStream bis = new BufferedInputStream(is);

            if (folder.exists()) {
            }else{
                folder.mkdirs();
            }


            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();

            bos.close();
            fos.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }

    public ArrayList<BusStation> getStation(String query) {
        ArrayList<BusStation> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;

        c = db.rawQuery("SELECT * FROM BusStation WHERE stId LIKE \'" + query + "%\' OR stName LIKE \'%" + query + "%\' ORDER BY stLocation", null);

        if(c.moveToFirst()) {
            do {
                BusStation st = new BusStation();

                st.setName(c.getString(0));
                st.setId(c.getString(1));
                st.setRegion(c.getString(2));
                st.setCode(c.getString(3));
                st.setX(c.getString(4));
                st.setY(c.getString(5));

                list.add(st);
                Log.i("station", st.toString());
            }while(c.moveToNext());
        }

        ArrayList<BusStation> result = new ArrayList<>();
        String loc = null;

        if(list.size() > 0) {
            result.add(new BusStationTag(list.get(0).getRegion()));
            result.add(list.get(0));
            loc = list.get(0).getRegion();
        }
        for(int i=1; i<list.size(); i++) {
            if(!loc.equals(list.get(i).getRegion())) {
                loc = list.get(i).getRegion();
                result.add(new BusStationTag(loc));
            }
            result.add(list.get(i));
        }

        return result;
    }

    public List<BusStation> fillStation(List<BusStation> st) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;

        for(BusStation i : st) {
            c = db.rawQuery("SELECT * FROM BusStation WHERE stCode=\'" + i.getCode() + "\'", null);
            if(c.moveToFirst()) {
                do {
                    i.setName(c.getString(0));
                    i.setId(c.getString(1));
                    i.setRegion(c.getString(2));
                    i.setRegion(c.getString(2));
                    i.setCode(c.getString(3));
                    i.setX(c.getString(4));
                    i.setY(c.getString(5));
                    Log.d("fillStation", i.toString());
                }while(c.moveToNext());
            }
        }

        return st;
    }
    public BusStation fillStation(BusStation st) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;

        c = db.rawQuery("SELECT * FROM BusStation WHERE stCode=\'" + st.getCode() + "\'", null);
        if(c.moveToFirst()) {
            do {
                st.setName(c.getString(0));
                st.setId(c.getString(1));
                st.setRegion(c.getString(2));
                st.setCode(c.getString(3));
                st.setX(c.getString(4));
                st.setY(c.getString(5));
//                Log.d("SQL", c.getString(0) + ", " + c.getString(1) + ", " + c.getString(2) + ", " + c.getString(3) + ", " + c.getString(4) + ", " + c.getString(5));
            }while(c.moveToNext());
        }
        return st;
    }

    public List<BusRoute> fillBusRoute(List<BusRoute> list) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;

        for(BusRoute busRoute : list) {
            c = db.rawQuery("SELECT * FROM BusRoute WHERE routeId=\'" + busRoute.getRouteId() + "\'", null);
            if(c.moveToFirst()) {
                do {
                    busRoute.setRegionName(c.getString(1));
                    busRoute.setRouteName(c.getString(3));
                    busRoute.setRouteTypeCd(c.getString(4));
                    busRoute.setRouteTypeName(c.getString(5));
                    Log.d("fillBusRoute", busRoute.toString());
                }while(c.moveToNext());
            }
        }
        return list;
    }

    public List<BusRoute> getBusRoutesByStationId(String stationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        List<BusRoute> busRouteList = new ArrayList<>();
        List<String> routeIdList = new ArrayList<>();

        c = db.rawQuery("SELECT * FROM BusRouteList WHERE busStationID=\'" + stationId + "\'", null);
        int routeIdIndex = c.getColumnIndex("busRouteID");
        if(c.moveToFirst()) {
            do {
                if(!routeIdList.contains(c.getString(routeIdIndex))) {
                    routeIdList.add(c.getString(routeIdIndex));
                }
            }while(c.moveToNext());
        }
        Log.d("DBHelper", "routeIdList: " + routeIdList.toString());

        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        };
        Collections.sort(routeIdList, comparator);

        BusRoute busRoute;
        for(int i = 0; i<routeIdList.size(); i++) {
            busRoute = new BusRoute();
            busRoute.setRouteId(routeIdList.get(i));
            Log.d("DBHelper", "routeIdList.get(i) : " + routeIdList.get(i));
            busRouteList.add(busRoute);
        }
        busRouteList = fillBusRoute(busRouteList);

        //debug code
        Log.d("DBHelper", "routeIdList.size() : " + routeIdList.size());
        for(int i = 0; i<routeIdList.size(); i++) {
            Log.d("DBHelper", "routeIdList.toString : " + routeIdList.toString());
        }

        return busRouteList;
    }
}
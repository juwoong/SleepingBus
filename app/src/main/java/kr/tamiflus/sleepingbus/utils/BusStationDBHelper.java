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

import kr.tamiflus.sleepingbus.structs.BusStation;

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

        c = db.rawQuery("SELECT * FROM BusStation WHERE stId LIKE \'" + query + "%\' OR stName LIKE \'%" + query + "%\'", null);

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

        return list;
    }

    public ArrayList<BusStation> fillStation(ArrayList<BusStation> st) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;

        for(BusStation i : st) {
            c = db.rawQuery("SELECT * FROM BusStation WHERE stCode=\'" + i.getCode() + "\'", null);
            if(c.moveToFirst()) {
                do {
                    i.setId(c.getString(1));
                    i.setRegion(c.getString(2));
                }while(c.moveToNext());
            }
        }

        return st;
    }
}

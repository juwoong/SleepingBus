package kr.tamiflus.sleepingbus.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import kr.tamiflus.sleepingbus.structs.BookMark;
import kr.tamiflus.sleepingbus.structs.BusRoute;
import kr.tamiflus.sleepingbus.structs.HomeObject;

/**
 * Created by tamiflus on 16. 8. 26..
 */
public class InfomationDBHelper extends SQLiteOpenHelper{
    Context context;
    public static final String DATABASE_NAME = "info.db";

    public InfomationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE bookmark(name TEXT NOT NULL, routeID TEXT NOT NULL, startStID TEXT NOT NULL, endStID TEXT NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE station_history(stName TEXT NOT NULL, stId TEXT NOT NULL, stLocation TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void querty(String query) { getWritableDatabase().execSQL(query);}

    public void addBookMark(String name, String routeID, String startStID, String endStID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("INSERT INTO bookmark VALUES (\'%s\', \'%s\', \'%s\', \'%s\')", name, routeID, startStID, endStID));
        db.close();
    }

    public void removeBookMark(String routeID, String startStID, String endStID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("DELETE FROM bookmark WHERE routeID=\'%s\' AND startStID=\'%s\' AND endStID=\'%s\'", routeID, startStID, endStID));
        db.close();
    }


    public boolean hasBookMark(String routeID, String startStID, String endStID) {
        SQLiteDatabase db = getReadableDatabase();
        BusStationDBHelper helper = new BusStationDBHelper(context);
        Cursor c;
        c = db.rawQuery(String.format("SELECT * FROM bookmark WHERE routeID=\'%s\' AND startStID=\'%s\' AND endStID=\'%s\'", routeID, startStID, endStID), null);
        if(c.moveToFirst()) {
             return true;
        }
        return false;
    }

    public ArrayList<HomeObject> getBookMarks() {
        ArrayList<HomeObject> bookMarks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        BusStationDBHelper helper = new BusStationDBHelper(context);
        Cursor c;
        c = db.rawQuery("SELECT * FROM bookmark;", null);
        if(c.moveToFirst()) {
            do {
                Log.d("getBookMark", String.format("{%s, %s, %s, %s}", c.getString(0), c.getString(1), c.getString(2), c.getString(3)));
                BookMark bookMark = new BookMark();
                bookMark.name = c.getString(0);

                bookMark.startSt = helper.getStationByStationId(c.getString(2));
                bookMark.endSt = helper.getStationByStationId(c.getString(3));

                bookMark.arrivingBus = helper.getBusRouteByRouteId(c.getString(1));
                bookMarks.add(bookMark);
            }while(c.moveToNext());
        }

        return bookMarks;
    }
}


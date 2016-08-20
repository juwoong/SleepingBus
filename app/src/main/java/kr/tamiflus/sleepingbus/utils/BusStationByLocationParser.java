package kr.tamiflus.sleepingbus.utils;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import kr.tamiflus.sleepingbus.structs.BusStation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tamiflus on 2016. 8. 2..
 */
public class BusStationByLocationParser{

    OkHttpClient client;

    public BusStationByLocationParser() {
        client = new OkHttpClient();
    }

    private ArrayList<BusStation> xmlToObject(String body) throws XmlPullParserException, IOException {
        ArrayList list = new ArrayList<>();

        XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
        XmlPullParser xpp= factory.newPullParser();
        xpp.setInput(new StringReader(body));

        Log.d("XMLresult", body);

        String tag;

        xpp.next();
        int eventType= xpp.getEventType();
        BusStation st = new BusStation();

        while( eventType != XmlPullParser.END_DOCUMENT ){
            switch( eventType ){
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    tag = xpp.getName();
                    Log.i("tag", tag + " : " + xpp.getText());

                    if(tag.equals("itemList")) st = new BusStation();
                    else if(tag.equals("gpsX")) { xpp.next(); st.setX(xpp.getText()); }
                    else if(tag.equals("gpsY")) { xpp.next(); st.setY(xpp.getText()); }
                    else if(tag.equals("stationId")) { xpp.next(); st.setCode(xpp.getText()); }
                    else if(tag.equals("stationNm")) { xpp.next(); st.setName(xpp.getText()); }

                    break;
                case XmlPullParser.TEXT:
                    break;
                case XmlPullParser.END_TAG:
                    tag= xpp.getName();
                    if(tag.equals("itemList")) {
                        list.add(st);
                        Log.i("list", st.toString());
                    }
                    break;
            }
            eventType = xpp.next();
        }
        return list;
    }

    public ArrayList<BusStation> parse(String x, String y) throws IOException, XmlPullParserException{

        Request request = new Request.Builder()
                .url("http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?ServiceKey=mprvOxIo4u5PCVIVStlnRI6rMDBmJGRvC6%2BNurrGAkl0Ctsmt7UJxU9XwMwP4IOAuaRxjScQ2hGKaDm1n1z%2BgA%3D%3D&tmX="+x+"&tmY="+y+"&radius=500&numOfRows=999&pageSize=999&pageNo=1&startPage=1")
                .build();

        Response response = client.newCall(request).execute();

        return xmlToObject(response.body().string());
    }
}

package kr.tamiflus.sleepingbus.utils;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.structs.BusStation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 김정욱 on 2016-08-12.
 * BusStationByLocationParser 클래스 조금 수정
 *
 * 반드시 특정 routeId의 버스도착정보만 담겨있는 XML파일이 매개변수로 들어와야 함!!!!!!!!!!!!!!!! (두개이상있으면 안됨)
 * 버스도착정보조회서비스 API 사용, 도착 중인 버스 두 개의 번호판과 예상도착대기시간을 객체화하여 리턴
 *
 */
public class BusArrivalTimeParser {

    private OkHttpClient client;
    public BusArrivalTimeParser() {
        client = new OkHttpClient();
    }

    private ArrivingBus[] xmlToObject(String xml) throws XmlPullParserException, IOException {
        ArrivingBus[] buses = new ArrivingBus[2];

        XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
        XmlPullParser xpp= factory.newPullParser();
        xpp.setInput(new StringReader(xml));

        String tag;

        xpp.next();
        int eventType= xpp.getEventType();
        ArrivingBus bus1 = new ArrivingBus();
        ArrivingBus bus2 = new ArrivingBus();

        while(eventType != XmlPullParser.END_DOCUMENT){
            switch(eventType){
                case XmlPullParser.START_TAG:
                    tag = xpp.getName();
                    Log.i("tag", tag + " : " + xpp.getText());

                    if(tag.equals("plateNo1")) { xpp.next(); bus1.setPlateNo(xpp.getText()); }
                    else if(tag.equals("plateNo2")) { xpp.next(); bus2.setPlateNo(xpp.getText()); }
                    else if(tag.equals("predictTime1")) { xpp.next(); bus1.setTimeToWait(Integer.parseInt(xpp.getText())); }
                    else if(tag.equals("predictTime2")) { xpp.next(); bus2.setTimeToWait(Integer.parseInt(xpp.getText())); }
                    else if(tag.equals("resultCode")) {
                        xpp.next();
                        Log.d("ERROR", "param ERROR");
                        if("5".equals(xpp.getText())) throw new XmlPullParserException("parameter error");
                    }

                    break;
            }
            eventType = xpp.next();
        }
        buses[0] = bus1;
        buses[1] = bus2;
        return buses;
    }

    public ArrivingBus[] parse(String stationId, String routeId) throws IOException, XmlPullParserException{

        Request request = new Request.Builder()
                .url("http://openapi.gbis.go.kr/ws/rest/busarrivalservice?serviceKey=mprvOxIo4u5PCVIVStlnRI6rMDBmJGRvC6%2BNurrGAkl0Ctsmt7UJxU9XwMwP4IOAuaRxjScQ2hGKaDm1n1z%2BgA%3D%3D&stationId=" + stationId + "&routeId=" + routeId + "&numOfRows=999&pageSize=999&pageNo=1&startPage=1")
                .build();

        Response response = client.newCall(request).execute();

        return xmlToObject(response.body().string());
    }
}

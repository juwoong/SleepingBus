package kr.tamiflus.sleepingbus.utils;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.structs.BusStation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 김정욱 on 2016-08-17.
 */

public class NearestStationParser {
    public static final String FIND_RADIUS = "500";

    private OkHttpClient client;
    public NearestStationParser() {
        client = new OkHttpClient();
    }

    private List<BusStation> xmlToObject(String xml) throws XmlPullParserException, IOException {
        List<BusStation> stations = new ArrayList<>();

        XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
        XmlPullParser xpp= factory.newPullParser();
        xpp.setInput(new StringReader(xml));

        String tag;

        xpp.next();
        int eventType= xpp.getEventType();

        BusStation station = new BusStation();
        while(eventType != XmlPullParser.END_DOCUMENT){
            switch(eventType){
                case XmlPullParser.START_TAG:
                    tag = xpp.getName();
                    Log.i("tag", tag + " : " + xpp.getText());

                    if(tag.equals("itemList")) { xpp.next(); station = new BusStation(); }
                    else if(tag.equals("dist")) { xpp.next(); station.setDist(xpp.getText()); }
                    else if(tag.equals("stationId")) { xpp.next(); station.setCode(xpp.getText()); }
                    else if(tag.equals("headerMsg")) {
                        xpp.next();
                        if(!"정상적으로 처리되었습니다.".equals(xpp.getText())) {
                            Log.d("ERROR", "ERROR");
                            throw new XmlPullParserException("error");
                        }

                    }
                    break;
                case XmlPullParser.END_TAG:
                    tag= xpp.getName();
                    if(tag.equals("itemList")) {
                        stations.add(station);
                        Log.i("list", station.toString());
                    }
                    break;
            }
            eventType = xpp.next();
        }
        return stations;
    }

    public BusStation getNearestStationByXY(String x, String y) throws IOException, XmlPullParserException{ // todo 거리가 같은 경우엔 어떻게? 가까운 정류장이 없을 경우엔 어떻게?
        List<BusStation> stations;

        Request request = new Request.Builder()
                .url("http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?ServiceKey=mprvOxIo4u5PCVIVStlnRI6rMDBmJGRvC6%2BNurrGAkl0Ctsmt7UJxU9XwMwP4IOAuaRxjScQ2hGKaDm1n1z%2BgA%3D%3D&tmX="
                        + y + "&tmY=" + x + "&radius=" + FIND_RADIUS + "&numOfRows=999&pageSize=999&pageNo=1&startPage=1")
                .build();

        Response response = client.newCall(request).execute();

        stations = xmlToObject(response.body().string());
        // TODO findIndexOfNearestStation 함수 작성하고 제일 가까운 정류장 리턴해주기, 근데 가까운 정류장이 없을 경우엔 어떻게?
        return null;
    }

    private int findIndexOfNearestStation(List<BusStation> list) {
        // TODO 리스트에서 제일 가까운 버스정류장의 인덱스값 리턴해주는 함수 작성하기
        return -1;
    }
}

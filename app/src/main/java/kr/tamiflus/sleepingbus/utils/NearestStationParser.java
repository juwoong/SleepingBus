package kr.tamiflus.sleepingbus.utils;

import android.content.Context;
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
    public static final String FIND_RADIUS = "500"; // app finds nearest station in FIND_RADIUS (meter)
    private Context context;

    private OkHttpClient client;
    public NearestStationParser(Context context) {
        this.context = context;
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

    /**
     * @return null when no station was detected.
     * 거리가 같은 경우 랜덤으로 한 개의 정류장만 리턴
     */
    public BusStation getNearestStationByXY(String x, String y) throws IOException, XmlPullParserException{
        List<BusStation> stations;

        Request request = new Request.Builder()
                .url("http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?ServiceKey=mprvOxIo4u5PCVIVStlnRI6rMDBmJGRvC6%2BNurrGAkl0Ctsmt7UJxU9XwMwP4IOAuaRxjScQ2hGKaDm1n1z%2BgA%3D%3D&tmX="
                        + y + "&tmY=" + x + "&radius=" + FIND_RADIUS + "&numOfRows=999&pageSize=999&pageNo=1&startPage=1")
                .build();

        Response response = client.newCall(request).execute();

        stations = xmlToObject(response.body().string());
        if(stations.size() == 0) return null;
        else {
            int index = findIndexOfNearestStation(stations);
            (new BusStationDBHelper(context)).fillStation(stations.get(index));
            return stations.get(index);
        }
    }

    private int findIndexOfNearestStation(List<BusStation> list) {
        // TODO 리스트에서 제일 가까운 버스정류장의 인덱스값 리턴해주는 함수 작성하기
        if(list.size() == 0) {
            Log.d("ERROR", "size of list cannot be 0");
            System.exit(-1);
        }
        int nearestDist = Integer.parseInt(FIND_RADIUS);
        int nearestIndex = -1;
        for(int i = 0; i<list.size(); i++) {
            BusStation station = list.get(i);
            int dist = Integer.parseInt(station.getDist());
            if(dist < nearestDist) {
                nearestDist = dist;
                nearestIndex = i;
            }
        }
        if(nearestIndex < 0) {
            Log.d("ERROR", "unexpected nearestIndex");
            System.exit(-1);
        }
        return nearestIndex;
    }
}

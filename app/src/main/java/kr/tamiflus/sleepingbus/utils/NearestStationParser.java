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
 * getNearestStationByXY(int, int) may useful.
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

        Log.d("body", xml);

        String tag;

        xpp.next();
        int eventType= xpp.getEventType();

        BusStation station = new BusStation();
        while(eventType != XmlPullParser.END_DOCUMENT){
            switch(eventType){
                case XmlPullParser.START_TAG:
                    tag = xpp.getName();
                    xpp.next();
                    Log.i("tag", tag + " : " + xpp.getText());

                    if(tag.equals("itemList")) { station = new BusStation(); }
                    else if(tag.equals("dist")) { Log.d("dist", "dist"); station.setDist(xpp.getText()); }
                    else if(tag.equals("stationId")) { Log.d("si", "si");station.setCode(xpp.getText()); }
                    else if(tag.equals("headerMsg")) {
                        if(!"정상적으로 처리되었습니다.".equals(xpp.getText())) {
                            Log.d("ERROR", "Bus Info Loading Failed");
                            return null;
                        }

                    }
                    break;
                case XmlPullParser.END_TAG:
                    tag= xpp.getName();
                    if(tag.equals("itemList")) {
                        stations.add(station);
                        Log.i("list", station.getCode() + ", " + station.getDist());
                    }
                    break;
            }
            eventType = xpp.next();
        }
        return stations;
    }

    /**
     * @return null when no station was detected.
     * 거리가 같고 이름이 다른 경우, 랜덤으로 한 개의 BusStations을 담은 List를 리턴
     * 이름이 같은 정류장(방향반대)이 있을 경우, 이름이 같은 정류장 두 개 모두를 담은 List를 리턴
     */
    public List<BusStation> getNearestStationByXY(String x, String y) throws IOException, XmlPullParserException{
        List<BusStation> stations;

        Request request = new Request.Builder()
                .url("http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?ServiceKey=mprvOxIo4u5PCVIVStlnRI6rMDBmJGRvC6%2BNurrGAkl0Ctsmt7UJxU9XwMwP4IOAuaRxjScQ2hGKaDm1n1z%2BgA%3D%3D&tmX="
                        + x + "&tmY=" + y + "&radius=" + FIND_RADIUS + "&numOfRows=999&pageSize=999&pageNo=1&startPage=1")
                .build();

        Response response = client.newCall(request).execute();

        stations = xmlToObject(response.body().string());
        if(stations == null) return null;
        else {
            List<BusStation> result = new ArrayList<>();
            int index = findIndexOfNearestStation(stations);
            (new BusStationDBHelper(context)).fillStation(stations);
            Log.d("getNearestStationByXY", stations.get(index).getName() + ", " + stations.get(index).getCode() + ", " + stations.get(index).getDist());
            result.add(stations.get(index));
            stations.remove(index);
            for(int i = 0; i<stations.size(); i++) {
                if(stations.get(i).getName().equals(result.get(0).getName())) {
                    result.add(stations.get(i));
                }
            }
            return result;
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

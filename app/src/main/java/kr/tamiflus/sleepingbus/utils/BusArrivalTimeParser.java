package kr.tamiflus.sleepingbus.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.structs.BusRoute;
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

    private List<ArrivingBus[]> xmlToObject(String xml) throws XmlPullParserException, IOException {
        List<ArrivingBus[]> list = new ArrayList<>();

        XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
        XmlPullParser xpp= factory.newPullParser();
        xpp.setInput(new StringReader(xml));

        String tag;

        xpp.next();
        int eventType= xpp.getEventType();

        ArrivingBus[] buses = null;
        ArrivingBus bus1 = null;
        ArrivingBus bus2 = null;
        Log.d("ArrivalTimeParser", "ArrivalTimeParsing START!");
        while(eventType != XmlPullParser.END_DOCUMENT){
            switch(eventType){
                case XmlPullParser.START_TAG:
                    tag = xpp.getName();
                    Log.i("tag", tag + " : " + xpp.getText());

//                    if(tag.equals("plateNo1")) { xpp.next(); bus1.setPlateNo(xpp.getText()); }
//                    else if(tag.equals("plateNo2")) { xpp.next(); bus2.setPlateNo(xpp.getText()); }
//                    else if(tag.equals("predictTime1")) { xpp.next(); bus1.setTimeToWait(Integer.parseInt(xpp.getText())); }
//                    else if(tag.equals("predictTime2")) { xpp.next(); bus2.setTimeToWait(Integer.parseInt(xpp.getText())); }
//                    else if(tag.equals("resultCode")) {
//                        xpp.next();
//                        Log.d("ERROR", "param ERROR");
//                        if("5".equals(xpp.getText())) throw new XmlPullParserException("parameter error");
//                    }
                    if(tag.equals("busArrivalList")) {
                        buses = new ArrivingBus[2];
                        bus1 = new ArrivingBus();
                        bus2 = new ArrivingBus();
                    } else if(tag.equals("locationNo1")) { xpp.next(); bus1.setNumOfStationsToWait(xpp.getText()); }
                    else if(tag.equals("locationNo2")) { xpp.next(); bus2.setNumOfStationsToWait(xpp.getText()); }
                    else if(tag.equals("plateNo1")) { xpp.next(); bus1.setPlateNo(xpp.getText()); }
                    else if(tag.equals("plateNo2")) { xpp.next(); bus2.setPlateNo(xpp.getText()); }
                    else if(tag.equals("predictTime1")) { xpp.next(); bus1.setTimeToWait(Integer.parseInt(xpp.getText())); }
                    else if(tag.equals("predictTime2")) { xpp.next(); bus2.setTimeToWait(Integer.parseInt(xpp.getText())); }
                    else if(tag.equals("routeId")) {
                        xpp.next();
                        bus1.setRouteId(xpp.getText());
                        bus2.setRouteId(xpp.getText());
                    }

                    break;
                case XmlPullParser.END_TAG:
                    tag = xpp.getName();
                    if(tag.equals("busArrivalList")) {
                        buses[0] = bus1;
                        buses[1] = bus2;
                        Log.d("ArrivalTimeParser", "" + list.size() + "_" + "buses[0] : " + bus1.toString());
                        Log.d("ArrivalTimeParser", "" + list.size() + "_" + "buses[1] : " + bus2.toString());
                        list.add(buses);
                    }
            }
            eventType = xpp.next();
        }
        return list;
    }
    // todo stationId만 가지고 모든 노선별 도착정보 가져오자. 노선별 도착정보는 이미 가지고있는 BusRoute List에 매핑하자.
    public List<BusRoute> parse(String stationId, List<BusRoute> routeList) throws IOException, XmlPullParserException{
        if(routeList == null) Log.d("ArrivalTimeParser", "routeList is null!!!");

        Request request = new Request.Builder()
                .url("http://openapi.gbis.go.kr/ws/rest/busarrivalservice/station?serviceKey=mprvOxIo4u5PCVIVStlnRI6rMDBmJGRvC6%2BNurrGAkl0Ctsmt7UJxU9XwMwP4IOAuaRxjScQ2hGKaDm1n1z%2BgA%3D%3D&" +
                        "stationId=" + stationId + "&numOfRows=999&pageSize=999&pageNo=1&startPage=1")
                .build();

        Response response = client.newCall(request).execute();
        String xml = response.body().string();
        Log.d("ArrivalTimeParser", xml);

        List<ArrivingBus[]> resultList = xmlToObject(xml);

        // TODO routeList의 모든 route에 대해 resultList에서 가져온 시간 값으로 매핑함.
        for(int i = 0; i<routeList.size(); i++) {
            String routeId = routeList.get(i).getRouteId();
            for(int j = 0; j<resultList.size(); j++) {
                ArrivingBus[] resultBuses = resultList.get(j);
                if(resultBuses[0].getRouteId().equals(routeId)) {
                    routeList.get(i).setBus1(resultBuses[0]);
                    routeList.get(i).setBus2(resultBuses[1]);
                    Log.d("ArrivalTimeParser", "setBus1 : " + resultBuses[0].toString());
                    Log.d("ArrivalTimeParser", "setBus2 : " + resultBuses[1].toString());
                }
            }
        }
        return routeList;
    }
}

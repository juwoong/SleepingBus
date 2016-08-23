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
                    xpp.next();
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
                    if(tag.equals("delayYn1")) {
                        buses = new ArrivingBus[2];
                        bus1 = new ArrivingBus();
                        bus2 = new ArrivingBus();
                    } else if(tag.equals("locationNo1")) {
                        if(xpp.getText() == null) bus1.setNumOfStationsToWait("알 수 없음");
                        else bus1.setNumOfStationsToWait(xpp.getText());
                    }
                    else if(tag.equals("locationNo2")) {
                        if(xpp.getText() == null) bus2.setNumOfStationsToWait("알 수 없음");
                        else bus2.setNumOfStationsToWait(xpp.getText());
                    }
                    else if(tag.equals("plateNo1")) { bus1.setPlateNo(xpp.getText()); }
                    else if(tag.equals("plateNo2")) { bus2.setPlateNo(xpp.getText()); }
                    else if(tag.equals("predictTime1")) {
                        if(xpp.getText() == null) bus1.setTimeToWait(-1);
                        else bus1.setTimeToWait(Integer.parseInt(xpp.getText()));
                    }
                    else if(tag.equals("predictTime2")) {
                        if(xpp.getText() == null) bus2.setTimeToWait(-1);
                        else bus2.setTimeToWait(Integer.parseInt(xpp.getText()));
                    }
                    else if(tag.equals("routeId")) {
                        bus1.setRouteId(xpp.getText());
                        bus2.setRouteId(xpp.getText());
                    } else if(tag.equals("resultMessage")) {
                        if(!xpp.getText().equals("정상적으로 처리되었습니다.")) {
                            Log.d("ArrivalTimeParser", "정상적으로 처리 안됨");
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tag = xpp.getName();
                    if(tag.equals("stationId")) {
                        buses[0] = bus1;
                        buses[1] = bus2;
                        Log.d("ArrivalTimeParser", "" + list.size() + "_" + "buses[0] : " + bus1.toString());
                        Log.d("ArrivalTimeParser", "" + list.size() + "_" + "buses[1] : " + bus2.toString());
                        list.add(buses);
                        Log.d("ArrivialTimeParser", "------added! current list is..---------");
                        for(int i = 0; i<list.size(); i++) {
                            Log.d("return", "" + i + "_0 : "  + list.get(i)[0].toString());
                            Log.d("return", "" + i + "_1 : " + list.get(i)[1].toString());
                        }
                        buses = new ArrivingBus[2];
                        bus1 = new ArrivingBus();
                        bus2 = new ArrivingBus();
                    }
                    break;
            }
            eventType = xpp.next();
        }

        //debug
        Log.d("ArrivalTimeParser", "------xmlToObject returns ---------");
        for(int i = 0; i<list.size(); i++) {
            Log.d("return", "" + i + "_0 : "  + list.get(i)[0].toString());
            Log.d("return", "" + i + "_1 : " + list.get(i)[1].toString());
        }

        return list;
    }

    private ArrivingBus[] xmlToObject_old(String xml) throws XmlPullParserException, IOException {
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
                    xpp.next();
                    Log.i("tag", tag + " : " + xpp.getText());

                    if(tag.equals("plateNo1")) { bus1.setPlateNo(xpp.getText()); }
                    else if(tag.equals("plateNo2")) { bus2.setPlateNo(xpp.getText()); }
                    else if(tag.equals("predictTime1")) { bus1.setTimeToWait(Integer.parseInt(xpp.getText())); }
                    else if(tag.equals("predictTime2")) { bus2.setTimeToWait(Integer.parseInt(xpp.getText())); }
                    else if(tag.equals("locationNo1")) { bus1.setNumOfStationsToWait(xpp.getText()); }
                    else if(tag.equals("locationNo2")) { bus2.setNumOfStationsToWait(xpp.getText()); }
                    else if(tag.equals("resultCode")) {
                        Log.d("ERROR", "param ERROR");
                        if("5".equals(xpp.getText())) throw new XmlPullParserException("parameter error");
                    }

                    break;
            }
            eventType = xpp.next();
        }
        buses[0] = bus1;
        buses[1] = bus2;

        //debug
        Log.i("xmlToObject_old", "buses[0] : " + buses[0].toString());
        Log.i("xmlToObject_old", "buses[1] : " + buses[1].toString());

        return buses;
    }

    // todo stationId만 가지고 모든 노선별 도착정보 가져오자. 노선별 도착정보는 이미 가지고있는 BusRoute List에 매핑하자.
    public List<BusRoute> fillRouteListByStationId(String stationId, List<BusRoute> routeList) throws IOException, XmlPullParserException{
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
        Log.d("ArrivialTimeParser", "------ mapping.. ---------");
        for(int i = 0; i<routeList.size(); i++) {
            String routeId = routeList.get(i).getRouteId();
            Log.d("ArrivalTimeParser", "routeId : " + routeId);
            for(int j = 0; j<resultList.size(); j++) {
                ArrivingBus[] resultBuses = resultList.get(j);
                Log.d("ArrivalTimeParser", "resultBuses[0].getRouteId() : " + resultBuses[0].getRouteId());
                if(resultBuses[0].getRouteId().equals(routeId)) {
                    resultBuses[0].setRouteId(routeList.get(i).getRouteId());
                    resultBuses[0].setRouteId(routeList.get(i).getRouteId());
                    resultBuses[1].setRouteName(routeList.get(i).getRouteName());
                    resultBuses[1].setRouteName(routeList.get(i).getRouteName());
                    Log.d("ArrivalTimeParser", "setBus " + j + "_1 : " + resultBuses[0].toString());
                    Log.d("ArrivalTimeParser", "setBus " + j + "_2 : " + resultBuses[1].toString());

                    routeList.get(i).setBus1(resultBuses[0]);
                    routeList.get(i).setBus2(resultBuses[1]);
                }
            }
        }
        return routeList;
    }

    public ArrivingBus[] parse(String stationId, String routeId) throws IOException, XmlPullParserException{

        Request request = new Request.Builder()
                .url("http://openapi.gbis.go.kr/ws/rest/busarrivalservice?serviceKey=mprvOxIo4u5PCVIVStlnRI6rMDBmJGRvC6%2BNurrGAkl0Ctsmt7UJxU9XwMwP4IOAuaRxjScQ2hGKaDm1n1z%2BgA%3D%3D&stationId=" + stationId + "&routeId=" + routeId + "&numOfRows=999&pageSize=999&pageNo=1&startPage=1")
                .build();

        Response response = client.newCall(request).execute();
        String xml = response.body().string();
        Log.d("XML", xml);
        return xmlToObject_old(xml);
    }
}

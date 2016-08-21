package kr.tamiflus.sleepingbus.values;

import java.util.HashMap;

/**
 * Created by 김정욱 on 2016-08-21.
 */

public class BusRouteType {
    public static String RouteTypeToString(String code) {
        switch(code) {
            case "0":
                return "일반";
            case "11":
                return "직행좌석형시내버스";
            case "12":
                return "좌석형시내버스";
            case "13":
                return "일반형시내버스";
            case "14":
                return "광역급행형시내버스";
            case "15":
                return "따복형시내버스";
            case "23":
                return "일반형농어촌버스";
            case "30":
                return "마을버스";
            case "41":
                return "고속형시외버스";
            default:
                return "알수없음";
        }
    }
}

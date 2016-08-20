package kr.tamiflus.sleepingbus.utils;

import java.util.List;

import kr.tamiflus.sleepingbus.structs.BusStation;

/**
 * Created by 김정욱 on 2016-08-20.
 */

public class BusStationToStrArray {
    public static String[] listToArr(BusStation st) {
        String[] strarr = {
                st.getName(), st.getId(), st.getRegion(), st.getCode(), st.getX(), st.getY(), st.getDist()
        };
        return strarr;
    }

    public static BusStation arrToList(String[] strarr) {
        BusStation st = new BusStation();
        st.setName(strarr[0]);
        st.setId(strarr[1]);
        st.setRegion(strarr[2]);
        st.setCode(strarr[3]);
        st.setX(strarr[4]);
        st.setY(strarr[5]);
        st.setDist(strarr[6]);
        return st;
    }
}

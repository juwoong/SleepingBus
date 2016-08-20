package kr.tamiflus.sleepingbus.utils;

import java.util.HashMap;
import java.util.Map;

import kr.tamiflus.sleepingbus.R;

/**
 * Created by tamiflus on 16. 8. 20..
 */
public class ColorMap {
    public static HashMap<String, Integer> byID = new HashMap<String, Integer>() {{
        put("13", R.color.normal);
        put("12", R.color.juaseok);
        put("11", R.color.jikhang);
        put("14", R.color.jikhang);
        put("41", R.color.siwae);
        put("15", R.color.ddabeok);
        put("23", R.color.neonger);
        put("30", R.color.country);
    }};
}

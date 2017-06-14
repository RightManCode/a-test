package com.wishland.www.wanhaohuimaterialdesign.utils;

import com.wishland.www.wanhaohuimaterialdesign.app.Constants;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gerry on 2017/5/2. 0
 */

public class NetUtils {
    public static Map<String, String> getParamsPro() {
        Map<String, String> map = new HashMap<>();
        return getParamsPro(map);
    }

    public static Map<String, String> getParamsPro(Map<String, String> map) {
        map.put("time", TimeUtils.getSeconds());
        map.put("token", UserHandler.getToken());
        map.put("version", Constants.VERSION);
        String sign = encryption(map);
        map.put("signature", MD5Utils.toMD5(sign));
        return map;
    }

    private static String encryption(Map<String, String> map) {
        Set<String> strings = map.keySet();
        List<String> list = new ArrayList<>(strings);
        Collections.sort(list);
        StringBuilder stringBuffer = new StringBuilder();
        for (String s :
                list) {
            stringBuffer.append(s).append(map.get(s));
        }
        stringBuffer.append(Constants.KEYSTORE);
        return stringBuffer.toString();
    }

}

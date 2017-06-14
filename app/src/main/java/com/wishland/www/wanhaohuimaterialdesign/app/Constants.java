package com.wishland.www.wanhaohuimaterialdesign.app;

import java.io.File;

/**
 * Created by codeest on 2016/8/3.
 */
public class Constants {

    public static final String VERSION = "1";
    public static final String KEYSTORE = "4414c5d94ca24942bad650c18ecf49a5";

    //================= TYPE ====================

    public static final int TYPE_ZHIHU = 101;

    //================= KEY ====================

    //    public static final String KEY_API = "f95283476506aa756559dd28a56f0c0b"; //需要APIKEY请去 http://apistore.baidu.com/ 申请,复用会减少访问可用次数
    public static final String KEY_API = "52b7ec3471ac3bec6846577e79f20e4c"; //需要APIKEY请去 http://www.tianapi.com/#wxnew 申请,复用会减少访问可用次数

    //================= PATH ====================

    public static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";


    //================= PREFERENCE ====================

    public static final String SP_MANAGER_POINT = "manager_point";

    //================= INTENT ====================

    public static final String IT_VTEX_TOPIC_ID = "id";

    public static final String IT_VTEX_REPLIES_TOP = "top_info";

    public static final String IT_VTEX_NODE_NAME = "node_name";
}

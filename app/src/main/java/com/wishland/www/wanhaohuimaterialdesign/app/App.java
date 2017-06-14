package com.wishland.www.wanhaohuimaterialdesign.app;

import android.app.Application;
import android.util.Log;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.flurry.android.FlurryAgent;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.wishland.www.wanhaohuimaterialdesign.utils.SPUtils;

import java.util.logging.Level;

import static anetwork.channel.download.DownloadManager.TAG;

/**
 * Created by gerry on 2016/8/2.
 */
public class App extends Application {
    private static App instance;

    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "DNG4WTFYWJXV3W59RPRN");
        instance = this;
        SPUtils.init(this);
        TypefaceProvider.registerDefaultIconSets();
        initOkGO();
        initUmeng();
    }

    private void initUmeng() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d(TAG, "onSuccess: deviceToken" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    private void initOkGO() {
        OkGo.init(this);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.put("Accept-Language", "zh-cn,zh,en-0,en;");    //header不支持中文
            OkGo.getInstance()
                    .debug("OkGo", Level.INFO, true)
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                    .setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                    .setRetryCount(3)
                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效
                    .setCertificates()                               //方法一：信任所有证书,不安全有风险
                    .addCommonHeaders(headers); //设置全局公共头
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

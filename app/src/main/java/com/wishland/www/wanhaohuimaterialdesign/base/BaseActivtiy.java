package com.wishland.www.wanhaohuimaterialdesign.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;
import com.umeng.message.PushAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import static anet.channel.util.Utils.context;

/**
 * Created by gerry on 2017/4/29.
 */

public abstract class BaseActivtiy extends AutoLayoutActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            initVariable();
            super.onCreate(savedInstanceState);
            initDate();
            setupWindowAnimations();
            PushAgent.getInstance(context).onAppStart();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    protected void setupWindowAnimations() {
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(2000);
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
        getWindow().setEnterTransition(slideTransition);
        getWindow().setReturnTransition(slideTransition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }

    protected abstract void initVariable();

    protected abstract void initDate();

}

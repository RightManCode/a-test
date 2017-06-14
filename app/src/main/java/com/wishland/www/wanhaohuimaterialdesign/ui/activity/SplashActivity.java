package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseActivtiy;
import com.wishland.www.wanhaohuimaterialdesign.utils.SPUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.GlobalHandler;

public class SplashActivity extends BaseActivtiy {

    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (isFirst) {
                    SPUtils.putBoolean("isFirst", false);
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }.start();
    }

    @Override
    protected void initVariable() {
        GlobalHandler.refreshGlobalData();
        GlobalHandler.refreshWallet();
    }

    @Override
    protected void initDate() {
        isFirst = SPUtils.getBoolean("isFirst", true);
    }
}

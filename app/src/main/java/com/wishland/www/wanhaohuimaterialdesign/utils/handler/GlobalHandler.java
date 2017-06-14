package com.wishland.www.wanhaohuimaterialdesign.utils.handler;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.GlobalData;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.TransferAccountsLimit;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.wishland.www.wanhaohuimaterialdesign.model.net.Api.GLOBAL_DATA;

/**
 * Created by gerry on 2017/5/29.
 */

public class GlobalHandler {
    private static GlobalData globalData;
    private static final String GLOBALDATA = "GLOBALDATA";
    private static final String WALLET = "WALLET";
    private static List<TransferAccountsLimit.WalletBean> wallet;


    public static void refreshWallet() {
        if (UserHandler.isLogin())
            OkGo.post(Api.TRANSFER_ACCOUNTS_LIMIT)
                    .params(NetUtils.getParamsPro())
                    .execute(new AbsCallbackPro() {
                        @Override
                        public void onBefore(BaseRequest request) {

                        }

                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            updateWallet(s);
                        }
                    });
    }

    private static void updateWallet(String s) {
        TransferAccountsLimit transferAccountsLimit = new Gson().fromJson(s, TransferAccountsLimit.class);
        SPUtils.putString(WALLET, s);
    }

    public static List<TransferAccountsLimit.WalletBean> getWallet() {
        if (wallet == null) {
            String s = SPUtils.getString(WALLET);
            if (TextUtils.isEmpty(s))
                wallet = new ArrayList<>();
            else
                wallet = new Gson().fromJson(s, TransferAccountsLimit.class).getWallet();
        }
        return wallet;
    }

    public static void refreshGlobalData() {
        OkGo.post(GLOBAL_DATA)
                .params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        updateGlobalData(s);
                    }
                });
    }

    private static void updateGlobalData(String s) {
        GlobalHandler.globalData = new Gson().fromJson(s, GlobalData.class);
        SPUtils.putString(GLOBAL_DATA, s);
    }

    public static GlobalData getGlobaldata() {
        if (globalData == null) {
            String s = SPUtils.getString(GLOBALDATA);
            if (TextUtils.isEmpty(s))
                globalData = new GlobalData();
            else
                globalData = new Gson().fromJson(s, GlobalData.class);
        }
        return globalData;
    }

}

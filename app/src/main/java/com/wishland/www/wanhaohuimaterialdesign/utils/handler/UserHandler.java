package com.wishland.www.wanhaohuimaterialdesign.utils.handler;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.AccountInfo;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.RemenberUser;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.UserInfo;
import com.wishland.www.wanhaohuimaterialdesign.model.event.UserEvent;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gerry on 2017/5/4.
 */

public class UserHandler {

    private static UserInfo userInfo;
    private static String LOGIN_USER = "UserInfo";
    private static String REMENBER_USER = "REMENBER_USER";


    public static void updateUser(UserInfo userInfo) {
        UserHandler.userInfo = userInfo;
        SPUtils.putString(LOGIN_USER, new Gson().toJson(userInfo));
    }

    public static void updateUser() {
        SPUtils.putString(LOGIN_USER, new Gson().toJson(getUserInfo()));
    }


    public static void login() {
        UserInfo userInfo = getUserInfo();
        userInfo.setLogin(true);
        updateUser(userInfo);
        EventBus.getDefault().post(new UserEvent(true));
        MessageHandler.refreshData();
    }

    public static void logout() {
        userInfo = null;
        SPUtils.remove(LOGIN_USER);
        EventBus.getDefault().post(new UserEvent(false));
        MessageHandler.setUnReadMsg(0);
    }


    public static UserInfo getUserInfo() {
        if (userInfo == null) {
            String s = SPUtils.getString(LOGIN_USER);
            if (TextUtils.isEmpty(s))
                userInfo = new UserInfo();
            else
                userInfo = new Gson().fromJson(s, UserInfo.class);
        }
        return userInfo;
    }


    public static boolean isLogin() {
        return getUserInfo() != null && getUserInfo().isLogin();
    }


    public static String getToken() {
        return getUserInfo().getToken() == null ? "null" : getUserInfo().getToken();
    }

    public static void setToken(String token) {
        UserInfo userInfo = getUserInfo();
        userInfo.setToken(token);
    }


    public static void refreshUserInfo() {
        if (isLogin()) {
            OkGo.post(Api.GET_ACCOUNT_INFO)
                    .params(NetUtils.getParamsPro())
                    .execute(new AbsCallbackPro() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            AccountInfo accountInfo = new Gson().fromJson(s, new TypeToken<AccountInfo>() {
                            }.getType());
                            UserInfo userInfo = getUserInfo();
                            userInfo.setAccountInfo(accountInfo);
                            updateUser(userInfo);
                            EventBus.getDefault().post(userInfo);
                        }
                    });
        }
    }

    public static RemenberUser getRemenberUser() {
        String string = SPUtils.getString(REMENBER_USER);
        if (TextUtils.isEmpty(string))
            return null;
        else
            return new Gson().fromJson(string, RemenberUser.class);
    }

    public static void remenberUser(String name, String pw) {
        SPUtils.putString(REMENBER_USER, new Gson().toJson(new RemenberUser(name, pw)));
    }

    public static void cleanRemnberUser() {
        SPUtils.remove(REMENBER_USER);
    }
}

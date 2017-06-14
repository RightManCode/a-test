package com.wishland.www.wanhaohuimaterialdesign.model.net;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.OkGoException;
import com.wishland.www.wanhaohuimaterialdesign.app.App;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.LoginActivity;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gerry on 2017/5/2.
 */

public abstract class AbsCallbackPro extends AbsCallback<String> {
    @Override
    public String convertSuccess(Response response) throws Exception {
        String string = response.body().string();
        if (TextUtils.isEmpty(string))
            throw new OkGoException("服务器错误");
        JSONObject jsonObject = new JSONObject(string);
        try {
            if (!TextUtils.isEmpty(jsonObject.getString("token")))
                UserHandler.setToken(jsonObject.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (200 == (jsonObject.getInt("status"))) {
            return jsonObject.getString("data");
        } else if (301 == (jsonObject.getInt("status"))) {
            Intent intent = new Intent(App.getInstance(), LoginActivity.class);
            intent.putExtra("toast", "用户登录状态异常，请重新登录");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getInstance().startActivity(intent);
            UserHandler.logout();
            return "";
        } else {
            throw new OkGoException(String.valueOf(jsonObject.getString("errorMsg")));
        }
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        if (e instanceof OkGoException)
            Toast.makeText(App.getInstance(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

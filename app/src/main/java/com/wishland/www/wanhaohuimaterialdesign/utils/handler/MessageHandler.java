package com.wishland.www.wanhaohuimaterialdesign.utils.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.Message;
import com.wishland.www.wanhaohuimaterialdesign.model.event.MessageEvent;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gerry on 2017/6/9.
 */

public class MessageHandler {

    private static int unReadMsg;

    public static int getUnReadMsg() {
        return unReadMsg;
    }

    public static void setUnReadMsg(int unReadMsg) {
        MessageHandler.unReadMsg = unReadMsg;
        EventBus.getDefault().post(new MessageEvent(unReadMsg));
    }

    public static void refreshData() {
        if (UserHandler.isLogin()) {
            Map<String, String> map = new HashMap<>();
            map.put("queryCnt", "1");
            map.put("queryId", "0");
            OkGo.post(Api.MESSAGE)
                    .params(NetUtils.getParamsPro(map))
                    .execute(new AbsCallbackPro() {

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {

                        }

                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(s);
                                if (jsonObject.getInt("queryId") > 0) {
                                    Message message = new Gson().fromJson(s, new TypeToken<Message>() {
                                    }.getType());
                                    unReadMsg = message.getUnReadMsg();
                                    EventBus.getDefault().post(new MessageEvent(unReadMsg));
                                } else {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }
    }

}

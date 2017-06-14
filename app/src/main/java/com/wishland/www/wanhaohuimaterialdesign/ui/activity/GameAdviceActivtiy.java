package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.wenchao.cardstack.CardStack;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.GameAdvice;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.ImageLoader;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class GameAdviceActivtiy extends BaseStyleActivity {

    @BindView(R.id.container)
    CardStack container;
    @BindView(R.id.btn_add)
    ImageButton btnAdd;
    private CardsDataAdapter mCardAdapter;
    private ArrayList<GameAdvice> gameAdvices;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_advice_activtiy, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        container.setContentResource(R.layout.item_game_advice);
        container.setStackMargin(20);
        container.setEnableLoop(true);
        mCardAdapter = new CardsDataAdapter(getApplicationContext(), R.layout.item_game);
        container.setAdapter(mCardAdapter);
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {
        OkGo
                .post(Api.GAME_ADVICE)
                .params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        gameAdvices = new Gson().fromJson(s, new TypeToken<ArrayList<GameAdvice>>() {
                        }.getType());
                        for (GameAdvice gameAdvice :
                                gameAdvices) {
                            mCardAdapter.add(gameAdvice);
                        }
                    }
                });

    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        if (gameAdvices.size() <= 0)
            return;
        Map<String, String> map = new HashMap<>();
        map.put("rid", gameAdvices.get(container.getCurrIndex() % gameAdvices.size()).getId());
        OkGo.
                post(Api.ADD_COLLECTION)
                .params(NetUtils.getParamsPro(map))
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        materialDialog = new MaterialDialog.Builder(GameAdviceActivtiy.this)
                                .title("转换中")
                                .content("操作中,请稍后.....")
                                .progress(true, 0)
                                .show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                    }
                });
    }

    private class CardsDataAdapter extends ArrayAdapter<GameAdvice> {
        CardsDataAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, final View contentView, @NonNull ViewGroup parent) {
            TextView v = (TextView) (contentView.findViewById(R.id.tv_ganme_name));
            v.setText(getItem(position).getName());
            ImageLoader.load(GameAdviceActivtiy.this, getItem(position).getImg(), (ImageView) contentView.findViewById(R.id.iv_game));
            return contentView;
        }

    }
}

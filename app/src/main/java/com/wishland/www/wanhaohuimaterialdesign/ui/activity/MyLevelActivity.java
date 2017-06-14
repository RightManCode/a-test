package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.Level;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.ImageLoader;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class MyLevelActivity extends BaseStyleActivity {


    @BindView(R.id.iv_level)
    ImageView ivLevel;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.pb_level)
    ProgressBar pbLevel;
    @BindView(R.id.tv_zyxtze)
    TextView tvZyxtze;
    @BindView(R.id.tv_zcke)
    TextView tvZcke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_level, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {
        OkGo.post(Api.LEVEL).params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Level level = new Gson().fromJson(s, new TypeToken<Level>() {
                        }.getType());
                        ImageLoader.load(MyLevelActivity.this, level.getImg(), ivLevel);
                        tvLevel.setText(level.getLevelname());
                        String levelNum = "0";
                        if (level.getLevelname().length() == 4) {
                            levelNum = level.getLevelname().substring(3);
                        } else if (level.getLevelname().length() == 5) {
                            levelNum = level.getLevelname().substring(3, 5);
                        }
                        int i = Integer.parseInt(levelNum) * 10;
                        pbLevel.setProgress(i);
                        tvZcke.setText(String.valueOf(level.getZtzje()));
                        tvZyxtze.setText(String.valueOf(level.getCkje()));

                    }
                });
    }
}

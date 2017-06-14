package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.MyCollect;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.ImageLoader;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class MyCollectActivity extends BaseStyleActivity {

    @BindView(R.id.rec)
    RecyclerView rec;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private BaseQuickAdapter<MyCollect, BaseViewHolder> adapter;
    private ArrayList<MyCollect> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        rec.setLayoutManager(new LinearLayoutManager(this));
        getContentView().showLoading();
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        rec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<MyCollect, BaseViewHolder>(R.layout.item_game) {
            @Override
            protected void convert(BaseViewHolder helper, MyCollect item) {
                helper.setText(R.id.tv_ganme_name, item.getName());
                ImageLoader.load(MyCollectActivity.this, item.getImg().replace("&", "_"), (ImageView) helper.getView(R.id.iv_game));
            }

        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (UserHandler.isLogin()) {
                    Intent intent = new Intent(MyCollectActivity.this, WebActivity.class);
                    intent.putExtra("url", gameList.get(position).getUrl());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(MyCollectActivity.this, LoginActivity.class));
                }
            }
        });
        rec.setAdapter(adapter);
        getContentView().showLoading();
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {
        refreshData();
    }

    private void refreshData() {
        OkGo.post(Api.GET_COLLECTION)
                .params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        srl.setRefreshing(false);
                        if (e != null && e instanceof OkGoException) {
                            getContentView().showEmpty(R.drawable.arrow, "出错了", e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            gameList = new Gson().fromJson(s, new TypeToken<ArrayList<MyCollect>>() {
                            }.getType());
                            adapter.setNewData(gameList);
                            getContentView().showContent();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}

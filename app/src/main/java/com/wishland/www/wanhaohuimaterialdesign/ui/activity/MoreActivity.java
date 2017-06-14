package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.More;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class MoreActivity extends BaseStyleActivity {

    @BindView(R.id.rec)
    RecyclerView rec;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private BaseQuickAdapter<More.ActivityBean, BaseViewHolder> adapter;
    private List<More.ActivityBean> activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        rec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<More.ActivityBean, BaseViewHolder>(R.layout.item_more) {

            @Override
            protected void convert(BaseViewHolder helper, More.ActivityBean item) {
                helper.setText(R.id.tv_more, item.getTitle());
                Glide.with(MoreActivity.this).load(item.getImgUrl()).placeholder(R.drawable.loading_pic).into((ImageView) helper.getView(R.id.iv_more));
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MoreActivity.this, WebActivity.class);
                intent.putExtra("url", activity.get(position).getUrl());
                startActivity(intent);
            }
        });
        rec.setAdapter(adapter);
        getContentView().showLoading();
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {
        refreshData();
    }

    private void refreshData() {
        OkGo.post(Api.MORE).params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (!TextUtils.isEmpty(s)) {
                            More more = new Gson().fromJson(s, new TypeToken<More>() {
                            }.getType());
                            activity = more.getActivity();
                            if (activity.size() > 0) {
                                getContentView().showContent();
                                adapter.setNewData(activity);
                            } else {
                                getContentView().showEmpty(R.drawable.arrow, "暂无数据", "稍后刷新试试吧");
                            }
                        }
                    }
                });
    }
}
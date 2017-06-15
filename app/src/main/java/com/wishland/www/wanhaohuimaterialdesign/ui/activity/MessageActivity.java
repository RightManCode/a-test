package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.Message;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.MessageHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class MessageActivity extends BaseStyleActivity {

    private static final int MESAGE_DETAIL = 720;
    @BindView(R.id.rec)
    RecyclerView rec;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private String queryId = "0";
    private BaseQuickAdapter<Message.DataListBean, BaseViewHolder> adapter;
    private List<Message.DataListBean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryId = "0";
                refreshData();
            }
        });
        rec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<Message.DataListBean, BaseViewHolder>(R.layout.item_message, dataList) {


            @Override
            protected void convert(BaseViewHolder helper, Message.DataListBean item) {
                helper
                        .setText(R.id.tv_title, item.getTitle())
                        .setText(R.id.tv_content, item.getDetailedInfo())
                        .setText(R.id.tv_time, item.getTime());
                if (!TextUtils.isEmpty(item.getFrom()))
                    helper.setText(R.id.tv_from, item.getFrom());
                if (item.getIsNew() == 1)
                    helper.setVisible(R.id.iv_unread, true);
                else
                    helper.setVisible(R.id.iv_unread, false);

            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MessageActivity.this, MessageDetailActivity.class);
                intent.putExtra("message", dataList.get(position));
                intent.putExtra("position", position);
                startActivityForResult(intent, MESAGE_DETAIL);
            }
        });
        rec.setAdapter(adapter);
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {
        refreshData();
    }

    private void refreshData() {
        Map<String, String> map = new HashMap<>();
        map.put("queryCnt", "50");
        map.put("queryId", queryId);
        OkGo.post(Api.MESSAGE)
                .params(NetUtils.getParamsPro(map))
                .execute(new AbsCallbackPro() {


                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if (e != null && e instanceof OkGoException) {
                            getContentView().showEmpty(R.drawable.arrow, "出错了", e.getMessage());
                        }
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("queryId") > 0) {
                                Message message = new Gson().fromJson(s, new TypeToken<Message>() {
                                }.getType());
                                MessageHandler.setUnReadMsg(message.getUnReadMsg());
                                dataList = message.getDataList();
                                adapter.setNewData(dataList);
                                getContentView().showContent();
                            } else {
                                getContentView().showEmpty(R.drawable.arrow, "暂无数据", "稍后刷新试试吧");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MESAGE_DETAIL:
                    int position = data.getIntExtra("position", -1);
                    if (position > 0) {
                        dataList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

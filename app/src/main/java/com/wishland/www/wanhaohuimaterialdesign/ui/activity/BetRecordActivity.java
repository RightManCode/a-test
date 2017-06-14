package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.Record;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static com.wishland.www.wanhaohuimaterialdesign.R.id.tv_record1;

//activity_bet_record
public class BetRecordActivity extends BaseStyleActivity {
    @BindView(R.id.rec)
    RecyclerView recRecord;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private String startTime;
    private String endTime;
    private String type;
    private BaseQuickAdapter<Record, BaseViewHolder> adapter;
    private String queryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_record, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryId = "0";
                refreshData();
            }
        });
        recRecord.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<Record, BaseViewHolder>(R.layout.item_reccord) {
            @Override
            protected void convert(BaseViewHolder helper, Record item) {
                helper.setText(tv_record1, "流水号：" + item.getSerialNum() + (TextUtils.isEmpty(item.getNotes()) ? "" : "\n" + item.getNotes()));
                helper.setText(R.id.tv_record2, "美东时间：" + item.getUsaTime());
                helper.setText(R.id.tv_record3, "北京时间：" + item.getChinaTime());
                helper.setText(R.id.tv_record4, "金额：" + item.getSum());
                if (0 == item.getType()) {
                    helper.setText(R.id.tv_record5, "成功");
                    helper.setTextColor(R.id.tv_record5, getResources().getColor(R.color.green));
                } else {
                    helper.setText(R.id.tv_record5, "失败");
                    helper.setTextColor(R.id.tv_record5, getResources().getColor(R.color.red));
                }
                if (!TextUtils.isEmpty(item.getPoints())) {
                    helper.setText(R.id.tv_record6, "手续费：" + item.getPoints());
                } else {
                    helper.setText(R.id.tv_record6, "");
                }
            }
        };
        recRecord.setAdapter(adapter);
        getContentView().showLoading();
    }

    @Override
    protected void initVariable() {
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        type = getIntent().getStringExtra("type");
        queryId = "0";
    }

    @Override
    protected void initDate() {
        refreshData();
    }

    private void refreshData() {
        final String queryCnt = "500";
        String Subtype = "0";
        Map<String, String> map = new HashMap<>();
        map.put("start", startTime);
        map.put("queryCnt", queryCnt);
        map.put("queryid", queryId);
        map.put("end", endTime);
        map.put("subtype", Subtype);
        map.put("type", type);
        OkGo.post(Api.TRADE_QUERY)
                .params(NetUtils.getParamsPro(map))
                .tag(this)
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
                        srl.setRefreshing(false);
                        if (e != null && e instanceof OkGoException) {
                            getContentView().showEmpty(R.drawable.arrow, "出错了", e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("queryid") > 0) {
                                queryId = String.valueOf(jsonObject.getInt("queryid"));
                                String commonList = jsonObject.getString("commonList");
                                String otherList = jsonObject.getString("otherList");
                                if (!TextUtils.isEmpty(commonList) && TextUtils.isEmpty(otherList)) {
                                    ArrayList<Record> records = new Gson().fromJson(commonList, new TypeToken<ArrayList<Record>>() {
                                    }.getType());
                                    setTitle("投注记录（共" +  records.size() + "条）");
                                    adapter.setNewData(records);
                                } else if (TextUtils.isEmpty(commonList) && !TextUtils.isEmpty(otherList)) {
                                    ArrayList<Record> records = new Gson().fromJson(otherList, new TypeToken<ArrayList<Record>>() {
                                    }.getType());
                                    setTitle("投注记录（共" + records.size() + "条）");
                                    adapter.setNewData(records);
                                } else {
                                    getContentView().showEmpty(R.drawable.arrow, "数据错误", "稍后刷新试试吧");
                                }
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
}

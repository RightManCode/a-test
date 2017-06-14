package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.AccountToAccountRecode;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.TransferAccountsLimit;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.GlobalHandler;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static com.wishland.www.wanhaohuimaterialdesign.R.id.tv_record1;

public class AccountToAccountRecordActivtiy extends BaseStyleActivity {

    @BindView(R.id.rec)
    RecyclerView rec;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private String start;
    private String end;
    private String from;
    private String to;
    private String queryId;
    private BaseQuickAdapter<AccountToAccountRecode.ListBean, BaseViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_to_account_record_activtiy, R.layout.base_toolbar_back);
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
        adapter = new BaseQuickAdapter<AccountToAccountRecode.ListBean, BaseViewHolder>(R.layout.item_reccord) {

            @Override
            protected void convert(BaseViewHolder helper, AccountToAccountRecode.ListBean item) {
                /**
                 * uid : 68352
                 * usaTime : 2017-06-12 03:21:44
                 * chinaTime : 2017-06-11 15:21:44
                 * from : wallet
                 * to : bbin
                 * serialNum : 2017061203214453805
                 * sum : 100
                 * desc : 已处理
                 * status : 1
                 * result : 转账成功
                 */

                helper.setText(tv_record1, "流水号：" + item.getSerialNum());
                helper.setText(R.id.tv_record2, "美东时间：" + item.getUsaTime());
                helper.setText(R.id.tv_record3, "北京时间：" + item.getChinaTime());
                helper.setText(R.id.tv_record4, "金额：" + item.getSum());
                if (TextUtils.equals("转账成功", item.getResult())) {
                    helper.setText(R.id.tv_record5, item.getResult() + "(" + item.getDesc() + ")");
                    helper.setTextColor(R.id.tv_record5, getResources().getColor(R.color.green));
                } else {
                    helper.setText(R.id.tv_record5, item.getResult() + "(" + item.getDesc() + ")");
                    helper.setTextColor(R.id.tv_record5, getResources().getColor(R.color.red));
                }
                String s = "";
                List<TransferAccountsLimit.WalletBean> wallet = GlobalHandler.getWallet();
                for (TransferAccountsLimit.WalletBean walletBean :
                        wallet) {
                    if (TextUtils.equals(from, walletBean.getWallettype())) {
                        s = s + walletBean.getName();
                    }
                    if (!s.contains("->"))
                        s = s + "->";
                    if (TextUtils.equals(to, walletBean.getWallettype()))
                        s = s + walletBean.getName();
                }
                helper.setText(R.id.tv_record6, s);
            }
        };
        rec.setAdapter(adapter);
        getContentView().showLoading();
    }

    @Override
    protected void initVariable() {
        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        queryId = "0";
    }

    @Override
    protected void initDate() {
        refreshData();
    }

    private void refreshData() {

        HashMap<String, String> params = new HashMap<>();
        params.put("queryCnt", "50");
        params.put("start", start);
        params.put("end", end);
        params.put("from", from);
        params.put("to", to);
        params.put("queryid", queryId);
        OkGo
                .post(Api.QUERY_EXCHANGE)
                .params(NetUtils.getParamsPro(params))
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
                            AccountToAccountRecode accountToAccountRecode = new Gson().fromJson(s, AccountToAccountRecode.class);
                            if (accountToAccountRecode.getQueryid() > 0) {
                                setTitle("额度转换（共" + accountToAccountRecode.getList().size() + "条）");
                                adapter.setNewData(accountToAccountRecode.getList());
                                getContentView().showContent();
                            } else {
                                getContentView().showEmpty(R.drawable.arrow, "暂无数据", "稍后刷新试试吧");
                            }
                        } catch (JsonSyntaxException e) {
                            getContentView().showEmpty(R.drawable.arrow, "暂无数据", "稍后刷新试试吧");
                            e.printStackTrace();
                        }
                    }
                });
    }
}

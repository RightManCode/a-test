package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.BankList;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class BankListActivity extends BaseStyleActivity {

    @BindView(R.id.rec)
    RecyclerView rec;
    private List<BankList> bankLists;
    private BaseQuickAdapter<BankList, BaseViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        rec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<BankList, BaseViewHolder>(R.layout.item_bank, bankLists) {
            @Override
            protected void convert(BaseViewHolder helper, BankList item) {
                helper.setText(R.id.tv_bank, item.getBankname());
//                Glide.with(BankListActivity.this).load(item.getImg()).into((ImageView) helper.getView(R.id.iv_bank));
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                String bankno = bankLists.get(position).getBankno();
                String bankname = bankLists.get(position).getBankname();
                intent.putExtra("bankno", bankno);
                intent.putExtra("bankname", bankname);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rec.setAdapter(adapter);
    }

    @Override
    protected void initVariable() {
        OkGo.post(Api.BANK_LIST)
                .params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        bankLists = new Gson().fromJson(s, new TypeToken<ArrayList<BankList>>() {
                        }.getType());
                        adapter.setNewData(bankLists);
                    }
                });
    }

    @Override
    protected void initDate() {

    }
}

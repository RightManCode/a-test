package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.UserPayInfo;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.utils.MyTextUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class PayActivity extends BaseStyleActivity {

    @BindView(R.id.tv_user_accont)
    TextView tvUserAccont;
    @BindView(R.id.tv_pay_limite)
    TextView tvPayLimite;
    @BindView(R.id.et_pay_num)
    EditText etPayNum;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private String title;
    private String url;
    private UserPayInfo userPayInfo;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        getContentView().showLoading();
        setTitle(title);
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
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
    }

    @Override
    protected void initDate() {
        refreshData();

    }

    private void refreshData() {
        OkGo
                .post(url)
                .params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        getContentView().showContent();
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        userPayInfo = new Gson().fromJson(s, UserPayInfo.class);
                        tvUserAccont.setText(userPayInfo.getUser().getUsername());
                        tvPayLimite.setText(userPayInfo.getUser().getMoney());
                        etPayNum.setHint("最低额度：" + userPayInfo.getLimit() + "元");
                    }
                });
    }

    @OnClick(R.id.btn_submit_login)
    public void onViewClicked() {
//        http://029256.com/api.d/url_agent.php?token=[token]&url=pay&type=rhlepay&id=93&mony=[money]&code=[code]
        if (userPayInfo == null) {
            Toast.makeText(this, "获取用户信息失败，请下拉刷新", Toast.LENGTH_SHORT).show();
            return;
        }
        if (MyTextUtils.checkEmpty(etPayNum, "请输入金额", this))
            return;
        if (Double.parseDouble(etPayNum.getText().toString().trim()) > Double.parseDouble(userPayInfo.getUser().getMoney())) {
            Toast.makeText(this, "充值金额大于总金额", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Double.parseDouble(etPayNum.getText().toString().trim()) < Double.parseDouble(userPayInfo.getLimit())) {
            Toast.makeText(this, "充值金额小于最低额度", Toast.LENGTH_SHORT).show();
            return;
        }
        String payUrl = userPayInfo.getUrl().replace("[money]", etPayNum.getText().toString().trim());
        if (TextUtils.isEmpty(code))
            payUrl = payUrl.replace("&code=[code]", "");
        else
            payUrl = payUrl.replace("[code]", code);
        Intent intent = new Intent(PayActivity.this, WebActivity.class);
        intent.putExtra("url", payUrl);
        startActivity(intent);
    }
}

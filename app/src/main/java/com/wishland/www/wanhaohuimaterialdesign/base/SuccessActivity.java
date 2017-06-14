package com.wishland.www.wanhaohuimaterialdesign.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.wishland.www.wanhaohuimaterialdesign.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessActivity extends BaseStyleActivity {

    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.btn_i_kown)
    Button btnIKown;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        if (TextUtils.isEmpty(money))
            money = "0";
        tvMoney.setText(money);
    }

    @Override
    protected void initVariable() {
        money = getIntent().getStringExtra("money");
    }

    @Override
    protected void initDate() {

    }

    @OnClick(R.id.btn_i_kown)
    public void onViewClicked() {
        finish();
    }
}

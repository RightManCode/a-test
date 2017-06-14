package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReEditPwActivity extends BaseStyleActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_edit_pw, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {

    }


    @OnClick({R.id.btn_login_pw, R.id.btn_trans_pw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_pw:
                startActivity(new Intent(this, ReEditLoginActivity.class));
                break;
            case R.id.btn_trans_pw:
                startActivity(new Intent(this, ReEditTransPwActivity.class));
                break;
        }
    }
}

package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCenterActivity extends BaseStyleActivity {

    @BindView(R.id.iv_head)
    BootstrapCircleThumbnail ivHead;
    @BindView(R.id.btn_sign)
    Button btnSign;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_can_withdraw)
    TextView tvCanWithdraw;
    @BindView(R.id.btn_recharge)
    Button btnRecharge;
    @BindView(R.id.btn_withdraw_now)
    LinearLayout btnWithdrawNow;
    @BindView(R.id.btn_transaction_record)
    LinearLayout btnTransactionRecord;
    @BindView(R.id.btn_betting_withdrawal)
    LinearLayout btnBettingWithdrawal;
    @BindView(R.id.btn_game_advice)
    LinearLayout btnGameAdvice;
    @BindView(R.id.btn_my_level)
    LinearLayout btnMyLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        if (UserHandler.getUserInfo().getAccountInfo() != null) {
            tvBalance.setText(UserHandler.getUserInfo().getAccountInfo().getBalanceInfo().getBalance());
            tvCanWithdraw.setText(String.valueOf(UserHandler.getUserInfo().getAccountInfo().getBalanceInfo().getDrawingBet()));
        }
        UserHandler.refreshUserInfo();
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {

    }

    @OnClick({R.id.btn_sign,
            R.id.btn_recharge,
            R.id.btn_withdraw_now,
            R.id.btn_transaction_record,
            R.id.btn_betting_withdrawal,
            R.id.btn_game_advice,
            R.id.btn_reedit_pw,
            R.id.btn_my_level})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                break;
            case R.id.btn_recharge:
                break;
            case R.id.btn_withdraw_now:
                break;
            case R.id.btn_transaction_record:
                startActivity(new Intent(this, EditTransactionRecordTimeActivity.class));
                break;
            case R.id.btn_betting_withdrawal:
                startActivity(new Intent(this, EditBetRecordTimeActivity.class));
                break;
            case R.id.btn_game_advice:

                break;
            case R.id.btn_my_level:
                startActivity(new Intent(this, MyLevelActivity.class));
                break;
            case R.id.btn_reedit_pw:
                startActivity(new Intent(this, ReEditPwActivity.class));
                break;
        }
    }
}

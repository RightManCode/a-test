package com.wishland.www.wanhaohuimaterialdesign.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseFragment;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.AccountInfo;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.UserInfo;
import com.wishland.www.wanhaohuimaterialdesign.model.event.UserEvent;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.AccontBindActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.EditBetRecordTimeActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.EditTransactionRecordTimeActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.GameAdviceActivtiy;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.LoginActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.MyLevelActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.ReEditPwActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.TMActivity;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

import static com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler.getUserInfo;
import static com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler.isLogin;

/**
 * Created by gerry on 2017/5/2.
 */

public class UserCenterFragment extends BaseFragment {
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
    @BindView(R.id.btn_reedit_pw)
    LinearLayout btnReeditPw;
    @BindView(R.id.fl_login)
    FrameLayout flLogin;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_user_center, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        if (isLogin()) {
            if (getUserInfo().getAccountInfo() != null) {
                tvBalance.setText(getUserInfo().getAccountInfo().getBalanceInfo().getBalance());
                tvCanWithdraw.setText(String.valueOf(getUserInfo().getAccountInfo().getBalanceInfo().getDrawingBet()));
            } else {
                refreshUserInfo();
            }
        }
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUserInfo();
            }
        });
        return inflate;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {

    }

    public void refreshUserInfo() {
        if (UserHandler.isLogin()) {
            OkGo.post(Api.GET_ACCOUNT_INFO)
                    .params(NetUtils.getParamsPro())
                    .execute(new AbsCallbackPro() {
                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
                            srl.setRefreshing(false);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            AccountInfo accountInfo = new Gson().fromJson(s, new TypeToken<AccountInfo>() {
                            }.getType());
                            if (accountInfo != null) {
                                tvBalance.setText(accountInfo.getBalanceInfo().getBalance());
                                tvCanWithdraw.setText(String.valueOf(accountInfo.getBalanceInfo().getDrawingBet()));
                                UserInfo userInfo = UserHandler.getUserInfo();
                                userInfo.setAccountInfo(accountInfo);
                                UserHandler.updateUser(userInfo);
                            }
                        }
                    });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isLogin())
                flLogin.setVisibility(View.GONE);
            refreshUserInfo();
        } else {

        }
    }

    @OnClick({R.id.btn_sign,
            R.id.btn_recharge,
            R.id.btn_withdraw_now,
            R.id.btn_transaction_record,
            R.id.btn_betting_withdrawal,
            R.id.btn_game_advice,
            R.id.btn_reedit_pw,
            R.id.btn_logout,
            R.id.btn_submit_login,
            R.id.btn_my_level, R.id.fl_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                startActivity(new Intent(getActivity(), AccontBindActivity.class));
                break;
            case R.id.btn_recharge:

                break;
            case R.id.btn_withdraw_now:
                startActivity(new Intent(getActivity(), TMActivity.class));
                break;
            case R.id.btn_transaction_record:
                startActivity(new Intent(getActivity(), EditTransactionRecordTimeActivity.class));
                break;
            case R.id.btn_betting_withdrawal:
                startActivity(new Intent(getActivity(), EditBetRecordTimeActivity.class));
                break;
            case R.id.btn_game_advice:
                startActivity(new Intent(getActivity(), GameAdviceActivtiy.class));
                break;
            case R.id.btn_my_level:
                startActivity(new Intent(getActivity(), MyLevelActivity.class));
                break;
            case R.id.btn_reedit_pw:
                startActivity(new Intent(getActivity(), ReEditPwActivity.class));
                break;
            case R.id.btn_logout:
                UserHandler.logout();

                OkGo.post(Api.LOGIN_OUT)
                        .params(NetUtils.getParamsPro()).execute(new AbsCallbackPro() {
                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);

                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                    }
                });
                break;
            case R.id.btn_submit_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.fl_login:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserEvent event) {
        if (event.isLogin()) {
            flLogin.setVisibility(View.GONE);
            refreshUserInfo();
        } else {
            flLogin.setVisibility(View.VISIBLE);
            tvBalance.setText("0.00");
            tvCanWithdraw.setText("0.00");
            flLogin.setVisibility(View.VISIBLE);
        }
    }
}

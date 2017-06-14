package com.wishland.www.wanhaohuimaterialdesign.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseFragment;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.IsSuccess;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.TransferAccountsLimit;
import com.wishland.www.wanhaohuimaterialdesign.model.event.UserEvent;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.LoginActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.WebActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.spinner.NiceSpinner;
import com.wishland.www.wanhaohuimaterialdesign.utils.MyTextUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gerry on 2017/5/1.
 */

public class TransferAccountsFragment extends BaseFragment {
    @BindView(R.id.ns_from)
    NiceSpinner nsFrom;
    @BindView(R.id.ns_to)
    NiceSpinner nsTo;
    @BindView(R.id.et_edit_money)
    EditText etEditMoney;
    Unbinder unbinder;
    @BindView(R.id.btn_transfer_account)
    ImageButton btnTransferAccount;
    @BindView(R.id.fl_login)
    FrameLayout flLogin;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.fl_halo)
    View flHalo;
    @BindView(R.id.btn_submit_login)
    Button btnSubmitLogin;
    private TransferAccountsLimit transferAccountsLimit;
    private MaterialDialog materialDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_accounts, container, false);
        unbinder = ButterKnife.bind(this, view);
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        YoYo.with(Techniques.FadeOut)
                .duration(1500)
                .repeat(Integer.MAX_VALUE)
                .playOn(flHalo);
        return view;
    }

    @Override
    protected void initVariable() {
    }

    @Override
    protected void initDate() {
        if (UserHandler.isLogin())
            refreshData();
    }

    public void refreshData() {
        OkGo.post(Api.TRANSFER_ACCOUNTS_LIMIT)
                .params(NetUtils.getParamsPro())
                .tag(this)
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onBefore(BaseRequest request) {

                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        transferAccountsLimit = new Gson().fromJson(s, new TypeToken<TransferAccountsLimit>() {
                        }.getType());
                        if (transferAccountsLimit != null) {
                            nsFrom.attachDataSource(transferAccountsLimit.getWallet());
                            nsTo.attachDataSource(transferAccountsLimit.getWallet());
                        }
                    }
                });
    }

    private void transferAccounts() {
        Map<String, String> map = new HashMap<>();
        map.put("fromWalletType", transferAccountsLimit.getWallet().get(nsFrom.getSelectedIndex()).getWallettype());
        map.put("toWalletType", transferAccountsLimit.getWallet().get(nsTo.getSelectedIndex()).getWallettype());
        map.put("amount", etEditMoney.getText().toString());
        OkGo.post(Api.TRANSFER_ACCOUNTS)
                .params(NetUtils.getParamsPro(map))
                .tag(this)
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        materialDialog = new MaterialDialog.Builder(getActivity())
                                .title("转换中")
                                .content("操作中,请稍后.....")
                                .progress(true, 0)
                                .show();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                        builder.title("出错了");
                        if (e != null && e instanceof OkGoException) {
                            builder.content(e.getMessage());
                        }
                        builder.negativeText("联系客服")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(getActivity(), WebActivity.class);
                                        intent.putExtra("url", Api.CUSTOMER_SERVICES);
                                        startActivity(intent);
                                    }
                                }).positiveText("知道了").show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        refreshData();
                        IsSuccess isSuccess = new Gson().fromJson(s, new TypeToken<IsSuccess>() {
                        }.getType());
                        if (isSuccess != null && isSuccess.getStatus()) {
                            new MaterialDialog.Builder(getContext())
                                    .title("转换成功！")
                                    .content(transferAccountsLimit.getWallet().get(nsFrom.getSelectedIndex()).getName() + "->" + transferAccountsLimit.getWallet().get(nsTo.getSelectedIndex()).getName())
                                    .positiveText("知道了").show();
                        }
                    }
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (UserHandler.isLogin() && flLogin != null)
                flLogin.setVisibility(View.GONE);
            else if (flLogin != null)
                flLogin.setVisibility(View.VISIBLE);
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
            if (flLogin != null)
                flLogin.setVisibility(View.GONE);
            refreshData();
        } else {
            if (flLogin != null)
                flLogin.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.btn_transfer_account, R.id.btn_submit_login, R.id.fl_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_transfer_account:
                if (MyTextUtils.checkEmpty(etEditMoney, "请输入金额", getActivity()))
                    return;
                if (transferAccountsLimit != null)
                    transferAccounts();
                else
                    Toast.makeText(getActivity(), "暂无账户信息", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_submit_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.fl_login:
                break;
        }
    }
}

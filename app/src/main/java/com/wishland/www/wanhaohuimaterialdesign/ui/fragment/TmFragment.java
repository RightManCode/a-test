package com.wishland.www.wanhaohuimaterialdesign.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseFragment;
import com.wishland.www.wanhaohuimaterialdesign.base.SuccessActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.AccountInfo;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.IsSuccess;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.WebActivity;
import com.wishland.www.wanhaohuimaterialdesign.utils.MyTextUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gerry on 2017/6/10.
 */

public class TmFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {
    @BindView(R.id.tv_tm_1)
    TextView tvTm1;
    @BindView(R.id.tv_tm_2)
    TextView tvTm2;
    @BindView(R.id.tv_tm_3)
    TextView tvTm3;
    @BindView(R.id.tv_tm_4)
    TextView tvTm4;
    @BindView(R.id.tv_tm_5)
    TextView tvTm5;
    @BindView(R.id.et_tm_1)
    EditText etTm1;
    @BindView(R.id.et_tm_2)
    EditText etTm2;
    @BindView(R.id.sv_tm)
    ScrollView svTm;
    private MaterialDialog materialDialog;
    Unbinder unbinder;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tm_money, container, false);
        unbinder = ButterKnife.bind(this, view);
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUserInfo();
            }
        });
        AccountInfo accountInfo = UserHandler.getUserInfo().getAccountInfo();
        if (accountInfo != null)
            initView(accountInfo);
        else
            refreshUserInfo();
        return view;
    }

    private void initView(AccountInfo userInfo) {
        tvTm1.setText(userInfo.getAccountInfo().getAccountName());
        tvTm2.setText(userInfo.getFinanceInfo().getPayeeName());
        tvTm3.setText(userInfo.getFinanceInfo().getBank());
        try {
            String bankAccount = userInfo.getFinanceInfo().getBankAccount();
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i < bankAccount.length() / 2; i++) {
                stringBuffer.append("*");
            }
            bankAccount = bankAccount.replace(bankAccount.substring(bankAccount.length() / 4, bankAccount.length() - (bankAccount.length() / 4)), stringBuffer.toString());
            tvTm4.setText(bankAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvTm5.setText(userInfo.getFinanceInfo().getAccountAddress());
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {

    }

    @OnClick(R.id.btn_submit_login)
    public void onViewClicked() {
        if (MyTextUtils.checkEmpty(etTm1, "请输入取款金额", getActivity()))
            return;
        if (MyTextUtils.checkEmpty(etTm2, "请输入提款密码", getActivity()))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("Amount", etTm1.getText().toString().trim());
        params.put("passWord", etTm2.getText().toString().trim());
        OkGo.post(Api.WITHDRAW_SUBMIT)
                .params(NetUtils.getParamsPro(params))
                .tag(this)
                .execute(new AbsCallbackPro() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        materialDialog = new MaterialDialog.Builder(getActivity())
                                .title("提款中")
                                .content("提款操作中,请稍后.....")
                                .progress(true, 0)
                                .show();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                                .title("出错了");
                        if (e != null && e instanceof OkGoException) {
                            builder.content(e.getMessage());
                        }

                        builder.positiveText("知道了")
                                .negativeText("联系客服")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(getActivity(), WebActivity.class);
                                        intent.putExtra("url", Api.CUSTOMER_SERVICES);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        IsSuccess isSuccess = new Gson().fromJson(s, new TypeToken<IsSuccess>() {
                        }.getType());
                        if (isSuccess != null && isSuccess.getStatus()) {
                            Intent intent = new Intent(getActivity(), SuccessActivity.class);
                            intent.putExtra("money", etTm1.getText().toString().trim());
                            startActivity(intent);
                        }
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View getScrollableView() {
        return svTm;
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
                                initView(accountInfo);
                            }
                        }
                    });
        }
    }

}

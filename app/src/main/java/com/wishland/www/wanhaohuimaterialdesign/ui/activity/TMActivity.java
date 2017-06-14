package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.base.SuccessActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.IsSuccess;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.UserInfo;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.MyTextUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class TMActivity extends BaseStyleActivity {

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
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tm, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        UserInfo userInfo = UserHandler.getUserInfo();
        tvTm1.setText(userInfo.getAccountInfo().getAccountInfo().getAccountName());
        tvTm2.setText(userInfo.getAccountInfo().getFinanceInfo().getPayeeName());
        tvTm3.setText(userInfo.getAccountInfo().getFinanceInfo().getBank());
        try {
            String bankAccount = userInfo.getAccountInfo().getFinanceInfo().getBankAccount();
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i < bankAccount.length() / 2; i++) {
                stringBuffer.append("*");
            }
            bankAccount = bankAccount.replace(bankAccount.substring(bankAccount.length() / 4, bankAccount.length() - (bankAccount.length() / 4)), stringBuffer.toString());
            tvTm4.setText(bankAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvTm5.setText(userInfo.getAccountInfo().getFinanceInfo().getAccountAddress());

    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {

    }

    @OnClick(R.id.btn_submit_login)
    public void onViewClicked() {
        if (MyTextUtils.checkEmpty(etTm1, "请输入取款金额", this))
            return;
        if (MyTextUtils.checkEmpty(etTm2, "请输入提款密码", this))
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
                        materialDialog = new MaterialDialog.Builder(TMActivity.this)
                                .title("提款中")
                                .content("提款操作中,请稍后.....")
                                .progress(true, 0)
                                .show();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(TMActivity.this)
                                .title("出错了");
                        if (e != null && e instanceof OkGoException) {
                            builder.content(e.getMessage());
                        }

                        builder.positiveText("知道了")
                                .negativeText("联系客服")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(TMActivity.this, WebActivity.class);
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
                            Intent intent = new Intent(TMActivity.this, SuccessActivity.class);
                            intent.putExtra("money", etTm1.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }
}

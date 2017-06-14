package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.IsSuccess;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.MyTextUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.VerificationCodeUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wishland.www.wanhaohuimaterialdesign.R.id.tv_ab_2;

public class AccontBindActivity extends BaseStyleActivity {

    private static final int BANK = 880;
    @BindView(R.id.tv_tm_1)
    TextView tvTm1;
    @BindView(R.id.tv_ab_1)
    EditText tvAb1;
    @BindView(tv_ab_2)
    TextView tvAb2;
    @BindView(R.id.tv_ab_3)
    EditText tvAb3;
    @BindView(R.id.tv_ab_4)
    EditText tvAb4;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.iv_verification_code)
    ImageView ivVerificationCode;
    @BindView(R.id.tv_ab_5)
    EditText tvAb5;
    @BindView(R.id.tv_ab_6)
    EditText tvAb6;
    @BindView(R.id.btn_refresh_verification_code)
    ImageButton btnRefreshVerificationCode;
    @BindView(R.id.btn_submit_login)
    Button btnSubmitLogin;
    private String code;
    private MaterialDialog materialDialog;
    private String bankno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accont_bind, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        tvTm1.setText(UserHandler.getUserInfo().getAccountInfo().getAccountInfo().getAccountName());

    }

    @Override
    protected void initVariable() {
        refreshVerificationCode();
    }

    @Override
    protected void initDate() {

    }

    private void refreshVerificationCode() {
        OkGo
                .post(Api.GET_VERIFICATION_CODE)
                .params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            code = jsonObject.getString("code");
                            VerificationCodeUtils.getInstance().createBitmap(code, new VerificationCodeUtils.ICreatCode() {
                                @Override
                                public void onFinish(String code, final Bitmap bitmap) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivVerificationCode.setImageBitmap(bitmap);
                                        }
                                    });
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick({R.id.btn_refresh_verification_code, R.id.btn_submit_login, tv_ab_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_refresh_verification_code:
                refreshVerificationCode();
                break;
            case tv_ab_2:
                startActivityForResult(new Intent(this, BankListActivity.class), BANK);
                break;
            case R.id.btn_submit_login:
                if (MyTextUtils.checkEmpty(tvAb1, "请输入收款人姓名", this))
                    return;
                if (MyTextUtils.checkEmpty(tvAb2, "请输入收款银行", this))
                    return;
                if (MyTextUtils.checkEmpty(tvAb3, "请输入银行账号", this))
                    return;
                if (MyTextUtils.checkEmpty(tvAb4, "请输入开户地址", this))
                    return;
                if (MyTextUtils.checkEmpty(tvAb5, "请输入密码", this))
                    return;
                if (MyTextUtils.checkEmpty(tvAb6, "请确认密码", this))
                    return;
                if (!tvAb5.getText().toString().trim().equals(tvAb6.getText().toString().trim())) {
                    Toast.makeText(this, "两次输入的密码不匹配,请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!etCode.getText().toString().trim().equals(code)) {
                    Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("bankCode", bankno);
                params.put("code", etCode.getText().toString());
                params.put("pay_name", tvAb1.getText().toString().trim());
                params.put("pay_card", tvAb2.getText().toString().trim());
                params.put("pay_num", tvAb3.getText().toString().trim());
                params.put("pay_address", tvAb4.getText().toString().trim());
                params.put("wpassword", tvAb5.getText().toString().trim());
                OkGo.post(Api.BIND_ACCOUNT)
                        .params(NetUtils.getParamsPro(params))
                        .tag(this)
                        .execute(new AbsCallbackPro() {

                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);
                                materialDialog = new MaterialDialog.Builder(AccontBindActivity.this)
                                        .title("注册中")
                                        .content("注册操作中,请稍后.....")
                                        .progress(true, 0)
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
                                    Toast.makeText(AccontBindActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                    UserHandler.refreshUserInfo();
                                    finish();
                                }
                            }
                        });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BANK:
                    bankno = data.getStringExtra("bankno");
                    String bankname = data.getStringExtra("bankname");
                    tvAb2.setText(bankname);
                    break;
            }
        }
    }
}

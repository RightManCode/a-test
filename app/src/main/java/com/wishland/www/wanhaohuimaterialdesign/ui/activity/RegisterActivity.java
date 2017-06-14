package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
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
import com.wishland.www.wanhaohuimaterialdesign.utils.MD5Utils;
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

public class RegisterActivity extends BaseStyleActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_realname)
    EditText etRealname;
    //    @BindView(R.id.et_phone)
//    EditText etPhone;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.et_re_pw)
    EditText etRePw;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.iv_verification_code)
    ImageView ivVerificationCode;
    @BindView(R.id.btn_refresh_verification_code)
    ImageButton btnRefreshVerificationCode;
    private MaterialDialog materialDialog;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {
        refreshVerificationCode();
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
                            Log.e(code, "code: " + code);
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

    @OnClick({R.id.btn_refresh_verification_code, R.id.btn_submit_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_refresh_verification_code:
                refreshVerificationCode();
                break;
            case R.id.btn_submit_register:
                if (MyTextUtils.checkEmpty(etName, "请输入用户名", this))
                    return;
                if (MyTextUtils.checkEmpty(etRealname, "请输入真实姓名", this))
                    return;
//                if (MyTextUtils.checkEmpty(etPhone, "请输入手机号码", this))
//                    return;
                if (MyTextUtils.checkEmpty(etPw, "请输入密码", this))
                    return;
                if (MyTextUtils.checkEmpty(etRePw, "请确认密码", this))
                    return;
                if (!etPw.getText().toString().trim().equals(etRePw.getText().toString().trim())) {
                    Toast.makeText(this, "两次输入的密码不匹配,请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!etCode.getText().toString().trim().equals(code)) {
                    Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("username", etName.getText().toString());
                map.put("realName", etRealname.getText().toString());
                map.put("rpassword", etPw.getText().toString());
                map.put("code", etCode.getText().toString());
                OkGo.post(Api.REGISTER)
                        .params(NetUtils.getParamsPro(map))
                        .tag(this)
                        .execute(new AbsCallbackPro() {

                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);
                                materialDialog = new MaterialDialog.Builder(RegisterActivity.this)
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
                                IsSuccess isSuccess = new Gson().fromJson(s, IsSuccess.class);
                                if (isSuccess.getStatus()) {
                                    MaterialDialog.Builder builder = new MaterialDialog.Builder(RegisterActivity.this);
                                    builder.title("注册成功");
                                    builder.negativeText("暂不登录")
                                            .negativeColor(getResources().getColor(R.color.text_hint))
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    finish();
                                                }
                                            }).positiveText("知道了")
                                            .positiveText("自动登录")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    login();
                                                }
                                            }).show();

                                } else {
                                    Toast.makeText(RegisterActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;
        }
    }


    private void login() {

        Map<String, String> map = new HashMap<>();
        map.put("username", etName.getText().toString().trim());
        map.put("password", MD5Utils.toMD5(etPw.getText().toString()).trim());
        OkGo.post(Api.LOGIN)
                .params(NetUtils.getParamsPro(map))
                .tag(this)
                .execute(new AbsCallbackPro() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        materialDialog = new MaterialDialog.Builder(RegisterActivity.this)
                                .title("登陆中")
                                .content("登陆操作中,请稍后.....")
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
                            UserHandler.login();
                            UserHandler.refreshUserInfo();
                            UserHandler.remenberUser(etName.getText().toString(), etPw.getText().toString());
                            finish();
                        }

                    }
                });


    }

}

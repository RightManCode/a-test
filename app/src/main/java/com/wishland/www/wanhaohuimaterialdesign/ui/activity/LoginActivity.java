package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigmercu.cBox.CheckBox;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.IsSuccess;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.RemenberUser;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.MD5Utils;
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

public class LoginActivity extends BaseStyleActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.btn_submit_login)
    Button btnSubmitLogin;
    @BindView(R.id.cb_remenber_me)
    CheckBox cbRemenberMe;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        RemenberUser remenberUser = UserHandler.getRemenberUser();
        if (remenberUser != null) {
            etName.setText(remenberUser.getUserName());
            etPw.setText(remenberUser.getPw());
            cbRemenberMe.setChecked(true);
        }
    }

    @Override
    protected void initVariable() {
        String toast = getIntent().getStringExtra("toast");
        if (!TextUtils.isEmpty(toast))
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initDate() {

    }

    private void login() {

        if (MyTextUtils.checkEmpty(etName, "请输入用户名", this))
            return;
        if (MyTextUtils.checkEmpty(etPw, "请输入密码", this))
            return;
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
                        materialDialog = new MaterialDialog.Builder(LoginActivity.this)
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
                            if (cbRemenberMe.isChecked()) {
                                UserHandler.remenberUser(etName.getText().toString(), etPw.getText().toString());
                            } else {
                                UserHandler.cleanRemnberUser();
                            }
                            finish();
                        }

                    }
                });


    }


    @OnClick({R.id.btn_submit_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_login:
                login();
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
        }
    }
}

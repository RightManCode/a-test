package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class ReEditLoginActivity extends BaseStyleActivity {

    @BindView(R.id.et_old_pw)
    EditText etOldPw;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.et_re_pw)
    EditText etRePw;
    @BindView(R.id.btn_submit_register)
    Button btnSubmitRegister;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_edit_login, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {

    }


    @OnClick(R.id.btn_submit_register)
    public void onViewClicked() {

        if (MyTextUtils.checkEmpty(etOldPw, "请输入旧密码", this))
            return;
        if (MyTextUtils.checkEmpty(etPw, "请输入新密码", this))
            return;
        if (MyTextUtils.checkEmpty(etRePw, "请确认密码", this))
            return;
        if (!etPw.getText().toString().trim().equals(etRePw.getText().toString().trim())) {
            Toast.makeText(this, "两次输入的密码不匹配,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("oriPW", etOldPw.getText().toString());
        map.put("newPW", etPw.getText().toString());
        OkGo.post(Api.REEDIT_LOGIN_PW)
                .params(NetUtils.getParamsPro(map))
                .tag(this)
                .execute(new AbsCallbackPro() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        materialDialog = new MaterialDialog.Builder(ReEditLoginActivity.this)
                                .title("修改中")
                                .content("修改操作中,请稍后.....")
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
                            Toast.makeText(ReEditLoginActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            UserHandler.refreshUserInfo();
                            finish();
                        }


                    }
                });

    }
}

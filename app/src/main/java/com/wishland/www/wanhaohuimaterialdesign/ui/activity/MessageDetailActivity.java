package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.IsSuccess;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.Message;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.MessageHandler;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class MessageDetailActivity extends BaseStyleActivity {

    @BindView(R.id.tv_title_message)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_from)
    TextView tvFrom;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_submit_login)
    Button btnSubmitLogin;
    private Message.DataListBean dataListBean;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        tvTitle.setText(dataListBean.getTitle());
        tvContent.setText(dataListBean.getDetailedInfo());
        tvTime.setText(dataListBean.getTime());
        if (!TextUtils.isEmpty(dataListBean.getFrom()))
            tvFrom.setText(dataListBean.getFrom());

    }

    @Override
    protected void initVariable() {
        dataListBean = (Message.DataListBean) getIntent().getSerializableExtra("message");
        position = getIntent().getIntExtra("position", -1);

    }

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<>();
        map.put("Msgid", String.valueOf(dataListBean.getMsgId()));
        OkGo.post(Api.MSG_READ)
                .params(NetUtils.getParamsPro(map))
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                    }
                });

    }

    @OnClick(R.id.btn_submit_login)
    public void onViewClicked() {
        new MaterialDialog.Builder(this)
                .title("确认")
                .content("是否删除该信息")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Map<String, String> map = new HashMap<>();
                        map.put("msgid", String.valueOf(dataListBean.getMsgId()));
                        OkGo.post(Api.DEL_MSG)
                                .params(NetUtils.getParamsPro(map))
                                .execute(new AbsCallbackPro() {

                                    private MaterialDialog materialDialog;

                                    @Override
                                    public void onBefore(BaseRequest request) {
                                        super.onBefore(request);
                                        materialDialog = new MaterialDialog.Builder(MessageDetailActivity.this)
                                                .title("转换中")
                                                .content("操作中,请稍后.....")
                                                .progress(true, 0)
                                                .show();
                                    }

                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        materialDialog.dismiss();
                                        IsSuccess isSuccess = new Gson().fromJson(s, IsSuccess.class);
                                        if (isSuccess.getStatus()) {
                                            MessageHandler.refreshData();
                                            Intent intent = new Intent();
                                            intent.putExtra("position", position);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        } else {
                                            Toast.makeText(MessageDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }).show();
    }
}

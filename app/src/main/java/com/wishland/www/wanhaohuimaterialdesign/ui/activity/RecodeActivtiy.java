package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecodeActivtiy extends BaseStyleActivity {

    @BindView(R.id.recv_recode)
    RecyclerView recvRecode;
    private String url;
    private String   startTime;
    private String  endTime;
    private String   type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_activtiy, R.layout.base_toolbar_back);
        ButterKnife.bind(this);

    }

    @Override
    protected void initVariable() {
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");
    }

    @Override
    protected void initDate() {

    }
}

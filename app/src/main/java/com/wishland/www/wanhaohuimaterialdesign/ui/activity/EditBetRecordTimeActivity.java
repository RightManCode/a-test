package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.TabEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditBetRecordTimeActivity extends BaseStyleActivity {

    @BindView(R.id.tl_edit_transaction_record_time)
    CommonTabLayout tlEditTransactionRecordTime;
    @BindView(R.id.tv_start_year)
    TextView tvStartYear;
    @BindView(R.id.tv_start_hour)
    TextView tvStartHour;
    @BindView(R.id.tv_start_minute)
    TextView tvStartMinute;
    @BindView(R.id.tv_end_year)
    TextView tvEndYear;
    @BindView(R.id.tv_end_hour)
    TextView tvEndHour;
    @BindView(R.id.tv_end_mimute)
    TextView tvEndMimute;
    @BindView(R.id.btn_start_search)
    Button btnStartSearch;
    private int[] mIconUnselectIds = {
            R.drawable.br1_unselect,
            R.drawable.br2_unselect,
            R.drawable.br3_unselect,
            R.drawable.br4_unselect,
    };
    private int[] mIconSelectIds = {
            R.drawable.br1_select,
            R.drawable.br2_select,
            R.drawable.br3_select,
            R.drawable.br4_select,
    };

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String startTime;
    private String endTime;
    private String type = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bet_record_time, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        initTable();
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {

    }

    private void initTable() {
        for (int i = 0; i < mIconUnselectIds.length; i++) {
            mTabEntities.add(new TabEntity(null, mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tlEditTransactionRecordTime.setTabData(mTabEntities);
        tlEditTransactionRecordTime.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                type = String.valueOf(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @OnClick({R.id.tv_start_year, R.id.tv_start_hour, R.id.tv_start_minute, R.id.tv_end_year, R.id.tv_end_hour, R.id.tv_end_mimute, R.id.btn_start_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_year:
                new TimePickerDialog.Builder()
                        .setType(Type.YEAR_MONTH_DAY)
//                        .setMinMillseconds(System.currentTimeMillis())
//                        .setMaxMillseconds(System.currentTimeMillis())
                        .setThemeColor(getResources().getColor(R.color.colorAccent))
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAccent))
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                String format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(millseconds);
                                tvStartYear.setText(format);
                                startTime = format;
                            }
                        })
                        .build().show(getSupportFragmentManager(), "year_month_day");
                break;
            case R.id.tv_start_hour:
                new TimePickerDialog.Builder()
                        .setType(Type.HOURS_MINS)
                        .setThemeColor(getResources().getColor(R.color.colorAccent))
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAccent))
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                tvStartHour.setText(new SimpleDateFormat("HH", Locale.CHINA).format(millseconds));
                                tvStartMinute.setText(new SimpleDateFormat("mm", Locale.CHINA).format(millseconds));
                            }
                        })
                        .build().show(getSupportFragmentManager(), "hour_minute");
                break;
            case R.id.tv_start_minute:
                new TimePickerDialog.Builder()
                        .setType(Type.HOURS_MINS)
                        .setThemeColor(getResources().getColor(R.color.colorAccent))
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAccent))
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                tvStartHour.setText(new SimpleDateFormat("HH", Locale.CHINA).format(millseconds));
                                tvStartMinute.setText(new SimpleDateFormat("mm", Locale.CHINA).format(millseconds));
                            }
                        })
                        .build().show(getSupportFragmentManager(), "month_day_hour_minute");
                break;
            case R.id.tv_end_year:
                new TimePickerDialog.Builder()
                        .setType(Type.YEAR_MONTH_DAY)
                        .setThemeColor(getResources().getColor(R.color.colorAccent))
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAccent))
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                String format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(millseconds);
                                tvEndYear.setText(format);
                                endTime = format;
                            }
                        })
                        .build().show(getSupportFragmentManager(), "year_month_day");
                break;
            case R.id.tv_end_hour:
                new TimePickerDialog.Builder()
                        .setType(Type.HOURS_MINS)
                        .setThemeColor(getResources().getColor(R.color.colorAccent))
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAccent))
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                tvStartHour.setText(new SimpleDateFormat("HH", Locale.CHINA).format(millseconds));
                                tvStartMinute.setText(new SimpleDateFormat("mm", Locale.CHINA).format(millseconds));
                            }
                        })
                        .build().show(getSupportFragmentManager(), "month_day_hour_minute");
                break;
            case R.id.tv_end_mimute:
                new TimePickerDialog.Builder()
                        .setType(Type.HOURS_MINS)
                        .setThemeColor(getResources().getColor(R.color.colorAccent))
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAccent))
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                tvStartHour.setText(new SimpleDateFormat("HH", Locale.CHINA).format(millseconds));
                                tvStartMinute.setText(new SimpleDateFormat("mm", Locale.CHINA).format(millseconds));
                            }
                        })
                        .build().show(getSupportFragmentManager(), "month_day_hour_minute");
                break;
            case R.id.btn_start_search:
                if (TextUtils.isEmpty(tvStartYear.getText())) {
                    Toast.makeText(this, "请输入开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tvEndYear.getText())) {
                    Toast.makeText(this, "请输入结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this, BetRecordActivity.class);
                intent.putExtra("startTime", startTime + " " + tvStartHour.getText().toString() + ":" + tvStartMinute.getText().toString());
                intent.putExtra("endTime", endTime + " " + tvEndHour.getText().toString() + ":" + tvEndMimute.getText().toString());
                intent.putExtra("type", type);
                startActivity(intent);
                break;
        }
    }
}

package com.wishland.www.wanhaohuimaterialdesign.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseFragment;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.TransactionRecordActivtiy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by gerry on 2017/6/10.
 */

public class EditTransactionRecordFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {

    @BindView(R.id.tl_edit_transaction_record_time)
    SegmentTabLayout tlEditTransactionRecordTime;
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
    private String[] mTitles = {"存款", "汇款", "提款", "其他"};
    private ArrayList<CustomTabEntity> mTabEntities;
    private String startTime;
    private String endTime;
    private String type = "0";
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_transaction_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        initTable();
        return view;
    }

    @Override
    protected void initVariable() {
        mTabEntities = new ArrayList<>();
    }

    @Override
    protected void initDate() {

    }

    @Override
    public View getScrollableView() {
        return null;
    }
    private void initTable() {
        tlEditTransactionRecordTime.setTabData(mTitles);
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
                        .build().show(getActivity().getSupportFragmentManager(), "year_month_day");
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
                        .build().show(getActivity().getSupportFragmentManager(), "hour_minute");
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
                        .build().show(getActivity().getSupportFragmentManager(), "month_day_hour_minute");
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
                        .build().show(getActivity().getSupportFragmentManager(), "year_month_day");
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
                        .build().show(getActivity().getSupportFragmentManager(), "month_day_hour_minute");
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
                        .build().show(getActivity().getSupportFragmentManager(), "month_day_hour_minute");
                break;
            case R.id.btn_start_search:
                if (TextUtils.isEmpty(tvStartYear.getText())) {
                    Toast.makeText(getActivity(), "请输入开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tvEndYear.getText())) {
                    Toast.makeText(getActivity(), "请输入结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), TransactionRecordActivtiy.class);
                intent.putExtra("startTime", startTime + " " + tvStartHour.getText().toString() + ":" + tvStartMinute.getText().toString());
                intent.putExtra("endTime", endTime + " " + tvEndHour.getText().toString() + ":" + tvEndMimute.getText().toString());
                intent.putExtra("type", type);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

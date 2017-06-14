package com.wishland.www.wanhaohuimaterialdesign.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseFragment;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.TransferAccountsLimit;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.AccountToAccountRecordActivtiy;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.RecyclerSpace;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.expandableView.ExpandableLayout;
import com.wishland.www.wanhaohuimaterialdesign.utils.MyTextUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gerry on 2017/6/12.
 */

public class EditAccountToAccountRecordFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {
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
    @BindView(R.id.from)
    ExpandableLayout from;
    @BindView(R.id.to)
    ExpandableLayout to;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private String startTime;
    private String endTime;
    private String type = "0";
    private TransferAccountsLimit transferAccountsLimit;
    private RecyclerView recFrom;
    private RecyclerView recTo;
    private FromTransferAccountsAdapter fromTransferAccountsAdapter;
    private ToTransferAccountsAdapter toTransferAccountsAdapter;
    private TextView fromTvName;
    private TextView toTvName;
    private String fromPosition;
    private String toPosition;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_account_to_account_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        initView();
        return view;
    }

    @Override
    protected void initVariable() {
        if (UserHandler.isLogin())
            refreshData();
    }

    @Override
    protected void initDate() {

    }

    private void initView() {
        RecyclerSpace dividerItemDecoration = new RecyclerSpace(1, R.color.colorPrimaryDark, 0);
        FrameLayout fromContentLayout = from.getContentLayout();
        from.setFoldListener(new ExpandableLayout.IFoldListener() {
            @Override
            public void isFold(boolean fold) {
                if (fold)
                    ((ImageView) from.getHeaderLayout().findViewById(R.id.iv_arrow)).setImageResource(R.drawable.ic_action_fold_up);
                else
                    ((ImageView) from.getHeaderLayout().findViewById(R.id.iv_arrow)).setImageResource(R.drawable.ic_action_fold_down);

            }
        });
        recFrom = (RecyclerView) fromContentLayout.findViewById(R.id.rec);
        recFrom.setNestedScrollingEnabled(false);
        recFrom.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        fromTransferAccountsAdapter = new FromTransferAccountsAdapter(R.layout.item_transfer_account, null);
        fromTransferAccountsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                from.fold();
                if (TextUtils.isEmpty(toPosition))
                    to.unfold();
                fromPosition = String.valueOf(position);
                adapter.notifyDataSetChanged();
                fromTvName.setText(transferAccountsLimit.getWallet().get(position).getName() + "(转出)");
            }
        });
        recFrom.setAdapter(fromTransferAccountsAdapter);
        recFrom.addItemDecoration(dividerItemDecoration);
        FrameLayout toContentLayout = to.getContentLayout();
        to.setFoldListener(new ExpandableLayout.IFoldListener() {
            @Override
            public void isFold(boolean fold) {
                if (fold)
                    ((ImageView) to.getHeaderLayout().findViewById(R.id.iv_arrow)).setImageResource(R.drawable.ic_action_fold_up);
                else
                    ((ImageView) to.getHeaderLayout().findViewById(R.id.iv_arrow)).setImageResource(R.drawable.ic_action_fold_down);
            }
        });
        recTo = (RecyclerView) toContentLayout.findViewById(R.id.rec);
        recTo.setNestedScrollingEnabled(false);
        recTo.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        toTransferAccountsAdapter = new ToTransferAccountsAdapter(R.layout.item_transfer_account, null);
        toTransferAccountsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                to.fold();
                toPosition = String.valueOf(position);
                adapter.notifyDataSetChanged();
                toTvName.setText(transferAccountsLimit.getWallet().get(position).getName() + "(转入)");
            }
        });
        recTo.setAdapter(toTransferAccountsAdapter);
        recTo.addItemDecoration(dividerItemDecoration);
        FrameLayout fromHeaderLayout = from.getHeaderLayout();
        fromTvName = (TextView) fromHeaderLayout.findViewById(R.id.tv_transfer_account_name);
        fromTvName.setText("请选择转出账户");
        FrameLayout toHeaderLayout = to.getHeaderLayout();
        toTvName = (TextView) toHeaderLayout.findViewById(R.id.tv_transfer_account_name);
        toTvName.setText("请选择转入账户");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void refreshData() {
        OkGo.post(Api.TRANSFER_ACCOUNTS_LIMIT)
                .params(NetUtils.getParamsPro())
                .tag(this)
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onBefore(BaseRequest request) {

                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        transferAccountsLimit = new Gson().fromJson(s, new TypeToken<TransferAccountsLimit>() {
                        }.getType());
                        if (transferAccountsLimit != null) {
                            if (!from.isOpened())
                                from.unfold();
                            to.fold();
                            fromPosition = "";
                            toPosition = "";
                            fromTvName.setText("请选择转出账户");
                            toTvName.setText("请选择转入账户");
                            fromTransferAccountsAdapter.setNewData(transferAccountsLimit.getWallet());
                            fromTransferAccountsAdapter.notifyDataSetChanged();
                            toTransferAccountsAdapter.setNewData(transferAccountsLimit.getWallet());
                            toTransferAccountsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @OnClick({R.id.tv_start_year,
            R.id.tv_start_hour,
            R.id.tv_start_minute,
            R.id.tv_end_year,
            R.id.tv_end_hour,
            R.id.tv_end_mimute,
            R.id.btn_start_search})
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
                if (MyTextUtils.checkEmpty(fromPosition, "请选择转出账户", getActivity()))
                    return;
                if (MyTextUtils.checkEmpty(toPosition, "请选择转入账户", getActivity()))
                    return;
                if (transferAccountsLimit != null) {
                    Intent intent = new Intent(getActivity(), AccountToAccountRecordActivtiy.class);
                    intent.putExtra("startTime", startTime + " " + tvStartHour.getText().toString() + ":" + tvStartMinute.getText().toString());
                    intent.putExtra("endTime", endTime + " " + tvEndHour.getText().toString() + ":" + tvEndMimute.getText().toString());
                    try {
                        intent.putExtra("from", transferAccountsLimit.getWallet().get(Integer.parseInt(fromPosition)).getWallettype());
                        intent.putExtra("to", transferAccountsLimit.getWallet().get(Integer.parseInt(toPosition)).getWallettype());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), "暂无账户信息", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public View getScrollableView() {
        return srl;
    }

    private class FromTransferAccountsAdapter extends BaseQuickAdapter<TransferAccountsLimit.WalletBean, BaseViewHolder> {
        FromTransferAccountsAdapter(@LayoutRes int layoutResId, @Nullable List<TransferAccountsLimit.WalletBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TransferAccountsLimit.WalletBean item) {
            helper.setText(R.id.tv_transfer_account, item.getName());
            if (!TextUtils.isEmpty(fromPosition) && Integer.valueOf(fromPosition) == helper.getLayoutPosition())
                helper.setVisible(R.id.iv_check, true);
            else
                helper.setVisible(R.id.iv_check, false);
        }
    }

    private class ToTransferAccountsAdapter extends BaseQuickAdapter<TransferAccountsLimit.WalletBean, BaseViewHolder> {
        ToTransferAccountsAdapter(@LayoutRes int layoutResId, @Nullable List<TransferAccountsLimit.WalletBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TransferAccountsLimit.WalletBean item) {
            helper.setText(R.id.tv_transfer_account, item.getName());
            if (!TextUtils.isEmpty(toPosition) && Integer.valueOf(toPosition) == helper.getLayoutPosition())
                helper.setVisible(R.id.iv_check, true);
            else
                helper.setVisible(R.id.iv_check, false);

        }
    }
}

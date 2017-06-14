package com.wishland.www.wanhaohuimaterialdesign.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseFragment;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.IsSuccess;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.TransferAccountsLimit;
import com.wishland.www.wanhaohuimaterialdesign.model.event.UserEvent;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.LoginActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.WebActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.RecyclerSpace;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.expandableView.ExpandableLayout;
import com.wishland.www.wanhaohuimaterialdesign.utils.MyTextUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gerry on 2017/5/1.
 */

public class TransferAccountsFragmentPro extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.fl_login)
    FrameLayout flLogin;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.btn_submit_login)
    Button btnSubmitLogin;
    @BindView(R.id.from)
    ExpandableLayout from;
    @BindView(R.id.to)
    ExpandableLayout to;
    @BindView(R.id.et_transfer_money)
    EditText etTransferMoney;
    private TransferAccountsLimit transferAccountsLimit;
    private MaterialDialog materialDialog;
    private RecyclerView recFrom;
    private RecyclerView recTo;
    private FromTransferAccountsAdapter fromTransferAccountsAdapter;
    private ToTransferAccountsAdapter toTransferAccountsAdapter;
    private TextView fromTvName;
    private TextView toTvName;
    private String fromPosition;
    private String toPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_accounts_pro, container, false);
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
                fromTvName.setText(transferAccountsLimit.getWallet().get(position).getName() + "--"+transferAccountsLimit.getWallet().get(position).getAmout()+"(转出)");
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
                toTvName.setText(transferAccountsLimit.getWallet().get(position).getName() + "--"+transferAccountsLimit.getWallet().get(position).getAmout()+"(转入)");
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
    protected void initVariable() {
    }

    @Override
    protected void initDate() {
        if (UserHandler.isLogin())
            refreshData();
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
                            etTransferMoney.setText("");
                            fromTransferAccountsAdapter.setNewData(transferAccountsLimit.getWallet());
                            fromTransferAccountsAdapter.notifyDataSetChanged();
                            toTransferAccountsAdapter.setNewData(transferAccountsLimit.getWallet());
                            toTransferAccountsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void transferAccounts() {
        Map<String, String> map = new HashMap<>();
        map.put("fromWalletType", transferAccountsLimit.getWallet().get(Integer.parseInt(fromPosition)).getWallettype());
        map.put("toWalletType", transferAccountsLimit.getWallet().get(Integer.parseInt(toPosition)).getWallettype());
        map.put("amount", etTransferMoney.getText().toString());
        OkGo.post(Api.TRANSFER_ACCOUNTS)
                .params(NetUtils.getParamsPro(map))
                .tag(this)
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        materialDialog = new MaterialDialog.Builder(getActivity())
                                .title("转换中")
                                .content("操作中,请稍后.....")
                                .progress(true, 0)
                                .show();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                        builder.title("出错了");
                        if (e != null && e instanceof OkGoException) {
                            builder.content(e.getMessage());
                        }
                        builder.negativeText("联系客服")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(getActivity(), WebActivity.class);
                                        intent.putExtra("url", Api.CUSTOMER_SERVICES);
                                        startActivity(intent);
                                    }
                                }).positiveText("知道了").show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        refreshData();
                        IsSuccess isSuccess = new Gson().fromJson(s, new TypeToken<IsSuccess>() {
                        }.getType());
                        if (isSuccess != null && isSuccess.getStatus()) {
                            new MaterialDialog.Builder(getContext())
                                    .title("转换成功！")
                                    .content(transferAccountsLimit.getWallet().get(Integer.parseInt(fromPosition)).getName() + "->" +
                                            transferAccountsLimit.getWallet().get(Integer.parseInt(toPosition)).getName()
                                            + "\n转款金额：" + etTransferMoney.getText().toString())
                                    .positiveText("知道了").show();
                        }
                    }
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (UserHandler.isLogin() && flLogin != null)
                flLogin.setVisibility(View.GONE);
            else if (flLogin != null)
                flLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserEvent event) {
        if (event.isLogin()) {
            if (flLogin != null)
                flLogin.setVisibility(View.GONE);
            refreshData();
        } else {
            if (flLogin != null)
                flLogin.setVisibility(View.VISIBLE);
            fromTransferAccountsAdapter.setNewData(null);
            toTransferAccountsAdapter.setNewData(null);
        }

    }


    @OnClick({R.id.btn_all,
            R.id.btn_100,
            R.id.btn_300,
            R.id.btn_500,
            R.id.btn_1000,
            R.id.btn_5000,
            R.id.btn_10000,
            R.id.btn_submit_transfer,
            R.id.fl_login,
            R.id.btn_submit_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_all:
                if (TextUtils.isEmpty(fromPosition)) {
                    Toast.makeText(getActivity(), "请选择转出账户", Toast.LENGTH_SHORT).show();
                    return;
                }
                etTransferMoney.setText(transferAccountsLimit.getWallet().get(Integer.parseInt(fromPosition)).getAmout());
                break;
            case R.id.btn_100:
                etTransferMoney.setText("100");
                break;
            case R.id.btn_300:
                etTransferMoney.setText("300");
                break;
            case R.id.btn_500:
                etTransferMoney.setText("500");
                break;
            case R.id.btn_1000:
                etTransferMoney.setText("1000");
                break;
            case R.id.btn_5000:
                etTransferMoney.setText("5000");
                break;
            case R.id.btn_10000:
                etTransferMoney.setText("10000");
                break;
            case R.id.btn_submit_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.btn_submit_transfer:
                if (MyTextUtils.checkEmpty(etTransferMoney, "请输入金额", getActivity()))
                    return;
                if (MyTextUtils.checkEmpty(fromPosition, "请选择转出账户", getActivity()))
                    return;
                if (MyTextUtils.checkEmpty(toPosition, "请选择转入账户", getActivity()))
                    return;
                if (transferAccountsLimit != null)
                    transferAccounts();
                else
                    Toast.makeText(getActivity(), "暂无账户信息", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class FromTransferAccountsAdapter extends BaseQuickAdapter<TransferAccountsLimit.WalletBean, BaseViewHolder> {
        FromTransferAccountsAdapter(@LayoutRes int layoutResId, @Nullable List<TransferAccountsLimit.WalletBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TransferAccountsLimit.WalletBean item) {
            helper.setText(R.id.tv_transfer_account, item.getName() + "\n" + item.getAmout());
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
            helper.setText(R.id.tv_transfer_account, item.getName() + "\n" + item.getAmout());
            if (!TextUtils.isEmpty(toPosition) && Integer.valueOf(toPosition) == helper.getLayoutPosition())
                helper.setVisible(R.id.iv_check, true);
            else
                helper.setVisible(R.id.iv_check, false);

        }
    }
}

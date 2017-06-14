package com.wishland.www.wanhaohuimaterialdesign.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseFragment;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.AccountInfo;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.UserInfo;
import com.wishland.www.wanhaohuimaterialdesign.model.event.UserEvent;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.LoginActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.TabEntity;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gerry on 2017/5/1.
 */

public class PayMoneyFragmentPro extends BaseFragment {
    Unbinder unbinder;
    //    @BindView(R.id.srl)
//    SwipeRefreshLayout srl;
    @BindView(R.id.fl_login)
    FrameLayout flLogin;
    @BindView(R.id.ctl)
    CommonTabLayout ctl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.scrollableLayout)
    ScrollableLayout scrollableLayout;
    List<BaseFragment> fragmentList = new ArrayList<>();
    @BindView(R.id.fl_halo)
    View flHalo;
    @BindView(R.id.iv_head)
    BootstrapCircleThumbnail ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.btn_submit_login)
    Button btnSubmitLogin;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"存款", "取款", "交易记录"
            , "转换查询"
    };
    private int[] mIconUnselectIds = {
            R.drawable.pic_test,
            R.drawable.pic_test,
            R.drawable.pic_test,
            R.drawable.pic_test
    };
    private int[] mIconSelectIds = {
            R.drawable.pic_test,
            R.drawable.pic_test,
            R.drawable.pic_test,
            R.drawable.pic_test
    };
    private FragmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_money_pro, container, false);
        unbinder = ButterKnife.bind(this, view);
//        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshData();
//            }
//        });
        initView();
        return view;
    }

    private void initView() {
        ctl.setTabData(mTabEntities);
        ctl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewpager.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        initViewPager();
    }

    @Override
    protected void initVariable() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
    }

    private void initViewPager() {
        if (UserHandler.isLogin()) {
            fragmentList.add(new PayMoneyFragment());
            fragmentList.add(new TmFragment());
            fragmentList.add(new EditTransactionRecordFragment());
            fragmentList.add(new EditAccountToAccountRecordFragment());
        }
        viewpager.setOffscreenPageLimit(10);
        adapter = new FragmentAdapter(getActivity().getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ctl.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(0);


    }

    @Override
    protected void initDate() {
        if (UserHandler.isLogin())
            refreshData();
    }

    public void refreshData() {
//        for (BaseFragment baseFragment :
//                fragmentList) {
//            baseFragment.refreshData();
//        }
//        new CountDownTimer(3000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                srl.setRefreshing(false);
//            }
//        }.start();
    }


    private class FragmentAdapter extends FragmentPagerAdapter {
        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

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
            fragmentList.add(new PayMoneyFragment());
            fragmentList.add(new TmFragment());
            fragmentList.add(new EditTransactionRecordFragment());
            fragmentList.add(new EditAccountToAccountRecordFragment());
            adapter.notifyDataSetChanged();
            refreshData();
        } else {
            if (flLogin != null)
                flLogin.setVisibility(View.VISIBLE);
            fragmentList.clear();
            adapter.notifyDataSetChanged();
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfo userInfo) {
        if (tvName != null) {
            tvName.setText(userInfo.getAccountInfo().getAccountInfo().getAccountName());
        }
        if (tvBalance != null) {
            tvBalance.setText(userInfo.getAccountInfo().getBalanceInfo().getBalance());
        }

    }

    @OnClick({
            R.id.fl_login,
            R.id.btn_submit_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }


    public void refreshUserInfo() {
        if (UserHandler.isLogin()) {
            OkGo.post(Api.GET_ACCOUNT_INFO)
                    .params(NetUtils.getParamsPro())
                    .execute(new AbsCallbackPro() {
                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
//                            srl.setRefreshing(false);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            AccountInfo accountInfo = new Gson().fromJson(s, new TypeToken<AccountInfo>() {
                            }.getType());
                            if (accountInfo != null) {
                                tvBalance.setText(accountInfo.getBalanceInfo().getBalance());
                                tvName.setText(accountInfo.getAccountInfo().getAccountName());
                                UserInfo userInfo = UserHandler.getUserInfo();
                                userInfo.setAccountInfo(accountInfo);
                                UserHandler.updateUser(userInfo);
                            }
                        }
                    });
        }
    }

}

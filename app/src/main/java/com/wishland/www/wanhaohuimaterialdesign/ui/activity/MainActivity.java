package com.wishland.www.wanhaohuimaterialdesign.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseActivtiy;
import com.wishland.www.wanhaohuimaterialdesign.model.event.MessageEvent;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.TabEntity;
import com.wishland.www.wanhaohuimaterialdesign.ui.fragment.HomeFragment;
import com.wishland.www.wanhaohuimaterialdesign.ui.fragment.PayMoneyFragmentPro;
import com.wishland.www.wanhaohuimaterialdesign.ui.fragment.TransferAccountsFragmentPro;
import com.wishland.www.wanhaohuimaterialdesign.ui.fragment.UserCenterFragment;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivtiy {


    @BindView(R.id.vp_main)
    ViewPager vpMain;
    List<Fragment> fragmentList = new ArrayList<>();
    @BindView(R.id.tl_main)
    CommonTabLayout tlMain;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String[] mTitles = {"首页", "资金", "额度转换", "个人中心"
//            , "支付宝"
    };
    private int[] mIconUnselectIds = {
            R.drawable.home_table3_un,
            R.drawable.home_table5_un,
            R.drawable.home_table4_un,
            R.drawable.home_table6_un
    };
    private int[] mIconSelectIds = {
            R.drawable.home_table3,
            R.drawable.home_table5,
            R.drawable.home_table4,
            R.drawable.home_table6
    };
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTable();
        initViewPager();
        UserHandler.refreshUserInfo();
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initDate() {

    }


    private void initViewPager() {
        fragmentList.add(new HomeFragment());
        fragmentList.add(new PayMoneyFragmentPro());
        fragmentList.add(new TransferAccountsFragmentPro());
        fragmentList.add(new UserCenterFragment());
        vpMain.setOffscreenPageLimit(10);
        vpMain.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tlMain.setCurrentTab(position);
                tvTitle.setText(mTitles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpMain.setCurrentItem(0);
    }

    private void initTable() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tlMain.setTabData(mTabEntities);
        tlMain.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpMain.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @OnClick(R.id.btn_custom_service)
    public void onViewClicked() {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("url", Api.CUSTOMER_SERVICES);
        startActivity(intent);
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

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 500) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    public void onMessageEvent(MessageEvent event) {
        int unReadMsg = event.getUnReadMsg();
        if (unReadMsg > 0) {
            tlMain.showDot(0);
        } else {
            tlMain.hideMsg(0);
        }
    }
}

package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseActivtiy;
import com.wishland.www.wanhaohuimaterialdesign.ui.fragment.HomeFragment;
import com.wishland.www.wanhaohuimaterialdesign.ui.fragment.PayMoneyFragment;
import com.wishland.www.wanhaohuimaterialdesign.ui.fragment.TransferAccountsFragmentPro;
import com.wishland.www.wanhaohuimaterialdesign.ui.fragment.UserCenterFragment;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityPro extends BaseActivtiy {
    @BindView(R.id.vp_main)
    ViewPager vpMain;
    List<Fragment> fragmentList;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    private ArrayList<CustomTabEntity> mTabEntities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pro);
        ButterKnife.bind(this);
        initTable();
        initViewPager();
        UserHandler.refreshUserInfo();
    }
    @Override
    protected void initVariable() {
        if (UserHandler.isLogin())
            UserHandler.refreshUserInfo();
        fragmentList = new ArrayList<>();
        mTabEntities = new ArrayList<>();
    }

    @Override
    protected void initDate() {

    }
    private void initTable() {
        bottomNavigationBar
                .setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        vpMain.setCurrentItem(position, false);
                    }

                    @Override
                    public void onTabUnselected(int position) {

                    }

                    @Override
                    public void onTabReselected(int position) {

                    }
                })
                .addItem(new BottomNavigationItem(R.drawable.home_table3, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.home_table4, "资金"))
                .addItem(new BottomNavigationItem(R.drawable.home_table5, "额度转换"))
                .addItem(new BottomNavigationItem(R.drawable.home_table6, "个人中心"))
                .initialise();
    }

    private void initViewPager() {
        fragmentList.add(new HomeFragment());
        fragmentList.add(new PayMoneyFragment());
        fragmentList.add(new TransferAccountsFragmentPro());
        fragmentList.add(new UserCenterFragment());
        vpMain.setOffscreenPageLimit(10);
        vpMain.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
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
}

package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseActivtiy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivtiy {

    @BindView(R.id.vp_splash)
    ViewPager vpSplash;
    ArrayList<ImageView> imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        vpSplash.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView child = imageViews.get(position);
                container.addView(child, 0);//添加页卡
                return imageViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews.get(position));//删除页卡
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    @Override
    protected void initVariable() {
        imageViews = new ArrayList<>();
        ImageView inflate = (ImageView) getLayoutInflater().inflate(R.layout.pic, vpSplash, false);
        inflate.setImageResource(R.drawable.splash1);
        imageViews.add(inflate);
        ImageView inflate1 = (ImageView) getLayoutInflater().inflate(R.layout.pic, vpSplash, false);
        inflate1.setImageResource(R.drawable.splash2);
        imageViews.add(inflate1);
        ImageView inflate2 = (ImageView) getLayoutInflater().inflate(R.layout.pic, vpSplash, false);
        inflate2.setImageResource(R.drawable.splash3);
        imageViews.add(inflate2);
        ImageView inflate3 = (ImageView) getLayoutInflater().inflate(R.layout.pic, vpSplash, false);
        inflate3.setImageResource(R.drawable.splash4);
        inflate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                finish();
            }
        });
        imageViews.add(inflate3);
    }

    @Override
    protected void initDate() {

    }
}

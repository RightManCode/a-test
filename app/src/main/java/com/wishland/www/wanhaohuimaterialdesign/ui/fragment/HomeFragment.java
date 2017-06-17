package com.wishland.www.wanhaohuimaterialdesign.ui.fragment;


import android.content.Context;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseFragment;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.Index;
import com.wishland.www.wanhaohuimaterialdesign.model.event.MessageEvent;
import com.wishland.www.wanhaohuimaterialdesign.model.event.UserEvent;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.GameListActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.LoginActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.MessageActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.MoreActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.MyCollectActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.WebActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.MarqueeTextView;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.expandableView.ExpandableLayout;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.MessageHandler;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

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
 * Created by gerry on 2017/4/29.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_marquee)
    MarqueeTextView tvMarquee;
    @BindView(R.id.btn_login)
    FrameLayout btnLogin;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_message_num)
    TextView tvMessageNum;
    private Index index;
    private View.OnClickListener l;
    private ArrayList<ExpandableLayout> expandableLayouts;
    private View.OnClickListener url;
    private ExpandableLayout expandingLayout;
    private View clickedMEnu;
    private YoYo.YoYoString yoYoString;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle sInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                MessageHandler.refreshData();
            }
        });
        if (UserHandler.isLogin())
            btnLogin.setVisibility(View.GONE);
        else
            btnLogin.setVisibility(View.VISIBLE);
        return inflate;
    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播时间
        banner.setDelayTime(3500);
        ArrayList<String> imageUrls = new ArrayList<>();
        for (Index.TopBannerBean topBannerBean :
                index.getTopBanner()) {
            imageUrls.add(topBannerBean.getImg());
        }
        banner.setImages(imageUrls);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String weburl = index.getTopBanner().get(position).getWeburl();
                if ("#".equals(weburl) || TextUtils.isEmpty(weburl) || weburl.length() <= 1) {

                } else {
                    if (UserHandler.isLogin()) {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("url", weburl);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                }
            }
        });
        banner.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initVariable() {
        url = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserHandler.isLogin()) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", (String) v.getTag());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        };
        l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yoYoString != null)
                    yoYoString.stop();//停止当前Menu动画
                final int subMenuPosition = (int) v.getTag();
                List<Index.TopGameBean.SubMenuBean> subMenu = index.getTopGame().getSubMenu().get(subMenuPosition);
                if (!TextUtils.isEmpty(index.getTopGame().getMenuImgUrls().get(subMenuPosition).getWeburl())) {//没有二级菜单，直接跳转网页
                    if (UserHandler.isLogin()) {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("url", index.getTopGame().getMenuImgUrls().get(subMenuPosition).getWeburl());
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                    if (expandingLayout != null) {//收起其他菜单
                        expandingLayout.fold();
                        expandingLayout = null;
                    }
                    if (clickedMEnu != null) {
                        Glide.with(HomeFragment.this).load(R.drawable.menu_game_unselect).into((ImageView) clickedMEnu.findViewById(R.id.iv_game_menu));
                        clickedMEnu = null;
                    }
                } else {//有二级菜单
                    if (v.equals(clickedMEnu)) {//点击已展开的菜单收起
                        clickedMEnu = null;
                        expandingLayout.fold();
                        expandingLayout = null;
                        Glide.with(HomeFragment.this).load(R.drawable.menu_game_unselect).into((ImageView) v.findViewById(R.id.iv_game_menu));
                    } else {
                        Glide.with(HomeFragment.this).load(R.drawable.menu_game_select).into((ImageView) v.findViewById(R.id.iv_game_menu));
                        if (clickedMEnu != null) {
                            Glide.with(HomeFragment.this).load(R.drawable.menu_game_unselect).into((ImageView) clickedMEnu.findViewById(R.id.iv_game_menu));
                        }
                        clickedMEnu = v;
                        yoYoString = YoYo
                                .with(Techniques.Swing)
                                .duration(1500)
                                .repeat(Integer.MAX_VALUE)
                                .playOn(clickedMEnu.findViewById(R.id.fl_home_menu));

                        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(getContext()).inflate(R.layout.item_content_titles, expandingLayout, false);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        recyclerView.setNestedScrollingEnabled(false);
                        HomeMenuAdapter adapter = new HomeMenuAdapter(R.layout.item_home_game_menu, subMenu);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                if (!UserHandler.isLogin()) {
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    return;
                                }
                                if (TextUtils.equals("data", index.getTopGame().getSubMenu().get(subMenuPosition).get(position).getType())) {
                                    Intent intent = new Intent(getActivity(), GameListActivity.class);
                                    intent.putExtra("name", index.getTopGame().getSubMenu().get(subMenuPosition).get(position).getTitle());
                                    intent.putExtra("url", index.getTopGame().getSubMenu().get(subMenuPosition).get(position).getWeburl());
                                    startActivity(intent);
                                } else if (TextUtils.equals("url", index.getTopGame().getSubMenu().get(subMenuPosition).get(position).getType())) {
                                    if (UserHandler.isLogin()) {
                                        Intent intent = new Intent(getActivity(), WebActivity.class);
                                        intent.putExtra("url", index.getTopGame().getSubMenu().get(subMenuPosition).get(position).getWeburl());
                                        startActivity(intent);
                                    } else {
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                    }
                                }
                            }
                        });
                        recyclerView.setAdapter(adapter);
                        if (!expandableLayouts.get(subMenuPosition / 3).equals(expandingLayout)) {//同排菜单已展开
                            if (expandingLayout != null)
                                expandingLayout.fold();
                            expandingLayout = expandableLayouts.get(subMenuPosition / 3);
                            expandingLayout.unfold();
                        }
                        expandingLayout.setContenView(recyclerView);
                    }
                }
            }
        };
        expandableLayouts = new ArrayList<>();
    }

    @Override
    protected void initDate() {
        refreshData();
    }

    public void refreshData() {
        if (UserHandler.isLogin())
            MessageHandler.refreshData();
        OkGo.post(Api.INDEX)
                .params(NetUtils.getParamsPro())
                .tag(this)
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            index = new Gson().fromJson(s, new TypeToken<Index>() {
                            }.getType());
                            if (index != null) {
                                if (index.getTopBanner() != null)
                                    initBanner();
                                if (index.getTopGame() != null)
                                    initMenu();
                                if (index.getAdsTitle() != null & index.getAdsTitle().getTitle() != null)
                                    tvMarquee.setText(index.getAdsTitle().getTitle());
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initMenu() {
        expandableLayouts.clear();
        llHome.removeAllViews();
        ExpandableLayout inflate;
        LinearLayout linearLayout = null;
        for (int i = 0; i < index.getTopGame().getMenuImgUrls().size(); i++) {
            if (i % 3 == 0) {
                inflate = (ExpandableLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_home, llHome, false);
                expandableLayouts.add(inflate);
                FrameLayout headerLayout = inflate.getHeaderLayout();
                linearLayout = (LinearLayout) headerLayout.findViewById(R.id.ll_head);
                llHome.addView(inflate);
            }
            assert linearLayout != null;
            View inflate1 = LayoutInflater.from(getContext()).inflate(R.layout.item_home_title, linearLayout, false);
            inflate1.setTag(i);
            inflate1.setOnClickListener(l);
            ImageView ivHome = (ImageView) inflate1.findViewById(R.id.iv_home);
            TextView tvHome = (TextView) inflate1.findViewById(R.id.tv_home);
            tvHome.setText(index.getTopGame().getMenuImgUrls().get(i).getTitle());
            Glide.with(HomeFragment.this).load(index.getTopGame().getMenuImgUrls().get(i).getImg()).placeholder(R.drawable.loading_pic).into(ivHome);
            Glide.with(HomeFragment.this).load(R.drawable.menu_game_unselect).into((ImageView) inflate1.findViewById(R.id.iv_game_menu));
            linearLayout.addView(inflate1);
        }
    }


    @OnClick({R.id.btn_custom_service2, R.id.btn_register, R.id.btn_login, R.id.btn_favorable})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_custom_service2:
                if (!UserHandler.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                Intent intent2 = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_register:
                if (!UserHandler.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MyCollectActivity.class));
                break;
            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.btn_favorable:
                startActivity(new Intent(getActivity(), MoreActivity.class));
                break;
            default:
                break;
        }
    }


    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(path).placeholder(R.drawable.loading_pic).into(imageView);
        }

        @Override
        public ImageView createImageView(Context context) {
            return new ImageView(context);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    private class HomeMenuAdapter extends BaseQuickAdapter<Index.TopGameBean.SubMenuBean, BaseViewHolder> {
        HomeMenuAdapter(@LayoutRes int layoutResId, @Nullable List<Index.TopGameBean.SubMenuBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Index.TopGameBean.SubMenuBean item) {
            helper.setText(R.id.tv_home_game_menu, item.getTitle());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(UserEvent event) {
        if (event.isLogin()) {
            btnLogin.setVisibility(View.GONE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        int unReadMsg = event.getUnReadMsg();
        if (unReadMsg > 0) {
            tvMessageNum.setVisibility(View.VISIBLE);
            tvMessageNum.setText(unReadMsg > 9 ? "N" : String.valueOf(unReadMsg));
        } else {
            tvMessageNum.setVisibility(View.GONE);
            tvMessageNum.setText("0");
        }
    }
}
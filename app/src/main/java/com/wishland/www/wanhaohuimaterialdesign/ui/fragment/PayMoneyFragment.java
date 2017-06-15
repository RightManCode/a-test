package com.wishland.www.wanhaohuimaterialdesign.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseFragment;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.PayType;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.model.net.Api;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.LoginActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.PayActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.activity.WebActivity;
import com.wishland.www.wanhaohuimaterialdesign.ui.customeView.CustomExpandableListView;
import com.wishland.www.wanhaohuimaterialdesign.utils.ImageLoader;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;

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

public class PayMoneyFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    Unbinder unbinder;
    @BindView(R.id.fl_login)
    FrameLayout flLogin;
    @BindView(R.id.cel)
    CustomExpandableListView cel;
    private FundsBankExpandAdapter fundsBankExpandAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_money, container, false);
        unbinder = ButterKnife.bind(this, view);
        srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        //设置Group不可以点击
        cel.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//返回true,表示不可点击
            }
        });
        cel.setNestedScrollingEnabled(false);
        //设置child点击事件
        cel.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                return false;
            }
        });
        fundsBankExpandAdapter = new FundsBankExpandAdapter(getActivity());
        return view;
    }

    @Override
    protected void initVariable() {
        refreshData();
    }

    public void refreshData() {
        OkGo.post(Api.PAY_TYPE)
                .params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {
                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        PayType payType = new Gson().fromJson(s, PayType.class);
                        List<PayType.PayListBean> pay_list = payType.getPay_list();
                        fundsBankExpandAdapter.setExpandableData(pay_list);
                        cel.setAdapter(fundsBankExpandAdapter);
                        //展开所有分组
                        int groupCount = cel.getCount();
                        for (int i = 0; i < groupCount; i++) {
                            cel.expandGroup(i);
                        }
                        cel.setGroupIndicator(null);//将控件默认的左边箭头去掉
                        fundsBankExpandAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_submit_login)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public View getScrollableView() {
        return cel;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if (isLogin())
//                flLogin.setVisibility(View.GONE);
//            refreshData();
//        } else {
//
//        }
//    }

    class FundsBankExpandAdapter extends BaseExpandableListAdapter {

        private List<PayType.PayListBean> pay_list;
        private Context mContext;
        private Intent intent;

        FundsBankExpandAdapter(Context bastcontext) {
            this.mContext = bastcontext;
        }

        void setExpandableData(List<PayType.PayListBean> pay_list) {
            this.pay_list = pay_list;
        }


        @Override

        public int getGroupCount() {
            return pay_list.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return pay_list.get(i).getItem().size();
        }

        @Override
        public Object getGroup(int i) {
            return pay_list.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return pay_list.get(i).getItem().get(i);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            GroupHolder groupholder;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_pay_type, viewGroup, false);
                groupholder = new GroupHolder(view);
                view.setTag(groupholder);
            } else {
                groupholder = (GroupHolder) view.getTag();
            }
            ImageLoader.load(getActivity(), pay_list.get(i).getTitle_img(), groupholder.ivPayTypeTitle);
            groupholder.tvPayTypeTitle.setText(pay_list.get(i).getTitle());
            return view;
        }

        @Override
        public View getChildView(final int i, final int i1, boolean b, View view, final ViewGroup viewGroup) {
            ChildHolder childholder;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_pay_type_content, viewGroup, false);
                childholder = new ChildHolder(view);
                view.setTag(childholder);
            } else {
                childholder = (ChildHolder) view.getTag();
            }
            final String name = pay_list.get(i).getItem().get(i1).getName();
            childholder.btnPayTypeContent.setText(name);
            childholder.btnPayTypeContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PayType.PayListBean payListBean = pay_list.get(i);  //父类
                    String type = payListBean.getType();
                    switch (type) {
                        case "url":
                            intent = new Intent(getActivity(), WebActivity.class);
                            intent.putExtra("url", payListBean.getUrl());
                            break;
                        case "data":
                            String url = payListBean.getUrl().replace("[para]", payListBean.getItem().get(i1).getPara());
                            intent = new Intent(mContext, PayActivity.class);
                            intent.putExtra("title", name);
                            intent.putExtra("url", url);
                            break;
                    }
                    mContext.startActivity(intent);
                }
            });
//            ImageLoader.load(getActivity(),pay_list.get(i).getItem().get(i1).get);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }

        class ChildHolder {
            @BindView(R.id.iv_pay)
            ImageView ivPay;
            @BindView(R.id.btn_pay_type_content)
            TextView btnPayTypeContent;

            ChildHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        class GroupHolder {
            @BindView(R.id.iv_pay_type_title)
            ImageView ivPayTypeTitle;
            @BindView(R.id.tv_pay_type_title)
            TextView tvPayTypeTitle;

            GroupHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}

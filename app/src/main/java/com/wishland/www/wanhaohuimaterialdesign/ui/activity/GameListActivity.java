package com.wishland.www.wanhaohuimaterialdesign.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;
import com.lzy.okgo.request.BaseRequest;
import com.wishland.www.wanhaohuimaterialdesign.R;
import com.wishland.www.wanhaohuimaterialdesign.base.BaseStyleActivity;
import com.wishland.www.wanhaohuimaterialdesign.model.bean.GameList;
import com.wishland.www.wanhaohuimaterialdesign.model.net.AbsCallbackPro;
import com.wishland.www.wanhaohuimaterialdesign.utils.ImageLoader;
import com.wishland.www.wanhaohuimaterialdesign.utils.NetUtils;
import com.wishland.www.wanhaohuimaterialdesign.utils.handler.UserHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class GameListActivity extends BaseStyleActivity {

    @BindView(R.id.rec_game)
    RecyclerView recGame;
    private String url;
    private GameList gameList;
    private BaseQuickAdapter adapter;
    private List<GameList.GameBean> gameListGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list, R.layout.base_toolbar_back);
        ButterKnife.bind(this);
        recGame.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<GameList.GameBean, BaseViewHolder>(R.layout.item_game, gameListGame) {

            @Override
            protected void convert(BaseViewHolder helper, GameList.GameBean item) {
                helper.setText(R.id.tv_ganme_name, item.getName());
                ImageLoader.load(GameListActivity.this, gameList.getImgPath() + item.getImage().replace("&","_"), (ImageView) helper.getView(R.id.iv_game));
            }

        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (UserHandler.isLogin()) {
                    Intent intent = new Intent(GameListActivity.this, WebActivity.class);
                    intent.putExtra("url", gameList.getPlayPath() + gameList.getGame().get(position).getPara());
                    startActivity(intent);
                }else {
                    startActivity(new Intent(GameListActivity.this,LoginActivity.class));
                }
            }
        });
        recGame.setAdapter(adapter);
        getContentView().showLoading();
    }

    @Override
    protected void initVariable() {
        url = getIntent().getStringExtra("url");
        String name = getIntent().getStringExtra("name");
        setTitle(name);
        gameListGame = new ArrayList<>();
    }

    @Override
    protected void initDate() {
//        OkGo.get(url).execute(new StringCallback() {
//            @Override
//            public void onSuccess(String s, Call call, Response response) {
//                if (TextUtils.isEmpty(s))
//                    Toast.makeText(GameListActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
//                try {
//                    JSONObject jsonObject = new JSONObject(s);

//                    if (200 == (jsonObject.getInt("status"))) {
//                        String data = jsonObject.getString("data");
//                        gameList = new Gson().fromJson(data, new TypeToken<GameList>() {
//                        }.getType());
//                        gameListGame.addAll(gameList.getGame());
//                        adapter.notifyDataSetChanged();
//                        getContentView().showContent();
//                    } else if (301 == (jsonObject.getInt("status"))) {
//                        Intent intent = new Intent(App.getInstance(), LoginActivity.class);
//                        intent.putExtra("toast", "用户登录状态异常，请重新登录");
//                        App.getInstance().startActivity(intent);
//                        UserHandler.logout();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        OkGo.post(url)
                .params(NetUtils.getParamsPro())
                .execute(new AbsCallbackPro() {


                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if (e != null&& e instanceof OkGoException) {
                            getContentView().showEmpty(R.drawable.arrow, "出错了", e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            gameList = new Gson().fromJson(s, new TypeToken<GameList>() {
                            }.getType());
                            gameListGame.addAll(gameList.getGame());
                            adapter.notifyDataSetChanged();
                            getContentView().showContent();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
    }
}

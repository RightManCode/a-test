package com.wishland.www.wanhaohuimaterialdesign.model.bean;

/**
 * Created by gerry on 2017/6/10.
 */

public class MyCollect {

    /**
     * id : 3
     * img : http://029256.com/mgline/images/DoubleExposureBlackjackGold_Logo2.png
     * name : 金牌经典21点
     * url : http://029256.com/api.d/url_agent.php?token=[token]&url=mgline/mg.php?game=ClassicBlackjackGold
     * state : 1
     */

    private String id;
    private String img;
    private String name;
    private String url;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

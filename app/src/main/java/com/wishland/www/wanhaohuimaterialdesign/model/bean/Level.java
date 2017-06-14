package com.wishland.www.wanhaohuimaterialdesign.model.bean;

/**
 * Created by gerry on 2017/5/26.
 */

public class Level {

    /**
     * period : 周
     * lastpeirod : 2017-05-28 23:59:59
     * nextperiod : 2017-05-29 02:00:00
     * img : http://029256.com/common/images/vipico/vip/2.gif
     * levelname : VIP2
     * ckje : null
     * ztzje : null
     */

    private String period;
    private String lastpeirod;
    private String nextperiod;
    private String img;
    private String levelname;
    private Double ckje;
    private Double ztzje;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLastpeirod() {
        return lastpeirod;
    }

    public void setLastpeirod(String lastpeirod) {
        this.lastpeirod = lastpeirod;
    }

    public String getNextperiod() {
        return nextperiod;
    }

    public void setNextperiod(String nextperiod) {
        this.nextperiod = nextperiod;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }

    public Double getCkje() {
        return ckje;
    }

    public void setCkje(Double ckje) {
        this.ckje = ckje;
    }

    public Double getZtzje() {
        return ztzje;
    }

    public void setZtzje(Double ztzje) {
        this.ztzje = ztzje;
    }
}

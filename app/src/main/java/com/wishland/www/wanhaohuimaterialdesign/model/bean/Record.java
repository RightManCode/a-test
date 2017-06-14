package com.wishland.www.wanhaohuimaterialdesign.model.bean;

/**
 * Created by gerry on 2017/5/17.
 */

public class Record {

    /**
     * serialNum : testjishu_dzfs_600148
     * usaTime : 2017-05-19 03:09:57
     * chinaTime : 2017-05-19 15:09:57
     * sum : 0.06
     * type : 3
     * notes : 电子游艺自动返水[管理员-admindv-结算]
     * state : 成功
     */

    private String serialNum;
    private String usaTime;
    private String chinaTime;
    private String sum;
    private int type;
    private String notes;
    private String state;
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getUsaTime() {
        return usaTime;
    }

    public void setUsaTime(String usaTime) {
        this.usaTime = usaTime;
    }

    public String getChinaTime() {
        return chinaTime;
    }

    public void setChinaTime(String chinaTime) {
        this.chinaTime = chinaTime;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}

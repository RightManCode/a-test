package com.wishland.www.wanhaohuimaterialdesign.model.bean;

/**
 * Created by gerry on 2017/5/18.
 */

public class RemenberUser {
    private String userName;
    private String pw;

    public RemenberUser(String name, String pw) {
        this.userName = name;
        this.pw = pw;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}

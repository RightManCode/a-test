package com.wishland.www.wanhaohuimaterialdesign.model.bean;

/**
 * Created by gerry on 2017/5/4.
 */

public class UserInfo {
    private AccountInfo accountInfo;
    private String token;
    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

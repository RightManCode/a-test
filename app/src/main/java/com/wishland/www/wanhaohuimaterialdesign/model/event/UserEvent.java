package com.wishland.www.wanhaohuimaterialdesign.model.event;

/**
 * Created by gerry on 2017/5/17.
 */

public class UserEvent {
    private boolean isLogin;

    public UserEvent(boolean b) {
        this.isLogin = b;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}

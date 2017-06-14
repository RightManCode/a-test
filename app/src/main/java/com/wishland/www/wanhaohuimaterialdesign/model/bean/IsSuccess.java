package com.wishland.www.wanhaohuimaterialdesign.model.bean;

import android.text.TextUtils;

/**
 * Created by gerry on 2017/5/22.
 */

public class IsSuccess {
    String status;

    public boolean getStatus() {
        return TextUtils.equals(status, "success");
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

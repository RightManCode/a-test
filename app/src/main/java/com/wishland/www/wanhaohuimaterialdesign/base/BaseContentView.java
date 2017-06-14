package com.wishland.www.wanhaohuimaterialdesign.base;

/**
 * Created by gerry
 * 显示内容ViewGroup基类
 */
interface BaseContentView {

    void showErrorMsg(String msg);

    //=======  State  =======
    void stateError();

    void stateLoading();

    void stateNormal();
}

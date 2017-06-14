package com.wishland.www.wanhaohuimaterialdesign.model.event;

/**
 * Created by gerry on 2017/6/9.
 */

public class MessageEvent {
    private int unReadMsg;

    public MessageEvent(int unReadMsg) {
        this.unReadMsg = unReadMsg;
    }

    public int getUnReadMsg() {
        return unReadMsg;
    }

    public void setUnReadMsg(int unReadMsg) {
        this.unReadMsg = unReadMsg;
    }
}

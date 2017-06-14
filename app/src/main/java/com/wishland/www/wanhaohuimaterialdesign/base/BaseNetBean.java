package com.wishland.www.wanhaohuimaterialdesign.base;

/**
 * Created by gerry on 2017/5/2.
 */

public class BaseNetBean<T> {

    /**
     * status : 200
     * data : T
     */

    private int status;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

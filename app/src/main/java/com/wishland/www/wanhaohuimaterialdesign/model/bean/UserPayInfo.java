package com.wishland.www.wanhaohuimaterialdesign.model.bean;

import java.util.List;

/**
 * Created by gerry on 2017/6/9.
 */

public class UserPayInfo {

    /**
     * user : {"username":"testjishu","money":"1837.570"}
     * select : []
     * limit : 10
     * url : http://029256.com/api.d/url_agent.php?token=[token]&url=pay&type=rhlepay&id=93&money=[money]&code=[code]
     */

    private UserBean user;
    private String limit;
    private String url;
    private List<?> select;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<?> getSelect() {
        return select;
    }

    public void setSelect(List<?> select) {
        this.select = select;
    }

    public static class UserBean {
        /**
         * username : testjishu
         * money : 1837.570
         */

        private String username;
        private String money;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}

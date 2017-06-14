package com.wishland.www.wanhaohuimaterialdesign.model.bean;

import java.util.List;

/**
 * Created by gerry on 2017/5/10.
 */

public class TradeQuery {

    /**
     * otherList :
     * commonList : [{"serialNum":"201705051337291000i97","usaTime":"2017-05-05 13:37:37","chinaTime":"2017-05-06 01:37:37","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"201705051329051000i97","usaTime":"2017-05-05 13:29:45","chinaTime":"2017-05-06 01:29:45","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"201705040700351000_94","usaTime":"2017-05-04 07:02:31","chinaTime":"2017-05-04 19:02:31","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"683521493824175904_95","usaTime":"2017-05-03 11:10:15","chinaTime":"2017-05-03 23:10:15","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"VC6835220170503100105581","usaTime":"2017-05-03 10:02:24","chinaTime":"2017-05-03 22:02:24","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"XB201705030410051283","usaTime":"2017-05-03 04:11:39","chinaTime":"2017-05-03 16:11:39","sum":"11.00","type":0,"points":"0.110","state":"成功"},{"serialNum":"XB201705030307397587","usaTime":"2017-05-03 03:16:07","chinaTime":"2017-05-03 15:16:07","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"VC6835220170502074624467","usaTime":"2017-05-02 07:47:16","chinaTime":"2017-05-02 19:47:16","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"VC6835220170502065147908","usaTime":"2017-05-02 07:39:44","chinaTime":"2017-05-02 19:39:44","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"VC6835220170501075151239","usaTime":"2017-05-02 00:48:37","chinaTime":"2017-05-02 12:48:37","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"VC6835220170501084104927","usaTime":"2017-05-02 00:46:52","chinaTime":"2017-05-02 12:46:52","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"VC6835220170501084649562","usaTime":"2017-05-02 00:45:46","chinaTime":"2017-05-02 12:45:46","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"201705011340251000_94","usaTime":"2017-05-01 13:40:36","chinaTime":"2017-05-02 01:40:36","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"XTBPAY201704290752052238_69","usaTime":"2017-04-29 07:55:05","chinaTime":"2017-04-29 19:55:05","sum":"11.00","type":0,"points":"0.110","state":"成功"},{"serialNum":"XTBPAY201704281652398084_69","usaTime":"2017-04-28 16:53:04","chinaTime":"2017-04-29 04:53:04","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"XTBPAY201704281650334841_69","usaTime":"2017-04-28 16:51:32","chinaTime":"2017-04-29 04:51:32","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"68352201704241111389842_93","usaTime":"2017-04-24 11:11:55","chinaTime":"2017-04-24 23:11:55","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"XTBPAY201704121027064247_66","usaTime":"2017-04-12 10:27:28","chinaTime":"2017-04-12 22:27:28","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"testjishuYF2017041104031222","usaTime":"2017-04-11 04:03:48","chinaTime":"2017-04-11 16:03:48","sum":"10.00","type":0,"points":"0.100","state":"成功"},{"serialNum":"XTBPAY201704100502078350_65","usaTime":"2017-04-10 05:02:22","chinaTime":"2017-04-10 17:02:22","sum":"10.00","type":0,"points":"0.100","state":"成功"}]
     * queryid : 20
     * queryCnt : 20
     */

    private String otherList;
    private int queryid;
    private int queryCnt;
    private List<CommonListBean> commonList;

    public String getOtherList() {
        return otherList;
    }

    public void setOtherList(String otherList) {
        this.otherList = otherList;
    }

    public int getQueryid() {
        return queryid;
    }

    public void setQueryid(int queryid) {
        this.queryid = queryid;
    }

    public int getQueryCnt() {
        return queryCnt;
    }

    public void setQueryCnt(int queryCnt) {
        this.queryCnt = queryCnt;
    }

    public List<CommonListBean> getCommonList() {
        return commonList;
    }

    public void setCommonList(List<CommonListBean> commonList) {
        this.commonList = commonList;
    }

    public static class CommonListBean {
        /**
         * serialNum : 201705051337291000i97
         * usaTime : 2017-05-05 13:37:37
         * chinaTime : 2017-05-06 01:37:37
         * sum : 10.00
         * type : 0
         * points : 0.100
         * state : 成功
         */

        private String serialNum;
        private String usaTime;
        private String chinaTime;
        private String sum;
        private int type;
        private String points;
        private String state;

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

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}

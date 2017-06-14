package com.wishland.www.wanhaohuimaterialdesign.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gerry on 2017/5/26.
 */

public class Message {


    /**
     * unReadMsg : 0
     * queryId : 20841691
     * dataList : [{"isNew":0,"msgId":20841691,"time":"2017-05-26 03:27:32","title":"【不定期更换入款银行账号】","detailedInfo":"尊敬的会员，您好，我司将不定期更换公司入款银行账号，每次公司入款前请查看最新的入款银行账号，避免存入到过期账号导致资金损失，谢谢。","from":""},{"isNew":0,"msgId":20646320,"time":"2017-05-25 06:09:41","title":"工商杨风云停用公告","detailedInfo":"因财务业务需要，目前将暂停使用工商银行杨风云，请入款前查看最新的银行账号，如入款到停止使用的银行账户，公司将概不负责，请广大会员相互转告！！！","from":"万濠会集团"},{"isNew":0,"msgId":20574474,"time":"2017-05-19 03:52:21","title":"汇款取消","detailedInfo":".","from":"万濠会"}]
     */

    private int unReadMsg;
    private int queryId;
    private List<DataListBean> dataList;

    public int getUnReadMsg() {
        return unReadMsg;
    }

    public void setUnReadMsg(int unReadMsg) {
        this.unReadMsg = unReadMsg;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean implements Serializable{
        /**
         * isNew : 0
         * msgId : 20841691
         * time : 2017-05-26 03:27:32
         * title : 【不定期更换入款银行账号】
         * detailedInfo : 尊敬的会员，您好，我司将不定期更换公司入款银行账号，每次公司入款前请查看最新的入款银行账号，避免存入到过期账号导致资金损失，谢谢。
         * from :
         */

        private int isNew;
        private int msgId;
        private String time;
        private String title;
        private String detailedInfo;
        private String from;

        public int getIsNew() {
            return isNew;
        }

        public void setIsNew(int isNew) {
            this.isNew = isNew;
        }

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetailedInfo() {
            return detailedInfo;
        }

        public void setDetailedInfo(String detailedInfo) {
            this.detailedInfo = detailedInfo;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}

package com.wishland.www.wanhaohuimaterialdesign.model.bean;

import java.util.List;

/**
 * Created by gerry on 2017/5/5.
 */

public class AccountInfo {

    /**
     * accountInfo : {"accountName":"testjishu","registerTime":"2016-10-01 08:47:04","lastLoginTime":"2017-05-05 08:39:00"}
     * balanceInfo : {"balance":"900.076","drawingBet":1020}
     * financeInfo : {"PayeeName":"技术","Bank":"中国农业银行","BankAccount":"72727227727282828282","AccountAddress":"北京"}
     * remittanceInfo : {"remittanceType":[{"typeID":"银行柜台","title":"银行柜台"},{"typeID":"ATM现金","title":"ATM现金"},{"typeID":"ATM卡转","title":"ATM卡转"},{"typeID":"网银转账","title":"网银转账"},{"typeID":0,"title":"其它[手动输入]"}]}
     */

    private AccountInfoBean accountInfo;
    private BalanceInfoBean balanceInfo;
    private FinanceInfoBean financeInfo;
    private RemittanceInfoBean remittanceInfo;

    public AccountInfoBean getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfoBean accountInfo) {
        this.accountInfo = accountInfo;
    }

    public BalanceInfoBean getBalanceInfo() {
        return balanceInfo;
    }

    public void setBalanceInfo(BalanceInfoBean balanceInfo) {
        this.balanceInfo = balanceInfo;
    }

    public FinanceInfoBean getFinanceInfo() {
        return financeInfo;
    }

    public void setFinanceInfo(FinanceInfoBean financeInfo) {
        this.financeInfo = financeInfo;
    }

    public RemittanceInfoBean getRemittanceInfo() {
        return remittanceInfo;
    }

    public void setRemittanceInfo(RemittanceInfoBean remittanceInfo) {
        this.remittanceInfo = remittanceInfo;
    }

    public static class AccountInfoBean {
        /**
         * accountName : testjishu
         * registerTime : 2016-10-01 08:47:04
         * lastLoginTime : 2017-05-05 08:39:00
         */

        private String accountName;
        private String registerTime;
        private String lastLoginTime;

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(String registerTime) {
            this.registerTime = registerTime;
        }

        public String getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(String lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }
    }

    public static class BalanceInfoBean {
        /**
         * balance : 900.076
         * drawingBet : 1020
         */

        private String balance;
        private int drawingBet;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public int getDrawingBet() {
            return drawingBet;
        }

        public void setDrawingBet(int drawingBet) {
            this.drawingBet = drawingBet;
        }
    }

    public static class FinanceInfoBean {
        /**
         * PayeeName : 技术
         * Bank : 中国农业银行
         * BankAccount : 72727227727282828282
         * AccountAddress : 北京
         */

        private String PayeeName;
        private String Bank;
        private String BankAccount;
        private String AccountAddress;

        public String getPayeeName() {
            return PayeeName;
        }

        public void setPayeeName(String PayeeName) {
            this.PayeeName = PayeeName;
        }

        public String getBank() {
            return Bank;
        }

        public void setBank(String Bank) {
            this.Bank = Bank;
        }

        public String getBankAccount() {
            return BankAccount;
        }

        public void setBankAccount(String BankAccount) {
            this.BankAccount = BankAccount;
        }

        public String getAccountAddress() {
            return AccountAddress;
        }

        public void setAccountAddress(String AccountAddress) {
            this.AccountAddress = AccountAddress;
        }
    }

    public static class RemittanceInfoBean {
        private List<RemittanceTypeBean> remittanceType;

        public List<RemittanceTypeBean> getRemittanceType() {
            return remittanceType;
        }

        public void setRemittanceType(List<RemittanceTypeBean> remittanceType) {
            this.remittanceType = remittanceType;
        }

        public static class RemittanceTypeBean {
            /**
             * typeID : 银行柜台
             * title : 银行柜台
             */

            private String typeID;
            private String title;

            public String getTypeID() {
                return typeID;
            }

            public void setTypeID(String typeID) {
                this.typeID = typeID;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}

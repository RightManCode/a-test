package com.wishland.www.wanhaohuimaterialdesign.model.bean;

import java.util.List;

/**
 * Created by gerry on 2017/5/9.
 */

public class TransferAccountsLimit {

    private List<WalletBean> wallet;

    public List<WalletBean> getWallet() {
        return wallet;
    }

    public void setWallet(List<WalletBean> wallet) {
        this.wallet = wallet;
    }

    public static class WalletBean {
        /**
         * name : 我的钱包
         * amout : 1444.076
         * wallettype : wallet
         */

        private String name;
        private String amout;
        private String wallettype;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmout() {
            return amout;
        }

        public void setAmout(String amout) {
            this.amout = amout;
        }

        public String getWallettype() {
            return wallettype;
        }

        public void setWallettype(String wallettype) {
            this.wallettype = wallettype;
        }

        @Override
        public String toString() {
            return name+"   "+amout;
        }
    }
}

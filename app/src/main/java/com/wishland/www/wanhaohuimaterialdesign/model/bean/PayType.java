package com.wishland.www.wanhaohuimaterialdesign.model.bean;

import java.util.List;

/**
 * Created by gerry on 2017/6/8.
 */

public class PayType {

    /**
     * pay_list : [{"title":"01 在线支付","title_img":"http://029256.com/m/data/templates/v.3/images/info_icon01.png","item":[{"name":"测试艾米森","para":"type=amspay&id=59"},{"name":"四：网银支付送1%","para":"type=jfpay&id=94"},{"name":"五：微信与网银支付送1%","para":"type=ddbpay&id=97"},{"name":"八：网银充值送1%","para":"type=jfpay&id=108"},{"name":"输入手机号码短信支付送1%","para":"type=jfpay&id=101"},{"name":"六：微信支付送1%","para":"type=rhlepay&id=93"},{"name":"测试锐付支付宝","para":"type=ruifupay_alipay&id=106"},{"name":"测试锐付微信","para":"type=ruifupay_wechat&id=107"}],"url":"http://029256.com/api.d/index.php?pay/para&[para]","type":"data"},{"title":"02 公司入款","title_img":"http://029256.com/m/data/templates/v.3/images/info_icon02.png","item":[{"name":"汇款提交","para":"hk"}],"url":"http://029256.com/api.d/url_agent.php?token=[token]&url=member%2Fhkmoney.php%3Fmodel%3Dy%26api","type":"url"},{"title":"03 支付宝入款","title_img":"http://029256.com/m/data/templates/v.3/images/info_icon02.png","item":[{"name":"支付宝提交","para":"alipay"}],"url":"http://029256.com/api.d/url_agent.php?token=[token]&url=member%2Falipay.php%3Fmodel%3Dy%26api","type":"url"}]
     * data_img : http://029256.com/m/data/templates/v.3/images/info_icon03.png
     */

    private String data_img;
    private List<PayListBean> pay_list;

    public String getData_img() {
        return data_img;
    }

    public void setData_img(String data_img) {
        this.data_img = data_img;
    }

    public List<PayListBean> getPay_list() {
        return pay_list;
    }

    public void setPay_list(List<PayListBean> pay_list) {
        this.pay_list = pay_list;
    }

    public static class PayListBean {
        /**
         * title : 01 在线支付
         * title_img : http://029256.com/m/data/templates/v.3/images/info_icon01.png
         * item : [{"name":"测试艾米森","para":"type=amspay&id=59"},
         * {"name":"四：网银支付送1%","para":"type=jfpay&id=94"},
         * {"name":"五：微信与网银支付送1%","para":"type=ddbpay&id=97"},
         * {"name":"八：网银充值送1%","para":"type=jfpay&id=108"},
         * {"name":"输入手机号码短信支付送1%","para":"type=jfpay&id=101"},
         * {"name":"六：微信支付送1%","para":"type=rhlepay&id=93"},
         * {"name":"测试锐付支付宝","para":"type=ruifupay_alipay&id=106"},
         * {"name":"测试锐付微信","para":"type=ruifupay_wechat&id=107"}]
         * url : http://029256.com/api.d/index.php?pay/para&[para]
         * type : data
         */

        private String title;
        private String title_img;
        private String url;
        private String type;
        private List<ItemBean> item;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle_img() {
            return title_img;
        }

        public void setTitle_img(String title_img) {
            this.title_img = title_img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class ItemBean {
            /**
             * name : 测试艾米森
             * para : type=amspay&id=59
             */

            private String name;
            private String para;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPara() {
                return para;
            }

            public void setPara(String para) {
                this.para = para;
            }
        }
    }
}

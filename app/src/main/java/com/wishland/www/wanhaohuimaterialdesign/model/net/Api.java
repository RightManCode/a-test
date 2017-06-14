package com.wishland.www.wanhaohuimaterialdesign.model.net;

/**
 * Created by gerry on 2017/5/2.
 */

public class Api {
    private static final String HOST = "http://029256.com/api.d/";

    public static final String CUSTOMER_SERVICES = "https://chat6.livechatvalue.com/chat/chatClient/chatbox.jsp?companyID=17779&configID=48708&jid=&s=1";

    public static final String LOGIN = HOST + "index.php?user/login";
    //首页信息
    public static final String INDEX = HOST + "index.php?index/index";
    public static final String GET_ACCOUNT_INFO = HOST + "index.php?user/account";
    public static final String REGISTER = HOST + "index.php?user/reg";
    //账户余额情况
    public static final String TRANSFER_ACCOUNTS_LIMIT = HOST + "index.php?fund/balance";
    //金额转换
    public static final String TRANSFER_ACCOUNTS = HOST + "index.php?fund/walletExchange";
    //交易查询
    public static final String TRADE_QUERY = HOST + "index.php?fund/tradeQuery";
    //修改登录密码
    public static final String REEDIT_LOGIN_PW = HOST + "index.php?user/mlpw";
    //修改交易密码
    public static final String REEDIT_TRANS_PW = HOST + "index.php?user/mqkpw";
    //获取验证码
    public static final String GET_VERIFICATION_CODE = HOST + "index.php?vcode/get";
    //用户注销
    public static final String LOGIN_OUT = HOST + "index.php?user/logout";
    //取款
    public static final String WITHDRAW_SUBMIT = HOST + "index.php?fund/withdrawSubmit";
    //绑定账户
    public static final String BIND_ACCOUNT = HOST + "index.php?fund/bind";
    //    public static final String BIND_ACCOUNT = HOST + "index.php?fund/bindAccount";
    //更多（优惠活动）
    public static final String MORE = HOST + "index.php?index/moreActivity";
    //游戏推荐
    public static final String GAME_ADVICE = HOST + "index.php?favorite/recommend";
    //添加收藏
    public static final String ADD_COLLECTION = HOST + "index.php?favorite/add";
    //收藏列表
    public static final String GET_COLLECTION = HOST + "index.php?favorite/get";
    //银行列表
    public static final String BANK_LIST = HOST + "index.php?{15d80a9f-dd69-4dd1-bfe7-3598068dd30e}fund/banklist";
    //我的等级
    public static final String LEVEL = HOST + "index.php?user/level";
    //我的消息
    public static final String MESSAGE = HOST + "index.php?user/message";
    //请求全局信息
    public static final String GLOBAL_DATA = HOST + "index.php?user/globaldata";
    //信息已读
    public static final String MSG_READ = HOST + "index.php?user/msgRead";
    //信息删除
    public static final String DEL_MSG = HOST + "index.php?user/delMsg";
    //存款接口
    public static final String PAY_TYPE = HOST + "index.php?pay/type";
    //转换记录查询
    public static final String QUERY_EXCHANGE = HOST + "index.php?fund/queryExchange";

}

package com.bluemobi.constant;

public class Constant {
	/** 当前用户 */
	public final static String CURRENT_USER = "currentUser";
	/** 网站当前用户 */
	public final static String WEB_CURRENT_USER = "webcurrentUser";
	/** 当前验证码 */
	public final static String CURRENT_USER_VALIDATE_CODE_KEY = "CURRENT_USER_VALIDATE_CODE_KEY";

	// 云片网key
	public final static String YUNPIAN_KEY = "9be86b85556252ebdef9363d955656fb";
    // 七牛access_key
    public final static String ACCESS_KEY = "gNG6A6WSrom-vGQtQQiCZQ1uDhQyt9Ode7Z9ewn_";
    // 七牛select_key
    public final static String SECRET_KEY = "cqby6ezdFna2nOKYaVa4e1rtc_7rt2wd5_G08vRF";
    // 七牛图片空间名
    public final static String bucket = "uhoem-face-test";
    // 空间uhoem-face-test的默认域名
    public final static String domain = "http://7xkfb2.com2.z0.glb.qiniucdn.com/";

	/**
	 * 删除标记
	 */
	public final static String NOTCUT="0";//未删除
	public final static String CUT="1";//删除
	/**
	 * 性别
	 */
	public final static String MAN="0";//男
	public final static String WOMAN="1";//女
	/**
	 * 审核状态
	 */
	public final static String PASS = "0";//审核通过
	public final static String WAIT="1";//待审核
	public final static String NOTPASS="2";//审核未通过
	/**
	 * 用户类型
	 */
	public final static String CLERK="0";//店员、
	public final static String ADMIN="1";//管理员
	public final static String INSIDER="2";//会员
	public final static String CUSTOMER="3";//用户
    /**
     * 设备类型
     */
    public final static String WEB_PC="web_pc";
    public final static String WEB_MOBILE="web_mobile";
    public final static String APP_ANDROID="app_android";
    public final static String APP_IPHONE="app_iphone";

	/**
	 * 第三方登录类型
	 */
	public final static String LOGIN_TYPE_NONE="1";
	public final static String LOGIN_TYPE_WEIXIN="2";
	public final static String LOGIN_TYPE_QQ="3";
	public final static String LOGIN_TYPE_XINLANG="4";

	/**
	 * 第三方登录appid
	 */
	public final static String LOGIN_WEIXIN_APPID = "wxf4b70027ec00a7dc";
	public final static String LOGIN_QQ_APPID = "1104712303";
	public final static String LOGIN_XINLANG_APPID = "669999433";

	/**
	 * 是否是第一次通过第三方登录
	 */
	public final static String IF_FIRST_LOGIN_YES = "yes";
	public final static String IF_FIRST_LOGIN_NO = "no";
	
	/**
	 * 
	 * 订单状态
	 */
	public final static int ORDER_STATUS_WAIT_PAY = 0; //待支付
	public final static int ORDER_STATUS_PAY = 1; // 已支付
	public final static int ORDER_STATUS_QUIT = 2; // 已取消
	public final static int ORDER_STATUS_RECEIVE = 3; // 已收货
	/**
	 * 支付状态
	 */
	public final static int PAY_NOT = 0; // 为支付
	public final static int PAY_HAVE = 1; // 已支付
	
	/**
	 * 
	 * 配送状态
	 */
	public final static int SHIP_NOT = 0; // 未发货
	public final static int SHIP_HAVE = 1; // 已发货
	public final static int SHIP_RECEIVE = 2; // 已收货
	
	/**
	 * 
	 * 订单操作
	 */
	public final static int ORDER_CONFIRM = 0; // 确认订单
	public final static int ORDER_PAY = 1;     // 支付
	public final static int ORDER_CANCEL = 2;  // 取消
	public final static int ORDER_RECEICE = 3; // 确认收货
	
}

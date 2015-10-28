package com.bluemobi.pay.util;

public class ConstantUtil {
	/**
	 * �̼ҿ��Կ��Ƕ�ȡ�����ļ�
	 */
	
	//��ʼ��
//	public static String APP_ID = "wxd930ea5d5a258f4f";//΢�ſ���ƽ̨Ӧ��id
	public static String APP_ID = "wxf4b70027ec00a7dc";// �ͻ�
//	public static String APP_SECRET = "db426a9829e4b49a0dcac7b4162da6b6";//Ӧ�ö�Ӧ��ƾ֤
	public static String APP_SECRET = "97ffe50dd6e8cfbe9de22e5302026fa9";// �ͻ�
	                                   
	//Ӧ�ö�Ӧ����Կ
//	public static String APP_KEY = "L8LrMqqeGRxST5reouB0K66CaYAWpqhAVsq7ggKkxHCOastWksvuX1uvmvQclxaHoYd3ElNBrNO2DHnnzgfVG9Qs473M3DTOZug5er46FhuGofumV8H2FVR9qkjSlC5K";
	public static String APP_KEY = "";
//	public static String PARTNER = "1900000109";//�Ƹ�ͨ�̻���
	public static String PARTNER = "1269932701";// �ͻ�
	
//	public static String PARTNER_KEY = "8934e7d15453e97507ef794cf7b0519d";//�̻��Ŷ�Ӧ����Կ
	public static String API_KEY = "Uhoem20151008123weixin4000279600";//�̻�
	public static String TOKENURL = "https://api.weixin.qq.com/cgi-bin/token";//��ȡaccess_token��Ӧ��url
	public static String GRANT_TYPE = "client_credential";//�����̶�ֵ 
	public static String EXPIRE_ERRCODE = "42001";//access_tokenʧЧ�����󷵻ص�errcode
	public static String FAIL_ERRCODE = "40001";//�ظ���ȡ������һ�λ�ȡ��access_tokenʧЧ,���ش�����
	public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//��ȡԤ֧��id�Ľӿ�url
	public static String ACCESS_TOKEN = "access_token";//access_token����ֵ
	public static String ERRORCODE = "errcode";//�����ж�access_token�Ƿ�ʧЧ��ֵ
	public static String SIGN_METHOD = "sha1";//ǩ���㷨����ֵ
	
	public static String DEVICE_INFO = "WEB"; // �ն��豸��(�ŵ�Ż������豸ID)��ע�⣺PC��ҳ���ں���֧���봫"WEB"
	public static String TRADE_TYPE = "APP"; // ��������  ȡֵ���£�JSAPI��NATIVE��APP��WAP,��ϸ˵�������涨
	
	//package����ֵ
	public static String packageValue = "bank_type=WX&body=%B2%E2%CA%D4&fee_type=1&input_charset=GBK&notify_url=http%3A%2F%2F127.0.0.1%3A8180%2Ftenpay_api_b2c%2FpayNotifyUrl.jsp&out_trade_no=2051571832&partner=1900000109&sign=10DA99BCB3F63EF23E4981B331B0A3EF&spbill_create_ip=127.0.0.1&time_expire=20131222091010&total_fee=1";
	public static String traceid = "testtraceid001";//�����û�id
}

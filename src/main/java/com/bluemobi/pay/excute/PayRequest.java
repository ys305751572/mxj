package com.bluemobi.pay.excute;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.bluemobi.pay.AccessTokenRequestHandler;
import com.bluemobi.pay.ClientRequestHandler;
import com.bluemobi.pay.PackageRequestHandler;
import com.bluemobi.pay.PrepayIdRequestHandler;
import com.bluemobi.pay.util.ConstantUtil;
import com.bluemobi.pay.util.TenpayUtil;
import com.bluemobi.pay.util.WXUtil;
import com.bluemobi.utils.CommonUtils;

public class PayRequest {

	public static Map<String,Object> pay(HttpServletRequest request, HttpServletResponse response) {
		
		response.resetBuffer();
		response.setHeader("ContentType", "text/xml");
		String notify_url = "http://121.40.120.10:8080/mxj/api/pay/notifyWeixin"; // 客户
//		String notify_url = "http://121.41.106.3:8070/mxj/api/pay/notifyWeixin"; // 6Mai服务器

		String currTime = TenpayUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = TenpayUtil.buildRandom(4) + "";
		String out_trade_no = request.getAttribute("sn").toString();

		PackageRequestHandler packageReqHandler = new PackageRequestHandler(request, response);//���package�������� 
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);//��ȡprepayid��������
		ClientRequestHandler clientHandler = new ClientRequestHandler(request, response);//���ؿͻ���֧�������������

		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		int retcode;
		String retmsg = "";
		String xml_body = "";
		

		//��ȡprepayId
		String prepayid = "";
		
		//��ȡtokenֵ 
		String token = AccessTokenRequestHandler.getAccessToken();
		if (!"".equals(token)) {
			prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
			
			prepayReqHandler.setParameter("body", "test"); 
			prepayReqHandler.setParameter("device_info", ConstantUtil.DEVICE_INFO); 
			prepayReqHandler.setParameter("fee_type", "CNY");
			prepayReqHandler.setParameter("mch_id", ConstantUtil.PARTNER); //�̻���    
			String noncestr = WXUtil.getNonceStr();
			prepayReqHandler.setParameter("nonce_str", noncestr);
			prepayReqHandler.setParameter("notify_url", notify_url); 
			prepayReqHandler.setParameter("out_trade_no", out_trade_no); 
			
//			packageReqHandler.setParameter("input_charset", "GBK"); 
			
			String timestamp = WXUtil.getTimeStamp();
			String traceid = "";

			prepayReqHandler.setParameter("spbill_create_ip",CommonUtils.getRealAddress(request)); //������ɵĻ���IP��ָ�û��������IP  
			prepayReqHandler.setParameter("total_fee", request.getAttribute("fee").toString()); //��Ʒ���,�Է�Ϊ��λ  
			prepayReqHandler.setParameter("trade_type", ConstantUtil.TRADE_TYPE); // ֧������
			
			
			String sign = prepayReqHandler.createMD5Sign();
			prepayReqHandler.setParameter("sign", sign);
			
			try {
				prepayid = prepayReqHandler.sendPrepay();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (null != prepayid && !"".equals(prepayid)) {
				
				clientHandler.setParameter("appid", ConstantUtil.APP_ID);
				clientHandler.setParameter("noncestr", noncestr);
				clientHandler.setParameter("package", "Sign=WXPay");
				clientHandler.setParameter("partnerid", ConstantUtil.PARTNER);
				clientHandler.setParameter("prepayid", prepayid);
				clientHandler.setParameter("timestamp", WXUtil.getTimeStamp());
				
				sign = clientHandler.createMD5Sign();
				clientHandler.setParameter("sign", sign);
				
				resultMap = clientHandler.getMap();
				retcode = 0;
				retmsg = "OK";
			} else {
				retcode = -2;
				retmsg = "";
			}
		} else {
			retcode = -1;
			retmsg = "";
		}
		return resultMap;
	}
}

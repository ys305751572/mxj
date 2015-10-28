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
//		out.println("<?xml version=\"1.0\" encoding=\"GBK\"?>");
//		out.println("<root>");
		//---------------------------------------------------------
		//΢��֧������ʾ���̻����մ��ĵ����п������� 
		//---------------------------------------------------------

		//���ղƸ�֪ͨͨ��URL
		String notify_url = "http://121.40.120.10:8080/api/pay/notifyWeixin";

		//---------------��ɶ����� ��ʼ------------------------
		//��ǰʱ�� yyyyMMddHHmmss
		String currTime = TenpayUtil.getCurrTime();
		//8λ����
		String strTime = currTime.substring(8, currTime.length());
		//��λ�����
		String strRandom = TenpayUtil.buildRandom(4) + "";
		//10λ���к�,�������е���
//		String strReq = strTime + strRandom;
		//�����ţ��˴���ʱ����������ɣ��̻�����Լ��������ֻҪ����ȫ��Ψһ����
		String out_trade_no = request.getAttribute("sn").toString();
		//---------------��ɶ����� ����------------------------

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
			//����package��������
//			packageReqHandler.setParameter("bank_type", "WX");//��������
			prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
			
			prepayReqHandler.setParameter("body", "test"); //��Ʒ����   
			prepayReqHandler.setParameter("device_info", ConstantUtil.DEVICE_INFO); // �ն��豸��
			prepayReqHandler.setParameter("fee_type", "CNY");
			prepayReqHandler.setParameter("mch_id", ConstantUtil.PARTNER); //�̻���    
			String noncestr = WXUtil.getNonceStr();
			prepayReqHandler.setParameter("nonce_str", noncestr);
			prepayReqHandler.setParameter("notify_url", notify_url); //���ղƸ�֪ͨͨ��URL  
			prepayReqHandler.setParameter("out_trade_no", out_trade_no); //�̼Ҷ�����   
			
//			packageReqHandler.setParameter("input_charset", "GBK"); //�ַ����
			
			String timestamp = WXUtil.getTimeStamp();
			String traceid = "";
			////���û�ȡprepayid֧������
//			prepayReqHandler.setParameter("appkey", ConstantUtil.APP_KEY);
			
//			prepayReqHandler.setParameter("package", packageValue);
//			prepayReqHandler.setParameter("timestamp", timestamp);
//			prepayReqHandler.setParameter("traceid", traceid);

			//��ɻ�ȡԤ֧��ǩ��
			prepayReqHandler.setParameter("spbill_create_ip",CommonUtils.getRealAddress(request)); //������ɵĻ���IP��ָ�û��������IP  
			prepayReqHandler.setParameter("total_fee", request.getAttribute("fee").toString()); //��Ʒ���,�Է�Ϊ��λ  
			prepayReqHandler.setParameter("trade_type", ConstantUtil.TRADE_TYPE); // ֧������
			
			
			String sign = prepayReqHandler.createMD5Sign();
			//���ӷǲ���ǩ��Ķ������
			prepayReqHandler.setParameter("sign", sign);
			
//			prepayReqHandler.setParameter("sign_method",
//					ConstantUtil.SIGN_METHOD);
//			String gateUrl = ConstantUtil.GATEURL + token;
//			prepayReqHandler.setGateUrl(gateUrl);
			try {
				prepayid = prepayReqHandler.sendPrepay();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//�»ظ�ͻ��˵Ĳ���
			if (null != prepayid && !"".equals(prepayid)) {
				//��������б�
				
				clientHandler.setParameter("appid", ConstantUtil.APP_ID);
//				clientHandler.setParameter("appkey", ConstantUtil.APP_KEY);
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

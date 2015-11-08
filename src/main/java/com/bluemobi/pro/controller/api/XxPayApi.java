package com.bluemobi.pro.controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pay.excute.PayRequest;
import com.bluemobi.pay.util.XMLUtil;
import com.bluemobi.pro.service.impl.XxShopServiceImpl;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;

@RequestMapping("/api/pay/")
@Controller
public class XxPayApi {

	@Autowired
	private XxShopServiceImpl iShopServiceImpl;

	// 获取预支付ID
	@RequestMapping(value = "weixin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> payWeixin(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {

		if (ParamUtils.existEmpty(params, "sn"))
			return ResultUtils.error(ErrorCode.ERROR_02);
		Double fee = 0.0;
		try {
			fee = iShopServiceImpl.countPrice(params.get("sn").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sn = params.get("sn").toString();
		request.setAttribute("sn", sn);
		request.setAttribute("fee", "1");
//		request.setAttribute("fee", fee != null ? fee.doubleValue() * 100 : 0.0); // 正确价格
		Map<String, Object> resultMap = PayRequest.pay(request, response);
		return ResultUtils.map2(resultMap);
	}

	// ----------------------------------------------------------------------------------
	// ------------------------------------通知方法-----------------------------------------
	// ----------------------------------------------------------------------------------
	@RequestMapping(value = "notifyAlipay", method = RequestMethod.POST)
	public void notifyAlipay(HttpServletRequest request) {
		try {
			iShopServiceImpl.toEntityByAlipay(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 微信支付回调地址
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "notifyWeixin", method = RequestMethod.POST)
	public void notifyWeixin(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
			String result = "";
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			System.out.println("result:" + result);
			Map<String,Object> resultMap = XMLUtil.doXMLParse(result);
			iShopServiceImpl.toEntityByWeixin(resultMap);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------------------------------------
	// -------------------------------测试-------------------------------------------------
	// -----------------------------------------------------------------------------------
	@RequestMapping(value = "testPayment", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> testPayment() {
		try {
			iShopServiceImpl.savePayment("20151020144056569");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtils.success();
	}
}

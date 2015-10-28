package com.bluemobi.pro.controller.api;

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
	public Map<String,Object> payWeixin(@RequestParam Map<String,Object> params,
			                           HttpServletRequest request, HttpServletResponse response) {
		
		if(ParamUtils.existEmpty(params, "sn")) return ResultUtils.error(ErrorCode.ERROR_02);
		
		String sn = params.get("sn").toString();
		request.setAttribute("sn", sn);
		request.setAttribute("fee", "1");
		Map<String,Object> resultMap = PayRequest.pay(request, response);
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
    @RequestMapping(value = "notifyWeixin", method = RequestMethod.POST)
    public void notifyWeixin(HttpServletRequest request) {
    	try {
			iShopServiceImpl.toEntityByWeixin(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    // -----------------------------------------------------------------------------------
    // -------------------------------测试-------------------------------------------------
    // -----------------------------------------------------------------------------------
    @RequestMapping(value = "testPayment", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> testPayment() {
    	try {
			iShopServiceImpl.savePayment("20151020144056569");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ResultUtils.success();
    }
}

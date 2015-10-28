package com.bluemobi.pro.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.cache.CacheService;
import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.XxMemberServiceImpl;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;

@RequestMapping("/api/member/manager/")
@Controller
public class XxMemberApi {

	@Autowired
	private XxMemberServiceImpl xxMemberServiceImpl;
	
    @Resource(name = "cacheTempCodeServiceImpl")
    private CacheService<String> cacheService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "allAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> findAllAddress(@RequestParam Map<String,Object> params) {
		List list = null;
		try {
			list = xxMemberServiceImpl.findAllAddress(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtils.list(list);
	}
	
	// 新增/更新 地址信息
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addOrEditAddress(@RequestParam Map<String,Object> params) {
		Object obj = params.get("addressId");
		String addressId = params.get("addressId") != null && !params.get("addressId").toString().equals("") ?  params.get("addressId").toString() : null;
		// 设置是否默认地址
		params.put("is_default", params.get("is_default") == null ? (byte)0 : Byte.parseByte(params.get("is_default").toString()));
		try {
			if (StringUtils.isNotBlank(addressId)) {
				edit(params);
			}
			else{
				add(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> removeAddress(@RequestParam Map<String,Object> params) {
		
		try {
			xxMemberServiceImpl.removeAddress(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}	
	
	@RequestMapping(value = "default", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectDefaultAddress(@RequestParam Map<String,Object> params) {
		
		try {
			xxMemberServiceImpl.selectDefaultAddres(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	//13247176223
	@RequestMapping(value = "validateMobile", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> validateMobileByMemberId(@RequestParam Map<String,Object> params) {
		
		int flag = 0;
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			if(xxMemberServiceImpl.validateMobileByMemberId(params)) {
				flag = 1;
			}
			else {
				resultMap.put("resultType", 0);
				return ResultUtils.map2(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String code = cacheService.get(params.get("mobile").toString());
		if(code != null && code.equals(params.get("content").toString())) {
			flag = 1;
		}
		else {
			flag = 0;
		}
		
		resultMap.put("resultType", flag);
		return ResultUtils.map2(resultMap);
	}
	
	// 修改手机号
	@RequestMapping(value = "modifyMobile", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> modityMobile(@RequestParam Map<String,Object> params) {
		
		if(ParamUtils.existEmpty(params, "memberId","mobile","content")) return ResultUtils.error(ErrorCode.ERROR_02);
		
		String code  = cacheService.get(params.get("mobile").toString());
		if(code == null || !params.get("content").toString().equals(code)) {
			return ResultUtils.error(ErrorCode.ERROR_10);
		}
		try {
			xxMemberServiceImpl.modifyMobile(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	@RequestMapping(value = "modifyOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> modifyOrder(@RequestParam Map<String,Object> params) {
		
		if(ParamUtils.existEmpty(params, "memberId","orderId")) return ResultUtils.error(ErrorCode.ERROR_02);
		
		try {
			xxMemberServiceImpl.modifyOrder(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	private void add(Map<String, Object> params) throws Exception {
		xxMemberServiceImpl.addAddress(params);
	}

	private void edit(Map<String, Object> params) throws Exception {
		xxMemberServiceImpl.editAddress(params);
	}
}

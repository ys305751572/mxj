package com.bluemobi.pro.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluemobi.sys.service.BaseService;
import com.bluemobi.utils.DateUtils;

@Service
public class XxMemberServiceImpl extends BaseService{
	
	private static final String PRIFIX = XxMemberServiceImpl.class.getName();
	
	@Transactional
	public void addAddress(Map<String,Object> params) throws Exception {
		params.put("create_date", DateUtils.getCurrentTime());
		params.put("modify_date", DateUtils.getCurrentTime());
		
		Integer isDefault = Integer.parseInt(params.get("is_default").toString());
		
		this.getBaseDao().save(PRIFIX + ".addAddress", params);
		params.put("addressId", params.get("id"));
		
		if(isDefault == 1){
			this.getBaseDao().update(PRIFIX + ".editAddressIsDefault", params);
			this.getBaseDao().update(PRIFIX + ".selectDefaultAddress", params);
		}
		this.getBaseDao().update(PRIFIX + ".editAddress", params);
	}
	
	public void editAddress(Map<String,Object> params) throws Exception {
		
		String is_default = params.get("is_default").toString();
		if("1".equals(is_default)){
			this.getBaseDao().update(PRIFIX + ".editAddressIsDefault", params);
			this.getBaseDao().update(PRIFIX + ".selectDefaultAddress", params);
		}
		params.put("is_default", Byte.parseByte(params.get("is_default").toString()));
		this.getBaseDao().update(PRIFIX + ".editAddress", params);
	}
	
	@SuppressWarnings("rawtypes")
	public List findAllAddress(Map<String,Object> params) throws Exception {
		List<Map<String,Object>> list = this.getBaseDao().getList(PRIFIX + ".selectAllAddress", params);
		return list == null ? Collections.emptyList() : list;
	}
	
	// 设置默认地址
	@Transactional
	public void selectDefaultAddres(Map<String,Object> params) throws Exception {
		this.getBaseDao().update(PRIFIX + ".selectDefaultAddress", params);
		this.getBaseDao().update(PRIFIX + ".updateReceiver", params);
	} 
	
	// 删除收货地址
	public void removeAddress(Map<String,Object> params) throws Exception {
		// 如果该地址是用户的默认地址，更新用户表address值
//		Map<String,Object> memberAddress = this.getBaseDao().get(PRIFIX + ".findMemberAddress", params);
//		if(memberAddress != null && memberAddress.get("delivery_address") != null) {
//			
//		}
		this.getBaseDao().delete(PRIFIX + ".deleteAddress", params);
	}
	
	public void modifyOrder(Map<String,Object> params) throws Exception{
		
		Object addressObj = params.get("addressId");
		Object expireObj = params.get("expire");
		Map<String,Object> addressMap = new HashMap<String, Object>();
		if(addressObj != null) {
			addressMap = this.getBaseDao().get(PRIFIX + ".findByAddressId", params);
		}
		if(expireObj != null) {
			addressMap.put("expire", expireObj);
		}
		addressMap.put("orderId", params.get("orderId"));
		this.getBaseDao().update("com.bluemobi.pro.service.impl.XxShopServiceImpl.motifyOrder", addressMap);
	}
	
	// 根据Id获取地址信息
	public Map<String,Object> findByAddressId(String addressId) throws Exception {
		return this.getBaseDao().get(PRIFIX + ".findByAddressId", addressId);
	}
	
	// 查询绑定手机是否正确
	public Boolean validateMobileByMemberId(Map<String,Object> params) throws Exception{
		
		String mobile = this.getBaseDao().get(PRIFIX + ".findMobileById", params);
		if(mobile != null && mobile.equals(params.get("mobile"))) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	} 
	
	// 修改手机号
	public void modifyMobile(Map<String,Object> params) throws Exception {
		this.getBaseDao().update(PRIFIX + ".modifyMobile", params);
	}
}

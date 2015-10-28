package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.UhoemErpDictionaryServiceImpl;
import com.bluemobi.pro.service.impl.UhoemErpMeasureHouseOrderServiceImpl;
import com.bluemobi.pro.service.impl.UhoemErpPlanServiceImpl;
import com.bluemobi.pro.service.impl.XxCommonsServiceImpl;
import com.bluemobi.pro.service.impl.XxShopServiceImpl;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 公共方法
 */
@Controller
@RequestMapping("/api/common")
public class CommonApi implements Serializable{
    @Autowired
    private UhoemErpMeasureHouseOrderServiceImpl uhoemErpMeasureHouseOrderServiceImpl;

    @Autowired
    private UhoemErpPlanServiceImpl uhoemErpPlanServiceImpl;

    @Autowired
    private UhoemErpDictionaryServiceImpl uhoemErpDictionaryServiceImpl;
    
    @Autowired
    private XxCommonsServiceImpl xxCommonsServiceImpl;

    @Autowired
    private XxShopServiceImpl iShopServiceImpl;
    
    /**
     * 查询房屋面积和户型（完成）
     * 参数：
     * member 用户id （必填）
     */
    @RequestMapping(value = "/houseInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> page(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "member")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            // 先找uhoem_erp_measure_house_order，如果没有就去找uhoem_erp_plan
            Map<String,Object> temp = uhoemErpMeasureHouseOrderServiceImpl.findByMember(params);
            if(temp == null || temp.size() == 0){
                temp = uhoemErpPlanServiceImpl.findMyPlan(params);
            }

            Map<String,Object> result = new HashMap<String,Object>();
            if(temp == null || temp.size() == 0){
                result.put("house_type",0);
                result.put("covered_area","");
            }else {
                result.put("house_type",temp.get("house_type"));
                result.put("covered_area",temp.get("covered_area"));
                Map<String,Object> dic = uhoemErpDictionaryServiceImpl.getByHourseType((String) temp.get("house_type"));
                if(dic != null){
                    result.put("DicName",dic.get("DicName"));
                }else {
                    result.put("DicName","");
                }
            }
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    // 根据区ID查询街道集合
    @RequestMapping(value = "/findStreet", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findStreet(@RequestParam Map<String,Object> params) {
    	List<Map<String,Object>> list = null;
    	try {
    		list = xxCommonsServiceImpl.findStreet(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
    	return ResultUtils.list(list);
    }
    
    // 首页图片
    @RequestMapping(value = "/indexImages", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> indexImages() {
    	List<Map<String,Object>> imageList = new ArrayList<Map<String,Object>>();
    	try {
    		imageList = xxCommonsServiceImpl.findIndexImages();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
    	return ResultUtils.list(imageList);
    }
    
    /**
     * 首页未读消息数据
     * @param params
     * @return
     */
    @RequestMapping(value = "indexMessageCount", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> indexMessageCount(@RequestParam Map<String,Object> params) {
    	
    	if(ParamUtils.existEmpty(params, "memberId")) return ResultUtils.error(ErrorCode.ERROR_02);
    	int count = 0;
    	Map<String,Object> respMap = new HashMap<String,Object>();
    	try {
    		count = xxCommonsServiceImpl.indexMessageCount(params);
    		respMap.put("count", count);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
    	return ResultUtils.map2(respMap);
    }
    
    // ====================================================
    // 待定暂未删除
    // ====================================================
    
    // 支付宝回调地址

}

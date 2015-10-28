package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.*;
import com.bluemobi.sys.page.Page;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鎴戠殑鏂规
 */
@Controller
@RequestMapping("/api/uhoemErpPlan")
public class UhoemErpPlanApi {
    @Autowired
    private UhoemErpPlanServiceImpl uhoemErpPlanServiceImpl;

    @Autowired
    private UhoemErpPlanProductServiceImpl uhoemErpPlanProductServiceImpl;

    @Autowired
    private XxProductCategoryServiceImpl xxProductCategoryServiceImpl;

    @Autowired
    private XxDecorGroupProductServiceImpl xxDecorGroupProductServiceImpl;

    /**
     * 淇濆瓨鏂规锛堝紑鍙戯級
     * 濡傛灉涓嶅瓨鍦ㄥ氨鏂板锛屽鏋滃瓨鍦ㄥ氨淇敼
     * 鍙傛暟锛�
     * house_type 鎴垮眿绫诲瀷锛堝繀濉級锛堝幓鎺夛級
     * price 鍩虹纭鐨勫崟浠凤紙蹇呭～锛夛紙鍘绘帀锛�
     * covered_area 鎴垮眿闈㈢Н锛堝繀濉級锛堝幓鎺夛級
     * covered_price 鍩虹纭鐨勫崟浠稾鎴峰瀷闈㈢Н璁＄畻鐨勭粨鏋滀环鏍硷紙蹇呭～锛夛紙鍘绘帀锛�
     * total_price 鏁翠綋鏂规鐨勬�讳环鏍硷紙蹇呭～锛夛紙鍘绘帀锛�
     * member 鐢ㄦ埛id 锛堝繀濉級
     * product_items 鍟嗗搧淇℃伅 鍟嗗搧id@鏁伴噺锛屽晢鍝乮d@鏁伴噺锛堝繀濉級
     */
    @RequestMapping(value="/save",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> page(@RequestParam Map<String, Object> params) {
        try{
            if(ParamUtils.existEmpty(params, "member", "product_items")){
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            // 鎶婁腑闂寸殑杩炵画,,鏇挎崲鎴�,鎶婃渶鍓嶉潰鍜屾渶鍚庨潰鐨�,鍘绘帀
            if(StringUtils.isNotEmpty((String)params.get("product_items"))){
                String product_items = (String)params.get("product_items");
                product_items = product_items.replace(",,",",");
                String first = product_items.substring(0,1);
                if(",".equals(first)){
                    product_items = product_items.substring(1, product_items.length());
                }
                String end = product_items.substring(product_items.length()-1);
                if(",".equals(end)){
                    product_items = product_items.substring(0,product_items.length()-1);
                }
                params.put("product_items",product_items);
            }

            uhoemErpPlanServiceImpl.saveOrMofify(params);

            return ResultUtils.success();
        }catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 淇敼鏂规涓殑鎴峰瀷鍜岄潰绉紙寮�鍙戯級
     * 鍙傛暟锛�
     * house_type 鎴垮眿绫诲瀷锛堝繀濉級
     * covered_area 鎴垮眿闈㈢Н锛堝繀濉級
     * member 鐢ㄦ埛id 锛堝繀濉級
     */
    @RequestMapping(value="/modifyHourseTypeAndCoveredArea",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyHourseTypeAndCoveredArea(@RequestParam Map<String, Object> params) {
        try{
            if(ParamUtils.existEmpty(params, "member", "house_type", "covered_area")){
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            uhoemErpPlanServiceImpl.iModifyHourseTypeAndCoveredArea(params);

            return ResultUtils.success();
        }catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 鎴戠殑鏂规锛堝畬鎴愶級
     * 鍙傛暟锛�
     * member 鐢ㄦ埛id锛堝繀濉級
     */
    @RequestMapping(value="/myPlan",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> myPlan(@RequestParam Map<String, Object> params) {
        try{
            if(ParamUtils.existEmpty(params, "member")){
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            Map<String, Object> result = new HashMap<String, Object>();
            // 鏂规
            Map<String, Object> plan = uhoemErpPlanServiceImpl.findMyPlan(params);
            if(plan==null || plan.size()==0){ // 娌℃湁鏂规
                plan = new HashMap<String, Object>();
                result.put("plan",plan);
            }else { // 鏈夋柟妗�
                result.put("plan",plan);
                result = getPlanProduct(result, (Long) plan.get("id")+"");
            }

            return ResultUtils.parse(result,"myPlan");
        }catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 鍏朵粬鏂规锛堝畬鎴愶級
     * 鍙傛暟锛�
     * member 鐢ㄦ埛id锛堝繀濉級
     */
    @RequestMapping(value="/otherPlan",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> otherPlan(@RequestParam Map<String, Object> params) {
        try{
            if(ParamUtils.existEmpty(params, "member")){
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            Map<String, Object> result = new HashMap<String, Object>();
            // 鏂规
            Map<String, Object> plan = uhoemErpPlanServiceImpl.findOtherPlanByMember(params);
            if(plan==null || plan.size()==0){ // 娌℃湁鏂规
                plan = new HashMap<String, Object>();
                result.put("plan",plan);
            }else { // 鏈夋柟妗�
                result.put("plan",plan);
                result = getPlanProduct(result, (Long) plan.get("id")+"");
            }

            return ResultUtils.parse(result,"otherPlan");
        }catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    // 鍘绘煡璇㈡柟妗堜腑鐨勪骇鍝侊紝鎶婄粨鏋滃瓨鍏ュ弬鏁癿ap锛屼富瑕佹湇鍔′簬myPlan,otherPlan
    public Map<String, Object> getPlanProduct(Map<String, Object> result,String planid) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("planid",planid);
        // 杞鍗曞搧
        param.put("product_main_category",3);
        List<Map<String,Object>> alone = uhoemErpPlanProductServiceImpl.findByPlanAndProductMainCategory(param);
        // 杞缁勫悎
        param.put("product_main_category",4);
        List<Map<String,Object>> group = uhoemErpPlanProductServiceImpl.findByPlanAndProductMainCategory(param);
        // 杞缁勫悎閲岄潰鐨勫晢鍝�
        for (int i = 0; i < group.size(); i++) {
            Map<String,Object> product = group.get(i);
            product.put("product",product.get("id"));
            List<Map<String,Object>> productList = xxDecorGroupProductServiceImpl.list(product);
            group.get(i).put("productList",productList);
        }
        // 涓�х┖闂�
        param.put("product_main_category",5);
        List<Map<String,Object>> space = uhoemErpPlanProductServiceImpl.findByPlanAndProductMainCategory(param);

        result.put("alone",alone);
        result.put("group",group);
        result.put("space",space);
        return result;
    }










}

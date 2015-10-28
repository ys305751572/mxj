package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UhoemErpPlanServiceImpl extends BaseService {
    public static String PREFIX = UhoemErpPlanServiceImpl.class.getName();

    @Autowired
    private UhoemMemberServiceImpl uhoemMemberServiceImpl;

    @Autowired
    private UhoemErpSystemconfigurationServiceImpl uhoemErpSystemconfigurationServiceImpl;

    @Autowired
    private XxProductServiceImpl xxProductServiceImpl;

    @Autowired
    private UhoemErpPlanProductServiceImpl uhoemErpPlanProductServiceImpl;

    @Transactional
    public void saveOrMofify(Map<String, Object> params) throws Exception {
        Map<String, Object> temp = findMyPlan(params);
        if (temp == null || temp.size() == 0) { // 新增
            save(params);
        } else { // 修改
            params.put("planid", temp.get("id"));
            modifyPlan(params);
        }
    }

    // 保存我的方案
    public void save(Map<String, Object> params) throws Exception {
        // 方案名
        Map<String, Object> member = new HashMap<String, Object>();
        member.put("id", params.get("member"));
        member = uhoemMemberServiceImpl.getMemberInfo(member);
        String name = (String) member.get("name");
        if (name == null) {
            name = "";
        }
        String plan_name = name + "的方案";

        // 户型house_type 面积covered_area
        Double covered_area = Double.valueOf(0);
        Map<String, Object> myplan = findMyPlan(params);
        if (myplan != null && myplan.size() > 0) {
            params.put("house_type", myplan.get("house_type"));
            params.put("covered_area", myplan.get("covered_area"));
            covered_area = Double.valueOf((String) myplan.get("covered_area"));
        } else {
            params.put("house_type", 0);
            params.put("covered_area", 0);
        }

        // 基础硬装价格
        Map<String, Object> sysCon = uhoemErpSystemconfigurationServiceImpl.basicYzPrice(params);
        Double basicYzPrice = Double.valueOf((String) sysCon.get("basic_yz_price"));

        // 基础总价covered_price
        Double covered_price = basicYzPrice * covered_area;

        // 方案总结total_price，每个商品的价格的和与covered_price相加
        Double total_price = Double.valueOf(0) + covered_price;
        String product_items1 = (String) params.get("product_items");
        String[] productArr1 = product_items1.split(",");
        for (int i = 0; i < productArr1.length; i++) {
            String[] temp = productArr1[i].split("@");
            Map<String, Object> product = new HashMap<String, Object>();
            product.put("id", temp[0]);
            product = xxProductServiceImpl.getById(product);
            Double quantity = Double.valueOf(temp[1]);
            Double price = Double.valueOf((String) product.get("price"));
            total_price = total_price + quantity * price;
        }

        // 保存uhoem_erp_plan
        params.put("plan_name", plan_name);
        params.put("covered_area", covered_area);
        params.put("price", basicYzPrice);
        params.put("covered_price", covered_price);
        params.put("total_price", total_price);
        params.put("erp_user", 0);
        params.put("is_member_create", 1);
        params.put("create_date", new Date());
        params.put("modify_date", new Date());
        Map<String, Object> plan = (Map<String, Object>) this.getBaseDao().saveBackId(PREFIX + ".insert", params);

        // 保存xx_decor_group_product
        String product_items = (String) params.get("product_items");
        String[] productArr = product_items.split(",");
        for (int i = 0; i < productArr.length; i++) {
            String[] temp = productArr[i].split("@");
            Map<String, Object> plan_product = new HashMap<String, Object>();
            plan_product.put("planid", plan.get("id"));
            plan_product.put("product", temp[0]);
            plan_product.put("quantity", temp[1]);
            plan_product.put("is_apply", 0);
            plan_product.put("create_user", params.get("member"));
            plan_product.put("create_date", new Date());
            plan_product.put("modify_user", params.get("member"));
            plan_product.put("modify_date", new Date());
            this.getBaseDao().save(UhoemErpPlanProductServiceImpl.class.getName() + ".insert", plan_product);
        }
    }

    // 修改我的方案
    public void modifyPlan(Map<String, Object> params) throws Exception {
        // 面积covered_area
        Double covered_area = Double.valueOf(0);
        Map<String, Object> myPlan = findMyPlan(params);
        if (myPlan != null && myPlan.size() > 0) {
            covered_area = Double.valueOf((String) myPlan.get("covered_area"));
        }

        // 基础硬装价格
        Map<String, Object> sysCon = uhoemErpSystemconfigurationServiceImpl.basicYzPrice(params);
        Double basicYzPrice = Double.valueOf((String) sysCon.get("basic_yz_price"));

        // 基础总价covered_price
        Double covered_price = basicYzPrice * covered_area;

        // 方案总结total_price，每个商品的价格的和
        Double total_price = Double.valueOf(0) + covered_price;
        String product_items1 = (String) params.get("product_items");
        String[] productArr1 = product_items1.split(",");
        for (int i = 0; i < productArr1.length; i++) {
            String[] temp = productArr1[i].split("@");
            Map<String, Object> product = new HashMap<String, Object>();
            product.put("id", temp[0]);
            product = xxProductServiceImpl.getById(product);
            Double quantity = Double.valueOf(temp[1]);
            Double price = Double.valueOf((String) product.get("price"));
            total_price = total_price + quantity * price;
        }

        // 保存uhoem_erp_plan
        params.put("price", basicYzPrice);
        params.put("covered_price", covered_price);
        params.put("total_price", total_price);
        params.put("modify_date", new Date());
        params.put("id", params.get("planid"));
        this.getBaseDao().update(PREFIX + ".update", params);

        // 保存xx_decor_group_product
        uhoemErpPlanProductServiceImpl.deleteByPlanid(params);
        String product_items = (String) params.get("product_items");
        String[] productArr = product_items.split(",");
        for (int i = 0; i < productArr.length; i++) {
            String[] temp = productArr[i].split("@");
            Map<String, Object> plan_product = new HashMap<String, Object>();
            plan_product.put("planid", params.get("planid"));
            plan_product.put("product", temp[0]);
            plan_product.put("quantity", temp[1]);
            plan_product.put("is_apply", 0);
            plan_product.put("create_user", params.get("member"));
            plan_product.put("create_date", new Date());
            plan_product.put("modify_user", params.get("member"));
            plan_product.put("modify_date", new Date());
            this.getBaseDao().save(UhoemErpPlanProductServiceImpl.class.getName() + ".insert", plan_product);
        }
    }

    // 修改方案的户型和面积
    @Transactional
    public void iModifyHourseTypeAndCoveredArea(Map<String, Object> params) throws Exception {
        Map<String, Object> plan = findMyPlan(params);
        // 先缓存上一次的基础硬装总价和总价
        Double total_price = Double.parseDouble(plan.get("total_price") + "");
        Double old_covered_price = Double.parseDouble(plan.get("covered_price") + "");

        // 户型house_type 面积covered_area
        Double covered_area = Double.valueOf((String) params.get("covered_area"));

        // 基础硬装价格
        Map<String, Object> sysCon = uhoemErpSystemconfigurationServiceImpl.basicYzPrice(params);
        Double basicYzPrice = Double.valueOf((String) sysCon.get("basic_yz_price"));

        // 基础总价covered_price
        Double covered_price = basicYzPrice * covered_area;

        // 保存uhoem_erp_plan
        plan.put("covered_area", covered_area);
        plan.put("house_type", params.get("house_type"));
        plan.put("price", basicYzPrice);
        plan.put("covered_price", covered_price);
        plan.put("modify_date", new Date());
        plan.put("total_price", total_price - old_covered_price + covered_price);
        this.getBaseDao().update(PREFIX + ".update", plan);
    }

    public Map<String, Object> findMyPlan(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".findMyPlan", params);
    }

    public Map<String, Object> findOtherPlanByMember(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".findOtherPlan", params);
    }


}
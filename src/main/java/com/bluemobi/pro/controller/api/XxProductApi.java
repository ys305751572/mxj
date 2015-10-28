package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.UhoemMemberServiceImpl;
import com.bluemobi.pro.service.impl.XxProductServiceImpl;
import com.bluemobi.sys.page.Page;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 商品
 */
@Controller
@RequestMapping("/api/xxProduct")
public class XxProductApi {
    @Autowired
    private XxProductServiceImpl xxProductServiceImpl;

    /**
     * 商品查询（开发中）
     * 参数：
     * product_category 商品分类 （非必填）
     * keywords 商品大分类 （非必填）
     * memberid 用户id （非必填）
     * price 价格区间 （非必填） 0-999 1000-2999 3000-4999 5000-9999 10000以上，传入1/2/3/4/5
     * 排序 （非必填） 最新上架（默认最新上架）、价格最低、人气最高 传入1/2/3
     * pageNum （必填）
     * pageSize （必填）
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> page(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "pageNum", "pageSize")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            Page result = xxProductServiceImpl.page(params, Integer.parseInt((String) params.get("pageNum")), Integer.parseInt((String) params.get("pageSize")));
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 热卖商品（完成）
     * 参数：
     * memberid 用户id （非必填）
     * keywords 商品大分类 （非必填）
     * pageNum （必填）
     * pageSize （必填）
     */
    @RequestMapping(value = "/pageHot", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> pageHot(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "pageNum", "pageSize")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            Page result = xxProductServiceImpl.pageHot(params, Integer.parseInt((String) params.get("pageNum")), Integer.parseInt((String) params.get("pageSize")));
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 同类推荐（完成）
     * 按当前展示商品分类的同类商品来查询，且标签有交集，排除当前商品id，按收藏数倒序展示6条信息
     * 参数：
     * productid 商品大分类 （必填）
     * memberid 用户id （非必填）
     */
    @RequestMapping(value = "/sameKind", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sameKind(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "productid")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            Page result = xxProductServiceImpl.sameKind(params, 1, 6);
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 商品详情（完成）
     * 参数：
     * productid 商品id （必填）
     * memberid 用户id （必填）
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> detail(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "productid")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            Map<String, Object> result = xxProductServiceImpl.iDetail(params);
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }


}

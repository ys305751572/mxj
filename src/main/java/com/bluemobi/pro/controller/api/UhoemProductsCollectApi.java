package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.UhoemProductsCollectServiceImpl;
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
 * 商品收藏
 */
@Controller
@RequestMapping("/api/uhoemProductsCollect")
public class UhoemProductsCollectApi {
    @Autowired
    private UhoemProductsCollectServiceImpl uhoemProductsCollectServiceImpl;

    /**
     * 我的方案（查询我收藏的商品）--完成
     * 参数：
     * memberid 用户id
     * product_category 收藏商品的类型
     * pageNum （必填）
     * pageSize（必填）
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> page(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "memberid", "pageNum", "pageSize")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            Page result = uhoemProductsCollectServiceImpl.page(params, Integer.parseInt((String) params.get("pageNum")), Integer.parseInt((String) params.get("pageSize")));
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 收藏商品--完成
     * 请求参数：
     * memberid 用户id （必填）
     * productid 商品id（非必填）
     */
    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> collect(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "memberid", "productid")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            uhoemProductsCollectServiceImpl.collect(params);
            return ResultUtils.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 取消收藏--完成
     * 参数：
     * memberid 用户id
     * productid 商品id
     * @return
     */
    @RequestMapping(value = "/cancelCollect", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> cancelCollect(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "memberid", "productid")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            uhoemProductsCollectServiceImpl.cancelCollect(params);
            return ResultUtils.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }


}

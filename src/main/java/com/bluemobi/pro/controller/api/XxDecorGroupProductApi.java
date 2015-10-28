package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.XxDecorGroupProductServiceImpl;
import com.bluemobi.pro.service.impl.XxProductCategoryServiceImpl;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 软装组合商品
 */
@Controller
@RequestMapping("/api/xxDecorGroupProduct")
public class XxDecorGroupProductApi {
    @Autowired
    private XxDecorGroupProductServiceImpl xxDecorGroupProductServiceImpl;

    /**
     * 查询软装组合的组合商品
     * 参数：
     * product 软装组合id（必填）
     */
    @RequestMapping(value="/list",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> list(@RequestParam Map<String, Object> params) {
        try{
            if (ParamUtils.existEmpty(params, "product")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            List<Map<String, Object>> result = xxDecorGroupProductServiceImpl.list(params);
            return ResultUtils.parse(result);
        }catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }







}

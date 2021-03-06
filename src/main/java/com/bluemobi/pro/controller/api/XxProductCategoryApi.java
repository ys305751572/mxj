package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
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
 * 商品分类
 */
@Controller
@RequestMapping("/api/xxProductCategory")
public class XxProductCategoryApi {
    @Autowired
    private XxProductCategoryServiceImpl xxProductCategoryServiceImpl;

    /**
     * 查询分类（完成）
     * 参数：
     * parent:上级分类id（必填）
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> list(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "parent")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            List<Map<String, Object>> result = xxProductCategoryServiceImpl.list(params);
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }


}

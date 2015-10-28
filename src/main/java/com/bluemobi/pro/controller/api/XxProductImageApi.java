package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.XxProductImageServiceImpl;
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
 * 商品图片
 */
@Controller
@RequestMapping("/api/xxProductImage")
public class XxProductImageApi {
    @Autowired
    private XxProductImageServiceImpl xxProductImageServiceImpl;

    /**
     * 查询商品图片（完成）
     * 参数：
     * product 商品id （必填）
     */
    @RequestMapping(value = "/listByProduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listByProduct(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "product")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            List<Map<String, Object>> result = xxProductImageServiceImpl.findProductImage(params);
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }


}

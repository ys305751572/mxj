package com.bluemobi.pro.controller.api;

import com.bluemobi.pro.service.impl.UhoemErpSystemconfigurationServiceImpl;
import com.bluemobi.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 系统配置
 */
@Controller
@RequestMapping("/api/uhoemErpSystemconfiguration")
public class UhoemErpSystemconfigurationApi {
    @Autowired
    private UhoemErpSystemconfigurationServiceImpl uhoemErpSystemconfigurationServiceImpl;

    /**
     * 基础硬装价格
     * 参数：
     * 无
     */
    @RequestMapping(value = "/basicYzPrice", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> basicYzPrice(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = uhoemErpSystemconfigurationServiceImpl.basicYzPrice(params);
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }


}

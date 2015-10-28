package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.UhoemErpDictionaryServiceImpl;
import com.bluemobi.pro.service.impl.UhoemErpMeasureHouseOrderServiceImpl;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;
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
 * 字典
 */
@Controller
@RequestMapping("/api/uhoemErpDictionary")
public class UhoemErpDictionaryApi {
    @Autowired
    private UhoemErpDictionaryServiceImpl uhoemErpDictionaryServiceImpl;

    /**
     * 房屋类型（完成）
     * 参数：
     * 无
     */
    @RequestMapping(value = "/houseType", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> houseType(@RequestParam Map<String, Object> params) {
        try {
            List<Map<String, Object>> result = uhoemErpDictionaryServiceImpl.houseType(params);
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }


}

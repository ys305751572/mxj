package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.UhoemMessagesServiceImpl;
import com.bluemobi.sys.page.Page;
import com.bluemobi.utils.DateUtils;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通知
 */
@Controller
@RequestMapping("/api/uhoemMessages")
public class UhoemMessagesApi {
    @Autowired
    private UhoemMessagesServiceImpl uhoemMessagesServiceImpl;

    /**
     * 通知（完成）
     * 参数：
     * memberid （必填）
     * pageNum（必填）
     * pageSize（必填）
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> page(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "pageNum", "pageSize","memberid")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            Page<List<Map<String, Object>>> result = uhoemMessagesServiceImpl.page(params, Integer.parseInt((String) params.get("pageNum")), Integer.parseInt((String) params.get("pageSize")));
            Collection<List<Map<String, Object>>> collection = result.getRows();
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                Map<String, Object> map = (Map<String, Object>) it.next();

                // 时间格式化
                Long templong = 0L;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String temp = (String) map.get("create_time");
                    templong = sdf.parse(temp).getTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.put("create_time", DateUtils.time(templong));
                map.put("title", map.get("title"));
                map.put("content", map.get("content"));
                map.put("id", map.get("id"));
            }
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 读取消息
     * @param params
     * @return
     */
    @RequestMapping(value = "/readMessages", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> readNews(@RequestParam Map<String,Object> params) {
    	try {
    		uhoemMessagesServiceImpl.read(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
    	return ResultUtils.success();
    }
}

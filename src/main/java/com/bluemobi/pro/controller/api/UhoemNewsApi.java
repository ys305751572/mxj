package com.bluemobi.pro.controller.api;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.UhoemNewsServiceImpl;
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
@RequestMapping("/api/uhoemNews")
public class UhoemNewsApi {
    @Autowired
    private UhoemNewsServiceImpl uhoemNewsServiceImpl;

    /**
     * 查询
     * 参数：
     * 无
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> page(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "pageNum", "pageSize")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            Page result = uhoemNewsServiceImpl.page(params, Integer.parseInt((String) params.get("pageNum")), Integer.parseInt((String) params.get("pageSize")));
            Collection<List<Map<String, Object>>> collection = result.getRows();
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                Map<String, Object> map = (Map<String, Object>) it.next();

                // 时间格式化
                Long templong = 0L;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String temp = (String) map.get("create_time");
                    templong = sdf.parse(temp).getTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.put("create_time", DateUtils.time(templong));
                map.put("title", map.get("title").toString());
                map.put("content", map.get("content").toString());
                map.put("id", map.get("id").toString());
            }
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }

    /**
     * 读取通知
     * @param params
     * @return
     */
    @RequestMapping(value = "/readNews", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> readNews(@RequestParam Map<String,Object> params) {
    	try {
			uhoemNewsServiceImpl.read(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
    	return ResultUtils.success();
    }
}

package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class XxProductHitsServiceImpl extends BaseService {
    public static String PREFIX = XxProductHitsServiceImpl.class.getName();

    /**
     * 新增商品点击记录
     * 请求参数：
     *  product 商品ID
     *  his_source 设备标识
     *  member 会员ID
     * @return 受影响的行数
     * @throws Exception
     */
    public int iAddHitsInfo(Map<String, Object> params) throws Exception {
        int flag = 0;
        params.put("create_date", new Date());
        flag = this.getBaseDao().save(PREFIX + ".iAddHitsInfo", params);
        return flag;
    }
}
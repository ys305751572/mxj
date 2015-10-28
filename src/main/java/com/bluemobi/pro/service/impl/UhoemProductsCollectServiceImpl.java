package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UhoemProductsCollectServiceImpl extends BaseService {
    public static String PREFIX = UhoemProductsCollectServiceImpl.class.getName();

    @Autowired
    private XxProductServiceImpl xxProductServiceImpl;

    public Page<List<Map<String, Object>>> page(Map<String, Object> params, int pageNum,
            int pageSize) throws Exception {
        return this.getBaseDao().page(PREFIX + ".page", params, pageNum, pageSize);
    }

    // 收藏
    // 如果数据不存在，则进行收藏，如果存在则进行修改
    @Transactional
    public void collect(Map<String, Object> params) throws Exception {
        Map<String,Object> map = this.getBaseDao().get(PREFIX + ".findByMemberidAndProductid",params);

        if(map == null){
            params.put("status",1);
            params.put("creater",params.get("memberid"));
            params.put("create_time",new Date());
            this.getBaseDao().save(PREFIX + ".insert", params);

            // 商品收藏量+1
            Map<String,Object> product = new HashMap<>();
            product.put("id",params.get("productid"));
            xxProductServiceImpl.productFavoriteCountAddOne(product);
        }else {
            if(!"1".equals((String)map.get("status"))){
                map.put("status",1);
                map.put("modifier",params.get("memberid"));
                map.put("modify_time",new Date());
                this.getBaseDao().save(PREFIX + ".update", map);

                // 商品收藏量+1
                Map<String,Object> product = new HashMap<>();
                product.put("id",params.get("productid"));
                xxProductServiceImpl.productFavoriteCountAddOne(product);
            }
        }
    }

    // 取消收藏
    @Transactional
    public void cancelCollect(Map<String, Object> params) throws Exception {
        Map<String,Object> map = this.getBaseDao().get(PREFIX + ".findByMemberidAndProductid", params);
        if(map!=null && !"2".equals((String)map.get("status"))){
            Map<String,Object> product = new HashMap<>();
            product.put("id",params.get("productid"));
            xxProductServiceImpl.productFavoriteCountCutOne(product);

            map.put("status",2);
            map.put("modifier",params.get("memberid"));
            map.put("modify_time",new Date());
            this.getBaseDao().save(PREFIX + ".update", map);
        }
    }


}
package com.bluemobi.pro.controller.api;

import com.bluemobi.pro.service.impl.UhoemErpDictionaryServiceImpl;
import com.bluemobi.pro.service.impl.UhoemPictureServiceImpl;
import com.bluemobi.pro.service.impl.XxProductServiceImpl;
import com.bluemobi.utils.CommonUtils;
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
 * 图片
 */
@Controller
@RequestMapping("/api/uhoemPicture")
public class UhoemPictureApi {
    @Autowired
    private UhoemPictureServiceImpl uhoemPictureServiceImpl;

    @Autowired
    private XxProductServiceImpl xxProductServiceImpl;

    /**
     * 首页广告（完成）
     * 参数：
     * 无
     */
    @RequestMapping(value = "/homepageAd", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> houseType(@RequestParam Map<String, Object> params) {
        try {
            params = new HashMap<String, Object>();
            params.put("pic_no","007");
            List<Map<String, Object>> result = uhoemPictureServiceImpl.homepageAd(params);
            for (int i = 0; i < result.size(); i++) {
                Map<String,Object> map = result.get(i);

                // 截取出商品id
                String link_url = (String) map.get("link_url");
                String productid = CommonUtils.getProductid(link_url);
                map.put("productid",productid);

                long id = 0;
                String name = "";
                String seo_description = "";
                String image = "";
                String price = "";
                String favorite_count = "";
                if(link_url.contains("product/detail")){ // 表示有商品id
                    Map<String,Object> product = new HashMap<>();
                    product.put("id",productid);
                    product = xxProductServiceImpl.findOneById(product);
                    if(product!=null && product.size()>0){
                        id = (Long) product.get("id");
                        name = (String) product.get("name");
                        seo_description = (String) product.get("name");
                        image = (String) product.get("seo_description");
                        price = (String) product.get("image");
                        favorite_count = (String) product.get("price");
                    }
                }
                map.put("id",id);
                map.put("name",name);
                map.put("seo_description",seo_description);
                map.put("image",image);
                map.put("price",price);
                map.put("favorite_count",favorite_count);
            }
            return ResultUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error();
        }
    }


}

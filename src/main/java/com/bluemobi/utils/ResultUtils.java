package com.bluemobi.utils;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.sys.page.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultUtils {

    public static Map<String, Object> parse(Object o,String ... name) {
        if(o instanceof Page){
            return page((Page)o);
        }else if(o instanceof List){
            return list((List)o);
        }else if(o instanceof String){
            return string((String) o);
        }else if(o instanceof Map){
            return map((Map<String,Object>) o,name);
        }
        else {
            return other(o);
        }
    }

    /**
     * page
     */
    public static Map<String, Object> page(Page page) {
        Map<String, Object> retmap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
            Map<String, Object> pagemap = new HashMap<String, Object>();
            pagemap.put("totalNum", page.getTotal());
            pagemap.put("totalPage", page.getPageCount());
            pagemap.put("currentPage", page.getCurrent());
            retmap.put("status", "0");
            data.put("page", pagemap);
            data.put("list", page.getRows());
            retmap.put("msg", "");
        retmap.put("data", data);
        return retmap;
    }
    
    /**
     * 返回不包含page信息的list
     */
    public static Map<String, Object> list(List<Map<String, Object>> list) {
        Map<String, Object> retmap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
            retmap.put("status", "0");
            data.put("list", list);
            retmap.put("msg", "");
        retmap.put("data", data);
        return retmap;
    }

    /**
     * 返回不包含page信息的list
     */
    public static Map<String, Object> listObject(List list) {
        Map<String, Object> retmap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
            retmap.put("status", "0");
            data.put("list", list);
            retmap.put("msg", "");
        retmap.put("data", data);
        return retmap;
    }

    
    /**
     * 返回对象
     */
    public static Map<String, Object> map(Map<String,Object> map,String ... name) {
    	
    	if(map == null) map = new HashMap<String,Object>();
    	
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        if(name.length == 0){
            data.put("object",map);
        }else {
            data.put(name[0],map);
        }
      
        returnMap.put("status", "0");
        returnMap.put("msg", "");
        returnMap.put("data", data);
        return returnMap;
    }
    
    /**
     * 返回对象
     */
    public static Map<String, Object> map2(Map<String,Object> map,String ... name) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        if(name.length != 0) {
            data.put(name[0],map);
        }
        else {
        	data = map;
        }
        returnMap.put("status", "0");
        returnMap.put("msg", "");
        returnMap.put("data", data);
        return returnMap;
    }

    /**
     * 返回msg
     */
    public static Map<String, Object> string(String str) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("status", "0");
        returnMap.put("msg", str);
        returnMap.put("data", new HashMap<String, Object>());
        return returnMap;
    }

    /**
     * 返回msg
     */
    public static Map<String, Object> object(Object object) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("status", "0");
        returnMap.put("msg", "");
        returnMap.put("data", object);
        return returnMap;
    }

    /**
     * 其他信息
     */
    public static Map<String, Object> other(Object t) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("status", "0");
        returnMap.put("msg", "");
        returnMap.put("data", t);
        return returnMap;
    }

    public static Map<String, Object> error(String ... msg) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("status", "1");
        if(msg.length == 0){
            returnMap.put("msg", ErrorCode.ERROR_01);
        }else {
            returnMap.put("msg", msg[0]);
        }
        returnMap.put("data", new HashMap<String, Object>());
        return returnMap;
    }

    public static Map<String, Object> success() {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("status", "0");
        returnMap.put("msg", "");
        returnMap.put("data", new HashMap<String, Object>());
        return returnMap;
    }



}

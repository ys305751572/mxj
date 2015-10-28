package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UhoemMemberServiceImpl extends BaseService {
    public static String PREFIX = UhoemMemberServiceImpl.class.getName();

    public Map<String, Object> login(Map<String, Object> params) throws Exception {
        Map<String, Object> map = this.getBaseDao().get(PREFIX + ".login", params);

        if (null == map || map.size() == 0) {
            return null;
        }

        // 更新登录时间
        map.put("login_date", new Date());
        this.getBaseDao().update(PREFIX + ".iUpdateTime", map);
        return map;
    }

    public Page<List<Map<String, Object>>> list(Map<String, Object> params, int current, int pagesize) {
        return this.getBaseDao().page(PREFIX + ".find", params, current, pagesize);
    }

    public Map<String, Object> iLoginByOpenId(Map<String, Object> params) throws Exception {
        Map<String, Object> map = this.getBaseDao().get(PREFIX + ".iLoginByOpenId", params);

        if (null == map || map.size() == 0) {
            return null;
        }
        // 更新登录时间
        map.put("login_date", new Date());
        this.getBaseDao().update(PREFIX + ".iUpdateTime", map);
        return map;
    }

    public Boolean iCheckMobile(Map<String, Object> params) throws Exception {
        Map<String, Object> map = this.getBaseDao().get(PREFIX + ".login", params);
        if (null == map || map.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public int iRegister(Map<String, Object> params) throws Exception {
        params.put("create_date", new Date());
        params.put("username", params.get("mobile"));
        return this.getBaseDao().save(PREFIX + ".iRegister", params);
    }

    public int iUpdateInfo(Map<String, Object> params) throws Exception {
        // 更新修改时间
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", params.get("memberid"));
        map.put("modify_date", new Date());
        this.getBaseDao().update(PREFIX + ".iUpdateTime", map);

        return this.getBaseDao().update(PREFIX + ".iUpdateInfo", params);
    }

    public int iUpdatePassword(Map<String, Object> params) throws Exception {
        return this.getBaseDao().update(PREFIX + ".iUpdatePassword", params);
    }

    public Map<String, Object> iGetMemberInfo(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".iGetMemberInfo", params);
    }

    public String iGetMemberNameByName(Map<String,Object> params) throws Exception {
    	return this.getBaseDao().getObject(PREFIX + ".iGetMemberNameByName", params);
    }
    
    public Map<String, Object> iGetMemberInfoByMobile(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".iGetMemberInfoByMobile", params);
    }

    public Map<String, Object> iGetMemberInfoByUserName(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".iGetMemberInfoByUserName", params);
    }

    public int iUpdateTime(Map<String, Object> params) throws Exception {
        return this.getBaseDao().update(PREFIX + ".iUpdateTime", params);
    }

    // 获取用户信息 by gaolei
    public Map<String, Object> getMemberInfo(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".getMemberInfo", params);
    }

    // 将用户ID跟第三方OpenId绑定
    public int bindingOpenId(Map<String, Object> params) throws Exception {
        return this.getBaseDao().save(PREFIX + ".bindingOpenId", params);
    }

    // 通过用户ID查询所有已绑定的第三方openId
    public List<Map<String, Object>> iFindOpenIdByMemberId(Map<String, Object> params) throws Exception {
        return this.getBaseDao().getList(PREFIX + ".iFindOpenIdByMemberId", params);
    }

    // 根据用户ID和第三方OpenId获取绑定信息
    public Map<String, Object> iCheckBinding(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".iCheckBinding", params);
    }
}
package com.bluemobi.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.service.BaseService;
import com.bluemobi.utils.MD5;
/**
 * @Description 本地登陆用户管理serviceImpl
 * @date 2014-12-25
 * @author 涂毅恒
 */
@Service
public class SysuserServiceImpl extends BaseService {
    public static String PREFIX = SysuserServiceImpl.class.getName();
    /**
     * @Description 执行登陆操作
     * @param
     * @return
     * @date 2014-12-25 17:59:51
     * @author 龙哲
     */
    public Map<String, Object> getLogin(Map<String, Object> params) throws Exception {
        params.put("password", MD5.md5((String) params.get("password")));
        return this.getBaseDao().getObject(PREFIX + ".getLogin", params);
    }
    /**
     * @Description 分页查询后台用户
     * @return Page<E>
     * @date 2015-01-12 10:13:08
     * @author lvjunjun
     */
    public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
        return this.getBaseDao().page(PREFIX + ".page", params, current, pagesize);
    }
    /**
     * @Description 添加后台帐号
     * @return int
     * @date 2015-01-12 11:02:08
     * @author lvjunjun
     */
    public int save(Map<String, Object> params) throws Exception {
        //验证用户编号是否重复
        Page<List<Map<String, Object>>> page =  this.getBaseDao().page(PREFIX+".checkcode", params, 1, 10);
        if(page.getTotal()>0){
            return -1;
        }else{
            // 请注意：是否需要设置默认值 如创建时间等
            params.put("updateTime", System.currentTimeMillis());
            params.put("password", MD5.md5("888888"));
            // 保存
            return this.getBaseDao().save(PREFIX + ".insert", params);
        }
    }
    /**
     * @Description 根据Id查询后台帐号
     * @return Map<K, V>
     * @date 2015-1-12 11:16:08
     * @author lvjj
     */
    public Map<String, Object> findOne(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".findOne", params);
    }
    /**
     * @Description 修改后台用户
     * @return int
     * @date 2015-01-12 11:37:08
     * @author lvjj
     */
    public int update(Map<String, Object> params) throws Exception {
        if(params.get("password")!=null && params.get("password").toString().trim().length() != 0){
            params.put("password", MD5.md5(params.get("password").toString()));
        }else{
            params.put("updateTime", System.currentTimeMillis());
        }
        // 修改
        return this.getBaseDao().update(PREFIX + ".update", params);
    }

    /**
     * @Description 修改密码
     * @return int
     * @date 2015-01-12 11:37:08
     * @author lvjj
     */
    public int updatepassword(Map<String, Object> params) throws Exception {
        params.put("oldpassword", MD5.md5(params.get("oldpassword").toString()));
        Page<List<Map<String, Object>>> page = this.getBaseDao().page(PREFIX+".checkpassword", params, 0, 1);
        //验证密码是否正确
        if(page.getTotal()>0){
            // 修改
            params.put("password", MD5.md5(params.get("password").toString()));
            return this.getBaseDao().update(PREFIX + ".update", params);
        }else{
            return -1;
        }
    }
}

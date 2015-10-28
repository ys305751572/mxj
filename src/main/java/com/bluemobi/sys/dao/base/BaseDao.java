package com.bluemobi.sys.dao.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.page.PageContainer;
import com.bluemobi.utils.SessionUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

/**
 * 
 * <p>Title: BaseDao.java</p> 
 * <p>Description: dao基类</p> 
 * @author yesong 
 * @date 2014-11-5 下午05:13:30
 * @version V1.0 
 * ------------------------------------
 * 历史版本
 *
 */
@Repository
public class BaseDao extends SqlSessionDaoSupport{
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	
	public int save(String key, Object obj) throws Exception {
		if(obj != null && obj instanceof Map){
			Map<String, Object> entity = (Map<String, Object>) obj;
			entity.put("createTime", System.currentTimeMillis());
			
			Map<String, Object> user = SessionUtils.getCurrentUser();
			if(user != null){
				entity.put("createUser", String.valueOf(user.get("id")));
			}
			return this.getSqlSession().insert(key, entity);
		}
		return this.getSqlSession().insert(key, obj);
	}
	public Object saveBackId(String key, Object obj) throws Exception {
        if(obj != null && obj instanceof Map){
            Map<String, Object> entity = (Map<String, Object>) obj;
            entity.put("createTime",System.currentTimeMillis());
            
            Map<String, Object> user = SessionUtils.getCurrentUser();
            if(user != null){
                entity.put("createUser",String.valueOf(user.get("id")));
            }
              this.getSqlSession().insert(key, entity);
        }
        return obj;
    }
	public int update(String key, Object obj) throws Exception{
		if(obj != null && obj instanceof Map){
			Map<String, Object> entity = (Map<String, Object>) obj;
			return this.getSqlSession().update(key, entity);
		}
		return this.getSqlSession().update(key, obj);
	}
	
	public int delete(String key, Serializable obj) throws Exception {
		return this.getSqlSession().delete(key, obj);
	}
	
	public int delete(String key, Object obj) throws Exception {
		return this.getSqlSession().delete(key, obj);
	}
	
	@SuppressWarnings("unchecked") 
	public <T> T get(String key, Object param) throws Exception {
		return (T) this.getSqlSession().selectOne(key, param);
	}
	

	public <T> T getObject(String key, Object param) throws Exception{
		List<T> objList = this.getSqlSession().selectList(key, param);
		if(objList==null || objList.size()==0)
			return null;
		return objList.get(0);
		
	}
	
	public <T> List<T> getList(String key) throws Exception {
		return this.getSqlSession().selectList(key);
	}
	
	public <T> List<T> getList(String key, Object param) throws Exception {
		return this.getSqlSession().selectList(key, param);
	}
	
	@SuppressWarnings("unchecked")
	public <E, K, V> Page<E> page(String pageStatement, Map<K, V> parameter, int current, int pagesize) {
		PageBounds pageBounds = new PageBounds(current, pagesize);
		PageList<E> result = (PageList<E>) this.getSqlSession().selectList(pageStatement, parameter, pageBounds);
		Paginator paginator = result.getPaginator();
		return new PageContainer<E, K, V>(paginator.getPage(),paginator.getTotalCount(), paginator.getTotalPages(), result);
	}

	
}

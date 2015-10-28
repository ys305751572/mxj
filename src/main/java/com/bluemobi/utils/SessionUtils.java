package com.bluemobi.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bluemobi.constant.Constant;

/**
 * Session 操作辅助类
 * 
 * @author maew
 * 
 */
public class SessionUtils {

	public static HttpSession getSession(boolean arg0) {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
		return request.getSession(arg0);
	}

	/**
	 * 将对象存入session
	 */
	public static void put(String key, Object obj) {
		getSession(true).setAttribute(key, obj);
	}

	/**
	 * 从session取对象
	 */
	public static Object get(String key) {
		HttpSession session = getSession(false);
		if (session != null) {
			return session.getAttribute(key);
		} else {
			return null;
		}
	}

	/**
	 * 检查Session是否存在指定Key值的对象
	 */
	public static boolean contains(String key) {
		return (get(key) != null);
	}

	/**
	 * 清除指定session对象
	 */
	public static void remove(String key) {
		HttpSession session = getSession(false);
		if (session != null) {
			session.removeAttribute(key);
		}
	}

	/**
	 * 清除所有存放在session的对象
	 */
	public static void clear() {
		HttpSession session = getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	/**
	 * 获取当前登录人
	 */
	public static Map<String, Object> getCurrentUser() {
		return (Map<String, Object>) get(Constant.CURRENT_USER);
	}
}

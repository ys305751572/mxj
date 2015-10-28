package com.bluemobi.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookiesUtils {

    /**
     * 设置cookie（接口方法）
     * 
     * @param response
     * @param name
     *            cookie名字
     * @param value
     *            cookie值
     * @param maxAge
     *            cookie生命周期 以秒为单位
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0)
            cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 将cookie封装到Map里面（非接口方法）
     * 
     * @param request
     * @return 返回之后走自动登陆流程
     */
    public static Map<String, Object> ReadCookieMap(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                String attname = cookie.getName();
                if (attname.equals("userName")) {
                    params.put("userName", cookie.getValue());
                }
                else if (attname.equals("userPassword")) {
                    params.put("userPassword", cookie.getValue());
                }
            }
        }
        return params;
    }
    
    /**
     * 主动退出删除cookie
     * @return
     * @author liu_shi_lin
     * @Date 2014-10-24 下午4:19:32
     */
    public static void logoutCookie(HttpServletResponse response) {
    	CookiesUtils.addCookie(response, "userName", null, 0);
	    CookiesUtils.addCookie(response, "userPassword", null, 0);
    }

}
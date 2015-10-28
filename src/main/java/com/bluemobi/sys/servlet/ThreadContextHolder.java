package com.bluemobi.sys.servlet;

import javax.servlet.http.HttpServletRequest;

public class ThreadContextHolder {

	private static ThreadLocal<HttpServletRequest> requestMap = new ThreadLocal<HttpServletRequest>();
	
	public static void setHttpServletRequest(HttpServletRequest request) {
		requestMap.set(request);
	}
	
	public static HttpServletRequest getHttpServletRequest() {
		return requestMap.get();
	}
}

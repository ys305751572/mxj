package com.bluemobi.sys.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * <p>Title: InitServlet.java</p> 
 * <p>Description: 初始化加载数据到DataCache</p> 
 * @author yesong 
 * @date 2014-11-6 上午09:51:50
 * @version V1.0 
 * ------------------------------------
 * 历史版本
 *
 */
public class InitServlet extends HttpServlet{

	private static final long serialVersionUID = 2110499739711866579L;
	protected final Log logger = LogFactory.getLog(getClass());
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.info("初始化开始.................");
		//TODO 加载需要初始化加载的数据 加入代码时请标明作者、时间、描述
		
		
		logger.info("初始化结束.................");
	}
}

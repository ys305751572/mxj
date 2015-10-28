package com.bluemobi.sys.page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 * 分页器接口，根据page,pageSize,total用于页面上分页显示多项内容，计算页码和当前页的偏移量，方便页面分页使用.
 * 
 */
public interface Page<E> {
	/**
	 * 当前页码
	 */
	public long getCurrent();
	
	/**
	 * 取总记录数
	 */
	public long getTotal();
	
	/**
	 * 取总页数.
	 */
	public int getPageCount();

	/**
	 * 得到该页的数据
	 */
	public Collection<E> getRows();

	void setTotal(long totalnum);
	
	void setRow(List<Map<String, Object>> list);

	void setTotalPage(int page);
}

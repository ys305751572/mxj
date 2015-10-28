package com.bluemobi.sys.page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PageContainer<E, K, V> implements Page<E> {

	/** 当前页数 */
	private long current = 0;
	
	/** 记录的总数 */
	private long totalnum = 0;

	/** 记录的总页数 */
	private int totalpage = 0;

	/** 记录集合 */
	private Collection<E> info;

	public PageContainer(long current,long totalnum, int totalpage, Collection<E> info) {
		this.current = current;
		this.totalnum = totalnum;
		this.totalpage = totalpage;
		this.info = info;
	}
	
	@Override
	public long getCurrent() {
		return current;
	}

	@Override
	public long getTotal() {
		return totalnum;
	}
	
	@Override
	public void setTotal(long totalnum) {
		this.totalnum = totalnum;
	}

	@Override
	public int getPageCount() {
		return totalpage;
	}
	
	@Override
	public Collection<E> getRows() {
		return info;
	}

	@Override
	public void setRow(List<Map<String, Object>> list) {
		this.info = (Collection<E>) list;
	}

	@Override
	public void setTotalPage(int totalCount) {
		this.totalpage = totalCount;
	}
}

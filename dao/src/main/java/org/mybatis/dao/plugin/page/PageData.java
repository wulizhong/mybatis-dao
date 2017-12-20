package org.mybatis.dao.plugin.page;

import java.util.List;

public class PageData<T> {
	public static int DEFAULT_PAGE_SIZE = 20;
	
	private int pageNumber;
	private int pageSize;
	private long totalCount = -1;
	
	private List<T> data;
	
	private boolean selectCount;
	
	
	
	protected boolean isSelectCount() {
		return selectCount;
	}
	protected void setSelectCount(boolean selectCount) {
		this.selectCount = selectCount;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	
}

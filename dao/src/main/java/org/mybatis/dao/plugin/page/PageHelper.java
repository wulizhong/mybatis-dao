package org.mybatis.dao.plugin.page;

public class PageHelper {

	private static ThreadLocal<PageData<?>> local = new ThreadLocal<PageData<?>>();
	
	public static <T> PageData<T> select(Select select,int start,int size,boolean selectCount){	
		if(select == null){
			throw new NullPointerException("Select is null");
		}
		PageData<T> pageData = new PageData<T>();
		pageData.setPageNumber(start);
		pageData.setPageSize(size);
		pageData.setSelectCount(selectCount);
		local.set(pageData);
		select.doSelect();
		local.remove();
		return pageData;
	}
	public static <T> PageData<T> select(Select select,int start,int size){	
		return select(select,start,size,false);
	}
	
	public static <T> PageData<T> selectWithCount(Select select,int start,int size){	
		return select(select,start,size,true);
	}
	
	protected static ThreadLocal<PageData<?>> getThreadLocal() {
		return local;
	}
	
}

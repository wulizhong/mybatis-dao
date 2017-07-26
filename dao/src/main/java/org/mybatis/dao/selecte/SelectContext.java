package org.mybatis.dao.selecte;

import org.mybatis.dao.Condation;
import org.mybatis.dao.DaoConfig;
import org.mybatis.dao.DaoContext;
import org.mybatis.dao.mapper.DaoMapper;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 下午5:11:52 
 * 
 */
public class SelectContext extends DaoContext{

	private Class<?> type;
	
	public SelectContext(Class<?> type, DaoConfig daoConfig, DaoMapper daoMapper, Condation condation) {
		super(daoConfig, daoMapper, condation);
		// TODO Auto-generated constructor stub
		this.type = type;
	}

//	private Page page;
//
//	public Page getPage() {
//		return page;
//	}
//
//	public void setPage(Page page) {
//		this.page = page;
//	}
	
	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

}

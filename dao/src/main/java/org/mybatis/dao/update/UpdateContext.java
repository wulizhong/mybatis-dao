package org.mybatis.dao.update;

import org.mybatis.dao.Condation;
import org.mybatis.dao.DaoConfig;
import org.mybatis.dao.DaoContext;
import org.mybatis.dao.mapper.DaoMapper;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月24日 上午11:33:54 
 * 
 */
public class UpdateContext extends DaoContext{

	public UpdateContext(Object target,DaoConfig daoConfig,DaoMapper daoMapper,Condation condation){
		super(daoConfig, daoMapper,condation);
		this.target = target;
	}
	
	public UpdateContext(Object target, DaoConfig daoConfig, DaoMapper daoMapper) {
		super(daoConfig, daoMapper);
		// TODO Auto-generated constructor stub
		this.target = target;
	}
	
	private Object target;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

}

package org.mybatis.dao;

import org.mybatis.dao.impl.DefaultObjectCreator;
import org.mybatis.dao.mapper.DaoMapper;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 下午4:25:55 
 * 
 */
public class DaoContext {

	
	
	private DaoConfig daoConfig;
	
	private DaoMapper daoMapper;
	
	private Condation condation;
	
	
	
	private ObjectCreator objectCreator = new DefaultObjectCreator();
	
	
	public DaoContext(DaoConfig daoConfig,DaoMapper daoMapper,Condation condation){
		this.daoConfig = daoConfig;
		this.daoMapper = daoMapper;
		this.condation = condation;
	}
	
	public DaoContext(DaoConfig daoConfig,DaoMapper daoMapper){
		
		this.daoConfig = daoConfig;
		this.daoMapper = daoMapper;
	}

	
	

	public DaoConfig getDaoConfig() {
		return daoConfig;
	}

	public void setDaoConfig(DaoConfig daoConfig) {
		this.daoConfig = daoConfig;
	}

	public DaoMapper getDaoMapper() {
		return daoMapper;
	}

	public void setDaoMapper(DaoMapper daoMapper) {
		this.daoMapper = daoMapper;
	}

	public Condation getCondation() {
		return condation;
	}

	public void setCondation(Condation condation) {
		this.condation = condation;
	}

	public ObjectCreator getObjectCreator() {
		return objectCreator;
	}

	public void setObjectCreator(ObjectCreator objectCreator) {
		this.objectCreator = objectCreator;
	}
	
}

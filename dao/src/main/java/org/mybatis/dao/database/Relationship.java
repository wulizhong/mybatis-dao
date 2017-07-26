package org.mybatis.dao.database;

import org.mybatis.dao.FetchType;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月18日 上午9:29:35 
 * 
 */
public class Relationship {
	
	private FetchType fetchType = FetchType.LAZY;

	public FetchType getFetchType() {
		return fetchType;
	}

	public void setFetchType(FetchType fetchType) {
		this.fetchType = fetchType;
	}
	
}

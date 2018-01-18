package org.mybatis.dao.condation;

import java.util.Map;

import org.mybatis.dao.Condation;

public class RawSql implements Condation{

	private String sql;
	
	public RawSql(String sql){
		this.sql = sql;
	}
	
	@Override
	public String toSql(Class<?> clazz, Map<String, Object> paramter) {
		// TODO Auto-generated method stub
		return this.sql;
	}

}

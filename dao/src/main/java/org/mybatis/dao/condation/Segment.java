package org.mybatis.dao.condation;

import java.util.Map;

import org.mybatis.dao.Condation;

public class Segment implements Condation{

	private Condation condation;
	
	protected Segment(Condation condation) {
		this.condation = condation;
	}
	
	@Override
	public String toSql(Class<?> clazz, Map<String, Object> paramter) {
		// TODO Auto-generated method stub
		return condation.toSql(clazz, paramter);
	}

}

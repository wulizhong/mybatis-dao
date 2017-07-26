package org.mybatis.dao;

import java.util.Map;

/**
 */
public class SqlProvider {
	
	public String select(Map<String,Object> paramter) {
		Object sql = paramter.remove(Constant.SQL_SYMBOL);
		return sql.toString();
	}
	
	public String insertUseReturnGeneratedKeys(Map<String,Object> paramter) {
		Object sql = paramter.remove(Constant.SQL_SYMBOL);
		return sql.toString();
	}
	
	public String insert(Map<String,Object> paramter) {
		Object sql = paramter.remove(Constant.SQL_SYMBOL);
		return sql.toString();
	}
	
	public String update(Map<String,Object> paramter) {
		Object sql = paramter.remove(Constant.SQL_SYMBOL);
		return sql.toString();
	}
	
	public String delete(Map<String,Object> paramter) {
		Object sql = paramter.remove(Constant.SQL_SYMBOL);
		return sql.toString();
	}
}

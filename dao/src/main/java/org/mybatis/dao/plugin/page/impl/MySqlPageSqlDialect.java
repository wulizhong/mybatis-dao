package org.mybatis.dao.plugin.page.impl;

import org.mybatis.dao.plugin.page.PageSqlDialect;

public class MySqlPageSqlDialect implements PageSqlDialect{

	@Override
	public String getPageSql(String sourceSql, int start, int size) {
		// TODO Auto-generated method stub
		String sql = sourceSql+" limit "+start+" , "+size;
		return sql;
	}

	@Override
	public String getCountSql(String sourceSql) {
		// TODO Auto-generated method stub
		
		String sql = "select count(0) from ("+sourceSql+") as temp";
		
		return sql;
	}

}

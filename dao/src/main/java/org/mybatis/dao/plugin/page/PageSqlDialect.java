package org.mybatis.dao.plugin.page;

public interface PageSqlDialect {

	public String getPageSql(String sourceSql,int start,int size);
	
	public String getCountSql(String sourceSql);
}

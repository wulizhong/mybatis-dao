package org.mybatis.dao.plugin.page;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.MappedStatement;
import org.mybatis.dao.plugin.page.impl.MySqlPageSqlDialect;

public class PageSqlDialectFactory {

	private static Map<String, Class<?>> dialectMap = new HashMap<>();

	static {
		dialectMap.put("mysql", MySqlPageSqlDialect.class);
	}

	private static PageSqlDialect pageSqlDialect;

	public static PageSqlDialect createPageSqlDialect(MappedStatement ms) {

		if (pageSqlDialect != null) {
			return pageSqlDialect;
		}

		synchronized (PageSqlDialectFactory.class) {
			DataSource dataSource = ms.getConfiguration().getEnvironment().getDataSource();
			Connection conn = null;
			String url = "";
			try {
				conn = dataSource.getConnection();
				url = conn.getMetaData().getURL();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String dialect : dialectMap.keySet()) {
				if (url.indexOf(":" + dialect + ":") != -1) {
					try {
						pageSqlDialect = (PageSqlDialect) dialectMap.get(dialect).newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		return pageSqlDialect;
	}
}

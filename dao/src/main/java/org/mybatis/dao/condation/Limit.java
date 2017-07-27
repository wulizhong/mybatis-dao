package org.mybatis.dao.condation;

import java.util.Map;

import org.mybatis.dao.Condation;
import org.mybatis.dao.Constant;
import org.mybatis.dao.DataBase;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月19日 下午6:33:27
 * 
 */
public class Limit implements Condation {

	private Condation condation;

	private int start;
	private int end;
	private DataBase dataBase;

	public Limit(Condation condation, DataBase dataBase, int start, int end) {
		this.condation = condation;
		this.dataBase = dataBase;
		this.start = start;
		this.end = end;
	}

	@Override
	public String toSql(Class<?> clazz, Map<String, Object> paramter) {
		// TODO Auto-generated method stub
		if (dataBase == DataBase.MYSQL) {
			StringBuilder builder = new StringBuilder();
			builder.append(condation.toSql(clazz, paramter));
			builder.append(" limit ");
			builder.append("#{start} , #{end} ");
			paramter.put("start", start);
			paramter.put("end", end);
			return builder.toString();
		} else if (dataBase == DataBase.ORACLE) {

			StringBuilder builder = new StringBuilder();
			builder.append(" SELECT * ");
			builder.append(" FROM (SELECT a.*, ROWNUM rn ");
			builder.append("FROM ( ");
			builder.append(Constant.ORACLE_SQL_SYMBOL);
			builder.append(" ");
			builder.append(condation.toSql(clazz, paramter));
			builder.append(" ) a ");
			builder.append("WHERE ROWNUM <= #{end})");
			builder.append("		 WHERE rn >= #{start}");
			paramter.put("start", start);
			paramter.put("end", end);
			return builder.toString();

			

		}
		return "";
	}

}

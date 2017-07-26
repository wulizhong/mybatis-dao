package org.mybatis.dao.condation;

import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.util.StringUtils;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月13日 上午10:13:26
 * 
 */
public class SqlGenerator {

	private TableMap tableMap = TableMap.getInstance();

	protected SqlGenerator() {
	}

	public String selectList(Class<?> clazz, String conditionSql) {
		Table table = tableMap.getTableMap(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ");
		sql.append(table.getName());
		if(!StringUtils.isEmpty(conditionSql)){
			sql.append(conditionSql);
		}
		return sql.toString();
	}

	public <T> String insert(T t){
//		Table table = tableMap.getTableMap(t.getClass());
//		
//		StringBuilder sql = new StringBuilder();
//		
//		sql.append("insert into ");
//		sql.append(table.getName());
//		sql.append("(");
//		
//		StringBuilder valueSql = new StringBuilder();
//		String[] dataBaseFields = table.getDataBaseFields();
//		for(int i = 0;i<dataBaseFields.length;i++){
//			
//			Field field = table.getField(dataBaseFields[i]);
//			Object value = null;
//			try {
//				field.setAccessible(true);
//				value = field.get(t);
//				field.setAccessible(false);
//			} catch (IllegalArgumentException | IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if(value == null)
//				continue;
//			
//			if(value instanceof String||value instanceof Date){
//				valueSql.append("'");
//				valueSql.append(value.toString());
//				valueSql.append("'");
//			}else{
//				valueSql.append(value.toString());
//			}
//			
//			sql.append(dataBaseFields[i]);
//			if(i != dataBaseFields.length-1){
//				sql.append(",");
//				valueSql.append(",");
//			}
//
//		}
//		sql.append(")");
//		sql.append("values");
//		sql.append("(");
//		sql.append(valueSql);
//		sql.append(")");
//		return sql.toString();
		return null;
	}

}

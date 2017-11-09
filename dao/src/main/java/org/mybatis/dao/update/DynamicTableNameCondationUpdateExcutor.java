package org.mybatis.dao.update;

import java.util.HashMap;

import org.apache.ibatis.jdbc.SQL;
import org.mybatis.dao.Constant;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2017年11月09日 下午03:33:23 
 * 
 */
public class DynamicTableNameCondationUpdateExcutor extends UpdateExcutor{

	private Class<?> clazz;
	
	private String[] fields;
	
	private Object[] values;
	
	private String tableName;
	
	public DynamicTableNameCondationUpdateExcutor(Class<?> clazz,String tableName,String[] fields,Object[] values){
		this.clazz = clazz;
		this.fields = fields;
		this.values = values;
		this.tableName = tableName;
	}
	
	public int update(UpdateContext context){
		int count = -1;
		SQL sql = new SQL();
		sql.UPDATE(getTableName(context));
		HashMap<String, Object> paramter = new HashMap<>();
		for (int i = 0;i<fields.length;i++) {
			
			
			sql = sql.SET(fields[i]+ " = #{" + fields[i] + "}");
			paramter.put(fields[i], values[i]);
		}
		paramter.put(Constant.SQL_SYMBOL, sql.toString()+context.getCondation().toSql(clazz, paramter));
		
		count = context.getDaoMapper().update(paramter);
		
		return count;
	}
	
	protected String getTableName(UpdateContext context) {
		return tableName;
	}
}

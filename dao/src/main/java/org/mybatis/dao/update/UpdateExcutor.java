package org.mybatis.dao.update;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.ibatis.jdbc.SQL;
import org.mybatis.dao.Constant;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.util.ReflectionUtils;
import org.mybatis.dao.util.Toolkit;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月24日 上午11:33:28 
 * 
 */
public class UpdateExcutor {

	public int update(UpdateContext context){
		int count = -1;
		
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		SQL sql = new SQL();
		
		sql.UPDATE(getTableName(context));
		HashMap<String, Object> paramter = new HashMap<>();
		for (String fieldStr : table.getDataBaseFieldMap().values()) {
			Field field = table.getField(fieldStr);
			if (field == table.getId().getField())
				continue;
			Object value = ReflectionUtils.getValue(target, field);
			if (value == null)
				continue;
			sql = sql.SET(fieldStr+ " = #{" + field.getName() + "}");
			paramter.put(field.getName(), value);
		}
		paramter.put(Constant.SQL_SYMBOL, sql.toString()+context.getCondation().toSql(targetType, paramter));
		
		count = context.getDaoMapper().update(paramter);
		
		return count;
	}
	
	protected String getTableName(UpdateContext context) {
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		return table.getName();
	}
}

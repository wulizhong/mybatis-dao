package org.mybatis.dao.insert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.mybatis.dao.Constant;
import org.mybatis.dao.DataBase;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Id;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.util.ReflectionUtils;
import org.mybatis.dao.util.Toolkit;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月20日 下午2:13:21
 * 
 */
public class InsertExcutor {

	public int insert(InsertContext context) {
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		SQL sql = new SQL();
		sql = sql.INSERT_INTO(table.getName());
		HashMap<String, Object> paramter = new HashMap<>();
		for (String fieldStr : table.getDataBaseFieldMap().values()) {
			Field field = table.getField(fieldStr);
			if (field == table.getId().getField())
				continue;
			Object value = ReflectionUtils.getValue(target, field);
			if (value == null)
				continue;
			sql = sql.VALUES(fieldStr, "#{" + field.getName() + "}");
			paramter.put(field.getName(), value);
		}
		paramter.put(Constant.SQL_SYMBOL, sql.toString());
		
		Id id = table.getId();
		int count = -1;
		DataBase dataBase = context.getDaoConfig().getDataBase();
		if(id.isAutoGenerateId() && (dataBase == DataBase.MYSQL||dataBase == DataBase.SQL_SERVER)){
			Map<String, Object> result = context.getDaoMapper().insertUseReturnGeneratedKeys(paramter);
			Field idField = id.getField();
			Long resultId = (Long) result.get("id");
			if (resultId != null) {
				Class<?> type = (Class<?>) idField.getGenericType();
				if (type == int.class || type == Integer.class) {
					ReflectionUtils.setValue(target, idField, resultId.intValue());
				} else if (type == long.class || type == Long.class) {
					ReflectionUtils.setValue(target, idField, resultId);
				}
			}
			
			if (result.get("count") != null) {
				count = (int) result.get("count");
			}
		}else{
			count = context.getDaoMapper().insert(paramter);
		}
		
		return count;
	}
}

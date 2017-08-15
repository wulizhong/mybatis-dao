package org.mybatis.dao.delete;

import java.util.HashMap;

import org.apache.ibatis.jdbc.SQL;
import org.mybatis.dao.Constant;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.util.Toolkit;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月24日 下午3:02:56 
 * 
 */
public class DeleteExcutor {

	public int delete(DeleteContext context){
		int count = -1;
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		SQL sql = new SQL();
		sql.DELETE_FROM(getTableName(context));
		HashMap<String, Object> paramter = new HashMap<>();
		paramter.put(Constant.SQL_SYMBOL, context.getCondation() == null?sql.toString():sql.toString()+context.getCondation().toSql(targetType, paramter));
		count = context.getDaoMapper().delete(paramter);
		return count;
	}
	protected String getTableName(DeleteContext context) {
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		return table.getName();
	}
}

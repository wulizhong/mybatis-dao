package org.mybatis.dao.selecte;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.dao.Constant;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.util.Toolkit;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 下午4:43:12 
 * 
 */
public class SelectExcutor {

	public <T> List<T> select(SelectContext context){
		HashMap<String, Object> paramter = new HashMap<String, Object>();
		Table table = TableMap.getInstance().getTableMap(context.getType());
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ");
		sql.append(table.getName());
		if (context.getCondation() != null)
			sql.append(context.getCondation().toSql(context.getType(), paramter));
		paramter.put(Constant.SQL_SYMBOL, sql.toString());
		List<Map<String, Object>> mapResultList = context.getDaoMapper().select(paramter);
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) Toolkit.convertMapToObjectList(context.getType(), mapResultList, context.getObjectCreator());
		return result;
	}
	
	
}

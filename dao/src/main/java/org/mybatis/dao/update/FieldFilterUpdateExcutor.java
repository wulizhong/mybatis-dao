package org.mybatis.dao.update;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.mybatis.dao.Constant;
import org.mybatis.dao.FieldFilter;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.util.CollectionUtils;
import org.mybatis.dao.util.ReflectionUtils;
import org.mybatis.dao.util.Toolkit;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2017年7月28日 上午11:06:41 
 * 
 */
public class FieldFilterUpdateExcutor extends UpdateExcutor{

	private FieldFilter fieldFilter;
	
	private UpdateExcutor excutor;
	
	public FieldFilterUpdateExcutor(FieldFilter fieldFilter,UpdateExcutor excutor){
		this.fieldFilter = fieldFilter;
		this.excutor = excutor;
	}
	
	@Override
	public int update(UpdateContext context) {
		// TODO Auto-generated method stub
		if(fieldFilter==null){
			return excutor.update(context);
		}
		
		if(CollectionUtils.isEmpty(fieldFilter.getExcludeFields())&&CollectionUtils.isEmpty(fieldFilter.getIncludeFields())){
			return excutor.update(context);
		}
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		SQL sql = new SQL();
		sql.UPDATE(getTableName(context));
		
		HashMap<String, Object> paramter = new HashMap<>();
		
		if(CollectionUtils.isEmpty(fieldFilter.getExcludeFields())){
			List<String > includeFields = fieldFilter.getIncludeFields();
			for(int i = 0;i<includeFields.size();i++){
				Field field = table.getFieldByOriginalFieldName(includeFields.get(i))==null?table.getField(includeFields.get(i)):table.getFieldByOriginalFieldName(includeFields.get(i));
				if(field!=null){
					sql = sql.SET(table.getDataBaseField(includeFields.get(i))+ " = #{" + field.getName() + "}");
					Object value = ReflectionUtils.getValue(target, field);
					paramter.put(field.getName(), value);
				}
			}
		}else{
			List<String > excludeFields = fieldFilter.getExcludeFields();
			
			for(String cloumn : table.getDataBaseFieldMap().values()){
				
				if(cloumn.equals(table.getId().getId())){
					continue;
				}
				
				boolean exclude = false;
				for(String excludeField:excludeFields){
					if(cloumn.equals(table.getDataBaseField(excludeField))){
						exclude = true;
						break;
					}
				}
				if(!exclude){
					Field field = table.getField(cloumn);
					
					sql = sql.SET(cloumn + " = #{" + field.getName() + "}");
					Object value = ReflectionUtils.getValue(target, field);
					paramter.put(field.getName(), value);
				}
				
			}
			
			
		}
		

		paramter.put(Constant.SQL_SYMBOL, sql.toString()+context.getCondation().toSql(targetType, paramter));
		
		int count = context.getDaoMapper().update(paramter);
		
		
		return count;
	}

	@Override
	protected String getTableName(UpdateContext context) {
		// TODO Auto-generated method stub
		return excutor.getTableName(context);
	}

	
}

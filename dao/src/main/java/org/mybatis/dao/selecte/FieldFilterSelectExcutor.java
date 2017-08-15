package org.mybatis.dao.selecte;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.dao.Constant;
import org.mybatis.dao.DataBase;
import org.mybatis.dao.FieldFilter;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.condation.Limit;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.util.CollectionUtils;
import org.mybatis.dao.util.Toolkit;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 下午4:54:21 
 * 
 */
public class FieldFilterSelectExcutor extends SelectExcutor{

	private FieldFilter fieldFilter;
	private SelectExcutor selectExcutor;
	
	public FieldFilterSelectExcutor(FieldFilter fieldFilter,SelectExcutor selectExcutor){
		this.fieldFilter = fieldFilter;
		this.selectExcutor = selectExcutor;
	}
	
	public <T> List<T> select(SelectContext context){
		
		HashMap<String, Object> paramter = new HashMap<String, Object>();
		
		Table table = TableMap.getInstance().getTableMap(context.getType());
		
		StringBuilder sql = new StringBuilder();
		if(CollectionUtils.isEmpty(fieldFilter.getExcludeFields())&&CollectionUtils.isEmpty(fieldFilter.getIncludeFields())){
			
			sql.append("select * from ");
			
		}else{
			sql.append("select ");
			
			if(CollectionUtils.isEmpty(fieldFilter.getExcludeFields())){
				sql.append(table.getId().getId());
				sql.append(", ");
				List<String > includeFields = fieldFilter.getIncludeFields();
				for(int i = 0;i<includeFields.size();i++){
					if(table.getFieldByOriginalFieldName(includeFields.get(i))!=null||table.getField(includeFields.get(i))!=null){
						sql.append(table.getDataBaseField(includeFields.get(i)));
						sql.append(" , ");
					}
				}
				String temp = sql.substring(0,sql.length()-3);
				sql = new StringBuilder(temp);
			}else{
				List<String > excludeFields = fieldFilter.getExcludeFields();
				
				for(String cloumn : table.getDataBaseFieldMap().values()){
					boolean exclude = false;
					for(String excludeField:excludeFields){
						if(cloumn.equals(table.getDataBaseField(excludeField))){
							exclude = true;
							break;
						}
					}
					if(!exclude){
						sql.append(table.getDataBaseField(cloumn));
						sql.append(" , ");
					}
					
				}
				String temp = sql.substring(0,sql.length()-3);
				sql = new StringBuilder();
				sql.append(temp);
				
			}
			sql.append(" from ");
		}
		
		sql.append(getTableName(context));
		if (context.getCondation() != null&&!(context.getCondation() instanceof Limit)){
			sql.append(context.getCondation().toSql(context.getType(), paramter));
			paramter.put(Constant.SQL_SYMBOL, sql.toString());
		}else if(context.getCondation() != null&&context.getDaoConfig().getDataBase() == DataBase.MYSQL&&context.getCondation() instanceof Limit){
			sql.append(context.getCondation().toSql(context.getType(), paramter));
			paramter.put(Constant.SQL_SYMBOL, sql.toString());
		}else if(context.getCondation() != null&&context.getDaoConfig().getDataBase() == DataBase.ORACLE&&context.getCondation() instanceof Limit){
			String pageSql = context.getCondation().toSql(context.getType(), paramter);
			pageSql = pageSql.replace(Constant.ORACLE_SQL_SYMBOL, sql.toString());
			paramter.put(Constant.SQL_SYMBOL, pageSql.toString());
		}
//		if (context.getCondation() != null)
//			sql.append(context.getCondation().toSql(context.getType(), paramter));
		paramter.put(Constant.SQL_SYMBOL, sql.toString());
		List<Map<String, Object>> mapResultList = context.getDaoMapper().select(paramter);
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) Toolkit.convertMapToObjectList(context.getType(), mapResultList, context.getObjectCreator());
		return result;
	}

	@Override
	protected String getTableName(SelectContext context) {
		// TODO Auto-generated method stub
		return selectExcutor.getTableName(context);
	}
	
	
}

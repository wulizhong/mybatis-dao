package org.mybatis.dao.insert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.mybatis.dao.Constant;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Many;
import org.mybatis.dao.database.ManyToMany;
import org.mybatis.dao.database.One;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.mapper.DaoMapper;
import org.mybatis.dao.util.ReflectionUtils;
import org.mybatis.dao.util.Toolkit;

public class RelationInsertExcutor extends InsertExcutor {

	private InsertExcutor insertExcutor;

	public RelationInsertExcutor(InsertExcutor insertExcutor) {
		this.insertExcutor = insertExcutor;
	}

	@Override
	public int insert(InsertContext context) {
		// TODO Auto-generated method stub
		int count = insertOneObj(context);
		count = count + insertExcutor.insert(context);
		count = count + insertManyObj(context);
		count = count + insertManyManyObj(context);
		return count;
	}

	private int insertManyObj(InsertContext context) {
		int count = 0;
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		Object id = ReflectionUtils.getValue(target, table.getId().getField());
		for (Many many : table.getManyMap().values()) {
			List<?> objList = (List<?>) ReflectionUtils.getValue(target, many.getField());
			if(objList == null)
				continue;
			for (Object obj : objList) {
				Class<?> objType = Toolkit.isCglibProxyClass(many.getType())?many.getType().getSuperclass():many.getType();
				Table objTable = TableMap.getInstance().getTableMap(objType);
				Field objField = objTable.getFieldByOriginalFieldName(many.getManyIdFieldName());
				ReflectionUtils.setValue(obj, objField, id);
				InsertContext insertContext = new InsertContext(obj, context.getDaoConfig(), context.getDaoMapper());
				count += new RelationInsertExcutor(new InsertExcutor()).insert(insertContext);
			}
		}
		return count;
	}

	private int insertManyManyObj(InsertContext context){
		int count = 0;
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		Object fromId = ReflectionUtils.getValue(target, table.getId().getField());
		for(ManyToMany many:table.getManyToManyMap().values()){
			List<?> objList = (List<?>) ReflectionUtils.getValue(target, many.getField());
			if(objList == null)
				continue;
			for(Object obj : objList){
				InsertContext insertContext = new InsertContext(obj, context.getDaoConfig(), context.getDaoMapper());
				count += new RelationInsertExcutor(new InsertExcutor()).insert(insertContext);
				Class<?> objType = Toolkit.isCglibProxyClass(many.getType())?many.getType().getSuperclass():many.getType();
				Table objTable = TableMap.getInstance().getTableMap(objType);
				Object toId = ReflectionUtils.getValue(obj, objTable.getId().getField());
				count +=insert(fromId,toId,many,context.getDaoMapper());
			}
			
		}
		return count;
	}
	
	private int insert(Object fromId,Object toId,ManyToMany mm,DaoMapper daoMapper){
		SQL sql = new SQL();
		sql = sql.INSERT_INTO(mm.getRelationTableName());
		sql = sql.VALUES(mm.getFromId(), "#{" + mm.getFromId() + "}");
		sql = sql.VALUES(mm.getToId(), "#{" + mm.getToId() + "}");
		HashMap<String, Object> paramter = new HashMap<>();
		paramter.put(Constant.SQL_SYMBOL, sql.toString());
		paramter.put(mm.getFromId(), fromId);
		paramter.put(mm.getToId(), toId);
		return daoMapper.insert(paramter);
	}
	
	private int insertOneObj(InsertContext context) {
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		int result = 0;
		for (One one : table.getOneMap().values()) {
			Field oneField = one.getField();
			Class<?> oneTargetType = Toolkit.isCglibProxyClass(oneField.getType()) ? oneField.getType().getSuperclass() : oneField.getType();
			Table oneObjTable = TableMap.getInstance().getTableMap(oneTargetType);
			Object oneObj = ReflectionUtils.getValue(target, oneField);
			if (oneObj == null)
				continue;
			InsertContext insertContext = new InsertContext(oneObj, context.getDaoConfig(), context.getDaoMapper());
			result += new RelationInsertExcutor(new InsertExcutor()).insert(insertContext);

			Field idField = table.getFieldByOriginalFieldName(one.getOneIdFieldName());
			Field oneIdField = oneObjTable.getId().getField();

			Object id = ReflectionUtils.getValue(oneObj, oneIdField);
			ReflectionUtils.setValue(target, idField, id);
		}
		return result;
	}
}

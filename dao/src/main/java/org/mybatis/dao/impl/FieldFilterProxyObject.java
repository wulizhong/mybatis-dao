package org.mybatis.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.mybatis.dao.Condation;
import org.mybatis.dao.Constant;
import org.mybatis.dao.FieldFilter;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.condation.Cnd;
import org.mybatis.dao.database.Many;
import org.mybatis.dao.database.ManyToMany;
import org.mybatis.dao.database.One;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.selecte.SelectExcutor;
import org.mybatis.dao.selecte.SelectContext;
import org.mybatis.dao.util.CollectionUtils;
import org.mybatis.dao.util.ReflectionUtils;
import org.mybatis.dao.util.StringUtils;
import org.mybatis.dao.util.Toolkit;

public class FieldFilterProxyObject implements MethodInterceptor {

	private SelectContext selectContext;

	private FieldFilter fieldFilter;

	public FieldFilterProxyObject(SelectContext selectContext, FieldFilter fieldFilter) {
		this.selectContext = selectContext;
		this.fieldFilter = fieldFilter;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		String methodName = method.getName();
		Class<?> returnType = method.getReturnType();

		if (methodName.startsWith("get") && returnType != null && !Toolkit.isBasicType(returnType)) {
			Table table = TableMap.getInstance().getTableMap(obj.getClass().getSuperclass());
			String fieldName = StringUtils.toLowerCaseFirstOne(methodName.substring(3));

			List<String> includeFields = fieldFilter.getIncludeFields();

			List<String> excludeFields = fieldFilter.getExcludeFields();

			boolean isProxy = false;
			if (!CollectionUtils.isEmpty(includeFields)) {

				for(String includeField : includeFields){
					if(includeField.toLowerCase().equals(fieldName.toLowerCase())){
						isProxy = true;
						break;
					}
				}
				
			} 
			if (!CollectionUtils.isEmpty(excludeFields)) {
				isProxy = true;
				for(String excludeField : excludeFields){
					if(excludeField.toLowerCase().equals(fieldName.toLowerCase())){
						isProxy = false;
					}
				}
			}

			if(!isProxy){
				return proxy.invokeSuper(obj, args);
			}
			
			if (Toolkit.isListType(returnType)) {
				Many many = table.getManyMapField(fieldName);
				ManyToMany manyToMany = table.getManyToManyMapField(fieldName);
				if (many != null) {
					Field manyFiled = many.getField();
					Object fieldValue = ReflectionUtils.getValue(obj, manyFiled);
					if (fieldValue != null) {
						return fieldValue;
					}

					Field idField = table.getId().getField();
					Object idObj = ReflectionUtils.getValue(obj, idField);

					Class<?> clazz = Toolkit.getGenericType(manyFiled);
					if (clazz == null) {
						return null;
					}
					SelectContext context = new SelectContext(clazz, selectContext.getDaoConfig(), selectContext.getDaoMapper(), Cnd.where(
							many.getManyIdFieldName(), "=", idObj));
					SelectExcutor select = new SelectExcutor();
					Object result = select.select(context);
					Field field = many.getField();
					ReflectionUtils.setValue(obj, field, result);
					return result;
				} else if (manyToMany != null) {
					Field manyToManyField = manyToMany.getField();
					Object fieldValue = ReflectionUtils.getValue(obj, manyToManyField);
					if (fieldValue != null) {
						return fieldValue;
					}

					Field idField = table.getId().getField();
					Object idObj = ReflectionUtils.getValue(obj, idField);

					Class<?> clazz = Toolkit.getGenericType(manyToManyField);
					if (clazz == null) {
						return null;
					}

					StringBuffer sqlBuffer = new StringBuffer();

					Table resultTable = TableMap.getInstance().getTableMap(clazz);

					sqlBuffer.append("select ");
					sqlBuffer.append(resultTable.getName());
					sqlBuffer.append(".* from ");
					sqlBuffer.append(resultTable.getName());
					sqlBuffer.append(",");
					sqlBuffer.append(manyToMany.getRelationTableName());

					sqlBuffer.append(" where ");
					sqlBuffer.append(resultTable.getName());
					sqlBuffer.append(".");
					sqlBuffer.append(resultTable.getId().getId());
					sqlBuffer.append(" = ");
					sqlBuffer.append(manyToMany.getRelationTableName());
					sqlBuffer.append(".");
					sqlBuffer.append(manyToMany.getToId());
					sqlBuffer.append(" and ");
					sqlBuffer.append(manyToMany.getRelationTableName());
					sqlBuffer.append(".");
					sqlBuffer.append(manyToMany.getFromId());
					sqlBuffer.append(" = #{");
					sqlBuffer.append(manyToMany.getFromId());
					sqlBuffer.append("}");

					HashMap<String, Object> paramter = new HashMap<>();
					paramter.put(manyToMany.getFromId(), idObj);

					paramter.put(Constant.SQL_SYMBOL, sqlBuffer.toString());
					List<Map<String, Object>> mapResultList = selectContext.getDaoMapper().select(paramter);
					Object result = Toolkit.convertMapToObjectList(selectContext.getType(), mapResultList, new DefaultObjectCreator());

					Field field = manyToMany.getField();
					ReflectionUtils.setValue(obj, field, result);
					return result;
				}
				return null;
			} else {
				One one = table.getOneMapField(fieldName);
				Field oneFiled = one.getField();
				Object fieldValue = ReflectionUtils.getValue(obj, oneFiled);
				if (fieldValue != null) {
					return fieldValue;
				}
				Field idField = table.getFieldByOriginalFieldName(one.getOneIdFieldName());
				Object idObj = ReflectionUtils.getValue(obj, idField);

				Condation cnd = Cnd.where(table.getId().getId(), "=", idObj);
				SelectContext context = new SelectContext(returnType, selectContext.getDaoConfig(), selectContext.getDaoMapper(), cnd);
				SelectExcutor select = new SelectExcutor();
				List<?> resultList = select.select(context);
				Object result = null;
				if (!CollectionUtils.isEmpty(resultList)) {
					result = resultList.get(0);
				}
				Field field = one.getField();
				ReflectionUtils.setValue(obj, field, result);
				return result;
			}
		} else {
			return proxy.invokeSuper(obj, args);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T newProxyInstance(Class<T> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		T obj = (T) enhancer.create();
		return obj;
	}
}

package org.mybatis.dao.database;

import java.lang.reflect.Field;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月17日 下午5:17:05 
 * 
 */
public class Many extends Relationship{

	private String manyIdFieldName;
	
	private Field field;
	
	private Class<?> type;
	
	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getManyIdFieldName() {
		return manyIdFieldName;
	}

	public void setManyIdFieldName(String manyIdFieldName) {
		this.manyIdFieldName = manyIdFieldName;
	}


	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	
	
	
}

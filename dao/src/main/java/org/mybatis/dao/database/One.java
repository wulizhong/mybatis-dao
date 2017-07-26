package org.mybatis.dao.database;

import java.lang.reflect.Field;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月17日 下午5:17:05 
 * 
 */
public class One extends Relationship{

	private String oneIdFieldName;
	
	private Field field;
	

	public String getOneIdFieldName() {
		return oneIdFieldName;
	}

	public void setOneIdFieldName(String oneIdFieldName) {
		this.oneIdFieldName = oneIdFieldName;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	
	
	
}

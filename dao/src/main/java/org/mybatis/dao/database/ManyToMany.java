package org.mybatis.dao.database;

import java.lang.reflect.Field;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 上午10:07:55 
 * 
 */
public class ManyToMany extends Relationship{

	private String relationTableName;
	
	
	private Field field;
	
	private Class<?> type;
	
	private String fromId;
	
	private String toId;
	
	
	
	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getRelationTableName() {
		return relationTableName;
	}

	public void setRelationTableName(String relationTableName) {
		this.relationTableName = relationTableName;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}
	
	
}

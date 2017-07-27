package org.mybatis.dao.database;

import java.lang.reflect.Field;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午12:44:31 
 * 
 */
public class Id{
	private String id;
	private Field field;
	private boolean autoGenerateId = true;
	private String sequence;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public boolean isAutoGenerateId() {
		return autoGenerateId;
	}
	public void setAutoGenerateId(boolean autoGenerateId) {
		this.autoGenerateId = autoGenerateId;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
}

package org.mybatis.dao.update;


/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月24日 上午11:33:28 
 * 
 */
public class DynamicTableNameUpdateExcutor extends UpdateExcutor{

	private String dynamicTableName;
	
	public DynamicTableNameUpdateExcutor(String dynamicTableName){
		this.dynamicTableName = dynamicTableName;
	}
	protected String getTableName(UpdateContext context) {
		return dynamicTableName;
	}
}

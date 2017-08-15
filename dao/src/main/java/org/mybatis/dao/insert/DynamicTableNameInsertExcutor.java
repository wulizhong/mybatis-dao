package org.mybatis.dao.insert;
/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2017年8月15日 上午11:58:27 
 * 
 */
public class DynamicTableNameInsertExcutor extends InsertExcutor{

	private String dynamicTableName;
	
	public DynamicTableNameInsertExcutor(String dynamicTableName ){
		this.dynamicTableName = dynamicTableName;
	}
	
	@Override
	protected String getTableName(InsertContext context) {
		// TODO Auto-generated method stub
		return dynamicTableName;
	}

	
}

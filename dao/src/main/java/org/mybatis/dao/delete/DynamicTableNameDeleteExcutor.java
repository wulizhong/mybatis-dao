package org.mybatis.dao.delete;


/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2017年8月15日 上午10:37:58 
 * 
 */
public class DynamicTableNameDeleteExcutor extends DeleteExcutor{

	private String dynamicTableName;
	
	public DynamicTableNameDeleteExcutor(String dynamicTableName){
		this.dynamicTableName = dynamicTableName;
	}

	@Override
	protected String getTableName(DeleteContext context) {
		// TODO Auto-generated method stub
		return dynamicTableName;
	}

	
}

package org.mybatis.dao.selecte;
/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2017年8月15日 下午1:43:11 
 * 
 */
public class DynamicTableNameSelectExcutor extends SelectExcutor{

	private String dynamicTableName;
	public DynamicTableNameSelectExcutor(String name){
		dynamicTableName = name;
	}
	@Override
	protected String getTableName(SelectContext context) {
		// TODO Auto-generated method stub
		return dynamicTableName;
	}
	
	
}

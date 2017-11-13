package org.mybatis.dao.update;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2017年11月09日 下午03:33:23 
 * 
 */
public class DynamicTableNameCondationUpdateExcutor extends CondationUpdateExcutor{

	private String tableName;
	public DynamicTableNameCondationUpdateExcutor(Class<?> clazz,String tableName,String[] fields,Object[] values){
		super(clazz,fields,values);
		this.tableName = tableName;
	}
	
	protected String getTableName(UpdateContext context) {
		return tableName;
	}
}

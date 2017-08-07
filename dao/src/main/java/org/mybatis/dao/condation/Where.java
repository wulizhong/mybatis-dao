package org.mybatis.dao.condation;

import java.util.Map;

import org.mybatis.dao.Condation;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Table;



/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午4:24:49 
 * 
 */
public class Where implements Condation{

	
	private String name;
	
	private String op;
	
	private Object value;
	
	
	protected Where(String name,String op,Object value){
		this.name = name;
		this.op = op;
		this.value = value;
	}
	
	@Override
	public String toSql(Class<?> clazz,Map<String,Object> paramter) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		TableMap tableMap = TableMap.getInstance();
		builder.append(" where ");
		Table table = tableMap.getTableMap(clazz);
		builder.append(table.getDataBaseField(name));
		builder.append(op);
		builder.append("#{");
		builder.append(name);
		builder.append("}");
		paramter.put(name, value);

		
		return builder.toString();
	}

	public And and(String name,String op,Object value){
		And and = new And(this,name,op,value);
		return and;
	}
	
	public Or or(String name,String op,Object value){
		Or or = new Or(this,name,op,value);
		return or;
	}
	
	public OrderBy orderBy(String name,String value){
		OrderBy ob = new OrderBy(this,name,value);
		return ob;
	}
	
	public OrderBy orderBy(String name){
		OrderBy ob = new OrderBy(this,name,Cnd.ASC);
		return ob;
	}
}

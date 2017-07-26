package org.mybatis.dao.condation;

import java.util.Map;

import org.mybatis.dao.Condation;
import org.mybatis.dao.TableMap;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月13日 下午4:50:13
 * 
 */
public class Or implements Condation {

	private Condation condation;
	
	private String name;
	
	private String op;
	
	private Object value;
	
	protected Or(Condation condation,String name, String op, Object value) {
		this.condation = condation;
		this.name = name;
		this.op = op;
		this.value = value;
	}

	@Override
	public String toSql(Class<?> clazz,Map<String,Object> paramter) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		builder.append(condation.toSql(clazz,paramter));
		builder.append(" or ");
		builder.append(TableMap.getInstance().getTableMap(clazz).getDataBaseField(name));
		builder.append(op);
		builder.append("#{");
		builder.append(name);
		builder.append("}");
		paramter.put(name, value);
		return builder.toString();
	}
	
	public And and(String name,String op,Object value){
		And and = new And(this,name,op,value);
		condation = and;
		return and;
	}
	
	public Or or(String name,String op,Object value){
		Or or = new Or(this,name,op,value);
		condation = or;
		return or;
	}
}

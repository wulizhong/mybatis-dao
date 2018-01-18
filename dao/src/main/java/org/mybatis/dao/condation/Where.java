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

	

	private RawSql rawSql;
	
	private String name;
	
	private String op;
	
	private Object value;
	
	
	protected Where(String name,String op,Object value){
		this.name = name;
		this.op = op;
		this.value = value;
	}
	
	protected Where(RawSql rawSql){
		this.rawSql = rawSql;
	}
	
	@Override
	public String toSql(Class<?> clazz,Map<String,Object> paramter) {
		// TODO Auto-generated method stub
		int id = IdGrenerator.grenerateId();
		StringBuilder builder = new StringBuilder();
		TableMap tableMap = TableMap.getInstance();
		builder.append(" where ");
		if(rawSql == null){
			Table table = tableMap.getTableMap(clazz);
			builder.append(table.getDataBaseField(name));
			builder.append(" ");
			builder.append(op);
			builder.append(" ");
			builder.append("#{");
			builder.append(name+id);
			builder.append("}");
			paramter.put(name+id, value);
		}else{
			builder.append(this.rawSql.toSql(clazz, paramter));
		}
		

		
		return builder.toString();
	}

	public And and(String name,String op,Object value){
		And and = new And(this,name,op,value);
		return and;
	}
	
	
	
	public And and(Segment segment){
		And and = new And(this,segment);
		return and;
	}
	
	public Or or(String name,String op,Object value){
		Or or = new Or(this,name,op,value);
		return or;
	}
	
	public Or or(Segment segment){
		Or or = new Or(this,segment);
		return or;
	}
	
	public Or or(RawSql rawSql){
		Or or = new Or(this,rawSql);
		return or;
	}
	
	public And and(RawSql rawSql){
		And and = new And(this,rawSql);
		return and;
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

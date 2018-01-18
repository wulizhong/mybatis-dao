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
	
	private Segment segment;
	
	private RawSql rawSql;
	
	protected Or(Condation condation, RawSql rawSql) {
		this.condation = condation;
		this.rawSql = rawSql;
	}
	
	protected Or(Condation condation,String name, String op, Object value) {
		this.condation = condation;
		this.name = name;
		this.op = op;
		this.value = value;
	}

	protected Or(Condation condation, Segment segment) {
		this.condation = condation;
		this.segment = segment;
	}
	
	@Override
	public String toSql(Class<?> clazz,Map<String,Object> paramter) {
		// TODO Auto-generated method stub
		if (segment != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(condation.toSql(clazz, paramter));
			builder.append(" or (");
			String sql = segment.toSql(clazz, paramter);
			sql = sql.replace("where", "");
			builder.append(sql);
			builder.append(" ) ");

			return builder.toString();
		} else if(rawSql!=null){
			StringBuilder builder = new StringBuilder();
			builder.append(condation.toSql(clazz, paramter));
			builder.append(" or ");
			builder.append(rawSql.toSql(clazz, paramter));
			return builder.toString();
		} else {
			int id = IdGrenerator.grenerateId();
			StringBuilder builder = new StringBuilder();
			builder.append(condation.toSql(clazz, paramter));
			builder.append(" or ");
			builder.append(TableMap.getInstance().getTableMap(clazz).getDataBaseField(name));
			builder.append(" ");
			builder.append(op);
			builder.append(" ");
			builder.append("#{");
			builder.append(name+id);
			builder.append("}");
			paramter.put(name+id, value);

			return builder.toString();
		}
	}
	
	public And and(String name,String op,Object value){
		And and = new And(this,name,op,value);
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
	
	public And and(Segment segment){
		And and = new And(this,segment);
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

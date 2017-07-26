package org.mybatis.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.mybatis.dao.SqlProvider;


/** 
 * @author 作者 :吴立中 
 * @version 创建时间：2016年7月12日 下午3:35:42 
 * 类说明 
 */
public interface DaoMapper {

	@SelectProvider(type = SqlProvider.class, method = "select")  
	public List<Map<String, Object>> select(Map<String,Object> paramter);
	
	@SelectProvider(type = SqlProvider.class, method = "insertUseReturnGeneratedKeys")
	public Map<String, Object> insertUseReturnGeneratedKeys(Map<String,Object> paramter);
	
	@InsertProvider(type = SqlProvider.class, method = "insert")
	public int insert(Map<String,Object> paramter);
	
	@UpdateProvider(type = SqlProvider.class, method = "update")
	public int update(Map<String,Object> paramter);
	
	@DeleteProvider(type = SqlProvider.class, method = "delete")
	public int delete(Map<String,Object> paramter);
 
}

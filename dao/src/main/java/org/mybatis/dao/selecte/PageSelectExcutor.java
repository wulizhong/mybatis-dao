package org.mybatis.dao.selecte;

import java.util.HashMap;
import java.util.List;

import org.mybatis.dao.Condation;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.condation.Limit;
import org.mybatis.dao.database.Table;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月19日 下午5:01:00
 * 
 */
public class PageSelectExcutor extends SelectExcutor {
	private SelectExcutor selectExcutor;

	private Page page;
	public PageSelectExcutor(SelectExcutor select,Page page) {
		this.selectExcutor = select;
		this.page = page;
	}

	@Override
	public <T> List<T> select(SelectContext context) {
		// TODO Auto-generated method stub
		if (page != null) {
			HashMap<String, Object> paramter = new HashMap<String, Object>();
			Table table = TableMap.getInstance().getTableMap(context.getType());
			Condation cnd = context.getCondation();
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("select ");
			sqlBuilder.append(table.getId().getId());
			sqlBuilder.append(" from ");
			sqlBuilder.append(table.getName());
			if (cnd != null)
				sqlBuilder.append(cnd.toSql(context.getType(), paramter));
			page.setDaoMapper(context.getDaoMapper());
			page.setParamter(paramter);
			page.setSql(sqlBuilder.toString());
			context.setCondation(new Limit(context.getCondation(), context.getDaoConfig().getDataBase(), page.getPageNumber(), page.getPageSize()));
		}
		return selectExcutor.select(context);
	}
}

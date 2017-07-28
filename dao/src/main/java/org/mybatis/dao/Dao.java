package org.mybatis.dao;

import java.util.List;

import org.mybatis.dao.condation.Cnd;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.delete.DeleteContext;
import org.mybatis.dao.delete.DeleteExcutor;
import org.mybatis.dao.exception.IdIsNullException;
import org.mybatis.dao.insert.InsertContext;
import org.mybatis.dao.insert.InsertExcutor;
import org.mybatis.dao.insert.RelationInsertExcutor;
import org.mybatis.dao.mapper.DaoMapper;
import org.mybatis.dao.selecte.FieldFilterRelationSelectExcutor;
import org.mybatis.dao.selecte.FieldFilterSelectExcutor;
import org.mybatis.dao.selecte.Page;
import org.mybatis.dao.selecte.PageSelectExcutor;
import org.mybatis.dao.selecte.RelationSelectExcutor;
import org.mybatis.dao.selecte.SelectContext;
import org.mybatis.dao.selecte.SelectExcutor;
import org.mybatis.dao.update.FieldFilterUpdateExcutor;
import org.mybatis.dao.update.UpdateContext;
import org.mybatis.dao.update.UpdateExcutor;
import org.mybatis.dao.util.CollectionUtils;
import org.mybatis.dao.util.ReflectionUtils;
import org.mybatis.dao.util.Toolkit;

/**
 * @author 作者 :吴立中
 * @version 创建时间：2016年7月12日 下午4:16:39 类说明
 */
public class Dao {

	private DaoMapper daoMapper;

	private DaoConfig daoConfig;

	public Dao(DaoMapper daoMapper, DaoConfig daoConfig) {
		this.daoMapper = daoMapper;
		this.daoConfig = daoConfig;
	}

	public <T> int update(T t) {

		Class<?> targetType = Toolkit.isCglibProxy(t) ? t.getClass().getSuperclass() : t.getClass();

		Table table = TableMap.getInstance().getTableMap(targetType);

		Object id = ReflectionUtils.getValue(t, table.getId().getField());
		if (id == null) {
			throw new IdIsNullException(targetType.getName() + " Id is null");
		}

		Condation cnd = Cnd.where(table.getId().getId(), "=", id);

		UpdateContext updateContext = new UpdateContext(t, daoConfig, daoMapper);

		updateContext.setCondation(cnd);

		UpdateExcutor updateExcutor = new UpdateExcutor();

		return updateExcutor.update(updateContext);

	}
	
	public <T> int update(T t,FieldFilter filter) {

		Class<?> targetType = Toolkit.isCglibProxy(t) ? t.getClass().getSuperclass() : t.getClass();

		Table table = TableMap.getInstance().getTableMap(targetType);

		Object id = ReflectionUtils.getValue(t, table.getId().getField());
		if (id == null) {
			throw new IdIsNullException(targetType.getName() + " Id is null");
		}

		Condation cnd = Cnd.where(table.getId().getId(), "=", id);

		UpdateContext updateContext = new UpdateContext(t, daoConfig, daoMapper);

		updateContext.setCondation(cnd);

		UpdateExcutor updateExcutor = new UpdateExcutor();
		updateExcutor = new FieldFilterUpdateExcutor(filter,updateExcutor);
		return updateExcutor.update(updateContext);

	}

	public <T> int delete(T t) {
		Class<?> targetType = Toolkit.isCglibProxy(t) ? t.getClass().getSuperclass() : t.getClass();

		Table table = TableMap.getInstance().getTableMap(targetType);

		Object id = ReflectionUtils.getValue(t, table.getId().getField());
		if (id == null) {
			throw new IdIsNullException(targetType.getName() + " Id is null");
		}

		Condation cnd = Cnd.where(table.getId().getId(), "=", id);

		DeleteContext deleteContext = new DeleteContext(t, daoConfig, daoMapper);

		deleteContext.setCondation(cnd);

		DeleteExcutor deleteExcutor = new DeleteExcutor();

		return deleteExcutor.delete(deleteContext);
	}

	public <T> int delete(Class<T> t, Condation cnd) {

		DeleteContext deleteContext = null;
		try {
			deleteContext = new DeleteContext(t.newInstance(), daoConfig, daoMapper);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		deleteContext.setCondation(cnd);

		DeleteExcutor deleteExcutor = new DeleteExcutor();

		return deleteExcutor.delete(deleteContext);
	}

	public <T> int save(T t) {

		InsertContext insertContext = new InsertContext(t, daoConfig, daoMapper);

		InsertExcutor insertExcutor = new InsertExcutor();

		insertExcutor = new RelationInsertExcutor(insertExcutor);

		return insertExcutor.insert(insertContext);
	}

	public <T> int insert(T t) {
		InsertContext insertContext = new InsertContext(t, daoConfig, daoMapper);
		InsertExcutor insert = new InsertExcutor();
		return insert.insert(insertContext);
	}

	public <T> T find(Class<T> t, long id) {
		Table table = TableMap.getInstance().getTableMap(t);
		Condation c = Cnd.where(table.getId().getId(), "=", id);
		return find(t, c);
	}

	public <T> T find(Class<T> t, Condation cnd) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		SelectExcutor select = new SelectExcutor();
		select = new RelationSelectExcutor(select);
		List<T> resultList = select.select(sc);
		return CollectionUtils.isEmpty(resultList) ? null : resultList.get(0);
	}

	public <T> T selectOne(Class<T> t, long id) {
		Table table = TableMap.getInstance().getTableMap(t);
		Condation c = Cnd.where(table.getId().getId(), "=", id);
		return selectOne(t, c);
	}

	public <T> T selectOne(Class<T> t, FieldFilter filter, long id) {
		Table table = TableMap.getInstance().getTableMap(t);
		Condation c = Cnd.where(table.getId().getId(), "=", id);
		return selectOne(t, filter, c);
	}

	public <T> T selectOne(Class<T> t, FieldFilter filter, Condation cnd) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		FieldFilterSelectExcutor select = new FieldFilterSelectExcutor(filter);
		List<T> resultList = select.select(sc);
		return CollectionUtils.isEmpty(resultList) ? null : resultList.get(0);
	}

	public <T> T selectOne(Class<T> t, Condation cnd) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		SelectExcutor select = new SelectExcutor();
		List<T> resultList = select.select(sc);
		return CollectionUtils.isEmpty(resultList) ? null : resultList.get(0);
	}

	public <T> List<T> selectList(Class<T> t, Condation cnd) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		SelectExcutor select = new SelectExcutor();
		return select.select(sc);
	}

	public <T> List<T> selectList(Class<T> t, FieldFilter filter) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, null);
		FieldFilterSelectExcutor select = new FieldFilterSelectExcutor(filter);
		return select.select(sc);
	}

	public <T> List<T> selectList(Class<T> t, FieldFilter filter, Condation cnd) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		FieldFilterSelectExcutor select = new FieldFilterSelectExcutor(filter);
		return select.select(sc);
	}

	public <T> List<T> selectList(Class<T> t, FieldFilter filter, Condation cnd, Page page) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		SelectExcutor select = new FieldFilterSelectExcutor(filter);
		select = new PageSelectExcutor(select, page);
		return select.select(sc);
	}

	public <T> List<T> selectList(Class<T> t, Condation cnd, Page page) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		SelectExcutor select = new SelectExcutor();
		select = new PageSelectExcutor(select, page);
		List<T> resultList = select.select(sc);
		return resultList;
	}

	public <T> List<T> query(Class<T> t, Condation cnd, Page page) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		SelectExcutor select = new SelectExcutor();
		select = new RelationSelectExcutor(select);
		select = new PageSelectExcutor(select, page);
		return select.select(sc);
	}

	public <T> List<T> query(Class<T> t, Condation cnd) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		SelectExcutor select = new SelectExcutor();
		select = new RelationSelectExcutor(select);
		return select.select(sc);
	}

	public <T> List<T> query(Class<T> t, FieldFilter filter) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, null);
		SelectExcutor select = new FieldFilterSelectExcutor(filter);
		select = new FieldFilterRelationSelectExcutor(select, filter);
		return select.select(sc);
	}
	
	public <T> List<T> query(Class<T> t, FieldFilter filter, Condation cnd) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		SelectExcutor select = new FieldFilterSelectExcutor(filter);
		select = new FieldFilterRelationSelectExcutor(select, filter);
		return select.select(sc);
	}
	
	public <T> List<T> query(Class<T> t, FieldFilter filter, Condation cnd,Page page) {
		SelectContext sc = new SelectContext(t, daoConfig, daoMapper, cnd);
		SelectExcutor select = new FieldFilterSelectExcutor(filter);
		select = new FieldFilterRelationSelectExcutor(select, filter);
		select = new PageSelectExcutor(select, page);
		return select.select(sc);
	}

}

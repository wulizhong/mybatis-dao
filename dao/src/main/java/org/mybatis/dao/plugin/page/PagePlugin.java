package org.mybatis.dao.plugin.page;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.dao.plugin.page.impl.MySqlPageSqlDialect;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,
				BoundSql.class }), })
public class PagePlugin implements Interceptor {

//	private PageSqlDialect pageSqlDialect = new MySqlPageSqlDialect();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		PageData<?> pageData = PageHelper.getThreadLocal().get();
		if (pageData == null) {
			return invocation.proceed();
		}
		List resultList;

		try {
			Object[] args = invocation.getArgs();
			MappedStatement ms = (MappedStatement) args[0];
			Object parameter = args[1];
			RowBounds rowBounds = (RowBounds) args[2];
			ResultHandler<?> resultHandler = (ResultHandler<?>) args[3];
			Executor executor = (Executor) invocation.getTarget();
			CacheKey cacheKey;
			BoundSql boundSql;
			// 由于逻辑关系，只会进入一次
			if (args.length == 4) {
				// 4 个参数时
				boundSql = ms.getBoundSql(parameter);
				cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
			} else {
				// 6 个参数时
				cacheKey = (CacheKey) args[4];
				boundSql = (BoundSql) args[5];
			}
			String sourceSql = boundSql.getSql();
			PageSqlDialect pageSqlDialect = PageSqlDialectFactory.createPageSqlDialect(ms);
			
			if(pageData.isSelectCount()){
				String countSql = pageSqlDialect.getCountSql(sourceSql);
				setNewSql(boundSql,countSql);
				MappedStatement countMappedStatement = createCountMappedStatement(ms,ms.getId()+"_count");
				CacheKey countCacheKey = executor.createCacheKey(countMappedStatement, parameter, RowBounds.DEFAULT, boundSql);
				Object countResultList = executor.query(countMappedStatement, parameter, RowBounds.DEFAULT, resultHandler, countCacheKey, boundSql);
		        Long count = (Long) ((List) countResultList).get(0);
		        pageData.setTotalCount(count);
			}
			
			String pageSql = pageSqlDialect.getPageSql(sourceSql, pageData.getPageNumber(), pageData.getPageSize());
			setNewSql(boundSql,pageSql);
			resultList = executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, boundSql);
			pageData.setData(resultList);
			
		} finally {
			PageHelper.getThreadLocal().remove();
		}
		return resultList;
	}

	private MappedStatement createCountMappedStatement(MappedStatement ms, String newMsId) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), newMsId, ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        //count查询返回值int
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Long.class, new ArrayList<ResultMapping>(0)).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
	
	private void setNewSql(BoundSql obj,String sql) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Class<?> boundSqlClass = BoundSql.class;
		Field sqlField = boundSqlClass.getDeclaredField("sql");
		sqlField.setAccessible(true);
		sqlField.set(obj, sql);
		sqlField.setAccessible(false);
	}

	@Override
	public Object plugin(Object target) {
		// TODO Auto-generated method stub
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		// pageSqlDialect = new MySqlPageSqlDialect();
	}

}

package org.mybatis.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.CallableStatementHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.SimpleStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.mybatis.dao.mapper.DaoMapper;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月14日 下午4:09:16
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class DaoPlugin implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			StatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
			MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
			String mapperId = mappedStatement.getId();
			String mapperClassName = mapperId.substring(0, mapperId.lastIndexOf("."));
			Class<?> mapperClass = Class.forName(mapperClassName);
			if (isDaoMapper(mapperClass)&&mapperId.endsWith(".insertUseReturnGeneratedKeys")) {
				BaseStatementHandler baseStatementHandler = (BaseStatementHandler) metaStatementHandler.getValue("delegate");
				if ("query".equals(invocation.getMethod().getName())) {
					return handPreparedStatementHandlerQuery(invocation, baseStatementHandler);
				} else if ("prepare".equals(invocation.getMethod().getName())) {
					return handPreparedStatementHandlerPrepare(invocation, baseStatementHandler);
				}
			}
		}
		return invocation.proceed();
	}
	private  boolean isDaoMapper(Class<?> mapperClass){
		if(mapperClass == DaoMapper.class){
			return true;
		}
		Class<?> superInterfaces[] = mapperClass.getInterfaces();
		for(Class<?> superInterface : superInterfaces){
			if(superInterface == DaoMapper.class){
				return true;
			}else{
				return isDaoMapper(superInterface);
			}
		}
		return false;
	}
	
	private Object handPreparedStatementHandlerQuery(Invocation invocation, BaseStatementHandler baseStatementHandler) {
		Statement statement = (Statement) invocation.getArgs()[0];
		ResultSet rs = null;
		Map<String, Object> resultMap = new HashMap<>();
		try {
			if (baseStatementHandler instanceof SimpleStatementHandler) {
				BoundSql boundSql = baseStatementHandler.getBoundSql();
				String sql = boundSql.getSql();
				statement.execute(sql, Statement.RETURN_GENERATED_KEYS);
			} else if (baseStatementHandler instanceof PreparedStatementHandler) {
				PreparedStatement ps = (PreparedStatement) statement;
				ps.executeUpdate();
			} else if (baseStatementHandler instanceof CallableStatementHandler) {
				return null;
			}
			int rows = statement.getUpdateCount();
			resultMap.put("count", rows);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				long id = rs.getLong(1);
				resultMap.put("id", id);
			}
		} catch (Exception e) {
			throw new ExecutorException("Error getting generated key or setting result to parameter object. Cause: " + e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					// ignore
				}
			}
		}
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>(2);
		result.add(resultMap);
		return result;
	}

	private Object handPreparedStatementHandlerPrepare(Invocation invocation, BaseStatementHandler baseStatementHandler) {

		Statement statement = null;
		Connection connection = (Connection) invocation.getArgs()[0];
		Integer transactionTimeout = (Integer) invocation.getArgs()[1];
		try {
			if (baseStatementHandler instanceof SimpleStatementHandler) {
				connection.createStatement();
			} else if (baseStatementHandler instanceof PreparedStatementHandler) {
				BoundSql boundSql = baseStatementHandler.getBoundSql();
				String sql = boundSql.getSql();
				ErrorContext.instance().sql(boundSql.getSql());
				statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			}
		} catch (SQLException e) {
			throw new ExecutorException("Error preparing statement.  Cause: " + e, e);
		}
		try {
			Method setStatementTimeout = BaseStatementHandler.class.getDeclaredMethod("setStatementTimeout", Statement.class, Integer.class);
			setStatementTimeout.setAccessible(true);
			setStatementTimeout.invoke(baseStatementHandler, statement, transactionTimeout);
			setStatementTimeout.setAccessible(false);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return statement;
	}
	@Override
	public Object plugin(Object target) {
		// TODO Auto-generated method stub
		if (target instanceof RoutingStatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		System.out.println("测试下");
	}

}

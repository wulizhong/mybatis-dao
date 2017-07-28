package org.mybatis.dao.samples;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.dao.Dao;
import org.mybatis.dao.DaoConfig;
import org.mybatis.dao.DaoPlugin;
import org.mybatis.dao.DataBase;
import org.mybatis.dao.mapper.DaoMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** 
 * @author 作者 :吴立中
 * @version 创建时间：2016年7月12日 下午5:37:20 
 * 类说明 
 */
@Configuration
@MapperScan("org.mybatis.dao.mapper")
public class MybatisDaoConfig {
	
	@Bean(name = "dao")
    @Primary
    public Dao dao(@Qualifier("daoMapper")DaoMapper daoMapper) throws Exception {
		
		return new Dao(daoMapper,new DaoConfig() {
			
			@Override
			public DataBase getDataBase() {
				// TODO Auto-generated method stub
				return DataBase.MYSQL;
			}

		});
    }
	@Bean(name = "interceptors")
	@Primary
	public Interceptor[] interceptors(){
		return new Interceptor[]{new DaoPlugin()};
	}
}

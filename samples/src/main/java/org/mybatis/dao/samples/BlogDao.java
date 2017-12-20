package org.mybatis.dao.samples;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.mybatis.dao.samples.bean.Blog;

public interface BlogDao {
	@Select("select * from blog")
	public List<Blog> getBlog();
}

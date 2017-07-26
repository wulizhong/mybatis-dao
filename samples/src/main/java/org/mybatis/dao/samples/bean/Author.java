package org.mybatis.dao.samples.bean;

import java.util.List;

import org.mybatis.dao.FetchType;
import org.mybatis.dao.annotation.Many;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月17日 下午4:42:35 
 * 
 */
public class Author {

	private int id;
	
	private String name;
	
//	private int accountId;
	
	@Many(value = "authorId",fetchType = FetchType.NONE)
	private List<Blog> blogs;
	
	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}

//	public int getAccountId() {
//		return accountId;
//	}
//
//	public void setAccountId(int accountId) {
//		this.accountId = accountId;
//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
//	private Account account;
//
//	public Account getAccount() {
//		return account;
//	}
//
//	public void setAccount(Account account) {
//		this.account = account;
//	}
	
	
	
}

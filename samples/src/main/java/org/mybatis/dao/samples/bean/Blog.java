package org.mybatis.dao.samples.bean;

import org.mybatis.dao.FetchType;
import org.mybatis.dao.annotation.One;
import org.mybatis.dao.annotation.Table;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午1:26:53 
 * 
 */
@Table
public class Blog {

	private int id;
	
	private String title;
	
	private String content;
	
	private int authorId;

	@One(value="authorId",fetchType = FetchType.IMMEDIATELY)
	private Author author;
	
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	
	
}

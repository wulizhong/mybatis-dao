package org.mybatis.dao.samples.bean;

import java.util.List;

import org.mybatis.dao.annotation.ManyToMany;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 上午11:38:54 
 * 
 */
public class Student {

	private int id;
	
	private String name;
	
	@ManyToMany(relation="student_course",from="student_id",to="course_id")
	private List<Course> courses;


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


	public List<Course> getCourses() {
		return courses;
	}


	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	
}

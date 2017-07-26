package org.mybatis.dao.samples.bean;

import java.util.List;

import org.mybatis.dao.FetchType;
import org.mybatis.dao.annotation.ManyToMany;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 上午11:39:38 
 * 
 */
public class Course {

	private int id;
	
	private String name;
	
	@ManyToMany(relation="student_course",from="course_id",to="student_id",fetchType=FetchType.IMMEDIATELY)
	private List<Student> students;
	
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

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
	
	
}

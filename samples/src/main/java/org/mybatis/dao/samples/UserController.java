package org.mybatis.dao.samples;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.dao.Dao;
import org.mybatis.dao.FieldFilter;
import org.mybatis.dao.condation.Cnd;
import org.mybatis.dao.samples.bean.Author;
import org.mybatis.dao.samples.bean.Blog;
import org.mybatis.dao.samples.bean.Course;
import org.mybatis.dao.samples.bean.Student;
import org.mybatis.dao.selecte.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2017年7月26日 下午2:16:33
 * 
 */
@RestController
public class UserController {

	@Autowired
	private Dao dao;

	@RequestMapping("/updateBlog")
	public Blog updateBlog() {

		Blog b = dao.find(Blog.class, 1);
		b.setContent("修改之后的内容!");
		dao.update(b);
		return b;
	}

	@RequestMapping("/selectBlogs")
	public List<Blog> selectBlogs() {
		return dao.selectList(Blog.class, FieldFilter.include("title", "author", "authorId"), Cnd.where("id", ">", 6), new Page(1, 3));
	}

	@RequestMapping("/queryBlogs")
	public List<Blog> queryBlogs() {
		return dao.query(Blog.class, FieldFilter.include("title", "author", "authorId"), Cnd.where("id", ">", 6), new Page(1, 3));
	}

	@RequestMapping("/deleteBlog")
	public Blog deleteBlog() {

		Blog b = new Blog();
		b.setContent("修改之后的内容!");
		dao.delete(Blog.class, Cnd.where("authorId", Cnd.EQ, 2));
		return b;
	}

	@RequestMapping("/getBlogs")
	public List<Blog> getBlogs() {

		ArrayList<Blog> blogs = new ArrayList<Blog>();
		Blog b = dao.find(Blog.class, 1);
		System.out.println("getBlogs");
		b.getAuthor();
		blogs.add(b);

		return blogs;
	}

	@RequestMapping("/getAuthor")
	public Author getAuthor() {

		Author author = dao.find(Author.class, 1);
		System.out.println("author");
		List<Blog> blogs = author.getBlogs();
		return author;
	}

	@RequestMapping("/getCourses")
	public Course getCourses() {
		Course course = dao.find(Course.class, 3);
		System.out.println("getStudent");
		course.getStudents();
		return course;
	}

	@RequestMapping("/saveBlog")
	public Blog saveBlog() {

		Blog b = new Blog();

		b.setTitle("插入测试");

		Author author = new Author();
		author.setName("插入测试");
		b.setAuthor(author);
		int count = dao.save(b);

		System.out.println("插入数据条数:" + count);

		return b;
	}

	@RequestMapping("/saveAuthor")
	public Author saveAuthor() {
		Author author = new Author();
		author.setName("王五");

		List<Blog> blogs = new ArrayList<Blog>();
		Blog b1 = new Blog();
		b1.setContent("测试博客1");
		b1.setTitle("博客1");
		blogs.add(b1);

		Blog b2 = new Blog();
		b2.setContent("测试博客2");
		b2.setTitle("博客2");
		blogs.add(b2);

		Blog b3 = new Blog();
		b3.setContent("测试博客3");
		b3.setTitle("博客3");
		blogs.add(b3);

		author.setBlogs(blogs);

		dao.save(author);

		return author;
	}

	@RequestMapping("/saveStudent")
	public Student saveStudent() {

		Student s = new Student();
		s.setName("三好学生");

		ArrayList<Course> courses = new ArrayList<Course>();
		Course c1 = new Course();
		c1.setName("语文");
		courses.add(c1);

		c1 = new Course();
		c1.setName("英语");
		courses.add(c1);

		c1 = new Course();
		c1.setName("数学");
		courses.add(c1);

		s.setCourses(courses);
		dao.save(s);
		return s;

	}

	@RequestMapping("/getStudent")
	public Student getStudent() {
		Student s = dao.selectOne(Student.class, 1);
		return s;
	}

}

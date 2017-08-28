#  MyBatisDao是什么？
MyBatis 是支持定制化SQL、存储过程以及高级映射的优秀的持久层框架，但并不是完整的orm框架。
MyBatisDao扩展了MyBatis的orm部分，能像Hibernate那样使用MyBatis，同时不影响mybatis的其他部分。
# MyBatisDao特性
1. 支持java类和数据库表的映射。
2. 支持一对一、一对多、多对多关联。
3. 支持懒加载、立即加载模式。
4. 支持分页
5. 支持选择操作的字段。
6. 支持动态表名。
7. 支持多数据源。
# 快速入门（以springboot为例）
### 配置MyBatisDao

```java
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

```

### 创建bean类
```java
@Table//声明了Blog类所对应的数据库表
public class Blog {

	@Id//数据库表 id主键，如果是oracle数据库需要加@Seq注解，指定id的自增序列
	private int id;
	@Column("title") //数据库中表中所对应的字段，如果数据库表字段和类当中的字段相同可以省略，默认会把驼峰命名的字段转换为下划线，如 msgId-->msg_id
	private String title;
	
	private String content;
	
	private int authorId;
	//省略set get方法
	
}
```
### 创建数据库对应的表blog
省略建表过程
### 创建UserConroller

```
@RestController
public class UserController {

	@Autowired
	private Dao dao;//将dao注入进来
}
```
### 增加
```
@RequestMapping("/insertBlog")
public Blog insertBlog() {

	Blog b = new Blog();
		
	b.setTitle("插入测试");
		
	//save方法会将Blog关联的对象一起插入到数据库中
	int count = dao.insert(b);
	//insert方法只会插入Blog对象
	//int count = dao.save(b);
	
	//将Blog对象插入到blog_2表中
	//int count = dao.insert(b, "blog_2");
		
	System.out.println("插入数据条数:" + count);

	return b;
}
```
### 查询单个对象
```
@RequestMapping("/getBlog")
public Blog getBlog() {

	//查询id为5的对象,会查询出关联对象
	Blog b = dao.find(Blog.class, 5);
		
	//查询id为5的对象，只查询Blog对象，不关联查询
	//Blog b = dao.selectOne(Blog.class, 5);
		
		
	//按条件查询,查询title=abc id>3对象,会查询出关联对象
	Blog b2 = dao.find(Blog.class, Cnd.where("title", "=", "abc").and("id", ">", 3));
	//按条件查询,查询title=abc id>3对象,不关联查询
	//Blog b2 = dao.selectOne(Blog.class, Cnd.where("title", "like", "%abc%").and("id", ">", 3));
		
	//查询id为5的对象，并且只查询title、content的值
	Blog b3 = dao.selectOne(Blog.class, FieldFilter.include("title","content"), 5);
	//Blog b3 = dao.selectOne(Blog.class, FieldFilter.include("title","content"), Cnd.where("id", "=", 5));
		
	//查询id为5的对象，并且只除了title,"content"之外所有的的值
	Blog b4 = dao.selectOne(Blog.class, FieldFilter.exclude("title","content"), 5);
	//Blog b4 = dao.selectOne(Blog.class, FieldFilter.exclude("title","content"), Cnd.where("id", "=", 5));
		
	//从blog_2表中查询id为5的对象
	Blog b5 = dao.selectOne(Blog.class, "blog_2", 5);
	//Blog b5 = dao.selectOne(Blog.class, "blog_2", Cnd.where("id", "=", 5));

	//从blog_2表中查询id为5的对象，并且只查询title、content的值
	Blog b6 = dao.selectOne(Blog.class, "blog_2", FieldFilter.include("title","content"), 5);
	//Blog b6 = dao.selectOne(Blog.class, "blog_2", FieldFilter.include("title","content"), Cnd.where("id", "=", 5));
		
	//从blog_2表中查询id为5的对象，并且只除了title,"content"之外所有的的值
	Blog b7 = dao.selectOne(Blog.class, "blog_2", FieldFilter.exclude("title","content"), 5);
	//Blog b7 = dao.selectOne(Blog.class, "blog_2", FieldFilter.exclude("title","content"), Cnd.where("id", "=", 5));
		
	
	return b;
	
}
```
### 查询多条记录

```
@RequestMapping("/getBlogs")
public List<Blog> getBlogs(){
        //查询 title中包含abc字符并且id大于5的记录，按照title排序，第2页的每页10条的数据，会查询关联对象
	List<Blog> blogs = dao.query(Blog.class, Cnd.where("title", "like", "%abc%").and("id", ">", 5).orderBy("title"),new Page(2, 10));
	
	//从blog_2表中查询 title中包含abc字符并且id大于5的记录只包含title字段的值，按照title排序，第2页的每页10条的数据	
	//List<Blog> blogs = dao.selectList(Blog.class,"blog_2",FieldFilter.include("title"), Cnd.where("title", "like", "%abc%").and("id", ">", 5).orderBy("title"),new Page(2, 10));
	
	return blogs;
	
	}
```

### 修改
```
@RequestMapping("/updateBlog")
public Blog updateBlog() {

	Blog b = dao.find(Blog.class, 5);
	b.setContent("修改之后的内容3!");
	b.setTitle("修改测试");
	//更新blog对象
	dao.update(b);
	//只更新title和content字段
	dao.update(b,FieldFilter.include("title","content"));
	
	b = dao.selectOne(Blog.class, "blog_2", 5);
	b.setContent("修改之后的内容3!");
	b.setTitle("修改测试");
	//只更新blog_2表中的title和content字段 
	dao.update(b, "blog_2", FieldFilter.include("title","content"));
		
	return b;
}
```

### 删除
```
@RequestMapping("/deleteBlog")
public void deleteBlog() {
		
	Blog b = dao.selectOne(Blog.class,"blog_2", 1);
		
	dao.delete(b);
		
	dao.delete(Blog.class, Cnd.where("id", "=", 2));
		
	dao.delete(Blog.class, "blog_2", Cnd.where("id", "=", 2));
		
		
}

```


### 关于作者

邮箱 rz_wlz@126.com
有问题可邮件联系我！



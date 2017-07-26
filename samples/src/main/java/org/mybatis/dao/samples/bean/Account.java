package org.mybatis.dao.samples.bean;
/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午1:27:02 
 * 
 */
public class Account {

	private int id;
	
	private String account;
	
	private String nickName;
	
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

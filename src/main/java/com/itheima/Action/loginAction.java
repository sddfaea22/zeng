package com.itheima.Action;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import com.itheima.domain.User;
import com.itheima.utils.SysConstant;
import com.itheima.utils.UtilFuns;

public class loginAction extends BaseAction{
	private static final long serialVersionUID =1L;
	private String username;
	private String password;
	
	//这是登陆的方法
	public String login(){
		if(UtilFuns.isEmpty(username)){
			return "login";
		}
		try{
			//1.与Shiro交互
			Subject subject = SecurityUtils.getSubject();
			//2.调用subject中的方法，来实现登录
			UsernamePasswordToken token = new UsernamePasswordToken(username,password);;
			subject.login(token);//当login执行时，就会自动跳入authRealm域中的认证方法
			//3.从Shiro中取出用户登录结果信息
			User user = (User) subject.getPrincipal();
			//4.将用户信息保存到session中
			session.put(SysConstant.CURRENT_USER_INFO, user);
			return SUCCESS;
		} catch (Exception e){
			e.printStackTrace();
			request.put("errorInfo", "登录失败，用户名或密码错误！");   
			return "login";
		}
		
	}

	//这是退出的方法
	public String logout(){
		session.remove(SysConstant.CURRENT_USER_INFO);
		return "logout";
	}
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}

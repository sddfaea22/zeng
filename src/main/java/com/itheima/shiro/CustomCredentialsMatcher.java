package com.itheima.shiro;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import com.itheima.utils.Encrypt;

//自定义的密码比较器

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher{
	/**
	 * 重写了密码比较的方法
	 *  第一个参数AuthenticationToken  代表用户在界面上输入的用户名和密码
	 *  
	 *  第二个参数AuthenticationInfo   代表了当前这个用户在数据库中的信息，就会有加密后的密码
	 *  
	 *  返回值：true证明密码比较成功了
	 *        false证明密码比较失败，密码输入错误，程序会抛出异常
	 * 
	 */
	public boolean doCredentialsMatch(AuthenticationToken token,AuthenticationInfo info){
		//1.将用户在界面输入的密码进行加密 
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String password =new String(upToken.getPassword());
		
		String md5 = Encrypt.md5(password, upToken.getUsername());
		
		//2.将用户在数据库中的密码读取出来
		String string = info.getCredentials().toString();
		//3.进行比较
		boolean b = super.equals(md5, string);
		
		
		return b;
	}
	
	
}

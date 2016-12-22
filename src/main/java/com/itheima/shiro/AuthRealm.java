package com.itheima.shiro;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.itheima.domain.Module;
import com.itheima.domain.Role;
import com.itheima.domain.User;
import com.itheima.service.Userservice;

//自定义了一个Realm域 （主要作用是提供安全数据：用户，角色，模块）
public class AuthRealm extends AuthorizingRealm{
	//注入userService
    private Userservice Userservice;
	public void setUserservice(Userservice userservice) {
		Userservice = userservice;
	}
	

	//授权
	/**
	 * 验证用户是否具有某某权限
	 * 
	 * 当jsp页s面上碰到Shiro标签时就会调用这个方法，当第一次碰到时才调用这个方法
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		//1.得到用户信息
		User user = (User) pc.fromRealm(this.getName()).iterator().next();
		//2.通过对象导航,得到用户的角色列表
		Set<Role> roles = user.getRoles();
		List<String> list = new ArrayList<String>();
		//3.遍历角色列表，得到用户的每个角色
		for(Role role : roles){
			//得到每个角色 ，并通过对象导航，进一步加载这个角色下的模块列表
			Set<Module> set = role.getModules();
			//遍历模块的集合，得到每个模块信息
			for(Module en : set){
				list.add(en.getName());
			}
		}
		//声明AuthorizationInfo的一个子类对象
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(list);
		return info;
	}

	/**
	 * 认证（在登录时就会调用这个方法）    Subject.login();
	 * 参数：AuthenticationToken代表用户在界面上输入的用户名和密码
	 * 返回值不为null就会执行密码比较器
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.将token转化为子类对象
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		//2.从token中获取用户在界面输入的用户名
		String username = upToken.getUsername();
		//3.调用业务逻辑，根据用户名查询用户对象
		List<User> find = Userservice.find("from User where userName=?" , User.class , new String[]{username});
		
		if(find!=null && find.size()>0){
			//查询到了用户对象，说明用户名是正确的
			User user = find.get(0);
			//principal 代表用户信息             credentials 代表用户的密码              第三个参数：只要是一个字符串就可以
			AuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
			return info;
			
		}
		
		//4.组织返回的结果
		return null;
	}
	
	
	
	
	
	
	
	
	
}

package com.itheima.Action.sysadmin;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.itheima.Action.BaseAction;
import com.itheima.domain.Dept;
import com.itheima.domain.Role;
import com.itheima.domain.User;
import com.itheima.exception.SysException;
import com.itheima.service.Deptservice;
import com.itheima.service.Roleservice;
import com.itheima.service.Userservice;
import com.itheima.utils.Page;
import com.opensymphony.xwork2.ModelDriven;


/**
 * @author 27520
 *
 */
public class UserAction extends BaseAction implements ModelDriven<User>{
	private User model = new User();
	public User getModel() {
		return model;
	}
	
	private Userservice Userservice;
	public void setUserservice(Userservice userservice) {
		Userservice = userservice;
	}
	private Deptservice deptService;
	public void setDeptService(Deptservice deptService) {
		this.deptService = deptService;
	}

	private Roleservice Roleservice;
	public void setRoleservice(Roleservice roleservice) {
		Roleservice = roleservice;
	}

	private String[] roleIds;  //这是获取的角色列表
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	//分页组件
	private Page page = new Page();
	public void setPage(Page page) {
		this.page = page;
	}
	public Page getPage() {
		return page;
	}
	
	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		//1.调用业务方法，实现分页查询
		Userservice.findPage("from User where state=1", page, User.class, null);
		
		//2.设置url  用于上一页   下一页   
		//page.setUrl(ServletActionContext.getRequest().getContextPath()+"/sysadmin/UserAction_list");//绝对定位
		
		//http://localhost:8080/jk36_parent/sysadmin/UserAction_list
		page.setUrl("userAction_list");//相对定位
		
		//3.将结果放入值栈中
		super.push(page);
		
		return "list";
	}
	
	
	//查看数据
	public String toview(){
		User obj = Userservice.get(User.class, model.getId());
		
		super.push(obj);
		return "toview";
	}
	
	//跳到新增页面
	public String tocreate(){
	
		
		List<Dept> find2 = deptService.find("from Dept where state=1", Dept.class, null);
		
		super.put("deptList", find2);
		
		List<User> find = Userservice.find("from User where state=1", User.class, null);
		
		super.put("userList", find);
		
		return "tocreate";
	}
	
	//实现保存操作
	public String insert(){
		Userservice.saveOrUpdate(model);
		
		return "alist";
	}
	
	//跳到修改页面
	public String toupdate(){
		List<Dept> find = deptService.find("from Dept where state=1", Dept.class, null);
		
		super.put("deptList", find);

		return "toupdate";
	}
	
	public String delete(){
		String[] split = model.getId().split(", ");
		
		Userservice.delete(User.class, split);
		
		return "alist";
	}
	
	public String update(){
		//1.根据id,加载原有的部门对象
		User user = Userservice.get(User.class, model.getId());
		//2.设置要修改的属性
		user.setDept(model.getDept());
		user.setUserName(model.getUserName());
		user.setState(model.getState());
		
		Userservice.saveOrUpdate(user);
		
		return "alist";
	}
	
	
	//角色列表
	public String torole() throws SysException{
		try{
	    //1.根据用户id,得到用户对象
		User user = Userservice.get(User.class, model.getId());
		super.push(user);
		//通过对象导航获得用户有什么角色
		StringBuilder res = new StringBuilder();
		Set<Role> roles = user.getRoles();
		if(roles.size()!=0){
			for (Role role : roles) {
				
				res.append(role.getName()).append(",");
				
			}
		super.put("userRoleStr", res.toString());
		}
		
		//获得所有得分角色列表
		List<Role> find = Roleservice.find("from Role", Role.class, null);
		super.put("roleList",find);
		}catch(Exception e){
			e.printStackTrace();
			throw new SysException("使用方法错误");
		}
		
		return "torole";
	}
	
	//实现保存操作
	public String role(){
        //Role role = Roleservice.get(Role.class, model.getId());
		User user = Userservice.get(User.class, model.getId());
		//2.有哪些角色 
		Set<Role> hashSet = new HashSet<Role>();
		//处理roleIds数组
		for (String role : roleIds) {
			//遍历数组，得到每个角色的id
			Role role2 = Roleservice.get(Role.class, role);
			hashSet.add(role2);
		}
		//3.设置用户与角色的关系 
		user.setRoles(hashSet);
		//3.设置用户与角色的关系 
		Userservice.saveOrUpdate(user);
    	//5.跳页面
		return "alist";
	}
	
	
}



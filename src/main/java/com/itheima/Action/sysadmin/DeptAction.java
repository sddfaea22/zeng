package com.itheima.Action.sysadmin;

import java.util.List;

import com.itheima.Action.BaseAction;
import com.itheima.domain.Dept;
import com.itheima.service.Deptservice;
import com.itheima.utils.Page;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @author Administrator
 *为什么我们的Action要继承BaseAction
 *    目标：为了与struts2本身的api解藕合
 *        baseAction中可以抽取很多通用方法，这样子类直接使用
 *
 *DeptAction extends ActionSupport
 *UserAction
 *RoleAction
 *.....
 */
public class DeptAction extends BaseAction implements ModelDriven<Dept> {

	private Dept model = new Dept();
	public Dept getModel() {
		return model;
	}
	
	//分页组件
	private Page page = new Page();
	public void setPage(Page page) {
		this.page = page;
	}
	public Page getPage() {
		return page;
	}
	
	
	//注入业务逻辑
	private Deptservice deptService;
	public void setDeptService(Deptservice deptService) {
		this.deptService = deptService;
	}
	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		//1.调用业务方法，实现分页查询
		deptService.findPage("from Dept where state=1", page, Dept.class, null);
		
		//2.设置url  用于上一页   下一页   
		//page.setUrl(ServletActionContext.getRequest().getContextPath()+"/sysadmin/deptAction_list");//绝对定位
		
		//http://localhost:8080/jk36_parent/sysadmin/deptAction_list
		page.setUrl("deptAction_list");//相对定位
		
		//3.将结果放入值栈中
		super.push(page);
		
		return "list";
	}
	
	
	//查看数据
	public String toview(){
		Dept obj = deptService.get(Dept.class, model.getId());
		
		super.push(obj);
		return "toview";
	}
	
	//跳到新增页面
	public String tocreate(){
		List<Dept> find = deptService.find("from Dept where state=1", Dept.class, null);
		
		super.put("deptList", find);
		
		return "tocreate";
	}
	
	//实现保存操作
	public String insert(){
		deptService.saveOrUpdate(model);
		
		return "alist";
	}
	
	//跳到修改页面
	public String toupdate(){
		Dept obj = deptService.get(Dept.class, model.getId());
		
		super.push(obj);
		
		List<Dept> find = deptService.find("from Dept where state=1", Dept.class, null);
		
		find.remove(obj);
		super.put("deptList", find);
		
		
		return "toupdate";
	}
	
	public String delete(){
		String[] split = model.getId().split(", ");
		
		deptService.delete(Dept.class, split);
		
		return "alist";
	}
	
	
}
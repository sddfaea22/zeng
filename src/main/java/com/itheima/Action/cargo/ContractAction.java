package com.itheima.Action.cargo;
import java.util.List;

import com.itheima.Action.BaseAction;
import com.itheima.domain.Contract;
import com.itheima.domain.User;
import com.itheima.exception.SysException;
import com.itheima.service.Cargoservice;
import com.itheima.utils.Page;
import com.opensymphony.xwork2.ModelDriven;

public class ContractAction extends BaseAction implements ModelDriven<Contract>{
	
	private Contract model = new Contract();
	public Contract getModel() {
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
	
	private Cargoservice cargoservice;
	public void setCargoservice(Cargoservice cargoservice) {
		this.cargoservice = cargoservice;
	}
	
	
	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		String hpl = "from Contract where 1=1";
		
		User user = super.getContract();
		
		int degree = user.getUserinfo().getDegree();//得到当前登录用户的等级
		
		if(degree==4){
			//员工
			hpl +="and createBy='"+user.getId()+"'";
		}else if(degree==3){
			hpl +="and createDept='"+user.getDept().getId()+"'";
		}else if(degree==1){
			hpl ="from Contract";
		}
		
		
		//1.调用业务方法，实现分页查询
		cargoservice.findPage(hpl, page, Contract.class, null);
		
		//2.设置url  用于上一页   下一页   
		//page.setUrl(ServletActionContext.getRequest().getContextPath()+"/sysadmin/ContractAction_list");//绝对定位
		
		//http://localhost:8080/jk36_parent/sysadmin/ContractAction_list
		page.setUrl("contractAction_list");//相对定位
		
		//3.将结果放入值栈中
		super.push(page);
		
		return "list";
	}
	
	
	//查看数据
	public String toview() throws SysException{
		
		try{
		Contract obj = cargoservice.get(Contract.class, model.getId());
		
		super.push(obj);
		}catch(Exception e){
			e.printStackTrace();
			throw new SysException("使用方法错误");
			
		}
		
		return "toview";
	}
	
	//跳到新增页面
	public String tocreate(){

		/*List<Contract> find = cargoservice.find("from Contract ", Contract.class, null);

		super.put("ContractList", find);*/

		return "tocreate";
	}

	//实现保存操作
	public String insert(){
		cargoservice.saveOrUpdate(model);
		
		return "alist";
	}

	//跳到修改页面
	public String toupdate(){
		Contract Contract = cargoservice.get(Contract.class, model.getId());

		super.push(Contract);
		
		return "toupdate";
	}
	
	public String delete(){
		String[] split = model.getId().split(", ");
		
		cargoservice.delete(Contract.class, split);
		
		return "alist";
	}
	
	public String update(){
		
		cargoservice.saveOrUpdate(model);
		
		return "alist";
	}
	
	
	

}

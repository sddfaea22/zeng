package com.itheima.Action.sysadmin;
import com.itheima.Action.BaseAction;
import com.itheima.domain.Module;
import com.itheima.service.ModuleService;
import com.itheima.utils.Page;
import com.opensymphony.xwork2.ModelDriven;

public class ModuleAction extends BaseAction implements ModelDriven<Module>{

	private Module model = new Module();
	public Module getModel() {
		return model;
	}
	
	private ModuleService Modeleservice;
	public void setModeleservice(ModuleService modeleservice) {
		Modeleservice = modeleservice;
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
			Modeleservice.findPage("from Module", page, Module.class, null);
			
			//2.设置url  用于上一页   下一页   
			//page.setUrl(ServletActionContext.getRequest().getContextPath()+"/sysadmin/ModuleAction_list");//绝对定位
			
			//http://localhost:8080/jk36_parent/sysadmin/ModuleAction_list
			page.setUrl("moduleAction_list");//相对定位
			
			//3.将结果放入值栈中
			super.push(page);
			
			return "list";
		}
		
		
		//查看数据
		public String toview(){
			Module obj = Modeleservice.get(Module.class, model.getId());
			
			super.push(obj);
			return "toview";
		}
		
		//跳到新增页面
		public String tocreate(){

			/*List<Module> find = Modeleservice.find("from Module ", Module.class, null);

			super.put("ModuleList", find);*/

			return "tocreate";
		}

		//实现保存操作
		public String insert(){
			Modeleservice.saveOrUpdate(model);
			
			return "alist";
		}

		//跳到修改页面
		public String toupdate(){
			Module Module = Modeleservice.get(Module.class, model.getId());

			super.push(Module);
			
			return "toupdate";
		}
		
		public String delete(){
			String[] split = model.getId().split(", ");
			
			Modeleservice.delete(Module.class, split);
			
			return "alist";
		}
		
		public String update(){
			
			Modeleservice.saveOrUpdate(model);
			
			return "alist";
		}


	
}

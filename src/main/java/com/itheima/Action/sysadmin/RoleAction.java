package com.itheima.Action.sysadmin;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.itheima.Action.BaseAction;
import com.itheima.domain.Module;
import com.itheima.domain.Role;
import com.itheima.exception.SysException;
import com.itheima.service.ModuleService;
import com.itheima.service.Roleservice;
import com.itheima.utils.Page;
import com.opensymphony.xwork2.ModelDriven;

import jedis.jedis;
import redis.clients.jedis.Jedis;


public class RoleAction extends BaseAction implements ModelDriven<Role>{
	private Role model = new Role();
	public Role getModel() {
		return model;
	}

	private Roleservice roleservice;
	public void setroleservice(Roleservice roleservice) {
		this.roleservice = roleservice;
	}

	private ModuleService Modeleservice;
	public void setModeleservice(ModuleService modeleservice) {
		Modeleservice = modeleservice;
	}

	private String moduleIds;  //模块的id数组
	public void setModuleIds(String moduleIds) {
		this.moduleIds = moduleIds;
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
		roleservice.findPage("from Role", page, Role.class, null);
		
		//2.设置url  用于上一页   下一页   
		//page.setUrl(ServletActionContext.getRequest().getContextPath()+"/sysadmin/RoleAction_list");//绝对定位
		
		//http://localhost:8080/jk36_parent/sysadmin/RoleAction_list
		page.setUrl("roleAction_list");//相对定位
		
		//3.将结果放入值栈中
		super.push(page);
		
		return "list";
	}
	
	
	//查看数据
	public String toview() throws SysException{
		
		try{
		Role obj = roleservice.get(Role.class, model.getId());
		
		super.push(obj);
		}catch(Exception e){
			e.printStackTrace();
			throw new SysException("使用方法错误");
			
		}
		
		return "toview";
	}
	
	//跳到新增页面
	public String tocreate(){

		/*List<Role> find = roleservice.find("from Role ", Role.class, null);

		super.put("RoleList", find);*/

		return "tocreate";
	}

	//实现保存操作
	public String insert(){
		roleservice.saveOrUpdate(model);
		
		return "alist";
	}

	//跳到修改页面
	public String toupdate(){
		Role role = roleservice.get(Role.class, model.getId());

		super.push(role);
		
		return "toupdate";
	}
	
	public String delete(){
		String[] split = model.getId().split(", ");
		
		roleservice.delete(Role.class, split);
		
		return "alist";
	}
	
	public String update(){
		
		roleservice.saveOrUpdate(model);
		
		return "alist";
	}

	
	/**
     * 为了加载zTree树上的结点
     * zTree树上结点的数据结构：
     *      [{"id":"编号","pId":"父结点的编号","name":"结点名称","checked":"true|false"},{"id":"编号","pId":"父结点的编号","name":"结点名称","checked":"true|false"}]
     *      
     * 使用什么技术来生成这样json串
     *      JSON-lib    FastJSON    GSON     自己手动拼接
     *      
     * 如何将这个JSON串传递给客户端
     * HttpServletResponse 
     *           getWrite()
     *     
     *   测试地址：
     *   http://localhost:8080/jk36_parent/sysadmin/roleAction_loadJSONTreeNodes.action?id=4028a1c34ec2e5c8014ec2ebf8430001
     */
	//进入权限界面
	public String tomodule(){
		//1.根据角色id,得到角色对象
		Role role = roleservice.get(Role.class, model.getId());
		//2.将角色对象放入值栈中
		super.push(role);
		
		return "tomodule";
	}
	//加载树资源
	public String loadJSONTreeNodes() throws IOException{
		//1.根据角色 id加载角色对象
		Role role = roleservice.get(Role.class, model.getId());
		
		//2.对象导航，得到当前这个角色所具有的模块列表
		Set<Module> modules = role.getModules();
		
		//3.加载数据库中所有的模块列表
		List<Module> find = Modeleservice.find("from Module where state=1", Module.class, null);
		
		//4.拼接JSON串
		jedis km = new jedis();
	    Jedis jedis = km.getJedis();
	    String string = jedis.get("zTree"+model.getId());
	    
	    if(string==null||string.equals("")){
	    	
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		int size = find.size();//得到集合中元素的个数
		for (Module module : find) {
			size--;
			//遍历出每个模块
			/*sb.append("{\"id\":\"").append(module.getId());
			sb.append("\",\"pId\":\"").append(module.getParentId());
			sb.append("\",\"name\":\"").append(module.getName());
    		sb.append("\",\"checked\":\"");*/
			sb.append("{\"id\":\"").append(module.getId());
    		sb.append("\",\"pId\":\"").append(module.getParentId());
    		sb.append("\",\"name\":\"").append(module.getName());
    		sb.append("\",\"checked\":\"");
			
			if(modules.contains(module)){
				//当前用户具有的这个模块
				sb.append("true");
			}else{
			  	sb.append("false");
			}
			sb.append("\"}");
			
			if(size>0){
				sb.append(","); //当size=0时，说明集合中没有其它元素要遍历了，此时的元素是最后一个元素，后面不能添加逗号了
			}
		}
		sb.append("]");
		//输出
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=UTF-8");//json数据的mime类型：application/json
		response.setHeader("Cache-Control", "no-cache");//设置响应消息头，没有缓存
		response.getWriter().write(sb.toString());
		jedis.set("zTree"+model.getId(), sb.toString());
		System.out.println("正常取的数据");
	    }else{
	    	String string2 = jedis.get("zTree"+model.getId());
	    	HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json;charset=UTF-8");//json数据的mime类型：application/json
			response.setHeader("Cache-Control", "no-cache");//设置响应消息头，没有缓存
			response.getWriter().write(string2);
			System.out.println("从缓存取的数据");
	    }
		return NONE;
	}
	
  /**
    * 实现模块分配   也就是实现保存操作
    * <input type="hidden" name="id" value="${id}"/>
	<input type="hidden" id="moduleIds" name="moduleIds" value="" />
    */
	public String module(){
		//1.根据角色id,得到角色对象
		Role role = roleservice.get(Role.class, model.getId());
		//2.切割模块的id字符串
		String[] split = moduleIds.split(",");
		//创建一个模块列表的集合
		Set<Module> set = new HashSet<Module>();
		
		for (String id : split) {
			Module module = Modeleservice.get(Module.class, id);
			set.add(module);//将加载出来的模块对象，放入模块列表中
		}
		//3.设置角色与模块的关系
		role.setModules(set);
		//4.保存角色与模块的关系
		roleservice.saveOrUpdate(role);
		
		//5.同时还要清理缓存
    	String key = "zTree"+model.getId();
    	
    	//获取jedis客户端
    	jedis km = new jedis();
	    Jedis jedis = km.getJedis();
	    
    	jedis.del(key);//删除旧的key-value
		
		
		
		
		//5.跳页面
		return "alist";
	}
	
	
	
	
	
	
	
}

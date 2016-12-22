package com.itheima.Action.cargo;

import com.itheima.Action.BaseAction;
import com.itheima.domain.PackingList;
import com.itheima.service.PackingListService;
import com.itheima.utils.Page;
import com.opensymphony.xwork2.ModelDriven;


/**
 * @Description:	PackingListService接口
 * @Author:			传智播客 java学院	传智宋江
 * @Company:		http://java.itcast.cn
 * @CreateDate:		2016-12-16 10:36:02
 */

public class PackingListAction extends BaseAction implements ModelDriven<PackingList> {
	//注入service
	private PackingListService packingListService;
	public void setPackingListService(PackingListService packingListService) {
		this.packingListService = packingListService;
	}
	
	//model驱动
	private PackingList model = new PackingList();
	public PackingList getModel() {
		return this.model;
	}
	
	//作为属性驱动，接收并封装页面参数
	private Page page = new Page();			//封装页面的参数，主要当前页参数
	public void setPage(Page page) {
		this.page = page;
	}
	public Page getPage() {
		return page;
	}



	//列表展示
	public String list(){
		String hql = "from PackingList";			//查询所有内容
		//给页面提供分页数据
		page.setUrl("packingListAction_list");		//配置分页按钮的转向链接
		page = packingListService.findPage(hql, page, PackingList.class, null);
		super.push(page);
		
		return "plist";						//page list
	}
	
	//转向新增页面
	public String tocreate(){
		//准备数据
	
		
		return "pcreate";
	}
	
	//新增保存
	public String insert(){
		packingListService.saveOrUpdate(model);
		
		return "alist";			//返回列表，重定向action_list
	}

	//转向修改页面
	public String toupdate(){
		//准备数据
		PackingList obj = packingListService.get(PackingList.class, model.getId());
		
		super.push(obj);//放入栈顶
		
		return "pupdate";
	}
	
	//修改保存
	public String update(){
		PackingList packingList = packingListService.get(PackingList.class, model.getId());
		
		//设置修改的属性，根据业务去掉自动生成多余的属性

		packingList.setSeller(model.getSeller());
		packingList.setBuyer(model.getBuyer());
		packingList.setInvoiceNo(model.getInvoiceNo());
		packingList.setInvoiceDate(model.getInvoiceDate());
		packingList.setMarks(model.getMarks());
		packingList.setDescriptions(model.getDescriptions());



		
		packingListService.saveOrUpdate(packingList);
		
		return "alist";
	}
	
	//删除一条
	public String deleteById(){
		packingListService.deleteById(PackingList.class, model.getId());
		
		return "alist";
	}
	
	
	//删除多条
	public String delete(){
		packingListService.delete(PackingList.class, model.getId().split(", "));
		
		return "alist";
	}
	
	//查看
	public String toview(){
		PackingList obj = packingListService.get(PackingList.class, model.getId());
		super.push(obj);
		
		return "pview";			//转向查看页面
	}
}

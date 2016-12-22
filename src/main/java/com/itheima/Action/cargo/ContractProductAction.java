package com.itheima.Action.cargo;
import java.util.List;
import com.itheima.Action.BaseAction;
import com.itheima.domain.ContractProduct;
import com.itheima.domain.Factory;
import com.itheima.service.ContractProductService;
import com.itheima.service.FactoryService;
import com.itheima.utils.Page;
import com.opensymphony.xwork2.ModelDriven;

public class ContractProductAction extends BaseAction implements ModelDriven<ContractProduct>{
	private ContractProduct model = new ContractProduct();
	public ContractProduct getModel() {
		return model;
	}
	
	private ContractProductService contractProductService;
	public void setContractProductService(ContractProductService contractProductService) {
		this.contractProductService = contractProductService;
	}
	private FactoryService factoryService;
	public void setFactoryService(FactoryService factoryService) {
		this.factoryService = factoryService;
	}

	//分页组件
	private Page page = new Page();
	public void setPage(Page page) {
		this.page = page;
	}
	public Page getPage() {
		return page;
	}
	
	//分页查询
	public String tocreate(){
		// 1.加载所有生产货物的厂家（并且该厂家与我们公司还要有正常的合作关系 ）
		/*List<Factory> find = factoryService.find("from Factory where ctype='货物' and state=1", Factory.class, null);
		super.put("factoryList", find);
		*/
		String hql = "from Factory where ctype='货物' and state=1";
		 List<Factory> find = factoryService.find(hql, Factory.class, null);
		super.put("factoryList", find);
		
		// 2.分页查询出货物列表
//	    contractProductService.findPage("from ContractProduct where contract.id=?", page, ContractProduct.class,
//		new String[]{model.getContract().getId()});
		 /* System.out.println(model.getContract().getId());*/
		contractProductService.findPage("from ContractProduct where contract.id=?", page, ContractProduct.class,
		new String[] {model.getContract().getId() });//查询当前购销合同下的货物列表
		
	    page.setUrl("contractProductAction_tocreate");
		
		super.push(page);
		
		return "tocreate";
	}
	
	//实现保存货物的操作
	public String insert(){
		
		contractProductService.saveOrUpdate(model);
		
		return tocreate();
	}
	//进入修改列表
	public String toupdate(){
		// 1.加载要回显的数据
		ContractProduct product = contractProductService.get(ContractProduct.class, model.getId());
		super.push(product);
		
		//2.加载生产厂家列表
		List<Factory> find = factoryService.find("from Factory where ctype='货物' and state=1",Factory.class, null);           
		
		super.put("factoryList", find);
		
		return "toupdate";
	}
	
	public String update(){
		
		ContractProduct obj = contractProductService.get(ContractProduct.class, model.getId());
		
		// 2.设置要修改的属性
		obj.setFactory(model.getFactory());
		obj.setFactoryName(model.getFactoryName());
		obj.setCnumber(model.getCnumber());
		obj.setProductNo(model.getProductNo());
		obj.setProductImage(model.getProductImage());
		obj.setPackingUnit(model.getPackingUnit());
		obj.setLoadingRate(model.getLoadingRate());
		obj.setBoxNum(model.getBoxNum());
		obj.setPrice(model.getPrice());
		obj.setOrderNo(model.getOrderNo());
		obj.setProductDesc(model.getProductDesc());
		obj.setProductRequest(model.getProductRequest());
		
		
		contractProductService.saveOrUpdate(obj);
		
		
		return tocreate();
	}
	
	
	public String delete(){
		
		contractProductService.deleteById(ContractProduct.class, model.getId());
		
		return tocreate();
	}
		
	
}

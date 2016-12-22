package com.itheima.Action.cargo;
import java.util.HashSet;
import java.util.Set;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.alibaba.fastjson.JSON;
import com.itheima.Action.BaseAction;
import com.itheima.domain.Contract;
import com.itheima.domain.Export;
import com.itheima.domain.ExportProduct;
import com.itheima.domain.User;
import com.itheima.service.Cargoservice;
import com.itheima.service.ExportProductservice;
import com.itheima.service.Exportservice;
import com.itheima.utils.Page;
import com.itheima.utils.UtilFuns;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.export.webservice.EpService;
import cn.itcast.export.webservice.Exception_Exception;

public class ExportAction extends BaseAction implements ModelDriven<Export>{
	private Export model = new Export();
	@Override
	public Export getModel() {
		return model;
	}
	
	private Exportservice exportservice;
	public void setExportservice(Exportservice exportservice) {
		this.exportservice = exportservice;
	}

	private Cargoservice cargoservice;
	public void setCargoservice(Cargoservice cargoservice) {
		this.cargoservice = cargoservice;
	}

	private ExportProductservice exportProductservice;
	public void setExportProductservice(ExportProductservice exportProductservice) {
		this.exportProductservice = exportProductservice;
	}

	//分页组件
	private Page page = new Page();
	public void setPage(Page page) {
		this.page = page;
	}
	public Page getPage() {
		return page;
	}
	
	
	private String[] mr_id;
    private String[] mr_changed;
    private Integer[] mr_cnumber;
    private Double[] mr_grossWeight;
    private Double[] mr_netWeight;
    private Double[] mr_sizeLength;
    private Double[] mr_sizeWidth;
    private Double[] mr_sizeHeight;
    private Double[] mr_exPrice;
    private Double[] mr_tax;
	public void setMr_id(String[] mr_id) {
		this.mr_id = mr_id;
	}
	public void setMr_changed(String[] mr_changed) {
		this.mr_changed = mr_changed;
	}
	public void setMr_cnumber(Integer[] mr_cnumber) {
		this.mr_cnumber = mr_cnumber;
	}
	public void setMr_grossWeight(Double[] mr_grossWeight) {
		this.mr_grossWeight = mr_grossWeight;
	}
	public void setMr_netWeight(Double[] mr_netWeight) {
		this.mr_netWeight = mr_netWeight;
	}
	public void setMr_sizeLength(Double[] mr_sizeLength) {
		this.mr_sizeLength = mr_sizeLength;
	}
	public void setMr_sizeWidth(Double[] mr_sizeWidth) {
		this.mr_sizeWidth = mr_sizeWidth;
	}
	public void setMr_sizeHeight(Double[] mr_sizeHeight) {
		this.mr_sizeHeight = mr_sizeHeight;
	}
	public void setMr_exPrice(Double[] mr_exPrice) {
		this.mr_exPrice = mr_exPrice;
	}
	public void setMr_tax(Double[] mr_tax) {
		this.mr_tax = mr_tax;
	}
	
	
	
	
	
	//进入合同列表
	public String list() throws Exception {
		// 1.调用业务方法，实现分页查询
		exportservice.findPage("from Export ", page, Export.class, null);

		// 2.设置url 用于上一页 下一页
		// page.setUrl(ServletActionContext.getRequest().getContextPath()+"/sysadmin/deptAction_list");//绝对定位

		// http://localhost:8080/jk36_parent/sysadmin/deptAction_list
		page.setUrl("exportAction_list");// 相对定位

		// 3.将结果放入值栈中
		//super.push(page);
		super.push(page);
		return "list";
	}
	//进入合同管理
	public String contractList(){
		//1.调用业务方法，实现分页查询
		cargoservice.findPage("from Contract", page, Contract.class, null);
		
		//2.设置url  用于上一页   下一页   
		//page.setUrl(ServletActionContext.getRequest().getContextPath()+"/sysadmin/ContractAction_list");//绝对定位
		
		//http://localhost:8080/jk36_parent/sysadmin/ContractAction_list
		page.setUrl("exportAction_contractList");//相对定位
		
		//3.将结果放入值栈中
		super.push(page);
		
		return "contractList";
	}
	
	//进入报运的方法
	public String tocreate(){
		
		return "tocreate";
	}
	
	//实现新增操作
	public String insert(){
		User user = super.getContract();
		model.setCreateBy(user.getId());
		model.setCreateDept(user.getDept().getId());
//		model.setState(0);
		exportservice.saveOrUpdate(model);
		return "update";
	}
	
	
	//进入查看页面
	public String toview(){
		Export export = exportservice.get(Export.class, model.getId());
		super.push(export);
		
		return "toview";
	}
	//跳转修改页面
	public String toupdate(){
		Export export = exportservice.get(Export.class, model.getId());
		super.push(export);
		//3.要将商品列表查询出来，并形成函数串
		Set<ExportProduct> set = export.getExportProducts();
		//遍历商品列表
		StringBuilder sb = new StringBuilder();
		for (ExportProduct ep : set) {
			//得到每个商品对象
    		sb.append("addTRRecord(\"mRecordTable\", \"").append(ep.getId());
    		sb.append("\", \"").append(ep.getProductNo());
    		sb.append("\", \"").append(UtilFuns.convertNull(ep.getCnumber()));      //这个是为了不让出现空值                           
    		sb.append("\", \"").append(UtilFuns.convertNull(ep.getGrossWeight()));
    		sb.append("\", \"").append(UtilFuns.convertNull(ep.getNetWeight()));
    		sb.append("\", \"").append(UtilFuns.convertNull(ep.getSizeLength()));
    		sb.append("\", \"").append(UtilFuns.convertNull(ep.getSizeWidth()));
    		sb.append("\", \"").append(UtilFuns.convertNull(ep.getSizeHeight()));
    		sb.append("\", \"").append(UtilFuns.convertNull(ep.getExPrice()));
    		sb.append("\", \"").append(UtilFuns.convertNull(ep.getTax())).append("\");");
		}
		super.put("mRecordData", sb.toString());
		
		
		return "toupdate";
	}
	//实现修改操作
	public String update(){
		
	 	//修改商品列表
    	//1.遍历mr_id数组    存放了商品的id
	Set<ExportProduct> epSet = new HashSet<ExportProduct>();
	for (int i = 0; i < mr_id.length; i++) {
		ExportProduct ep = exportProductservice.get(ExportProduct.class, mr_id[i]);//得到商品对象   
		if("1".equals(mr_changed[i])){                                    
		//说明当前行的值有改变
		ep.setCnumber(mr_cnumber[i]);
		ep.setGrossWeight(mr_grossWeight[i]);
		ep.setNetWeight(mr_netWeight[i]);
		ep.setSizeLength(mr_sizeLength[i]);
		ep.setSizeWidth(mr_sizeWidth[i]);
		ep.setSizeHeight(mr_sizeHeight[i]);
		ep.setExPrice(mr_exPrice[i]);
		ep.setTax(mr_tax[i]);
		}
		epSet.add(ep);
	}
	//报运单与商品   一对多
	model.setExportProducts(epSet);
	//3.保存更新后的结果
	exportservice.saveOrUpdate(model);
		return "update";
	}
	
	public String delete(){
		
		exportservice.deleteById(Export.class, model.getId());
		
		return "update";
	}
	
	//提交方法
	public String submit(){
		//Export export = exportservice.get(Export.class,model.getId() );
		String id = model.getId();
		String[] split = id.split(", ");
		for (String ids : split) {
			Export export = exportservice.get(Export.class, ids);
			export.setState(1);
			exportservice.saveOrUpdate(export);
		}
		
		
		return "update";
	}
	
	//取消的方法
	public String cancel(){
		String id = model.getId();
		String[] split = id.split(", ");
		for (String ids : split) {
			Export export = exportservice.get(Export.class, ids);
			export.setState(0);
			exportservice.saveOrUpdate(export);
		}
		
		return "update";
	}
	
	
	//电子报运
	public String DanZiBaoYun() throws Exception_Exception{
		//1.根据选中的报运单的id,得到报运单对象
		Export export = exportservice.get(Export.class, model.getId());
		//2.转json(这个json串要符合webservice服务端的要求)
		String string = JSON.toJSONString(export);
		System.out.println(string);
		//3.调用webservice服务端提供的方法，实现电子报运
		String exportE = epService.exportE(string);
		System.out.println(exportE);
		//4.将webservice服务端返回的结果，去更新oracle数据库中的记录
    	//4.1要将返回的json结果 ，转化为对象
		Export export2 = JSON.parseObject(exportE,Export.class);
		//4.2调用业务方法，实现报运结果的更新
		exportservice.updateExportE(export2);
		
		
		return "update";
	}
	
	private  EpService epService;
	public void setEpService(EpService epService) {
		this.epService = epService;
	}

	
    
}

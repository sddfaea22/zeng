package com.itheima.Action.cargo;

import java.util.List;

import com.itheima.Action.BaseAction;
import com.itheima.domain.ExtCproduct;
import com.itheima.domain.Factory;
import com.itheima.exception.SysException;
import com.itheima.service.ExtCproductService;
import com.itheima.service.FactoryService;
import com.itheima.utils.Page;
import com.opensymphony.xwork2.ModelDriven;



public class ExtCproductAction extends BaseAction implements ModelDriven<ExtCproduct>{
    private ExtCproduct model = new ExtCproduct();
	public ExtCproduct getModel() {
		return model;
	}
	
	// 分页组件
		private Page page = new Page();

		public void setPage(Page page) {
			this.page = page;
		}

		public Page getPage() {
			return page;
		}

		// 注入业务逻辑
		private ExtCproductService extCproductService;
		public void setExtCproductService(ExtCproductService extCproductService) {
			this.extCproductService = extCproductService;
		}

		private FactoryService factoryService;
		public void setFactoryService(FactoryService factoryService) {
			this.factoryService = factoryService;
		}

		/**
		 * 分页查询
		 * 
		 * @return
		 * @throws Exception
		 */
		public String list() throws Exception {
			// 1.调用业务方法，实现分页查询
			extCproductService.findPage("from ExtCproduct", page, ExtCproduct.class, null);

			// 2.设置url 用于上一页 下一页
			// page.setUrl(ServletActionContext.getRequest().getContextPath()+"/sysadmin/deptAction_list");//绝对定位

			// http://localhost:8080/jk36_parent/sysadmin/deptAction_list
			page.setUrl("extCproductAction_list");// 相对定位

			// 3.将结果放入值栈中
			super.push(page);

			return "list";
		}

		/**
		 * 查看详情 <input type="checkbox" name="id" value="${dept.id }"/> model对象
		 * Dept类型 id属性：
		 * 
		 * @return
		 * @throws Exception
		 */
		public String toview() throws Exception {
			try {
				// 1.调用业务方法，加载部门对象
				ExtCproduct obj = extCproductService.get(ExtCproduct.class, model.getId());

				// 2.将对象放入值栈中
				super.push(obj);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SysException("你有病 ，要先选中，再查看！");
			}

			// 3.跳页面
			return "toview";
		}

		/**
		 * 进入新增界面
		 * ?contract.id=xxxxx
		 * model
		 *    contract属性（对象）
		 *       id属性
		 */
		public String FuJian() throws Exception {
			// 1.加载所有生产附件的厂家（并且该厂家与我们公司还要有正常的合作关系 ）
			String hql = "from Factory where ctype='附件' and state=1";
			List<Factory> factoryList = factoryService.find(hql, Factory.class, null);

			super.put("factoryList", factoryList);

			// 2.分页查询出附件列表
			extCproductService.findPage("from ExtCproduct where contractProduct.id=?", page, ExtCproduct.class,
					new String[] {model.getContractProduct().getId() });//查询当前购销合同下的货物列表
			
			page.setUrl("extCproductAction_FuJian");
			
			super.push(page);//放入栈顶

			// 3.跳页面
			return "tocreate";
		}

		/**
		 * 实现新增操作 model deptName: parent.id:
		 */
		public String insert() throws Exception {
			// 1.调用业务方法，实现保存
			extCproductService.saveOrUpdate(model);

			// 3.跳页面
			return FuJian();
		}

		/**
		 * 进入修改界面
		 * 
		 * @return
		 * @throws Exception
		 */
		public String toupdate() throws Exception {
			// 1.加载要回显的数据
			ExtCproduct obj = extCproductService.get(ExtCproduct.class, model.getId());

			// 2.放入值栈中
			super.push(obj);
			
			//3.加载生产厂家列表
			String hql = "from Factory where ctype='附件' and state=1";
			List<Factory> factoryList = factoryService.find(hql, Factory.class, null);

			super.put("factoryList", factoryList);

			// 5.跳页面
			return "toupdate";
		}

		/**
		 * 更新操作
		 */
		public String update() throws Exception {
			// 1.根据id,加载原有的部门对象
			ExtCproduct obj = extCproductService.get(ExtCproduct.class, model.getId());

			// 2.设置要修改的属性
			obj.setFactory(model.getFactory());
			obj.setFactoryName(model.getFactoryName());
			obj.setCnumber(model.getCnumber());
			obj.setProductNo(model.getProductNo());
			obj.setProductImage(model.getProductImage());
			obj.setPackingUnit(model.getPackingUnit());
			obj.setPrice(model.getPrice());
			obj.setOrderNo(model.getOrderNo());
			obj.setProductDesc(model.getProductDesc());
			obj.setProductRequest(model.getProductRequest());
	         
			// 3.保存更新后的结果
			extCproductService.saveOrUpdate(obj);

			// 4.跳页面
			return FuJian();
		}

		/**
		 * 删除
		 * ?id=${o.id}&contractProduct.id=${o.contractProduct.id}&contractProduct.contract.id=${contractProduct.contract.id}">[删除]</a>
		 * model 
		 *     id:${o.id}
		 *     contractProduct
		 *                 id:${o.contractProduct.id}
		 *                 contract
		 *                       id:${contractProduct.contract.id}
		 * 
		 */
		public String delete() throws Exception {
			// 2.调用业务方法，实现删除操作
			extCproductService.delete(ExtCproduct.class, model);

			// 4.跳页面
			return FuJian();
		}

}

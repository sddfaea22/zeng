package com.itheima.job;
import java.util.Date;
import java.util.List;

import com.itheima.Action.BaseAction;
import com.itheima.domain.Contract;
import com.itheima.service.Cargoservice;
import com.itheima.utils.MailUtil;
//定时任务    (发送邮件)
public class JobTest extends BaseAction{
		
	
	private Cargoservice cargoservice;
	public void setCargoservice(Cargoservice cargoservice) {
		this.cargoservice = cargoservice;
	}



	public String zengshuang() throws Exception{
		/*ContractAction bean = new ContractAction();
		List<Contract> list = bean.zengshaung2();*/
//		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//		WebApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
//		Deptserviceimpl bean = (Deptserviceimpl) app.getBean("deptService");
//		List<Contract> list = bean.zengshuang2();
		List<Contract> list = cargoservice.find("from Contract", Contract.class, null);
		
		String email=" 主人您好，今天有您的交期，请您提前做好准备！购销合同号：";
		for (Contract en : list) {
			Date date = new Date();
			Date date2 = en.getDeliveryPeriod();
			if(date==date2){
				MailUtil.sendMsg("mang@itheima36.com","购销合同到期", email+en.getContractNo());
				System.out.println("email发送完成");
			}
		}
		return NONE;
		
	}
	
}






package com.itheima.Action;

import com.opensymphony.xwork2.ActionSupport;

public class HomeAction extends ActionSupport{
	private String moduleName;		//动态指定跳转的模块，在struts.xml中配置动态的result
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	

	public String title(){
		
		return "title";
	}
	
	public String toleft(){
		
		return "toleft";
	}
	
	public String tomain(){
		
		return "tomain";
	}
	
	
	
	
	
}

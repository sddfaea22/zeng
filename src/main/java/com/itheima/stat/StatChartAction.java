package com.itheima.stat;
import java.util.List;
import com.itheima.Action.BaseAction;
import com.itheima.dao.impl.SqlDao;

//图形报表
public class StatChartAction extends BaseAction {
	
	private SqlDao sqlDao;
	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}
	
	/*public String factorySale() throws FileNotFoundException{
		//1.统计出数据库中结果信息
		String sql ="select factory_name,sum(amount) from Contract_Product_c group by factory_name order by sum(amount) desc";        
		List<String> list = sqlDao.executeSQL(sql);
		//2.将这个结果拼接成符合data.xml要求的字符串
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<pie>");
		for(int i=0;i<list.size();i++){
			sb.append("<slice title=\""+list.get(i++)+"\" pull_out=\"true\">"+list.get(i)+"</slice>");
		}
		sb.append("</pie>");
		
		String content = sb.toString();
		
		//3.将这个符合要求的字符串写入data.xml文件中
		FileUtil util = new FileUtil();
		String path = ServletActionContext.getServletContext().getRealPath("/");
		util.createTxt(path, "stat\\chart\\factorysale\\data.xml",content,"UTF-8");
		
		return "factorySale";
	}*/
	
	public String factorysale(){
		
		//1.统计出数据库中结果信息
		String sql ="select factory_name,sum(amount) from Contract_Product_c group by factory_name order by sum(amount) desc";
		List<String> dataList = sqlDao.executeSQL(sql);
		
		String colors[]={"#FF0F00","#FF6600","#FCD202", "#F8FF01"};
		//2.组织json数据
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int j=0;
		for(int i=0;i<dataList.size();i++){
			sb.append("{")
			   .append("\"factoryName\": \""+dataList.get(i)+"\",")
			   .append("\"saleAmount\": "+dataList.get(++i)+",")
			   .append("\"color\": \""+colors[j]+"\"").append("},");
			
			j++;
			if(j>3){
				j=0;
			}
		}
		sb.delete(sb.length()-1, sb.length());
		sb.append("]");
		//3.放入值栈
		super.put("jsonData", sb.toString());
		
		
		return "factorySale";
	}
	
	//产品销售额   柱状图
	public String productsale(){
		//1.统计出数据库中结果信息
		String sql ="select * from (select product_no,sum(amount) from Contract_Product_c group by product_no order by sum(amount) desc) saletable where rownum<16";
		List<String> dataList = sqlDao.executeSQL(sql);
		
		
		//2.组织json数据
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < dataList.size(); i++) {
			sb.append("{").append("\"country\": \""+dataList.get(i)+"\",")
			.append("\"visits\": "+dataList.get(++i)+"").append("},");
			
		}
		sb.delete(sb.length()-1, sb.length());
		sb.append("]");
		
		//3.放入值栈
		super.put("jsonData2", sb.toString());
		return "productsale";
	}
	//系统访问压力图
	public String onlineinfo(){
		//1.统计出数据库中结果信息
		String sql ="select a.a1,nvl(b.logincount,0) from  (select * from online_info_t) a left join (select to_char(login_time,'hh24') a1, count(*) logincount from login_log_p group by to_char(login_time,'hh24') ) b  on (a.a1 = b.a1) order by a.a1";
		List<String> dataList = sqlDao.executeSQL(sql);
		
		
		//2.组织json数据
		StringBuilder sb = new StringBuilder();
		int jj=0;
		String[] DMA={"#FF0F00","#FF6600","#FF9E01","#FCD202","#F8FF01"};
		sb.append("[");
		for (int i = 0; i < dataList.size(); i++) {
			sb.append("{").append("\"country\": \""+dataList.get(i)+"\",")
			.append("\"litres\": "+dataList.get(++i)+",")
			.append("\"short\": \""+DMA[jj]+"\"")
			.append("},");
			jj++;
			if(jj>4){
				jj=0;
			}
		}
		sb.delete(sb.length()-1, sb.length());
		sb.append("]");
		
		//3.放入值栈
		super.put("jsonData3", sb.toString());
				
		return "onlineinfo";
	}
	
}

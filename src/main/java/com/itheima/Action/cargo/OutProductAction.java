package com.itheima.Action.cargo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import com.itheima.Action.BaseAction;
import com.itheima.domain.ContractProduct;
import com.itheima.service.ContractProductService;
import com.itheima.utils.DownloadUtil;
import com.itheima.utils.UtilFuns;
//POI报表
public class OutProductAction extends BaseAction{
	private String inputDate;
	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
	
	//注入Service
	private ContractProductService contractProductService;
	public void setContractProductService(ContractProductService contractProductService) {
		this.contractProductService = contractProductService;
	}

	public String toedit(){
		
		return "toedit";
	}
	
	
	public String print() throws ParseException, IOException{
		//1.创建工作簿
		Workbook wb = new HSSFWorkbook();//只能操作excel 2003版本
	
		//2.创建工作表
		Sheet sheet = wb.createSheet();
		
		//定义一些公共变量
		int rowNo=0,cellNo=1;  //定义标题  cellNo=1是因为可以在左侧留一些空余的空间
		Row nRow=null;     //行
		Cell nCell =null;   //列
		
		//设置列宽   第一个参数为列的下标     第二个参数，列的宽度   setColumnWidth存在bug，后面要用别的方法
		//使用的是工作表的对象
		sheet.setColumnWidth(0, 2*256);  //都要乘以256
		sheet.setColumnWidth(1, 26*256);  //占了9行
		sheet.setColumnWidth(2, 11*256); 
		sheet.setColumnWidth(3, 29*256);  
		sheet.setColumnWidth(4, 12*256);  
		sheet.setColumnWidth(5, 15*256);  
		sheet.setColumnWidth(6, 10*256);  
		sheet.setColumnWidth(7, 10*256);  
		sheet.setColumnWidth(8, 8*256); 
		
		//3.=================================大标题======================
		nRow = sheet.createRow(rowNo++);//第一行的行对象   rowNo++就会进入第二行
		nRow.setHeightInPoints(36f);//设置行高
		
		//产生单元格对象
		nCell = nRow.createCell(cellNo);
		
		//合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
		
		//设置单元格的内容
		nCell.setCellValue(inputDate.replace("-0", "-").replace("-", "年")+"月份出货表");              
		
		//设置单元格的样式
		nCell.setCellStyle(this.bigTitle(wb));   //下面有个样式的类
		
		//4.==================================小标题===========================
		String[] titles ={"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};//列表
		//产生小标题的行对象
		nRow = sheet.createRow(rowNo);
		nRow.setHeightInPoints(26.25f);//设置行高
		for (int i = 0; i < titles.length; i++) {
			nCell = nRow.createCell(cellNo++);//产生单元格对象
			nCell.setCellValue(titles[i]);//设置单元格的内容 
			nCell.setCellStyle(this.title(wb));//设置小标题的样式
		}
		
		//5.==================================内容 ==============================
		String hql="from ContractProduct where to_char(contract.shipTime,'yyyy-MM')='"+inputDate+"'";
		List<ContractProduct> find = contractProductService.find(hql,ContractProduct.class , null);
		
		//遍历货物列表
		for (ContractProduct cp : find) {
			nRow = sheet.createRow(rowNo++);//产生行对象
			nRow.setHeightInPoints(24f);//设置行高
			
			cellNo=1;
			
			nCell = nRow.createCell(cellNo++);//产生单元格对象
			nCell.setCellValue(cp.getContract().getCustomName());//单元格的内容 
			nCell.setCellStyle(this.text(wb));//设置样式
			
			//订单号
			nCell = nRow.createCell(cellNo++);//产生单元格对象
			nCell.setCellValue(cp.getOrderNo());//单元格的内容 
			nCell.setCellStyle(this.text(wb));//设置样式
			
			//货号
			nCell = nRow.createCell(cellNo++);//产生单元格对象
			nCell.setCellValue(cp.getProductNo());//单元格的内容 
			nCell.setCellStyle(this.text(wb));//设置样式
			
			//数量
			nCell = nRow.createCell(cellNo++);//产生单元格对象
			nCell.setCellValue(cp.getCnumber());//单元格的内容 
			nCell.setCellStyle(this.text(wb));//设置样式
			
			//工厂
			nCell = nRow.createCell(cellNo++);//产生单元格对象
			nCell.setCellValue(cp.getFactoryName());//单元格的内容 
			nCell.setCellStyle(this.text(wb));//设置样式
			
			//工厂交期
			nCell = nRow.createCell(cellNo++);//产生单元格对象
			nCell.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getDeliveryPeriod()));//单元格的内容 
			nCell.setCellStyle(this.text(wb));//设置样式
			
			//船期
			nCell = nRow.createCell(cellNo++);//产生单元格对象
			nCell.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getShipTime()));//单元格的内容 
			nCell.setCellStyle(this.text(wb));//设置样式
			
			//贸易条款
			nCell = nRow.createCell(cellNo++);//产生单元格对象
			nCell.setCellValue(cp.getContract().getTradeTerms());//单元格的内容 
			nCell.setCellStyle(this.text(wb));//设置样式
				
		}
		
		//6.==============================实现文件下载========================
		DownloadUtil down = new DownloadUtil();
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();//缓存
		wb.write(stream);//将工作簿的内容全部输出到缓存
		stream.close();//刷新后，缓存中的数据是最新的
		
		HttpServletResponse response = ServletActionContext.getResponse();//得到response对象
		
		down.download(stream, response, "出货表.xls");
		
		return NONE;
	}
	
	
	//大标题的样式
		public CellStyle bigTitle(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("宋体");
			font.setFontHeightInPoints((short)16);
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);					//字体加粗
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
			
			return style;
		}
		//小标题的样式
		public CellStyle title(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("黑体");
			font.setFontHeightInPoints((short)12);
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
			
			style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
			style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
			style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
			style.setBorderRight(CellStyle.BORDER_THIN);				//右细线
			
			return style;
		}
		
		//文字样式
		public CellStyle text(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short)10);
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_LEFT);					//横向居左
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
			
			style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
			style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
			style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
			style.setBorderRight(CellStyle.BORDER_THIN);				//右细线
			
			return style;
		}
	
	
}
	
	
	
	
	
	
	
		



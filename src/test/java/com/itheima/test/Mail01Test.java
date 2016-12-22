package com.itheima.test;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;

public class Mail01Test {

	@Test
	public void testJavaMail() throws Exception{
		//1.设置邮件的一些信息
		Properties props = new Properties();
		//发送邮件的服务器地址
		props.put("mail.smtp.host", "smtp.163.com");//  stmp.qq.com   smtp.sina.com
		props.put("mail.smtp.auth", "true");
		
		//2.创建Session对象
		Session session =Session.getInstance(props);
		
		//3.创建出MimeMessage，邮件的消息对象
		MimeMessage message = new MimeMessage(session);
		
		//4.设置发件人
		Address fromAddr = new InternetAddress("m18701381836_1@163.com");
		message.setFrom(fromAddr);
		
		//5.设置收件人
		Address toAddr = new InternetAddress("2752001489@qq.com");
		message.setRecipient(RecipientType.TO, toAddr);
		
		//6.设置邮件的主题
		message.setSubject("项目进展顺序");
		
		//7.设置邮件的正文
		message.setText("项目进展顺序，所有兄弟们都非常努力，老板今天可以请吃饭");
		message.saveChanges();//保存更新
		
		//8.得到火箭
		Transport transport = session.getTransport("smtp");
		
		transport.connect("smtp.163.com", "m18701381836_1@163.com", "wasd123");//设置了火箭的发射地址
									   	//这是发件人				//iamsorry是密码
		transport.sendMessage(message, message.getAllRecipients());//发送具体内容及接收人
		
		transport.close();
		
	}
}

package com.itheima.test;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class Mail03Test {

	@Test
	public void testJavaMail() throws Exception{
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-mail.xml");
		
		//得到发送器
		JavaMailSender mailSender = (JavaMailSender) ac.getBean("mailSender");
		
		//得到一个MimeMessage对象
		MimeMessage message = mailSender.createMimeMessage();
		
		//产生出一个MimeMessageHelper helper 
		MimeMessageHelper helper = new MimeMessageHelper(message, true);//工具类本质是操作message消息   true代表可以带附件，图片
		
		//3.使用helper工具类，设置邮件的发送者，接收者，主题，正文
		helper.setFrom("zengshuang@itheima36.com");   //发送者
		helper.setTo("mang@itheima36.com");         //接收者
		helper.setSubject("发送图片和附件");        //标题
		helper.setText("<html><head></head><body><h1>本人是一个18岁的小姑娘，相约回龙观东大街</h1><a href=http://www.baidu.com>baidu</a><img src='cid:image' /></body></html>", true);
		//正文
		
		//指定cid的取值
		FileSystemResource imgResource = new FileSystemResource(new File("H:/QQ图片20160622212304.jpg"));
		helper.addInline("image", imgResource);
		
		//带附件
		FileSystemResource fileResource = new FileSystemResource(new File("H:/Everything-1.2.1.371.zip"));
		helper.addAttachment("Everything-1.2.1.371.zip", fileResource);
		
		
		//发送
		mailSender.send(message);
		
	}
}

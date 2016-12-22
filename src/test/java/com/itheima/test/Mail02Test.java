package com.itheima.test;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
public class Mail02Test {

	@Test
	public void testJavaMail() throws Exception{
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-mail.xml");
		
		SimpleMailMessage smm = (SimpleMailMessage) ac.getBean("mailMessage");//得到了简单邮件的消息对象
		smm.setTo("2752001489@qq.com");  //收件人
		smm.setSubject("今天晚上约你");    //标题
		smm.setText("本人是一个18岁的小姑娘，相约回龙观东大街");  //内容
		
		//得到发送器
		JavaMailSender mailSender = (JavaMailSender) ac.getBean("mailSender");
		
		//发送
		mailSender.send(smm);
		
	}
}




package com.sujan.thymleafdemo.service;


import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendSimpleEmail(String name, String receiver, String password) throws AddressException, MessagingException, IOException {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(receiver);
		msg.setSubject("Thanks For Signing Up");
		String text = "Sign up completed. \n  \nWelcome, "+name+"\nEmail: "+receiver+"\nPassword: "+password+"\n Thanks.";
		msg.setText(text);
		javaMailSender.send(msg);
	}
    
}

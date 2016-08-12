package com.miguel.HYIP.helper;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.ProxyConfig;
import org.simplejavamail.mailer.config.ServerConfig;
import org.simplejavamail.mailer.config.TransportStrategy;


public class EmailManager {

	public static void main(String[] args) {
		
		
		
		Mailer mailer = new Mailer(
		        new ServerConfig("smtp.gmail.com", 587, "user", "pass"),
		        TransportStrategy.SMTP_PLAIN,
		        new ProxyConfig("proxy", 8080 , "user", "pass" )
		);

		mailer.sendMail(new EmailBuilder()
		        .from("mytest", "mytest@test.com")
		        .to("test", "user@domain.com")
		        .subject("This is the subject line")
		        .textHTML("<h1>This is the actual message</h1>")
		        .build());

		System.out.println("Message sent...");
		
		//.....................................................
		
		
	      // Recipient's email ID needs to be mentioned.
	      String to = "user@domain.com";//change accordingly

	      // Sender's email ID needs to be mentioned
	      String from = "user@domain.com";//change accordingly
	      final String username = "user";//change accordingly
	      final String password = "pass";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtp.gmail.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "587");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	      new javax.mail.Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	         }
	      });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	         InternetAddress.parse(to));

	         // Set Subject: header field
	         message.setSubject("Testing Subject");

	         // Now set the actual message
	         message.setText("Hello, this is sample for to check send "
	            + "email using JavaMailAPI ");

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	            throw new RuntimeException(e);
	      }
	   }
}

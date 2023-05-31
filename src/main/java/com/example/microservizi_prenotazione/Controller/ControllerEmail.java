package com.example.microservizi_prenotazione.Controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import com.example.microservizi_prenotazione.Entities.emailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import com.example.microservizi_prenotazione.Configuration.configurationOauth;


@Component
public class ControllerEmail {

    private Object obj;
    @Autowired
    private JavaMailSender javaMailSender;

    public ControllerEmail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }


    public void sendSimpleMessage(emailDetails details,String sender) {

        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            mimeMessage.setRecipient(Message.RecipientType.TO,
//                    new InternetAddress(to));
//            mimeMessage.setFrom(new InternetAddress("pietro.vitale23@gmail.com"));
//            mimeMessage.setText(text);
//            mimeMessage.setSubject(subject);
//            mailSender.send(mimeMessage);
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(details.getRecipient());
            msg.setText(details.getMsgBody());
            msg.setFrom("mailgratis60@gmail.com");
            msg.setSubject(details.getSubject());
            JavaMailSenderImpl mails = new JavaMailSenderImpl();
            mails.setHost("smtp.gmail.com");
            mails.setUsername("mailgratis60@gmail.com");
		    mails.setPassword("*Fantastico1818");
            Properties props = mails.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.debug", "true");
            setMailSender(mails);

            javaMailSender.send(msg);

//
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//            // Setting up necessary details
//            mailMessage.setFrom(sender);
//            mailMessage.setTo(details.getRecipient());
//            mailMessage.setText(details.getMsgBody());
//            mailMessage.setSubject(details.getSubject());
//
//            // Sending the mail
//            javaMailSender.send(mailMessage);
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


}

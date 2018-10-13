/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.ejb;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author franc
 */
@Stateless
public class MailSessionBean {

    @Asynchronous
    public void shareLoginCredentials(String username, String password, String email) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Welcome to demo revenue portal https://revenue.mobitill.com/ "
                + "\nYour login credentials are\nUsername : ")
                .append(username)
                .append("\nPassword : ").append(password);
        
        sendMail(stringBuilder.toString(), email);
    }
    @Asynchronous
    public void shareResetLoginCredentials(String username, String password, String email) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Password reset for demo revenue portal https://revenue.mobitill.com/ "
                + "\nYour login credentials are\nUsername : ")
                .append(username)
                .append("\nPassword : ").append(password);
        
        sendMail(stringBuilder.toString(), email);
    }
    public void sendMail(String content,String receipient){
        boolean success = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.checkserveridentity", "true");
        props.put("mail.smtp.ssl.enable", "true");

        try {
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("management@dataintegrated.co.ke", "management@dataintegratedpassword");
                    }
                });
            //session.setDebug(true);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("revenue.mobitill.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(receipient));
            message.setSubject("Credentials");
            message.setText(content);

            Transport.send(message);

            System.out.println("Done");
            success = true;
        } catch (Exception e) {
            Logger.getLogger("mail").log(Level.SEVERE, e.getMessage());
        }
    }
}

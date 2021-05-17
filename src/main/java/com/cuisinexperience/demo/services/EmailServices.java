package com.cuisinexperience.demo.services;

import com.cuisinexperience.demo.models.FriendshipStatus;
import com.cuisinexperience.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailServices {

    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String from;

    public void prepareAndSend(User userSenderId, User userRecipientId, FriendshipStatus status) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(userRecipientId.getEmail());
        msg.setSubject(String.valueOf(userSenderId));
        msg.setText(String.valueOf(status));

        try {
            this.emailSender.send(msg);
        } catch (MailException ex) {
            ex.printStackTrace();
        }
    }
}

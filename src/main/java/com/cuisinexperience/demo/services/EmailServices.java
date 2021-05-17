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

    public void prepareAndSend(User userSenderId, User userRecipientId) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(userRecipientId.getEmail());
        msg.setSubject("You've been friended by: " + userSenderId.getUsername());
        msg.setText("Your friendship status is pending, please log on to CuisineXperience to accept their friend request");

        System.out.println("email sent to " + userRecipientId.getUsername());

        try {
            this.emailSender.send(msg);
        } catch (MailException ex) {
            ex.printStackTrace();
        }
    }
}

package com.cuisinexperience.demo.services;

import com.cuisinexperience.demo.models.Post;
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

    public void prepareAndSend(Post post, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(post.getOwner().getEmail());
        msg.setSubject(subject);
        msg.setText(body);

        try {
            this.emailSender.send(msg);
        } catch (MailException ex) {
            ex.printStackTrace();
        }
    }
}

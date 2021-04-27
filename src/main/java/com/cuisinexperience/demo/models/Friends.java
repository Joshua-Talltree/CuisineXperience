package com.cuisinexperience.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "friends")
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_sender_id")
    private User userSenderId;

    @ManyToOne
    @JoinColumn(name = "user_recipient_id")
    private User userRecipientId;

    @Column(name = "status")
    private String status;

    public Friends() {
    }

    public Friends(User userSenderId, User userRecipientId, String status) {
        this.userSenderId = userSenderId;
        this.userRecipientId = userRecipientId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(User userSenderId) {
        this.userSenderId = userSenderId;
    }

    public User getUserRecipientId() {
        return userRecipientId;
    }

    public void setUserRecipientId(User userRecipientId) {
        this.userRecipientId = userRecipientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

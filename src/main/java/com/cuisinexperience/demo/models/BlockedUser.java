package com.cuisinexperience.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "blocked")
public class BlockedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_sender_id")
    @JsonManagedReference
    private User userSenderId;

    @ManyToOne
    @JoinColumn(name = "user_recipient_id")
    @JsonManagedReference
    private User userRecipientId;

    public BlockedUser() {
    }

    public BlockedUser(User userSenderId, User userRecipientId) {
        this.userSenderId = userSenderId;
        this.userRecipientId = userRecipientId;
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
}

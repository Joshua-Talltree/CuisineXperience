package com.cuisinexperience.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "friends")
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_sender_id")
    @JsonManagedReference
    private User userSenderId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_recipient_id")
    @JsonManagedReference
    private User userRecipientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FriendshipStatus status;


    public Friends() {
    }

    public Friends(User userSenderId, User userRecipientId, FriendshipStatus status) {
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

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
}

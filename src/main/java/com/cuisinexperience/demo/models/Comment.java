package com.cuisinexperience.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "comment", columnDefinition = "TEXT", length = 3000, nullable = false)
    private String comment;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_sent")
    private Date timeSent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User userId;

    public Comment() {
    }

    public Comment(Long postId, String comment, Date timeSent, User userId) {
        this.postId = postId;
        this.comment = comment;
        this.timeSent = timeSent;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}

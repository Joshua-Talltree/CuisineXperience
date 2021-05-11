package com.cuisinexperience.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "title", length = 225, nullable = false)
    private String title;


    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "posts_categories",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )

    private List<Categories> categories;


    @Column(name = "content", columnDefinition = "TEXT", length = 3000, nullable = false)
    private String content;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_posted")
    private Date timePosted;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List <Comment> comment;

    @ManyToMany(mappedBy = "posts")
    private List<User> usersLiked;


    public Post() {
    }

    public Post(String title, List<Categories> categories, String content, Date timePosted, String imageUrl, User owner, List<Comment> comment, List<User> usersLiked) {
        this.title = title;
        this.categories = categories;
        this.content = content;
        this.timePosted = timePosted;
        this.imageUrl = imageUrl;
        this.owner = owner;
        this.comment = comment;
        this.usersLiked = usersLiked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(Date timePosted) {
        this.timePosted = timePosted;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public List<User> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(List<User> usersLiked) {
        this.usersLiked = usersLiked;
    }
}

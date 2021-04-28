package com.cuisinexperience.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
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
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")}
    )

    private List<Categories> categories;

    @Column(name = "content", columnDefinition = "TEXT", length = 3000, nullable = false)
    private String content;

    @Column(name = "time_posted")
    private String timePosted;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User owner;

    public Post() {
    }

    public Post(String title, String content, String timePosted, String imageUrl, User owner) {
        this.title = title;
        this.content = content;
        this.timePosted = timePosted;
        this.imageUrl = imageUrl;
        this.owner = owner;
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

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

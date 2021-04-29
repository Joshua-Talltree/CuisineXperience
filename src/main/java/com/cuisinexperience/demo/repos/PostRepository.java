package com.cuisinexperience.demo.repos;

import com.cuisinexperience.demo.models.Categories;
import com.cuisinexperience.demo.models.Post;
import com.cuisinexperience.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostsByOwnerId(Long ownerId);

    List<Post> findPostsByCategoryAndContainsOwner(Categories category, User owner);

}

package com.cuisinexperience.demo.repos;

import com.cuisinexperience.demo.models.Categories;
import com.cuisinexperience.demo.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByOwnerId(Long ownerId);
//    List<Post> findPostsByCategoryAndContainsOwner
    List<Post> findAllByCategoriesEquals (Categories category);
    List<Post> findPostsByCategoriesEquals(Categories category);
    List<Post> findByContentOrTitle(String word, String word1);
}

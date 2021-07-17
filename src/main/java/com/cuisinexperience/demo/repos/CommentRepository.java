package com.cuisinexperience.demo.repos;

import com.cuisinexperience.demo.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

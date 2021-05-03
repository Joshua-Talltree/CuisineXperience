package com.cuisinexperience.demo.repos;

import com.cuisinexperience.demo.models.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    List<Friends> getFriendsById(Long id);
}

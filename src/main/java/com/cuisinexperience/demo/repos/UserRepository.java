package com.cuisinexperience.demo.repos;

import com.cuisinexperience.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    User findUserById(Long userRecipientId);
    User findByUsernameContaining(String username);
}

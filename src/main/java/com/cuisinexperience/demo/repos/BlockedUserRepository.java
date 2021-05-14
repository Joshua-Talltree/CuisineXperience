package com.cuisinexperience.demo.repos;

import com.cuisinexperience.demo.models.BlockedUser;
import com.cuisinexperience.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {

    List<BlockedUser> getAllByUserRecipientIdAndUserSenderId(User recipientId, User senderId);
}

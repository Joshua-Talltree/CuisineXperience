package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.models.Friends;
import com.cuisinexperience.demo.models.FriendshipStatus;
import com.cuisinexperience.demo.models.User;
import com.cuisinexperience.demo.repos.FriendsRepository;
import com.cuisinexperience.demo.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FriendsController {

    private final FriendsRepository friendsDao;
    private final UserRepository userDao;

    public FriendsController(FriendsRepository friendsDao, UserRepository userDao) {
        this.friendsDao = friendsDao;
        this.userDao = userDao;
    }

    @GetMapping("/user/friends")
    public String viewFriends(Model vModel, Long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Friends> friendsList = friendsDao.getFriendsById(id);
        List<User> userList = new ArrayList<>();
        for (Friends friend : friendsList) {
            if (userDao.findUserById(friend.getUserRecipientId().getId()).getId().equals(loggedInUser.getId())) {
                userList.add(userDao.findUserById(friend.getUserSenderId().getId()));
            } else {
                userList.add(userDao.findUserById(friend.getUserRecipientId().getId()));
            }
        }
        vModel.addAttribute("friends", userList);
        vModel.addAttribute("owner", loggedInUser);
        return "profile";
    }

    @GetMapping("user/{id}/friend-request")
    public void friendRequestSent(@PathVariable Long id) {
        friendsDao.save(new Friends((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), userDao.getOne(id), FriendshipStatus.PENDING));
    }
}

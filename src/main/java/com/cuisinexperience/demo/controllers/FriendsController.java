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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

//    @GetMapping("/user/friends")
//    public String viewFriends(Model vModel, Long id) {
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Friends> friendsList = friendsDao.getFriendsById(id);
//        List<User> userList = new ArrayList<>();
//        for (Friends friend : friendsList) {
//            if (userDao.findUserById(friend.getUserRecipientId().getId()).getId().equals(loggedInUser.getId())) {
//                userList.add(userDao.findUserById(friend.getUserSenderId().getId()));
//            } else {
//                userList.add(userDao.findUserById(friend.getUserRecipientId().getId()));
//            }
//        }
//        vModel.addAttribute("friends", userList);
//        vModel.addAttribute("owner", loggedInUser);
//        return "profile";
//    }

    @PostMapping("/user/{id}/friend-request")
    public String friendRequestSent(@PathVariable Long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedInUser = userDao.getOne(loggedInUser.getId());
        if (loggedInUser.getId().equals(id)) {
            return "redirect:/profile/" + id;
        }

        Friends friend = new Friends();
        friend.setUserRecipientId(userDao.getOne(id));
        friend.setUserSenderId(loggedInUser);
        friend.setStatus(FriendshipStatus.PENDING);
        friendsDao.save(friend);

        return "redirect:/profile/" + id;
    }

    @PostMapping("/user/{id}/friend-approval")
    public String friendApproval(@PathVariable Long id, @RequestParam(name = "approvalId") Long approvalId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedInUser = userDao.getOne(loggedInUser.getId());
        if (loggedInUser.getId().equals(id) && approvalId == null) {
            return "redirect:/profile/" + id;
        }

        Friends approval = friendsDao.getAllByUserRecipientIdAndUserSenderId(loggedInUser, userDao.getOne(approvalId)).get(0);
        approval.setStatus(FriendshipStatus.ACCEPTED);
        friendsDao.save(approval);

        return "redirect:/profile/" + id;
    }

    @PostMapping("/user/{id}/friend-reject")
    public String friendReject(@PathVariable Long id, @RequestParam(name = "rejectId") Long rejectId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedInUser = userDao.getOne(loggedInUser.getId());
        if (loggedInUser.getId().equals(id) && rejectId == null) {
            return "redirect:/profile/" + id;
        }


        Friends rejected = friendsDao.getAllByUserRecipientIdAndUserSenderId(loggedInUser, userDao.getOne(rejectId)).get(0);
        rejected.setStatus(FriendshipStatus.REJECTED);
        friendsDao.save(rejected);

        return "redirect:/profile/" + id;
    }
}

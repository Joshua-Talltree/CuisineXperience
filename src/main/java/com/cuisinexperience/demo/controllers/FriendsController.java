package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.models.BlockedUser;
import com.cuisinexperience.demo.models.Friends;
import com.cuisinexperience.demo.models.FriendshipStatus;
import com.cuisinexperience.demo.models.User;
import com.cuisinexperience.demo.repos.BlockedUserRepository;
import com.cuisinexperience.demo.repos.FriendsRepository;
import com.cuisinexperience.demo.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendsController {

    private final FriendsRepository friendsDao;
    private final UserRepository userDao;
    private final BlockedUserRepository blockedUserDao;

    public FriendsController(FriendsRepository friendsDao, UserRepository userDao, BlockedUserRepository blockedUserDao) {
        this.friendsDao = friendsDao;
        this.userDao = userDao;
        this.blockedUserDao = blockedUserDao;
    }

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

    @PostMapping("/user/{id}/block")
    public String userBlock(@PathVariable Long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedInUser = userDao.getOne(loggedInUser.getId());
        if (loggedInUser.getId().equals(id)) {
            return "redirect:/profile/" + id;
        }

        BlockedUser blockedUser = new BlockedUser();
        blockedUser.setUserRecipientId(userDao.getOne(id));
        blockedUser.setUserSenderId(loggedInUser);
        blockedUserDao.save(blockedUser);

        return "redirect:/profile/" + id;
    }

    @PostMapping("/user/{id}/unblock")
    public String userUnblock(@PathVariable Long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedInUser = userDao.getOne(loggedInUser.getId());
        if (loggedInUser.getId().equals(id)) {
            return "redirect:/profile/" + id;
        }

        BlockedUser blockedUser = blockedUserDao.getAllByUserRecipientIdAndUserSenderId(userDao.getOne(id), loggedInUser).get(0);
        blockedUserDao.delete(blockedUser);

        return "redirect:/profile/" + id;
    }
}

package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.models.*;
import com.cuisinexperience.demo.repos.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GroupController {

    private final UserRepository userDao;
    private final PostRepository postDao;
    private final CategoriesRepository categoriesDao;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupDao;
    private final FriendsRepository friendsDao;


    public GroupController(UserRepository userDao, PostRepository postDao, CategoriesRepository categoriesDao, PasswordEncoder passwordEncoder, GroupRepository groupDao, FriendsRepository friendsDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.categoriesDao = categoriesDao;
        this.passwordEncoder = passwordEncoder;
        this.groupDao = groupDao;
        this.friendsDao = friendsDao;
    }

    @GetMapping("/groups/{groupId}")
    public String viewGroups(Model vModel, @PathVariable Long groupId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedInUser = userDao.getOne(loggedInUser.getId());
        Group thisGroup = groupDao.getOne(groupId);
        boolean isMember = false;
        if(thisGroup.getUsers().contains(loggedInUser)) {
            isMember = true;
        }
        vModel.addAttribute("groups", groupDao.getOne(groupId));
        vModel.addAttribute("isMember", isMember);
        return "group-display";
    }

    @PostMapping("/groups/{groupId}/join")
    public String groupJoin(@PathVariable Long groupId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Group groups = groupDao.getOne(groupId);
        List<User> members = groups.getUsers();

        members.add(loggedInUser);
        groups.setUsers(members);
        groupDao.save(groups);



        return "redirect:/groups/" + groupId;
    }
}

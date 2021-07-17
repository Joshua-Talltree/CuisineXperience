package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.models.*;
import com.cuisinexperience.demo.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class UserController {

    @Autowired
    UserRepository userDao;

    @Autowired
    PostRepository postDao;

    @Autowired
    CategoriesRepository categoriesDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GroupRepository groupDao;

    @Autowired
    FriendsRepository friendsDao;

    @Autowired
    BlockedUserRepository blockedUserDao;


    public UserController(UserRepository userDao, PostRepository postDao, CategoriesRepository categoriesDao, PasswordEncoder passwordEncoder, GroupRepository groupDao, FriendsRepository friendsDao, BlockedUserRepository blockedUserDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.categoriesDao = categoriesDao;
        this.passwordEncoder = passwordEncoder;
        this.groupDao = groupDao;
        this.friendsDao = friendsDao;
        this.blockedUserDao = blockedUserDao;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String saveUser(@ModelAttribute User user, @RequestParam(name = "post_image_url") String imgUrl) {
        String hash = passwordEncoder.encode(user.getPassword());
        User savedImage = userDao.save(user);
        user.setPassword(hash);
        savedImage.setAvatarURl(imgUrl);

        userDao.save(user);
        userDao.save(savedImage);
        return "login";
    }

//    @RequestMapping(path = "/keys.js", produces = "application/javascript")
//    @ResponseBody
//    public String apiKey() implements PostController {
//        System.out.println(fileStackApiKey);
//        return "const FileStackApiKey = `" + fileStackApiKey + "`";
//    }

    @GetMapping("/profile")
    public String showUserProfile(Model model){
        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
        }
        return "redirect:/profile/" + user.getId();
    }


    @GetMapping("profile/{ownerId}")
    public String showProfile(Model vModel, @PathVariable Long ownerId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedInUser = userDao.getOne(loggedInUser.getId());
        List<Post> posts = postDao.findPostsByOwnerId(ownerId);
        vModel.addAttribute("posts", posts);

        // show categories and post to categories
        List<Categories> categories = new ArrayList<>();
        HashMap<Long, String> existingCategories = new HashMap<>();
        for (Post post : posts) {
            if(post.getCategories().size() > 0) {
                if (categories.contains(post.getCategories().get(0))) {

                } else {
                    categories.add(post.getCategories().get(0));
//                    existingCategories.(post.getCategories.get(0).getId()):
//                    existingCategories.addValue(post.getCategories().get(0));
                }
            }
        }
        List<Group> groups = groupDao.findAllByCreatedById(ownerId);
        List<Friends> friendsList = friendsDao.getAllByUserRecipientIdOrUserSenderId(userDao.getOne(ownerId), userDao.getOne(ownerId));
        List<User> pending = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        // filter out your id as a friend on your friends list
        for (Friends friend : friendsList) {
            if (friend.getStatus() == FriendshipStatus.valueOf("ACCEPTED")) {
                if (friend.getUserSenderId().getId().equals(ownerId)) {userList.add(friend.getUserRecipientId());
                } else {
                    userList.add(userDao.findUserById(friend.getUserSenderId().getId()));
                }
            } else if (friend.getStatus() == FriendshipStatus.valueOf("PENDING")) {
                if (friend.getUserRecipientId().getId().equals(ownerId)) {
                    pending.add(userDao.findUserById(friend.getUserSenderId().getId()));
                }
            }
        }

        List<Friends> existingFriends = friendsDao.getAllByUserRecipientIdAndUserSenderId(loggedInUser, userDao.getOne(ownerId));
        List<Friends> existingFriendsReverse = friendsDao.getAllByUserRecipientIdAndUserSenderId(userDao.getOne(ownerId), loggedInUser);

        existingFriends.removeIf(friend -> friend.getStatus() == FriendshipStatus.REJECTED);
        existingFriendsReverse.removeIf(friend -> friend.getStatus() == FriendshipStatus.REJECTED);

        boolean isBlocked = false;
        boolean blockedByMe = false;

        // block and unblock users
        List<BlockedUser> blockedUser = blockedUserDao.getAllByUserRecipientIdAndUserSenderId(loggedInUser, userDao.getOne(ownerId));
        List<BlockedUser> blockedUserReverse = blockedUserDao.getAllByUserRecipientIdAndUserSenderId(userDao.getOne(ownerId), loggedInUser);

       if(blockedUser.size() > 0) {
           isBlocked = true;
       }

       if(blockedUserReverse.size() > 0) {
           isBlocked = true;
           blockedByMe = true;
       }

        boolean isAlreadyFriends = false;

        if(existingFriends.size() > 0 || existingFriendsReverse.size() > 0) {
            isAlreadyFriends = true;
        }

        boolean isOwner = false;

        if(loggedInUser.getId().equals(ownerId)) {
            isOwner = true;
        }

        vModel.addAttribute("blockedByMe", blockedByMe);
        vModel.addAttribute("isBlocked", isBlocked);
        vModel.addAttribute("isOwner", isOwner);
        vModel.addAttribute("alreadyFriends", isAlreadyFriends);
        vModel.addAttribute("pending", pending);
        vModel.addAttribute("friends", userList);
        vModel.addAttribute("owner", loggedInUser);
        vModel.addAttribute("groups", groups);
        vModel.addAttribute("owner", userDao.getOne(ownerId));
        vModel.addAttribute("categories", categories);

        return "profile";
    }

    @GetMapping("/profile/{ownerId}/category/{categoryId}")
    public String seeCategory(@PathVariable Long categoryId, @PathVariable String ownerId, Model vModel) {
        Categories category = categoriesDao.getOne(categoryId);
        List<Post> posts =  postDao.findPostsByCategoriesEquals(category);
        vModel.addAttribute("posts", posts);
        vModel.addAttribute("owner", userDao.getOne(Long.parseLong(ownerId)));
        vModel.addAttribute("category", categoriesDao.findAll());
        return "profile";
    }

    @GetMapping("/user/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String userManage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDao.getOne(user.getId());
        List<User> users = userDao.findAll();
        model.addAttribute("users", users);
        if (loggedInUser.getIsAdmin()) {
            return "admin";
        } else {
            return "redirect:/profile";
        }
    }

    @PostMapping("/user/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        User user = userDao.findUserById(id);
        userDao.delete(user);
        return "redirect:/admin";
    }

    @GetMapping("/user/{id}/update")
    public String updateUserForm(Model model, @PathVariable Long id){
        model.addAttribute("user", userDao.getOne(id));
        return "signup";
    }

    @PostMapping("/user/{id}/update")
    public String updatePost(@ModelAttribute User userToUpdate, @PathVariable Long id, @RequestParam(name = "post_image_url") String imgUrl){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String hashPass = passwordEncoder.encode(userToUpdate.getPassword());
        userToUpdate.setPassword(hashPass);
        userToUpdate.setId(loggedInUser.getId());
        userToUpdate.setAvatarURl(imgUrl);

        userDao.save(userToUpdate);
        return "redirect:/profile/" + id;
    }

    @GetMapping("/groups/create")
    public String createGroups(Model model) {
        model.addAttribute("group", new Group());
        return "groups";
    }

    @PostMapping("/groups/create")
    public String createGroupsHere(Model vModel, @ModelAttribute Group groupToCreate, String name) {
        User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userToAdd = userDao.getOne(userToAdd.getId());
        groupToCreate.setCreatedById(userToAdd.getId());

        // check if group name already exists
        List<Group> groups = groupDao.findAll();
        int count = 0;
        for (Group group : groups) {
           if (group.getName().equalsIgnoreCase(groupToCreate.getName())) {
               count++;
           }
        }
        // save group if name does not exist
        if(count == 0) {
            groupDao.save(groupToCreate);
        } else {
            vModel.addAttribute("alert", "<div class=\"alert alert-light\" role=\"alert\">\n" +
                    "                                Invalid group name, name already exists" +
                    "                                </div>");
            return "groups";
        }

        // add user to group that they created.
        List<User> member = new ArrayList<>();
        member.add(userToAdd);
        groupToCreate.setUsers(member);
        groupDao.save(groupToCreate);
        // set the group id
        groupToCreate.setName(String.valueOf(userToAdd));
        return "redirect:/profile/" + userToAdd.getId();
    }

    @GetMapping("user/{ownerId}/images")
    public String allImagesFromUser(@PathVariable Long ownerId, Model vModel) {
        List<Post> posts = postDao.findPostsByOwnerId(ownerId);
        vModel.addAttribute("posts", posts);
        vModel.addAttribute("owner", userDao.getOne(ownerId));
        return "users/images";
    }

//    @PostMapping("/users/search")
//    public String searchPosts(@RequestParam(name = "users") String term, Model vModel) {
//        vModel.addAttribute("activeUser", userDao.findByUsernameContaining(term));
//        vModel.addAttribute("users", term);
//        return "index";
//    }
}


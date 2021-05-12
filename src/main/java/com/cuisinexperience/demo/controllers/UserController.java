package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.models.*;
import com.cuisinexperience.demo.repos.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserRepository userDao;
    private final PostRepository postDao;
    private final CategoriesRepository categoriesDao;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupDao;
    private final FriendsRepository friendsDao;


    public UserController(UserRepository userDao, PostRepository postDao, CategoriesRepository categoriesDao, PasswordEncoder passwordEncoder, GroupRepository groupDao, FriendsRepository friendsDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.categoriesDao = categoriesDao;
        this.passwordEncoder = passwordEncoder;
        this.groupDao = groupDao;
        this.friendsDao = friendsDao;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "/signup";
    }

    @PostMapping("/signup")
    public String saveUser(@ModelAttribute User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDao.save(user);
        return "/login";
    }

    @GetMapping("profile/{ownerId}")
    public String showProfile(Model vModel, @PathVariable Long ownerId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Post> posts = postDao.findPostsByOwnerId(ownerId);
        vModel.addAttribute("posts", posts);

        List<Categories> categories = new ArrayList<>();
        for (Post post : posts) {
            post.getCategories().stream().filter(category -> !categories.contains(category)).forEach(categories::add);
        }
        List<Group> groups = groupDao.findAllByCreatedById(ownerId);

        List<Friends> friendsList = friendsDao.getAllByUserRecipientIdOrUserSenderId(userDao.getOne(ownerId), userDao.getOne(ownerId));
        System.out.println("friendsList = " + friendsList);
        List<User> pending = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        // filter out your id as a friend on your friends list
        for (Friends friend : friendsList) {
            if (userDao.findUserById(friend.getUserRecipientId().getId()).getId().equals(loggedInUser.getId())) {
                userList.add(userDao.findUserById(friend.getUserSenderId().getId()));
            } else if (friend.getStatus() == FriendshipStatus.valueOf("ACCEPTED")) {
                userList.add(userDao.findUserById(friend.getUserSenderId().getId()));
            } else if (friend.getStatus() == FriendshipStatus.valueOf("PENDING")) {
                pending.add(userDao.findUserById(friend.getUserSenderId().getId()));
            }
        }
//        pending.add(userDao.getOne(2L));
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
//        List<Post> posts = postDao.findPostsByCategoryAndContainsOwner(searchCategories, userDao.getOne(Long.parseLong(ownerId)));
//        vModel.addAttribute("posts", posts);
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
        return "/signup";
    }

    @PostMapping("/user/{id}/update")
    public String updatePost(@ModelAttribute User userToUpdate, @PathVariable String username){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String hashPass = passwordEncoder.encode(userToUpdate.getPassword());
        userToUpdate.setPassword(hashPass);
        userToUpdate.setId(loggedInUser.getId());
        userDao.save(userToUpdate);
        return "redirect:/profile/" + userToUpdate.getId();
    }

    @GetMapping("/groups/create")
    public String createGroups(Model model) {
        model.addAttribute("group", new Group());
        return "/groups";
    }

    @PostMapping("/groups/create")
    public String createGroupsHere(@ModelAttribute Group groupToCreate, String createdById) {
        User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        groupToCreate.setCreatedById(userToAdd.getId());
        // save the group
        groupDao.save(groupToCreate);
        // set the group id
        groupToCreate.setName(String.valueOf(userToAdd));
        return "redirect:/profile/" + userToAdd.getId();
    }

    @GetMapping("user/{ownerId}/images")
    public String allImagesFromUser(@PathVariable Long ownerId, Model vModel) {
        List<Post> imagesFromDB = postDao.findAll();
        List<Post> posts = postDao.findPostsByOwnerId(ownerId);
        vModel.addAttribute("posts", imagesFromDB);
        vModel.addAttribute("owner", userDao.getOne(ownerId));
        return "users/images";
    }
}
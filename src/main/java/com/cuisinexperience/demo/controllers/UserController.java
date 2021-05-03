package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.models.Categories;
import com.cuisinexperience.demo.models.Friends;
import com.cuisinexperience.demo.models.Post;
import com.cuisinexperience.demo.models.User;
import com.cuisinexperience.demo.repos.CategoriesRepository;
import com.cuisinexperience.demo.repos.FriendsRepository;
import com.cuisinexperience.demo.repos.PostRepository;
import com.cuisinexperience.demo.repos.UserRepository;
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
    private final FriendsRepository friendsDao;


    public UserController(UserRepository userDao, PostRepository postDao, CategoriesRepository categoriesDao, PasswordEncoder passwordEncoder, FriendsRepository friendsDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.categoriesDao = categoriesDao;
        this.passwordEncoder = passwordEncoder;
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
        List<Post> posts = postDao.findPostsByOwnerId(ownerId);
        vModel.addAttribute("posts", posts);

        List<Categories> categories = new ArrayList<>();
        for (Post post : posts) {
            post.getCategories().stream().filter(category -> !categories.contains(category)).forEach(categories::add);
        }
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

    @GetMapping("/user/friends")
    public String viewFriends(Model vModel, Long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Friends> friendsList = friendsDao.getFriendsById(id);
        List<User> userList = new ArrayList<User>();
        for (Friends friend : friendsList) {
           if (userDao.findUserById(friend.getUserRecipientId().getId()).getId() == loggedInUser.getId()) {
               userList.add(userDao.findUserById(friend.getUserSenderId().getId()));
            } else {
               userList.add(userDao.findUserById(friend.getUserRecipientId().getId()));
           }
        }
        vModel.addAttribute("friends", userList);
        vModel.addAttribute("owner", loggedInUser);
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
}
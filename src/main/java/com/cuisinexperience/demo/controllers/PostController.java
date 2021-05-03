package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.models.Categories;
import com.cuisinexperience.demo.models.Post;
import com.cuisinexperience.demo.models.User;
import com.cuisinexperience.demo.repos.CategoriesRepository;
import com.cuisinexperience.demo.repos.PostRepository;
import com.cuisinexperience.demo.repos.UserRepository;
import com.cuisinexperience.demo.services.EmailServices;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    private final UserRepository userDao;
    private final PostRepository postDao;
    private final CategoriesRepository categoriesDao;
    private final EmailServices emailServices;

    public PostController(UserRepository userDao, PostRepository postDao, CategoriesRepository categoriesDao, EmailServices emailServices) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.categoriesDao = categoriesDao;
        this.emailServices = emailServices;
    }

    @GetMapping("/posts")
    public String allPosts(Model viewModel) {
        List<Post> postsFromDB = postDao.findAll();
        viewModel.addAttribute("posts", postsFromDB);
        return "/index";
    }


    @GetMapping("/posts/create")
    public String createPosts(Model model) {
        model.addAttribute("post", new Post());
        return "/create";
    }

    @PostMapping("/posts/create")
    public String createPostsHere(@ModelAttribute Post postToCreate) {

        User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // save the post
        postDao.save(postToCreate);
        // set the user
        postToCreate.setOwner(userToAdd);
        Post savedPost = postDao.save(postToCreate);
        emailServices.prepareAndSend(savedPost,"Here is the title", "Here is the body");
        return "redirect:/posts";
    }

    @GetMapping("/posts/category/{categoryId}")
    public String showCategory(@PathVariable String categoryId , Model model) {
        Categories searchCategories = categoriesDao.getOne(Long.parseLong(categoryId));
//        List<Post> posts = postDao.findPostsByCategoryAndContainsOwner(searchCategories, userDao.getOne(Long.parseLong(categoryId)));
//        model.addAttribute("posts", posts);
        model.addAttribute("categories", categoriesDao.findAll());
        return "index";
    }

    @GetMapping("/posts/{id}/update")
    public String updatePostForm(Model model, @PathVariable Long id){
        model.addAttribute("post", postDao.getOne(id));
        return "create";
    }

    @PostMapping("/posts/{id}/update")
    public String updatePost(@ModelAttribute Post postToUpdate, @PathVariable String id){

        User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(id);
        System.out.println(userToAdd.getUsername());
        System.out.println(postToUpdate.getTitle());
        postToUpdate.setId(Long.parseLong(id));

        // set the user
        postToUpdate.setOwner(userToAdd);

        // save the post
        postDao.save(postToUpdate);

        return "redirect:/posts";
    }

}

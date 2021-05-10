package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.models.*;
import com.cuisinexperience.demo.repos.CategoriesRepository;
import com.cuisinexperience.demo.repos.CommentRepository;
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
    private final CommentRepository commentDao;
    private final EmailServices emailServices;

    public PostController(UserRepository userDao, PostRepository postDao, CategoriesRepository categoriesDao, CommentRepository commentDao, EmailServices emailServices) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.categoriesDao = categoriesDao;
        this.commentDao = commentDao;
        this.emailServices = emailServices;
    }

    @GetMapping("/posts")
    public String allPosts(Model vModel) {
        List<Post> postsFromDB = postDao.findAll();
        vModel.addAttribute("posts", postsFromDB);
        return "/index";
    }
    @GetMapping("/posts/{id}")
    public String individualPosts(@PathVariable Long id, Model vModel) {

//        List<Comment> comments = commentDao.findCommentByIdAndPost(id);
//        vModel.addAttribute("comment", comments);
        vModel.addAttribute("post", postDao.getOne(id));
        return "/show";
    }

    @GetMapping("/posts/create")
    public String createPosts(Model vModel) {
        vModel.addAttribute("post", new Post());
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
    public String showCategory(@PathVariable String categoryId , Model vModel) {
        Categories searchCategories = categoriesDao.getOne(Long.parseLong(categoryId));
//        List<Post> posts = postDao.findPostsByCategoryAndContainsOwner(searchCategories, userDao.getOne(Long.parseLong(categoryId)));
//        model.addAttribute("posts", posts);
        vModel.addAttribute("categories", categoriesDao.findAll());
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
        postToUpdate.setId(Long.parseLong(id));
        // set the user
        postToUpdate.setOwner(userToAdd);
        // save the post
        postDao.save(postToUpdate);
        return "redirect:/posts";
    }

    @PostMapping("/posts/search")
    public String searchPosts(@RequestParam(name = "word") String term, Model vModel) {
        vModel.addAttribute("posts", postDao.findAllByContentContains(term));
        vModel.addAttribute("word", term);
        return "index";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postDao.deleteById(id);
        return "redirect:/profile";
    }

    @GetMapping("comment/create")
    public String showCommentForm(Model vModel) {
        vModel.addAttribute("comments", new Comment());
        return "/index";
    }

    @PostMapping("/comment/create")
    public String createComment(@ModelAttribute(name = "comment") String commentBody, @ModelAttribute(name = "postId") Long id) {
        User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment commentToCreate = new Comment();
        commentToCreate.setComment(commentBody);
        commentToCreate.setPost(postDao.getOne(id));
        commentToCreate.setUserId(userToAdd);
        // save the comment
        commentDao.save(commentToCreate);
        return "redirect:/posts";
    }
}

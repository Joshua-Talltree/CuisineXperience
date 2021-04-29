package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.models.Categories;
import com.cuisinexperience.demo.models.Post;
import com.cuisinexperience.demo.repos.CategoriesRepository;
import com.cuisinexperience.demo.repos.PostRepository;
import com.cuisinexperience.demo.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PostController {

    private final UserRepository userDao;
    private final PostRepository postDao;
    private final CategoriesRepository categoriesDao;

    public PostController(UserRepository userDao, PostRepository postDao, CategoriesRepository categoriesDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.categoriesDao = categoriesDao;
    }

    @GetMapping("/posts")
    public String allPosts(Model viewModel) {
        List<Post> postsFromDB = postDao.findAll();
        viewModel.addAttribute("posts", postsFromDB);
        return "/index";
    }

    @GetMapping("/posts/category/{categoryId}")
    public String showCategory(@PathVariable String categoryId , Model model) {
        Categories searchCategories = categoriesDao.getOne(Long.parseLong(categoryId));
        List<Post> posts = postDao.findPostsByCategoryAndContainsOwner(searchCategories, userDao.getOne(Long.parseLong(categoryId)));
        model.addAttribute("posts", posts);
        model.addAttribute("categories", categoriesDao.findAll());
        return "index";
    }
}

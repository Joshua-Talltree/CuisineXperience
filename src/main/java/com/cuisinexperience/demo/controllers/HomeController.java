package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.repos.PostRepository;
import com.cuisinexperience.demo.repos.UserRepository;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

    private final UserRepository userDao;
    private final PostRepository postDao;

    public HomeController(UserRepository userDao, PostRepository postDao) {
        this.userDao = userDao;
        this.postDao = postDao;
    }


}

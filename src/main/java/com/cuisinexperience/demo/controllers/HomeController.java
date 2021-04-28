package com.cuisinexperience.demo.controllers;

import com.cuisinexperience.demo.repos.PostRepository;
import com.cuisinexperience.demo.repos.UserRepository;
import com.cuisinexperience.demo.services.EmailServices;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

    private final UserRepository userDao;
    private final EmailServices emailService;
    private final PostRepository postDao;

    public HomeController(UserRepository userDao, EmailServices emailService, PostRepository postDao) {
        this.userDao = userDao;
        this.emailService = emailService;
        this.postDao = postDao;
    }

}

package com.scm.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.helpers.Helper;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    // user dashboard page
    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }

    // user profile page
    @RequestMapping(value = "/profile")
    public String userProfile(Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUser(authentication);

        logger.info("User logged in {}", username);

        return "user/profile";
    }

    // user contact page

    // user edit contact page

    // user delete page

    // user search page
}

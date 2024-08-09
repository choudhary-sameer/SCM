package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticationToken) {

            var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User) authentication.getPrincipal();
            var userName = "";

            if (clientId.equalsIgnoreCase("google")) {
                System.out.println("Getting email from google.");
                userName = oauth2User.getAttribute("email").toString();
            } else if (clientId.equalsIgnoreCase("github")) {
                System.out.println("Getting email from github.");
                userName = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
                        : oauth2User.getAttribute("login").toString() + "@gmail.com";
            }

            return userName;

        } else {
            System.out.println("Getting data from local database.");
            return authentication.getName();
        }
    }
}

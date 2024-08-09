package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repositories.UserRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.scm.entities.Providers;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");

        // Identify the provider
        var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        var oAuthUser = (DefaultOAuth2User) authentication.getPrincipal();

        oAuthUser.getAttributes().forEach((key, value) -> {
            logger.info(key + " : " + value);
        });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword("dummy");

        // Google Login
        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            user.setEmail(oAuthUser.getAttribute("email").toString());
            user.setProfilePic(oAuthUser.getAttribute("picture").toString());
            user.setName(oAuthUser.getAttribute("name").toString());
            user.setProviderId(oAuthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setAbout("This account is created using Google.");
        } // Github Login
        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            String email = oAuthUser.getAttribute("email") != null ? oAuthUser.getAttribute("email").toString()
                    : oAuthUser.getAttribute("login").toString() + "@gmail.com";
            String picture = oAuthUser.getAttribute("avatar_url").toString();
            String name = oAuthUser.getAttribute("login").toString();
            String providerId = oAuthUser.getName();

            user.setName(name);
            user.setEmail(email);
            user.setProfilePic(picture);
            user.setProviderId(providerId);
            user.setProvider(Providers.GITHUB);
            user.setAbout("This account is created using Github.");
        } else {
            logger.info("OAuthAuthenticationSuccessHandler: Unknown provider!");
        }

        /*
         * DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
         * 
         * // logger.info(user.getName());
         * 
         * // user.getAttributes().forEach((key, value) -> {
         * // logger.info("{} => {}", key, value);
         * // });
         * 
         * // logger.info(user.getAuthorities().toString());
         * 
         * // save data to database
         * String email = user.getAttribute("email").toString();
         * String name = user.getAttribute("name").toString();
         * String picture = user.getAttribute("picture").toString();
         * 
         * User user1 = new User();
         * user1.setEmail(email);
         * user1.setName(name);
         * user1.setProfilePic(picture);
         * user1.setPassword("password");
         * user1.setUserId(UUID.randomUUID().toString());
         * user1.setProvider(Providers.GOOGLE);
         * user1.setEnabled(true);
         * user1.setEmailVerified(true);
         * user1.setProviderId(user.getName());
         * user1.setRoleList(List.of(AppConstants.ROLE_USER));
         * user1.setAbout("This account is created using gmail...");
         */

        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (user2 == null) {
            userRepo.save(user);
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}

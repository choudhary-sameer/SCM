package com.scm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    // user create and login using in memory service
    // @Bean
    // public UserDetailsService userDetailService() {
    // UserDetails user1 = User
    // .withDefaultPasswordEncoder()
    // .username("admin123")
    // .roles("ADMIN", "USER")
    // .password("admin123")
    // .build();

    // UserDetails user2 = User
    // .withUsername("user123")
    // // .roles("ADMIN", "USER")
    // .password("user123")
    // .build();

    // var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1,
    // user2);
    // return inMemoryUserDetailsManager;
    // }

    @Autowired
    private OAuthenticationSuccessHandler handler;

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    // Configuration of Authentication Provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // object of user detail service
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        // object of password encoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Configuration
        // configure URLs (which needs to be public and which needs to be authenticated)
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/register", "/services").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // form default login
        // Come here if you want to change anything related to form login related
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login")
                    .loginProcessingUrl("/authentication")
                    .successForwardUrl("/user/profile")
                    // .failureForwardUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password");
            // .failureHandler(new AuthenticationFailureHandler() {

            // @Override
            // public void onAuthenticationFailure(HttpServletRequest request,
            // HttpServletResponse response,
            // AuthenticationException exception) throws IOException, ServletException {
            // // TODO Auto-generated method stub
            // throw new UnsupportedOperationException("Unimplemented method
            // 'onAuthenticationFailure'");
            // }
            // });
            // formLogin.successHandler(new AuthenticationSuccessHandler() {

            // @Override
            // public void onAuthenticationSuccess(HttpServletRequest request,
            // HttpServletResponse response,
            // Authentication authentication) throws IOException, ServletException {
            // // TODO Auto-generated method stub
            // throw new UnsupportedOperationException("Unimplemented method
            // 'onAuthenticationSuccess'");
            // }

        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        // OAuth2 Configurations
        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

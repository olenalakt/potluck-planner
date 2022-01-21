package com.olena.tokenservice.controller;

import com.olena.tokenservice.model.User;
import com.olena.tokenservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserInfoEndpoint {

    @Autowired
    UserRepository userRepository;

    // TODO: improve
    @RequestMapping("/v1/api/users/me")
    public ResponseEntity<User> profile() {
        //Build some dummy data to return for testing
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User profile = userRepository.findFirstByUserName(user.getUserName());

        return ResponseEntity.ok(profile);
    }



}

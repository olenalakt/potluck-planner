package com.olena.oauthserver.controller;

import com.olena.oauthserver.model.User;
import com.olena.oauthserver.repository.UserRepository;
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
    @RequestMapping("/api/users/me")
    public ResponseEntity<User> profile() {
        //Build some dummy data to return for testing
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User profile = userRepository.findFirstByUserName(user.getUserName());

        return ResponseEntity.ok(profile);
    }

//    @RequestMapping(value= "/oauth/token", method= RequestMethod.OPTIONS)
//    public void corsHeaders(HttpServletResponse response) {
//        response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
//        response.addHeader("Access-Control-Max-Age", "3600");
//    }

}

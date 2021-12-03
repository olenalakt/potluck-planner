package com.olena.authserver.service;

import com.olena.authserver.model.User;
import com.olena.authserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("OL: loadUserByUsername: {}", username);

        //TBD replace with service call
        User user = userRepository.findFirstByUserName(username);
        log.info("OL: user: {}", user);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found!");
        }

        org.springframework.security.core.userdetails.User.UserBuilder builder = null;

        builder = org.springframework.security.core.userdetails.User.withUsername(user.getUserName());
        builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
        builder.roles(user.getUserRole());

        return builder.build();
    }

}

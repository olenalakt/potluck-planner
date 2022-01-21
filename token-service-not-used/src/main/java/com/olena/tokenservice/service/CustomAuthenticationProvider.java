package com.olena.tokenservice.service;

import com.olena.tokenservice.model.User;
import com.olena.tokenservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        log.info("OL: authenticate: {}", authentication.toString());

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("OL: authenticate: name={}, password={}", name, password);
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();

        //TBD replace with service call
        User user = userRepository.findFirstByUserName(name);
        log.info("OL: user: {}", user);

        // validate against retrieved creds
        if (user != null && user.getPassword().equals(password)) {
            grantedAuthorityList.add(new SimpleGrantedAuthority(user.getUserRole()));
            return new UsernamePasswordAuthenticationToken(name, password, grantedAuthorityList);
        } else {
            return new UsernamePasswordAuthenticationToken(name, password);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
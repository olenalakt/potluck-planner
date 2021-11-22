package com.olena.userservice.controller;

import com.olena.userservice.exception.ServiceException;
import com.olena.userservice.model.UserDTO;
import com.olena.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * @param userName
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("name") String userName) throws ServiceException {

        return ResponseEntity.ok(userService.getUserFromDb(userName));
    }

    /**
     * @param userDTO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws ServiceException {

        if (userDTO != null) {
            userService.addUser(userDTO);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
                    .buildAndExpand(userDTO.getUserName()).toUri();

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}

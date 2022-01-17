package com.olena.userservice.controller;

import com.olena.userservice.exception.BadInputException;
import com.olena.userservice.exception.ServiceException;
import com.olena.userservice.model.UserDTO;
import com.olena.userservice.publisher.PotluckPlannerCleanupPublisher;
import com.olena.userservice.service.UserService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PotluckPlannerCleanupPublisher potluckPlannerCleanupPublisher;

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
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws ServiceException, BadInputException {

        // userName and userRole can not be null -  check only  if pwd provided
        if (StringUtils.isBlank(userDTO.getPassword())) {
            throw new BadInputException("createUser: empty password");
        } else {
            return ResponseEntity.ok(userService.addUser(userDTO));
        }
    }

    /**
     * @param userDTO
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) throws ServiceException, BadInputException {

        if (!StringUtils.isBlank(userDTO.getUserId())) {
            return ResponseEntity.ok(userService.updateUser(userDTO));
        } else {
            throw new BadInputException("updateUser: userId can not be null");
        }
    }

    /**
     * @param userId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{userid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("userid") String userId) throws ServiceException, BadInputException {

        if (!StringUtils.isBlank(userId)) {
            return ResponseEntity.ok(userService.deleteUser(userId, potluckPlannerCleanupPublisher));
        } else {
            throw new BadInputException("deleteUser: userId can not be null");
        }
    }
}

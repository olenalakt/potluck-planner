package com.olena.dishcleanupservice.controller;

import com.olena.dishcleanupservice.exception.ServiceException;
import com.olena.dishcleanupservice.service.DishCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/dishcleanup")
public class DishCleanupController {

    @Autowired
    DishCleanupService dishCleanupService;

    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "username/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventMenu(@PathVariable("username") final String userName) throws ServiceException {
        // returns list  of requests
        return ResponseEntity.ok(dishCleanupService.getCleanupRequestsByUsername(userName));
    }

}

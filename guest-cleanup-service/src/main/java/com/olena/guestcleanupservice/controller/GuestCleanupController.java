package com.olena.guestcleanupservice.controller;

import com.olena.guestcleanupservice.exception.ServiceException;
import com.olena.guestcleanupservice.service.GuestCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/guestcleanup")
public class GuestCleanupController {

    @Autowired
    GuestCleanupService guestCleanupService;


    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "username/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getCleanupRequestList(@PathVariable("username") final String userName) throws ServiceException {
        // returns list  of requests
        return ResponseEntity.ok(guestCleanupService.getCleanupRequestsByUsername(userName));
    }

}

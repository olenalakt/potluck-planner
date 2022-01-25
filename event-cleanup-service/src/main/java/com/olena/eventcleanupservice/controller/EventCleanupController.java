package com.olena.eventcleanupservice.controller;

import com.olena.eventcleanupservice.exception.ServiceException;
import com.olena.eventcleanupservice.service.EventCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/eventcleanup")
public class EventCleanupController {

    @Autowired
    EventCleanupService eventCleanupService;


    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "username/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventMenu(@PathVariable("username") final String userName) throws ServiceException {
        // returns list  of requests
        return ResponseEntity.ok(eventCleanupService.getCleanupRequestsByUsername(userName));
    }

}

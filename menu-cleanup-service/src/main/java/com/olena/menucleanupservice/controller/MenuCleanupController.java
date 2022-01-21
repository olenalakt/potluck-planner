package com.olena.menucleanupservice.controller;

import com.olena.menucleanupservice.exception.ServiceException;
import com.olena.menucleanupservice.service.MenuCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/menucleanup")
// TODO -  get rid of @CrossOrigin -  should use CORS regex external config instead
@CrossOrigin(origins = "http://localhost:4200")
public class MenuCleanupController {

    @Autowired
    MenuCleanupService menuCleanupService;


    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "username/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventMenu(@PathVariable("username") final String userName) throws ServiceException {
        // returns list  of requests
        return ResponseEntity.ok(menuCleanupService.getCleanupRequestsByUsername(userName));
    }

}

package com.olena.guestcleanupservice.controller;

import com.olena.guestcleanupservice.config.GuestCleanupServiceProperties;
import com.olena.guestcleanupservice.repository.GuestCleanupRequestRepository;
import com.olena.guestcleanupservice.repository.GuestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GuestCleanupServiceHealth {

    @Autowired
    GuestCleanupServiceProperties guestCleanupServiceProperties;

    @Autowired
    GuestRepository guestRepository;

    @Autowired
    GuestCleanupRequestRepository guestCleanupRequestRepository;

    /**
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<String> welcome() {
        log.debug("welcome, entered");
        return checkHealth();
    }

    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        log.debug("checkHealth, entered");
        return new ResponseEntity<>("Welcome to Guest Cleanup Service", HttpStatus.OK);
    }

    @GetMapping(value = "/version", produces = MediaType.TEXT_PLAIN_VALUE)
    public String checkVersion() {
        log.debug("checkVersion, entered");
        return "App version: " + guestCleanupServiceProperties.getAppVersion() +
                "\nDB schema version: " + guestCleanupServiceProperties.getDbSchemaVersion();
    }

    @GetMapping(value = "/info", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getInfo() {
        log.debug("getInfo, entered");
        return checkVersion();
    }

}

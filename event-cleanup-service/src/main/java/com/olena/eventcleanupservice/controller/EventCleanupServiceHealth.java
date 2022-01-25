package com.olena.eventcleanupservice.controller;

import com.olena.eventcleanupservice.config.EventCleanupServiceProperties;
import com.olena.eventcleanupservice.repository.EventCleanupRequestRepository;
import com.olena.eventcleanupservice.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EventCleanupServiceHealth {

    @Autowired
    EventCleanupServiceProperties eventCleanupServiceProperties;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventCleanupRequestRepository eventCleanupRequestRepository;

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
        return new ResponseEntity<>("Welcome to Event Cleanup Service", HttpStatus.OK);
    }

    @GetMapping(value = "/version", produces = MediaType.TEXT_PLAIN_VALUE)
    public String checkVersion() {
        log.debug("checkVersion, entered");
        return "App version: " + eventCleanupServiceProperties.getAppVersion() +
                "\nDB schema version: " + eventCleanupServiceProperties.getDbSchemaVersion();
    }

    @GetMapping(value = "/info", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getInfo() {
        log.debug("getInfo, entered");
        return checkVersion();
    }

}

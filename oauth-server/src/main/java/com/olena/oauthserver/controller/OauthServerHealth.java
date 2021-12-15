package com.olena.oauthserver.controller;

import com.olena.oauthserver.config.OauthServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OauthServerHealth {

    @Autowired
    OauthServerProperties oauthServerProperties;

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
        return new ResponseEntity<>("Welcome to Potluck Planner Authorization Server", HttpStatus.OK);
    }

    @GetMapping(value = "/version", produces = MediaType.TEXT_PLAIN_VALUE)
    public String checkVersion() {
        log.debug("checkVersion, entered");
        return "App version: " + oauthServerProperties.getAppVersion() +
                "\nDB schema version: " + oauthServerProperties.getDbSchemaVersion();
    }

    @GetMapping(value = "/info", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getInfo() {
        log.debug("getInfo, entered");
        return checkVersion();
    }

}

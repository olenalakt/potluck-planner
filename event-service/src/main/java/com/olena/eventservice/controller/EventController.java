package com.olena.eventservice.controller;

import com.olena.eventservice.exception.ServiceException;
import com.olena.eventservice.model.EventDTO;
import com.olena.eventservice.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    EventService eventService;

    //TBD extract username from JWT token and update queries

    /**
     * @param userName
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventListByUserName(@PathVariable("username") String userName) throws ServiceException {
        return ResponseEntity.ok(eventService.getEventListByUserName(userName));
    }

    @RequestMapping(value = "/like/{eventnamepattern}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventListByPattern(@PathVariable("eventnamepattern") String eventNamePattern) throws ServiceException {
        return ResponseEntity.ok(eventService.getEventListByPattern(eventNamePattern));
    }


    @RequestMapping(value = "/{eventname}", method = RequestMethod.GET)
    public ResponseEntity<?> getEvent(@PathVariable("eventname") String eventName) throws ServiceException {

        return ResponseEntity.ok(eventService.getEventFromDb(eventName));
    }

    /**
     * @param eventDTO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody EventDTO eventDTO) throws ServiceException {

        if (eventDTO != null) {
            eventService.addEvent(eventDTO);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
                    .buildAndExpand(eventDTO.getUserName()).toUri();

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}

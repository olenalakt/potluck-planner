package com.olena.eventservice.controller;

import com.olena.eventservice.exception.ServiceException;
import com.olena.eventservice.model.EventDTO;
import com.olena.eventservice.service.EventService;
import com.olena.eventservice.service.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    GuestService guestService;

    // TODO extract username from JWT token and update queries

    /**
     * @param eventDTO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO) throws ServiceException {

        if (eventDTO != null) {

// TBD implement saga here -  rollback event if guests failed to  add
            eventDTO.setEventId(UUID.randomUUID().toString());
            eventService.addEvent(eventDTO);

            if (eventDTO.getGuests() != null) {
                guestService.processGuests(eventDTO);
            }

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{eventid}")
                    .buildAndExpand(eventDTO.getEventId()).toUri();

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{eventid}", method = RequestMethod.GET)
    public ResponseEntity<?> getEvent(@PathVariable("eventid") final String eventId) throws ServiceException {
        // returns event with guest info
        return ResponseEntity.ok(eventService.getEvent(eventId, true, guestService));
    }

    /**
     * @param userName
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @GetMapping(value = "/user/{username}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> getEventListByUserName(@PathVariable(name = "username") String userName) throws ServiceException {
        log.debug("getEventListByUserName");
        return ResponseEntity.ok(eventService.getEventListByUserName(userName));
    }


    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/eventname/{eventname}", method = RequestMethod.GET)
    public ResponseEntity<?> getEvent(@PathVariable("eventname") final String eventName, @RequestParam(name = "contains", required = false, defaultValue = "false") final Boolean contains) throws ServiceException {
        if (contains) {
            // return list with event names like requested, no  guest info
            return ResponseEntity.ok(eventService.getEventListByPattern(eventName));
        } else {
            // returns exact  match with guest info
            return ResponseEntity.ok(eventService.getEventByName(eventName, guestService));
        }
    }

    /**
     * @param eventDTO
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> addGuests(@RequestBody EventDTO eventDTO) throws ServiceException {

        if (eventDTO != null && eventDTO.getEventId() != null && eventDTO.getGuests() != null) {

            eventService.checkEvent(eventDTO);

            guestService.processGuests(eventDTO);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{eventid}")
                    .buildAndExpand(eventDTO.getEventId()).toUri();

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

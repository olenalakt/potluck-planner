package com.olena.eventservice.controller;

import com.olena.eventservice.exception.ServiceException;
import com.olena.eventservice.model.EventDTO;
import com.olena.eventservice.model.GuestDTO;
import com.olena.eventservice.repository.entity.Event;
import com.olena.eventservice.service.EventService;
import com.olena.eventservice.service.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    GuestService guestService;

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

        return ResponseEntity.ok(eventService.getEventByName(eventName, guestService));
    }

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

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
                    .buildAndExpand(eventDTO.getEventName()).toUri();

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    /**
     * @param eventDTO
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> addGuests(@RequestBody EventDTO eventDTO) throws ServiceException {

        if (eventDTO != null && eventDTO.getGuests() != null) {

//  query event from DB by  name to  get  the eventId
            Event event = eventService.getEventFromDb(eventDTO.getEventName());

            eventDTO.setEventId(event.getEventId().toString());
            guestService.processGuests(eventDTO);


            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
                    .buildAndExpand(eventDTO.getEventName()).toUri();

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

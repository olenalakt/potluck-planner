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

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/v1/events")
// TODO -  get rid of @CrossOrigin -  should use CORS regex external config instead
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    GuestService guestService;

    // ! only  for compatibility  with  SPA client
    // TODO extract username from JWT token and update queries
    String username = "olena";
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getEventListByUserName() throws ServiceException {
        log.debug("getEventListByUserName");

        // TODO - get username from JWT token later
        return ResponseEntity.ok(eventService.getEventListByUserName(username));
    }

    /**
     * @param userName
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/username/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventListByUserName(@PathVariable("username") String userName) throws ServiceException {
        log.debug("getEventListByUserName");

        // TODO - get username from JWT token later
        return ResponseEntity.ok(eventService.getEventListByUserName(userName));
    }

    /**
     *
     * @param userName
     * @param bearerToken
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/username/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteGuestByEventId(@PathVariable("username") String userName
            , @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {

        return ResponseEntity.ok(eventService.deleteGuestsByEventId(userName, bearerToken, guestService));
    }

    /**
     *
     * @param eventName
     * @param bearerToken
     * @param contains
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/eventname/{eventname}", method = RequestMethod.GET)
    public ResponseEntity<?> getEvent(@PathVariable("eventname") final String eventName,
                                      @RequestHeader(name = "Authorization") String bearerToken,
                                      @RequestParam(name = "contains", required = false, defaultValue = "false") final Boolean contains) throws ServiceException {

        // TODO - get username from JWT token later
        if (contains) {
            // return list with event names like requested, no  guest info
            return ResponseEntity.ok(eventService.getEventListByPattern(username, eventName));
        } else {
            // returns exact  match with guest info
            return ResponseEntity.ok(eventService.getEventByName(username, eventName, bearerToken, guestService));
        }
    }

    /**
     * @param eventDTO
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO,
                                         @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {

        if (eventDTO != null) {

// TODO implement saga here -  rollback event if guests failed to  add

            eventDTO.setEventId(UUID.randomUUID().toString());
            eventService.addEvent(eventDTO);

            if (eventDTO.getGuests() != null && eventDTO.getGuests().length > 0) {
                guestService.processGuests(eventDTO, bearerToken);
            }

            return ResponseEntity.ok(eventService.getEventFromDb(eventDTO.getEventId()));

        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * get  event by  id
     *
     * @param eventId
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{eventid}", method = RequestMethod.GET)
    public ResponseEntity<?> getEvent(@PathVariable("eventid") final String eventId,
                                      @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {
        // returns event with guest info
        return ResponseEntity.ok(eventService.getEvent(eventId, true, bearerToken, guestService));
    }

    /**
     *
     * @param eventDTO
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateGuest(@RequestBody EventDTO eventDTO) throws ServiceException {

        if (eventDTO == null || eventDTO.getEventId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(eventService.updateEvent(eventDTO));
    }


    /**
     * @param eventId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{eventid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEvent(@PathVariable("eventid") String eventId
            , @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {

        return ResponseEntity.ok(eventService.deleteEvent(eventId, bearerToken, guestService));
    }

    /**
     * @param eventDTO
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/event/guests", method = RequestMethod.PUT)
    public ResponseEntity<?> addGuests(@RequestBody EventDTO eventDTO,
                                       @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {

        if (eventDTO != null && eventDTO.getEventId() != null &&
                eventDTO.getGuests() != null && eventDTO.getGuests().length > 0) {

            Event event = eventService.checkEvent(eventDTO);

            guestService.processGuests(eventDTO, bearerToken);

            return ResponseEntity.ok(event);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/{eventid}/guests", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEvents(@PathVariable("eventid") String eventId
            , @RequestHeader(name = "Authorization") String bearerToken, @RequestBody GuestDTO[] guests) throws ServiceException {

        if (eventId == null || guests == null || guests.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(guestService.deleteGuests(eventId, bearerToken, guests, eventService));
    }

}

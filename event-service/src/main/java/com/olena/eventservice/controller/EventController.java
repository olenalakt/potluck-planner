package com.olena.eventservice.controller;

import com.olena.eventservice.exception.BadInputException;
import com.olena.eventservice.exception.ServiceException;
import com.olena.eventservice.model.EventDTO;
import com.olena.eventservice.model.GuestDTO;
import com.olena.eventservice.publisher.EventPublisher;
import com.olena.eventservice.repository.entity.Event;
import com.olena.eventservice.service.EventService;
import com.olena.eventservice.service.GuestService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    EventPublisher eventPublisher;

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
     * @param userName
     * @param bearerToken
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/username/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEventByUserName(@PathVariable("username") String userName
            , @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {

        return ResponseEntity.ok(eventService.deleteEventByUserName(userName, bearerToken, guestService, eventPublisher));
    }

    /**
     * @param eventName
     * @param bearerToken
     * @param contains
     * @return
     * @throws ServiceException
     */
// TODO  - move to  CQRS service
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/eventname/{eventname}", method = RequestMethod.GET)
    public ResponseEntity<?> getEvent(@PathVariable("eventname") final String eventName,
                                      @RequestHeader(name = "Authorization") String bearerToken,
                                      @RequestParam(name = "contains", required = false, defaultValue = "false") final Boolean contains) throws ServiceException {

        if (contains) {
            // return list with event names like requested, no  guest info
            return ResponseEntity.ok(eventService.getEventListByPattern(username, eventName));
        } else {
            // returns exact  match with guest info
            return ResponseEntity.ok(eventService.getEventByName(username, eventName, bearerToken, guestService));
        }
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
     * @param eventDTO
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO) throws ServiceException, BadInputException {

        if (!StringUtils.isBlank(eventDTO.getEventName())) {
            return ResponseEntity.ok(eventService.addEvent(eventDTO, eventPublisher));
        } else {
            throw new BadInputException("createEvent: empty event name");
        }
    }

    /**
     * @param eventDTO
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateGuest(@RequestBody EventDTO eventDTO) throws ServiceException, BadInputException {

        if (eventDTO.getEventId() == null) {
            return ResponseEntity.ok(eventService.updateEvent(eventDTO, eventPublisher));
        } else {
            throw new BadInputException("createEvent: eventId can not be null");
        }
    }

    /**
     * @param eventId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{eventid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEvent(@PathVariable("eventid") String eventId
            , @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {

        return ResponseEntity.ok(eventService.deleteEvent(eventId, bearerToken, guestService, eventPublisher));

    }

    /**
     * @param eventId
     * @param guests
     * @param bearerToken
     * @return
     * @throws ServiceException
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{eventid}/guests", method = RequestMethod.PUT)
    public ResponseEntity<?> addGuests(@PathVariable("eventid") String eventId, @RequestBody GuestDTO[] guests,
                                       @RequestHeader(name = "Authorization") String bearerToken)
            throws ServiceException, BadInputException {

        if (guests.length != 0) {

            guestService.processGuests(eventId, guests, bearerToken, eventService);

            return ResponseEntity.ok(eventService.getEvent(eventId, true, bearerToken, guestService));
        } else {
            throw new BadInputException("addGuests: guest list is empty");
        }
    }

    /**
     * @param eventId
     * @param guests
     * @param bearerToken
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/{eventid}/guests", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteGuests(@PathVariable("eventid") String eventId, @RequestBody GuestDTO[] guests,
                                          @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException, BadInputException {

        if (guests.length != 0) {

            guestService.deleteGuests(eventId, guests, bearerToken, eventService);

            return ResponseEntity.ok(eventService.getEvent(eventId, true, bearerToken, guestService));

        } else {
            throw new BadInputException("deleteGuests: delete guest list is empty");
        }
    }

}

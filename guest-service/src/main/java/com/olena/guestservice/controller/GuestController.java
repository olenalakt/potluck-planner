package com.olena.guestservice.controller;

import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.GuestDTO;
import com.olena.guestservice.service.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/guests")
public class GuestController {

    @Autowired
    GuestService guestService;

    //TBD extract username from JWT token and update queries

    /**
     * @param userName
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{username}/{eventid}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventListByUserName(@PathVariable("username") String userName, @PathVariable("eventid") String eventId) throws ServiceException {
        return ResponseEntity.ok(guestService.getGuestListByUserAndEvent(userName, UUID.fromString(eventId)));
    }

    /**
     * @param guestDTOList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateGuests(@RequestBody GuestDTO[] guestDTOList) throws ServiceException {

        if (guestDTOList == null || guestDTOList.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        guestService.addGuests(guestDTOList);

        return ResponseEntity.noContent().build();
    }

}

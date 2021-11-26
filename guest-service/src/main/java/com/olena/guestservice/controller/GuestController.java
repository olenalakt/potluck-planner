package com.olena.guestservice.controller;

import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.GuestDTO;
import com.olena.guestservice.service.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody List<GuestDTO> guestDTOList) throws ServiceException {

        if (guestDTOList != null) {
            guestService.addGuests(guestDTOList);
// TBD -  fix
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
                    .buildAndExpand(guestDTOList.get(0).getUserName()).toUri();

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}

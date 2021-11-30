package com.olena.guestservice.controller;

import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.DishDTO;
import com.olena.guestservice.model.GuestDTO;
import com.olena.guestservice.service.DishService;
import com.olena.guestservice.service.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/guests")
public class GuestController {

    @Autowired
    GuestService guestService;

    @Autowired
    DishService dishService;

    //TODO extract username from JWT token and update queries

    /**
     * @param guestDTOList
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateGuests(@RequestBody GuestDTO[] guestDTOList) throws ServiceException {

        if (guestDTOList == null || guestDTOList.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        guestService.addGuests(guestDTOList);

        return ResponseEntity.noContent().build();
    }

    /**
     * @param guestId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/{guestid}", method = RequestMethod.GET)
    public ResponseEntity<?> getGuestInfo(@PathVariable("guestid") String guestId) throws ServiceException {
        return ResponseEntity.ok(guestService.getGuestInfo(guestId, dishService));
    }

    @RequestMapping(value = "/{guestid}/dishes", method = RequestMethod.POST)
    public ResponseEntity<?> updateDishes(@PathVariable("guestid") String guestId, @RequestBody DishDTO[] dishes) throws ServiceException {

        if (guestId == null || dishes == null || dishes.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        dishService.processDishes(guestId, dishes);

        return ResponseEntity.noContent().build();
    }

    /**
     * @param eventId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/event/{eventid}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventListByUserName(@PathVariable("eventid") String eventId) throws ServiceException {
        return ResponseEntity.ok(guestService.getGuestListByEventId(UUID.fromString(eventId)));
    }

}

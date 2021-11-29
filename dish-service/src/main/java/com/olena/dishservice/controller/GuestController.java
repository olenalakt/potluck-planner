package com.olena.dishservice.controller;

import com.olena.dishservice.exception.ServiceException;
import com.olena.dishservice.model.DishDTO;
import com.olena.dishservice.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/dishes")
public class GuestController {

    @Autowired
    DishService dishService;

    /**
     * @param eventId
     * @param guestId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{eventid}/{guestid}", method = RequestMethod.GET)
    public ResponseEntity<?> getDishListByGuest(@PathVariable("eventid") String eventId, @PathVariable("guestid") String guestId) throws ServiceException {
        //TODO - check eventId
        return ResponseEntity.ok(dishService.getDishListByGuestId(UUID.fromString(guestId)));
    }

    /**
     * @param dishDTOList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateDishes(@RequestBody DishDTO[] dishDTOList) throws ServiceException {

        if (dishDTOList == null || dishDTOList.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        dishService.addDishes(dishDTOList);

        return ResponseEntity.noContent().build();
    }

}

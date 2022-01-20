package com.olena.guestservice.controller;

import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.DishDTO;
import com.olena.guestservice.model.DrinkDTO;
import com.olena.guestservice.model.GuestDTO;
import com.olena.guestservice.producer.PotluckEventPublisher;
import com.olena.guestservice.service.DishService;
import com.olena.guestservice.service.DrinkService;
import com.olena.guestservice.service.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/v1/guests")
public class GuestController {

    @Autowired
    GuestService guestService;

    @Autowired
    DishService dishService;

    @Autowired
    DrinkService drinkService;

    @Autowired
    PotluckEventPublisher potluckEventPublisher;

    /**
     * @param eventId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/event/{eventid}", method = RequestMethod.GET)
    public ResponseEntity<?> getGuestListByEventId(@PathVariable("eventid") String eventId) throws ServiceException {
        return ResponseEntity.ok(guestService.getGuestListByEventId(UUID.fromString(eventId)));
    }

    /**
     *
     * @param eventId
     * @param bearerToken
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/event/{eventid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteGuestByEventId(@PathVariable("eventid") String eventId
            , @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {

        return ResponseEntity.ok(guestService.deleteGuestsByEventId(eventId, bearerToken, dishService, drinkService));
    }

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

        return ResponseEntity.ok(guestService.addGuests(guestDTOList, potluckEventPublisher));
    }

    /**
     * @param guestDTOList
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteGuests(@RequestBody GuestDTO[] guestDTOList
            , @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {

        if (guestDTOList == null || guestDTOList.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(guestService.deleteGuests(guestDTOList, bearerToken, dishService, drinkService, potluckEventPublisher));
    }

    /**
     * @param guestId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/{guestid}", method = RequestMethod.GET)
    public ResponseEntity<?> getGuestInfo(@PathVariable("guestid") String guestId,
                                          @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {
        return ResponseEntity.ok(guestService.getGuestInfo(guestId, bearerToken, dishService, drinkService));
    }

    /**
     * @param guestDTO
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/guest", method = RequestMethod.PUT)
    public ResponseEntity<?> updateGuest(@RequestBody GuestDTO guestDTO) throws ServiceException {

        if (guestDTO == null || guestDTO.getGuestId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(guestService.updateGuest(guestDTO, potluckEventPublisher));
    }

    /**
     * @param guestId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/{guestid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteGuest(@PathVariable("guestid") String guestId
            , @RequestHeader(name = "Authorization") String bearerToken) throws ServiceException {

        return ResponseEntity.ok(guestService.deleteGuest(guestId, bearerToken, dishService, drinkService, potluckEventPublisher));
    }

    /**
     * @param guestId
     * @param dishes
     * @return
     * @throws ServiceException
     */
//    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/{guestid}/dishes", method = RequestMethod.POST)
    public ResponseEntity<?> updateDishes(@PathVariable("guestid") String guestId
            , @RequestHeader(name = "Authorization") String bearerToken, @RequestBody DishDTO[] dishes) throws ServiceException {

        if (guestId == null || dishes == null || dishes.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(dishService.processDishes(guestId, bearerToken, dishes, guestService));
    }

    /**
     * @param guestId
     * @param dishes
     * @return
     * @throws ServiceException
     */
//    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/{guestid}/dishes", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDishes(@PathVariable("guestid") String guestId
            , @RequestHeader(name = "Authorization") String bearerToken, @RequestBody DishDTO[] dishes) throws ServiceException {

        if (guestId == null || dishes == null || dishes.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(dishService.deleteDishes(guestId, bearerToken, dishes, guestService));
    }

    /**
     * @param guestId
     * @param drinks
     * @return
     * @throws ServiceException
     */
//    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/{guestid}/drinks", method = RequestMethod.POST)
    public ResponseEntity<?> updateDrinks(@PathVariable("guestid") String guestId
            , @RequestHeader(name = "Authorization") String bearerToken, @RequestBody DrinkDTO[] drinks) throws ServiceException {

        if (guestId == null || drinks == null || drinks.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(drinkService.processDrinks(guestId, bearerToken, drinks, guestService));
    }

    /**
     * @param guestId
     * @param drinks
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('guest')")
    @RequestMapping(value = "/{guestid}/drinks", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDrinks(@PathVariable("guestid") String guestId
            , @RequestHeader(name = "Authorization") String bearerToken, @RequestBody DrinkDTO[] drinks) throws ServiceException {

        if (guestId == null || drinks == null || drinks.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(drinkService.deleteDrinks(guestId, bearerToken, drinks, guestService));
    }

}

package com.olena.drinkservice.controller;

import com.olena.drinkservice.exception.ServiceException;
import com.olena.drinkservice.model.DrinkDTO;
import com.olena.drinkservice.producer.PotluckEventPublisher;
import com.olena.drinkservice.service.DrinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/v1/drinks")
public class DrinkController {

    @Autowired
    DrinkService drinkService;

    @Autowired
    PotluckEventPublisher potluckEventPublisher;

    /**
     * @param guestId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('drink')")
    @RequestMapping(value = "/guest/{guestid}", method = RequestMethod.GET)
    public ResponseEntity<?> getDrinkListByGuest(@PathVariable("guestid") String guestId) throws ServiceException {
        return ResponseEntity.ok(drinkService.getDrinkListByGuestId(UUID.fromString(guestId)));
    }


    /**
     * @param drinkDTOList
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('drink')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> updateDrinks(@RequestBody DrinkDTO[] drinkDTOList) throws ServiceException {

        if (drinkDTOList == null || drinkDTOList.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        drinkService.updateDrinks(drinkDTOList, potluckEventPublisher);

        return ResponseEntity.noContent().build();
    }

    //    @PreAuthorize("#oauth2.hasScope('drink')")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDrinks(@RequestBody DrinkDTO[] drinkDTOList) throws ServiceException {

        if (drinkDTOList == null || drinkDTOList.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        drinkService.deleteDrinks(drinkDTOList, potluckEventPublisher);

        return ResponseEntity.noContent().build();
    }

    /**
     * @param guestId
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('drink')")
    @RequestMapping(value = "/guest/{guestid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDishesByGuestId(@PathVariable("guestid") String guestId) throws ServiceException {
        return ResponseEntity.ok(drinkService.deleteDrinksByGuestId(UUID.fromString(guestId), potluckEventPublisher));
    }
}

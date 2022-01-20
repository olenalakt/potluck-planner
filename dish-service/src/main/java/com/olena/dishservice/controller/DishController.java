package com.olena.dishservice.controller;

import com.olena.dishservice.exception.ServiceException;
import com.olena.dishservice.model.DishDTO;
import com.olena.dishservice.producer.PotluckEventPublisher;
import com.olena.dishservice.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/v1/dishes")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    PotluckEventPublisher potluckEventPublisher;

    /**
     * @param guestId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('dish')")
    @RequestMapping(value = "/guest/{guestid}", method = RequestMethod.GET)
    public ResponseEntity<?> getDishListByGuest(@PathVariable("guestid") String guestId) throws ServiceException {
        return ResponseEntity.ok(dishService.getDishListByGuestId(UUID.fromString(guestId)));
    }

    /**
     * @param dishDTOList
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('dish')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> updateDishes(@RequestBody DishDTO[] dishDTOList) throws ServiceException {

        if (dishDTOList == null || dishDTOList.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        dishService.updateDishes(dishDTOList, potluckEventPublisher);

        return ResponseEntity.noContent().build();
    }

    /**
     * @param dishDTOList
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('dish')")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDishes(@RequestBody DishDTO[] dishDTOList) throws ServiceException {

        if (dishDTOList == null || dishDTOList.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        dishService.deleteDishes(dishDTOList, potluckEventPublisher);

        return ResponseEntity.noContent().build();
    }

    /**
     * @param guestId
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('dish')")
    @RequestMapping(value = "/guest/{guestid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDishesByGuestId(@PathVariable("guestid") String guestId) throws ServiceException {
        return ResponseEntity.ok(dishService.deleteDishesByGuestId(UUID.fromString(guestId), potluckEventPublisher));
    }

}

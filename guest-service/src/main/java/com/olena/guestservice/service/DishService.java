package com.olena.guestservice.service;

import com.olena.guestservice.config.GuestServiceProperties;
import com.olena.guestservice.enums.Constants;
import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.DishDTO;
import com.olena.guestservice.repository.entity.Guest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;

@Service
@Slf4j
public class DishService {

    @Autowired
    GuestServiceProperties guestServiceProperties;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * @param guestId
     * @return
     * @throws ServiceException
     */
    public DishDTO[] getDishList(String guestId) throws ServiceException {
        log.debug("getDishList: guestId={}", guestId);

        URI uri = URI.create(guestServiceProperties.getDishServiceUrl() + "/" + Constants.GUEST.getValue() + "/" + guestId);
        try {

            DishDTO[] dishes = restTemplate.getForObject(uri, DishDTO[].class);

            return dishes;

        } catch (Exception e) {
            String errMsg = "Failed to get guest dishes from " + uri + ": " + e;
            log.error("getDishList: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param guestId
     * @param dishes
     * @throws ServiceException
     */
    public Guest processDishes(String guestId, DishDTO[] dishes, GuestService guestService) throws ServiceException {
        // call guest service to process guests
        log.debug("processDishes: guestId={}, dishes={}", guestId, dishes.length);

        URI uri = URI.create(guestServiceProperties.getDishServiceUrl());
        try {

            Guest guest = guestService.getGuestFromDb(guestId);

            restTemplate.postForObject(uri, dishes, DishDTO[].class);

            return guest;
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            String errMsg = "Failed to update guest dishes for " + uri + ": " + e;
            log.error("processDishes: guestId={}, dishes={}, {}", guestId, dishes.length, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     *
     * @param guestId
     * @param dishes
     * @param guestService
     * @return
     * @throws ServiceException
     */
    public Guest deleteDishes(String guestId, DishDTO[] dishes, GuestService guestService) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteDishes: guestId={}, dishes={}", guestId, dishes.length);

        URI uri = URI.create(guestServiceProperties.getDishServiceUrl());
        try {

            Guest guest = guestService.getGuestFromDb(guestId);

            restTemplate.delete(uri.toString(), dishes, DishDTO[].class);

            return guest;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            String errMsg = "Failed to delete guest dishes for " + uri + ": " + e;
            log.error("deleteDishes: guestId={}, dishes={}, {}", guestId, dishes.length, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     *
     * @param guestId
     * @throws ServiceException
     */
    public void deleteDishesByGuest(String guestId) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteDishesByGuest: guestId={}", guestId);

        URI uri = URI.create(guestServiceProperties.getDishServiceUrl() + "/guest/"+ guestId);
        try {

            restTemplate.delete(uri);

        } catch (Exception e) {
            String errMsg = "Failed to delete guest dishes for " + uri + ": " + e;
            log.error("deleteDishesByGuest: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

}

package com.olena.guestservice.service;

import com.olena.guestservice.config.GuestServiceConfig;
import com.olena.guestservice.enums.Constants;
import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.DrinkDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class DrinkService {

    @Autowired
    GuestServiceConfig guestServiceConfig;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * @param guestId
     * @return
     * @throws ServiceException
     */
    public DrinkDTO[] getDrinkList(String guestId) throws ServiceException {
        log.debug("getDrinkList: guestId={}", guestId);

        URI uri = URI.create(guestServiceConfig.getDrinkServiceUrl() + "/" + Constants.GUEST.getValue() + "/" + guestId);
        try {

            DrinkDTO[] drinks = restTemplate.getForObject(uri, DrinkDTO[].class);

            return drinks;

        } catch (Exception e) {
            String errMsg = "Failed to get guest drinks from " + uri + ": " + e;
            log.error("getDrinkList: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param guestId
     * @param drinks
     * @throws ServiceException
     */
    public void processDrinks(String guestId, DrinkDTO[] drinks) throws ServiceException {
        // call guest service to process guests
        log.debug("processDrinks: guestId={}, drinks={}", guestId, drinks.length);

        URI uri = URI.create(guestServiceConfig.getDrinkServiceUrl());
        try {
            restTemplate.postForObject(uri, drinks, DrinkDTO[].class);
        } catch (Exception e) {
            String errMsg = "Failed to update guest drinks for " + uri + ": " + e;
            log.error("processDrinks: guestId={}, drinks={}, {}", guestId, drinks.length, errMsg);
            throw new ServiceException(errMsg);
        }
    }

}

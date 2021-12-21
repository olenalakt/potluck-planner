package com.olena.guestservice.service;

import com.olena.guestservice.config.GuestServiceProperties;
import com.olena.guestservice.enums.Constants;
import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.DishDTO;
import com.olena.guestservice.model.DrinkDTO;
import com.olena.guestservice.repository.entity.Guest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class DrinkService {

    @Autowired
    GuestServiceProperties guestServiceProperties;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * @param guestId
     * @return
     * @throws ServiceException
     */
    public DrinkDTO[] getDrinkList(String guestId, String bearerToken) throws ServiceException {
        log.debug("getDrinkList: guestId={}, bearerToken={}", guestId, bearerToken);

        URI uri = URI.create(guestServiceProperties.getDrinkServiceUrl() + "/" + Constants.GUEST.getValue() + "/" + guestId);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", bearerToken);

            HttpEntity<Void> entityReq = new HttpEntity<>(headers);

            ResponseEntity<DrinkDTO[]> respEntity = restTemplate
                    .exchange(uri.toString(), HttpMethod.GET, entityReq, DrinkDTO[].class);

//            DrinkDTO[] drinks = restTemplate.getForObject(uri, DrinkDTO[].class);
//            return drinks;

            return respEntity.getBody();

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
    public Guest processDrinks(String guestId, DrinkDTO[] drinks, GuestService guestService) throws ServiceException {
        // call guest service to process guests
        log.debug("processDrinks: guestId={}, drinks={}", guestId, drinks.length);

        URI uri = URI.create(guestServiceProperties.getDrinkServiceUrl());
        try {

            Guest guest = guestService.getGuestFromDb(guestId);

            restTemplate.postForObject(uri, drinks, DrinkDTO[].class);

            return guest;

        } catch (Exception e) {
            String errMsg = "Failed to update guest drinks for " + uri + ": " + e;
            log.error("processDrinks: guestId={}, drinks={}, {}", guestId, drinks.length, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    public Guest deleteDrinks(String guestId, DrinkDTO[] drinks, GuestService guestService) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteDrinks: guestId={}, drinks={}", guestId, drinks);

        URI uri = URI.create(guestServiceProperties.getDrinkServiceUrl());
        try {

            Guest guest = guestService.getGuestFromDb(guestId);

            restTemplate.delete(uri.toString(), drinks, DrinkDTO[].class);

            return guest;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            String errMsg = "Failed to delete guest drinks for " + uri + ": " + e;
            log.error("deleteDrinks: guestId={}, drinks={}, {}", guestId, drinks, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    public void deleteDrinksByGuest(String guestId) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteDrinksByGuest: guestId={}", guestId);

        URI uri = URI.create(guestServiceProperties.getDrinkServiceUrl() + "/guest/"+ guestId);
        try {

            restTemplate.delete(uri);

        } catch (Exception e) {
            String errMsg = "Failed to delete guest drinks for " + uri + ": " + e;
            log.error("deleteDrinksByGuest: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

}

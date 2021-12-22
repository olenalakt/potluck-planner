package com.olena.guestservice.service;

import com.olena.guestservice.config.GuestServiceProperties;
import com.olena.guestservice.enums.Constants;
import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.DrinkDTO;
import com.olena.guestservice.repository.entity.Guest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        log.debug("getDrinkList: guestId={}", guestId);

        String url = guestServiceProperties.getDrinkServiceUrl() + "/" + Constants.GUEST.getValue() + "/" + guestId;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<Void> entityReq = new HttpEntity<>(headers);

            ResponseEntity<DrinkDTO[]> respEntity = restTemplate
                    .exchange(url, HttpMethod.GET, entityReq, DrinkDTO[].class);

            return respEntity.getBody();

        } catch (Exception e) {
            String errMsg = "Failed to get guest drinks from " + url + ": " + e;
            log.error("getDrinkList: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param guestId
     * @param drinks
     * @throws ServiceException
     */
    public Guest processDrinks(String guestId, String bearerToken, DrinkDTO[] drinks, GuestService guestService) throws ServiceException {
        // call guest service to process guests
        log.debug("processDrinks: guestId={}, drinks={}", guestId, drinks);

        String url = guestServiceProperties.getDrinkServiceUrl();
        try {

            Guest guest = guestService.getGuestFromDb(guestId);

//            restTemplate.postForObject(uri, drinks, DrinkDTO[].class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<DrinkDTO[]> entityReq = new HttpEntity<>(drinks, headers);

            //ResponseEntity<?> respEntity =
            restTemplate.exchange(url, HttpMethod.POST, entityReq, Void.class);

            return guest;

        } catch (Exception e) {
            String errMsg = "Failed to update guest drinks for " + url + ": " + e;
            log.error("processDrinks: guestId={}, drinks={}, {}", guestId, drinks, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    public Guest deleteDrinks(String guestId, String bearerToken, DrinkDTO[] drinks, GuestService guestService) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteDrinks: guestId={}, drinks={}", guestId, drinks);

        String url = guestServiceProperties.getDrinkServiceUrl();
        try {

            Guest guest = guestService.getGuestFromDb(guestId);

//            restTemplate.delete(uri.toString(), drinks, DrinkDTO[].class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<DrinkDTO[]> entityReq = new HttpEntity<>(drinks, headers);

            //ResponseEntity<?> respEntity =
            restTemplate.exchange(url, HttpMethod.DELETE, entityReq, Void.class);

            return guest;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            String errMsg = "Failed to delete guest drinks for " + url + ": " + e;
            log.error("deleteDrinks: guestId={}, drinks={}, {}", guestId, drinks, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    public void deleteDrinksByGuest(String guestId, String bearerToken) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteDrinksByGuest: guestId={}", guestId);

        String url = guestServiceProperties.getDrinkServiceUrl() + "/guest/" + guestId;
        try {

            //            restTemplate.delete(uri);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<String> entityReq = new HttpEntity<>(guestId, headers);

            //ResponseEntity<?> respEntity =
            restTemplate.exchange(url, HttpMethod.DELETE, entityReq, Void.class);

        } catch (Exception e) {
            String errMsg = "Failed to delete guest drinks for " + url + ": " + e;
            log.error("deleteDrinksByGuest: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

}

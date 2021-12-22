package com.olena.guestservice.service;

import com.olena.guestservice.config.GuestServiceProperties;
import com.olena.guestservice.enums.Constants;
import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.DishDTO;
import com.olena.guestservice.repository.entity.Guest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public DishDTO[] getDishList(String guestId, String bearerToken) throws ServiceException {
        log.debug("getDishList: guestId={}", guestId);

//        URI uri = URI.create(guestServiceProperties.getDishServiceUrl() + "/" + Constants.GUEST.getValue() + "/" + guestId);
        String url = guestServiceProperties.getDishServiceUrl() + "/" + Constants.GUEST.getValue() + "/" + guestId;
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<Void> entityReq = new HttpEntity<>(headers);

            ResponseEntity<DishDTO[]> respEntity = restTemplate
                    .exchange(url, HttpMethod.GET, entityReq, DishDTO[].class);
/*
            DishDTO[] dishes = restTemplate.getForObject(uri, DishDTO[].class);
            return dishes;
*/
            return respEntity.getBody();

        } catch (Exception e) {
            String errMsg = "Failed to get guest dishes from " + url + ": " + e;
            log.error("getDishList: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param guestId
     * @param dishes
     * @throws ServiceException
     */
    public Guest processDishes(String guestId, String bearerToken, DishDTO[] dishes, GuestService guestService) throws ServiceException {
        // call guest service to process guests
        log.debug("processDishes: guestId={}, dishes={}", guestId, dishes);

        String url = guestServiceProperties.getDishServiceUrl();
        try {

            Guest guest = guestService.getGuestFromDb(guestId);

            //restTemplate.postForObject(uri, dishes, DishDTO[].class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<DishDTO[]> entityReq = new HttpEntity<>(dishes, headers);

            //ResponseEntity<?> respEntity =
            restTemplate.exchange(url, HttpMethod.POST, entityReq, Void.class);

            return guest;
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            String errMsg = "Failed to update guest dishes for " + url + ": " + e;
            log.error("processDishes: guestId={}, dishes={}, {}", guestId, dishes, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param guestId
     * @param dishes
     * @param guestService
     * @return
     * @throws ServiceException
     */
    public Guest deleteDishes(String guestId, String bearerToken, DishDTO[] dishes, GuestService guestService) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteDishes: guestId={}, dishes={}", guestId, dishes);

        String url = guestServiceProperties.getDishServiceUrl();
        try {

            Guest guest = guestService.getGuestFromDb(guestId);

//            restTemplate.delete(uri.toString(), dishes, DishDTO[].class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<DishDTO[]> entityReq = new HttpEntity<>(dishes, headers);

            //ResponseEntity<?> respEntity =
            restTemplate.exchange(url, HttpMethod.DELETE, entityReq, Void.class);

            return guest;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            String errMsg = "Failed to delete guest dishes for " + url + ": " + e;
            log.error("deleteDishes: guestId={}, dishes={}, {}", guestId, dishes, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param guestId
     * @throws ServiceException
     */
    public void deleteDishesByGuest(String guestId, String bearerToken) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteDishesByGuest: guestId={}", guestId);

        String url = guestServiceProperties.getDishServiceUrl() + "/guest/" + guestId;
        try {

            //restTemplate.delete(uri);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<String> entityReq = new HttpEntity<>(guestId, headers);

            //ResponseEntity<?> respEntity =
            restTemplate.exchange(url, HttpMethod.DELETE, entityReq, Void.class);

        } catch (Exception e) {
            String errMsg = "Failed to delete guest dishes for " + url + ": " + e;
            log.error("deleteDishesByGuest: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

}

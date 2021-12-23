package com.olena.eventservice.service;

import com.olena.eventservice.config.EventServiceProperties;
import com.olena.eventservice.enums.Constants;
import com.olena.eventservice.exception.ServiceException;
import com.olena.eventservice.model.EventDTO;
import com.olena.eventservice.model.GuestDTO;
import com.olena.eventservice.publisher.EventPublisher;
import com.olena.eventservice.repository.entity.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GuestService {

    @Autowired
    EventServiceProperties eventServiceProperties;

    RestTemplate restTemplate = new RestTemplate();


    /**
     *
     * @param eventDTO
     * @param bearerToken
     * @return
     * @throws ServiceException
     */
    public GuestDTO[] getGuestList(EventDTO eventDTO, String bearerToken) throws ServiceException {
        log.debug("OL: getGuestList: eventDTO={}", eventDTO.toString());

//        URI uri = URI.create(eventServiceProperties.getGuestServiceUrl() + "/" + Constants.EVENT.getValue() + "/" + eventDTO.getEventId());
        String url = eventServiceProperties.getGuestServiceUrl() + "/" + Constants.EVENT.getValue() + "/" + eventDTO.getEventId();
        try {


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<Void> entityReq = new HttpEntity<>(headers);

            ResponseEntity<GuestDTO[]> respEntity = restTemplate
                    .exchange(url, HttpMethod.GET, entityReq, GuestDTO[].class);
/*
            GuestDTO[] guestList = restTemplate.getForObject(uri, GuestDTO[].class);
            return guestList;
*/
            return respEntity.getBody();

        } catch (Exception e) {
            String errMsg = "Failed to get guest list  from " + url + ": " + e;
            log.error("OL: getGuestList: eventDTO={}, {}", eventDTO, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     *
     * @param eventId
     * @param bearerToken
     * @throws ServiceException
     */
    public void deleteGuestsByEvent(String eventId, String bearerToken) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteGuestsByEvent: eventId={}", eventId);

        String url = eventServiceProperties.getGuestServiceUrl() + "/event/" + eventId;
        try {

            //restTemplate.delete(uri);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<String> entityReq = new HttpEntity<>(eventId, headers);

            //ResponseEntity<?> respEntity =
            restTemplate.exchange(url, HttpMethod.DELETE, entityReq, Void.class);

        } catch (Exception e) {
            String errMsg = "Failed to delete event guests for " + url + ": " + e;
            log.error("deleteGuestsByEvent: eventId={}, {}", eventId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     *
     * @param eventId
     * @param guests
     * @param bearerToken
     * @param eventService
     * @throws ServiceException
     */
    public void processGuests(String eventId, GuestDTO[] guests, String bearerToken, EventService eventService) throws ServiceException {
        // call guest service to  process guests
        log.debug("processGuests: eventId={}", eventId);

        String url = eventServiceProperties.getGuestServiceUrl();

            try {

                Event event = eventService.getEventFromDb(eventId);
                List<GuestDTO> guestDTOList = prepareGuestList(event, guests);

//                restTemplate.put(uri, guestDTOList);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", bearerToken);

                HttpEntity<List<GuestDTO>> entityReq = new HttpEntity<>(guestDTOList, headers);

                //ResponseEntity<?> respEntity =
                restTemplate.exchange(url, HttpMethod.PUT, entityReq, Void.class);

            } catch (Exception e) {
                String errMsg = "Failed to update guest list for " + url + ": " + e;
                log.error("processGuests: eventId={}, {}", eventId, errMsg);
                throw new ServiceException(errMsg);
            }
    }

    /**
     *
     * @param event
     * @param guests
     * @return
     * @throws ServiceException
     */
    public List<GuestDTO> prepareGuestList(Event event, GuestDTO[] guests) throws ServiceException {
        log.debug("prepareGuestList: event={}", event.toString());

        List<GuestDTO> guestDTOList = new ArrayList<>();
        try {
            for (GuestDTO guest : guests) {

                guest.setEventId(event.getEventId().toString());
                guest.setUserName(event.getUserName());
                guestDTOList.add(guest);
            }

            return guestDTOList;
        } catch (Exception e) {
            String errMsg = "Failed to prepare guest list from " + event.getEventName() + ": " + e;
            log.error("prepareGuestList: event={}, {}", event.toString(), errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param eventId
     * @param bearerToken
     * @param guests
     * @param eventService
     * @return
     * @throws ServiceException
     */
    public void deleteGuests(String eventId, GuestDTO[] guests, String bearerToken,
                              EventService eventService) throws ServiceException {
        // call guest service to process guests
        log.debug("deleteGuests: eventId={}, guests={}", eventId, guests);

        String url = eventServiceProperties.getGuestServiceUrl();
        try {

            Event event = eventService.getEventFromDb(eventId);

//            restTemplate.delete(uri.toString(), dishes, DishDTO[].class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bearerToken);

            HttpEntity<GuestDTO[]> entityReq = new HttpEntity<>(guests, headers);

            //ResponseEntity<?> respEntity =
            restTemplate.exchange(url, HttpMethod.DELETE, entityReq, Void.class);

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            String errMsg = "Failed to delete event guests for " + url + ": " + e;
            log.error("deleteGuests: eventId={}, guests={}, {}", eventId, guests, errMsg);
            throw new ServiceException(errMsg);
        }
    }

}

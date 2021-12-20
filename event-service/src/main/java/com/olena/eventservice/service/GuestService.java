package com.olena.eventservice.service;

import com.olena.eventservice.config.EventServiceProperties;
import com.olena.eventservice.enums.Constants;
import com.olena.eventservice.exception.ServiceException;
import com.olena.eventservice.model.EventDTO;
import com.olena.eventservice.model.GuestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GuestService {

    @Autowired
    EventServiceProperties eventServiceProperties;

//    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private OAuth2RestTemplate restTemplate;

    /**
     * @param eventDTO
     * @return
     * @throws ServiceException
     */
    public GuestDTO[] getGuestList(EventDTO eventDTO) throws ServiceException {
        log.debug("OL: getGuestList: eventDTO={}", eventDTO.toString());

        URI uri = URI.create(eventServiceProperties.getGuestServiceUrl() + "/" + Constants.EVENT.getValue() + "/" + eventDTO.getEventId());
        try {

            GuestDTO[] guestList = restTemplate.getForObject(uri, GuestDTO[].class);

            return guestList;

        } catch (Exception e) {
            String errMsg = "Failed to get guest list  from " + uri + ": " + e;
            log.error("OL: getGuestList: eventDTO={}, {}", eventDTO, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param eventDTO
     * @throws ServiceException
     */
    public void processGuests(EventDTO eventDTO) throws ServiceException {
        // call guest service to  process guests
        log.debug("processGuests: eventDTO={}", eventDTO.toString());

        //TODO replace with asynch via Kafka
        URI uri = URI.create(eventServiceProperties.getGuestServiceUrl());
        List<GuestDTO> guestDTOList = prepareGuestList(eventDTO);
        if (guestDTOList != null && guestDTOList.size() > 0) {
            try {
                restTemplate.put(uri, guestDTOList);
            } catch (Exception e) {
                String errMsg = "Failed to update guest list for " + uri + ": " + e;
                log.error("processGuests: eventDTO={}, {}", eventDTO, errMsg);
                throw new ServiceException(errMsg);
            }
        }
    }

    /**
     * @param eventDTO
     * @return
     * @throws ServiceException
     */
    public List<GuestDTO> prepareGuestList(EventDTO eventDTO) throws ServiceException {
        log.debug("prepareGuestList: eventDTO={}", eventDTO.toString());

        List<GuestDTO> guestDTOList = new ArrayList<>();
        try {
            for (GuestDTO guest : eventDTO.getGuests()) {

                guest.setEventId(eventDTO.getEventId());
                guest.setUserName(eventDTO.getUserName());

                guestDTOList.add(guest);
            }

            return guestDTOList;
        } catch (Exception e) {
            String errMsg = "Failed to prepare guest list from " + eventDTO.getEventName() + ": " + e;
            log.error("prepareGuestList: eventDTO={}, {}", eventDTO, errMsg);
            throw new ServiceException(errMsg);
        }
    }


}

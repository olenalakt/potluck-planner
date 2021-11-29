package com.olena.eventservice.service;

import com.olena.eventservice.config.EventServiceConfig;
import com.olena.eventservice.exception.ServiceException;
import com.olena.eventservice.model.EventDTO;
import com.olena.eventservice.model.GuestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GuestService {

    @Autowired
    EventServiceConfig eventServiceConfig;


    /**
     * @param event
     * @return
     * @throws ServiceException
     */
    public List<GuestDTO> prepareGuestList(EventDTO event) throws ServiceException {
        log.debug("prepareGuestList: event={}", event.toString());

        List<GuestDTO> guestDTOList = new ArrayList<>();
        for (GuestDTO guest : event.getGuests()) {

            guest.setEventId(event.getEventId());
            guest.setUserName(event.getUserName());

            guestDTOList.add(guest);
        }

        return guestDTOList;
    }


}

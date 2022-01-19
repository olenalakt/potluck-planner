package com.olena.menuservice.service;

import com.olena.menuservice.config.KafkaProperties;
import com.olena.menuservice.config.MenuServiceProperties;
import com.olena.menuservice.exception.ServiceException;
import com.olena.menuservice.repository.MenuRepository;
import com.olena.menuservice.repository.entity.EventMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class MenuService {

    @Autowired
    MenuServiceProperties menuServiceProperties;

    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired
    MenuRepository menuRepository;

    public EventMenu getEventMenu(String eventId) throws ServiceException {
        log.debug("getEventMenu: eventId={}", eventId);

        try {
            EventMenu eventMenu = menuRepository.findFirstByEventId(UUID.fromString(eventId));
            if (eventMenu == null) {
                String errMsg = "Event Menu not found";
                log.error("getEventMenu: eventId={}, {}", eventId, errMsg);
                throw new ServiceException(errMsg);
            }

            return eventMenu;
        } catch (Exception e) {
            String errMsg = "Unexpected exception: " + e;
            log.error("getEventMenu: eventId={}, {}", eventId, errMsg);
            throw new ServiceException(errMsg);
        }

    }

}

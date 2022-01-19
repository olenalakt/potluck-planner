package com.olena.menuservice.service;

import com.olena.menuservice.config.MenuServiceProperties;
import com.olena.menuservice.enums.ActionEnum;
import com.olena.menuservice.exception.ServiceException;
import com.olena.menuservice.model.EventMenuDTO;
import com.olena.menuservice.model.Guest;
import com.olena.menuservice.repository.MenuRepository;
import com.olena.menuservice.repository.entity.EventMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MenuService {

    @Autowired
    MenuServiceProperties menuServiceProperties;

    @Autowired
    MenuRepository menuRepository;

    /**
     * @param eventId
     * @return
     * @throws ServiceException
     */
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

    public ActionEnum getActionEnum(String actionCode) {

        if (actionCode.equalsIgnoreCase(ActionEnum.UPDATE.getCode())) {
            return ActionEnum.UPDATE;
        }
        if (actionCode.equalsIgnoreCase(ActionEnum.DELETE.getCode())) {
            return ActionEnum.DELETE;
        }

        // all other cases
        return ActionEnum.ADD;

    }

    public void processEventMenuDTO(EventMenuDTO eventMenuDTO) {

        log.debug("processEventMenuDTO: eventMenuDTO={}", eventMenuDTO);

        if (eventMenuDTO != null && eventMenuDTO.getEventId() != null && eventMenuDTO.getActionType() != null) {

            EventMenu existingEventMenu = menuRepository.findFirstByEventId(UUID.fromString(eventMenuDTO.getEventId()));

            ActionEnum action = getActionEnum(eventMenuDTO.getActionType());
            EventMenu eventMenu = null;
                    switch (action) {
                case ADD: {
                    eventMenu = setData(eventMenuDTO, existingEventMenu);
                    break;
                }
                case UPDATE: {
                    eventMenu = setData(eventMenuDTO, existingEventMenu);
                    break;
                }
                case DELETE: {
                    eventMenu = deleteData(eventMenuDTO, existingEventMenu);
                    break;
                }
            }
            if (eventMenu  != null) {
                log.debug("processEventMenuDTO: eventMenu={}", eventMenu);
                menuRepository.save(eventMenu);
            }
        }

    }

    public EventMenu setData(EventMenuDTO eventMenuDTO, EventMenu existingEventMenu) {
        log.debug("setData: eventMenuDTO={}", eventMenuDTO);
        EventMenu eventMenu = new EventMenu(eventMenuDTO, menuServiceProperties);

        if (existingEventMenu != null && eventMenuDTO.getGuest() != null) {
            eventMenu = setGuest(eventMenuDTO.getGuest(), existingEventMenu);
            return eventMenu;
        }
        if (existingEventMenu != null && eventMenuDTO.getDish() != null) {
//            eventMenu = setDish(eventMenuDTO.getDish(), existingEventMenu);
            return eventMenu;
        }
        if (existingEventMenu != null && eventMenuDTO.getDrink() != null) {
 //           eventMenu = setDrink(eventMenuDTO.getDrink(), existingEventMenu);
            return eventMenu;
        }

        if (existingEventMenu != null && existingEventMenu.getId() != null) {
            eventMenu.setId(existingEventMenu.getId());
        }
        log.debug("setData: eventMenu={}", eventMenu);
        return eventMenu;

    }

    public EventMenu setGuest(Guest guest, EventMenu existingEventMenu) {
        log.debug("setGuest: guest={}", guest);
        EventMenu eventMenu = existingEventMenu;
        boolean isFound =  false;
        for (Guest existingGuest:  eventMenu.getGuests()) {
            if (existingGuest.getGuestId().equals(guest.getGuestId())) {
                isFound =  true;
            }
        }
        if (!isFound) {

        }
        return eventMenu;
    }
        public EventMenu deleteData(EventMenuDTO eventMenuDTO, EventMenu existingEventMenu) {
        log.debug("deleteData: eventMenuDTO={}", eventMenuDTO);
        EventMenu eventMenu = existingEventMenu;
// TODO delete
        return eventMenu;
    }

}

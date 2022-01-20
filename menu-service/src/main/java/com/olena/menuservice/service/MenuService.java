package com.olena.menuservice.service;

import com.olena.menuservice.config.MenuServiceProperties;
import com.olena.menuservice.enums.ActionEnum;
import com.olena.menuservice.enums.MessageTypeEnum;
import com.olena.menuservice.exception.ServiceException;
import com.olena.menuservice.model.Dish;
import com.olena.menuservice.model.Drink;
import com.olena.menuservice.model.EventMenuDTO;
import com.olena.menuservice.model.Guest;
import com.olena.menuservice.repository.MenuRepository;
import com.olena.menuservice.repository.entity.EventMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public MessageTypeEnum getMessageTypeEnum(String messageType) {

        if (messageType.equalsIgnoreCase(MessageTypeEnum.GUEST.getValue())) {
            return MessageTypeEnum.GUEST;
        }
        if (messageType.equalsIgnoreCase(MessageTypeEnum.DISH.getValue())) {
            return MessageTypeEnum.DISH;
        }
        if (messageType.equalsIgnoreCase(MessageTypeEnum.DRINK.getValue())) {
            return MessageTypeEnum.DRINK;
        }

        // all other cases
        return MessageTypeEnum.EVENT;

    }

    /**
     * @param eventMenuDTO
     */
    public void processEventMenuDTO(EventMenuDTO eventMenuDTO) {

        log.debug("processEventMenuDTO: eventMenuDTO={}", eventMenuDTO);

        if (eventMenuDTO != null && eventMenuDTO.getEventId() != null && eventMenuDTO.getMessageType() != null && eventMenuDTO.getActionType() != null) {

            ActionEnum action = getActionEnum(eventMenuDTO.getActionType());
            MessageTypeEnum messageType = getMessageTypeEnum(eventMenuDTO.getMessageType());

            // construct new event
            EventMenu eventMenu = new EventMenu(eventMenuDTO, menuServiceProperties);
            // query to  check if already exists
            EventMenu existingEventMenu = menuRepository.findFirstByEventId(UUID.fromString(eventMenuDTO.getEventId()));

            if (existingEventMenu != null) {

                switch (messageType) {

                    case GUEST: {
                        if (eventMenuDTO.getGuest() != null) {
                            eventMenu = processGuest(eventMenuDTO.getGuest(), action, existingEventMenu);
                        }
                        break;
                    }
                    case DISH: {
                        if (eventMenuDTO.getDish() != null) {
                            eventMenu = processDish(eventMenuDTO.getDish(), action, existingEventMenu);
                        }
                        break;
                    }
                    case DRINK: {
                        if (eventMenuDTO.getDrink() != null) {
                            eventMenu = processDrink(eventMenuDTO.getDrink(), action, existingEventMenu);
                        }
                        break;
                    }
                    default: {
                        // event processing
                        if (existingEventMenu.getId() != null) {
                            // existing event
                            eventMenu.setId(existingEventMenu.getId());
                        }
                        break;
                    }
                }
            }

            log.debug("processEventMenuDTO: eventMenu={}", eventMenu);
            menuRepository.save(eventMenu);
        }

    }

    /**
     * @param guest
     * @param action
     * @param existingEventMenu
     * @return
     */
    public EventMenu processGuest(Guest guest, ActionEnum action, EventMenu existingEventMenu) {
        log.debug("processGuest: guest={}", guest);

        EventMenu eventMenu = existingEventMenu;
        boolean isFound = false;

        ArrayList<Guest> updatedGuestList = new ArrayList<>();
        updatedGuestList.addAll(eventMenu.getGuests());

        for (int i = 0; i < updatedGuestList.size(); i++) {
            if (updatedGuestList.get(i).getGuestId().equals(guest.getGuestId())) {
                isFound = true;
                log.debug("processGuest: i={}, guestId={}", i, guest.getGuestId());
                if (action.equals(ActionEnum.DELETE)) {
                    updatedGuestList.remove(i);
                } else {
                    // upsert
                    updatedGuestList.set(i, guest);
                }
                break;
            }
        }

        if (!isFound && !action.equals(ActionEnum.DELETE)) {
            updatedGuestList.add(guest);
        }
        log.debug("processGuest: updatedGuestList={}", updatedGuestList);
        eventMenu.setGuests(updatedGuestList);
        return eventMenu;
    }

    /**
     * @param dish
     * @param action
     * @param existingEventMenu
     * @return
     */
    public EventMenu processDish(Dish dish, ActionEnum action, EventMenu existingEventMenu) {
        log.debug("processDish: dish={}", dish);

        EventMenu eventMenu = existingEventMenu;
        boolean isFound = false;

        ArrayList<Dish> updatedDishList = new ArrayList<>();
        updatedDishList.addAll(eventMenu.getDishes());

        for (int i = 0; i < updatedDishList.size(); i++) {
            if (updatedDishList.get(i).getDishId().equals(dish.getDishId())) {
                isFound = true;
                log.debug("processDish: i={}, dishId={}", i, dish.getDishId());
                if (action.equals(ActionEnum.DELETE)) {
                    updatedDishList.remove(i);
                } else {
                    // upsert
                    updatedDishList.set(i, dish);
                }
                break;
            }
        }

        if (!isFound && !action.equals(ActionEnum.DELETE)) {
            updatedDishList.add(dish);
        }
        log.debug("processDish: updatedDishList={}", updatedDishList);
        eventMenu.setDishes(updatedDishList);
        return eventMenu;
    }


    /**
     * @param drink
     * @param action
     * @param existingEventMenu
     * @return
     */
    public EventMenu processDrink(Drink drink, ActionEnum action, EventMenu existingEventMenu) {
        log.debug("processDrink: drink={}", drink);

        EventMenu eventMenu = existingEventMenu;
        boolean isFound = false;

        ArrayList<Drink> updatedDrinkList = new ArrayList<>();
        updatedDrinkList.addAll(eventMenu.getDrinks());

        for (int i = 0; i < updatedDrinkList.size(); i++) {
            if (updatedDrinkList.get(i).getDrinkId().equals(drink.getDrinkId())) {
                isFound = true;
                log.debug("processDrink: i={}, drinkId={}", i, drink.getDrinkId());
                if (action.equals(ActionEnum.DELETE)) {
                    updatedDrinkList.remove(i);
                } else {
                    // upsert
                    updatedDrinkList.set(i, drink);
                }
                break;
            }
        }

        if (!isFound && !action.equals(ActionEnum.DELETE)) {
            updatedDrinkList.add(drink);
        }
        log.debug("processDrink: updatedDrinkList={}", updatedDrinkList);
        eventMenu.setDrinks(updatedDrinkList);
        return eventMenu;
    }
}

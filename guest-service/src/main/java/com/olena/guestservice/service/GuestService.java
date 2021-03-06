package com.olena.guestservice.service;

import com.olena.guestservice.config.GuestServiceProperties;
import com.olena.guestservice.enums.ActionEnum;
import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.GuestDTO;
import com.olena.guestservice.model.GuestMessage;
import com.olena.guestservice.producer.PotluckEventPublisher;
import com.olena.guestservice.repository.GuestRepository;
import com.olena.guestservice.repository.entity.Guest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class GuestService {

    @Autowired
    GuestServiceProperties guestServiceProperties;

    @Autowired
    GuestRepository guestRepository;

    @Autowired
    private Producer<String, GuestMessage> potluckEventProducer;

    public GuestDTO getGuestInfo(String guestId, String bearerToken, DishService dishService, DrinkService drinkService) throws ServiceException {
        log.debug("getGuestInfo: guestId={}", guestId);

        Guest guest = getGuestFromDb(guestId);
        ModelMapper modelMapper = new ModelMapper();
        GuestDTO guestDTO = modelMapper.map(guest, GuestDTO.class);
        log.debug("getGuestInfo: guestId={}", guestId);

        guestDTO.setDishes(dishService.getDishList(guestId, bearerToken));

        guestDTO.setDrinks(drinkService.getDrinkList(guestId, bearerToken));

        return guestDTO;
    }

    public Guest getGuestFromDb(String guestId) throws ServiceException {
        log.debug("getGuestFromDb: guestId={}", guestId);

        Guest guest = guestRepository.findFirstByGuestId(UUID.fromString(guestId));
        if (guest == null) {
            String errMsg = "Guest not found";
            log.error("getGuestFromDb: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }

        return guest;
    }

    /**
     * @param eventId
     * @return
     * @throws ServiceException
     */
    public List<Guest> getGuestListByEventId(UUID eventId) throws ServiceException {
        log.debug("getGuestListByEventId: eventId={}", eventId);
        try {
            List<Guest> guestList = guestRepository.findAllByEventIdOrderByGuestName(eventId);
            return guestList;
        } catch (Exception e) {
            String errMsg = e.toString();
            log.error("getGuestListByEventId: eventId={}, {}", eventId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param guest
     * @return
     * @throws ServiceException
     */
    public void setGuest(Guest guest) throws ServiceException {
        log.debug("setGuest: guest={}", guest.toString());
        try {
            guestRepository.save(guest);
        } catch (Exception e) {
            String errMsg = "Failed to update DB: " + e;
            log.error("setGuest: guest={}, {}", guest, errMsg);
            throw new ServiceException(errMsg);
        }

        //TODO: push guest  asynchronously  to  the Kafka queue for processing by  other services
        //            inventoryClient.updateInventory(guest.getItems());
        //          guest.setOrderId(UUID.randomUUID().toString());


    }

    /**
     * @param guestDTOList
     * @return
     * @throws ServiceException
     */
    public List<Guest> addGuests(GuestDTO[] guestDTOList, PotluckEventPublisher potluckEventPublisher) throws ServiceException {
        log.debug("addGuests: guestDTO={}", guestDTOList);

        List<Guest> guestList = new ArrayList<>();
        for (GuestDTO guestDTO : guestDTOList) {

            try {

                Guest guest = new Guest(guestDTO, guestServiceProperties);
                // unique index on username, eventId and guestEmail
                Guest guestExisting = guestRepository.findFirstByUserNameAndEventIdAndGuestEmail(guestDTO.getUserName(), UUID.fromString(guestDTO.getEventId()), guestDTO.getGuestEmail());

                // check if guestDTO  already  exists  -  update
                if (guestExisting != null) {
                    guest.setId(guestExisting.getId());
                }
                // save to  DB
                setGuest(guest);
                guestList.add(guest);

                // publish event into  Kafka topic
                GuestMessage guestMessage = new GuestMessage(guest, ActionEnum.ADD);
                potluckEventPublisher.publish(potluckEventProducer, guestServiceProperties.getPotluckEventProducerTopic(), guestMessage);


            } catch (ServiceException se) {
                throw se;
            } catch (Exception e) {
                StringBuffer errMsg = new StringBuffer();
                errMsg.append("Failed to map guest: ").append(e);
                log.error("addGuests: guestDTO={}, {}", guestDTO, errMsg);
                throw new ServiceException(errMsg.toString());
            }
        }

        return guestList;
    }

    /**
     * @param guestDTOList
     * @param bearerToken
     * @param dishService
     * @param drinkService
     * @param potluckEventPublisher
     * @return
     * @throws ServiceException
     */
    public List<Guest> deleteGuests(GuestDTO[] guestDTOList, String bearerToken, DishService dishService, DrinkService drinkService, PotluckEventPublisher potluckEventPublisher) throws ServiceException {
        log.debug("deleteGuests: guestDTOList={}", guestDTOList);

        StringBuffer errMsg = new StringBuffer();
        List<Guest> guestList = new ArrayList<>();
        try {
            for (GuestDTO guestDTO : guestDTOList) {

                Guest guest = guestRepository.findFirstByUserNameAndEventIdAndGuestEmail(guestDTO.getUserName(), UUID.fromString(guestDTO.getEventId()), guestDTO.getGuestEmail());
                if (guest != null) {

                    dishService.deleteDishesByGuest(guest.getGuestId().toString(), bearerToken);
                    drinkService.deleteDrinksByGuest(guest.getGuestId().toString(), bearerToken);

                    guestList.add(guest);
                    guestRepository.delete(guest);

                    // publish event into  Kafka topic
                    GuestMessage guestMessage = new GuestMessage(guest, ActionEnum.DELETE);
                    potluckEventPublisher.publish(potluckEventProducer, guestServiceProperties.getPotluckEventProducerTopic(), guestMessage);
                }

            }

            return guestList;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("deleteGuests: guestDTOList={}, {}", guestDTOList, errMsg);
            throw new ServiceException(errMsg.toString());
        }

    }

    /**
     * @param eventId
     * @param dishService
     * @param drinkService
     * @return
     * @throws ServiceException
     */
    public List<Guest> deleteGuestsByEventId(String eventId, String bearerToken, DishService dishService, DrinkService drinkService) throws ServiceException {
        log.debug("deleteGuestsByEventId: eventId={}", eventId);

        StringBuffer errMsg = new StringBuffer();
        try {
            List<Guest> guestList = guestRepository.findAllByEventIdOrderByGuestName(UUID.fromString(eventId));

            if (guestList != null || guestList.size() > 0) {
                for (Guest guest : guestList) {

                    dishService.deleteDishesByGuest(guest.getGuestId().toString(), bearerToken);
                    drinkService.deleteDrinksByGuest(guest.getGuestId().toString(), bearerToken);
                    guestRepository.delete(guest);

                }

            }

            return guestList;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("deleteGuestsByEventId: eventId={}, {}", eventId, errMsg);
            throw new ServiceException(errMsg.toString());
        }

    }

    /**
     * @param guestDTO
     * @param potluckEventPublisher
     * @return
     * @throws ServiceException
     */
    public Guest updateGuest(GuestDTO guestDTO, PotluckEventPublisher potluckEventPublisher) throws ServiceException {
        log.debug("updateGuest: guestDTO={}", guestDTO);

        StringBuffer errMsg = new StringBuffer();
        try {

            Guest guest = new Guest(guestDTO, guestServiceProperties);
            // unique index on username, eventId and guestEmail
            Guest guestExisting = guestRepository.findFirstByGuestId(UUID.fromString(guestDTO.getGuestId()));

            // check if guestDTO  already  exists  -  update
            if (guestExisting != null) {

                // set PK
                guest.setId(guestExisting.getId());

                // save to  DB
                setGuest(guest);

                // publish event into  Kafka topic
                GuestMessage guestMessage = new GuestMessage(guest, ActionEnum.UPDATE);
                potluckEventPublisher.publish(potluckEventProducer, guestServiceProperties.getPotluckEventProducerTopic(), guestMessage);

            } else {
                errMsg.append("Guest not found: ").append(guestDTO.getGuestId());
                throw new ServiceException(errMsg.toString());
            }

            return guest;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            errMsg.append("Failed to update guest: ").append(e);
            log.error("updateGuest: guestDTO={}, {}", guestDTO, errMsg);
            throw new ServiceException(errMsg.toString());
        }
    }

    /**
     * @param guestId
     * @param bearerToken
     * @param dishService
     * @param drinkService
     * @param potluckEventPublisher
     * @return
     * @throws ServiceException
     */
    public Guest deleteGuest(String guestId, String bearerToken, DishService dishService, DrinkService drinkService, PotluckEventPublisher potluckEventPublisher) throws ServiceException {
        log.debug("deleteGuest: guestId={}", guestId);

        StringBuffer errMsg = new StringBuffer();
        try {
            Guest guest = guestRepository.findFirstByGuestId(UUID.fromString(guestId));

            if (guest != null) {

                dishService.deleteDishesByGuest(guestId, bearerToken);
                drinkService.deleteDrinksByGuest(guestId, bearerToken);

                guestRepository.delete(guest);

                // publish event into  Kafka topic
                GuestMessage guestMessage = new GuestMessage(guest, ActionEnum.DELETE);
                potluckEventPublisher.publish(potluckEventProducer, guestServiceProperties.getPotluckEventProducerTopic(), guestMessage);

            } else {
                errMsg.append("Guest not found: ").append(guestId);
                throw new ServiceException(errMsg.toString());
            }

            return guest;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("deleteGuest: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg.toString());
        }

    }
}

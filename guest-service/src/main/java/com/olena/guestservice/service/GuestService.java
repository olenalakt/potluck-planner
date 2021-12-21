package com.olena.guestservice.service;

import com.olena.guestservice.config.GuestServiceProperties;
import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.GuestDTO;
import com.olena.guestservice.repository.GuestRepository;
import com.olena.guestservice.repository.entity.Guest;
import lombok.extern.slf4j.Slf4j;
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
    public List<Guest> addGuests(GuestDTO[] guestDTOList) throws ServiceException {
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
     *
     * @param guestDTOList
     * @param dishService
     * @param drinkService
     * @return
     * @throws ServiceException
     */
    public List<Guest> deleteGuests(GuestDTO[] guestDTOList, DishService dishService, DrinkService drinkService) throws ServiceException {
        log.debug("deleteGuests: guestDTOList={}", guestDTOList);

        StringBuffer errMsg = new StringBuffer();
        List<Guest> guestList = new ArrayList<>();
        try {
            for (GuestDTO guestDTO : guestDTOList) {

                Guest guestExisting = guestRepository.findFirstByUserNameAndEventIdAndGuestEmail(guestDTO.getUserName(), UUID.fromString(guestDTO.getEventId()), guestDTO.getGuestEmail());
                if (guestExisting != null) {
                    dishService.deleteDishesByGuest(guestDTO.getGuestId());
                    drinkService.deleteDrinksByGuest(guestDTO.getGuestId());
                    guestList.add(guestExisting);
                    guestRepository.delete(guestExisting);
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
     *
     * @param eventId
     * @param dishService
     * @param drinkService
     * @return
     * @throws ServiceException
     */
    public List<Guest> deleteGuestsByEventId(String eventId, DishService dishService, DrinkService drinkService) throws ServiceException {
        log.debug("deleteGuestsByEventId: eventId={}", eventId);

        StringBuffer errMsg = new StringBuffer();
        try {
            List<Guest> guestList = guestRepository.findAllByEventIdOrderByGuestName(UUID.fromString(eventId));

            if (guestList != null || guestList.size() > 0) {
                for (Guest guest : guestList) {

                    dishService.deleteDishesByGuest(guest.getGuestId().toString());
                    drinkService.deleteDrinksByGuest(guest.getGuestId().toString());
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
     * @return
     * @throws ServiceException
     */
    public Guest updateGuest(GuestDTO guestDTO) throws ServiceException {
        log.debug("addGuests: guestDTO={}", guestDTO);

        StringBuffer errMsg = new StringBuffer();
        try {

            Guest guest = new Guest(guestDTO, guestServiceProperties);
            // unique index on username, eventId and guestEmail
            Guest guestExisting = guestRepository.findFirstByGuestId(UUID.fromString(guestDTO.getGuestId()));

            // check if guestDTO  already  exists  -  update
            if (guestExisting != null) {

                // save to  DB
                setGuest(guest);

            } else {
                errMsg.append("Guest not found: ").append(guestDTO.getGuestId());
                throw new ServiceException(errMsg.toString());
            }

            return guest;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            errMsg.append("Failed to map guest: ").append(e);
            log.error("addGuests: guestDTO={}, {}", guestDTO, errMsg);
            throw new ServiceException(errMsg.toString());
        }
    }

    /**
     * @param guestId
     * @param dishService
     * @param drinkService
     * @return
     * @throws ServiceException
     */
    public Guest deleteGuest(String guestId, DishService dishService, DrinkService drinkService) throws ServiceException {
        log.debug("deleteGuest: guestId={}", guestId);

        StringBuffer errMsg = new StringBuffer();
        try {
            Guest guest = guestRepository.findFirstByGuestId(UUID.fromString(guestId));

            if (guest != null) {

                dishService.deleteDishesByGuest(guestId);
                drinkService.deleteDrinksByGuest(guestId);
                guestRepository.delete(guest);

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

package com.olena.guestservice.service;

import com.olena.guestservice.config.GuestServiceConfig;
import com.olena.guestservice.exception.ServiceException;
import com.olena.guestservice.model.GuestDTO;
import com.olena.guestservice.repository.GuestRepository;
import com.olena.guestservice.repository.entity.Guest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class GuestService {

    @Autowired
    GuestServiceConfig guestServiceConfig;

    @Autowired
    GuestRepository guestRepository;

    public GuestDTO getGuestInfo(String guestId, DishService dishService) throws ServiceException {
        log.debug("getGuestInfo: guestId={}", guestId);

        Guest guest = getGuestFromDb(guestId);
        ModelMapper modelMapper = new ModelMapper();
        GuestDTO guestDTO = modelMapper.map(guest, GuestDTO.class);
        log.debug("getGuestInfo: guestId={}", guestId);

        guestDTO.setDishes(dishService.getDishList(guestId));
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
    public void addGuests(GuestDTO[] guestDTOList) throws ServiceException {
        log.debug("addGuests: guestDTO={}", guestDTOList);

        for (GuestDTO guestDTO : guestDTOList) {

            try {

                Guest guest = new Guest(guestDTO, guestServiceConfig);
                // unique index on username, eventId and guestEmail
                Guest guestExisting = guestRepository.findFirstByUserNameAndEventIdAndGuestEmail(guestDTO.getUserName(), UUID.fromString(guestDTO.getEventId()), guestDTO.getGuestEmail());

                // check if guestDTO  already  exists  -  update
                if (guestExisting != null) {
                    guest.setId(guestExisting.getId());
                }
                // save to  DB
                setGuest(guest);

            } catch (ServiceException se) {
                throw se;
            } catch (Exception e) {
                StringBuffer errMsg = new StringBuffer();
                errMsg.append("Failed to map guest: ").append(e);
                log.error("addGuests: guestDTO={}, {}", guestDTO, errMsg);
                throw new ServiceException(errMsg.toString());
            }

        }
    }

}
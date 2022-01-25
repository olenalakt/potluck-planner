package com.olena.guestcleanupservice.service;

import com.olena.guestcleanupservice.config.GuestCleanupServiceProperties;
import com.olena.guestcleanupservice.enums.MessageTypeEnum;
import com.olena.guestcleanupservice.enums.RequestStatusEnum;
import com.olena.guestcleanupservice.exception.ServiceException;
import com.olena.guestcleanupservice.model.GuestCleanupRequestDTO;
import com.olena.guestcleanupservice.repository.GuestCleanupRequestRepository;
import com.olena.guestcleanupservice.repository.GuestRepository;
import com.olena.guestcleanupservice.repository.entity.Guest;
import com.olena.guestcleanupservice.repository.entity.GuestCleanupRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class GuestCleanupService {

    @Autowired
    GuestCleanupServiceProperties guestCleanupServiceProperties;

    @Autowired
    GuestRepository guestRepository;

    @Autowired
    GuestCleanupRequestRepository guestCleanupRequestRepository;

    /**
     * @param userName
     * @return
     * @throws ServiceException
     */
    public List<GuestCleanupRequest> getCleanupRequestsByUsername(String userName) throws ServiceException {
        log.debug("getCleanupRequestsByUsername: userName={}", userName);

        try {

            List<GuestCleanupRequest> guestCleanupRequestList = guestCleanupRequestRepository.findAllByUserNameOrderByLastmodifiedDesc(userName);
            if (guestCleanupRequestList == null || guestCleanupRequestList.size() == 0) {
                String errMsg = "Cleanup Requests not found";
                log.error("getCleanupRequestsByUsername: userName={}, {}", userName, errMsg);
                throw new ServiceException(errMsg);
            }

            return guestCleanupRequestList;

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            String errMsg = "Unexpected exception: " + e;
            log.error("getCleanupRequestsByUsername: userName={}, {}", userName, errMsg);
            throw new ServiceException(errMsg);
        }

    }

    public MessageTypeEnum getMessageTypeEnum(String messageType) {

        if (messageType.equalsIgnoreCase(MessageTypeEnum.EVENT.getValue())) {
            return MessageTypeEnum.EVENT;
        }
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
        return MessageTypeEnum.USER;

    }

    /**
     * @param guestCleanupRequestDTO
     */
    // TODO  -  consider insert  every state -  might be pushed to  zipkin, rather then store in DB
    public void processCleanupRequest(GuestCleanupRequestDTO guestCleanupRequestDTO) {

        log.debug("processCleanupRequest: guestCleanupRequestDTO={}", guestCleanupRequestDTO);
        // construct new request entry
        GuestCleanupRequest guestCleanupRequest = new GuestCleanupRequest(guestCleanupRequestDTO, guestCleanupServiceProperties);
        guestCleanupRequestRepository.save(guestCleanupRequest);

        if (guestCleanupRequestDTO != null && guestCleanupRequestDTO.getUserName() != null && guestCleanupRequestDTO.getMessageType() != null) {

            MessageTypeEnum messageType = getMessageTypeEnum(guestCleanupRequest.getMessageType());

            try {
                switch (messageType) {
                    case EVENT: {
                        break;
                    }
                    case GUEST: {
                        break;
                    }
                    case DISH: {
                        break;
                    }
                    case DRINK: {
                        break;
                    }
                    default: {
                        processUserCleanup(guestCleanupRequest);
                        break;
                    }
                }

                updateRequestStatus(guestCleanupRequest, RequestStatusEnum.PROCESSED);
                log.debug("processCleanupRequest: guestCleanupRequest={}", guestCleanupRequest);
            } catch (Exception e) {

                guestCleanupRequest.setRequestError(e.toString());
                updateRequestStatus(guestCleanupRequest, RequestStatusEnum.FAILED);
                log.error("processCleanupRequest exception: guestCleanupRequest={}, {}", guestCleanupRequest, e.toString());

            }
        } else {
            updateRequestStatus(guestCleanupRequest, RequestStatusEnum.INVALID);
        }

    }

    /**
     * @param guestCleanupRequest
     */
    private void updateRequestStatus(GuestCleanupRequest guestCleanupRequest, RequestStatusEnum requestStatusEnum) {
        Instant now;
        guestCleanupRequest.setRequestStatus(requestStatusEnum.getValue());
        now = Instant.now();
        guestCleanupRequest.setLastmodified(Timestamp.from(now));
        guestCleanupRequestRepository.save(guestCleanupRequest);
    }

    public void processUserCleanup(GuestCleanupRequest guestCleanupRequest) {
        log.debug("processUserCleanup: guestCleanupRequest={}", guestCleanupRequest);

        // query existing events for user
        // TODO -  replace Mongo repository with Mongo query  to  delete all  entries by  username
        List<Guest> guestList = guestRepository.findAllByUserNameOrderByLastmodifiedDesc(guestCleanupRequest.getUserName());
        for (Guest guest : guestList) {
            guestRepository.delete(guest);
        }

    }
}

package com.olena.eventcleanupservice.service;

import com.olena.eventcleanupservice.config.EventCleanupServiceProperties;
import com.olena.eventcleanupservice.enums.MessageTypeEnum;
import com.olena.eventcleanupservice.enums.RequestStatusEnum;
import com.olena.eventcleanupservice.exception.ServiceException;
import com.olena.eventcleanupservice.model.EventCleanupRequestDTO;
import com.olena.eventcleanupservice.repository.EventCleanupRequestRepository;
import com.olena.eventcleanupservice.repository.EventRepository;
import com.olena.eventcleanupservice.repository.entity.Event;
import com.olena.eventcleanupservice.repository.entity.EventCleanupRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class EventCleanupService {

    @Autowired
    EventCleanupServiceProperties eventCleanupServiceProperties;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventCleanupRequestRepository eventCleanupRequestRepository;

    /**
     * @param userName
     * @return
     * @throws ServiceException
     */
    public List<EventCleanupRequest> getCleanupRequestsByUsername(String userName) throws ServiceException {
        log.debug("getCleanupRequestsByUsername: userName={}", userName);

        try {

            List<EventCleanupRequest> eventCleanupRequestList = eventCleanupRequestRepository.findAllByUserNameOrderByLastmodifiedDesc(userName);
            if (eventCleanupRequestList == null || eventCleanupRequestList.size() == 0) {
                String errMsg = "Cleanup Requests not found";
                log.error("getCleanupRequestsByUsername: userName={}, {}", userName, errMsg);
                throw new ServiceException(errMsg);
            }

            return eventCleanupRequestList;

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
     * @param eventCleanupRequestDTO
     */
    // TODO  -  consider insert  every state -  might be pushed to  zipkin, rather then store in DB
    public void processCleanupRequest(EventCleanupRequestDTO eventCleanupRequestDTO) {

        log.debug("processCleanupRequest: eventCleanupRequestDTO={}", eventCleanupRequestDTO);
        // construct new request entry
        EventCleanupRequest eventCleanupRequest = new EventCleanupRequest(eventCleanupRequestDTO, eventCleanupServiceProperties);
        eventCleanupRequestRepository.save(eventCleanupRequest);

        if (eventCleanupRequestDTO != null && eventCleanupRequestDTO.getUserName() != null && eventCleanupRequestDTO.getMessageType() != null) {

            MessageTypeEnum messageType = getMessageTypeEnum(eventCleanupRequest.getMessageType());

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
                        processUserCleanup(eventCleanupRequest);
                        break;
                    }
                }

                updateRequestStatus(eventCleanupRequest, RequestStatusEnum.PROCESSED);
                log.debug("processCleanupRequest: eventCleanupRequest={}", eventCleanupRequest);
            } catch (Exception e) {

                eventCleanupRequest.setRequestError(e.toString());
                updateRequestStatus(eventCleanupRequest, RequestStatusEnum.FAILED);
                log.error("processCleanupRequest exception: eventCleanupRequest={}, {}", eventCleanupRequest, e.toString());

            }
        } else {
            updateRequestStatus(eventCleanupRequest, RequestStatusEnum.INVALID);
        }

    }

    /**
     * @param eventCleanupRequest
     */
    private void updateRequestStatus(EventCleanupRequest eventCleanupRequest, RequestStatusEnum requestStatusEnum) {
        Instant now;
        eventCleanupRequest.setRequestStatus(requestStatusEnum.getValue());
        now = Instant.now();
        eventCleanupRequest.setLastmodified(Timestamp.from(now));
        eventCleanupRequestRepository.save(eventCleanupRequest);
    }

    public void processUserCleanup(EventCleanupRequest eventCleanupRequest) {
        log.debug("processUserCleanup: eventCleanupRequest={}", eventCleanupRequest);

        // query existing events for user
        // TODO -  replace Mongo repository with Mongo query  to  delete all  entries by  username
        List<Event> eventList = eventRepository.findAllByUserNameOrderByEventDate(eventCleanupRequest.getUserName());
        for (Event event : eventList) {
            eventRepository.delete(event);
        }

    }
}

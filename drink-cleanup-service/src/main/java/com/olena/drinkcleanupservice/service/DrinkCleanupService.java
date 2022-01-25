package com.olena.drinkcleanupservice.service;

import com.olena.drinkcleanupservice.config.DrinkCleanupServiceProperties;
import com.olena.drinkcleanupservice.enums.MessageTypeEnum;
import com.olena.drinkcleanupservice.enums.RequestStatusEnum;
import com.olena.drinkcleanupservice.exception.ServiceException;
import com.olena.drinkcleanupservice.model.DrinkCleanupRequestDTO;
import com.olena.drinkcleanupservice.repository.DrinkCleanupRequestRepository;
import com.olena.drinkcleanupservice.repository.DrinkRepository;
import com.olena.drinkcleanupservice.repository.entity.Drink;
import com.olena.drinkcleanupservice.repository.entity.DrinkCleanupRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class DrinkCleanupService {

    @Autowired
    DrinkCleanupServiceProperties drinkCleanupServiceProperties;

    @Autowired
    DrinkRepository drinkRepository;

    @Autowired
    DrinkCleanupRequestRepository drinkCleanupRequestRepository;

    /**
     * @param userName
     * @return
     * @throws ServiceException
     */
    public List<DrinkCleanupRequest> getCleanupRequestsByUsername(String userName) throws ServiceException {
        log.debug("getCleanupRequestsByUsername: userName={}", userName);

        try {

            List<DrinkCleanupRequest> drinkCleanupRequestList = drinkCleanupRequestRepository.findAllByUserNameOrderByLastmodifiedDesc(userName);
            if (drinkCleanupRequestList == null || drinkCleanupRequestList.size() == 0) {
                String errMsg = "Cleanup Requests not found";
                log.error("getCleanupRequestsByUsername: userName={}, {}", userName, errMsg);
                throw new ServiceException(errMsg);
            }

            return drinkCleanupRequestList;

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
     * @param drinkCleanupRequestDTO
     */
    // TODO  -  consider insert  every state -  might be pushed to  zipkin, rather then store in DB
    public void processCleanupRequest(DrinkCleanupRequestDTO drinkCleanupRequestDTO) {

        log.debug("processCleanupRequest: drinkCleanupRequestDTO={}", drinkCleanupRequestDTO);
        // construct new request entry
        DrinkCleanupRequest drinkCleanupRequest = new DrinkCleanupRequest(drinkCleanupRequestDTO, drinkCleanupServiceProperties);
        drinkCleanupRequestRepository.save(drinkCleanupRequest);

        if (drinkCleanupRequestDTO != null && drinkCleanupRequestDTO.getUserName() != null && drinkCleanupRequestDTO.getMessageType() != null) {

            MessageTypeEnum messageType = getMessageTypeEnum(drinkCleanupRequest.getMessageType());

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
                        processUserCleanup(drinkCleanupRequest);
                        break;
                    }
                }

                updateRequestStatus(drinkCleanupRequest, RequestStatusEnum.PROCESSED);
                log.debug("processCleanupRequest: drinkCleanupRequest={}", drinkCleanupRequest);
            } catch (Exception e) {

                drinkCleanupRequest.setRequestError(e.toString());
                updateRequestStatus(drinkCleanupRequest, RequestStatusEnum.FAILED);
                log.error("processCleanupRequest exception: drinkCleanupRequest={}, {}", drinkCleanupRequest, e.toString());

            }
        } else {
            updateRequestStatus(drinkCleanupRequest, RequestStatusEnum.INVALID);
        }

    }

    /**
     * @param drinkCleanupRequest
     */
    private void updateRequestStatus(DrinkCleanupRequest drinkCleanupRequest, RequestStatusEnum requestStatusEnum) {
        Instant now;
        drinkCleanupRequest.setRequestStatus(requestStatusEnum.getValue());
        now = Instant.now();
        drinkCleanupRequest.setLastmodified(Timestamp.from(now));
        drinkCleanupRequestRepository.save(drinkCleanupRequest);
    }

    public void processUserCleanup(DrinkCleanupRequest drinkCleanupRequest) {
        log.debug("processUserCleanup: drinkCleanupRequest={}", drinkCleanupRequest);

        // query existing events for user
        // TODO -  replace Mongo repository with Mongo query  to  delete all  entries by  username
        List<Drink> drinkList = drinkRepository.findAllByUserNameOrderByLastmodifiedDesc(drinkCleanupRequest.getUserName());
        for (Drink drink : drinkList) {
            drinkRepository.delete(drink);
        }

    }
}

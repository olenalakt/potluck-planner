package com.olena.dishcleanupservice.service;

import com.olena.dishcleanupservice.config.DishCleanupServiceProperties;
import com.olena.dishcleanupservice.enums.MessageTypeEnum;
import com.olena.dishcleanupservice.enums.RequestStatusEnum;
import com.olena.dishcleanupservice.exception.ServiceException;
import com.olena.dishcleanupservice.model.DishCleanupRequestDTO;
import com.olena.dishcleanupservice.repository.DishCleanupRequestRepository;
import com.olena.dishcleanupservice.repository.DishRepository;
import com.olena.dishcleanupservice.repository.entity.Dish;
import com.olena.dishcleanupservice.repository.entity.DishCleanupRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class DishCleanupService {

    @Autowired
    DishCleanupServiceProperties dishCleanupServiceProperties;

    @Autowired
    DishRepository dishRepository;

    @Autowired
    DishCleanupRequestRepository dishCleanupRequestRepository;

    /**
     * @param userName
     * @return
     * @throws ServiceException
     */
    public List<DishCleanupRequest> getCleanupRequestsByUsername(String userName) throws ServiceException {
        log.debug("getCleanupRequestsByUsername: userName={}", userName);

        try {

            List<DishCleanupRequest> dishCleanupRequestList = dishCleanupRequestRepository.findAllByUserNameOrderByLastmodifiedDesc(userName);
            if (dishCleanupRequestList == null || dishCleanupRequestList.size() == 0) {
                String errMsg = "Cleanup Requests not found";
                log.error("getCleanupRequestsByUsername: userName={}, {}", userName, errMsg);
                throw new ServiceException(errMsg);
            }

            return dishCleanupRequestList;

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
     * @param dishCleanupRequestDTO
     */
    // TODO  -  consider insert  every state -  might be pushed to  zipkin, rather then store in DB
    public void processDishCleanupRequest(DishCleanupRequestDTO dishCleanupRequestDTO) {

        log.debug("processDishCleanupRequest: dishCleanupRequestDTO={}", dishCleanupRequestDTO);
        // construct new request entry
        DishCleanupRequest dishCleanupRequest = new DishCleanupRequest(dishCleanupRequestDTO, dishCleanupServiceProperties);
        dishCleanupRequestRepository.save(dishCleanupRequest);

        if (dishCleanupRequestDTO != null && dishCleanupRequestDTO.getUserName() != null && dishCleanupRequestDTO.getMessageType() != null) {

            MessageTypeEnum messageType = getMessageTypeEnum(dishCleanupRequest.getMessageType());

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
                        processUserCleanup(dishCleanupRequest);
                        break;
                    }
                }

                updateRequestStatus(dishCleanupRequest, RequestStatusEnum.PROCESSED);
                log.debug("processDishCleanupRequest: dishCleanupRequest={}", dishCleanupRequest);
            } catch (Exception e) {

                dishCleanupRequest.setRequestError(e.toString());
                updateRequestStatus(dishCleanupRequest, RequestStatusEnum.FAILED);
                log.error("processDishCleanupRequest exception: dishCleanupRequest={}, {}", dishCleanupRequest, e.toString());

            }
        } else {
            updateRequestStatus(dishCleanupRequest, RequestStatusEnum.INVALID);
        }

    }

    /**
     * @param dishCleanupRequest
     */
    private void updateRequestStatus(DishCleanupRequest dishCleanupRequest, RequestStatusEnum requestStatusEnum) {
        Instant now;
        dishCleanupRequest.setRequestStatus(requestStatusEnum.getValue());
        now = Instant.now();
        dishCleanupRequest.setLastmodified(Timestamp.from(now));
        dishCleanupRequestRepository.save(dishCleanupRequest);
    }

    public void processUserCleanup(DishCleanupRequest dishCleanupRequest) {
        log.debug("processUserCleanup: dishCleanupRequest={}", dishCleanupRequest);

        // query existing events for user
        // TODO -  replace Mongo repository with Mongo query  to  delete all  entries by  username
        List<Dish> dishList = dishRepository.findAllByUserNameOrderByLastmodifiedDesc(dishCleanupRequest.getUserName());
        for (Dish dish : dishList) {
            dishRepository.delete(dish);
        }

    }
}

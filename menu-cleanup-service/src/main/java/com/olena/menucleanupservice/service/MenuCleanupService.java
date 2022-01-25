package com.olena.menucleanupservice.service;

import com.olena.menucleanupservice.config.MenuCleanupServiceProperties;
import com.olena.menucleanupservice.enums.MessageTypeEnum;
import com.olena.menucleanupservice.enums.RequestStatusEnum;
import com.olena.menucleanupservice.exception.ServiceException;
import com.olena.menucleanupservice.model.MenuCleanupRequestDTO;
import com.olena.menucleanupservice.repository.MenuCleanupRequestRepository;
import com.olena.menucleanupservice.repository.EventMenuRepository;
import com.olena.menucleanupservice.repository.entity.EventMenu;
import com.olena.menucleanupservice.repository.entity.MenuCleanupRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class MenuCleanupService {

    @Autowired
    MenuCleanupServiceProperties menuCleanupServiceProperties;

    @Autowired
    EventMenuRepository eventMenuRepository;

    @Autowired
    MenuCleanupRequestRepository menuCleanupRequestRepository;

    /**
     *
     * @param userName
     * @return
     * @throws ServiceException
     */
    public List<MenuCleanupRequest> getCleanupRequestsByUsername(String userName) throws ServiceException {
        log.debug("getCleanupRequestsByUsername: userName={}", userName);

        try {

            List<MenuCleanupRequest> menuCleanupRequestList = menuCleanupRequestRepository.findAllByUserNameOrderByLastmodifiedDesc(userName);
            if (menuCleanupRequestList == null || menuCleanupRequestList.size() == 0) {
                String errMsg = "Cleanup Requests not found";
                log.error("getCleanupRequestsByUsername: userName={}, {}", userName, errMsg);
                throw new ServiceException(errMsg);
            }

            return menuCleanupRequestList;

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
     * @param menuCleanupRequestDTO
     */
    // TODO  -  consider insert  every state -  might be pushed to  zipkin, rather then store in DB
    public void processCleanupRequest(MenuCleanupRequestDTO menuCleanupRequestDTO) {

        log.debug("processCleanupRequest: menuCleanupRequestDTO={}", menuCleanupRequestDTO);
        // construct new request entry
        MenuCleanupRequest menuCleanupRequest = new MenuCleanupRequest(menuCleanupRequestDTO, menuCleanupServiceProperties);
        menuCleanupRequestRepository.save(menuCleanupRequest);

        if (menuCleanupRequestDTO != null && menuCleanupRequestDTO.getUserName() != null && menuCleanupRequestDTO.getMessageType() != null) {

            MessageTypeEnum messageType = getMessageTypeEnum(menuCleanupRequest.getMessageType());

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
                        processUserCleanup(menuCleanupRequest);
                        break;
                    }
                }

                updateRequestStatus(menuCleanupRequest, RequestStatusEnum.PROCESSED);
                log.debug("processCleanupRequest: menuCleanupRequest={}", menuCleanupRequest);
            } catch (Exception e) {

                menuCleanupRequest.setRequestError(e.toString());
                updateRequestStatus(menuCleanupRequest, RequestStatusEnum.FAILED);
                log.error("processCleanupRequest exception: menuCleanupRequest={}, {}", menuCleanupRequest, e.toString());

            }
        } else {
            updateRequestStatus(menuCleanupRequest, RequestStatusEnum.INVALID);
        }

    }

    /**
     * @param menuCleanupRequest
     */
    private void updateRequestStatus(MenuCleanupRequest menuCleanupRequest, RequestStatusEnum requestStatusEnum) {
        Instant now;
        menuCleanupRequest.setRequestStatus(requestStatusEnum.getValue());
        now = Instant.now();
        menuCleanupRequest.setLastmodified(Timestamp.from(now));
        menuCleanupRequestRepository.save(menuCleanupRequest);
    }

    public void processUserCleanup(MenuCleanupRequest menuCleanupRequest) {
        log.debug("processUserCleanup: menuCleanupRequest={}", menuCleanupRequest);

        // query existing events for user
        // TODO -  replace Mongo repository with Mongo query  to  delete all  entries by  username
        List<EventMenu> eventMenuList = eventMenuRepository.findAllByUserNameOrderByLastmodifiedDesc(menuCleanupRequest.getUserName());
        for (EventMenu eventMenu: eventMenuList) {
            eventMenuRepository.delete(eventMenu);
        }

    }
}

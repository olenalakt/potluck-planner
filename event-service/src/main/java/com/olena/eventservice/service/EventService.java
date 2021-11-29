package com.olena.eventservice.service;

import com.olena.eventservice.config.EventServiceConfig;
import com.olena.eventservice.exception.ServiceException;
import com.olena.eventservice.model.EventDTO;
import com.olena.eventservice.repository.EventRepository;
import com.olena.eventservice.repository.entity.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventService {

    @Autowired
    EventServiceConfig eventServiceConfig;

    @Autowired
    EventRepository eventRepository;

    /**
     * @param userName
     * @return
     * @throws ServiceException
     */
    public List<Event> getEventListByUserName(String userName) throws ServiceException {
        log.debug("getEventListFromDb: userName={}", userName);
        try {
            List<Event> eventList = eventRepository.findAllByUserNameOrderByEventDate(userName);
            return eventList;
        } catch (Exception e) {
            String errMsg = e.toString();
            log.error("getEventListFromDb: userName={}, {}", userName, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param eventNamePattern
     * @return
     * @throws ServiceException
     */
    public List<Event> getEventListByPattern(String eventNamePattern) throws ServiceException {
        log.debug("getEventListByPattern: userName={}", eventNamePattern);
        try {
            List<Event> eventList = eventRepository.findAllByEventNameContainsOrderByEventDateDesc(eventNamePattern);
            return eventList;
        } catch (Exception e) {
            String errMsg = e.toString();
            log.error("getEventListByPattern: eventNamePattern={}, {}", eventNamePattern, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    public Event getEventFromDb(String eventName) throws ServiceException {
        log.debug("getEventFromDb: userName={}", eventName);

        Event event = eventRepository.findFirstByEventName(eventName);
        if (event == null) {
            String errMsg = "Event not found";
            log.error("getEventFromDb: userName={}, {}", eventName, errMsg);
            throw new ServiceException(errMsg);
        }

        return event;
    }

    /**
     * @param event
     * @return
     * @throws ServiceException
     */
    public void setEvent(Event event) throws ServiceException {
        log.debug("setEvent: event={}", event.toString());
        try {
            eventRepository.save(event);
        } catch (Exception e) {
            String errMsg = "Failed to update DB: " + e;
            log.error("setEvent: event={}, {}", event, errMsg);
            throw new ServiceException(errMsg);
        }

        //TBD: push event  asynchronously  to  the Kafka queue for processing by  other services
        //            inventoryClient.updateInventory(event.getItems());
        //          event.setOrderId(UUID.randomUUID().toString());


    }

    /**
     * @param eventDTO
     * @return
     * @throws ServiceException
     */
    public void addEvent(EventDTO eventDTO) throws ServiceException {
        log.debug("addEvent: eventDTO={}", eventDTO);

        // check if eventDTO  already  exists  -  reject
        if (eventRepository.findFirstByEventName(eventDTO.getEventName()) != null) {
            StringBuffer errMsg = new StringBuffer();
            errMsg.append("Event already exists in DB");
            log.error("addEvent: eventDTO={}, {}", eventDTO, errMsg);
            throw new ServiceException(errMsg.toString());
        }

        try {

            setEvent(new Event(eventDTO, eventServiceConfig));

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            StringBuffer errMsg = new StringBuffer();
            errMsg.append("Failed to map user: ").append(e);
            log.error("addEvent: eventDTO={}, {}", eventDTO, errMsg);
            throw new ServiceException(errMsg.toString());
        }
    }


}

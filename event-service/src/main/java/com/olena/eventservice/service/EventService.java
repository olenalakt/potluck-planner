package com.olena.eventservice.service;

import com.olena.eventservice.config.EventServiceConfig;
import com.olena.eventservice.exception.ServiceException;
import com.olena.eventservice.model.EventDTO;
import com.olena.eventservice.repository.EventRepository;
import com.olena.eventservice.repository.entity.Event;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EventService {

    @Autowired
    EventServiceConfig eventServiceConfig;

    @Autowired
    EventRepository eventRepository;


    public Event getEventFromDb(String eventId) throws ServiceException {
        log.debug("getEventFromDbByName: eventId={}", eventId);

        Event event = eventRepository.findFirstByEventId(UUID.fromString(eventId));
        if (event == null) {
            String errMsg = "Event not found";
            log.error("getEventFromDbByName: eventId={}, {}", eventId, errMsg);
            throw new ServiceException(errMsg);
        }

        return event;
    }

    public Event getEventFromDbByName(String userName, String eventName) throws ServiceException {
        log.debug("getEventFromDbByName: userName={}, eventName={}", userName, eventName);

        Event event = eventRepository.findFirstByUserNameAndEventName(userName, eventName);
        if (event == null) {
            String errMsg = "Event not found";
            log.error("getEventFromDbByName: userName={}, eventName={}, {}", userName, eventName, errMsg);
            throw new ServiceException(errMsg);
        }

        return event;
    }

    /**
     * @param eventDTO
     * @return
     * @throws ServiceException
     */
    public void checkEvent(EventDTO eventDTO) throws ServiceException {
        log.debug("checkEvent: eventDTO={}", eventDTO.toString());

        Event event = getEventFromDb(eventDTO.getEventId());
        eventDTO.setUserName(event.getUserName());

        log.debug("checkEvent done: eventDTO={}", eventDTO);
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

        //TODO: push event  asynchronously  to  the Kafka queue for processing by  other services
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
        if (eventRepository.findFirstByUserNameAndEventName(eventDTO.getUserName(), eventDTO.getEventName()) != null) {
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

    public EventDTO getEvent(String eventId, Boolean includeGuests, GuestService guestService) throws ServiceException {
        log.debug("getEvent: eventId={}", eventId);

        Event event = getEventFromDb(eventId);
        ModelMapper modelMapper = new ModelMapper();
        EventDTO eventDTO = modelMapper.map(event, EventDTO.class);
        log.debug("getEvent: eventDTO={}", eventDTO);

        if (includeGuests) {
            eventDTO.setGuests(guestService.getGuestList(eventDTO));
        }

        return eventDTO;
    }


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
    public List<Event> getEventListByPattern(String userName, String eventNamePattern) throws ServiceException {
        log.debug("getEventListByPattern: userName={}, eventNamePattern={}", userName, eventNamePattern);
        try {
            List<Event> eventList = eventRepository.findAllByUserNameAndEventNameContainsOrderByEventDateDesc(userName,eventNamePattern);
            return eventList;
        } catch (Exception e) {
            String errMsg = e.toString();
            log.error("getEventListByPattern: eventNamePattern={}, {}", eventNamePattern, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    public EventDTO getEventByName(String userName, String eventName, GuestService guestService) throws ServiceException {
        log.debug("getEventListByPattern: userName={}, eventNamePattern={}", userName, eventName);

        Event event = getEventFromDbByName(userName, eventName);
        ModelMapper modelMapper = new ModelMapper();
        EventDTO eventDTO = modelMapper.map(event, EventDTO.class);
        log.debug("getEventByName: eventDTO={}", eventDTO);
        eventDTO.setGuests(guestService.getGuestList(eventDTO));
        return eventDTO;
    }


}

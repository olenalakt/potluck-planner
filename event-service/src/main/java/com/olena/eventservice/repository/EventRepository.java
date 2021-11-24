package com.olena.eventservice.repository;

import com.olena.eventservice.repository.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    List<Event> findAllByUserNameOrderByEventDate(String userName);

    // retrieve all fields
    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    Event findFirstByEventName(String eventName);

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    List<Event> findAllByEventNameContainsOrderByEventDateDesc (String eventNamePattern);

}

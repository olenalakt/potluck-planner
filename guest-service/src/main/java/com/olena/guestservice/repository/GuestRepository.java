package com.olena.guestservice.repository;

import com.olena.guestservice.repository.entity.Guest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GuestRepository extends MongoRepository<Guest, String> {

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    List<Guest> findAllByUserNameAndEventIdOrderByGuestName(String userName, UUID eventId);

    // retrieve all fields
    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    Guest findFirstByUserNameAndEventIdAndGuestEmail(String eventName, UUID eventId, String guestEmail);

}

package com.olena.guestcleanupservice.repository;

import com.olena.guestcleanupservice.repository.entity.Guest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GuestRepository extends MongoRepository<Guest, String> {

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    List<Guest> findAllByUserNameOrderByLastmodifiedDesc(String userName);

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    Guest findFirstByGuestId(UUID guestId);
}

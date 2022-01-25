package com.olena.guestcleanupservice.repository;

import com.olena.guestcleanupservice.repository.entity.GuestCleanupRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GuestCleanupRequestRepository extends MongoRepository<GuestCleanupRequest, String> {

    List<GuestCleanupRequest> findAllByUserNameOrderByLastmodifiedDesc(String userName);

}

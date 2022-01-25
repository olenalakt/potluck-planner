package com.olena.eventcleanupservice.repository;

import com.olena.eventcleanupservice.repository.entity.EventCleanupRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventCleanupRequestRepository extends MongoRepository<EventCleanupRequest, String> {

    List<EventCleanupRequest> findAllByUserNameOrderByLastmodifiedDesc(String userName);

}

package com.olena.drinkcleanupservice.repository;

import com.olena.drinkcleanupservice.repository.entity.DrinkCleanupRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DrinkCleanupRequestRepository extends MongoRepository<DrinkCleanupRequest, String> {

    List<DrinkCleanupRequest> findAllByUserNameOrderByLastmodifiedDesc(String userName);

}

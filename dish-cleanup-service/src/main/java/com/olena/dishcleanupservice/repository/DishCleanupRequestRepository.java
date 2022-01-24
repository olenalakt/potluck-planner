package com.olena.dishcleanupservice.repository;

import com.olena.dishcleanupservice.repository.entity.DishCleanupRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DishCleanupRequestRepository extends MongoRepository<DishCleanupRequest, String> {

    List<DishCleanupRequest> findAllByUserNameOrderByLastmodifiedDesc(String userName);

}

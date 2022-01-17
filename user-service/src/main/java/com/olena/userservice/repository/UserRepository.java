package com.olena.userservice.repository;

import com.olena.userservice.repository.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {

    // retrieve all fields
    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    User findFirstByUserName(String userName);

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    User findFirstByUserId(UUID userId);

}

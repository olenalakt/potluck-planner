package com.olena.oathserver.repository;

import com.olena.oathserver.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    // retrieve  fields
    @Query(fields = "{'userName':1,'password':1,'userRole':1}", collation = "{ 'locale' :  'en_US', strength: 2 }")
    User findFirstByUserName(String userName);

}

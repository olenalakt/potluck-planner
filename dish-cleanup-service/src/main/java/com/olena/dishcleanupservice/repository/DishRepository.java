package com.olena.dishcleanupservice.repository;

import com.olena.dishcleanupservice.repository.entity.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DishRepository extends MongoRepository<Dish, String> {

    Dish findFirstByDishId(UUID dishId);

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    List<Dish> findAllByUserNameOrderByLastmodifiedDesc(String username);

}

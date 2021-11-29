package com.olena.dishservice.repository;

import com.olena.dishservice.repository.entity.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DishRepository extends MongoRepository<Dish, String> {

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    List<Dish> findAllByGuestIdOrderByDishName(UUID guestId);

    // retrieve all fields
    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    Dish findFirstByDishName(UUID guestId, String dishName);

}

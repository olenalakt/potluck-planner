package com.olena.dishcleanupservice.repository;

import com.olena.dishcleanupservice.repository.entity.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface DishRepository extends MongoRepository<Dish, String> {

    Dish findFirstByDishId(UUID dishId);

    List<Dish> findAllByUserNameOrderByLastmodifiedDesc(String username);

}

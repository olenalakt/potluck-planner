package com.olena.drinkcleanupservice.repository;

import com.olena.drinkcleanupservice.repository.entity.Drink;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DrinkRepository extends MongoRepository<Drink, String> {

    Drink findFirstByDrinkId(UUID drinkId);

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    List<Drink> findAllByUserNameOrderByLastmodifiedDesc(String username);


}

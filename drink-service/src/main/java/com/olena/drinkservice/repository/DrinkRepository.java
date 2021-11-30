package com.olena.drinkservice.repository;

import com.olena.drinkservice.repository.entity.Drink;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DrinkRepository extends MongoRepository<Drink, String> {

    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    List<Drink> findAllByGuestIdOrderByDrinkName(UUID guestId);

    // retrieve all fields
    @Query(collation = "{ 'locale' :  'en_US', strength: 2 }")
    Drink findFirstByDrinkName(UUID guestId, String drinkName);

}

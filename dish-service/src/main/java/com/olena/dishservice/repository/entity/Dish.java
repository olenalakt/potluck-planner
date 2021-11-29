package com.olena.dishservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.dishservice.config.DishServiceConfig;
import com.olena.dishservice.model.DishDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Data
@Document(collection = "Dish")
@NoArgsConstructor
public class Dish {

    @Id
    private ObjectId id;

    //TODO -  index on userName, eventName, guestEmail
    @NonNull
    private String userName;

    @NonNull
    private UUID eventId;

    @NonNull
    private String guestId;

    @Indexed
    private UUID dishId;

    @Indexed
    @NonNull
    private String dishName;

    private String dishRecipe;

    @NonNull
    private Boolean isLactoseFree;

    @NonNull
    private Boolean isVegetarian;

    @NonNull
    private Boolean isNutFree;

    @NonNull
    private Boolean hasFish;

    @NonNull
    private Boolean hasMeat;

    @NonNull
    private String schemaVersion;
    @NonNull
    private Timestamp lastmodified;

    public Dish(DishDTO dishDTO, DishServiceConfig dishServiceConfig) {

        this.userName = dishDTO.getUserName();
        this.eventId = UUID.fromString(dishDTO.getEventId());
        this.guestId = dishDTO.getGuestId();
        this.dishId = UUID.fromString(dishDTO.getDishId());
        this.dishName = dishDTO.getDishName();

        this.dishRecipe = dishDTO.getDishRecipe();
        this.isLactoseFree = dishDTO.getIsLactoseFree();
        this.isVegetarian = dishDTO.getIsVegetarian();
        this.isNutFree = dishDTO.getIsNutFree();
        this.hasFish = dishDTO.getHasFish();
        this.hasMeat = dishDTO.getHasMeat();


        this.schemaVersion = dishServiceConfig.getDbSchemaVersion();

        Instant now = Instant.now();
        this.lastmodified = Timestamp.from(now);
    }
}

package com.olena.dishservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.dishservice.config.DishServiceProperties;
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

    @NonNull
    private String userName;

    @NonNull
    private UUID eventId;

    //TODO -  add index guestId, dishName
    @Indexed
    @NonNull
    private UUID guestId;

    @Indexed
    private UUID dishId;

    @NonNull
    private String dishName;

    @NonNull
    private String dishType;

    private String dishRecipe;

    @NonNull
    private Boolean isLactoseFree;

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

    public Dish(DishDTO dishDTO, DishServiceProperties dishServiceProperties) {

        this.userName = dishDTO.getUserName();
        this.eventId = UUID.fromString(dishDTO.getEventId());
        this.guestId = UUID.fromString(dishDTO.getGuestId());
        this.dishId = (dishDTO.getDishId() == null ? UUID.randomUUID() : UUID.fromString(dishDTO.getDishId()));
        this.dishName = dishDTO.getDishName();
        this.dishType = dishDTO.getDishType();

        this.dishRecipe = dishDTO.getDishRecipe();
        this.isLactoseFree = dishDTO.getIsLactoseFree();
        this.isNutFree = dishDTO.getIsNutFree();
        this.hasFish = dishDTO.getHasFish();
        this.hasMeat = dishDTO.getHasMeat();

        this.schemaVersion = dishServiceProperties.getDbSchemaVersion();

        Instant now = Instant.now();
        this.lastmodified = Timestamp.from(now);
    }
}

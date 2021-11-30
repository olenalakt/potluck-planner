package com.olena.drinkservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.drinkservice.config.DrinkServiceConfig;
import com.olena.drinkservice.model.DrinkDTO;
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
@Document(collection = "Drink")
@NoArgsConstructor
public class Drink {

    @Id
    private ObjectId id;

    @NonNull
    private String userName;

    @Indexed
    @NonNull
    private UUID eventId;

    //TODO -  add index guestId, drinkName
    @Indexed
    @NonNull
    private UUID guestId;

    @Indexed
    @NonNull
    private UUID drinkId;

    @NonNull
    private Integer quantityL;

    @NonNull
    private String drinkName;

    @NonNull
    private String drinkType;

    private String drinkRecipe;

    @NonNull
    private Boolean isLactoseFree;

    @NonNull
    private Boolean isAlcoholFree;


    @NonNull
    private String schemaVersion;
    @NonNull
    private Timestamp lastmodified;

    public Drink(DrinkDTO drinkDTO, DrinkServiceConfig drinkServiceConfig) {

        this.userName = drinkDTO.getUserName();
        this.eventId = UUID.fromString(drinkDTO.getEventId());
        this.guestId = UUID.fromString(drinkDTO.getGuestId());
        this.drinkId = (drinkDTO.getDrinkId() == null ? UUID.randomUUID() : UUID.fromString(drinkDTO.getDrinkId()));
        this.quantityL = drinkDTO.getQuantityL();
        this.drinkName = drinkDTO.getDrinkName();
        this.drinkType = drinkDTO.getDrinkType();

        this.drinkRecipe = drinkDTO.getDrinkRecipe();
        this.isLactoseFree = drinkDTO.getIsLactoseFree();
        this.isAlcoholFree = drinkDTO.getIsAlcoholFree();

        this.schemaVersion = drinkServiceConfig.getDbSchemaVersion();

        Instant now = Instant.now();
        this.lastmodified = Timestamp.from(now);
    }
}

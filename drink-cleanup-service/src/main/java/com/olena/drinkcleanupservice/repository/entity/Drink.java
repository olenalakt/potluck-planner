package com.olena.drinkcleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
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
    private Integer quantityMl;

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

}

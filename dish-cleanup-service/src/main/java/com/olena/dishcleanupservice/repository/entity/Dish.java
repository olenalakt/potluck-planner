package com.olena.dishcleanupservice.repository.entity;

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

}

package com.olena.menucleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Drink {

    @NonNull
    private UUID guestId;

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
    private Timestamp lastmodified;
}

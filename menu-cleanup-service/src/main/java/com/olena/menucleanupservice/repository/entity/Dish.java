package com.olena.menucleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.sql.Timestamp;
import java.util.UUID;


@Data
@NoArgsConstructor
public class Dish {


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
    private Timestamp lastmodified;
}

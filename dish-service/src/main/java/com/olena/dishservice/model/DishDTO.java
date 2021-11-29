package com.olena.dishservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {
    @NonNull
    private String userName;

    @NonNull
    private String eventId;

    @NonNull
    private String guestId;

    private String dishId;

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
}

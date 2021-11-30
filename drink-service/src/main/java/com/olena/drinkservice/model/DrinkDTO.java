package com.olena.drinkservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrinkDTO {
    @NonNull
    private String userName;

    @NonNull
    private String eventId;

    @NonNull
    private String guestId;

    private String drinkId;

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

}

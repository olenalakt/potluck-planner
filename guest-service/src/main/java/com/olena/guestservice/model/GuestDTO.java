package com.olena.guestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestDTO {
    @NonNull
    private String userName;

    @NonNull
    private String eventId;

    private String guestId;

    @NonNull
    private String guestName;

    @NonNull
    private String guestEmail;

    private String notes;

    @JsonProperty("dishes")
    private DishDTO[] dishes;

    @JsonProperty("drinks")
    private DrinkDTO[] drinks;

}

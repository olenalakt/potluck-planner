package com.olena.menucleanupservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCleanupRequestDTO {

    private String userId;
    private String userName;

    // possible extention in the future -  delete reactively
    private String eventId;
    private String guestId;
    private String dishId;
    private String drinkId;

    private String messageType;

}

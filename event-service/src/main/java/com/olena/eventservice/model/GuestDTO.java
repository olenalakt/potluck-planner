package com.olena.eventservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestDTO {

    private String userName;
    private String eventId;

    @NonNull
    private String guestName;

    @NonNull
    private String guestEmail;

    private String notes;

}

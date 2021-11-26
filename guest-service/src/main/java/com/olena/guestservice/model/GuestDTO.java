package com.olena.guestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class GuestDTO {
    @NonNull
    private String userName;

    @NonNull
    private UUID eventId;

    @NonNull
    private String guestName;

    @NonNull
    private String guestEmail;

    private String notes;

}

package com.olena.eventservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    @NonNull
    private String userName;

    private String eventId;

    @NonNull
    private String eventName;

    @JsonProperty("guests")
    private GuestDTO[] guests;

    private Timestamp eventDate;

    private String notes;

}

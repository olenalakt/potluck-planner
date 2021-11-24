package com.olena.eventservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.sql.Date;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class EventDTO {
    @NonNull
    private String userName;

    @NonNull
    private String eventName;

    private Timestamp eventDate;

    private String notes;

}

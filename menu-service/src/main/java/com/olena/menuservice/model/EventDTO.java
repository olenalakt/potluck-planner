package com.olena.menuservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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


    private Timestamp eventDate;

    private String notes;

}

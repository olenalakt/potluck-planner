package com.olena.menuservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private ObjectId id;
    private String userName;
    private String eventId;
    private String eventName;
    private Timestamp eventDate;
    private String notes;
    private Timestamp lastmodified;
    private String actionType;

}

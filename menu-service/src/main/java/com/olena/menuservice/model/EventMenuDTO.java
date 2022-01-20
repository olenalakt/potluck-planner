package com.olena.menuservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
/**

 */
public class EventMenuDTO {

    private String userName;
    private String eventId;
    private String eventName;
    private Timestamp eventDate;
    private String notes;

    private Guest guest;
    private Dish dish;
    private Drink drink;

    private String messageType;
    private String actionType;

}

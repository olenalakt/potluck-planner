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
 *  if action type=  "I"
 *      if Guest <> null - add guest
 *      if Dish <> null - add dish
 *      if Drink <> null - add drink
 *      else - add event
 *  if action type=  "U"
 *      if Guest <> null - update guest
 *      if Dish <> null - update dish
 *      if Drink <> null - update drink
 *      else - add event
 *  if action type=  "D"
 *      if Guest <> null - add guest
 *      if Dish <> null - add dish
 *      if Drink <> null - add drink
 *      else - add event
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

    private String actionType;

}

package com.olena.menucleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.menucleanupservice.model.Dish;
import com.olena.menucleanupservice.model.Drink;
import com.olena.menucleanupservice.model.Guest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


@Data
@Document(collection = "EventMenu")
@NoArgsConstructor
public class EventMenu {

    @Id
    private ObjectId id;

    // TODO -  index on userName, eventName
    @Indexed
    @NonNull
    private String userName;

    @NonNull
    private UUID eventId;

    @Indexed
    @NonNull
    private String eventName;

    private Timestamp eventDate;

    private String notes;

    private List<Guest> guests;

    private List<Dish> dishes;

    private List<Drink> drinks;

    @NonNull
    private String schemaVersion;
    @NonNull
    private Timestamp lastmodified;

}

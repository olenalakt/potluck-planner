package com.olena.eventcleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Document(collection = "Event")
@NoArgsConstructor
public class Event {

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

    @NonNull
    private String schemaVersion;
    @NonNull
    private Timestamp lastmodified;

    @Transient
    private String messageType;
    @Transient
    private String actionType;

}

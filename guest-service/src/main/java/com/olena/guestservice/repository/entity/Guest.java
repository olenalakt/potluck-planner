package com.olena.guestservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.guestservice.config.GuestServiceConfig;
import com.olena.guestservice.model.GuestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Data
@Document(collection = "Guest")
@NoArgsConstructor
public class Guest {

    @Id
    private ObjectId id;

    // TBD -  index on userName, eventName, guestEmail
    @Indexed
    @NonNull
    private String userName;

    @Indexed
    @NonNull
    private UUID eventId;

    private String guestName;

    private String guestEmail;

    private String notes;

    @NonNull
    private String schemaVersion;
    @NonNull
    private Timestamp lastmodified;

    public Guest(GuestDTO guestDTO, GuestServiceConfig guestServiceConfig) {
        this.userName = guestDTO.getUserName();
        this.eventId = guestDTO.getEventId();
        this.guestName = guestDTO.getGuestName();
        this.guestEmail = guestDTO.getGuestEmail();
        this.notes = guestDTO.getNotes();

        this.schemaVersion = guestServiceConfig.getDbSchemaVersion();

        Instant now = Instant.now();
        this.lastmodified = Timestamp.from(now);
    }
}

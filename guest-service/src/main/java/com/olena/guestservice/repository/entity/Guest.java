package com.olena.guestservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.guestservice.config.GuestServiceProperties;
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

    //TODO - unique case insensitive index on userName, eventId, guestEmail
    @Indexed
    @NonNull
    private String userName;

    @Indexed
    @NonNull
    private UUID eventId;

    @NonNull
    private UUID guestId;

    private String guestName;
    private String guestStatus;

    private String guestEmail;

    private String notes;

    @NonNull
    private String schemaVersion;

    public Guest(GuestDTO guestDTO, GuestServiceProperties guestServiceProperties) {
        this.guestId = (guestDTO.getGuestId() == null ? UUID.randomUUID() : UUID.fromString(guestDTO.getGuestId()));
        this.userName = guestDTO.getUserName();
        this.eventId = UUID.fromString(guestDTO.getEventId());
        this.guestName = guestDTO.getGuestName();
        // TODO  - introduce enum for guest status
        this.guestStatus = (guestDTO.getGuestStatus() == null ? "INVITED" : guestDTO.getGuestStatus());
        this.guestEmail = guestDTO.getGuestEmail();
        this.notes = guestDTO.getNotes();

        this.schemaVersion = guestServiceProperties.getDbSchemaVersion();

        Instant now = Instant.now();
        this.lastmodified = Timestamp.from(now);
    }
}

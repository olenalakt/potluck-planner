package com.olena.guestcleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
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
    @NonNull
    private Timestamp lastmodified;

}

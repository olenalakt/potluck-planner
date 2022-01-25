package com.olena.guestcleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.guestcleanupservice.config.GuestCleanupServiceProperties;
import com.olena.guestcleanupservice.enums.RequestStatusEnum;
import com.olena.guestcleanupservice.model.GuestCleanupRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Data
@Document(collection = "GuestCleanupRequest")
@NoArgsConstructor
public class GuestCleanupRequest {

    @Id
    private UUID id;

    @Indexed
    @NonNull
    private String userName;

    // possible extention in the future -  delete reactively
    private UUID eventId;
    private UUID guestId;

    @NonNull
    private String messageType;

    @NonNull
    private String requestStatus;
    private String requestError;

    @NonNull
    private String schemaVersion;
    @NonNull
    private Timestamp created;
    @NonNull
    private Timestamp lastmodified;

    public GuestCleanupRequest(GuestCleanupRequestDTO guestCleanupRequestDTO, GuestCleanupServiceProperties guestCleanupServiceProperties) {

        this.id = UUID.randomUUID();

        this.userName = guestCleanupRequestDTO.getUserName();

        this.messageType = guestCleanupRequestDTO.getMessageType();

        this.requestStatus = RequestStatusEnum.PENDING.getValue();

        this.schemaVersion = guestCleanupServiceProperties.getDbSchemaVersion();

        Instant now = Instant.now();
        this.created = Timestamp.from(now);
        this.lastmodified = Timestamp.from(now);

    }
}

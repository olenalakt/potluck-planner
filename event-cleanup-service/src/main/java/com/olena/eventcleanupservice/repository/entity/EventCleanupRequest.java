package com.olena.eventcleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.eventcleanupservice.config.EventCleanupServiceProperties;
import com.olena.eventcleanupservice.enums.RequestStatusEnum;
import com.olena.eventcleanupservice.model.EventCleanupRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Data
@Document(collection = "EventCleanupRequest")
@NoArgsConstructor
public class EventCleanupRequest {

    @Id
    private UUID id;

    @Indexed
    @NonNull
    private String userName;

    // possible extention in the future -  delete reactively
    private UUID eventId;
    private UUID guestId;
    private UUID dishId;
    private UUID drinkId;

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

    public EventCleanupRequest(EventCleanupRequestDTO eventCleanupRequestDTO, EventCleanupServiceProperties eventCleanupServiceProperties) {

        this.id = UUID.randomUUID();

        this.userName = eventCleanupRequestDTO.getUserName();

        this.messageType = eventCleanupRequestDTO.getMessageType();

        this.requestStatus = RequestStatusEnum.PENDING.getValue();

        this.schemaVersion = eventCleanupServiceProperties.getDbSchemaVersion();

        Instant now = Instant.now();
        this.created = Timestamp.from(now);
        this.lastmodified = Timestamp.from(now);

    }
}

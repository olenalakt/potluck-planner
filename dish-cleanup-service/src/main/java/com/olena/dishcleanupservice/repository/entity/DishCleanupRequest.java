package com.olena.dishcleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.dishcleanupservice.config.DishCleanupServiceProperties;
import com.olena.dishcleanupservice.enums.RequestStatusEnum;
import com.olena.dishcleanupservice.model.DishCleanupRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Data
@Document(collection = "DishCleanupRequest")
@NoArgsConstructor
public class DishCleanupRequest {

    @Id
    private UUID id;

    @Indexed
    @NonNull
    private String userName;

    // possible extention in the future -  delete reactively
    private UUID eventId;
    private UUID guestId;
    private UUID dishId;

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

    public DishCleanupRequest(DishCleanupRequestDTO dishCleanupRequestDTO, DishCleanupServiceProperties dishCleanupServiceProperties) {

        this.id = UUID.randomUUID();

        this.userName = dishCleanupRequestDTO.getUserName();

        this.messageType = dishCleanupRequestDTO.getMessageType();

        this.requestStatus = RequestStatusEnum.PENDING.getValue();

        this.schemaVersion = dishCleanupServiceProperties.getDbSchemaVersion();

        Instant now = Instant.now();
        this.created = Timestamp.from(now);
        this.lastmodified = Timestamp.from(now);

    }
}

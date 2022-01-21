package com.olena.menucleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.menucleanupservice.config.MenuCleanupServiceProperties;
import com.olena.menucleanupservice.enums.RequestStatusEnum;
import com.olena.menucleanupservice.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Data
@Document(collection = "MenuCleanupRequest")
@NoArgsConstructor
public class MenuCleanupRequest {

    @Id
    private UUID id;

    @Indexed
    @NonNull
    private String userName;

    @NonNull
    private UUID userId;

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

    public MenuCleanupRequest(MenuCleanupRequestDTO menuCleanupRequestDTO, MenuCleanupServiceProperties menuCleanupServiceProperties) {

        this.id = UUID.randomUUID();

        this.userId = UUID.fromString(menuCleanupRequestDTO.getUserId());
        this.userName = menuCleanupRequestDTO.getUserName();

        this.messageType = menuCleanupRequestDTO.getMessageType();

        this.requestStatus = RequestStatusEnum.PENDING.getValue();

        this.schemaVersion = menuCleanupServiceProperties.getDbSchemaVersion();

        Instant now = Instant.now();
        this.created = Timestamp.from(now);
        this.lastmodified = Timestamp.from(now);

    }
}

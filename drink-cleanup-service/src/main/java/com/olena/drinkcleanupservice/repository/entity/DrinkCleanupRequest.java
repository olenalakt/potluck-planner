package com.olena.drinkcleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.drinkcleanupservice.config.DrinkCleanupServiceProperties;
import com.olena.drinkcleanupservice.enums.RequestStatusEnum;
import com.olena.drinkcleanupservice.model.DrinkCleanupRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Data
@Document(collection = "DrinkCleanupRequest")
@NoArgsConstructor
public class DrinkCleanupRequest {

    @Id
    private UUID id;

    @Indexed
    @NonNull
    private String userName;

    // possible extention in the future -  delete reactively
    private UUID eventId;
    private UUID guestId;
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

    public DrinkCleanupRequest(DrinkCleanupRequestDTO drinkCleanupRequestDTO, DrinkCleanupServiceProperties drinkCleanupServiceProperties) {

        this.id = UUID.randomUUID();

        this.userName = drinkCleanupRequestDTO.getUserName();

        this.messageType = drinkCleanupRequestDTO.getMessageType();

        this.requestStatus = RequestStatusEnum.PENDING.getValue();

        this.schemaVersion = drinkCleanupServiceProperties.getDbSchemaVersion();

        Instant now = Instant.now();
        this.created = Timestamp.from(now);
        this.lastmodified = Timestamp.from(now);

    }
}

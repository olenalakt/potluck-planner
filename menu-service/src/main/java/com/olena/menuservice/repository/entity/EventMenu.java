package com.olena.menuservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.menuservice.config.MenuServiceProperties;
import com.olena.menuservice.enums.ActionEnum;
import com.olena.menuservice.model.EventDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;
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

    @NonNull
    private String schemaVersion;
    @NonNull
    private Timestamp lastmodified;

    @Transient
    private String actionType;

    public EventMenu(EventDTO eventDTO, ActionEnum actionEnum, MenuServiceProperties menuServiceProperties) {
        this.eventId = UUID.fromString(eventDTO.getEventId());
        this.userName = eventDTO.getUserName();
        this.eventName = eventDTO.getEventName();
        this.eventDate = eventDTO.getEventDate();
        this.notes = eventDTO.getNotes();

        this.schemaVersion = menuServiceProperties.getDbSchemaVersion();

        Instant now = Instant.now();
        this.lastmodified = Timestamp.from(now);

        actionType = actionEnum.getCode();
    }
}

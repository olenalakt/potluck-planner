package com.olena.guestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.olena.guestservice.config.GuestServiceProperties;
import com.olena.guestservice.enums.ActionEnum;
import com.olena.guestservice.enums.Constants;
import com.olena.guestservice.repository.entity.Guest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GuestMessage {

    private String userName;
    private String eventId;

    private Guest guest;

    private String messageType;
    private String actionType;

    public GuestMessage(Guest guest, ActionEnum action) {

        this.userName = guest.getUserName();
        this.eventId = guest.getEventId().toString();

        this.guest = guest;

        this.messageType = Constants.GUEST.getValue();
        this.actionType = action.getCode();

    }
}

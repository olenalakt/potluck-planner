package com.olena.menucleanupservice.repository.entity;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;


@Data
@NoArgsConstructor
public class Guest {

    @NonNull
    private UUID guestId;

    private String guestName;
    private String guestStatus;

    private String guestEmail;

    private String notes;

    @NonNull
    private Timestamp lastmodified;
}

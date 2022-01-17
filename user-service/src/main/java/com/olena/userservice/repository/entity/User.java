package com.olena.userservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.userservice.config.UserServiceProperties;
import com.olena.userservice.enums.ActionEnum;
import com.olena.userservice.model.UserDTO;
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
@Document(collection = "User")
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;

    @Indexed
    @NonNull
    private String userName;

    @NonNull
    private UUID userId;

    @NonNull
    private String password;
    @NonNull
    private String userRole;

    @NonNull
    private String schemaVersion;
    @NonNull
    private Timestamp lastmodified;

    @Transient
    private String actionType;

    public User(UserDTO userDTO, ActionEnum actionType, UserServiceProperties userServiceProperties) {

        this.userId = UUID.fromString(userDTO.getUserId());
        this.userName = userDTO.getUserName();
        this.password = userDTO.getPassword();
        this.userRole = userDTO.getUserRole();

        this.schemaVersion = userServiceProperties.getDbSchemaVersion();

        Instant now = Instant.now();
        this.lastmodified = Timestamp.from(now);

        this.actionType = actionType.getCode();
    }
}

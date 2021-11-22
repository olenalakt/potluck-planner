package com.olena.userservice.repository.entity;

import com.mongodb.lang.NonNull;
import com.olena.userservice.config.UserServiceConfig;
import com.olena.userservice.model.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;


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
    private String password;
    @NonNull
    private String userRole;

    @NonNull
    private String schemaVersion;
    @NonNull
    private Timestamp lastmodified;

    public User(UserDTO userDTO, UserServiceConfig userServiceConfig) {
        this.userName = userDTO.getUserName();
        this.password = userDTO.getPassword();
        this.userRole = userDTO.getUserRole();

        this.schemaVersion = userServiceConfig.getDbSchemaVersion();

        Instant now = Instant.now();
        this.lastmodified = Timestamp.from(now);
    }
}

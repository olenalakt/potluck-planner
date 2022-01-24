package com.olena.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.olena.userservice.config.UserServiceProperties;
import com.olena.userservice.enums.ActionEnum;
import com.olena.userservice.enums.Constants;
import com.olena.userservice.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class PotluckPlannerCleanup {

    private String userName;

    private String messageType;

    public PotluckPlannerCleanup(User user) {

        this.userName = user.getUserName();

        this.messageType = Constants.USER.getValue();
    }

    public PotluckPlannerCleanup(String userName) {

        this.userName = userName;

        this.messageType = Constants.USER.getValue();
    }
}

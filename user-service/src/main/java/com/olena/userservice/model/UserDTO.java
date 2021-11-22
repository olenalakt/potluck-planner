package com.olena.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.olena.userservice.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class UserDTO {
    @NonNull
    private String userName;

    private String password;
    @NonNull
    private String userRole;

    public UserDTO(User user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.userRole = user.getUserRole();
    }
}

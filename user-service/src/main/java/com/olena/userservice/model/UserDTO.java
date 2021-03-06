package com.olena.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.olena.userservice.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NonNull
    private String userName;

    private String userId;

    private String password;
    @NonNull
    private String userRole;

}

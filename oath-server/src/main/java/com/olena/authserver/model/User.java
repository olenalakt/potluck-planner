package com.olena.authserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class User {
    @NonNull
    private String userName;
    @NonNull
    private String userRole;
    @NonNull
    private String userEmail;

    private String password;

}

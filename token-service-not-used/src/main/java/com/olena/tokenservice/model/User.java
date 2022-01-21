package com.olena.tokenservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Document(collection = "User")
@NoArgsConstructor
public class User {
    @NonNull
    private String userName;
    @NonNull
    private String userRole;
    @NonNull
    private String userEmail;

    private String password;

}

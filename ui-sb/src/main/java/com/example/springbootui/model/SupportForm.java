package com.example.springbootui.model;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SupportForm {

    @NotNull
    @Min(value=10000, message = "Customer Id has to be >= 10000")
    private Long id;

    @NotNull
    @Size(min=5, max=50)
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    private String gender;

    @NotNull
    private String membership;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9 ]{3,255}", message ="Please, enter only letters and numbers")
    private String content;

}

package com.dyongs.demo.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequest {

    @NotBlank
    @Size(min=2, max=20)
    private String name;

    @Email
    @NotBlank
    private String email;
}
